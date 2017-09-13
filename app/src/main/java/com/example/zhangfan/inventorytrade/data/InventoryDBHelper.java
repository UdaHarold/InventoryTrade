package com.example.zhangfan.inventorytrade.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.zhangfan.inventorytrade.data.InventoryContract.InventoryEntry;

/**
 * Created by Harold on 2017/9/4.
 */

public class InventoryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";

    private static final int VERSION = 1;

    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME + " (" +
                InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InventoryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                InventoryEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, " +
                InventoryEntry.COLUMN_PRICE + " REAL NOT NULL, " +
                InventoryEntry.COLUMN_SALETIMES + " INTEGER NOT NULL, " +
                InventoryEntry.COLUMN_IMAGEPATH + " TEXT);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + InventoryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
