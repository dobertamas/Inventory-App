package com.example.android.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class ProductCursorAdapter extends CursorAdapter {
    public ProductCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0 /* int flags */);
    }

    @Override public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return null;
    }

    @Override public void bindView(View view, Context context, Cursor cursor) {

    }
}
