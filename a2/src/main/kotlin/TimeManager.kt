import javafx.application.Platform
import java.util.*

class TimeManager(private val model: Model){
    var isPaused = false
    var timer: Timer = Timer()

    fun start() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                Platform.runLater {
                    model.incrementFrameCounter()
                    model.selfUpdate()
                }
            }
        }, 0, 1000)
    }

    fun pause() {
        if (!isPaused) {
            isPaused = true
            timer.cancel()
        }
    }

    fun next() {
        if(isPaused) {
            model.incrementFrameCounter()
            model.selfUpdate()
        }
    }

    fun resume() {
        if (isPaused) {
            isPaused = false
            timer = Timer()
            start()
        }
    }
}