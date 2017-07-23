package com.example.android.inventoryapp;

public class Product {

    /**
     * Name of the product
     */
    private String mName;

    /**
     * Non-negative quantity of the product in database - can be changed through UI.
     */
    private int mQuantity;
    /**
     * Price of the product in USD.
     */
    private double mPrice;
    /**
     * Optional image resource mProductId for the product.
     */
    private int mImageResourceId;
    /**
     * Constant value that represents no image was provided for this word
     */
    private static final int NO_IMAGE_PROVIDED = -1;

    public Product(String name, int quantity, double price, int imageResourceId) {
        mName = name;
        mQuantity = quantity;
        mPrice = price;
        mImageResourceId = imageResourceId;
    }

    @Override public String toString() {
        if ((mName != null) && (mQuantity > 0) && (mPrice > 0.0)) {
            return "Product{" +
                    "mName='" + mName + '\'' +
                    ", mQuantity=" + mQuantity +
                    ", mPrice=" + mPrice +
                    ", mImageResourceId=" + mImageResourceId +
                    '}';
        }
        else {
            return "";
        }

    }

    /**
     * Returns whether or not there is an image for this product.
     */
    boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

}
