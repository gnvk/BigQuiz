# Big Quiz

A quiz game for big(ger) groups.

## Rules

* The players sit in a circle
* Each player is given a number of life tokens
  * The optimal number of life tokens depends on the number of players and how long
    you want to play
* A starting player is selected randomly and is given an active player token
* A new question is displayed with the possible answers. The active player picks an answer.
  If it's incorrect (red), they lose a life token. Regardless of the correctness of the answer,
  the active player token is passed to the next player.
* When there's only one correct answer remaining on the board, the background turns blue
* When all the correct asnwers are picked, the background turns green and a new question comes up
* When all the incorrect answers are picked, the background turns red and a new question comes up
* A player who lost their last life token is eliminated
* The last player alive wins.

### Optional helps

We suggest the following help tokens introduced after 3 questions:

* [Introduced after question no. 3] Pass
  * Each player is given 2 pass tokens
  * They can be used anytime when the player is active
  * When used, the active player doesn't have to give an answer, the active player
    token is simply passed to the next player
* [Introduced after question no. 6] The help of the computer
  * Each player is given 2 tokens
  * They can be used when
    * The player is active
    * There are at least two correct answers remaining on the board (the background is not blue)
  * When used, the computer randomly highlights 3 answers on the board.
    Two of which are correct, and one incorrect.
* [Introduced when the first player is eliminated] The help of the eliminated players
  * Each remaining player is given 2 tokens
  * They can be used anytime when the player is active
  * When used, the active player can ask the eliminated players to suggest an answer

### Optional time constraints

Optionally, the quizmaster can decide to put a time limit for the answer.
This is indicated by an overflowing red color and a ticking sound.
After this, the active player has 10 seconds to answer the question.
If they fail to do so, they lose a life token and the active player token
is passed to the next player.

## Quizmaster quide

### Questions

To play the game first construct the questions. An example can be seen in `resources/questions.tsv`.
The format must be the following:

* Tab separated file with 17 columns
* The first line is header and ignored
* Each remaining line corresponds to a single question
  * Column 1: the question
  * Column 2-9: the correct answers
  * Column 10-17: the incorrect answers
  * You may leave columns empty, if you wish to provide less than 8 correct/incorrect answers
    but still, the incorrect answers must start at column 10 and you need all the 17 columns

### Run the program

To run the program download (or build) the jar file and simply run it by double
clicking it or typing

```shell
java -jar BigQuiz-0.1.jar
```

To use your questions, have them in the same directory with the JAR file with the
name `questions.tsv`.

### During the game

When you first start the game, it will show your first question (or the first question
of the defult - at the moment Hungarian - test suite). To navigate through the game you can

* press F10 to go fullscreen
* click on the question to display the answers after you read it out loud
* to select an answer picked by the active player, click on it
* to reveal if it was correct, click on it again
* to unselect the selected answer, click on any other answer
* if the background is green or red, click on the question to go to the next question
* to use the computer help, press S
* to start the timer, press T
* to reset the timer, press R

### Build from source

To build from source you need a java JDK and JavaFX. Then simply run

```shell
mvn package
```

This will generate the JAR file to the `target` directory.
