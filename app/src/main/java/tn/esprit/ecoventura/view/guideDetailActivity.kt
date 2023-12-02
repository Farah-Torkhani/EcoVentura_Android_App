package tn.esprit.ecoventura.view


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tn.esprit.ecoventura.R
import tn.esprit.ecoventura.apiService.GuideApi
import tn.esprit.ecoventura.model.Guide


class guideDetailActivity : AppCompatActivity() {
//    lateinit var chambreRecyclerView: RecyclerView
//    private var guideList: ArrayList<guide> = ArrayList()
//    lateinit var guideAdapter: guideAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guidedetail)
         val fullnameTextView: TextView = findViewById(R.id.fullnameTextView)
         val locationTextView: TextView = findViewById(R.id.locationTextView)
         val imageView: ImageView = findViewById(R.id.imageView)
         val priceTextView:TextView = findViewById(R.id.priceTextView)
       val  descriptionTextView:TextView = findViewById(R.id.text_Guide_description)


        // get guidedId from intent
        val _id = intent.getStringExtra("_id")
        if (_id.isNullOrEmpty()) {
            showToast("Invalid guide ID.")
            finish() // Close the activity if the ID is not provided.
            return
        }
        val apiService = GuideApi.create()
        lifecycleScope.launch(Dispatchers.Main) {
            try {
                val response = apiService.getGuideDetail(_id ?: "")
                if (response.isSuccessful) {
                    val guide = response.body()


                        fullnameTextView.text = guide?.fullname
                        descriptionTextView.text = guide?.description
                        priceTextView.text = guide?.price
                        locationTextView.text = guide?.location
                        val imageUrl = guide?.image

                        Log.d("image", "$imageUrl")


                    Picasso.get()
                        .load(guide?.image)
                        .into(imageView)

                } else {
                    showToast("Failed to fetch guided details.")
                    Log.e("API_ERROR", "Error: ${response.code()}")

                }
            } catch (e: Exception) {
                showToast("An error occurred. Please try again later. ${e.message}")
                Log.e("guideDetailActivity", "Error: ${e.message}", e)
            }

        }
    }
    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }
}