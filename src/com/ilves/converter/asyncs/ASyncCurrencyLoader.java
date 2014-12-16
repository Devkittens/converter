/**
 * 
 */
package com.ilves.converter.asyncs;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;
import android.util.Log;

import com.ilves.converter.MainActivity;
import com.ilves.converter.MySQLiteHelper;
import com.ilves.converter.RatesContract.RatesEntry;
import com.ilves.converter.parsers.CurrencyXmlParser;

/**
 * @author Seb
 * 
 */
public class ASyncCurrencyLoader extends AsyncTask<String, Boolean, String> {
	
	// private final String CURRENCY_URL =
	// "http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote";
	private final String	CURRENCY_URL_FXCM	= "http://rates.fxcm.com/RatesXML";
	private MainActivity			context;
	
	public ASyncCurrencyLoader(MainActivity context) {
		this.context = context;
	}
	
	@Override
	protected String doInBackground(String... urls) {
		try {
			return loadXmlFromNetwork(CURRENCY_URL_FXCM);
		} catch (XmlPullParserException e) {
			Log.i(this.getClass().getSimpleName(), "doInBackground XmlPullParserException");
			e.printStackTrace();
			return "XmlPullParserException";
		} catch (IOException e) {
			Log.i(this.getClass().getSimpleName(), "doInBackground IOException");
			e.printStackTrace();
			return "IOException";
		}
	}
	
	@Override
	protected void onPostExecute(String result) {
		// setContentView(R.layout.main);
		// Displays the HTML string in the UI via a WebView
		// WebView myWebView = (WebView) findViewById(R.id.webview);
		// myWebView.loadData(result, "text/html", null);
		context.reload();
	}
	
	private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
		InputStream stream = null;
		// Instantiate the parser
		CurrencyXmlParser currencyXmlParser = new CurrencyXmlParser();
		List<CurrencyXmlParser.Entry> entries = null;
		
		try {
			stream = readFromServer(urlString);
			entries = currencyXmlParser.parse(stream);
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		
		// StackOverflowXmlParser returns a List (called "entries") of Entry
		// objects.
		// Each Entry object represents a single post in the XML feed.
		// This section processes the entries list to combine each entry with
		// HTML markup.
		// Each entry is displayed in the UI as a link that optionally includes
		// a text summary.
		
		// Add items to db
		MySQLiteHelper mDbHelper = new MySQLiteHelper(this.context);
		// Gets the data repository in write mode
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		for (int i = 0; i < entries.size(); i++) {
			CurrencyXmlParser.Entry entry = entries.get(i);
			Log.i("ASyncCurrencyLoader", "from: " + entry.from + " to: " + entry.to + " ask: " + entry.ask
					+ " bid: " + entry.bid);
			
			// Create a new map of values, where column names are the keys
			ContentValues values = new ContentValues();
			values.put(RatesEntry._ID, i);
			values.put(RatesEntry.COLUMN_NAME_FROM, entry.from);
			values.put(RatesEntry.COLUMN_NAME_TO, entry.to);
			values.put(RatesEntry.COLUMN_NAME_BID, entry.bid);
			values.put(RatesEntry.COLUMN_NAME_ASK, entry.ask);
			values.put(RatesEntry.COLUMN_NAME_HIGH, entry.high);
			values.put(RatesEntry.COLUMN_NAME_LOW, entry.low);
			values.put(RatesEntry.COLUMN_NAME_DIRECTION, entry.direction);
			values.put(RatesEntry.COLUMN_NAME_LAST, entry.last);
			
			// Insert the new row, returning the primary key value of the new
			// row
			long newRowId;
			newRowId = db.insertWithOnConflict(RatesEntry.TABLE_NAME,
					null,
					values,
					SQLiteDatabase.CONFLICT_REPLACE);
			Log.i(getClass().getSimpleName(), "newRowId: "+newRowId);
			
			// htmlString.append("<p><a href='");
			// htmlString.append(entry.link);
			// htmlString.append("'>" + entry.title + "</a></p>");
			// If the user set the preference to include summary text,
			// adds it to the display.
			
		}
		
		return "Currencies fetched!";
	}
	
	public InputStream readFromServer(String url_string) {
		URL url;
		try {
			url = new URL(url_string);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			return conn.getInputStream();
		} catch (MalformedURLException e) {
			Log.i(this.getClass().getSimpleName(), "readFromServer MalformedURLException");
			e.printStackTrace();
		} catch (ProtocolException e) {
			Log.i(this.getClass().getSimpleName(), "readFromServer ProtocolException");
			e.printStackTrace();
		} catch (IOException e) {
			Log.i(this.getClass().getSimpleName(), "readFromServer IOException");
			e.printStackTrace();
		}
		return null;
	}
	
}
