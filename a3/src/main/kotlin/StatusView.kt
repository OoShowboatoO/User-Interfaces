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
    var numInfo = Label("${model.totalImg} image(s) is loaded")
    var modeInfo = Label("Cascade Mode")


    init {
        val space = Pane()
        setHgrow(space, Priority.ALWAYS)
        this.children.addAll(numInfo, space, modeInfo)
    }

    override fun update() {
        numInfo.text = "${model.totalImg} image(s) is loaded."
        if (model.isCascade) {
            modeInfo.text = "Cascade Mode"
        } else {
            modeInfo.text = "Tile Mode"
        }
    }
}