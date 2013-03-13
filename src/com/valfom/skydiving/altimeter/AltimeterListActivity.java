package com.valfom.skydiving.altimeter;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class AltimeterListActivity extends ListActivity implements 
		LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		String[] fromColumns = { AltimeterDB.KEY_TRACKS_DATE };
        int[] toViews = { android.R.id.text1 };
		
		adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, 
				null, fromColumns, toViews, 0);
		
		setListAdapter(adapter);
		
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		return new CursorLoader(this, AltimeterContentProvider.CONTENT_URI,
                null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

		adapter.swapCursor(null);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);
	}

}
