package com.ilves.converter;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ilves.converter.CurrenciesContract.CurrenciesEntry;
import com.ilves.converter.RatesContract.RatesEntry;
import com.ilves.converter.parsers.CurrencyXmlParser.Entry;

public class MySQLiteHelper extends SQLiteOpenHelper {

	// If you are generally doing updates I would ..
	//
	// Begin a transaction
	// Do the update
	// Check the rowcount
	// If it is 0 do the insert
	// Commit
	
	private static final String NAME_TYPE = " CHARACTER(3) UNIQUE NOT NULL";
	private static final String INT_TYPE = " INT NOT NULL";
	private static final String DOUBLE_TYPE = " DOUBLE NOT NULL";
	private static final String DATETIME_TYPE = " DATETIME NOT NULL";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES_CURRENCIES =
		    "CREATE TABLE " + CurrenciesEntry.TABLE_NAME + " (" +
		    CurrenciesEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
		    CurrenciesEntry.COLUMN_NAME_NAME + NAME_TYPE +
		     // Any other options for the CREATE command
		    " )";
	private static final String SQL_CREATE_ENTRIES_RATES =
		    "CREATE TABLE " + RatesEntry.TABLE_NAME + " (" +
    		RatesEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
    		RatesEntry.COLUMN_NAME_FROM + INT_TYPE + COMMA_SEP +
    		RatesEntry.COLUMN_NAME_TO + INT_TYPE + COMMA_SEP +
    		RatesEntry.COLUMN_NAME_BID + DOUBLE_TYPE + COMMA_SEP +
    		RatesEntry.COLUMN_NAME_ASK + DOUBLE_TYPE + COMMA_SEP +
    		RatesEntry.COLUMN_NAME_HIGH + DOUBLE_TYPE + COMMA_SEP +
    		RatesEntry.COLUMN_NAME_LOW + DOUBLE_TYPE + COMMA_SEP +
    		RatesEntry.COLUMN_NAME_DIRECTION + INT_TYPE + COMMA_SEP +
    		RatesEntry.COLUMN_NAME_LAST + DATETIME_TYPE +
		     // Any other options for the CREATE command
		    " )";


	private static final String	DATABASE_NAME				= "converter.db";
	private static final int	DATABASE_VERSION			= 1;

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES_CURRENCIES);
		db.execSQL(SQL_CREATE_ENTRIES_RATES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(this.getClass().getName(), "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + CurrenciesEntry.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + RatesEntry.TABLE_NAME);
		onCreate(db);
	}
	
	public void insertRates(SQLiteDatabase db, List<Entry> entries) {
		
	}

}
