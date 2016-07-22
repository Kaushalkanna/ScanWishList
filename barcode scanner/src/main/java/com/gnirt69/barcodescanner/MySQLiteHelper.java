package com.gnirt69.barcodescanner;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_UPC = "upc";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IMAGEURL = "imageurl";
    public static final String COLUMN_PRODUCTURL = "producturl";
    public static final String COLUMN_STORENAME = "storename";

    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 4;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_ITEMS + "( "
            + COLUMN_UPC + " text primary key not null, "
            + COLUMN_NAME + " text not null, "
            + COLUMN_PRICE + " text not null, "
            + COLUMN_IMAGEURL + " text, "
            + COLUMN_PRODUCTURL + " text, "
            + COLUMN_STORENAME + " text)";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

}
