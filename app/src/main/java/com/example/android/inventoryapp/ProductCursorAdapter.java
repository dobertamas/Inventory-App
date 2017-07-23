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

    ProductCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0 /* int flags */);
    }

    @Override public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);

        // Find the columns of product attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        //Log.i(LOG_TAG, " nameColumnIndex was: " + nameColumnIndex);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        //Log.i(LOG_TAG, " quantityColumnIndex was: " + quantityColumnIndex);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);

        // Read the product attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);
        Log.i(LOG_TAG, " productName was: " + productName);
        String productQuantity = cursor.getString(quantityColumnIndex);
        Log.i(LOG_TAG, " productQuantity was: " + productQuantity);
        Double productPrice = cursor.getDouble(priceColumnIndex);
        Log.i(LOG_TAG, " productPrice was: " + productPrice);

        // Update the TextViews with the attributes for the current product
        nameTextView.setText(productName);
        quantityTextView.setText(productQuantity);
        priceTextView.setText(productPrice.toString());
    }
}
