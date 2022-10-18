package com.example.a4starter

import android.graphics.BitmapFactory
import android.graphics.Path
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class HomeFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle? ): View? {

        var mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val canvasView: CanvasView = root.findViewById(R.id.home_canvas)
        val image = BitmapFactory.decodeResource(resources, R.drawable.paper)
        canvasView.setImage(image)

        val homeClearBtn: Button = root.findViewById(R.id.home_clear_button)
        homeClearBtn.setOnClickListener { v ->
            canvasView.clear()
            canvasView.setImage(image)
        }

        val matchBtn: Button = root.findViewById(R.id.home_match_button)
        matchBtn.setOnClickListener { v ->
            var thisPointList = canvasView.pointList
            var gestureArray: ArrayList<MyGesture> = mViewModel.getMatchGes(thisPointList)

            val adapter = GestureArrayAdapter(this.requireContext(),gestureArray)
            val listView: ListView = root.findViewById(R.id.rec_gestureList) as ListView
            listView.setAdapter(adapter)
        }


        return root
    }
}