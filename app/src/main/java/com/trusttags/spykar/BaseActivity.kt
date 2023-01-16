package com.trusttags.spykar

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog

import android.content.pm.PackageManager

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

abstract class BaseActivity : AppCompatActivity() {

    private val TAG = "JiometryBaseActivity"
    private val REQUEST_LOCATION_PROVIDER = 22155
    private val REQUEST_PERMISSION = 10341
    private var REQUEST_LOCATION_DENIED = false
    private var isGPSMandatory = false
    internal val ZOOM_SCALE = 2500.0

    /**
     * Method to check all app permissions required.
     */
    fun checkAllPermissions(): Boolean {

        if (
            !hasPermission(Manifest.permission.INTERNET) ||
            !hasPermission(Manifest.permission.READ_PHONE_STATE) ||
            !hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ||
            !hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) ||
            !hasPermission(Manifest.permission.ACCESS_NETWORK_STATE) ||
            !hasPermission(Manifest.permission.ACCESS_WIFI_STATE) ||
            !hasPermission(Manifest.permission.VIBRATE)||
            !hasPermission(Manifest.permission.CAMERA)||
            !hasPermission(Manifest.permission.READ_EXTERNAL_STORAGE)||
            !hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.VIBRATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                10341
            )
            return false
        }
        return true
    }

    /**
     * Private method to check a specific permission for this application.
     */
    private inline fun hasPermission(permission: String) = ContextCompat
        .checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED



    /**
     * Method to hide the soft keyboard from the UI.
     */
    fun hideSoftKeyboard() {

        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private var mProgressDialog: ProgressDialog? = null

    /**
     * Method to show progress dialog with given message.
     */
    fun showProgressDialog(message: String, title: String = "") {

        dismissProgressDialog()
        mProgressDialog = ProgressDialog.show(getActivity(), title, message)
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.show()
    }

    /**
     * Method to dismiss the progress dialog in case if it exists.
     */
    fun dismissProgressDialog() {
        if (mProgressDialog != null) mProgressDialog!!.dismiss()
    }



    /**
     * Getter method to fetch JiometryBaseActivity object reference.
     */
    fun getActivity() = this

    /**
     * Method to show toast within a specific activity.
     *
     * @param msg String message to display in toast.
     * @param interval Toast enum interval optional value.
     */
    fun toast(msg: String, interval: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, msg, interval).show()

    /**
     * Method to show toast within a specific activity.
     *
     * @param res String resource message to display in toast.
     * @param interval Toast enum interval optional value.
     */
    fun toast(res: Int, interval: Int = Toast.LENGTH_SHORT) =
        Toast.makeText(this, res, interval).show()



}