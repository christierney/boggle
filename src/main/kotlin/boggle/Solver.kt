package boggle

import kotlin.math.absoluteValue

/**
 * Represents a square Boggle board and provides a function for checking if a word is on the board.
 * Currently assumes board is square but does not validate that.
 */
class Board(val letters: List<List<String>>) {
    /**
     * Length of one side of the board (e.g. 4 for a 4x4 board.)
     */
    val size = letters.size

    /**
     * All valid Cubes for this board.
     */
    val allCubes = (0 until size).flatMap { i -> (0 until size).map { j -> Cube(i, j) } }

    /**
     * Returns the "letter" on the board at a given Cube. (A "letter" may actually be a multi-character string like "qu".)
     */
    fun letterAt(c: Cube): String = letters[c.x][c.y]

    /**
     * Returns true if the board contains word `w` according to the rules of Boggle.
     */
    fun containsWord(w: String): Boolean = wordFrom(w, null, allCubes)

    /**
     * Returns true if the word `w` is reachable from `current` Cube given the list of remaining available `cubes`.
     */
    private fun wordFrom(w: String, current: Cube?, cubes: List<Cube>): Boolean = when {
        w.isEmpty() -> true
        else -> cubes.any {
            // null current means we are starting a new word and have to pick the first cube
            (current == null || it.isAdj(current)) &&
                    w.startsWith(this.letterAt(it), true) &&
                    this.wordFrom(w.drop(this.letterAt(it).length), it, cubes - it)
        }
    }
}

/**
 * Represents a single position on a board.
 */
data class Cube(val x: Int, val y: Int) {
    /**
     * Returns true if another cube `o` is horizontally, vertically, or diagonally adjacent to this cube.
     * A cube is not adjacent to itself.
     *
     * @param o other Cube to test for adjacency
     */
    fun isAdj(o: Cube): Boolean =
            this != o &&
                    (o.x - this.x).absoluteValue <= 1 &&
                    (o.y - this.y).absoluteValue <= 1
}

class Solver(val dict: Collection<String>) {

    /**
     * Find all valid words in `board` that exist in wordlist `dict`.
     */
    fun findValidWords(board: Board): Collection<String> =
            dict.filter { word ->
                board.containsWord(word)
            }
}