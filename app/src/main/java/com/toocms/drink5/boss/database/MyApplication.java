package com.toocms.drink5.boss.database;

import com.baidu.mapapi.SDKInitializer;
import com.toocms.frame.config.WeApplication;

import org.xutils.DbManager;

/**
 * @author Zero
 * @date 2016/6/29 20:26
 */
public class MyApplication extends WeApplication {

    private DbManager.DaoConfig daoConfig;

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        daoConfig = new DbManager.DaoConfig()
                .setDbName("boss.db")
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager dbManager) {
                        // 开启WAL
                        dbManager.getDatabase().enableWriteAheadLogging();
                    }
                }).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager dbManager, int i, int i1) {
                    }
                });
    }

}
