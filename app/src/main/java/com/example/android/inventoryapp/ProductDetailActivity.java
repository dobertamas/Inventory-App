package com.example.android.inventoryapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.inventoryapp.data.ProductContract;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ProductDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.product_detail_name) TextView mProductName;
    @InjectView(R.id.product_detail_quantity) TextView mQuantity;
    @InjectView(R.id.product_detail_price) TextView mPrice;
    @InjectView(R.id.product_detail_imageResourceId) TextView mProductImageResourceId;

    /**
     * Content URI for the existing product (null if it's a new product)
     */
    private Uri mCurrentProductUri;

    /**
     * Identifier for the product data loader
     */
    private static final int EXISTING_PRODUCT_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        // Initialize a loader to read the product data from the database
        // and display the current values in the editor
        getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);

    }

    @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the page shows all product attributes, define a projection that contains
        // all columns from the product table
        String[] projection = {
                ProductContract.ProductEntry._ID,
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME,
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY,
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGERESOURCEID};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentProductUri,         // Query the content URI for the current product
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of product attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            // TODO: int imageResourceIdColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGERESOURCEID);

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString(nameColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            double price = cursor.getDouble(priceColumnIndex);
            // TODO:  int imageResourceId = cursor.getInt(imageResourceIdColumnIndex);

            // Update the views on the screen with the values from the database
            mProductName.setText(name);
            mQuantity.setText(quantity);
            mPrice.setText(Double.toString(price));
            // TODO: mProductImageResourceId
        }

    }

    @Override public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mProductName.setText("");
        mQuantity.setText("");
        mQuantity.setText("");
        // TODO: mProductImageResourceId

    }
}
