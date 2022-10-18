import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.io.FileInputStream

class ToolbarView(private val model: Model, private var timer: TimeManager ) : IView, ToolBar() {
    init {
        val sourcePath = "${System.getProperty("user.dir")}/src/main/resources/"

        val blockBtn = Button("Block")
        val blockImg = ImageView(Image(FileInputStream(sourcePath + "block.png")))
        blockImg.fitWidth = 25.0
        blockImg.fitHeight = 25.0
        blockBtn.graphic = blockImg

        val beehiveBtn = Button("Beehive")
        val beehiveImg = ImageView(Image(FileInputStream(sourcePath + "beehive.png")))
        beehiveImg.fitWidth = 25.0
        beehiveImg.fitHeight = 25.0
        beehiveBtn.graphic = beehiveImg

        val blinkerBtn = Button("Blinker")
        val blinkerImg = ImageView(Image(FileInputStream(sourcePath + "blinker.png")))
        blinkerImg.fitWidth = 25.0
        blinkerImg.fitHeight = 25.0
        blinkerBtn.graphic = blinkerImg

        val toadBtn = Button("Toad")
        val toadImg = ImageView(Image(FileInputStream(sourcePath + "toad.png")))
        toadImg.fitWidth = 25.0
        toadImg.fitHeight = 25.0
        toadBtn.graphic = toadImg

        val gliderBtn = Button("Glider")
        val gliderImg = ImageView(Image(FileInputStream(sourcePath + "glider.png")))
        gliderImg.fitWidth = 25.0
        gliderImg.fitHeight = 25.0
        gliderBtn.graphic = gliderImg

        val clearBtn = Button("Clear")
        val clearImg = ImageView(Image(FileInputStream(sourcePath + "clear.png")))
        clearImg.fitWidth = 25.0
        clearImg.fitHeight = 25.0
        clearBtn.graphic = clearImg

        val pauseBtn = Button("Pause")
        val pauseImg = ImageView(Image(FileInputStream(sourcePath + "pause.png")))
        pauseImg.fitWidth = 25.0
        pauseImg.fitHeight = 25.0
        pauseBtn.graphic = pauseImg

        val nextBtn = Button("Next")
        val nextImg = ImageView(Image(FileInputStream(sourcePath + "next.png")))
        nextImg.fitWidth = 25.0
        nextImg.fitHeight = 25.0
        nextBtn.graphic = nextImg

        val resumeBtn = Button("Resume")
        val resumeImg = ImageView(Image(FileInputStream(sourcePath + "resume.png")))
        resumeImg.fitWidth = 25.0
        resumeImg.fitHeight = 25.0
        resumeBtn.graphic = resumeImg


        // add buttons to toolbar
        this.items.addAll(blockBtn, beehiveBtn, blinkerBtn, toadBtn, gliderBtn,
            clearBtn, pauseBtn, nextBtn, resumeBtn)


        blockBtn.setOnAction {
            model.setShape("Block")
        }

        beehiveBtn.setOnAction {
            model.setShape("Beehive")
        }

        blinkerBtn.setOnAction {
            model.setShape("Blinker")
        }

        toadBtn.setOnAction {
            model.setShape("Toad")
        }

        gliderBtn.setOnAction {
            model.setShape("Glider")
        }

        clearBtn.setOnAction {
            model.setShape("Clear")
            if (timer.isPaused){
                model.clearBoard()
                model.incrementFrameCounter()
            } else {
                model.clearBoard()
            }
        }

        pauseBtn.setOnAction {
            timer.pause()
        }

        nextBtn.setOnAction {
            timer.next()
        }

        resumeBtn.setOnAction {
            timer.resume()
        }


    }

    override fun update() {
        // update my button state
        // how do we get data from the model?
        // println("ToolbarView is updated")
    }
}