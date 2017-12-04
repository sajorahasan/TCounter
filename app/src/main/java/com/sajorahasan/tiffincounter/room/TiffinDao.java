package com.sajorahasan.tiffincounter.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by admin on 14-11-2017.
 */

@Dao
public interface TiffinDao {

    @Query("SELECT * FROM Tiffin")
    List<Tiffin> getAllTiffins();

    @Query("SELECT * FROM Tiffin WHERE id=:id")
    Tiffin getTiffinById(int id);

    @Query("SELECT * FROM Tiffin where type LIKE :type")
    List<Tiffin> filterTiffins(String type);

    @Query("SELECT * FROM Tiffin where added LIKE :tiffinDate")
    List<Tiffin> getTodayTiffins(Date tiffinDate);

    @Query("SELECT * FROM Tiffin WHERE added BETWEEN :dayst AND :dayet")
    List<Tiffin> filterTiffinsByDate(Date dayst, Date dayet);

    @Query("SELECT COUNT(*) from Tiffin")
    int countUsers();

    @Insert
    void insert(Tiffin tiffin);

    @Update(onConflict = REPLACE)
    void updateTiffin(Tiffin tiffin);

    @Delete
    void delete(Tiffin tiffin);

    @Query("DELETE FROM Tiffin")
    void deleteAll();
}
