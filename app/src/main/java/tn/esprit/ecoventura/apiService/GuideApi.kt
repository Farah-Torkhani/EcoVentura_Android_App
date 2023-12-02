package tn.esprit.ecoventura.apiService



import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import tn.esprit.ecoventura.model.Guide
import tn.esprit.ecoventura.model.GuideApiResponse

interface GuideApi {
    @GET("/api/guides")
    suspend fun getAllGuides(): Response<GuideApiResponse>
    @GET("/api/guides/{id}/availability")
    suspend fun getGuideAvailability(@Path("id") guideId: String): Response<List<String>>
    @GET("/api/guide/{id}")
    suspend fun getGuideDetail(@Path("id") _id: String): Response<Guide>
    companion object {
        private var BASE_URL = "http://192.168.56.1:3000/"
        fun create(): GuideApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(GuideApi::class.java)
        }
    }
}