package com.exemple.android.inventoryapp;



/**
 * Created by adm on 17/07/2017.
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventoryapp.R;
import com.exemple.android.inventoryapp.data.ProductContract.ProductEntry;
import com.exemple.android.inventoryapp.data.ProductDbHelper;
/**
 * Displays list of products that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int PRODUCT_LOADER = 0;

    ProductCursorAdapter mCursorAdapter;
    /** Database helper that will provide us access to the database */
    private ProductDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the product data
        ListView productListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        //mDbHelper = new ProductDbHelper(this);

        //displayDatabaseInfo(); replaced by cursorloader

        //Setup an Adapter to create a list item for each row of product data in the Cursor
        //There is no product data yet (until the loader finishes) so pass in null for the cursor
        mCursorAdapter = new ProductCursorAdapter(this,null);
        productListView.setAdapter(mCursorAdapter);

        //setup item listener
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(CatalogActivity.this,EditorActivity.class);
                //create uri with ID clicked
                Uri currentProductUri = ContentUris.withAppendedId(ProductEntry.CONTENT_URI, id);
                //Set the URI on the data field of the intent
                intent.setData(currentProductUri);

                //Launch the {@link EditorActivity } to display  the data for the current product.
                startActivity(intent);
            }
        });

        //kick off the loader
        getSupportLoaderManager().initLoader(PRODUCT_LOADER,null,this);
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        displayDatabaseInfo();
//    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the products database.
     */
    /*private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        //ProductDbHelper mDbHelper = new ProductDbHelper(this);

        // Create and/or open a database to read from it
        //SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM products"
        // to get a Cursor that contains all rows from the products table.
        //Cursor cursor = db.rawQuery("SELECT * FROM " + ProductEntry.TABLE_NAME, null);

        String[] projection = {
          ProductEntry._ID,
          ProductEntry.COLUMN_PRODUCT_NAME,
          ProductEntry.COLUMN_PRODUCT_BREED,
          ProductEntry.COLUMN_PRODUCT_GENDER,
          ProductEntry.COLUMN_PRODUCT_WEIGHT
        };

        *//*Cursor cursor = db.query(
                ProductEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);*//*

        Cursor cursor = getContentResolver().query(ProductEntry.CONTENT_URI,projection,null,null,null);

        // Find the ListView which will be populated with the product data
        ListView productListView = (ListView) findViewById(R.id.list);

        // Setup an Adapter to create a list item for each row of product data in the Cursor.
        ProductCursorAdapter adapter = new ProductCursorAdapter(this, cursor);

        // Attach the adapter to the ListView.
        productListView.setAdapter(adapter);

        *//*TextView displayView = (TextView) findViewById(R.id.text_view_product);
        try {
            // Create a header in the Text View that looks like this:
            //
            // The products table contains <number of rows in Cursor> products.
            // _id - name - breed - gender - weight
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The products table contains " + cursor.getCount() + " products.\n\n");
            displayView.append(ProductEntry._ID + " - " +
                    ProductEntry.COLUMN_PRODUCT_NAME + " - " +
                    ProductEntry.COLUMN_PRODUCT_BREED + " - " +
                    ProductEntry.COLUMN_PRODUCT_GENDER + " - " +
                    ProductEntry.COLUMN_PRODUCT_WEIGHT + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(ProductEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_NAME);
            int breedColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_BREED);
            int genderColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRODUCT_WEIGHT);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBreed = cursor.getString(breedColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentWeight = cursor.getInt(weightColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentBreed + " - " +
                        currentGender + " - " +
                        currentWeight));
            }
        }finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }*//*
    }*/
    /**
     * Helper method to insert hardcoded product data into the database. For debugging purposes only.
     */
    private void insertProduct() {
        // Gets the database in write mode
        //SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and Toto's product attributes are the values.
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_PRODUCT_NAME, "Toto");
        values.put(ProductEntry.COLUMN_PRODUCT_BREED, "Terrier");
        values.put(ProductEntry.COLUMN_PRODUCT_GENDER, ProductEntry.GENDER_MALE);
        values.put(ProductEntry.COLUMN_PRODUCT_WEIGHT, 7);

        // Insert a new row for Toto in the database, returning the ID of that new row.
        // The first argument for db.insert() is the products table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for Toto.

        // Insert a new row for Toto into the provider using the ContentResolver.
        // Use the {@link ProductEntry#CONTENT_URI} to indicate that we want to insert
        // into the products database table.
        // Receive the new content URI that will allow us to access Toto's data in the future.
        Uri newUri = getContentResolver().insert(ProductEntry.CONTENT_URI, values);


        //long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);
        //Log.v("CatalogActivity", "New Row ID ", + newRowId);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // call method to insert dummy data
                insertProduct();
                //displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deleteAllProducts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //define projection with column that we need
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_BREED  };

        // This loader will execute ContentProvider query method on a background thread
        return new CursorLoader(this, //Parent activity context
                ProductEntry.CONTENT_URI, //Provider content URI to query
                projection,//Columns to include in cursor result
                null,//no selection clause
                null,//no argument
                null);//default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //update {@link ProductCursorAdapter} with this new cursor containing updated product data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //Callbakc when data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
    /**
     * Helper method to delete all products in the database.
     */
    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from product database");
    }
}