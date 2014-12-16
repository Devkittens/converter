package com.ilves.converter;

import java.text.DecimalFormat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ilves.converter.asyncs.ASyncCurrencyLoader;

public class MainActivity extends ActionBarActivity implements OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
	
	private final String		CURRENCY_URL		= "http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote";
	private int					random_price		= 330;
	private float				euro_conv			= 1.25f;
	private float				div					= 0.6461f;
	private float				my_price			= 0;
	private float				middle				= 0;
	private float				sum					= 0;
	private Spinner				spinner_currencies;
	private Spinner				spinner_dimensions;
	private EditText			edittext_mbf;
	private EditText			edittext_currency;
	private TextView			tv_currency_symbol;
	private Boolean				flag_is_my_price	= true;
	private TextView			textview_middle;
	private DecimalFormat		f;
	// Identifies a particular Loader being used in this component
	private static final int	URL_LOADER			= 0;
	private SimpleCursorAdapter mAdapter;

	public String[]				mFromColumns		= { RatesContract.RatesEntry._ID,RatesContract.RatesEntry.COLUMN_NAME_FROM,
			RatesContract.RatesEntry.COLUMN_NAME_TO,
			RatesContract.RatesEntry.COLUMN_NAME_ASK,
			RatesContract.RatesEntry.COLUMN_NAME_BID };
	public int[]				mToFields			= { 0,R.id.list_item_from, R.id.list_item_to, R.id.list_item_ask, R.id.list_item_bid };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		f = new DecimalFormat("####.00");
		
		// MySQLiteHelper mDbHelper = new
		// MySQLiteHelper(getApplicationContext());
		// Gets the data repository in write mode
		// SQLiteDatabase db = mDbHelper.getWritableDatabase();
		getSupportLoaderManager().initLoader(URL_LOADER, null, this);
		
		// Gets a handle to a List View
		ListView mListView = (ListView) findViewById(R.id.currency_listview);
		/*
		 * Defines a SimpleCursorAdapter for the ListView
		 */
		mAdapter = new SimpleCursorAdapter(this, // Current
																		// context
				R.layout.list_item, // Layout for a single row
				null, // No Cursor yet
				mFromColumns, // Cursor columns to use
				mToFields, // Layout fields to use
				0 // No flags
		);
		// Sets the adapter for the view
		mListView.setAdapter(mAdapter);
		
		// spinner_currencies = (Spinner) findViewById(R.id.spinner_currencies);
		// spinner_currencies.setOnItemSelectedListener(this);
		// spinner_dimensions = (Spinner) findViewById(R.id.spinner_dimensions);
		// spinner_dimensions.setOnItemSelectedListener(this);
		// tv_currency_symbol = (TextView) findViewById(R.id.currency_symbol);
		// tv_currency_symbol.setText(Currency.getInstance("EUR").getSymbol());
		edittext_mbf = (EditText) findViewById(R.id.input_price_mbf);
		
		edittext_mbf.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				Log.i(this.getClass().getSimpleName(), "onFocusChange edittext_mbf");
				if (hasFocus) {
					flag_is_my_price = true;
				}
			}
		});
		edittext_mbf.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				if (edittext_mbf.hasFocus()) {
					my_price = Float.parseFloat(s.toString());
					calculateMyPriceToSum(my_price);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		edittext_currency = (EditText) findViewById(R.id.input_price_currency);
		edittext_currency.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				Log.i(this.getClass().getSimpleName(), "onFocusChange edittext_currency");
				if (hasFocus) {
					flag_is_my_price = false;
				}
			}
		});
		edittext_currency.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				if (edittext_currency.hasFocus()) {
					sum = Float.parseFloat(s.toString());
					calculateSumToMyPrice(sum);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		textview_middle = (TextView) findViewById(R.id.textview_middle);
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		// Create new currency loader
		ASyncCurrencyLoader cxp = new ASyncCurrencyLoader(this);
		cxp.execute(CURRENCY_URL);
		super.onStart();
	}
	
	protected void calculateMyPriceToSum(float my_price2) {
		// TODO Auto-generated method stub
		middle = my_price2 * div;
		Log.i(this.getClass().getSimpleName(), "" + middle);
		sum = middle / euro_conv;
		Log.i(this.getClass().getSimpleName(), "" + sum);
		// Update fields
		textview_middle.setText(f.format(middle));
		edittext_currency.setText(f.format(sum));
	}
	
	protected void calculateSumToMyPrice(float sum2) {
		// TODO Auto-generated method stub
		middle = sum2 * euro_conv;
		Log.i(this.getClass().getSimpleName(), "" + middle);
		my_price = middle / div;
		Log.i(this.getClass().getSimpleName(), "" + my_price);
		// Update fields
		textview_middle.setText(f.format(middle));
		edittext_mbf.setText(f.format(my_price));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * Spinner changes
	 */
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		int spinner = parent.getId();
		switch (spinner) {
		case R.id.spinner_currencies:
			Log.i(this.getClass().getSimpleName(),
					"spinner_currencies "
							+ (getResources().getStringArray(R.array.currencies_array))[position]);
			break;
		case R.id.spinner_dimensions:
			Log.i(this.getClass().getSimpleName(),
					"spinner_dimensions "
							+ (getResources().getStringArray(R.array.dimensions_array))[position]);
			break;
		
		default:
			break;
		}
		
	}
	
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
		Log.i(getClass().getSimpleName(), "Loader ready!");
		// TODO Auto-generated method stub
		return new CursorLoader( getApplicationContext(), null, mFromColumns, null, null, null )
        {
            @Override
            public Cursor loadInBackground()
            {
                // You better know how to get your database.

        		MySQLiteHelper mDbHelper = new MySQLiteHelper(getApplicationContext());
        		// Gets the data repository in write mode
        		SQLiteDatabase db = mDbHelper.getWritableDatabase();
                // You can use any query that returns a cursor.
                return db.query( RatesContract.RatesEntry.TABLE_NAME, getProjection(), getSelection(), getSelectionArgs(), null, null, getSortOrder(), null );
            }
        };
		/*
		 * Takes action based on the ID of the Loader that's being created
		 */
        /*
		switch (loaderID) {
		case URL_LOADER:
			// Returns a new CursorLoader
			/*
			 * return new CursorLoader( getActivity(), // Parent activity
			 * context mDataUrl, // Table to query mProjection, // Projection to
			 * return null, // No selection clause null, // No selection
			 * arguments null // Default sort order );
			 */
        /*
		default:
			// An invalid id was passed in
			return null;
		}
		*/
		// return null;
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO Auto-generated method stub
		/*
	     * Moves the query results into the adapter, causing the
	     * ListView fronting this adapter to re-display
	     */
	    mAdapter.changeCursor(cursor);
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub

	    /*
	     * Clears out the adapter's reference to the Cursor.
	     * This prevents memory leaks.
	     */
	    mAdapter.changeCursor(null);
	}
	
	public void reload() {
		getSupportLoaderManager().restartLoader(URL_LOADER, null, this);
	}
}
