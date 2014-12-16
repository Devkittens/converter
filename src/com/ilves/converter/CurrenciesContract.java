package com.ilves.converter;

import android.provider.BaseColumns;

public class CurrenciesContract {

	public CurrenciesContract() {
		// TODO Auto-generated constructor stub
	}
	/* Inner class that defines the table contents */
    public static abstract class CurrenciesEntry implements BaseColumns {
        public static final String TABLE_NAME = "currencies";
        public static final String COLUMN_NAME_NAME = "c_name";
        
    }
}
