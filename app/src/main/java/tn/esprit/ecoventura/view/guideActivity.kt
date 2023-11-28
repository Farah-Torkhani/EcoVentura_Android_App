package tn.esprit.ecoventura.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import tn.esprit.ecoventura.R

import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import tn.esprit.ecoventura.adapter.GuideAdapter
import tn.esprit.ecoventura.apiService.GuideApi
import tn.esprit.ecoventura.model.Guide
import tn.esprit.ecoventura.model.GuideApiResponse

class guideActivity :  AppCompatActivity(), GuideAdapter.OnItemClickListener {


    lateinit var guideAdapter: GuideAdapter
    lateinit  var  guiderecyclerView: RecyclerView
    private var guideList: ArrayList<Guide> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Binding
        setContentView(R.layout.guide_activity)
        guiderecyclerView= findViewById(R.id.recycler_view_guides)

        // Configurer le RecyclerView
        val layoutManager = LinearLayoutManager(this)
        guiderecyclerView.setLayoutManager(layoutManager)

        // Initialiser et configurer l'adaptateur
        guideAdapter = GuideAdapter()
        guiderecyclerView.adapter = guideAdapter // Associer l'adaptateur au RecyclerView
        guideAdapter.setGuides(guideList)   // Configurer l'adaptateur pour RecyclerView avec la liste d'achats


        loadGuideData()
    }



    private fun loadGuideData() {
        val apiService = GuideApi.create()

        lifecycleScope.launch(Dispatchers.Main) {
            try {
                Log.d("tag", "zzzzzz")
                val response = apiService.getAllGuides()
                Log.d("teag", "${response.body()}")

                if (response.isSuccessful) {
                    val guideApiResponse: GuideApiResponse? = response.body()

                    if (guideApiResponse != null) {
                        guideList = ArrayList(guideApiResponse.guides)
                        guideAdapter.setGuides(guideList)
                        Log.d("guideList", "$guideList")
                        // showToast("History loaded successfully.")
                    } else {
                        showToast("Error: GuideApiResponse is null.")
                    }
                } else {
                    when (response.code()) {
                        401 -> {
                            // Code 401 : Unauthorized
                            // Redirect the user to the login page or show an error message
                            // showToast("Unauthorized. Redirecting to login.")
                        }
                        404 -> {
                            // Code 404 : Not Found
                            // showToast("History not found.")
                        }
                        else -> {
                            // Show a generic error message
                            // showToast("An error occurred.")
                        }
                    }
                }
            } catch (e: Exception) {
                showToast("An error occurred. Please try again later. ${e.message}")
                Log.d("aaw", "${e.message}")
            } finally {
                // progress.visibility = View.GONE
            }
        }
    }




    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

        private fun styleTextView(textView: TextView, partOne: String, partTwo: String) {
            val spannableString = SpannableString(partOne + partTwo)

            spannableString.setSpan(
                ForegroundColorSpan(Color.BLACK),
                0,
                partOne.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannableString.setSpan(
                ForegroundColorSpan(Color.parseColor("#44F1A6")),
                partOne.length,
                spannableString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            textView.text = spannableString
        }


    override fun onItemClick(guide: Guide) {
      Log.d("c","hello")
    }


}