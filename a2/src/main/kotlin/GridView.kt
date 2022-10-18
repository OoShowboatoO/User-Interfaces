import javafx.geometry.Pos
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle



class GridView(private val model: Model, private var timer: TimeManager): IView, GridPane() {
    private val gridHeight = 50 - 1
    private val gridWidth = 75 - 1

    init{
        this.alignment= Pos.CENTER
        this.isGridLinesVisible = true
        this.hgap = 1.0
        this.vgap = 1.0

        for (i in 0 .. gridWidth) {
            for (j in 0 .. gridHeight) {
                val rect = Rectangle(10.0, 10.0, Color.WHITE)
                this.add(rect, i, j)

                rect.setOnMouseClicked {
                    // println("mouse clicked")
                    model.setCellValid(false)
                    val x_Val = getColumnIndex(rect)
                    val y_Val = getRowIndex(rect)
                    model.setClick(x_Val, y_Val)
                    var selectedShape = model.getSelectedShape()
                    model.addShape(selectedShape, x_Val, y_Val)
                    if (timer.isPaused) {
                         model.incrementFrameCounter()
                    }
                    model.setStatusUpdate(true)
                }
            }
        }
    }


    override fun update() {
        // var startTime = System.currentTimeMillis()

        // update my button state
        // how do we get data from the model?
        // println("GridView is updated")
        var currBoard = model.getModelInfo()

        this.alignment= Pos.CENTER
        this.isGridLinesVisible = true
        this.hgap = 1.0
        this.vgap = 1.0
//
        for (child in this.children) {
            if (child is Rectangle) {
                var cell: Rectangle = child as Rectangle
                val x_Val = getColumnIndex(cell)
                val y_Val = getRowIndex(cell)
                if (currBoard[x_Val][y_Val]) {
                    cell.fill = Color.BLACK
                } else {
                    cell.fill = Color.WHITE
                }
            }

        }

    }
}
