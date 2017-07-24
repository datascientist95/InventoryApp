package com.exemple.android.inventoryapp;

/**
 * Created by adm on 17/07/2017.
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.exemple.android.inventoryapp.data.ProductContract;
import com.exemple.android.inventoryapp.data.ProductContract.ProductEntry;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;
import static com.exemple.android.inventoryapp.R.id.price;

/**
 * {@link ProductCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of product data as its data source. This adapter knows
 * how to create list items for each row of product data in the {@link Cursor}.
 */
public class ProductCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link ProductCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the product data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current product can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view,final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView textpriceTextView = (TextView) view.findViewById(R.id.text_price);
        TextView quantityTextView = (TextView) view.findViewById(R.id.quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        ImageButton buttonSale = (ImageButton) view.findViewById(R.id.buy);

        // Find the columns of product attributes that we're interested in
        final int productId = cursor.getInt(cursor.getColumnIndex(ProductContract.ProductEntry._ID));
        int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_PRICE);

        // Read the product attributes from the Cursor for the current product
        String productName = cursor.getString(nameColumnIndex);
        final int productQuantity = cursor.getInt(quantityColumnIndex);
        Double productPrice = cursor.getDouble(priceColumnIndex);

        // Update the TextViews with the attributes for the current product
        nameTextView.setText(productName);
        textpriceTextView.setText(R.string.list_price);
        //summaryTextView.setText(productQuantity);
        quantityTextView.setText(Integer.toString(productQuantity));
        priceTextView.setText(Double.toString(productPrice));

        // Bind buy event to list item button so quantity is reduced with each sale
        buttonSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri productUri = ContentUris.withAppendedId(ProductContract.ProductEntry.CONTENT_URI, productId);
                reduceQuantity(context, productUri, productQuantity);
            }
        });
    }
    /**
     * This method reduced product quantity by 1
     * @param context - Activity context
     * @param productUri - Uri used to update the stock of a specific product in the ListView
     * @param currentQuantity - current stock of that specific product
     */
    private void reduceQuantity(Context context, Uri productUri, int currentQuantity) {

        // Reduce stock, check if new stock is less than 0, in which case set it to 0
        int newStock = (currentQuantity >= 1) ? currentQuantity - 1 : 0;

        // Update table with new stock of the product
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_QUANTITY, newStock);
        int numRowsUpdated = context.getContentResolver().update(productUri, contentValues, null, null);

//        // Display error message in Log if product stock fails to update
//        if (!(numRowsUpdated > 0)) {
//            Log.e(TAG, context.getString(R.string.error_quantity_update));
//        }
    }
}