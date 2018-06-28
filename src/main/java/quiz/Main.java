package quiz;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Resource.getUri("quiz.fxml").toURL());
        Parent quizRoot = loader.load();
        Scene scene = new Scene(quizRoot, 1200, 750);

        QuizController quizController = loader.<QuizController>getController();
        if (getParameters().getUnnamed().size() > 0) {
            int firstQuestion = Integer.valueOf(getParameters().getUnnamed().get(0));
            quizController.setCurrentQuestionIndex(firstQuestion);
        }
        quizController.setStage(primaryStage);
        quizController.showQuestion();

        primaryStage.setTitle("A nagy kvíz");
        primaryStage.setScene(scene);
        primaryStage.show();
        quizController.focus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
