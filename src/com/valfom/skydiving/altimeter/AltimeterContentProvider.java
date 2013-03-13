package com.valfom.skydiving.altimeter;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class AltimeterContentProvider extends ContentProvider {

	public static final String PROVIDER_NAME = "com.valfom.skydiving.altimeter.provider";

	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/" + AltimeterDB.TABLE_TRACKS);

	private static final int TRACKS = 1;

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, AltimeterDB.TABLE_TRACKS, TRACKS);
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

		if (uriMatcher.match(uri) == TRACKS)
			return db.getAllTracks();
		else
			return null;
	}

	public int delete(Uri uri, String selection, String[] selectionArgs) {

		return 0;
	}

	public Uri insert(Uri uri, ContentValues values) {

		return null;
	}

	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		return 0;
	}
}
