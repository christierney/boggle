# Boggle Solver

A web application that finds solutions for the game [Boggle](https://en.wikipedia.org/wiki/Boggle).

## Running Locally

Run with `./gradlew run`

Open [http://localhost:8080/ping]() and you should get a "pong".

Alternative port: `PORT=XXXX ./gradlew run`

[http://localhost:8080/board]() is the solver interface; it will return a list of valid words.

Specify a board with a query parameter, `letters`:
[http://localhost:8080/board?letters=abcdefghi](). The board is read left-to-right, top-to-bottom.
For example, the string `abcdefghi` corresponds to the board:

    | a | b | c |
    | d | e | f |
    | g | h | i |

The application currently does not validate input and simply assumes that `letters` is a valid
(square) board. Since Boggle uses a "qu" instead of "q", a value of "q" in `letters` will be
converted to "qu" before solving.

Output is a JSON list of valid words.

## Deployment

`./gradlew stage` will create an uber-jar in `build/libs`. This can be run with:

    java -jar build/libs/boggle.jar

This is suitable for deploying to Heroku. The application has been deployed at
[https://shrouded-island-28213.herokuapp.com/]()

(This is a free Heroku application, so it may be asleep and a first request might take longer than
usual to return.)

## Design

A Board is represented as a List of Lists of Strings. To check if a word is on the board, the
Solver looks for any cube containing the first letter of the word. (Actually, it checks for a cube
whose string matches the start of the word, since a "letter" might actually be "qu".) If it finds
a matching cube, it searches the adjacent, unused cubes for the next letter (or letters) and
repeats until the word is completed or the cubes are exhausted. A cube is considered adjacent if it
is one space away horizontally, vertically, or diagonally.

The Solver is constructed with a word list and a minimum word length. It checks each word one at a
time (filtering out any that are too short) and checks if the word can be found on the board.