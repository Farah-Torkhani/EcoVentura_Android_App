package tn.esprit.ecoventura.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.ecoventura.model.Guide

class GuideViewModel: ViewModel() {

    private val guide: MutableLiveData<List<Guide>> = MutableLiveData()
    private val errorMessage: MutableLiveData<String> = MutableLiveData()



    fun getGuide(): LiveData<List<Guide>> = guide
    fun getErrorMessage(): LiveData<String> = errorMessage

    
}