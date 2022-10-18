package com.example.a4starter

import android.annotation.SuppressLint
import android.content.Context
import android.gesture.GesturePoint
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView

@SuppressLint("AppCompatCustomView")
class CanvasView: ImageView {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    var path: Path? = null
    var paths: ArrayList<Path?> = ArrayList()
    var paintbrush = Paint(Color.BLUE)
    var background: Bitmap? = null
    var pointList: ArrayList<SharedViewModel.MyPoint> = ArrayList()

    // we save a lot of points because they need to be processed
    // during touch events e.g. ACTION_MOVE
    var x1 = 0f
    var y1 = 0f
    var p1_id = 0
    var p1_index = 0

    // store cumulative transformations
    // the inverse matrix is used to align points with the transformations - see below
    var currentMatrix = Matrix()
    var inverse = Matrix()

    // capture touch events (down/move/up) to create a path/stroke that we draw later
    override fun onTouchEvent(event: MotionEvent): Boolean {
        var inverted = floatArrayOf()
        when (event.pointerCount) {
            1 -> {
                p1_id = event.getPointerId(0)
                p1_index = event.findPointerIndex(p1_id)

                // invert using the current matrix to account for pan/scale
                // inverts in-place and returns boolean
                inverse = Matrix()
                currentMatrix.invert(inverse)

                // mapPoints returns values in-place
                inverted = floatArrayOf(event.getX(p1_index), event.getY(p1_index))
                inverse.mapPoints(inverted)
                x1 = inverted[0]
                y1 = inverted[1]
//                when (event.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        // Log.d(LOGNAME, "Action down")
//                        path = Path()
//                        if (paths.size == 0) {
//                            paths.add(path)
//                        }
//                        path!!.moveTo(x1, y1)
//                    }
//                    MotionEvent.ACTION_MOVE -> {
//                        // Log.d(LOGNAME, "Action move")
//                        path!!.lineTo(x1, y1)
//                    }
//                    // MotionEvent.ACTION_UP -> Log.d(LOGNAME, "Action up")
//                }

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Log.d(LOGNAME, "Action down")

                        if (paths.size == 0) {
                            path = Path()
                            pointList = ArrayList()
                            paths.add(path)
                            var thisPoint = SharedViewModel.MyPoint(x1.toDouble(),y1.toDouble())
                            pointList.add(thisPoint)
                            path!!.moveTo(x1, y1)

                        }

                    }
                    MotionEvent.ACTION_MOVE -> {
                        // Log.d(LOGNAME, "Action move")
                        if (paths.size == 1) {
                            var thisPoint = SharedViewModel.MyPoint(x1.toDouble(),y1.toDouble())
                            pointList.add(thisPoint)
                            path!!.lineTo(x1, y1)
                        }
                    }
                    // MotionEvent.ACTION_UP -> Log.d(LOGNAME, "Action up")
                }

            }
        }
        return true
    }

    // set image as background
    fun setImage(bitmap: Bitmap?) {
        background = bitmap
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // apply transformations from the event handler above
        canvas.concat(currentMatrix)

        // draw background
        if (background != null) {
            setImageBitmap(background)
        }

        // draw lines over it
        for (path in paths) {
            canvas.drawPath(path!!, paintbrush)
        }
    }

    fun clear() {
        path = null
        paths.clear()
        currentMatrix = Matrix()
        inverse = Matrix()
    }


    // constructor
    init {
        paintbrush.style = Paint.Style.STROKE
        paintbrush.strokeWidth = 5f
    }
}