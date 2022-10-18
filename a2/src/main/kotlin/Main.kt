import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlin.system.exitProcess


class Main : Application() {



    override fun start(stage: Stage?) {
        val model = Model()
        var timer = TimeManager(model)

        // our layout is the root of the scene graph
        val root = VBox()

        // views are the children of the top-level layout
        val toolbar = ToolbarView(model, timer)
        val grid = GridView(model, timer)
        val status = StatusView(model)

        // register views with the model
        model.addView(toolbar)
        model.addView(grid)
        model.addView(status)

        // setup and display
        root.children.addAll(toolbar, grid, status)

        // timer starts
        timer.start()


        var handler = EventHandler<KeyEvent> { event ->
            // Pause and get into Manual Mode
            if (event.code === KeyCode.P) {
                timer.pause()

            // advance one frame at a time
            } else if (event.code === KeyCode.N ) {
                timer.next()

            // Resume
            } else if (event.code === KeyCode.R) {
                timer.resume()
            }
        }
        root.onKeyPressed = handler

        stage?.scene = Scene(root)
        stage?.isResizable = false
        stage?.width = 850.0
        stage?.height = 650.0
        stage?.title = "Conway's Game of Life (m25ding)"
        stage?.setOnCloseRequest { exitProcess(0) }
        stage?.show()
    }
}
