package com.valfom.skydiving.altimeter;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class AltimeterContentProvider extends ContentProvider {

	public static final String AUTHORITY = "com.valfom.skydiving.altimeter.provider";

	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ AUTHORITY + "/" + AltimeterDB.TABLE_TRACKS);

	private static final int URI_TRACKS = 1;
	private static final int URI_TRACK_ID = 2;

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, AltimeterDB.TABLE_TRACKS, URI_TRACKS);
		uriMatcher.addURI(AUTHORITY, AltimeterDB.TABLE_TRACKS + "/#", URI_TRACK_ID);
	}

	AltimeterDB db;

	public boolean onCreate() {

		db = new AltimeterDB(getContext());

		return true;
	}

	public String getType(Uri uri) {

		return null;
	}

	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		Cursor cursor = null;
		
		if (uriMatcher.match(uri) == URI_TRACKS)
			cursor = db.getAllTracks();
		else
			throw new IllegalArgumentException("Wrong URI: " + uri);
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}

	public int delete(Uri uri, String selection, String[] selectionArgs) {

		Log.d("LALA", "delete, uri" + uri.toString());
		
		int rowsDeleted = 0;
		
		if (uriMatcher.match(uri) == URI_TRACK_ID) {
		
			Log.d("LALA", "id " + uri.getLastPathSegment());
			
			String id = uri.getLastPathSegment();
			
			rowsDeleted = db.deleteTrack(Integer.valueOf(id));
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return rowsDeleted;
	}

	public Uri insert(Uri uri, ContentValues values) {

		return null;
	}

	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		return 0;
	}
}
