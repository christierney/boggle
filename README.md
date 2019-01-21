# Boggle Solver

A web application that finds solutions for the game [Boggle](https://en.wikipedia.org/wiki/Boggle).

## Running Locally

Run with `./gradlew run`

Open http://localhost:8080/ping and you should get a "pong".

Alternative port: `PORT=XXXX ./gradlew run`

http://localhost:8080/board is the solver interface; it will return a list of valid words.

Specify a board with a query parameter, `letters`:
http://localhost:8080/board?letters=abcdefghi. The board is read left-to-right, top-to-bottom.
For example, the string `abcdefghi` corresponds to the board:

    | a | b | c |
    | d | e | f |
    | g | h | i |

The application currently does not validate input and simply assumes that `letters` is a valid
(square) board. Since Boggle uses a "qu" instead of "q", a value of "q" in `letters` will be
converted to "qu" before solving.

Output is a JSON list of valid words.

A simple HTML UI is available at http://localhost:8080/

## Deployment

`./gradlew stage` will create an uber-jar in `build/libs`. This can be run with:

    java -jar build/libs/boggle.jar

This is suitable for deploying to Heroku. The application has been deployed at:

* JSON interface: https://shrouded-island-28213.herokuapp.com/board?letters=abcdefghi
* HTML interface: https://shrouded-island-28213.herokuapp.com/

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

The built-in word list is the standard FreeBSD dictionary (`/usr/share/dict/words`).

## Ideas For Improvement

* Non-square boards: the Board class could take an optional `rows` or `cols` parameter to define a
  non-square board.
* Input validation: the application could validate that input is all letters, that it is the
  correct number of letters for the board size, etc.
* Extensible input: instead of a String the application could take an object describing the board:
  letters, dimensions, possibly non-standard cube values (e.g. each cube might contain multiple
  letters.)
* Extensible output: instead of just returning a list of Strings, return additional metadata about
  each word like word score, coordinates of each letter, etc.
* UI improvements:
    * Allow a user to input letters into a visual representation of the board; this would reduce
    confusion about what order the letters should be entered in.
    * Allow a user to upload a picture of a board and detect the characters.
* Performance:
    * Word search could be parallelized to speed up performance.
    * Alternative algorithms: instead of starting from a word list, maybe generate every possible
      valid combination of letters for the board and check them against an index of valid words.
      My intuition is that this would be slower, but maybe not.