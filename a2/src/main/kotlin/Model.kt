
class Model {
    private var frameCounter = 0
    private var clickedCell = Pair(-1, -1)
    private var thisShape = "Clear"
    // represent my board
    private val sizeOuter = 50
    private val sizeInner = 75
    private var updateStatus = false
    private var validCell = false

    private val views = ArrayList<IView>()
    private val board = Array(sizeInner) { BooleanArray(sizeOuter) }
    private val neighborBoard = Array(sizeInner) { IntArray(sizeOuter) }

    init {
        for (i in 0 .. 74) {
            for (j in 0 .. 49) {
                board[i][j] = false
            }
        }
        frameCounter = 0
    }

    // board manipulation
    // (a) add a shape
    fun addShape(shapeName: String, xVal: Int, yVal: Int) {
//        println("x value is $xVal")
//        println("y value is $yVal")
        when (shapeName) {
            // Block Shape
            "Block" -> {
                // println("select Block shape")
                if (xVal + 1 < 75 && yVal + 1 < 50) {
                    validCell = true
                    board[xVal][yVal] = true
                    board[xVal + 1][yVal] = true
                    board[xVal][yVal + 1] = true
                    board[xVal + 1][yVal + 1] = true
                }
            }

            // Beehive
            "Beehive" -> {
                if (xVal + 3 < 75 && yVal + 2 < 50) {
                    validCell = true
                    board[xVal + 1][yVal] = true
                    board[xVal + 2][yVal] = true
                    board[xVal][yVal + 1] = true
                    board[xVal + 3][yVal + 1] = true
                    board[xVal + 1][yVal + 2] = true
                    board[xVal + 2][yVal + 2] = true
                }
            }

            // Blinker
            "Blinker" -> {
                if (xVal + 2 < 75 && yVal + 2 < 50) {
                    validCell = true
                    board[xVal][yVal + 1] = true
                    board[xVal + 1][yVal + 1] = true
                    board[xVal + 2][yVal + 1] = true
                }
            }
            // Toad
            "Toad" -> {
                if (xVal + 3 < 75 && yVal + 1 < 50) {
                    validCell = true
                    board[xVal + 1][yVal] = true
                    board[xVal + 2][yVal] = true
                    board[xVal + 3][yVal] = true
                    board[xVal][yVal + 1] = true
                    board[xVal + 1][yVal + 1] = true
                    board[xVal + 2][yVal + 1] = true
                }
            }

            // Glider
            "Glider" -> {
                if (xVal + 2 < 75 && yVal + 2 < 50) {
                    validCell = true
                    board[xVal + 2][yVal] = true
                    board[xVal][yVal + 1] = true
                    board[xVal + 2][yVal + 1] = true
                    board[xVal + 1][yVal + 2] = true
                    board[xVal + 2][yVal + 2] = true
                }
            }

        }
    }

    fun isCellValid(): Boolean {
        return validCell
    }

    fun setCellValid(value: Boolean) {
        validCell = value
    }

    fun setClick(x_Val: Int, y_Val:Int) {
        clickedCell = clickedCell.copy(x_Val, y_Val)

    }


    fun getClick():  Pair<Int, Int> {
        return clickedCell
    }


    fun getModelInfo(): Array<BooleanArray> {
        return board
    }


    // (b) clear the board
    fun clearBoard() {
        for (i in 0 .. 74) {
            for (j in 0 .. 49) {
                board[i][j] = false
            }
        }
    }


    fun getFrameCounter(): Int {
        return frameCounter
    }


    fun setShape(shape: String) {
        thisShape = shape
    }


    fun getSelectedShape(): String {
        return thisShape
    }

    // sizeInner = 75 && sizeOuter = 50
    private fun countNeighbours(col: Int, row:Int): Int {
        var neighborNum = 0

        // top left
        if (col - 1 >= 0 && row - 1 >= 0) {
            if (board[col - 1][row - 1]) {
                neighborNum += 1
            }
        }
        // top
        if (row - 1 >= 0) {
            if (board[col][row - 1]) {
                neighborNum += 1
            }
        }
        // top right
        if (col + 1 < sizeInner && row - 1 >= 0) {
            if (board[col + 1][row - 1]) {
                neighborNum += 1
            }
        }
        // left
        if (col - 1 >= 0) {
            if (board[col - 1][row]) {
                neighborNum += 1
            }
        }
        // right
        if (col + 1 < sizeInner) {
            if (board[col + 1][row]) {
                neighborNum += 1
            }
        }
        // bottom left
        if (col - 1 >= 0 && row + 1 < sizeOuter) {
            if (board[col - 1][row + 1]) {
                neighborNum += 1
            }
        }
        // bottom
        if (row + 1 < sizeOuter) {
            if (board[col][row + 1]) {
                neighborNum += 1
            }
        }
        // bottom right
        if (col + 1 < sizeInner && row + 1 < sizeOuter) {
            if (board[col + 1][row + 1]) {
                neighborNum += 1
            }
        }

        return neighborNum
    }

    fun selfUpdate() {
        // figure out the number of neighbours for each cell in the grid
        for (i in 0 .. 74) {
            for (j in 0 .. 49) {
                neighborBoard[i][j] = countNeighbours(i, j)
            }
        }

        for (i in 0 .. 74) {
            for (j in 0 .. 49) {
                if (board[i][j]) {
                    if (neighborBoard[i][j] < 2) {
                        // Any live cell with fewer than two live neighbours dies
                        board[i][j] = false
                    } else if (neighborBoard[i][j] > 3) {
                        // Any live cell with more than three live neighbours dies
                        board[i][j] = false
                    }
                } else {
                    if (neighborBoard[i][j] == 3) {
                        // Any dead cell with exactly three live neighbours becomes a live cell
                        board[i][j] = true
                    }
                }
            }
        }
    }

    fun setStatusUpdate(value: Boolean) {
        updateStatus = value
    }

    fun getStatusUpdate(): Boolean {
        return updateStatus
    }


    // view management
    fun addView(view: IView) {
        views.add(view)
    }

    fun removeView(view: IView) {
        views.remove(view)
    }

    private fun notifyViews() {
        for (view in views) {
            view.update()
        }
    }

    fun incrementFrameCounter() {
        frameCounter++
        this.notifyViews()
    }
}
