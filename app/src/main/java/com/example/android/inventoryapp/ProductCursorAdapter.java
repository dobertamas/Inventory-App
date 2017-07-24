package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract;

class ProductCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = ProductCursorAdapter.class.getSimpleName();

    private Context mContext;

    ProductCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0 /* int flags */);
        mContext = context;
    }

  /*  @Override public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.nameTextView = (TextView) convertView.findViewById(R.id.name);
            holder.quantityTextView = (TextView) convertView.findViewById(R.id.quantity);
            holder.priceTextView = (TextView) convertView.findViewById(R.id.price);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = (Product) getItem(position);

        holder.nameTextView.setText(product.getName());
        holder.quantityTextView.setText(product.getQuantity());
        holder.priceTextView.setText(String.valueOf(product.getPrice()));

        return convertView;
    }*/

    @Override public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Find the columns of product attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);

        // Read the product attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);
        Log.i(LOG_TAG, " productName was: " + productName);
        String productQuantity = cursor.getString(quantityColumnIndex);
        Log.i(LOG_TAG, " productQuantity was: " + productQuantity);
        Double productPrice = cursor.getDouble(priceColumnIndex);
        Log.i(LOG_TAG, " productPrice was: " + productPrice);

        // Update the TextViews with the attributes for the current product
        viewHolder.nameTextView.setText(productName);
        viewHolder.quantityTextView.setText(productQuantity);
        viewHolder.priceTextView.setText(productPrice.toString());
    }

    private static class ViewHolder {
        TextView nameTextView;
        TextView quantityTextView;
        TextView priceTextView;

        ViewHolder(View view) {
            // Find individual views that we want to modify in the list item layout
            nameTextView = (TextView) view.findViewById(R.id.name);
            quantityTextView = (TextView) view.findViewById(R.id.quantity);
            priceTextView = (TextView) view.findViewById(R.id.price);

        }
    }

}
