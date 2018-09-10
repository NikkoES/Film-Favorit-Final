package io.github.nikkoes.katalogfilm.model;

import android.database.Cursor;

import java.io.Serializable;

import io.github.nikkoes.katalogfilm.db.DatabaseContract;

import static android.provider.BaseColumns._ID;
import static io.github.nikkoes.katalogfilm.db.DatabaseContract.getColumnInt;
import static io.github.nikkoes.katalogfilm.db.DatabaseContract.getColumnString;

public class Favorite implements Serializable {

    private int id;
    private String name;
    private String backdrop;
    private String date;
    private String description;

    public Favorite(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.name = getColumnString(cursor, DatabaseContract.FavoriteColumns.NAME);
        this.backdrop = getColumnString(cursor, DatabaseContract.FavoriteColumns.BACKDROP);
        this.date = getColumnString(cursor, DatabaseContract.FavoriteColumns.RELEASE_DATE);
        this.description = getColumnString(cursor, DatabaseContract.FavoriteColumns.DESCRIPTION);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }
}
