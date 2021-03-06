package com.detimil.breadcrumbs1;

import java.util.ArrayList;
import java.util.List;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "breadcrumbsManager";
 
    // Contacts table name
    private static final String TABLE_BREADCRUMBS = "breadcrumbs";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BREADCRUMBS_TABLE = "CREATE TABLE " + TABLE_BREADCRUMBS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE + " TEXT" + ")";
        db.execSQL(CREATE_BREADCRUMBS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BREADCRUMBS);
 
        // Create tables again
        onCreate(db);
    }
    
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
     
    
    	// Adding new location/breadcrumb
    public void addBreadcrumb(Breadcrumb breadcrumb) {
    		SQLiteDatabase db = this.getWritableDatabase();
 
    		ContentValues values = new ContentValues();
    		values.put(KEY_LATITUDE, breadcrumb.getBreadcrumbLatitude()); // Latitude
    		values.put(KEY_LONGITUDE, breadcrumb.getBreadcrumbLongitude()); // Longitude
 
    		// Inserting Row
    		db.insert(TABLE_BREADCRUMBS, null, values);
    		db.close(); // Closing database connection
    	}
    

    	// Getting single breadcrumb
    public Breadcrumb getBreadcrumb(int id) {
    	SQLiteDatabase db = this.getReadableDatabase();
 
    	Cursor cursor = db.query(TABLE_BREADCRUMBS, new String[] { KEY_ID,
    			KEY_LATITUDE, KEY_LONGITUDE }, KEY_ID + "=?",
    			new String[] { String.valueOf(id) }, null, null, null, null);
    	if (cursor != null)
    		cursor.moveToLast();
 
    		Breadcrumb breadcrumb = new Breadcrumb(Integer.parseInt(cursor.getString(0)),
    		cursor.getInt(1), cursor.getInt(2));
    	// return breadcrumb
    	cursor.close();
    	return breadcrumb;
    }
    
    
    
    	// Getting All locations
    public List<Breadcrumb> getAllBreadcrumbs() {
	 List<Breadcrumb> breadcrumbList = new ArrayList<Breadcrumb>();
    	// Select All Query
    	String selectQuery = "SELECT  * FROM " + TABLE_BREADCRUMBS;
 
    	SQLiteDatabase db = this.getWritableDatabase();
    	Cursor cursor = db.rawQuery(selectQuery, null);
 
    	// looping through all rows and adding to list
    	if (cursor.moveToFirst()) {
        	do {
            	Breadcrumb breadcrumb = new Breadcrumb();
            	breadcrumb.setId(Integer.parseInt(cursor.getString(0)));
            	breadcrumb.setBreadcrumbLatitude(cursor.getInt(1));
            	breadcrumb.setBreadcrumbLongitude(cursor.getInt(2));
            	// Adding location to list
            	breadcrumbList.add(breadcrumb);
        	} while (cursor.moveToNext());
    	}
 
    	// return locations list
    	cursor.close();
    	return breadcrumbList;
    }
    
    // Getting breadcrumbs/locations Count
    	public int getBreadcrumbsCount() {
        	String countQuery = "SELECT  * FROM " + TABLE_BREADCRUMBS;
        	SQLiteDatabase db = this.getReadableDatabase();
        	Cursor cursor = db.rawQuery(countQuery, null);
        	cursor.close();
 
        	// return count
        	return cursor.getCount();
    	}
    	
    	
    	  // Updating single location
    	public int updateBreadcrumbs(Breadcrumb breadcrumb) {
    	    SQLiteDatabase db = this.getWritableDatabase();
    	 
    	    ContentValues values = new ContentValues();
    	    values.put(KEY_LATITUDE, breadcrumb.getBreadcrumbLatitude());
    	    values.put(KEY_LONGITUDE, breadcrumb.getBreadcrumbLongitude());
    	 
    	    // updating row
    	    return db.update(TABLE_BREADCRUMBS, values, KEY_ID + " = ?",
    	            new String[] { String.valueOf(breadcrumb.getId()) });
    	}
    	
    	// Deleting single location
    	  public void deleteBreadcrumb(Breadcrumb breadcrumb) {
      		SQLiteDatabase db = this.getWritableDatabase();
      		int id = breadcrumb.getId();
    		System.out.println("Comment deleted with id: " + id);
    		db.delete(TABLE_BREADCRUMBS, KEY_ID
    		        + " = " + id, null);
        	db.close();
    		  }
    	
}