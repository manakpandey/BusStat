package com.ugdevs.smartbus.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel(){
    var code = ""
    var details = HashMap<String,String>()

    fun setValue(code: String, details: HashMap<String,String>){
        this.code = code
        this.details = details
    }
}