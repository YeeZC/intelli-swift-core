package com.fr.bi.web.conf.services.cubetask;

import com.finebi.cube.conf.BICubeConfiguration;
import com.finebi.cube.conf.BICubeManagerProvider;
import com.finebi.cube.conf.CubeBuild;
import com.finebi.cube.conf.CubeGenerationManager;
import com.finebi.cube.conf.table.BIBusinessTable;
import com.finebi.cube.data.ICubeResourceDiscovery;
import com.finebi.cube.impl.conf.CubeBuildByPart;
import com.finebi.cube.impl.conf.CubeBuildSingleTable;
import com.finebi.cube.impl.conf.CubeBuildStaff;
import com.finebi.cube.location.BICubeResourceRetrieval;
import com.finebi.cube.location.ICubeResourceRetrievalService;
import com.finebi.cube.relation.BITableRelation;
import com.finebi.cube.structure.BICube;
import com.finebi.cube.structure.Cube;
import com.fr.bi.base.BIUser;
import com.fr.bi.cal.generate.BuildCubeTask;
import com.fr.bi.common.factory.BIFactoryHelper;
import com.fr.bi.conf.provider.BIConfigureManagerCenter;
import com.fr.bi.stable.data.BITableID;
import com.fr.bi.stable.data.db.PersistentTable;
import com.fr.bi.stable.engine.CubeTask;
import com.finebi.cube.common.log.BILoggerFactory;
import com.fr.bi.stable.utils.time.BIDateUtils;
import com.fr.bi.web.conf.services.utils.BICubeGenerateUtils;

import java.util.Set;

/**
 * Created by kary on 16/5/30.
 */
public class CubeTaskHelper {

    private static BICubeManagerProvider cubeManager = CubeGenerationManager.getCubeManager();

    public static boolean CubeBuildSingleTable(long userId, BITableID hostTableId, String childTableSourceId, int updateType) {
        BILoggerFactory.getLogger().info(BIDateUtils.getCurrentDateTime() + " Cube single table update start");
        CubeBuild cubeBuild = new CubeBuildSingleTable(new BIBusinessTable(hostTableId), childTableSourceId, userId, updateType);
        boolean taskAdd = cubeManager.addTask(new BuildCubeTask(new BIUser(userId), cubeBuild), userId);
        return taskAdd;
    }

    public static boolean CubeBuildStaff(long userId) {
        boolean taskAddResult = false;
        CubeBuild cubeBuild;
        /*若cube不存在,全局更新*/
/*若有新增表或者新增关联，增量更新，否则进行全量*/
        StringBuffer msg = new StringBuffer();
        if (isPart(userId)) {
            Set<BIBusinessTable> businessTables = BICubeGenerateUtils.getTables4CubeGenerate(userId);
            Set<BITableRelation> relations = BICubeGenerateUtils.getRelations4CubeGenerate(userId);
            cubeBuild = new CubeBuildByPart(userId, businessTables, relations);
            msg.append(" Cube part update start"+"\n");
            for (BIBusinessTable table : businessTables) {
                msg.append(table.getTableSource().getTableName()+"\n");
            }
            for (BITableRelation relation : relations) {
                try {
                    msg.append(relation.createJSON().toString()+"\n");
                } catch (Exception e) {
                    BILoggerFactory.getLogger().error(e.getMessage(), e);
                }
            }
        } else {
            msg.append(" Cube all update start");
            cubeBuild = new CubeBuildStaff(new BIUser(userId));
            BILoggerFactory.getLogger().info(BIDateUtils.getCurrentDateTime() + " preCondition checking……");
        }
        if (preConditionsCheck(userId, cubeBuild)) {
            CubeTask task = new BuildCubeTask(new BIUser(userId), cubeBuild);
            BILoggerFactory.getLogger().info(BIDateUtils.getCurrentDateTime() + msg);
            taskAddResult = cubeManager.addTask(task, userId);
        }
        return taskAddResult;
    }


    private static boolean isPart(long userId) {
        Set<BIBusinessTable> newTables = BICubeGenerateUtils.getTables4CubeGenerate(userId);
        Set<BITableRelation> newRelationSet = BICubeGenerateUtils.getRelations4CubeGenerate(userId);
        ICubeResourceDiscovery discovery = BIFactoryHelper.getObject(ICubeResourceDiscovery.class);
        ICubeResourceRetrievalService resourceRetrievalService = new BICubeResourceRetrieval(BICubeConfiguration.getConf(Long.toString(userId)));
        Cube cube = new BICube(resourceRetrievalService, discovery);
        boolean isPart = (newTables.size() > 0 || newRelationSet.size() > 0) && cube.isVersionAvailable();
        return isPart;
    }

    private static boolean preConditionsCheck(long userId, CubeBuild cubeBuild) {
        boolean conditionsMeet = cubeBuild.preConditionsCheck();
        if (!conditionsMeet) {
            String errorMessage = "preConditions check failed! Please check the available HD space and data connections";
            BILoggerFactory.getLogger().error(errorMessage);
            BIConfigureManagerCenter.getLogManager().errorTable(new PersistentTable("", "", ""), errorMessage, userId);
            BIConfigureManagerCenter.getLogManager().logEnd(userId);
        }
        return conditionsMeet;
    }
}
