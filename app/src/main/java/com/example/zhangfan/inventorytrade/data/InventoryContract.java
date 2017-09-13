package com.example.zhangfan.inventorytrade.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Harold on 2017/9/4.
 */

public class InventoryContract {

    public static final String AUTHORITY = "com.example.zhangfan.inventorytrade";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_INVENTORY = "inventory";

    public static final class InventoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INVENTORY).build();

        public static final String TABLE_NAME = "inventory";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_PRICE = "price";
        public static final String COLUMN_SALETIMES = "saleTimes";
        public static final String COLUMN_IMAGEPATH = "imagePath";
    }
}
