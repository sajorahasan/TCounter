package com.sajorahasan.tiffincounter.room;

import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.EntityDeletionOrUpdateAdapter;
import android.arch.persistence.room.EntityInsertionAdapter;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.RoomSQLiteQuery;
import android.arch.persistence.room.SharedSQLiteStatement;
import android.database.Cursor;
import com.sajorahasan.tiffincounter.utils.DateConverter;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Generated;

@Generated("android.arch.persistence.room.RoomProcessor")
public class TiffinDao_Impl implements TiffinDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTiffin;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfTiffin;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfTiffin;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public TiffinDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTiffin = new EntityInsertionAdapter<Tiffin>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Tiffin`(`id`,`tiffin_date`,`type`,`amount`,`added`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Tiffin value) {
        stmt.bindLong(1, value.getId());
        if (value.getTiffinDate() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTiffinDate());
        }
        if (value.getType() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getType());
        }
        stmt.bindLong(4, value.getAmount());
        final Long _tmp;
        _tmp = DateConverter.toTimestamp(value.getAdded());
        if (_tmp == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp);
        }
      }
    };
    this.__deletionAdapterOfTiffin = new EntityDeletionOrUpdateAdapter<Tiffin>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Tiffin` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Tiffin value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfTiffin = new EntityDeletionOrUpdateAdapter<Tiffin>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR REPLACE `Tiffin` SET `id` = ?,`tiffin_date` = ?,`type` = ?,`amount` = ?,`added` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Tiffin value) {
        stmt.bindLong(1, value.getId());
        if (value.getTiffinDate() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTiffinDate());
        }
        if (value.getType() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getType());
        }
        stmt.bindLong(4, value.getAmount());
        final Long _tmp;
        _tmp = DateConverter.toTimestamp(value.getAdded());
        if (_tmp == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, _tmp);
        }
        stmt.bindLong(6, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM Tiffin";
        return _query;
      }
    };
  }

  @Override
  public void insert(Tiffin tiffin) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfTiffin.insert(tiffin);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Tiffin tiffin) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfTiffin.handle(tiffin);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateTiffin(Tiffin tiffin) {
    __db.beginTransaction();
    try {
      __updateAdapterOfTiffin.handle(tiffin);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<Tiffin> getAllTiffins() {
    final String _sql = "SELECT * FROM Tiffin";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfTiffinDate = _cursor.getColumnIndexOrThrow("tiffin_date");
      final int _cursorIndexOfType = _cursor.getColumnIndexOrThrow("type");
      final int _cursorIndexOfAmount = _cursor.getColumnIndexOrThrow("amount");
      final int _cursorIndexOfAdded = _cursor.getColumnIndexOrThrow("added");
      final List<Tiffin> _result = new ArrayList<Tiffin>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Tiffin _item;
        _item = new Tiffin();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTiffinDate;
        _tmpTiffinDate = _cursor.getString(_cursorIndexOfTiffinDate);
        _item.setTiffinDate(_tmpTiffinDate);
        final String _tmpType;
        _tmpType = _cursor.getString(_cursorIndexOfType);
        _item.setType(_tmpType);
        final int _tmpAmount;
        _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
        _item.setAmount(_tmpAmount);
        final Date _tmpAdded;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfAdded)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfAdded);
        }
        _tmpAdded = DateConverter.toDate(_tmp);
        _item.setAdded(_tmpAdded);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Tiffin getTiffinById(int id) {
    final String _sql = "SELECT * FROM Tiffin WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfTiffinDate = _cursor.getColumnIndexOrThrow("tiffin_date");
      final int _cursorIndexOfType = _cursor.getColumnIndexOrThrow("type");
      final int _cursorIndexOfAmount = _cursor.getColumnIndexOrThrow("amount");
      final int _cursorIndexOfAdded = _cursor.getColumnIndexOrThrow("added");
      final Tiffin _result;
      if(_cursor.moveToFirst()) {
        _result = new Tiffin();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpTiffinDate;
        _tmpTiffinDate = _cursor.getString(_cursorIndexOfTiffinDate);
        _result.setTiffinDate(_tmpTiffinDate);
        final String _tmpType;
        _tmpType = _cursor.getString(_cursorIndexOfType);
        _result.setType(_tmpType);
        final int _tmpAmount;
        _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
        _result.setAmount(_tmpAmount);
        final Date _tmpAdded;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfAdded)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfAdded);
        }
        _tmpAdded = DateConverter.toDate(_tmp);
        _result.setAdded(_tmpAdded);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Tiffin> filterTiffins(String type) {
    final String _sql = "SELECT * FROM Tiffin where type LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (type == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, type);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfTiffinDate = _cursor.getColumnIndexOrThrow("tiffin_date");
      final int _cursorIndexOfType = _cursor.getColumnIndexOrThrow("type");
      final int _cursorIndexOfAmount = _cursor.getColumnIndexOrThrow("amount");
      final int _cursorIndexOfAdded = _cursor.getColumnIndexOrThrow("added");
      final List<Tiffin> _result = new ArrayList<Tiffin>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Tiffin _item;
        _item = new Tiffin();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTiffinDate;
        _tmpTiffinDate = _cursor.getString(_cursorIndexOfTiffinDate);
        _item.setTiffinDate(_tmpTiffinDate);
        final String _tmpType;
        _tmpType = _cursor.getString(_cursorIndexOfType);
        _item.setType(_tmpType);
        final int _tmpAmount;
        _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
        _item.setAmount(_tmpAmount);
        final Date _tmpAdded;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfAdded)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfAdded);
        }
        _tmpAdded = DateConverter.toDate(_tmp);
        _item.setAdded(_tmpAdded);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Tiffin> getTodayTiffins(Date tiffinDate) {
    final String _sql = "SELECT * FROM Tiffin where added LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final Long _tmp;
    _tmp = DateConverter.toTimestamp(tiffinDate);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfTiffinDate = _cursor.getColumnIndexOrThrow("tiffin_date");
      final int _cursorIndexOfType = _cursor.getColumnIndexOrThrow("type");
      final int _cursorIndexOfAmount = _cursor.getColumnIndexOrThrow("amount");
      final int _cursorIndexOfAdded = _cursor.getColumnIndexOrThrow("added");
      final List<Tiffin> _result = new ArrayList<Tiffin>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Tiffin _item;
        _item = new Tiffin();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTiffinDate;
        _tmpTiffinDate = _cursor.getString(_cursorIndexOfTiffinDate);
        _item.setTiffinDate(_tmpTiffinDate);
        final String _tmpType;
        _tmpType = _cursor.getString(_cursorIndexOfType);
        _item.setType(_tmpType);
        final int _tmpAmount;
        _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
        _item.setAmount(_tmpAmount);
        final Date _tmpAdded;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfAdded)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfAdded);
        }
        _tmpAdded = DateConverter.toDate(_tmp_1);
        _item.setAdded(_tmpAdded);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Tiffin> filterTiffinsByDate(Date dayst, Date dayet) {
    final String _sql = "SELECT * FROM Tiffin WHERE added BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final Long _tmp;
    _tmp = DateConverter.toTimestamp(dayst);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp);
    }
    _argIndex = 2;
    final Long _tmp_1;
    _tmp_1 = DateConverter.toTimestamp(dayet);
    if (_tmp_1 == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindLong(_argIndex, _tmp_1);
    }
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfTiffinDate = _cursor.getColumnIndexOrThrow("tiffin_date");
      final int _cursorIndexOfType = _cursor.getColumnIndexOrThrow("type");
      final int _cursorIndexOfAmount = _cursor.getColumnIndexOrThrow("amount");
      final int _cursorIndexOfAdded = _cursor.getColumnIndexOrThrow("added");
      final List<Tiffin> _result = new ArrayList<Tiffin>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Tiffin _item;
        _item = new Tiffin();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTiffinDate;
        _tmpTiffinDate = _cursor.getString(_cursorIndexOfTiffinDate);
        _item.setTiffinDate(_tmpTiffinDate);
        final String _tmpType;
        _tmpType = _cursor.getString(_cursorIndexOfType);
        _item.setType(_tmpType);
        final int _tmpAmount;
        _tmpAmount = _cursor.getInt(_cursorIndexOfAmount);
        _item.setAmount(_tmpAmount);
        final Date _tmpAdded;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfAdded)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfAdded);
        }
        _tmpAdded = DateConverter.toDate(_tmp_2);
        _item.setAdded(_tmpAdded);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public int countUsers() {
    final String _sql = "SELECT COUNT(*) from Tiffin";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getInt(0);
      } else {
        _result = 0;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
