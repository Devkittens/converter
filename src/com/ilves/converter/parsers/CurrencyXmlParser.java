package com.ilves.converter.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class CurrencyXmlParser {

	// We don't use namespaces
	private static final String	ns	= null;

	public static class Entry {
		public final String		name;
		public final Double		bid;
		public final Double		ask;
		public final Double		high;
		public final Double		low;
		public final Integer	direction;
		public final String		last;

		private Entry(String name, Double bid, Double ask, Double high, Double low,
				Integer direction, String last) {
			this.name = name;
			this.bid = bid;
			this.ask = ask;
			this.high = high;
			this.low = low;
			this.direction = direction;
			this.last = last;
		}
	}

	public List<Entry> parse(InputStream in) throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}
	}

	private List<Entry> readFeed(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		List<Entry> entries = new ArrayList<Entry>();

		parser.require(XmlPullParser.START_TAG, ns, "Rates");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the entry tag
			if (name.equals("Rate")) {
				Entry newEntry = readEntry(parser);
				if (newEntry.name.length() == 6 && isAllUpper(newEntry.name)) {
					entries.add(newEntry);
				}
			} else {
				skip(parser);
			}
		}
		return entries;
	}

	// Parses the contents of an entry. If it encounters a title, summary, or
	// link tag, hands them off
	// to their respective "read" methods for processing. Otherwise, skips the
	// tag.
	private Entry readEntry(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		parser.require(XmlPullParser.START_TAG, ns, "Rate");
		String name = parser.getAttributeValue(null, "Symbol");
		Double bid = 0d;
		Double ask = 0d;
		Double high = 0d;
		Double low = 0d;
		Integer direction = 0;
		String last = "";
		Log.i("CurrencyXmlParser", "name: " + name);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String xmlName = parser.getName();
			Log.i("CurrencyXmlParser", "xmlName: " + xmlName);
			if (xmlName.equals("Bid")) {
				bid = readBid(parser);
			} else if (xmlName.equals("Bid")) {
				bid = readBid(parser);
			} else if (xmlName.equals("Ask")) {
				ask = readAsk(parser);
			} else if (xmlName.equals("High")) {
				high = readHigh(parser);
			} else if (xmlName.equals("Low")) {
				low = readLow(parser);
			} else if (xmlName.equals("Direction")) {
				direction = readDirection(parser);
			} else if (xmlName.equals("Last")) {
				last = readLast(parser);
			} else {
				skip(parser);
			}
		}
		return new Entry(name, bid, ask, high, low, direction, last);
	}

	// Processes title tags in the feed.
	private Double readBid(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "Bid");
		Double bid = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "Bid");
		return bid;
	}

	// Processes title tags in the feed.
	private Double readAsk(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "Ask");
		Double ask = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "Ask");
		return ask;
	}

	// Processes title tags in the feed.
	private Double readHigh(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "High");
		Double high = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "High");
		return high;
	}

	// Processes title tags in the feed.
	private Double readLow(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "Low");
		Double low = Double.parseDouble(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "Low");
		return low;
	}

	// Processes title tags in the feed.
	private Integer readDirection(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "Direction");
		Integer direction = Integer.parseInt(readText(parser));
		parser.require(XmlPullParser.END_TAG, ns, "Direction");
		return direction;
	}

	// Processes link tags in the feed.
	private String readLast(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "Last");
		String time = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "Last");
		return time;
	}

	// For the tags title and summary, extracts their text values.
	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
	
	private static boolean isAllUpper(String s) {
	    for(char c : s.toCharArray()) {
	       if(Character.isDigit(c) || (Character.isLetter(c) && Character.isLowerCase(c))) {
	           return false;
	        }
	    }
	    return true;
	}

}
