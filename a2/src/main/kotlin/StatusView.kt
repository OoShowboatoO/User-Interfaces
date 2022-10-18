import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.scene.control.TextArea
import javafx.scene.layout.Pane
import java.util.Collections.addAll
import javax.swing.GroupLayout

class StatusView(private val model: Model) : IView, HBox() {
    var actionInfo = Label("")
    var frameInfo = Label("")


    init {
        var frameNum = model.getFrameCounter()

        frameInfo.text = "Frame $frameNum"
        //frameInfo.alignment = Pos.BASELINE_RIGHT
        val space = Pane()
        setHgrow(space, Priority.ALWAYS)
//        actionInfo.setMinSize(750.0, 20.0)
//        frameInfo.setMinSize(100.0, 20.0)

        this.children.addAll(actionInfo, space, frameInfo)
    }

    override fun update() {
        // react to update from model
        // how do we get data from the model? do we need it?
        // println("StatusView is updated")
        var shapeInfo = model.getSelectedShape()
        var xPos = model.getClick().first
        var yPos = model.getClick().second
        var frameNum = model.getFrameCounter()
        // init gets called after constructors
        // you can set up here
        if (shapeInfo == "Clear" || shapeInfo == "") {
            actionInfo.text = "Clear"
        }
        if (model.getStatusUpdate() && model.isCellValid()) {
            actionInfo.text = "Created $shapeInfo at $xPos,$yPos"
        }
        frameInfo.text = "Frame $frameNum"
        model.setStatusUpdate(false)
    }
}