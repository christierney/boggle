package boggle

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import java.io.File
import java.util.ArrayList

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
    }
}

private fun loadWords(): Collection<String> {
    // see FileReadWrite#File.readLines
    val result = ArrayList<String>()
    ClassLoader.getSystemResourceAsStream("words").bufferedReader().forEachLine { result.add(it) }
    return result
}
