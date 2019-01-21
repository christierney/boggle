This (embedded) is the Kotlin scratch file I used to work out the algorithm I wanted to use. It's
more functional/data-oriented; I ultimately put it into more of an OO structure.

```kotlin
/*
The game begins by shaking a covered tray of 16 cubic dice, each with a different letter printed on each of its sides.
The dice settle into a 4×4 tray so that only the top letter of each cube is visible.
...
Each player searches for words that can be constructed from the letters of sequentially adjacent cubes, where
"adjacent" cubes are those horizontally, vertically, and diagonally neighboring. Words must be at least three letters
long, may include singular and plural (or other derived forms) separately, but may not use the same letter cube more
than once per word.
 */

import java.io.File
import kotlin.math.absoluteValue

// We can represent a Board as a list of lists, one list of letters per row of the board.
typealias Board = List<List<Char>>
val sampleBoard: Board = listOf(
        listOf('T', 'E', 'O', 'V'),
        listOf('E', 'S', 'R', 'F'),
        listOf('W', 'N', 'A', 'P'),
        listOf('C', 'A', 'R', 'I')
)

// For identifying locations on the board, we can use coordinate pairs. (0,0) will be the upper left.
typealias Cube = Pair<Int, Int>

// It will be useful to generate a list of all the valid coordinates for an n x n board.
fun cubes(n: Int) = (0 until n).flatMap { i -> (0 until n).map { j -> Pair(i, j) } }

val cs = cubes(4) // [(0, 0), (0, 1), (0, 2), (0, 3), (1, 0), (1, 1), (1, 2), (1, 3), (2, 0), (2, 1), (2, 2), (2, 3), (3, 0), (3, 1), (3, 2), (3, 3)]

// We'll need to be able to check if one cube is adjacent to another.
fun Cube.isAdj(o: Cube): Boolean =
        this != o &&
                (o.first - this.first).absoluteValue <= 1 &&
                (o.second - this.second).absoluteValue <= 1

val c = Cube(0, 1)
cs.filter { it.isAdj(c) } // [(0,0), (0,2), (1,0), (1,1), (1,2)]

// We should be able to get the letter on the Board at a given Cube.
fun Board.letterAt(c: Cube) = this[c.first][c.second]

sampleBoard.letterAt(c) // E

// We can find valid words on the board by looping over a word list. For each word, can we make it on the board using
// adjacent cubes?

fun Board.wordFrom(w: String, current: Cube, cubes: List<Cube>): Boolean = when {
    w.isEmpty() -> true
    else -> cubes.any {
        it.isAdj(current) && this.letterAt(it).equals(w.first(), true) &&
                this.wordFrom(w.drop(1), it, cubes - it)
    }
}

fun Board.containsWord(w: String): Boolean {
    val cubes = cubes(this.size)
    return cubes.any {
        this.letterAt(it).equals(w.first(), true) &&
                this.wordFrom(w.drop(1), it, cubes - it)
    }
}

sampleBoard.containsWord("piranas") // true
sampleBoard.containsWord("PIRANAS") // true


val words = File("/usr/share/dict/words").readLines()
val answers = words.filter { it.length > 2 &&  sampleBoard.containsWord(it) }
answers.size // 307
answers.take(10) // [acne, afore, afreet, afret, air, Aira, airan, Ana, ana, Anas]

// TODOs:
// * handle q/qu
// * handle non-square boards
```
