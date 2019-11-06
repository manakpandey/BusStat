package com.ugdevs.smartbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.ugdevs.smartbus.utils.MainViewModel
import java.util.*

class SearchFragment: Fragment() {

    private lateinit var model: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchSource: TextInputEditText = view.findViewById(R.id.search_source)
        val searchDestination: TextInputEditText = view.findViewById(R.id.search_destination)
        val searchButton = view.findViewById<FloatingActionButton>(R.id.search_button)

        searchButton.setOnClickListener {
            if (!(searchSource.text!!.isBlank() || searchDestination.text!!.isBlank())){
                model.searchSrc = searchSource.text.toString().toLowerCase(Locale.ENGLISH)
                model.searchDest = searchDestination.text.toString().toLowerCase(Locale.ENGLISH)
                findNavController().navigate(R.id.dest_searchResultsFragment)
            }
        }
    }


}