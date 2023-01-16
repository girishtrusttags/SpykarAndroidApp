package com.trusttags.spykar.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceConnector {
	public static final int MODE = 0;
	public static final String PREF_NAME = "WS_RND_PREFERENCES";
	public static final String PREF_NAME_REMEMBER = "WS_RND_PREFERENCES";
	
	public static final String __ci_last_regenerate = "__ci_last_regenerate";
	public static final String identity = "identity";
	public static final String EMAIL_ID = "username";
	public static final String X_KEY = "x_key";
	public static final String ACCESS_TOKEN = "access_token";
	public static final String MAPPED_CODE = "MAPPED_CODE";


	public static SharedPreferences.Editor getEditor(Context paramContext) {
		return getPreferences(paramContext).edit();
	}

	public static SharedPreferences getPreferences(Context paramContext) {
		return paramContext.getSharedPreferences(PREF_NAME, 0);
	}

	public static boolean readBoolean(Context paramContext, String paramString,
                                      boolean paramBoolean) {
		return getPreferences(paramContext).getBoolean(paramString,
				paramBoolean);
	}

	public static int readInteger(Context paramContext, String paramString,
                                  int paramInt) {
		return getPreferences(paramContext).getInt(paramString, paramInt);
	}

	public static long readLong(Context paramContext, String paramString,
                                long paramLong) {
		return getPreferences(paramContext).getLong(paramString, paramLong);
	}

	public static String readString(Context paramContext, String paramString1,
                                    String paramString2) {
		return getPreferences(paramContext).getString(paramString1,
				paramString2);
	}

	public static void writeBoolean(Context paramContext, String paramString,
                                    boolean paramBoolean) {
		getEditor(paramContext).putBoolean(paramString, paramBoolean).commit();
	}

	public static void writeInteger(Context paramContext, String paramString,
                                    int paramInt) {
		getEditor(paramContext).putInt(paramString, paramInt).commit();
	}

	public static void writeLong(Context paramContext, String paramString,
                                 long paramLong) {
		getEditor(paramContext).putLong(paramString, paramLong).commit();
	}

	public static void writeString(Context paramContext, String paramString1,
                                   String paramString2) {
		getEditor(paramContext).putString(paramString1, paramString2).commit();
	}
}
