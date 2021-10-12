import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.stage.Stage
import java.io.File
import kotlin.system.exitProcess
import javafx.scene.control.ListView;
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import java.io.FileInputStream

import java.util.*

import javafx.scene.control.Label
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.input.*
import javafx.scene.layout.HBox
import javafx.stage.DirectoryChooser



class Main : Application() {

    private val root = File("${System.getProperty("user.dir")}/test/")
    private var history = mutableListOf<File>()
    private var list = mutableListOf<File>()
    private var tree = ListView<String>()
    private var fileMap = mutableMapOf<String, File>()
    private var text = TextArea("")
    private var image = ImageView()
    private var path =  Label("${System.getProperty("user.dir")}/test/")
    private val layout = BorderPane()
    private var showhidden = false
    private var thisFile = root

    override fun start(stage: Stage) {
        //--------------------------------------Local Function---------------------------------------//
        fun moveFuntion() {
            if (tree.selectionModel.isEmpty) {
                return
            }
            var currFileName = tree.selectionModel.selectedItems[0]
            val targetFile = fileMap?.get(currFileName)

            val chooser = DirectoryChooser()
            chooser.initialDirectory = root
            chooser.title = "Choose a file"
            val destinationDir = chooser.showDialog(stage)
            if (destinationDir != null) {
                // println(destinationDir)
                val destinationPath = destinationDir.toString() + "/" + currFileName
                val finalFile = File(destinationPath)
                if (finalFile.exists()) {
                    val alert = Alert(Alert.AlertType.ERROR)
                    alert.title = "ERROR"
                    alert.contentText = "The file with same name already exists."
                    alert.showAndWait()
                } else {
                    val success = targetFile?.renameTo(finalFile)
                    if (success != null && !success) {
                        val alert = Alert(Alert.AlertType.ERROR)
                        alert.title = "ERROR"
                        alert.contentText = "Rename failed."
                        alert.showAndWait()
                    }
                    list.remove(targetFile) //// ??????????????????????????
                    fileMap.remove(currFileName)
                }
            }
            getCurrList(thisFile)
            getFileListView(list, showhidden)
            layout.left = tree
            layout.bottom = Label(thisFile.toString())
        }

        //------------------------------Start-----------------------------------//
        stage.title = "File Browser"
        getCurrList(thisFile)

        //----------------------------- Menu Bar-----------------------------//
        // top: menubar
        val menuBar = MenuBar()
        val fileMenu = Menu("File")
        val viewMenu = Menu("View")
        val actionsMenu = Menu("Actions")
        val optionsMenu = Menu("Options")

        menuBar.menus.addAll(fileMenu, viewMenu, actionsMenu, optionsMenu)

        // Section for File
        val fileQuit = MenuItem("Quit")
        fileMenu.items.addAll(fileQuit)

        // handle default user action aka press
        fileQuit.setOnAction {
            exitProcess(0)
        }

        // fileNew.accelerator = KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN)

        // Section for View
        val viewPrev = MenuItem("Prev")
        viewPrev.accelerator = KeyCodeCombination(KeyCode.BACK_SPACE)
        val viewNext = MenuItem("Next")
        // viewNext.accelerator = KeyCodeCombination(KeyCode.A)
        viewMenu.items.addAll(viewPrev, viewNext)

        // Section for Actions
        val actMove = MenuItem("Move")
        val actRename = MenuItem("Rename")
        val actDelete = MenuItem("Delete")
        actionsMenu.items.addAll(actMove, actDelete, actRename)

        // Section for Options
        val hiddenToggle = RadioMenuItem("Show Hidden Files")
        optionsMenu.items.add(hiddenToggle)

        // Settings for Menu buttons
        viewNext.setOnAction {
            goToNext()
        }

        viewPrev.setOnAction {
            prevFunction()
        }

        actMove.setOnAction {
            moveFuntion()
        }

        actRename.setOnAction {
            renameFunction()
        }

        actDelete.setOnAction {
            deleteFuntion()
        }

        hiddenToggle.isSelected = false
        hiddenToggle.setOnAction {
            showhidden = !showhidden
            getCurrList(thisFile)
            getFileListView(list, showhidden)
            layout.left = tree
            layout.bottom = Label(root.toString())
        }

        //----------------------------- Tool Bar-----------------------------//
        // toolBar()
        val toolBar = ToolBar()
        val sourcePath = "${System.getProperty("user.dir")}/src/main/resources/"

        val homeBtn = Button("Home")
        val homePic = ImageView(Image(FileInputStream(sourcePath + "Home.png")))
        homePic.fitWidth = 15.0
        homePic.fitHeight = 15.0
        homeBtn.graphic = homePic

        val prevBtn = Button("Prev")
        val prevPic = ImageView(Image(FileInputStream(sourcePath + "Prev.png")))
        prevPic.fitWidth = 15.0
        prevPic.fitHeight = 15.0
        prevBtn.graphic = prevPic

        val nextBtn = Button("Next")
        val nextPic = ImageView(Image(FileInputStream(sourcePath + "Next.png")))
        nextPic.fitWidth = 15.0
        nextPic.fitHeight = 15.0
        nextBtn.graphic = nextPic

        val moveBtn = Button("Move")
        val movePic = ImageView(Image(FileInputStream(sourcePath + "Move.jpg")))
        movePic.fitWidth = 15.0
        movePic.fitHeight = 15.0
        moveBtn.graphic = movePic

        val deleteBtn = Button("Delete")
        val deletePic = ImageView(Image(FileInputStream(sourcePath + "Delete.png")))
        deletePic.fitWidth = 15.0
        deletePic.fitHeight = 15.0
        deleteBtn.graphic = deletePic


        val renameBtn = Button("Rename")
        val renamePic = ImageView(Image(FileInputStream(sourcePath + "Rename.png")))
        renamePic.fitWidth = 15.0
        renamePic.fitHeight = 15.0
        renameBtn.graphic = renamePic

        toolBar.items.addAll(homeBtn, prevBtn, nextBtn, moveBtn, deleteBtn, renameBtn)

        val topBtn = VBox()
        topBtn.children.addAll(menuBar, toolBar)

        // Home Button
        homeBtn.setOnAction {
            thisFile = root
            getCurrList(thisFile)
            getFileListView(list, showhidden)
            layout.left = tree
            layout.bottom = Label(thisFile.toString())
        }
        // Prev Button
        prevBtn.setOnAction {
            prevFunction()
        }

        // Next Button
        nextBtn.setOnAction {
            goToNext()
        }

        // Move Button
        moveBtn.setOnAction {
            moveFuntion()
        }

        // Delete Button
        deleteBtn.setOnAction {
            deleteFuntion()
        }

        // Rename Button
        renameBtn.setOnAction {
            renameFunction()
        }

        // ------------------------------- File List & Click Actions ------------------------------------//
        // left: tree
        getFileListView(list, showhidden)

        // handle mouse clicked action
        tree.setOnMouseClicked { event ->
            if (!tree.selectionModel.isEmpty) {
                var fileName = tree.selectionModel.selectedItems[0]

                if (fileName.endsWith(".txt") || fileName.endsWith(".md")) { // TXT file is found
                    showTxtContent(fileName)

                } else if (fileName.endsWith(".png") || fileName.endsWith(".jpg") ||
                    fileName.endsWith(".bmp")
                ) {

                    showImage(fileName)

                } else if (fileName.endsWith("/")) {
                    text.text = ""
                    layout.center = text
                    var dirPath = fileMap?.get(fileName)
                    layout.bottom = Label(dirPath.toString())

                    if (event.clickCount == 2) {
                        goToNext()
                    }

                } else {
                    text.text = ""
                    layout.center = text
                }
            }
        }

        menuBar.focusTraversableProperty().set(false)
        toolBar.focusTraversableProperty().set(false)
        tree.focusTraversableProperty().set(true)
        text.focusTraversableProperty().set(false)
        homeBtn.focusTraversableProperty().set(false)
        prevBtn.focusTraversableProperty().set(false)
        nextBtn.focusTraversableProperty().set(false)
        moveBtn.focusTraversableProperty().set(false)
        deleteBtn.focusTraversableProperty().set(false)
        renameBtn.focusTraversableProperty().set(false)

        // build the scene graph
        image.fitHeight = 300.0
        image.fitWidth = 500.0
        image.isPreserveRatio = true

        layout.top = topBtn
        layout.left = tree
        layout.bottom = path

        val scene = Scene(layout)

        //-------------------------------------- Hotkey Settings ---------------------------------//

        var handler = EventHandler<KeyEvent> { event ->
            if (!tree.selectionModel.isEmpty) {
                var fileName = tree.selectionModel.selectedItems[0]
                if (fileName != null) {
                    if (event.code === KeyCode.ENTER) {
                        if (fileName.endsWith(".txt") || fileName.endsWith(".md")) { // TXT file is found
                            showTxtContent(fileName)

                        } else if (fileName.endsWith(".png") || fileName.endsWith(".jpg") ||
                            fileName.endsWith(".bmp")
                        ) {
                            showImage(fileName)

                        } else if (fileName.endsWith("/")) {
                            goToNext()

                        } else {
                            text.text = ""
                            layout.center = text
                        }
                    } else if (event.code === KeyCode.DELETE) {
                        deleteFuntion()
                    }
                }
            }
        }
        tree.onKeyPressed = handler

        stage.width = 800.0
        stage.height = 500.0
        stage.scene = scene

        stage.show()
        // --------------------End of the Application------------------------//


    }

