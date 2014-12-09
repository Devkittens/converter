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

import android.os.AsyncTask;
import android.util.Log;

import com.ilves.converter.parsers.CurrencyXmlParser;

/**
 * @author Seb
 *
 */
public class ASyncCurrencyLoader extends AsyncTask<String, Boolean, String> {
	
	//private final String CURRENCY_URL = "http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote";
	private final String CURRENCY_URL_FXCM = "http://rates.fxcm.com/RatesXML";
	

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
        //setContentView(R.layout.main);
        // Displays the HTML string in the UI via a WebView
        //WebView myWebView = (WebView) findViewById(R.id.webview);
        //myWebView.loadData(result, "text/html", null);
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
        
        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
        for (CurrencyXmlParser.Entry entry : entries) {    
        	Log.i("ASyncCurrencyLoader", "name: "+entry.name
        			+" value: "+entry.bid);
            //htmlString.append("<p><a href='");
            //htmlString.append(entry.link);
            //htmlString.append("'>" + entry.title + "</a></p>");
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
