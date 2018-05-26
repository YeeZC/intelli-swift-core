package com.fr.swift.config.transaction;

import com.fr.swift.config.ConfigType;
import com.fr.swift.config.dao.SwiftMetaDataDAO;

import java.sql.SQLException;

/**
 * @author yee
 * @date 2018/5/25
 */
public abstract class SwiftMetaDataTransactionWorker implements TransactionWorker<SwiftMetaDataDAO> {

    @Override
    public ConfigType type() {
        return ConfigType.META;
    }

    @Override
    public abstract Object work(SwiftMetaDataDAO dao) throws SQLException;

    @Override
    public boolean needTransaction() {
        return true;
    }
}
