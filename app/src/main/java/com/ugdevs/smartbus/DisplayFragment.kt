package com.ugdevs.smartbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.ugdevs.smartbus.utils.MainViewModel
import kotlinx.android.synthetic.main.fragment_display.*

class DisplayFragment : Fragment(){

    private lateinit var model: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        return inflater.inflate(R.layout.fragment_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        display_code.text = model.code
        display_bus_number.text = model.details["number"]
        display_mode.text  = model.details["mode"]
        display_amt.text = model.details["price"]
    }

}