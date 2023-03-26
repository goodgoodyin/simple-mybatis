package com.goodyin.mybatis.datasource.pooled;

import com.goodyin.mybatis.datasource.unpooled.UnPooledDataSource;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Logger;

/**
 * 有连接池的数据源
 */
public class PooledDataSource implements DataSource {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(PooledDataSource.class);

    // 池状态
    private final PoolState state = new PoolState(this);

    private final UnPooledDataSource dataSource;

    // 活跃连接数
    protected int poolMaximumActiveConnections = 10;
    // 空闲连接数
    protected int poolMaximumIdleConnections = 5;

    // 在被强制返回之前，池中链接被检查的时间
    protected int poolMaximumCheckoutTime = 20000;

    // 这是给连接池一个打印日志状态机会的低层次设置,还有重新尝试获得连接, 这些情况下往往需要很长时间 为了避免连接池没有配置时静默失败)。
    protected int poolTimeToWait = 20000;

    //
    protected String poolPingQuery = "NO PING QUERY SET";

    // 开启或禁用侦测查询
    protected boolean poolPingEnabled = false;

    // 配置poolPingQuery多久时间使用一次
    protected int poolPingConnectionsNotUsedFor = 0;

    private int expectedConnectionTypeCode;

    public PooledDataSource() {
        this.dataSource = new UnPooledDataSource();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return popConnection(dataSource.getUsername(), dataSource.getPassword()).getProxyConnection();

    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return popConnection(username, password).getProxyConnection();
    }
    protected void finalize() throws Throwable {
        forceCloseAll();
        super.finalize();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(getClass().getName() + " is not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    /**
     * 回收连接
     * @param connection
     */
    protected void pushConnection(PooledConnection connection) throws SQLException {
        synchronized (state) {
            // 在活跃连接里去掉该连接
            state.activeConnections.remove(connection);
            // 判断链接是否有效
            if (connection.isValid()) {
                // 如果空闲连接太少，把该连接回收到空闲列表
                if (state.idleConnections.size() < poolMaximumIdleConnections
                        && connection.getConnectionTypeCode() == expectedConnectionTypeCode) {
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (!connection.getRealConnection().getAutoCommit()) {
                        connection.getRealConnection().rollback();;
                    }
                    // 实例化一个新的DB连接，加入到idle列表
                    PooledConnection newPooledConnection = new PooledConnection(connection.getRealConnection(), this);
                    state.idleConnections.add(newPooledConnection);
                    newPooledConnection.setCreatedTimestamp(connection.getCreatedTimestamp());
                    newPooledConnection.setLastUsedTimestamp(connection.getLastUsedTimestamp());
                    connection.invalidate();

                    // 其他正在等待DB到线程可以来获取连接了
                    state.notifyAll();
                } else {
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (!connection.getRealConnection().getAutoCommit()) {
                        connection.getRealConnection().rollback();
                    }
                    // 关闭这个连接
                    connection.getRealConnection().close();
                    connection.invalidate();
                }

            } else {
                state.badConnectionCount ++;
            }
        }
    }

    /***
     * 获取连接
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    private PooledConnection popConnection(String username, String password) throws SQLException {
        boolean countWait = false;
        PooledConnection conn = null;
        long t = System.currentTimeMillis();
        int localBadConnectionCount = 0;

        while (conn == null) {
            synchronized (state) {
                // 如果有空闲链接，返回第一个空闲连接
                if (!state.idleConnections.isEmpty()) {
                    conn = state.idleConnections.remove(0);
                    logger.info("Check out connection {} from pool." +  conn.getRealHashCode());
                } else { // 创建新链接
                    // 当前活跃数不足，就需要创建新连接
                    if (state.activeConnections.size() < poolMaximumActiveConnections) {
                        conn = new PooledConnection(dataSource.getConnection(), this);
                        logger.info("Created connection {} ." , conn.getRealHashCode());
                    } else {
                        // 取得活跃链接列表的第一个，也就是最老的一个链接
                        PooledConnection oldestActiveConnection = state.activeConnections.get(0);
                        long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
                        // 如果checkout时间过长，则这个链接标记为过期
                        if (longestCheckoutTime > poolMaximumCheckoutTime) {
                            state.claimedOverdueConnectionCount++;
                            state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                            state.accumulatedCheckoutTime += longestCheckoutTime;
                            state.activeConnections.remove(oldestActiveConnection);

                            // 回滚
                            if (!oldestActiveConnection.getRealConnection().getAutoCommit()) {
                                oldestActiveConnection.getRealConnection().rollback();;
                            }

                            // 删掉最老的链接，然后重新实例化一个新的链接
                            conn = new PooledConnection(oldestActiveConnection.getRealConnection(), this);
                            oldestActiveConnection.invalidate();
                            logger.info("Claimed overdue connection {} ." + conn.getRealHashCode());
                        } else {
                            // 如果checkout超时时间不够长，则等待
                            if (!countWait) {
                                state.hadToWaitCount ++;
                                countWait = true;
                            }
                            logger.info("Waiting as long as {} milliseconds for connection.", poolTimeToWait);
                            long wt = System.currentTimeMillis();
                            try {
                                state.wait(poolTimeToWait);
                                state.accumulatedCheckoutTime += System.currentTimeMillis() - wt;
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                }
                // 获得到新链接
                if (conn != null) {
                    if (conn.isValid()) {
                        if (!conn.getRealConnection().getAutoCommit()) {
                            conn.getRealConnection().rollback();
                        }
                        conn.setConnectionTypeCode(assembleConnectionTypeCode(dataSource.getUrl(), username, password));
                        // 记录checkout时间
                        conn.setCheckoutTimestamp(System.currentTimeMillis());
                        conn.setLastUsedTimestamp(System.currentTimeMillis());
                        state.activeConnections.add(conn);
                        state.requestCount++;
                        state.accumulatedRequestTime += System.currentTimeMillis() - t;
                    } else {
                        logger.info("A bad connection ({})  was returned from" + conn.getRealHashCode());
                        // 如果没拿到，统计信息：失败链接+1
                        state.badConnectionCount++;
                        localBadConnectionCount++;
                        // 失败次数超过阈值，直接跑异常
                        if (localBadConnectionCount > (poolMaximumActiveConnections + 3)) {
                            logger.debug("PooledDataSource: Could not get a good connection to the database.");
                            throw new SQLException("PooledDataSource: Could not get a good connection to the database.");
                        }
                    }
                }

            }
        }
        if (conn == null) {
            logger.debug("PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
            throw new SQLException("PooledDataSource: Unknown severe error condition.  The connection pool returned a null connection.");
        }
        return conn;
    }

    private int assembleConnectionTypeCode(String url, String username, String password) {
        return ("" + url + username + password).hashCode();
    }

    public void setDriver(String driver) {
        dataSource.setDriver(driver);
        forceCloseAll();
    }

    public void setUrl(String url) {
        dataSource.setUrl(url);
        forceCloseAll();
    }

    public void setUsername(String username) {
        dataSource.setUsername(username);
        forceCloseAll();
    }

    public void setPassword(String password) {
        dataSource.setPassword(password);
        forceCloseAll();
    }

    public void forceCloseAll() {
        synchronized (state) {
            expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
            // 关闭活跃连接
            for (int i = state.activeConnections.size(); i > 0; i --) {
                try {
                    PooledConnection conn = state.activeConnections.remove(i - 1);
                    conn.invalidate();

                    Connection realConnection = conn.getRealConnection();
                    if(!realConnection.getAutoCommit()) {
                        realConnection.rollback();
                    }
                    realConnection.close();
                } catch (Exception ignore) {

                }
            }
            // 关闭空闲连接
            for (int i = state.idleConnections.size(); i > 0; i --) {
                try {
                    PooledConnection realConn = state.idleConnections.get(i);
                    realConn.invalidate();

                    Connection realConnection = realConn.getRealConnection();
                    if (!realConnection.getAutoCommit()) {
                        realConnection.rollback();
                    }
                } catch (Exception ignore) {

                }
            }
            logger.info("PooledDataSource forcefully closed/removed all connections.");
        }
    }

    protected boolean pingConnection(PooledConnection conn) {
        boolean result = true;
        try {
            result = !conn.getRealConnection().isClosed();
        } catch (SQLException e) {
            logger.info("Connection {} is BAD: {}", conn.getRealHashCode(), e.getMessage());
            result = false;
        }

        if (result) {
            if (poolPingEnabled) {
                if (poolPingConnectionsNotUsedFor >= 0
                        && conn.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedFor) {
                    try {
                        logger.info("Testing connection {} ...", conn.getRealConnection());
                        Connection realConn = conn.getRealConnection();
                        Statement statement = realConn.createStatement();
                        ResultSet resultSet = statement.executeQuery(poolPingQuery);
                        resultSet.close();
                        if (!realConn.getAutoCommit()) {
                            realConn.rollback();
                        }
                        result = true;
                        logger.info("Connection {} is GOOD!", poolPingQuery);
                    } catch (Exception e) {
                        logger.info("Execution of ping query '{}' failed:{}", poolPingQuery, e.getMessage());
                        try {
                            conn.getRealConnection().close();
                        } catch (SQLException ignore) {

                        }
                        result = false;
                        logger.info("Connection {} is BAD: {}", conn.getRealConnection(), e.getMessage());

                    }
                }
            }
        }
        return result;
    }
}
