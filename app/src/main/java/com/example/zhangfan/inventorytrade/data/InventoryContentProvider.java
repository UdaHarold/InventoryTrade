package com.example.zhangfan.inventorytrade.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.zhangfan.inventorytrade.data.InventoryContract;
import com.example.zhangfan.inventorytrade.data.InventoryContract.InventoryEntry;
import com.example.zhangfan.inventorytrade.model.Inventory;

/**
 * Created by Harold on 2017/9/4.
 */

public class InventoryContentProvider extends ContentProvider {

    public static final int CODE_INVENTORY = 100;
    public static final int CODE_INVENTORY_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private InventoryDBHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = InventoryContract.AUTHORITY;
        matcher.addURI(authority, InventoryContract.PATH_INVENTORY, CODE_INVENTORY);
        matcher.addURI(authority, InventoryContract.PATH_INVENTORY + "/#", CODE_INVENTORY_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new InventoryDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_INVENTORY:
                cursor = mOpenHelper.getReadableDatabase().query(
                        InventoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        InventoryEntry._ID + " DESC ");
                break;
            case CODE_INVENTORY_WITH_ID:
                String id = uri.getLastPathSegment();
                cursor = mOpenHelper.getReadableDatabase().query(
                        InventoryEntry.TABLE_NAME,
                        projection,
                        InventoryEntry._ID + " = ? ",
                        new String[]{id},
                        null,
                        null,
                        null);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case CODE_INVENTORY:
                long id = db.insert(InventoryEntry.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int deletedId = 0;
        switch (match) {
            case CODE_INVENTORY_WITH_ID:
                String id = uri.getLastPathSegment();
                deletedId = db.delete(InventoryEntry.TABLE_NAME, " _id = ? ", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (deletedId != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedId;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int affectedId = 0;
        switch (match) {
            case CODE_INVENTORY_WITH_ID:
                String id = uri.getLastPathSegment();
                affectedId = db.update(InventoryEntry.TABLE_NAME, contentValues, " _id = ? ", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (affectedId > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affectedId;
    }
}
