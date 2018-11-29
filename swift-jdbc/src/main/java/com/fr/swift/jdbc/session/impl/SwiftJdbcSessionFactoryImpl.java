package com.fr.swift.jdbc.session.impl;

import com.fr.swift.db.SwiftDatabase;
import com.fr.swift.jdbc.mode.Mode;
import com.fr.swift.jdbc.proxy.invoke.JdbcCaller;
import com.fr.swift.jdbc.rpc.holder.JdbcAddressHolder;
import com.fr.swift.jdbc.session.SwiftJdbcSession;
import com.fr.swift.jdbc.session.SwiftJdbcSessionFactory;

/**
 * @author yee
 * @date 2018/8/27
 */
public class SwiftJdbcSessionFactoryImpl implements SwiftJdbcSessionFactory {

    protected JdbcAddressHolder holder;
    private SwiftDatabase schema;
    private static final String DEFAULT_ADDRESS = "localhost:7000";
    private Mode mode;

    public SwiftJdbcSessionFactoryImpl(SwiftDatabase schema, String host, int port) {
        holder = JdbcAddressHolder.getHolder(host, port, Mode.SERVER);
        this.schema = schema;
        mode = Mode.SERVER;
    }

    public SwiftJdbcSessionFactoryImpl(SwiftDatabase schema) {
        this.schema = schema;
        holder = JdbcAddressHolder.getHolder(DEFAULT_ADDRESS, Mode.EMB);
        mode = Mode.EMB;
    }

    @Override
    public SwiftJdbcSession openSession() {
        return new SwiftJdbcSessionImpl(schema, JdbcCaller.connectSelectService(holder.nextAnalyseAddress(), mode), JdbcCaller.connectMaintenanceService(holder.nextRealTimeAddress(), mode));
    }

    @Override
    public void close() {
//        proxyPool.close();
    }
}