package com.trusttags.spykar


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.barcode.Barcode

import com.trusttags.spykar.BuildConfig.*
import com.trusttags.spykar.services.ApiUtils
import com.trusttags.spykar.utils.PreferenceConnector
import info.bideens.barcode.BarcodeReader
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_scan.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException


@Suppress("DEPRECATION")
class MRPScanCodeActivity : BaseActivity(),BarcodeReader.BarcodeReaderListener {
    val TAG = MRPScanCodeActivity::class.java.simpleName

    lateinit var barcodeReader: BarcodeReader

    lateinit var extra: Bundle
    var strType : String = ""
    var strCode : String = ""

    var mrpTagCode : String = ""
    var washCareCode : String = ""

//    val ACTIVITY_REQUEST_FOR_SCANNING = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)


        if (FLAVOR == "spykar"){
            imgHeaderIcon.setImageResource(R.drawable.spykar_header_logo)
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
            txtScanType.setTextColor(ContextCompat.getColor(this, R.color.white))
        }else if (FLAVOR == "pepe"){
            imgHeaderIcon.setImageResource(R.drawable.pepe_header_logo)
            imgHeaderIcon.setBackgroundColor(Color.parseColor("#ffffff"));
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            txtScanType.setTextColor(ContextCompat.getColor(this, R.color.black))
        }else if (FLAVOR == "pantaloons"){
            imgHeaderIcon.setImageResource(R.drawable.pantaloons_header_logo)
            imgHeaderIcon.setBackgroundColor(Color.parseColor("#ffffff"));
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            txtScanType.setTextColor(ContextCompat.getColor(this, R.color.black))
        }else if (FLAVOR == "biba"){
            imgHeaderIcon.setImageResource(R.drawable.biba_header_logo)
            imgHeaderIcon.setBackgroundColor(Color.parseColor("#ffffff"));
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            txtScanType.setTextColor(ContextCompat.getColor(this, R.color.black))
        }else if (FLAVOR == "clovia"){
            imgHeaderIcon.setImageResource(R.drawable.clovia_header_logo)
            imgHeaderIcon.setBackgroundColor(Color.parseColor("#ffffff"));
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
            txtScanType.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        extra = intent.extras!!
        if(extra != null){
            strType = extra.getString("scan_type", "");
            strCode= extra.getString("scan_code", "");
        }

        if(FLAVOR == "pepe"){
            txtTitleScan.text = "Activate Codes"
        }

        if(FLAVOR == "pantaloons") {
//            if(strCode.equals("mrp")){
                txtScanType.setText("Scan QR Code")
//            }else{
//                txtScanType.setText("Scan Barcode")
//                mrpTagCode = extra.getString("mrpTagCode", "");
//            }
        }else{
//            if(strCode.equals("mrp")){
                txtScanType.setText("Scan MRP tag code")
//            }else{
//                txtScanType.setText("Scan Wash care code")
//                mrpTagCode = extra.getString("mrpTagCode", "");
//            }
        }


        // getting barcode instance
        // getting barcode instance

        barcodeReader =
            (supportFragmentManager.findFragmentById(R.id.barcode_fragment) as BarcodeReader?)!!

        imgNext.setOnClickListener {
            if(TextUtils.isEmpty(edtQRCode.text.toString())){
                Toast.makeText(applicationContext, "Scan or enter QR Code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
//            if(strCode.equals("mrp")) {

//                val intent = Intent(this@MRPScanCodeActivity, MRPScanCodeActivity::class.java)
//                intent.putExtra("scan_type", strType)
//                intent.putExtra("scan_code", "wash")
//                intent.putExtra("mrpTagCode", edtQRCode.text.toString())
//                startActivity(intent)
            mapCodes(mrpTagCode)
                //finish()
//            }else{
//
//                mapCodes(mrpTagCode, edtQRCode.text.toString())
//            }
        }

        imgClear.setOnClickListener {
            PreferenceConnector.writeInteger(this@MRPScanCodeActivity, PreferenceConnector.MAPPED_CODE, 0)
            Toast.makeText(
                    this@MRPScanCodeActivity, "Session cleared",
                    Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this@MRPScanCodeActivity, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
            finish()
        }


    }



 /*   override fun onDestroy() {
        super.onDestroy()
//        barcodeReader.pauseScanning()
        
    }*/


    override fun onResume() {
        super.onResume()
        updateCountText()
//        barcodeReader.resumeScanning()
    }

  /*  val ACTIVITY_REQUEST_FOR_SCANNING: Int = 1
    override fun onBackPressed() {
        barcodeReader.pauseScanning()
        val mIntent = Intent()
        mIntent.putExtra("is_success", true)
        mIntent.putExtra("unique_code", strQrCode)
        setResult(ACTIVITY_REQUEST_FOR_SCANNING, mIntent)
        super.onBackPressed()
    }*/
    var strQrCode = ""
    override fun onScanned(barcode: Barcode) {
        Log.e(TAG, "onScanned: " + barcode.displayValue)
//        barcodeReader.playBeep()


        runOnUiThread {

            /*Toast.makeText(
                applicationContext,
                "Barcode: " + barcode.displayValue,
                Toast.LENGTH_SHORT
            ).show()

            barcodeReader.pauseScanning()*/
//            barcodeReader.pauseScanning()
            strQrCode = barcode.displayValue
            Log.v("strQrCode","strQrCode "+strQrCode)
            if(!strQrCode.contains("ttags.in") && FLAVOR != "pantaloons"){
                return@runOnUiThread
            }
            if (strQrCode.contains("/")) {
                strQrCode = strQrCode.substring(strQrCode.lastIndexOf("/") + 1)
            } else if (strQrCode.contains("\\")) {
                strQrCode = strQrCode.substring(strQrCode.lastIndexOf("\\") + 1)
            }

            edtQRCode.setText(strQrCode)


//            if(strCode.equals("mrp")) {
            if (!FLAVOR.equals("pepe")){
                val intent = Intent(this@MRPScanCodeActivity, WashCodeScanActivity::class.java)
                intent.putExtra("scan_type", strType)
                intent.putExtra("scan_code", "wash")
                intent.putExtra("mrpTagCode", edtQRCode.text.toString())
                startActivity(intent)
                finish()
            }else{
//                imgNext.visibility = View.VISIBLE
                mapCodes(strQrCode)
            }

//                startActivityForResult(
//                    intent, ACTIVITY_REQUEST_FOR_SCANNING
//                )
                //finish()
//            }else{
//                mapCodes(mrpTagCode, edtQRCode.text.toString())
//            }

        }


    }

    override fun onScannedMultiple(barcodes: List<Barcode>) {

    }

    override fun onBitmapScanned(sparseArray: SparseArray<Barcode?>?) {}

    override fun onScanError(errorMessage: String?) {}

    override fun onCameraPermissionDenied() {
       /* Toast.makeText(applicationContext, "Camera permission denied!", Toast.LENGTH_LONG).show()
        finish()*/
    }



    private fun mapCodes(mrpTagCode: String){
        showProgressDialog("Mapping Codes")
        val base64DataJsonStringToken = JSONObject()

        base64DataJsonStringToken.put("mrpTagCode", mrpTagCode)
        base64DataJsonStringToken.put("washCareCode", "")

        val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = base64DataJsonStringToken.toString().toRequestBody(jsonMediaType)


        val apiService = ApiUtils.getAPIServiceByRx(this)
        var reqObserver : Observable<ResponseBody>

        if(strType.equals("map_code"))
            reqObserver = apiService.mapCodesPepe(requestBody)
        else
            reqObserver = apiService.reMapCodes(requestBody)


        reqObserver.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<ResponseBody> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(responseModel: ResponseBody) {

//                    listenerResponse.onSuccess(responseModel)
                    Log.v("Login", " Login response " + responseModel.toString())


                    //                List<Movie> movies = response.body().getResults();
//                    Log.d("", "Number of movies received: ");
                    try {

                        val jsonResponse = JSONObject(responseModel.string().toString())
                        if (jsonResponse.optString("success").equals("1")) {
                            barcodeReader.pauseScanning()
                            Toast.makeText(
                                    this@MRPScanCodeActivity,
                                    jsonResponse.optString("message").toString(),
                                    Toast.LENGTH_SHORT
                            ).show()

                            val count = PreferenceConnector.readInteger(
                                    this@MRPScanCodeActivity,
                                    PreferenceConnector.MAPPED_CODE,
                                    0
                            )
                            PreferenceConnector.writeInteger(
                                    this@MRPScanCodeActivity,
                                    PreferenceConnector.MAPPED_CODE,
                                    count + 1
                            )
                            strCode = "mrp"
                            txtScanType.setText("Scan MRP tag code")
                            updateCountText();
                            barcodeReader.resumeScanning()
//                            onBackPressed()
//                            finish()
                          /*  val intent = Intent(this@MRPScanCodeActivity, MRPScanCodeActivity::class.java)
                            intent.putExtra("scan_type","map_code")
                            intent.putExtra("scan_code","mrp")
                            startActivity(intent)*/

                        } else {
                            val strMsg = jsonResponse.optString("message");
                            if (strMsg.toString().contains("invalid token")){
                                sessionOut()
                            }else{
                                Toast.makeText(
                                        this@MRPScanCodeActivity,
                                        jsonResponse.optString("message").toString(),
                                        Toast.LENGTH_SHORT
                                ).show()
                                barcodeReader.resumeScanning()
                            }

                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
//                        Toast.makeText(
//                            this@LoginActivity,
//                            "Username or password invalid",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        e.printStackTrace()
                    }
                    barcodeReader.resumeScanning()
                    dismissProgressDialog()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    dismissProgressDialog()
                    if ((e as HttpException).code() == 401) {
                        sessionOut()
                    }else{
                        Toast.makeText(applicationContext, "Please scan valid code", Toast.LENGTH_SHORT).show()
                    }
//                    listenerResponse.onError(e.message.toString())
                }

                override fun onComplete() {
                    dismissProgressDialog()
                    barcodeReader.resumeScanning()
                }

            })
    }


    private fun sessionOut(){
        Toast.makeText(
                this@MRPScanCodeActivity,"Invalid Token or Key",
                Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this@MRPScanCodeActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun updateCountText(){
        val count = PreferenceConnector.readInteger(this, PreferenceConnector.MAPPED_CODE, 0)
        if(FLAVOR == "pepe"){
            txtScanedCodesCount.text = "Scanned this session " +count.toString()
        }else
            txtScanedCodesCount.text = "Mapped this session " +count.toString()

    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ACTIVITY_REQUEST_FOR_SCANNING) {
            updateCountText()
        }
    }*/

}