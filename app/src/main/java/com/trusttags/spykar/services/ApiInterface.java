package com.trusttags.spykar.services;

import com.trusttags.spykar.BuildConfig;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/*import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;*/

public interface ApiInterface {


   /* @POST("companylogin")
    Observable<ResponseBody> loginAuth(@Body RequestBody request);*/

  @POST("mappinguserlogin")
  Observable<ResponseBody> loginAuth(@Body RequestBody request);


  @POST("map/codes")
  Observable<ResponseBody> mapCodes(@Body RequestBody request);

  @POST("map/activate/code")
  Observable<ResponseBody> mapCodesPepe(@Body RequestBody request);

  @POST("map/retag/code")
  Observable<ResponseBody> reMapCodes(@Body RequestBody request);

    @POST("storeLogin")
    Observable<ResponseBody> loginPantaloonsAuth(@Body RequestBody request);

  @POST("store/map")
  Observable<ResponseBody> mapPantaloonsCodes(@Body RequestBody request);

  @POST("store/remap")
  Observable<ResponseBody> reMapPantaloonsCodes(@Body RequestBody request);


}
