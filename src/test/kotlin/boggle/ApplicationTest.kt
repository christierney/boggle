package boggle

import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.withCharset
import io.ktor.server.testing.contentType
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    private val solver = mockk<Solver>()

    @Test
    fun `ping returns pong`() = withTestApplication({ mainModule(solver) }) {
        with(handleRequest(HttpMethod.Get, "/ping")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("pong", response.content)
        }
    }

    @Test
    fun `board returns solutions`() = withTestApplication({ mainModule(solver) }) {
        every { solver.findValidWords(Board.of("abcdefghi")) } returns listOf("a", "b", "c")
        with(handleRequest(HttpMethod.Get, "/board?letters=abcdefghi")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
            assertEquals("""["a","b","c"]""", response.content)
        }
    }
}