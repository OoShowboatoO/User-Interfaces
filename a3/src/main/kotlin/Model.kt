import javafx.scene.image.ImageView
import javafx.scene.layout.FlowPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.TilePane
import java.awt.ScrollPane
import java.io.File

class Model {

    // represent my board
    var totalImg = 0
    val files = ArrayList<File>()
    val pictures = ArrayList<ImageView>()
    private val views = ArrayList<IView>()
    var hasSelected = false
    var prevSelectedImg = ImageView()
    var selectedImg = ImageView()
    var tilePic = TilePane(3.0, 3.0)
    var isCascade = true
    var paneHeight = 725.0
    var paneWidth = 1000.0


    fun setCanvasWidth(num: Double) {
        paneWidth = num
    }

    fun setCanvasHeight(num: Double) {
        paneHeight = num
    }

    fun reposition() {
        for (pic in pictures) {
            if (pic.boundsInParent.maxX > paneWidth) {
                pic.x = paneWidth - pic.maxWidth(pic.fitWidth) - 20.0
            }

            if (pic.boundsInParent.maxY > paneHeight - 20.0){
                pic.y = paneHeight - pic.maxHeight(pic.fitHeight) - 20.0
            }
        }
        notifyViews()
    }

    fun setTileSize() {
        tilePic.prefHeight = paneHeight - 20.0
        tilePic.prefWidth = paneWidth - 20.0
//        println("canvas height is $paneHeight")
//        println("tile height is ${tilePic.height}")
//        println("tile prefer height is ${tilePic.prefHeight}")
//        if (tilePic.prefHeight > paneHeight - 20.0) {
//            var rowNum = tilePic.prefRows
//            var colNum = tilePic.prefColumns
//            for (pic in tilePic.children) {
//                val thisPic: ImageView = pic as ImageView
//                thisPic.fitWidth = (paneWidth - 100.0) / colNum
//                thisPic.fitHeight = (paneHeight - 100.0) / rowNum
//                thisPic.isPreserveRatio = true
//            }
//        }
        notifyViews()
    }


    // view management
    fun addView(view: IView) {
        views.add(view)
    }


    fun removeView(view: IView) {
        views.remove(view)
    }


    fun notifyViews() {
        for (view in views) {
            view.update()
        }
    }

}
