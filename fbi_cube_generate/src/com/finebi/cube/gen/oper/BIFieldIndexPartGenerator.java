package com.finebi.cube.gen.oper;

import com.finebi.cube.structure.Cube;
import com.finebi.cube.structure.column.BIColumnKey;
import com.fr.bi.manager.PerformancePlugManager;
import com.fr.bi.stable.data.db.ICubeFieldSource;
import com.fr.bi.stable.data.source.CubeTableSource;

/**
 * Created by 小灰灰 on 2017/5/10.
 */
public class BIFieldIndexPartGenerator<T> extends BIFieldIndexGenerator<T>{
    public BIFieldIndexPartGenerator(Cube cube, CubeTableSource tableSource, ICubeFieldSource hostBICubeFieldSource, BIColumnKey targetColumnKey) {
        super(cube, tableSource, hostBICubeFieldSource, targetColumnKey);
    }

    protected void buildFieldIndex() {
        if (PerformancePlugManager.getInstance().isDiskSort()) {
            buildTableIndexExternal();
        } else {
            buildTableIndex();
        }
    }
}
