package boggle

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import java.io.File

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
    }
}

private fun loadWords(): Collection<String> =
    File(ClassLoader.getSystemResource("words").file).readLines()