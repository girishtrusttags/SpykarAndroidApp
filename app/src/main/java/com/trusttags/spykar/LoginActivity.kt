package com.trusttags.spykar

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.trusttags.spykar.BuildConfig.*
import com.trusttags.spykar.services.ApiUtils
import com.trusttags.spykar.utils.PreferenceConnector
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class LoginActivity : BaseActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        if (FLAVOR == "spykar"){
            imgHeaderIcon.setImageResource(R.drawable.spykar_header_logo)
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
        }else if (FLAVOR == "pepe"){
            imgHeaderIcon.setImageResource(R.drawable.pepe_header_logo)
            imgHeaderIcon.setBackgroundColor(Color.parseColor("#ffffff"));
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }else if (FLAVOR == "pantaloons"){
            imgHeaderIcon.setImageResource(R.drawable.pantaloons_header_logo)
            imgHeaderIcon.setBackgroundColor(Color.parseColor("#ffffff"));
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }else if (FLAVOR == "biba"){
            imgHeaderIcon.setImageResource(R.drawable.biba_header_logo)
            imgHeaderIcon.setBackgroundColor(Color.parseColor("#ffffff"));
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }else if (FLAVOR == "clovia"){
            imgHeaderIcon.setImageResource(R.drawable.clovia_header_logo)
            imgHeaderIcon.setBackgroundColor(Color.parseColor("#ffffff"));
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        }


//       edtUserName.setText("newemail@trusttags.in")
//       edtPassword.setText("Trusttags1@")
        imgLogin.setOnClickListener {
            PreferenceConnector.writeString(this@LoginActivity, PreferenceConnector.EMAIL_ID, "")
            PreferenceConnector.writeString(this@LoginActivity, PreferenceConnector.X_KEY, "")
            PreferenceConnector.writeString(
                this@LoginActivity,
                PreferenceConnector.ACCESS_TOKEN,
                ""
            )

            if(validateFields()){
                loginAuth(edtUserName.text.toString(), edtPassword.text.toString())
            }
        }


//        edtUserName.setText("store2user@trusttags.in")
//        edtPassword.setText("123456")

//        edtUserName.setText("chaitali@trusttags.in")
//        edtPassword.setText("Trusttags1@")

//        edtUserName.setText("factory@trusttags.in")
//        edtPassword.setText("123456")
//        Pepe Live- Activate Code Login Credentials
//                Email - mapping@trusttags.in
//        Password - Trusttags1@

//        edtUserName.setText("mapping@trusttags.in")
//        edtPassword.setText("Trusttags1@")

    }


    private fun validateFields():Boolean{
        var isValid = true;
        if(edtUserName.text.toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(edtUserName.text.toString()).matches()){
            Toast.makeText(this, "Enter email id", Toast.LENGTH_SHORT).show()
            isValid = false;
        }else if(edtPassword.text.toString().isEmpty()){
            Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
            isValid = false;
        }
        return isValid
    }

    private fun loginAuth(strEmail: String, strPass: String){

        val base64DataJsonStringToken = JSONObject()
        base64DataJsonStringToken.put("email", strEmail)
        base64DataJsonStringToken.put("password", strPass)
        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = base64DataJsonStringToken.toString().toRequestBody(jsonMediaType)


        val apiService = ApiUtils.getAPIServiceByRx(this)

        val reqObserver : Observable<ResponseBody>
        if(FLAVOR == "pantaloons") {
            reqObserver = apiService.loginPantaloonsAuth(requestBody)
        }else{
            reqObserver = apiService.loginAuth(requestBody)
        }


        showProgressDialog("Please wait")
        reqObserver
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(responseModel: ResponseBody) {
                    dismissProgressDialog()
//                    listenerResponse.onSuccess(responseModel)
                    Log.v("Login", " Login response " + responseModel.toString())


                    //                List<Movie> movies = response.body().getResults();
//                    Log.d("", "Number of movies received: ");
                    try {
                        val jsonResponse = JSONObject(responseModel.string().toString())
                        if (jsonResponse.optString("success").equals("1")) {
                            val jsonData = jsonResponse.optJSONObject("data")
                            PreferenceConnector.writeString(
                                this@LoginActivity,
                                PreferenceConnector.EMAIL_ID,
                                jsonData.optString(
                                    "email"
                                )
                            )
                            PreferenceConnector.writeString(
                                this@LoginActivity, PreferenceConnector.X_KEY, jsonData.optString(
                                    "random_id"
                                )
                            )
                            PreferenceConnector.writeString(
                                this@LoginActivity,
                                PreferenceConnector.ACCESS_TOKEN,
                                jsonResponse.optString(
                                    "access_token"
                                )
                            )
                            PreferenceConnector.writeInteger(
                                this@LoginActivity,
                                PreferenceConnector.MAPPED_CODE,
                                0
                            )
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)

                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                jsonResponse.optString("message").toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Username or password invalid",
                            Toast.LENGTH_SHORT
                        ).show()
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@LoginActivity,
                        "Server error",
                        Toast.LENGTH_SHORT
                    ).show()
                    dismissProgressDialog()
//                    listenerResponse.onError(e.message.toString())
                }

                override fun onComplete() {
                    dismissProgressDialog()
                }

            })
    }
}