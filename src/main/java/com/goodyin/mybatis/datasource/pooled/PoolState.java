package com.goodyin.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;

/**
 * 池状态 （享元模式）
 */
public class PoolState {

    protected PooledDataSource dataSource;

    // 空闲链接
    protected final List<PooledConnection> idleConnections = new ArrayList<>();

    // 活跃链接
    protected final List<PooledConnection> activeConnections = new ArrayList<>();

    // 请求次数
    protected long requestCount = 0;

    // 总请求时间
    protected long accumulatedRequestTime = 0;
    protected long accumulatedCheckoutTime = 0;
    protected long claimedOverdueConnectionCount = 0;
    protected long accumulatedCheckoutTimeOfOverdueConnections = 0;

    // 要等待的次数
    protected long hadToWaitCount = 0;

    // 失败连接次数
    protected long badConnectionCount = 0;

    public PoolState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
