package com.websharp.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import com.websharp.dao.EntityUserDao;
import com.websharp.dao.EntityOrgDao;
import com.websharp.dao.EntityBillRecordDao;
import com.websharp.dao.EntityCustomerDao;
import com.websharp.dao.EntityCustomerPackageDao;
import com.websharp.dao.EntityCustomerResourceDao;
import com.websharp.dao.EntityCustomerUserDao;
import com.websharp.dao.EntityPayAmountRecordDao;
import com.websharp.dao.EntityPlayRecordDao;
import com.websharp.dao.EntityAccountBookDao;
import com.websharp.dao.EntityCustomerOrderDao;
import com.websharp.dao.EntityCustomerOrderFeeDao;
import com.websharp.dao.EntityCustomerOrderProductDao;
import com.websharp.dao.EntityCustomerOrderResourceDao;
import com.websharp.dao.EntityStaticDataDao;
import com.websharp.dao.EntityOfferDao;
import com.websharp.dao.EntityProductDao;
import com.websharp.dao.EntityNoticeDao;
import com.websharp.dao.EntityWorkLogDgDao;
import com.websharp.dao.EntityWorkLogYeDao;
import com.websharp.dao.EntityWorkLogGhDao;
import com.websharp.dao.EntityCommandDao;
import com.websharp.dao.EntityClientConfigDao;
import com.websharp.dao.EntityPayOrderDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 1000): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1000;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        EntityUserDao.createTable(db, ifNotExists);
        EntityOrgDao.createTable(db, ifNotExists);
        EntityBillRecordDao.createTable(db, ifNotExists);
        EntityCustomerDao.createTable(db, ifNotExists);
        EntityCustomerPackageDao.createTable(db, ifNotExists);
        EntityCustomerResourceDao.createTable(db, ifNotExists);
        EntityCustomerUserDao.createTable(db, ifNotExists);
        EntityPayAmountRecordDao.createTable(db, ifNotExists);
        EntityPlayRecordDao.createTable(db, ifNotExists);
        EntityAccountBookDao.createTable(db, ifNotExists);
        EntityCustomerOrderDao.createTable(db, ifNotExists);
        EntityCustomerOrderFeeDao.createTable(db, ifNotExists);
        EntityCustomerOrderProductDao.createTable(db, ifNotExists);
        EntityCustomerOrderResourceDao.createTable(db, ifNotExists);
        EntityStaticDataDao.createTable(db, ifNotExists);
        EntityOfferDao.createTable(db, ifNotExists);
        EntityProductDao.createTable(db, ifNotExists);
        EntityNoticeDao.createTable(db, ifNotExists);
        EntityWorkLogDgDao.createTable(db, ifNotExists);
        EntityWorkLogYeDao.createTable(db, ifNotExists);
        EntityWorkLogGhDao.createTable(db, ifNotExists);
        EntityCommandDao.createTable(db, ifNotExists);
        EntityClientConfigDao.createTable(db, ifNotExists);
        EntityPayOrderDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        EntityUserDao.dropTable(db, ifExists);
        EntityOrgDao.dropTable(db, ifExists);
        EntityBillRecordDao.dropTable(db, ifExists);
        EntityCustomerDao.dropTable(db, ifExists);
        EntityCustomerPackageDao.dropTable(db, ifExists);
        EntityCustomerResourceDao.dropTable(db, ifExists);
        EntityCustomerUserDao.dropTable(db, ifExists);
        EntityPayAmountRecordDao.dropTable(db, ifExists);
        EntityPlayRecordDao.dropTable(db, ifExists);
        EntityAccountBookDao.dropTable(db, ifExists);
        EntityCustomerOrderDao.dropTable(db, ifExists);
        EntityCustomerOrderFeeDao.dropTable(db, ifExists);
        EntityCustomerOrderProductDao.dropTable(db, ifExists);
        EntityCustomerOrderResourceDao.dropTable(db, ifExists);
        EntityStaticDataDao.dropTable(db, ifExists);
        EntityOfferDao.dropTable(db, ifExists);
        EntityProductDao.dropTable(db, ifExists);
        EntityNoticeDao.dropTable(db, ifExists);
        EntityWorkLogDgDao.dropTable(db, ifExists);
        EntityWorkLogYeDao.dropTable(db, ifExists);
        EntityWorkLogGhDao.dropTable(db, ifExists);
        EntityCommandDao.dropTable(db, ifExists);
        EntityClientConfigDao.dropTable(db, ifExists);
        EntityPayOrderDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(EntityUserDao.class);
        registerDaoClass(EntityOrgDao.class);
        registerDaoClass(EntityBillRecordDao.class);
        registerDaoClass(EntityCustomerDao.class);
        registerDaoClass(EntityCustomerPackageDao.class);
        registerDaoClass(EntityCustomerResourceDao.class);
        registerDaoClass(EntityCustomerUserDao.class);
        registerDaoClass(EntityPayAmountRecordDao.class);
        registerDaoClass(EntityPlayRecordDao.class);
        registerDaoClass(EntityAccountBookDao.class);
        registerDaoClass(EntityCustomerOrderDao.class);
        registerDaoClass(EntityCustomerOrderFeeDao.class);
        registerDaoClass(EntityCustomerOrderProductDao.class);
        registerDaoClass(EntityCustomerOrderResourceDao.class);
        registerDaoClass(EntityStaticDataDao.class);
        registerDaoClass(EntityOfferDao.class);
        registerDaoClass(EntityProductDao.class);
        registerDaoClass(EntityNoticeDao.class);
        registerDaoClass(EntityWorkLogDgDao.class);
        registerDaoClass(EntityWorkLogYeDao.class);
        registerDaoClass(EntityWorkLogGhDao.class);
        registerDaoClass(EntityCommandDao.class);
        registerDaoClass(EntityClientConfigDao.class);
        registerDaoClass(EntityPayOrderDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
