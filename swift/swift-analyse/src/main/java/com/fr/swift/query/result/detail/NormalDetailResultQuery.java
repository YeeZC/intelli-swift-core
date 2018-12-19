package com.fr.swift.query.result.detail;

import com.fr.swift.query.info.element.target.DetailTarget;
import com.fr.swift.query.query.Query;
import com.fr.swift.result.qrs.QueryResultSet;
import com.fr.swift.result.qrs.QueryResultSetMerger;

import java.util.List;

/**
 *
 * @author pony
 * @date 2017/11/27
 */
public class NormalDetailResultQuery extends AbstractDetailResultQuery {

    public NormalDetailResultQuery(int fetchSize, List<Query<QueryResultSet>> queries) {
        super(fetchSize, queries);
    }

    public NormalDetailResultQuery(int fetchSize, List<Query<QueryResultSet>> queries, List<DetailTarget> targets) {
        super(fetchSize, queries, targets);
    }

    /**
     * TODO 接口调整了，这边暂时改不动，等lyon调整后再调整
     *
     * @return
     */
    @Override
    protected QueryResultSetMerger createMerger() {
        return null; //QueryResultSetUtils.createMerger(QueryType.DETAIL);
    }


}
