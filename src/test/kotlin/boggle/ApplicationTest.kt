package boggle

import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun `ping returns pong`() = withTestApplication({ main() }) {
        with(handleRequest(HttpMethod.Get, "/ping")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("pong", response.content)
        }
    }
}