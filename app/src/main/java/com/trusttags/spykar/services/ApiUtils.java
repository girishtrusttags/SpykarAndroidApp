package com.trusttags.spykar.services;

import android.content.Context;

/**
 * Created by Girish1.patel on 2/25/2019.
 */

public class ApiUtils {

    public static ApiInterface getAPIServiceByRx(Context mContext){
        return new RetrofitClient().getClientbyRx(mContext).create(ApiInterface.class);
    }
}
