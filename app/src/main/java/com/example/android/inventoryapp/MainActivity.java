package com.example.android.inventoryapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.add_product_button) Button mAddProductButton;
    @InjectView(R.id.list_products_button) Button mListProductsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mAddProductButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent productListIntent = new Intent(MainActivity.this, ProductDetailActivity.class);
                startActivity(productListIntent);
            }
        });

        mListProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent productListIntent = new Intent(MainActivity.this, ProductListActivity.class);
                startActivity(productListIntent);
            }
        });
    }
}
