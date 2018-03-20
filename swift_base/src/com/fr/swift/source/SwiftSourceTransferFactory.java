package com.fr.swift.source;

import com.fr.swift.source.db.ConnectionManager;
import com.fr.swift.source.db.QueryDBSource;
import com.fr.swift.source.db.QuerySourceTransfer;
import com.fr.swift.source.db.ServerDBSource;
import com.fr.swift.source.db.TableDBSource;
import com.fr.swift.source.db.TableDBSourceTransfer;
import com.fr.swift.source.etl.ETLOperator;
import com.fr.swift.source.etl.ETLSource;
import com.fr.swift.source.etl.EtlTransferFactory;
import com.fr.swift.source.excel.ExcelDataSource;
import com.fr.swift.source.excel.ExcelTransfer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pony
 * @date 2017/11/22
 */
public class SwiftSourceTransferFactory {
    public static SwiftSourceTransfer createSourceTransfer(DataSource dataSource) {
        SwiftSourceTransfer transfer = null;
        if (dataSource instanceof TableDBSource) {
            transfer = new TableDBSourceTransfer(ConnectionManager.getInstance().getConnectionInfo(((TableDBSource) dataSource).getConnectionName())
                    , dataSource.getMetadata(), ((TableDBSource) dataSource).getOuterMetadata(), ((TableDBSource) dataSource).getDBTableName());
        } else if (dataSource instanceof QueryDBSource) {
            transfer = new QuerySourceTransfer(ConnectionManager.getInstance().getConnectionInfo(((QueryDBSource) dataSource).getConnectionName())
                    , dataSource.getMetadata(), ((QueryDBSource) dataSource).getOuterMetadata(), ((QueryDBSource) dataSource).getQuery());
        } else if (dataSource instanceof ServerDBSource) {

        } else if (dataSource instanceof ExcelDataSource) {
            transfer = new ExcelTransfer(((ExcelDataSource) dataSource).getAllPaths(), dataSource.getMetadata(), ((ExcelDataSource) dataSource).getOuterMetadata());
        } else if (dataSource instanceof ETLSource) {
            transfer = EtlTransferFactory.createTransfer((ETLSource) dataSource);
        }
        return transfer;
    }

    private static SwiftMetaData getOrCreateETLTable(SwiftMetaData metaData, ETLSource source) {
        if (metaData == null) {
            ETLOperator operator = source.getOperator();
            List<DataSource> parentSource = source.getBasedSources();
            List<SwiftMetaData> list = new ArrayList<SwiftMetaData>();
            for (DataSource etlSource : parentSource) {
                SwiftMetaData parentMetaData = getOrCreateETLTable(metaData, (ETLSource) etlSource);
                if (parentMetaData == null) {
                    throw new RuntimeException();
                }
                list.add(parentMetaData);
            }
            List<SwiftMetaDataColumn> columnList = operator.getColumns(list.toArray(new SwiftMetaData[list.size()]));
            return new SwiftMetaDataImpl(source.getSourceKey().getId(), columnList);
        }
        return metaData;
    }
}