    private fun getCurrList(currFile: File): MutableList<File>{
        list = mutableListOf<File>()
        for (item in currFile.listFiles()) {
            list.add(item)
        }
        return list
    }

    private fun getFileListView(filelist: MutableList<File>, showHidden: Boolean) {
        fileMap.clear()
        tree.items.clear()
        for (item in filelist) {
            var is_hidden = false
            var filename = item.name
            var index = filename.lastIndexOf("/")
            var hiddenSign = filename[index + 1]
            if (hiddenSign == '.') {
                is_hidden = true
            }
            var simpleName = filename.substring(index + 1)
            if (item.isDirectory) {
                simpleName = simpleName + "/"
            } else if (!showHidden && is_hidden) {
                continue
            }
            tree.items.add(simpleName)
            fileMap?.put(simpleName, item)
        }
    }

    private fun showTxtContent(fileName: String){

        var targetfile = fileMap?.get(fileName)

        if (targetfile != null) {

            var content = targetfile.readText()

            text.text = content
            // text.style = "-fx-text-fill: red; -fx-font-size: 16px"
            text.isWrapText = true
            layout.center = text
            layout.bottom = Label(targetfile.toString())
        }


    }

    private fun showImage(picName: String) {
        var targetPic = fileMap?.get(picName)
        if (targetPic != null) {
            var imgInput = FileInputStream(targetPic)
            val chosenPic = Image(imgInput)
            imgInput.close()
            image.image = chosenPic
            layout.center = image
            layout.bottom = Label(targetPic.toString())
        }

    }

