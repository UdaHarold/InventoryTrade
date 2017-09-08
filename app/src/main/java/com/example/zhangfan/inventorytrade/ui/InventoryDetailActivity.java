package com.example.zhangfan.inventorytrade.ui;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhangfan.inventorytrade.R;
import com.example.zhangfan.inventorytrade.data.InventoryContract.InventoryEntry;

import static com.example.zhangfan.inventorytrade.R.id.sales_value;

public class InventoryDetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    //id > 0 update or id = 0 insert
    private int mInventoryId;
    private Button mSalesButton;
    private Button mOrderButton;
    private Button mDeleteButton;
    private EditText mNameEditView;
    private EditText mQuantityEditView;
    private EditText mPriceEditView;
    private TextView mSaleTextView;
    private TextView mSaleValueTextView;

    private static final int INVENTORY_DETAIL_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);

        mInventoryId = getIntent().getIntExtra(InventoryEntry._ID, 0);

        mSalesButton = (Button) findViewById(R.id.sales_button);
        mOrderButton = (Button) findViewById(R.id.order_button);
        mDeleteButton = (Button) findViewById(R.id.delete_button);
        mNameEditView = (EditText) findViewById(R.id.inventory_name_value);
        mQuantityEditView = (EditText) findViewById(R.id.inventory_quantity_value);
        mPriceEditView = (EditText) findViewById(R.id.inventory_price_value);
        mSaleTextView = (TextView) findViewById(R.id.sales_label);
        mSaleValueTextView = (TextView) findViewById(R.id.sales_value);

        initInventoryValues();
    }

    public void saveInventory(View view) {
        String name = mNameEditView.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(getBaseContext(), "商品名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String quantity = mQuantityEditView.getText().toString().trim();
        if (quantity.isEmpty()) {
            Toast.makeText(getBaseContext(), "商品数量不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String price = mPriceEditView.getText().toString().trim();
        if (price.isEmpty()) {
            Toast.makeText(getBaseContext(), "商品价格不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(InventoryEntry.COLUMN_NAME, name);
        contentValues.put(InventoryEntry.COLUMN_QUANTITY, Integer.valueOf(quantity));
        contentValues.put(InventoryEntry.COLUMN_PRICE, Double.valueOf(price));
        contentValues.put(InventoryEntry.COLUMN_SALETIMES, 0);
        if (mInventoryId == 0) {
            contentValues.put(InventoryEntry.COLUMN_SALETIMES, 0);
            Uri uri = getContentResolver().insert(InventoryEntry.CONTENT_URI, contentValues);
        }

        if (mInventoryId > 0) {
            int saleTimes = Integer.valueOf(mSaleValueTextView.getText().toString());
            contentValues.put(InventoryEntry.COLUMN_SALETIMES, saleTimes);
            Uri uri = InventoryEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(mInventoryId)).build();
            getContentResolver().update(uri, contentValues, null, null);
        }

        // turn back to List
        backToList();
    }

    // sales button clicked
    public void saleInventory(View view) {
        int quantity = Integer.valueOf(mQuantityEditView.getText().toString().trim());
        if (quantity > 0) {
            int saleTimes = Integer.valueOf(mSaleValueTextView.getText().toString()) + 1;
            quantity = quantity - 1;
            Uri uri = InventoryEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(mInventoryId)).build();
            String name = mNameEditView.getText().toString().trim();
            double price = Double.valueOf(mPriceEditView.getText().toString().trim());
            ContentValues contentValues = new ContentValues();
            contentValues.put(InventoryEntry.COLUMN_NAME, name);
            contentValues.put(InventoryEntry.COLUMN_QUANTITY, quantity);
            contentValues.put(InventoryEntry.COLUMN_PRICE, price);
            contentValues.put(InventoryEntry.COLUMN_SALETIMES, saleTimes);
            getContentResolver().update(uri, contentValues, null, null);
            backToList();

        } else {
            Toast.makeText(this, "库存数量不足", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteInventoryClicked(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Warning!");
        dialog.setMessage("Are you sure to delete?");
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri uri = InventoryEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(mInventoryId)).build();
                getContentResolver().delete(uri, null, null);
                dialog.dismiss();
                backToList();
            }
        });
        dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void orderInventoryClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, mNameEditView.getText().toString().trim());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void backToList() {
        Intent intent = new Intent(InventoryDetailActivity.this, InventoryListActivity.class);
        startActivity(intent);
    }

    // hide other button when add new inventory
    private void hideButtonAtInsert() {
        mSalesButton.setVisibility(View.INVISIBLE);
        mOrderButton.setVisibility(View.INVISIBLE);
        mDeleteButton.setVisibility(View.INVISIBLE);
        mSaleTextView.setVisibility(View.INVISIBLE);
    }

    // show other button when edit inventory
    private void showButtonAtUpdate() {
        mSalesButton.setVisibility(View.VISIBLE);
        mOrderButton.setVisibility(View.VISIBLE);
        mDeleteButton.setVisibility(View.VISIBLE);
        mSaleTextView.setVisibility(View.VISIBLE);
    }

    // load data from db by id
    private void initInventoryValues() {
        if (mInventoryId > 0) {
            showButtonAtUpdate();
            getSupportLoaderManager().initLoader(INVENTORY_DETAIL_LOADER_ID, null, this);
        } else {
            hideButtonAtInsert();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    Uri uri = InventoryEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(mInventoryId)).build();
                    return getContentResolver().query(uri,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            if (data.moveToFirst()) {
                String name = data.getString(InventoryListActivity.INDEX_INVENTORY_NAME);
                int quantity = data.getInt(InventoryListActivity.INDEX_INVENTORY_QUANTITY);
                double price = data.getDouble(InventoryListActivity.INDEX_INVENTORY_PRICE);
                int saleTimes = data.getInt(InventoryListActivity.INDEX_INVENTORY_SALETIMES);

                mNameEditView.setText(name);
                mQuantityEditView.setText(String.valueOf(quantity));
                mPriceEditView.setText(String.valueOf(price));
                mSaleValueTextView.setText(String.valueOf(saleTimes));
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
