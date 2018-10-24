package com.fr.swift.generate.history.transport;

import com.fr.swift.cube.io.ResourceDiscovery;
import com.fr.swift.cube.queue.CubeTasks;
import com.fr.swift.generate.Transporter;
import com.fr.swift.log.SwiftLogger;
import com.fr.swift.log.SwiftLoggers;
import com.fr.swift.segment.insert.HistoryBlockInserter;
import com.fr.swift.segment.operator.Inserter;
import com.fr.swift.source.DataSource;
import com.fr.swift.source.SwiftResultSet;
import com.fr.swift.source.SwiftSourceTransfer;
import com.fr.swift.source.SwiftSourceTransferFactory;
import com.fr.swift.source.resultset.progress.ProgressResultSet;
import com.fr.swift.task.TaskResult.Type;
import com.fr.swift.task.impl.BaseWorker;
import com.fr.swift.task.impl.TaskResultImpl;

import java.util.List;

/**
 * This class created on 2017-12-25 13:55:12
 *
 * @author Lucifer
 * @description
 * @since Advanced FineBI Analysis 1.0
 */
public class TableTransporter extends BaseWorker implements Transporter {
    private static final SwiftLogger LOGGER = SwiftLoggers.getLogger(TableTransporter.class);

    private DataSource dataSource;

    private Inserter inserter;

    public TableTransporter(DataSource dataSource) {
        this.dataSource = dataSource;
        inserter = new HistoryBlockInserter(dataSource);
    }

    @Override
    public void work() {
        try {
            transport();
            workOver(new TaskResultImpl(Type.SUCCEEDED));
        } catch (Exception e) {
            LOGGER.error("Datasource:" + dataSource.getSourceKey().getId() + " transport failed", e);
            workOver(new TaskResultImpl(Type.FAILED, e));
        }
    }

    @Override
    public void transport() throws Exception {
        SwiftSourceTransfer transfer = SwiftSourceTransferFactory.createSourceTransfer(dataSource);
        SwiftResultSet resultSet = new ProgressResultSet(transfer.createResultSet(), CubeTasks.newTableName(dataSource));
        try {
            inserter.insertData(resultSet);
        } finally {
            resultSet.close();
        }

        ResourceDiscovery.getInstance().setLastUpdateTime(dataSource.getSourceKey(), System.currentTimeMillis());
    }

    @Override
    public List<String> getIndexFieldsList() {
        return inserter.getFields();
    }
}
