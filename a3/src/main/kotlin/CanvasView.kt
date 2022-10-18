import javafx.scene.control.ScrollPane
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.io.FileInputStream


class CanvasView(stage: Stage, private val model: Model): IView, Pane() {

    private var startX = -1.0
    private var startY = -1.0

    private enum class STATE { NONE, DRAG }
    private var state = STATE.NONE

    var isPicPressed = false


    init {

        this.prefWidth = model.paneWidth
        this.prefHeight = model.paneHeight
        this.setOnMouseClicked {
            if (!isPicPressed) {
                val unselectShadow = DropShadow()
                unselectShadow.color = Color.WHITE
                model.selectedImg.effect = unselectShadow
                model.hasSelected = false
                // println("hasSelected is FALSE now")
            } else {
                model.hasSelected = true
                isPicPressed = false
                // println("isPicPressed is FALSE")
            }

            // println("click on Pane")
        }

    }


    override fun update() {
        this.prefWidth = model.paneWidth
        this.prefHeight = model.paneHeight
        this.children.clear()

        if (model.isCascade) {
            for (picture in model.pictures) {
                // Reference: 04.graphics / 04.drag_shape
                picture.setOnMousePressed { event ->
                    startX = event.x
                    startY = event.y
                    if (model.isCascade) {
                        state = STATE.DRAG
                        isPicPressed = true
                        // println("isPicPressed is True")
                        picture.toFront()
                        // model.hasSelected = true
                        // println("hasSelected is TRUE now")
                        model.prevSelectedImg = model.selectedImg
                        model.selectedImg = picture
                        val preShadow = DropShadow()
                        val currShadow = DropShadow()
                        preShadow.color = Color.WHITE
                        currShadow.color = Color.BLUE
                        model.prevSelectedImg.effect = preShadow
                        model.selectedImg.effect = currShadow
                        // event.consume()
                    }

                    // println("setOnMouseClicked")
                }

                // Reference: 07.layout / 07.boundaries
                picture.setOnMouseDragged { event ->
                    if (state == STATE.DRAG) {
                        val dx = event.x - startX
                        val dy = event.y - startY

                        if (picture.boundsInParent.minX + dx >= 0.0 &&
                            picture.boundsInParent.maxX + dx <= model.paneWidth - 20.0
                        ) {
                            picture.x += dx
                            startX = event.x
                        }

                        if (picture.boundsInParent.minY + dy >= 0.0 &&
                            picture.boundsInParent.maxY + dy <= model.paneHeight - 20.0
                        ) {
                            picture.y += dy
                            startY = event.y
                        }


                    }

                }

                picture.setOnMouseReleased {
                    // event.consume()
                    state = STATE.NONE
                    // model.hasSelected = true
                    // println("hasSelected is TRUE now 1")

                }

                picture.setOnMouseExited {

                    state = STATE.NONE
                    // model.hasSelected = true
                    // println("hasSelected is TRUE now 2")
                    // event.consume()
                }
                this.children.add(picture)
            }
        } else {
            this.children.add(model.tilePic)
        }


    }
}
