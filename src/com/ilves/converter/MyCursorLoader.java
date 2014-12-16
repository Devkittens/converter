package com.ilves.converter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

public class MyCursorLoader extends CursorLoader {
	
	public MyCursorLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public MyCursorLoader(Context context, Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		super(context, uri, projection, selection, selectionArgs, sortOrder);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Cursor loadInBackground() {
		// TODO Auto-generated method stub
		return super.loadInBackground();
	}
	
}
