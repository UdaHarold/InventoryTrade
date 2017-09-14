package com.example.zhangfan.inventorytrade.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.zhangfan.inventorytrade.R;
import com.example.zhangfan.inventorytrade.data.InventoryContract.InventoryEntry;

import static android.R.attr.id;

public class InventoryListActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor>,
        InventoryCursorAdapter.InventoryAdapterOnClickHandler {

    private static final int INVENTORY_LOADER_ID = 0;
    private InventoryCursorAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;

    public static final String[] MAIN_INVENTORY_PROJECTION = {
            InventoryEntry._ID,
            InventoryEntry.COLUMN_NAME,
            InventoryEntry.COLUMN_QUANTITY,
            InventoryEntry.COLUMN_PRICE,
            InventoryEntry.COLUMN_SALETIMES,
            InventoryEntry.COLUMN_IMAGEPATH
    };

    public static final int INDEX_INVENTORY_ID = 0;
    public static final int INDEX_INVENTORY_NAME = 1;
    public static final int INDEX_INVENTORY_QUANTITY = 2;
    public static final int INDEX_INVENTORY_PRICE = 3;
    public static final int INDEX_INVENTORY_SALETIMES = 4;
    public static final int INDEX_INVENTORY_IMAGEPATH = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);
        mEmptyTextView = (TextView) findViewById(R.id.emptyTextView);
        mRecyclerView = (RecyclerView) findViewById(R.id.inventoryRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InventoryCursorAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.addFab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(InventoryListActivity.this, InventoryDetailActivity.class);
                addTaskIntent.putExtra(InventoryEntry._ID, 0);
                startActivity(addTaskIntent);
            }
        });
        getSupportLoaderManager().initLoader(INVENTORY_LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportLoaderManager().restartLoader(INVENTORY_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mData = null;

            @Override
            protected void onStartLoading() {
                if (mData != null) {
                    deliverResult(mData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(InventoryEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.getCount() > 0) {
            mEmptyTextView.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter.swapCursor(data);
        } else {
            mEmptyTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            mAdapter.swapCursor(null);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(int id) {
        Intent intent = new Intent(InventoryListActivity.this, InventoryDetailActivity.class);
        intent.putExtra(InventoryEntry._ID, id);
        startActivity(intent);
    }
}
