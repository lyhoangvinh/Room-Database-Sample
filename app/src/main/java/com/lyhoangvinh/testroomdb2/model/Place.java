package com.lyhoangvinh.testroomdb2.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Place {
    @PrimaryKey(autoGenerate = true)
    private int idPlace;
}
