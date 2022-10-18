package com.example.a4starter

import android.graphics.Bitmap
import android.graphics.Path

// class MyGesture (var gestureImg:Bitmap, var gestureName: String, var gesturePath: Path)
class MyGesture (var gestureImg:Bitmap, var gestureName: String, var gesturePath: ArrayList<SharedViewModel.MyPoint>)