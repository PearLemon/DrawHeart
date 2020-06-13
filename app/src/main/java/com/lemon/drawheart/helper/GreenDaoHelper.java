package com.lemon.drawheart.helper;

import android.database.sqlite.SQLiteDatabase;

import com.lemon.drawheart.App;
import com.lemon.drawheart.dao.DaoMaster;
import com.lemon.drawheart.dao.DaoSession;
import com.lemon.drawheart.util.DataBaseUtil;

/**
 * @author lemon92xy
 */
public class GreenDaoHelper {
    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    private GreenDaoHelper() {
    }

    private static class GreenDaoHolder {
        private static final GreenDaoHelper INSTANCE = new GreenDaoHelper();
    }

    public static GreenDaoHelper getSingleton() {
        return GreenDaoHolder.INSTANCE;
    }

    private void initGreenDao() {
        database = DataBaseUtil.openDatabase(App.getAppContext());
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        if (daoMaster == null) {
            initGreenDao();
        }
        return daoSession;
    }

    public SQLiteDatabase getDatabase() {
        if (database == null) {
            initGreenDao();
        }
        return database;
    }
}
