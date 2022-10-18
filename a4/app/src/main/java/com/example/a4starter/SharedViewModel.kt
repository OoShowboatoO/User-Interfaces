package com.example.a4starter

import android.graphics.Bitmap
import android.graphics.Path
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val desc: MutableLiveData<String> = MutableLiveData()
    val strokeGestures: ArrayList<MyGesture> = ArrayList<MyGesture>()
    var recognizedGesture: ArrayList<MyGesture> = ArrayList<MyGesture>()

    init {
        desc.value = "Shared model"
//        strokeGestures.value?.add(Path()) // empty path for illustration purposes
    }

    fun addStroke(gName:String, path: ArrayList<MyPoint>, newBitmap: Bitmap) {
        val newGesture = MyGesture(newBitmap, gName, path)
        strokeGestures.add(newGesture)
    }

    // ... more methods added here

    class MyPoint internal constructor(mx: Double, my:Double) {
        var x = 0.0
        var y = 0.0

        fun distance(p: MyPoint): Double {
            return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y))
        }

        init {
            x = mx
            y = my
        }
    }

    fun compare2ges(ges1: ArrayList<MyPoint>, ges2:MyGesture): Double {
        var len = Math.min(ges1.size, ges2.gesturePath.size)
        var finalMark = 0.0
        for (i in (0 until len)) {
            finalMark += ges1[i].distance(ges2.gesturePath[i])
        }
        finalMark /= len
        return finalMark
    }

    fun getMatchGes(thisList: ArrayList<MyPoint>): ArrayList<MyGesture> {
        var rankList:ArrayList<Double> = ArrayList()
        recognizedGesture = ArrayList()

        var len = strokeGestures.size
        for (i in (0 until len)) {
            rankList.add(compare2ges(thisList, strokeGestures[i]))
        }
        var firstIndex = 0
        var secondIndex = 1
        var thirdIndex = 2

        if (len > 3) {
            for(i in (0 until len)) {
                if (rankList[i] < rankList[firstIndex]) {
                    thirdIndex = secondIndex
                    secondIndex = firstIndex
                    firstIndex = i
                } else if (rankList[i] < rankList[secondIndex]) {
                    thirdIndex = secondIndex
                    secondIndex = i
                } else if ((rankList[i] < rankList[thirdIndex])) {
                    thirdIndex = i
                }
            }
            recognizedGesture.add(strokeGestures[firstIndex])
            recognizedGesture.add(strokeGestures[secondIndex])
            recognizedGesture.add(strokeGestures[thirdIndex])

        } else if (len == 1) {
            recognizedGesture.add(strokeGestures[0])

        } else if (len == 2) {
            if (rankList[0] < rankList[1]) {
                recognizedGesture.add(strokeGestures[0])
                recognizedGesture.add(strokeGestures[1])
            } else {
                recognizedGesture.add(strokeGestures[1])
                recognizedGesture.add(strokeGestures[0])
            }
        } else {
            if (rankList[0] < rankList[1] && rankList[0] < rankList[2]) {
                recognizedGesture.add(strokeGestures[0])
                if (rankList[1] < rankList[2]) {
                    recognizedGesture.add(strokeGestures[1])
                    recognizedGesture.add(strokeGestures[2])
                } else {
                    recognizedGesture.add(strokeGestures[2])
                    recognizedGesture.add(strokeGestures[1])
                }

            } else if (rankList[1] < rankList[0] && rankList[1] < rankList[2]) {
                recognizedGesture.add(strokeGestures[1])
                if (rankList[0] < rankList[2]) {
                    recognizedGesture.add(strokeGestures[0])
                    recognizedGesture.add(strokeGestures[2])
                } else {
                    recognizedGesture.add(strokeGestures[2])
                    recognizedGesture.add(strokeGestures[0])
                }

            } else if (rankList[2] < rankList[0] && rankList[2] < rankList[1]) {
                recognizedGesture.add(strokeGestures[2])

                if (rankList[0] < rankList[1]) {
                    recognizedGesture.add(strokeGestures[0])
                    recognizedGesture.add(strokeGestures[1])
                } else {
                    recognizedGesture.add(strokeGestures[1])
                    recognizedGesture.add(strokeGestures[0])
                }
            }
        }

        return recognizedGesture
    }

}