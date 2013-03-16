package com.valfom.skydiving.altimeter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class AltimeterDB extends SQLiteOpenHelper {
 
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "altimeter";
    
    public static final String TABLE_TRACKS = "tracks";
    public static final String TABLE_POINTS = "points";
 
    // "tracks" table
    public static final String KEY_TRACKS_ID = "_id";
    public static final String KEY_TRACKS_DATE = "date";
    public static final String KEY_TRACKS_TIME = "time";
    public static final String KEY_TRACKS_TYPE = "type";
    
    // "points" table
    public static final String KEY_POINTS_ID = "_id";
    public static final String KEY_POINTS_DATE = "date";
    public static final String KEY_POINTS_ALTITUDE = "altitude";
    public static final String KEY_POINTS_SPEED = "speed";
    public static final String KEY_POINTS_TRACK_ID = "track_id";
 
    public AltimeterDB(Context context) {
    	
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
    	
        String CREATE_TRACKS_TABLE = "CREATE TABLE " + TABLE_TRACKS + "("
        	+ KEY_TRACKS_ID + " INTEGER PRIMARY KEY,"
        	+ KEY_TRACKS_DATE + " TEXT,"
        	+ KEY_TRACKS_TIME + " TEXT,"
        	+ KEY_TRACKS_TYPE + " INTEGER" + ")";
        
        db.execSQL(CREATE_TRACKS_TABLE);
        
        String CREATE_POINTS_TABLE = "CREATE TABLE " + TABLE_POINTS + "("
        		+ KEY_POINTS_ID + " INTEGER PRIMARY KEY,"
        		+ KEY_POINTS_DATE + " TEXT,"
            	+ KEY_POINTS_ALTITUDE + " INTEGER,"
            	+ KEY_POINTS_SPEED + " INTEGER,"
                + KEY_POINTS_TRACK_ID + " INTEGER" + ")";
            
        db.execSQL(CREATE_POINTS_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POINTS);
 
        onCreate(db);
    }
 
    public Cursor getTrack(int id) {
    	
        SQLiteDatabase db = this.getReadableDatabase();
 
        String[] columns = new String[] { KEY_TRACKS_DATE, KEY_TRACKS_TIME, KEY_TRACKS_TYPE };
        String[] selectionArgs = new String[] { String.valueOf(id) };
        
        Cursor cursor = db.query(TABLE_TRACKS, columns, KEY_TRACKS_ID + "=?",
        		selectionArgs, null, null, null, null);
        
        return cursor;
    }
 
    public Cursor getAllTracks() {
    	
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = new String[] { KEY_TRACKS_ID, KEY_TRACKS_DATE, KEY_TRACKS_TIME, KEY_TRACKS_TYPE };
        
        Cursor cursor = db.query(TABLE_TRACKS, columns, null, null, null, null, null);
        
        return cursor;
    }
}