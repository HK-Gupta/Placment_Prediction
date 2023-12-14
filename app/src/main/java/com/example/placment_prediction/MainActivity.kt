package com.example.placment_prediction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.BuildConfig
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.placment_prediction.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val url:String = com.example.placment_prediction.BuildConfig.API_KEY
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.color.actionBar))
        supportActionBar?.title = "Placement Prediction"
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        binding.progressBar.visibility = View.GONE

        binding.btnPredict.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val stringRequest = object : StringRequest(Method.POST, url,
                Response.Listener {
                    binding.progressBar.visibility = View.GONE
                    try {
                        val jsonObject = JSONObject(it)
                        val res = jsonObject.getString("placement")
                        if(res == "1") {
                            binding.result.text = "Placed"
                        } else{
                            binding.result.text = "Not Placed"
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                }) {
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()
                    params["cgpa"] = binding.cgpa.text.toString()
                    params["iq"] = binding.iq.text.toString()
                    params["profile_score"] = binding.profileScore.text.toString()

                    return params
                }
            }
            val queue: RequestQueue = Volley.newRequestQueue(this@MainActivity)
            queue.add(stringRequest)
        }
    }
}