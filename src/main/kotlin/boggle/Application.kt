package boggle

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import kotlinx.html.*
import java.util.*

fun Application.main() {
    val words = loadWords()
    val solver = Solver(words)
    mainModule(solver)
}

fun Application.mainModule(solver: Solver) {

    install(ContentNegotiation) {
        gson {

        }
    }

    install(StatusPages) {
        exception<Throwable> { t ->
            log.error(t.message, t)
            call.respond(HttpStatusCode.BadRequest)
        }
    }

    routing {
        get("/ping") {
            call.respondText("pong")
        }

        get("/board") {
            val board = call.request.queryParameters["letters"]?.let { s ->
                Board.of(s)
            } ?: Board(emptyList())

            call.respond(solver.findValidWords(board))
        }

        get("/") {
            val letters = call.request.queryParameters["letters"]
            val board = letters?.let { s ->
                Board.of(s)
            } ?: Board(emptyList())

            val answers = solver.findValidWords(board)

            call.respondHtml {
                head {
                    title { +"Boggle Solver" }
                }

                body {
                    h1 { +"Boggle Solver" }

                    p {
                        +"""
                            Enter all the letters on the board, starting at the top left and moving left-to-right, then
                            top-to-bottom. Enter "q" for the "qu" cube. For example, enter "abcdqfghi" for this board:
                        """.trimIndent()

                        pre {
                            +"""
                                |  a  |  b  |  c  |
                                |  d  |  qu |  f  |
                                |  g  |  h  |  i  |
                            """.trimIndent()
                        }
                    }

                    form(method = FormMethod.get) {
                        input(type = InputType.text) {
                            name = "letters"
                            value = letters ?: ""
                        }

                        input(type = InputType.submit)
                    }

                    h2 { +"Found ${answers.size} words: " }
                    ul {
                        id = "answers"
                        answers.forEach {word ->
                            li { +word }
                        }
                    }
                }
            }
        }
    }
}

private fun loadWords(): Collection<String> {
    // see FileReadWrite#File.readLines
    val result = ArrayList<String>()
    ClassLoader.getSystemResourceAsStream("words").bufferedReader().forEachLine { result.add(it) }
    return result
}
