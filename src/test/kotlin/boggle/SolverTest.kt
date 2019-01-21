package boggle

import kotlin.test.*

class SolverTest {
    val b = Board(listOf(
            listOf("a", "b", "c"),
            listOf("d", "e", "f"),
            listOf("g", "h", "qu")
    ))

    @Test
    fun `allCubes returns full board`() {
        assertEquals(listOf(
                Cube(0, 0), Cube(0, 1), Cube(0, 2),
                Cube(1, 0), Cube(1, 1), Cube(1, 2),
                Cube(2, 0), Cube(2, 1), Cube(2, 2)
        ),
                b.allCubes)
    }

    @Test
    fun `letterAt returns the character at a given position`() {
        assertEquals("a", b.letterAt(Cube(0,0)))
        assertEquals("b", b.letterAt(Cube(0,1)))
        assertEquals("c", b.letterAt(Cube(0,2)))
        assertEquals("d", b.letterAt(Cube(1,0)))
        assertEquals("e", b.letterAt(Cube(1,1)))
        assertEquals("f", b.letterAt(Cube(1,2)))
        assertEquals("g", b.letterAt(Cube(2,0)))
        assertEquals("h", b.letterAt(Cube(2,1)))
        assertEquals("qu", b.letterAt(Cube(2,2)))
    }

    @Test
    fun `isAdj finds adjacent cubes`() {
        val c = Cube(2, 2)

        assertFalse(c.isAdj(c))
        assertFalse(c.isAdj(Cube(2, 2)))

        assertTrue(c.isAdj(Cube(2, 1)))
        assertTrue(c.isAdj(Cube(1, 1)))
        assertTrue(c.isAdj(Cube(1, 2)))

        assertFalse(c.isAdj(Cube(0, 0)))
        assertFalse(c.isAdj(Cube(0, 1)))
        assertFalse(c.isAdj(Cube(0, 2)))
        assertFalse(c.isAdj(Cube(1, 0)))
        assertFalse(c.isAdj(Cube(2, 0)))
    }

    @Test
    fun `containsWord finds legal words`() {
        assertTrue(b.containsWord("abcfedghqu"))
        assertTrue(b.containsWord("adghebcfqu"))
        assertTrue(b.containsWord("abedghqufc"))
        assertTrue(b.containsWord("aequfc"))

        assertFalse(b.containsWord("abcdefghqu"), "cubes are not adjacent")
        assertFalse(b.containsWord("aequfce"), "cubes may not be reused")
        assertFalse(b.containsWord("aeq"), "whole cube must be used")
    }

    @Test
    fun `findValidWords finds all valid words from dict`() {
        val dict = listOf("abc", "def", "ghqu", "abcdefghqu", "abcfedghqu", "adghebcfqu", "abedghqufc", "aequfce")
        val solver = Solver(dict)
        val answer = solver.findValidWords(b)
        val expected = listOf("abc", "def", "ghqu", "abcfedghqu", "abedghqufc", "adghebcfqu")
        assertEquals(expected.size, answer.size)
        assertTrue(answer.containsAll(expected))
    }
}