package com.example.a4starter

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Path
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class AdditionFragment : Fragment() {
    private var mViewModel: SharedViewModel? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root: View = inflater.inflate(R.layout.fragment_addition, container, false)

        val canvasView: CanvasView = root.findViewById(R.id.addition_canvas)
        val image = BitmapFactory.decodeResource(resources, R.drawable.paper)
        canvasView.setImage(image)

        val saveBtn: Button = root.findViewById(R.id.save_button)
        saveBtn.setOnClickListener { v ->
            val builder: AlertDialog.Builder = AlertDialog.Builder(canvasView.context)
            builder.setTitle("Please name your gesture.")
            val input = EditText(canvasView.context)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)
            builder.setPositiveButton("Confirm") { dialog, which ->
                var isFound = false
                if (canvasView.path != null) {
                    val newGestureName = input.text.toString()
                    for (thisGes in mViewModel!!.strokeGestures) {
                        if (thisGes?.gestureName == newGestureName) {
                            isFound = true
                            thisGes.gesturePath = canvasView.pointList
                            thisGes.gestureImg = canvasView.drawToBitmap()
                        }
                    }
                    if (!isFound) {
                        mViewModel!!.addStroke(newGestureName,canvasView.pointList,canvasView.drawToBitmap())
                    }
                }
                canvasView.clear()
                canvasView.setImage(image)

            }

            builder.setNegativeButton("Cancel") { dialog, which ->
            }

            builder.show()
        }

        val clearBtn: Button = root.findViewById(R.id.clear_button)
        clearBtn.setOnClickListener { v ->
            canvasView.clear()
            canvasView.setImage(image)
        }

//        mViewModel!!.desc.observe(viewLifecycleOwner, { s:String -> textView.text = "$s - Addition" })
//        mViewModel!!.strokeGestures.observe(viewLifecycleOwner, { s:ArrayList<Path> -> textView.text = "stroke count: ${s.size}"})

        return root
    }


}
