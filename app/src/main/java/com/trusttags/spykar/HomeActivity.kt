package com.trusttags.spykar

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trusttags.spykar.BuildConfig.*
import com.trusttags.spykar.services.ApiUtils
import com.trusttags.spykar.utils.PreferenceConnector
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.imgHeaderIcon
import kotlinx.android.synthetic.main.activity_home.mainLayout
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        if (FLAVOR == "spykar"){
            imgHeaderIcon.setImageResource(R.drawable.spykar_header_logo)
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.black))
        }else if (FLAVOR == "pepe"){
            imgHeaderIcon.setImageResource(R.drawable.pepe_header_logo)
            imgHeaderIcon.setBackgroundColor(Color.parseColor("#ffffff"));
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }else if (FLAVOR == "pantaloons"){
            imgHeaderIcon.setImageResource(R.drawable.pantaloons_header_logo)
            imgHeaderIcon.setBackgroundColor(Color.parseColor("#ffffff"));
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }else if (FLAVOR == "biba"){
            imgHeaderIcon.setImageResource(R.drawable.biba_header_logo)
            imgHeaderIcon.setBackgroundColor(Color.parseColor("#ffffff"));
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }else if (FLAVOR == "clovia"){
            imgHeaderIcon.setImageResource(R.drawable.clovia_header_logo)
            imgHeaderIcon.setBackgroundColor(Color.parseColor("#ffffff"));
            mainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
        }
        if (FLAVOR == "pepe"){
            btnReTagCodes.visibility = View.GONE
            btnMapCodes.setText("Activate Codes")
        }else
            btnReTagCodes.visibility = View.VISIBLE

        btnMapCodes.setOnClickListener {
            val intent = Intent(this@HomeActivity, MRPScanCodeActivity::class.java)
            intent.putExtra("scan_type","map_code")
            intent.putExtra("scan_code","mrp")
            startActivity(intent)
        }



        btnReTagCodes.setOnClickListener {
            val intent = Intent(this@HomeActivity, MRPScanCodeActivity::class.java)
            intent.putExtra("scan_type","remap_code")
            intent.putExtra("scan_code","mrp")
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            PreferenceConnector.getEditor(this).clear();
            val back = Intent(this, LoginActivity::class.java)
            back.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(back)
            finish()
        }



    }

}