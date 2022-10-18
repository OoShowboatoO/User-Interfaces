import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlin.system.exitProcess


class Main : Application() {


    override fun start(stage: Stage) {
        val model = Model()

        // our layout is the root of the scene graph
        val root = VBox()


        // views are the children of the top-level layout
        val toolbar = ToolbarView(stage, model)
        val pane = CanvasView(stage, model)
        val status = StatusView(model)

        // register views with the model
        model.addView(toolbar)
        model.addView(pane)
        model.addView(status)

        // setup and display
        root.children.addAll(toolbar, pane, status)

        stage.scene = Scene(root)
        stage.isResizable = true

        stage.minWidth = 950.0
        stage.minHeight = 600.0

        stage.maxWidth = 1600.0
        stage.maxHeight = 1200.0

        stage.width = 1000.0
        stage.height = 800.0

        // Set listeners so that the CanvasView can resize its size by the stage size
        stage.widthProperty().addListener { obs, oldVal, newVal ->
            model.setCanvasWidth(newVal as Double)
            if (model.isCascade) {
                model.reposition()
            } else {
                model.setTileSize()
            }
        }
        stage.heightProperty().addListener { obs, oldVal, newVal ->
            model.setCanvasHeight(newVal as Double - 75.0)
            if (model.isCascade) {
                model.reposition()
            } else {
                model.setTileSize()
            }
        }

        stage.title = "Lightbox (m25ding)"
        stage.setOnCloseRequest { exitProcess(0) }
        stage.show()
    }

}
