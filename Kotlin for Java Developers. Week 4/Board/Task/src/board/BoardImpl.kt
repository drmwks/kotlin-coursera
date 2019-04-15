package board

import board.Direction.*
import java.lang.IllegalArgumentException
import kotlin.math.max
import kotlin.math.min

class SquareBoardImpl(override val width: Int) : SquareBoard {
    private val cells : List<MutableList<Cell>>
    init {
        cells = mutableListOf()
        (0 .. width-1).forEach { i->
            cells.add(mutableListOf())
            (0 .. width-1).forEach {j->
                cells[i].add(Cell(i+1, j+1))
            }
        }
    }

    private fun isInvalidIndexes(i: Int, j: Int) : Boolean =
        i > width || j > width || i < 1 || j < 1

    override fun getCell(i: Int, j: Int): Cell {
        if ((isInvalidIndexes(i, j))) throw IllegalArgumentException("width of the board is $width")
        return cells[i-1][j-1]
    }

    override fun getAllCells(): Collection<Cell> =
        (1 .. width).map { getRow(it, 1..width) }.flatten()

    private fun getCorrectRange(range : IntProgression) : IntProgression =
    if (range.first > range.last) min(width, range.first).downTo(max(1, range.last)) else max(1, range.first) .. min(width, range.last)

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
        getCorrectRange(jRange).map { getCell(i, it) }.toList()

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
            getCorrectRange(iRange).map { getCell(it, j) }.toList()

    override fun Cell.getNeighbour(direction: Direction): Cell? =
        when (direction) {
            UP -> getCellOrNull(this.i-1, j)
            DOWN -> getCellOrNull(this.i+1, j)
            RIGHT -> getCellOrNull(this.i, j+1)
            LEFT -> getCellOrNull(this.i, j-1)
        }


    override fun getCellOrNull(i: Int, j: Int): Cell? {
        if (isInvalidIndexes(i, j)) return null
        return getCell(i, j)
    }

}

class GameBoardImpl<T>(override val width: Int) : GameBoard<T> {
    private val squareBoardImpl : SquareBoard
    private val map : HashMap<Cell, T?>
    init {
        squareBoardImpl = createSquareBoard(width)
        map = hashMapOf()
        (1 .. width).forEach { i->
            (1 .. width).forEach {j->
                map.put(squareBoardImpl.getCell(i, j), null)
            }
        }

    }

    override fun getCellOrNull(i: Int, j: Int): Cell? = squareBoardImpl.getCellOrNull(i, j)

    override fun getCell(i: Int, j: Int): Cell = squareBoardImpl.getCell(i, j)

    override fun getAllCells(): Collection<Cell> = squareBoardImpl.getAllCells()

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> = squareBoardImpl.getRow(i, jRange)

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> = squareBoardImpl.getColumn(iRange, j)

    override fun Cell.getNeighbour(direction: Direction): Cell? =
        when (direction) {
            UP -> getCellOrNull(this.i-1, j)
            DOWN -> getCellOrNull(this.i+1, j)
            RIGHT -> getCellOrNull(this.i, j+1)
            LEFT -> getCellOrNull(this.i, j-1)
        }

    override fun get(cell: Cell): T? = map.get(cell)

    override fun set(cell: Cell, value: T?)  {
        map.put(cell, value)
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> =
        map.filter { entry -> predicate(entry.value) }.map { it.key }.toList()


    override fun find(predicate: (T?) -> Boolean): Cell? =
            map.filter { entry -> predicate(entry.value) }.map { it.key }.getOrNull(0)

    override fun any(predicate: (T?) -> Boolean): Boolean =
            !map.filter { entry -> predicate(entry.value) }.map { it.key }.isEmpty()

    override fun all(predicate: (T?) -> Boolean): Boolean =
            map.filter { entry -> !predicate(entry.value) }.map { it.key }.isEmpty()

}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

