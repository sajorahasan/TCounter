package com.sajorahasan.tiffincounter.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Callback;
import android.arch.persistence.db.SupportSQLiteOpenHelper.Configuration;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomOpenHelper;
import android.arch.persistence.room.RoomOpenHelper.Delegate;
import android.arch.persistence.room.util.TableInfo;
import android.arch.persistence.room.util.TableInfo.Column;
import android.arch.persistence.room.util.TableInfo.ForeignKey;
import android.arch.persistence.room.util.TableInfo.Index;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.HashSet;
import javax.annotation.Generated;

@Generated("android.arch.persistence.room.RoomProcessor")
public class AppDatabase_Impl extends AppDatabase {
  private volatile TiffinDao _tiffinDao;

  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Tiffin` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `tiffin_date` TEXT, `type` TEXT, `amount` INTEGER NOT NULL, `added` INTEGER)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"5b55d269704892984941ae8b6220ebfd\")");
      }

      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Tiffin`");
      }

      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsTiffin = new HashMap<String, TableInfo.Column>(5);
        _columnsTiffin.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsTiffin.put("tiffin_date", new TableInfo.Column("tiffin_date", "TEXT", false, 0));
        _columnsTiffin.put("type", new TableInfo.Column("type", "TEXT", false, 0));
        _columnsTiffin.put("amount", new TableInfo.Column("amount", "INTEGER", true, 0));
        _columnsTiffin.put("added", new TableInfo.Column("added", "INTEGER", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTiffin = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTiffin = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTiffin = new TableInfo("Tiffin", _columnsTiffin, _foreignKeysTiffin, _indicesTiffin);
        final TableInfo _existingTiffin = TableInfo.read(_db, "Tiffin");
        if (! _infoTiffin.equals(_existingTiffin)) {
          throw new IllegalStateException("Migration didn't properly handle Tiffin(com.sajorahasan.tiffincounter.room.Tiffin).\n"
                  + " Expected:\n" + _infoTiffin + "\n"
                  + " Found:\n" + _existingTiffin);
        }
      }
    }, "5b55d269704892984941ae8b6220ebfd");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Tiffin");
  }

  @Override
  public TiffinDao getTiffinDao() {
    if (_tiffinDao != null) {
      return _tiffinDao;
    } else {
      synchronized(this) {
        if(_tiffinDao == null) {
          _tiffinDao = new TiffinDao_Impl(this);
        }
        return _tiffinDao;
      }
    }
  }
}
