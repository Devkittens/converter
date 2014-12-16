package com.ilves.converter;

import android.provider.BaseColumns;

public class RatesContract {

	public RatesContract() {
		// TODO Auto-generated constructor stub
	}

	/* Inner class that defines the table contents */
	public static abstract class RatesEntry implements
			BaseColumns {
		public static final String	TABLE_NAME				= "rates";
		public static final String	COLUMN_NAME_FROM		= "r_from";
		public static final String	COLUMN_NAME_TO			= "r_to";
		public static final String	COLUMN_NAME_ASK			= "r_ask";
		public static final String	COLUMN_NAME_BID			= "r_bid";
		public static final String	COLUMN_NAME_HIGH		= "r_high";
		public static final String	COLUMN_NAME_LOW			= "r_low";
		public static final String	COLUMN_NAME_DIRECTION	= "r_direction";
		public static final String	COLUMN_NAME_LAST		= "r_last";

	}
}
