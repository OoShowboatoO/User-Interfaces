import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.effect.DropShadow
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.HBox
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.FileInputStream
import kotlin.random.Random

class ToolbarView(stage: Stage, private val model: Model) : IView, ToolBar() {
    init {
        val radioBox = ToggleGroup()
        val functionHBox = HBox()

        val sourcePath = "${System.getProperty("user.dir")}/src/main/resources/"

        val cascadeBtn = RadioButton("Cascade")
        cascadeBtn.isSelected = true
        val cascadeImg = ImageView(Image(FileInputStream(sourcePath + "cascade.png")))
        cascadeImg.fitWidth = 20.0
        cascadeImg.fitHeight = 20.0
        cascadeBtn.graphic = cascadeImg

        val tileBtn = RadioButton("Tile")
        val tileImg = ImageView(Image(FileInputStream(sourcePath + "tile.png")))
        tileImg.fitWidth = 20.0
        tileImg.fitHeight = 20.0
        tileBtn.graphic = tileImg

        cascadeBtn.toggleGroup = radioBox
        tileBtn.toggleGroup = radioBox

        val addBtn = Button("Add")
        val addImg = ImageView(Image(FileInputStream(sourcePath + "add.png")))
        addImg.fitWidth = 20.0
        addImg.fitHeight = 20.0
        addBtn.graphic = addImg

        val deleteBtn = Button("Delete")
        val deleteImg = ImageView(Image(FileInputStream(sourcePath + "delete.png")))
        deleteImg.fitWidth = 20.0
        deleteImg.fitHeight = 20.0
        deleteBtn.graphic = deleteImg

        val rotateLBtn = Button("Rotate Left")
        val rotateLImg = ImageView(Image(FileInputStream(sourcePath + "rotateL.png")))
        rotateLImg.fitWidth = 20.0
        rotateLImg.fitHeight = 20.0
        rotateLBtn.graphic = rotateLImg

        val rotateRBtn = Button("Rotate Right")
        val rotateRImg = ImageView(Image(FileInputStream(sourcePath + "rotateR.png")))
        rotateRImg.fitWidth = 20.0
        rotateRImg.fitHeight = 20.0
        rotateRBtn.graphic = rotateRImg

        val zoomInBtn = Button("Zoom In")
        val zoomInImg = ImageView(Image(FileInputStream(sourcePath + "zoomIn.png")))
        zoomInImg.fitWidth = 20.0
        zoomInImg.fitHeight = 20.0
        zoomInBtn.graphic = zoomInImg

        val zoomOutBtn = Button("Zoom Out")
        val zoomOutImg = ImageView(Image(FileInputStream(sourcePath + "zoomOut.png")))
        zoomOutImg.fitWidth = 20.0
        zoomOutImg.fitHeight = 20.0
        zoomOutBtn.graphic = zoomOutImg

        val resetBtn = Button("Reset")
        val resetImg = ImageView(Image(FileInputStream(sourcePath + "reset.png")))
        resetImg.fitWidth = 20.0
        resetImg.fitHeight = 20.0
        resetBtn.graphic = resetImg

        functionHBox.children.addAll(addBtn, deleteBtn, rotateLBtn, rotateRBtn,
            zoomInBtn, zoomOutBtn, resetBtn)

        // add buttons to toolbar
        val styleBar = HBox()
        styleBar.children.addAll(cascadeBtn, tileBtn)

        val maxBtn = Button("MAX")
        val minBtn = Button("MIN")
        val restoreBtn = Button("Restore")
        this.items.addAll(functionHBox, styleBar, maxBtn, minBtn, restoreBtn)


        addBtn.setOnAction { event ->
            val fileChooser = FileChooser()
            fileChooser.title = "Pick an image"
            val file = fileChooser.showOpenDialog(stage)
            if (file != null) {
                if (file.name.endsWith(".jpg") ||
                    file.name.endsWith(".png") ||
                    file.name.endsWith(".bmp")) {
                    model.files.add(file)
                    val pic = FileInputStream(file.absolutePath)
                    var imgViewPic = ImageView(Image(pic))
                    pic.close()
                    imgViewPic.isPreserveRatio = true
                    imgViewPic.fitHeight = 250.0
                    imgViewPic.fitWidth = 250.0
                    imgViewPic.x = Random.nextDouble(0.0, 100.0)
                    imgViewPic.y = Random.nextDouble(0.0, 100.0)

                    model.pictures.add(imgViewPic)
                    model.totalImg = model.files.size
                    model.notifyViews()
                } else {
                    val alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "Invalid File"
                    alert.contentText = "The selected file is invalid."
                    alert.showAndWait()
                }
            }
        }

        deleteBtn.setOnMouseClicked { event->
            if (model.hasSelected) {
                for (pic in model.pictures) {
                    if (pic.id == model.selectedImg.id) {
                        model.pictures.remove(pic)
                        model.totalImg = model.pictures.size
                        model.notifyViews()
                        break
                    }
                }
            }
        }

        rotateLBtn.setOnMouseClicked { event ->
            if (model.hasSelected) {
                model.selectedImg.rotate -= 10.0
                // model.notifyViews()
            }

        }


        rotateRBtn.setOnMouseClicked { event ->
            if (model.hasSelected) {
                model.selectedImg.rotate += 10.0
                // model.notifyViews()

            }
        }

        zoomInBtn.setOnMouseClicked { event ->
            if (model.hasSelected) {
                model.selectedImg.fitWidth *= 1.25
                model.selectedImg.fitHeight *= 1.25
                // model.notifyViews()

            }
        }

        zoomOutBtn.setOnMouseClicked { event ->
            if (model.hasSelected) {
                model.selectedImg.fitWidth *= 0.75
                model.selectedImg.fitHeight *= 0.75
//                model.notifyViews()

            }
        }

        resetBtn.setOnMouseClicked {
            for (pic in model.pictures) {
                pic.isPreserveRatio = true
                pic.fitWidth = 250.0
                pic.fitHeight = 250.0
                pic.rotate = 0.0
            }
//            model.notifyViews()
        }


        cascadeBtn.setOnMouseClicked {
            model.isCascade = true

            addBtn.isDisable = false
            deleteBtn.isDisable = false
            rotateLBtn.isDisable = false
            rotateRBtn.isDisable = false
            zoomInBtn.isDisable = false
            zoomOutBtn.isDisable = false
            resetBtn.isDisable = false
            model.notifyViews()
        }

        tileBtn.setOnMouseClicked {
            model.isCascade = false

            addBtn.isDisable = true
            deleteBtn.isDisable = true
            rotateLBtn.isDisable = true
            rotateRBtn.isDisable = true
            zoomInBtn.isDisable = true
            zoomOutBtn.isDisable = true
            resetBtn.isDisable = true

            model.tilePic.children.clear()
            model.tilePic.prefHeight = model.paneHeight - 20.0
            model.tilePic.prefWidth = model.paneWidth - 20.0
            for(pic in model.pictures) {
                pic.rotate = 0.0
                val unselectShadow = DropShadow()
                unselectShadow.color = Color.WHITE
                pic.effect = unselectShadow
                model.tilePic.children.add(pic)
            }
            model.setTileSize()
        }

        maxBtn.setOnMouseClicked {
            stage.width = 1600.0
            stage.height = 1200.0
        }

        minBtn.setOnMouseClicked {
            stage.width = 950.0
            stage.height = 600.0
        }

        restoreBtn.setOnMouseClicked {
            stage.width = 1000.0
            stage.height = 800.0
        }

    }


    override fun update() {
        // update my button state
        // how do we get data from the model?
        // println("ToolbarView is updated")
    }
}