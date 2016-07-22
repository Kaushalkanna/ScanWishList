package com.gnirt69.barcodescanner;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ItemsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = {
            MySQLiteHelper.COLUMN_UPC,
            MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_PRICE,
            MySQLiteHelper.COLUMN_IMAGEURL,
            MySQLiteHelper.COLUMN_PRODUCTURL,
            MySQLiteHelper.COLUMN_STORENAME,
    };

    public ItemsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Item createItem(String upc, String name, String price, String imageurl, String producturl, String storename) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_UPC, upc);
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_PRICE, price);
        values.put(MySQLiteHelper.COLUMN_IMAGEURL, imageurl);
        values.put(MySQLiteHelper.COLUMN_PRODUCTURL, producturl);
        values.put(MySQLiteHelper.COLUMN_STORENAME, storename);
        long insertId = database.insert(MySQLiteHelper.TABLE_ITEMS, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEMS,
                allColumns, "" + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Item newItem = cursorToItem(cursor);
        cursor.close();
        return newItem;
    }

    public void deleteItem(Item item) {
        String UPC = item.getUPC();
        System.out.println("Item deleted with id: " + UPC);
        database.delete(MySQLiteHelper.TABLE_ITEMS, MySQLiteHelper.COLUMN_UPC
                + " = " + UPC, null);
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_ITEMS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = cursorToItem(cursor);
            items.add(item);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return items;
    }

    private Item cursorToItem(Cursor cursor) {
        Item item = new Item();
        item.setUPC(cursor.getString(0));
        item.setName(cursor.getString(1));
        item.setPrice(cursor.getString(2));
        item.setImageUrl(cursor.getString(3));
        item.setProductUrl(cursor.getString(4));
        item.setStoreName(cursor.getString(5));
        return item;
    }
}
