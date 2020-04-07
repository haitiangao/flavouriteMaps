package com.example.flamvouritemap.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "Favourite_Result")

public class FavouriteResult {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    public FavouriteResult() {
    }

    @Ignore
    public FavouriteResult(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
