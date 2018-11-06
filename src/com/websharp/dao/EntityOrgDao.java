package com.websharp.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.websharp.dao.EntityOrg;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ENTITY_ORG.
*/
public class EntityOrgDao extends AbstractDao<EntityOrg, Void> {

    public static final String TABLENAME = "ENTITY_ORG";

    /**
     * Properties of entity EntityOrg.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Organization_code = new Property(0, String.class, "organization_code", false, "ORGANIZATION_CODE");
        public final static Property Organization_name = new Property(1, String.class, "organization_name", false, "ORGANIZATION_NAME");
    };


    public EntityOrgDao(DaoConfig config) {
        super(config);
    }
    
    public EntityOrgDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ENTITY_ORG' (" + //
                "'ORGANIZATION_CODE' TEXT," + // 0: organization_code
                "'ORGANIZATION_NAME' TEXT);"); // 1: organization_name
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ENTITY_ORG'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, EntityOrg entity) {
        stmt.clearBindings();
 
        String organization_code = entity.getOrganization_code();
        if (organization_code != null) {
            stmt.bindString(1, organization_code);
        }
 
        String organization_name = entity.getOrganization_name();
        if (organization_name != null) {
            stmt.bindString(2, organization_name);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public EntityOrg readEntity(Cursor cursor, int offset) {
        EntityOrg entity = new EntityOrg( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // organization_code
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // organization_name
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, EntityOrg entity, int offset) {
        entity.setOrganization_code(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setOrganization_name(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(EntityOrg entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(EntityOrg entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}