package com.sajorahasan.tiffincounter.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.sajorahasan.tiffincounter.utils.DateConverter;

import java.util.Date;

/**
 * Created by admin on 14-11-2017.
 */

@Entity
@TypeConverters({DateConverter.class})
public class Tiffin {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "tiffin_date")
    private String tiffinDate;

    private String type;

    private int amount;

    @TypeConverters({DateConverter.class})
    private Date added;

    public Tiffin() {
    }

    public Tiffin(String tiffinDate, String type, int amount, Date added) {
        this.tiffinDate = tiffinDate;
        this.type = type;
        this.amount = amount;
        this.added = added;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTiffinDate() {
        return tiffinDate;
    }

    public void setTiffinDate(String tiffinDate) {
        this.tiffinDate = tiffinDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getAdded() {
        return added;
    }

    public void setAdded(Date added) {
        this.added = added;
    }
}
