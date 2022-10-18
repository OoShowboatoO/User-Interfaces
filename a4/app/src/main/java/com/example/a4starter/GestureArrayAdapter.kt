package com.example.a4starter

import android.content.Context
import android.graphics.Bitmap
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider

class GestureArrayAdapter(context: Context?, gestures:ArrayList<MyGesture>):
    ArrayAdapter<MyGesture>(context!!, 0, gestures) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get the data item for this position
        var view: View? = convertView
        val gesture = getItem(position)

        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_gesture, parent, false)
        }

        // Lookup view for data population
        var gPic = view?.findViewById(R.id.gesture_pic) as ImageView
        val gName = view?.findViewById(R.id.gesture_name) as TextView
        val deleteBtn = view?.findViewById(R.id.delete_btn) as Button

        // Populate the data into the template view using the data object
        gPic.setImageBitmap(gesture!!.gestureImg)
        gName.text = gesture!!.gestureName

        deleteBtn.setOnClickListener { v ->
            this.remove(gesture)
        }

        // Return the completed view to render on screen
        return view
    }

}