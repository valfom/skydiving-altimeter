package com.valfom.skydiving.altimeter;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.ListView;

public class AltimeterListActivity extends ListActivity implements 
		LoaderManager.LoaderCallbacks<Cursor> {

	private AltimeterSimpleCursorAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		String[] fromColumns = { AltimeterDB.KEY_TRACKS_DATE };
        int[] toViews = { R.id.tvTime };
        
        adapter = new AltimeterSimpleCursorAdapter(this, R.layout.list_row, 
				null, fromColumns, toViews, 0);
        
		setListAdapter(adapter);
		
		getLoaderManager().initLoader(0, null, this);
		
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		getListView().setMultiChoiceModeListener(new MultiChoiceModeListener() {

		    public void onItemCheckedStateChanged(ActionMode mode, 
		    		int position, long id, boolean checked) {}
	
		    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	
		    	switch (item.getItemId()) {
		    	
		        case R.id.menu_row_delete:
		        	
	                long[] checkedItems = getListView().getCheckedItemIds();
	                
	                for (long id : checkedItems) {
	                
	                	Uri uri = Uri.parse(AltimeterContentProvider.CONTENT_URI_TRACKS + "/" + id);
	                    getContentResolver().delete(uri, null, null);
	                }

	                mode.finish();
		        	
		            return true;
		            
		        default:
		            return false;
		        }
		    }
	
		    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
	
		    	MenuInflater inflater = mode.getMenuInflater();
		        inflater.inflate(R.menu.menu_list_row_selection, menu);
		        
		        return true;
		    }
	
		    public void onDestroyActionMode(ActionMode mode) {}
	
		    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		        
		    	return false;
		    }
		});
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {

		return new CursorLoader(this, AltimeterContentProvider.CONTENT_URI_TRACKS,
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
		
		Intent info = new Intent(AltimeterListActivity.this, AltimeterInfoActivity.class);
		info.putExtra("trackId", id);
		startActivity(info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_list, menu);

		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		switch (item.getItemId()) {
        
	        case android.R.id.home:
	            
	        	onBackPressed();
	            
	            return true;
	            
	        case R.id.action_settings:
	        	
	        	Intent settings = new Intent(AltimeterListActivity.this, AltimeterPreferenceActivity.class);
	    		startActivity(settings);
	    		
	    		return true;
	        	
	        default:
	        	return super.onMenuItemSelected(featureId, item);
		}
	}
}
