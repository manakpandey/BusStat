package com.ugdevs.smartbus.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){

    var searchSrc = ""
    var searchDest = ""

    var selectedBus = HashMap<String,String>()

    val busResults: MutableLiveData<List<HashMap<String,String>>> by lazy {
        MutableLiveData<List<HashMap<String,String>>>()
    }
}