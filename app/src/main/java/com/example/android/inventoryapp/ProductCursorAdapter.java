package com.example.android.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract;

class ProductCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = ProductCursorAdapter.class.getSimpleName();

    private Context mContext;

    ProductCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0 /* int flags */);
        mContext = context;
    }

    @Override public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        // Find the columns of product attributes that we're interested in from the cursor
        int idColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int imageColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGERESOURCEID);

        // Read the product attributes from the Cursor for the current product
        final Integer productId = cursor.getInt(idColumnIndex);
        final String productName = cursor.getString(nameColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);
        final Double productPrice = cursor.getDouble(priceColumnIndex);
        final String productImage = cursor.getString(imageColumnIndex);

        // Update the TextViews with the attributes for the current product
        viewHolder.nameTextView.setText(productName);
        viewHolder.quantityTextView.setText(productQuantity);
        viewHolder.priceTextView.setText(productPrice.toString());

        Resources r = mContext.getResources();
        int drawableId = r.getIdentifier(productImage, "drawable", "com.example.android.inventoryapp");
        viewHolder.imageView.setImageResource(drawableId);

        viewHolder.saleButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // Create a ContentValues object where column names are the keys,
                // and product attributes are the values.
                ContentValues values = new ContentValues();
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, productName);
                if (Integer.valueOf(productQuantity) > 1) {
                    values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, Integer.valueOf(productQuantity) - 1);
                }
                values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, productPrice);

                String selection = "";
                String[] selectionArgs = {""};

                Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, productId);
                Log.i(LOG_TAG, " currentProductUri: " + currentProductUri.toString());
                mContext.getContentResolver().update(currentProductUri, values, selection, selectionArgs);
            }
        });
    }

    private static class ViewHolder {
        TextView nameTextView;
        TextView quantityTextView;
        TextView priceTextView;
        Button saleButton;
        ImageView imageView;

        ViewHolder(View view) {
            // Find individual views that we want to modify in the list item layout
            nameTextView = (TextView) view.findViewById(R.id.name);
            quantityTextView = (TextView) view.findViewById(R.id.quantity);
            priceTextView = (TextView) view.findViewById(R.id.price);
            imageView = (ImageView) view.findViewById(R.id.image_view);
            saleButton = (Button) view.findViewById(R.id.sale_button);
        }
    }

}
