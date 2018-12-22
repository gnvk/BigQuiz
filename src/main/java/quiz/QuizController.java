package quiz;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class QuizController {

    public Pane rootPane;
    public GridPane mainPane;

    public Label questionLabel;

    public Label answer0Label;
    public Label answer1Label;
    public Label answer2Label;
    public Label answer3Label;
    public Label answer4Label;
    public Label answer5Label;
    public Label answer6Label;
    public Label answer7Label;
    public Label answer8Label;
    public Label answer9Label;
    public Label answer10Label;
    public Label answer11Label;
    public Label answer12Label;
    public Label answer13Label;
    public Label answer14Label;
    public Label answer15Label;

    public VBox answer0Box;
    public VBox answer1Box;
    public VBox answer2Box;
    public VBox answer3Box;
    public VBox answer4Box;
    public VBox answer5Box;
    public VBox answer6Box;
    public VBox answer7Box;
    public VBox answer8Box;
    public VBox answer9Box;
    public VBox answer10Box;
    public VBox answer11Box;
    public VBox answer12Box;
    public VBox answer13Box;
    public VBox answer14Box;
    public VBox answer15Box;

    private Label[] answerLabels;
    private VBox[] answerBoxes;

    private List<Transition> transitions = new ArrayList<>();

    private Stage stage;

    private List<Question> questions = new ArrayList<>();

    private Question currentQuestion;
    private int currentQuestionIndex = 0;
    private Integer selectedAnswer = null;
    private List<String> selectedAnswers = new ArrayList<>();

    private Timer timeLeftTimer;

    enum GameState {
        QUESTION_SHOWN,
        ANSWERS_SHOWN,
        ROUND_OVER
    };
    private GameState gameState = GameState.QUESTION_SHOWN;

    public QuizController() {
        InputStream questionsStream = null;
        try {
            questionsStream = new FileInputStream("./questions.tsv");
        } catch (FileNotFoundException e) {
            questionsStream = getClass().getResourceAsStream("/questions.tsv");
        }
        if (questionsStream == null) {
            new Alert(Alert.AlertType.ERROR, "Couldn't read the questions.tsv file").showAndWait();
            System.exit(1);
        }
        List<List<String>> questionRows = TsvReader.readTsv(questionsStream);
        for (int i = 1; i < questionRows.size(); i++) {
            List<String> columns = questionRows.get(i);
            if (Question.isValid(columns)) {
                questions.add(Question.fromColumns(columns));
            }
        }
    }

    @FXML
    public void initialize() {
         answerBoxes = new VBox[] {
             answer0Box, answer1Box, answer2Box, answer3Box,
             answer4Box, answer5Box, answer6Box, answer7Box,
             answer8Box, answer9Box, answer10Box, answer11Box,
             answer12Box, answer13Box, answer14Box, answer15Box
         };
         answerLabels = new Label[] {
            answer0Label, answer1Label, answer2Label, answer3Label,
                    answer4Label, answer5Label, answer6Label, answer7Label,
                    answer8Label, answer9Label, answer10Label, answer11Label,
                    answer12Label, answer13Label, answer14Label,  answer15Label
         };
    }

    void focus() {
        mainPane.requestFocus();
    }

    void showQuestion() {
        for (VBox answerBox : answerBoxes) {
            ObservableList<String> styles = answerBox.getStyleClass();
            styles.clear();
            styles.add("defaultAnswerBox");
            answerBox.setVisible(false);
        }
        currentQuestion = questions.get(currentQuestionIndex);
        questionLabel.setText(currentQuestion.getQuestion());
        gameState = GameState.QUESTION_SHOWN;
    }

    private void showAnswers() {
        for (int i = 0; i < 16; i++) {
            if (currentQuestion.getAnswers().size() > i) {
                answerLabels[i].setText(currentQuestion.getAnswer(i));
                answerBoxes[i].setVisible(true);
                FadeTransition fadeTransition = new FadeTransition(Duration.millis(2000), answerBoxes[i]);
                fadeTransition.setFromValue(0.0);
                fadeTransition.setToValue(1.0);
                SequentialTransition seqTransition = new SequentialTransition(
                        new PauseTransition(Duration.millis(i * 150)),
                        fadeTransition
                );
                transitions.add(seqTransition);
                seqTransition.setOnFinished(event -> transitions.remove(seqTransition));
                seqTransition.play();
            }
        }
        selectedAnswer = null;
        selectedAnswers = new ArrayList<>();
        gameState = GameState.ANSWERS_SHOWN;
    }

    public void answerClicked(MouseEvent mouseEvent) {
        if (gameState != GameState.ANSWERS_SHOWN) {
            return;
        }

        VBox sourceBox = (VBox) mouseEvent.getSource();
        int answer = Integer.valueOf((String) sourceBox.getUserData());
        if (selectedAnswers.contains(currentQuestion.getAnswer(answer))) {
            return;
        }
        if (selectedAnswer == null) {
            // New selected answer
            selectedAnswer = answer;
            selectAnswer(answer);
        } else if (selectedAnswer != answer) {
            // Reset selection
            deselectAnswer(selectedAnswer);
            selectedAnswer = null;
        } else {
            // Final answer
            selectedAnswers.add(currentQuestion.getAnswer(answer));
            if (currentQuestion.isCorrect(answer)) {
                correctAnswer(answer);
            } else {
                wrongAnswer(answer);
            }
            selectedAnswer = null;

            if (currentQuestion.areAllWrongAnswersFound(selectedAnswers)) {
                rootPane.getStyleClass().add("allWrong");
                gameState = GameState.ROUND_OVER;
            } else if (currentQuestion.oneCorrectAnswerRemaining(selectedAnswers)) {
                rootPane.getStyleClass().add("oneRemains");
            } else if (currentQuestion.areAllCorrectAnswersFound(selectedAnswers)) {
                rootPane.getStyleClass().add("allCorrect");
                gameState = GameState.ROUND_OVER;
            }
        }
    }

    void setCurrentQuestionIndex(int index) {
        currentQuestionIndex = index;
        currentQuestion = questions.get(currentQuestionIndex);
    }

    public void questionClicked(MouseEvent mouseEvent) {
        if (gameState == GameState.QUESTION_SHOWN) {
            showAnswers();
            return;
        }

        if (gameState == GameState.ANSWERS_SHOWN) {
            return;
        }

        nextQuestion();
    }

    public void keyPressed(KeyEvent keyEvent) throws URISyntaxException, IOException {
        switch (keyEvent.getCode()) {
            case F10:
                stage.setFullScreen(true);
                break;
            case RIGHT:
                nextQuestion();
                break;
            case LEFT:
                previousQuestion();
                break;
            case S:
                if (gameState == GameState.ANSWERS_SHOWN &&
                        !currentQuestion.oneCorrectAnswerRemaining(selectedAnswers)) {
                    hintThreeAnswers();
                }
                break;
            case T:
                if (gameState == GameState.ANSWERS_SHOWN && transitions.isEmpty() && timeLeftTimer == null) {
                    TimeLeftCounter timeLeftCounter = new TimeLeftCounter(10, mainPane);
                    timeLeftTimer = new Timer();
                    timeLeftTimer.schedule(timeLeftCounter, 0, 1000);
                }
                break;
            case R:
                resetTimer();
                break;
        }
    }

    private void removeStyles() {
        HashMap<String, Pane> panes = new HashMap<>();
        panes.put("rootPane", rootPane);
        panes.put("mainPane", mainPane);
        for (Map.Entry<String, Pane> pane : panes.entrySet()) {
            ObservableList<String> styles = pane.getValue().getStyleClass();
            styles.clear();
            styles.addAll(pane.getKey());
        }
        for (Transition transition : transitions) {
            transition.stop();
        }
    }

    private void resetTimer() {
        mainPane.setStyle("-fx-background-color: transparent");
        if (timeLeftTimer != null) {
            timeLeftTimer.cancel();
            timeLeftTimer = null;
        }
    }

    private void previousQuestion() {
        removeStyles();
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            selectedAnswer = null;
            selectedAnswers = new ArrayList<>();
            showQuestion();
        }
    }

    private void nextQuestion() {
        removeStyles();
        if (currentQuestionIndex + 1 < questions.size()) {
            currentQuestionIndex++;
            selectedAnswer = null;
            selectedAnswers = new ArrayList<>();
            showQuestion();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Elfogytak a kérdések").showAndWait();
            System.exit(0);
        }
    }

    private  void selectAnswer(int answer) {
        ObservableList<String> styles = answerBoxes[answer].getStyleClass();
        if (!styles.contains("selectedAnswerBox")) {
            styles.add("selectedAnswerBox");
        }
        resetTimer();
    }

    private void deselectAnswer(int answer) {
        ObservableList<String> styles = answerBoxes[answer].getStyleClass();
        if (styles.contains("selectedAnswerBox")) {
            styles.remove("selectedAnswerBox");
        }
    }

    private void correctAnswer(int answer) {
        ObservableList<String> styles = answerBoxes[answer].getStyleClass();
        if (styles.contains("selectedAnswerBox")) {
            styles.remove("selectedAnswerBox");
        }
        if (!styles.contains("correctAnswerBox")) {
            styles.add("correctAnswerBox");
        }
    }

    private void wrongAnswer(int answer) {
        ObservableList<String> styles = answerBoxes[answer].getStyleClass();
        if (styles.contains("selectedAnswerBox")) {
            styles.remove("selectedAnswerBox");
        }
        if (!styles.contains("wrongAnswerBox")) {
            styles.add("wrongAnswerBox");
        }
    }

    private void hintThreeAnswers() {
        List<String> hints = currentQuestion.hint(selectedAnswers);
        for (int i = 0; i < currentQuestion.getAnswers().size(); i++) {
            if (hints.contains(currentQuestion.getAnswer(i))) {
                answerBoxes[i].getStyleClass().add("hintedAnswerBox");
            }
        }
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }
}
