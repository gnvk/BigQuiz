package quiz;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.TimerTask;

public class TimeLeftCounter extends TimerTask {

    private int timeSpent;
    private int timeout;
    private Pane pane;
    private MediaPlayer tickSound;
    private MediaPlayer bombSound;

    private MediaPlayer getMediaPlayer(String sound) {
        URL resource = getClass().getResource("/sounds/" + sound);
        try {
            if (resource != null) {
                String tickSoundString = resource.toURI().toString();
                Media tickSound = new Media(tickSoundString);
                return new MediaPlayer(tickSound);
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    TimeLeftCounter(int timeout, Pane pane) {
        this.timeSpent = 0;
        this.timeout = timeout;
        this.pane = pane;
        this.tickSound = getMediaPlayer("tick.mp3");
        this.bombSound = getMediaPlayer("bomb.mp3");
    }

    public void run() {
        if (timeSpent >= timeout) {
            Platform.runLater(() -> pane.setStyle("-fx-background-color: rgb(178, 34, 34, 0.9)"));
            this.cancel();
            if (bombSound != null) {
                bombSound.play();
            }
            return;
        } else {
            timeSpent++;
        }

        if (tickSound != null) {
            tickSound.seek(Duration.ZERO);
            tickSound.play();
        }

        int timeSpentPercentage = 100 - (int)((float)timeSpent / timeout * 100);
        String backgroundColor = "-fx-background-color: " +
            "linear-gradient(from 50% " + timeSpentPercentage + "% to 50% 100%, " +
                            "rgb(6, 22, 35, 0.0), " +
                            "rgb(178, 34, 34, 0.9))";
        Platform.runLater(() -> pane.setStyle(backgroundColor));
    }
}
