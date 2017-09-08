package com.example.zhangfan.inventorytrade.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.zhangfan.inventorytrade.R;

/**
 * Created by Harold on 2017/9/4.
 */

public class InventoryCursorAdapter extends RecyclerView.Adapter<InventoryCursorAdapter.InventoryViewHolder> {

    private Cursor mCursor;
    private Context mContext;
    final private InventoryAdapterOnClickHandler mClickHandler;

    public InventoryCursorAdapter(Context mContext, InventoryAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        this.mClickHandler = clickHandler;
    }

    public interface InventoryAdapterOnClickHandler {
        void onItemClick(int id);
    }

    @Override
    public InventoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.inventory_list_item, parent, false);
        view.setFocusable(true);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InventoryViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String name = mCursor.getString(InventoryListActivity.INDEX_INVENTORY_NAME);
        holder.mNameTextView.setText(name);
        int quantity = mCursor.getInt(InventoryListActivity.INDEX_INVENTORY_QUANTITY);
        holder.mQuantityTextView.setText(String.valueOf(quantity));
        double price = mCursor.getDouble(InventoryListActivity.INDEX_INVENTORY_PRICE);
        holder.mPriceTextView.setText(String.valueOf(price) + "元");
        int saleTimes = mCursor.getInt(InventoryListActivity.INDEX_INVENTORY_SALETIMES);
        holder.mSalesTextView.setText(String.valueOf(saleTimes) + "次");
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    public Cursor swapCursor(Cursor c) {
        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class InventoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTextView;
        private TextView mQuantityTextView;
        private TextView mPriceTextView;
        private TextView mSalesTextView;

        public InventoryViewHolder(View itemView) {
            super(itemView);
            mNameTextView = (TextView) itemView.findViewById(R.id.inventory_name_value);
            mQuantityTextView = (TextView) itemView.findViewById(R.id.inventory_quantity_value);
            mPriceTextView = (TextView) itemView.findViewById(R.id.inventory_price_value);
            mSalesTextView = (TextView) itemView.findViewById(R.id.inventory_sales_value);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mCursor.moveToPosition(position);
            int id = mCursor.getInt(InventoryListActivity.INDEX_INVENTORY_ID);
            mClickHandler.onItemClick(id);
        }
    }
}
