package com.example.android.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapp.data.ProductContract;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ProductDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    @InjectView(R.id.product_detail_name) TextView mProductNameTextView;
    @InjectView(R.id.product_detail_quantity) TextView mQuantityTextView;
    @InjectView(R.id.product_detail_price) TextView mPriceTextView;
    @InjectView(R.id.product_id) TextView mIdTextView;
    @InjectView(R.id.product_detail_imageResourceId) TextView mProductImageResourceId;
    @InjectView(R.id.product_detail_quantity_increase_button) Button mIncreaseButton;
    @InjectView(R.id.product_detail_quantity_decrease_button) Button mDecreaseButton;

    private static final String LOG_TAG = ProductDetailActivity.class.getSimpleName();

    /**
     * Content URI for the existing product (null if it's a new product)
     */
    private Uri mCurrentProductUri;

    /**
     * Identifier for the product data loader
     */
    private static final int EXISTING_PRODUCT_LOADER = 0;

    private String mName;
    private String mQuantity;
    private Double mPrice;
    private Integer mProductId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        mCurrentProductUri = intent.getData();

        // If the intent DOES NOT contain a product content URI, then we know that we are
        // creating a new product.
        if (mCurrentProductUri == null) {
            // This is a new product, so change the app bar to say "Add a product"
            setTitle("Add new product");

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a product that hasn't been created yet.)
            invalidateOptionsMenu();
        }
        else {
            // Otherwise this is an existing product, so change app bar to say "Edit Product"
            setTitle("Edit product");

            // Initialize a loader to read the product data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_productdetail, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save_product:
                // Save product to database
                    saveProduct();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete_product:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveProduct()  {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mProductNameTextView.getText().toString().trim();
        String quantityString = mQuantityTextView.getText().toString().trim();
        String priceString = mPriceTextView.getText().toString().trim();
        String imageResourceIdString = mProductImageResourceId.getText().toString().trim();
        if (nameString == null || nameString.isEmpty()) {
            Toast.makeText(this, "Product name can not be null", Toast.LENGTH_LONG).show();
            return;
        }
        if (priceString == null || priceString.isEmpty()) {
            Toast.makeText(this, "Product price can not be null", Toast.LENGTH_LONG).show();
            return;
        }
        if (imageResourceIdString == null || imageResourceIdString.isEmpty()) {
            Toast.makeText(this, "Product image can not be null", Toast.LENGTH_LONG).show();
            return;
        }

        // Check if this is supposed to be a new product
        // and check if all the fields in the editor are blank
        if (mCurrentProductUri == null &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) &&
                TextUtils.isEmpty(imageResourceIdString)) {
            // Since no fields were modified, we can return early without creating a new product.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }


        // Create a ContentValues object where column names are the keys,
        // and product attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, quantityString);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, priceString);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGERESOURCEID, imageResourceIdString);

        // Determine if this is a new or existing product by checking if mCurrentProductUri is null or not
        if (mCurrentProductUri == null) {
            // This is a NEW product, so insert a new product into the provider,
            // returning the content URI for the new product.
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, "1");
            Uri newUri = getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, "Error with saving product", Toast.LENGTH_SHORT).show();
            }
            else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, "Product successfully saved! ", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            // Otherwise this is an EXISTING product, so update the product with content URI: mCurrentProductUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentProductUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentProductUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, "Error with updating product", Toast.LENGTH_SHORT).show();
            }
            else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, "Product successfully updated! ", Toast.LENGTH_SHORT).show();
            }
        }

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
            int idColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            int quantityColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);

            // Extract out the value from the Cursor for the given column index
            mProductId = cursor.getInt(idColumnIndex);
            mName = cursor.getString(nameColumnIndex);
            mQuantity = cursor.getString(quantityColumnIndex);
            mPrice = cursor.getDouble(priceColumnIndex);

            // Update the views on the screen with the values from the database
            mIdTextView.setText(mProductId.toString());
            mProductNameTextView.setText(mName);
            mQuantityTextView.setText(mQuantity);
            mPriceTextView.setText(Double.toString(mPrice));
        }

    }

    @Override public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mProductNameTextView.setText("");
        mQuantityTextView.setText("");
        mPriceTextView.setText("");
    }

    @OnClick(R.id.product_detail_quantity_increase_button)
    public void increaseProductQuantity() {
        // Create a ContentValues object where column names are the keys,
        // and product attributes are the values.
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, mName);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, Integer.valueOf(mQuantity) + 1);
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, mPrice);

        String selection = "";
        String[] selectionArgs = {""};

        Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, mProductId);
        Log.i(LOG_TAG, " currentProductUri: " + currentProductUri.toString());
        getContentResolver().update(currentProductUri, values, selection, selectionArgs);

    }

    @OnClick(R.id.product_detail_quantity_decrease_button)
    public void decreaseProductQuantity() {
        // Create a ContentValues object where column names are the keys,
        // and product attributes are the values.
        ContentValues values = new ContentValues();
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME, mName);
        if (Integer.valueOf(mQuantity) > 1) {
            values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY, Integer.valueOf(mQuantity) - 1);
        }
        values.put(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE, mPrice);

        String selection = "";
        String[] selectionArgs = {""};

        Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, mProductId);
        Log.i(LOG_TAG, " currentProductUri: " + currentProductUri.toString());
        getContentResolver().update(currentProductUri, values, selection, selectionArgs);

    }

    @OnClick(R.id.product_detail_order_button)
    public void orderProduct() {
        // Create new intent to go to send out en email
        Intent sendingEmailIntent = new Intent(Intent.ACTION_SENDTO);
        sendingEmailIntent.setType("text/plain");
        sendingEmailIntent.putExtra(Intent.EXTRA_EMAIL, "supplier@supplier.com");
        sendingEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ordering your product");
        // Our margin is only 20%
        String orderDetails = "We'd like to order your " + mName + " at a price of " + mPrice * .08;
        sendingEmailIntent.putExtra(Intent.EXTRA_TEXT, orderDetails);
        startActivity(Intent.createChooser(sendingEmailIntent, "Send Email"));
    }

    /**
     * Prompt the user to confirm that they want to delete this product.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this product?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
                deleteProduct();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the product.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void deleteProduct() {

        Uri currentProductUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, mProductId);
        Log.i(LOG_TAG, " currentProductUri: " + currentProductUri.toString());

        String selection = ProductContract.ProductEntry._ID + "=?";
        String[] selectionArgs = {String.valueOf(mProductId)};

        int rowsDeleted = getContentResolver().delete(ProductContract.ProductEntry.CONTENT_URI, selection, selectionArgs);
        Log.i(LOG_TAG, rowsDeleted + " row deleted from product database");

        Intent productListIntent = new Intent(ProductDetailActivity.this, ProductListActivity.class);
        startActivity(productListIntent);

    }

}