    private fun goToNext() {
        if (tree.selectionModel.isEmpty) {
            return
        }
        var fileName = tree.selectionModel.selectedItems[0]
        if (fileName.endsWith("/")) {
            history.add(thisFile)
            var dirPath = fileMap?.get(fileName)
            var subDir = File(dirPath.toString() + "/")
            thisFile = File(subDir.toString() + "/")
            getCurrList(thisFile)
            getFileListView(list, showhidden)
            layout.left = tree
            layout.bottom = Label(thisFile.toString())

        }
    }

    private fun prevFunction() {
        var lastIndex = history.lastIndex
        if (lastIndex > -1) {
            var prevfile = history[lastIndex]
            history.removeLast()
            thisFile = prevfile
            getCurrList(thisFile)
            getFileListView(list, showhidden)
            layout.left = tree
            layout.bottom = Label(thisFile.toString())

        }
    }

    private fun renameFunction() {
        if (tree.selectionModel.isEmpty) {
            return
        }
        var currFileName = tree.selectionModel.selectedItems[0]
        var currfile = fileMap?.get(currFileName)

        val renameDialog = TextInputDialog("")
        renameDialog.title = "Rename File"
        renameDialog.headerText = "Please enter the new name"

        // Check validation
        val result = renameDialog.showAndWait()
        var newFileName = ""

        if(result.isPresent) {
            newFileName = result.get()
        } else {
            renameDialog.close()
            return
        }

        // Rename File
        val newFilePath = thisFile.toString() + "/" + newFileName
        val newFile = File(newFilePath)

        if (newFile.exists()) {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.title = "Warning"
            alert.contentText = "This new file name is invalid."
            alert.showAndWait()
        } else {
            val success = currfile?.renameTo(newFile)
            if (success != null && !success) {
                val alert = Alert(Alert.AlertType.ERROR)
                alert.title = "ERROR"
                alert.contentText = "Rename failed."
                alert.showAndWait()
            }
        }
        getCurrList(thisFile)
        getFileListView(list, showhidden)
        layout.left = tree
        layout.bottom = Label(thisFile.toString())
    }

    private fun deleteFuntion() {
        if (tree.selectionModel.isEmpty) {
            return
        }
        var currFileName = tree.selectionModel.selectedItems[0]
        var currfile = fileMap?.get(currFileName)
        // println(currfile.toString())

        val confirmation = Alert(Alert.AlertType.CONFIRMATION)
        confirmation.title = "Delete File"
        confirmation.headerText = "Make sure you want to delete the file"
        val result = confirmation.showAndWait()
        if (result.isPresent && result.get() == ButtonType.OK) {
            val success = currfile?.deleteRecursively()
            // println(success)
            if (success != null && !success) {
                val errMsg = Alert(Alert.AlertType.ERROR)
                errMsg.title = "ERROR: Delete File"
                errMsg.contentText = "The file cannot be deleted"
                errMsg.showAndWait()
            }
        }

        getCurrList(thisFile)
        getFileListView(list, showhidden)
        layout.left = tree
        layout.bottom = Label(thisFile.toString())

    }

}
