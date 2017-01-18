/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.breaker;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;

import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BlendMode;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 *
 * @author Shay
 */
public final class MainController implements Initializable {

    Image image;
    @FXML
    public Label score;

    @FXML
    public Label gameOver;

    @FXML
    private Label label1;
    @FXML
    private Label label2;
    @FXML
    private Label label3;
    @FXML
    private Label label4;

    @FXML
    private ImageView pill1;
    @FXML
    private ImageView pill2;
    @FXML
    private ImageView pill3;
    @FXML
    private ImageView pill4;

    @FXML
    public ProgressBar progBar = new ProgressBar();
    private static final String RED_BAR    = "red-bar";
    
    private ImageView[] pill;
    private Label[] label;

    private final int[] userNumbers;
    private int[] gameNumbers;
    private boolean isPlaying;
    public boolean alertSnd = false;
    private int numCount;
    private int greenCount;

    double decrement = .146;
    double barValue = 100;

    public int myScore = 0;

    Game game;
    Timer timer;
    Timer alertTimer;

    @SuppressWarnings("empty-statement")

    private void setProgBar() {
        barValue = barValue - decrement;
        double temp = barValue / 100;
        //  System.out.println(temp);
        progBar.setProgress(temp);
    }

    private void gameOver() {
        timer.cancel();
        alertTimer.cancel();
        this.isPlaying = false;

        FadeTransition ft = new FadeTransition(Duration.millis(1000), gameOver);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setInterpolator(Interpolator.EASE_OUT);
        ft.setAutoReverse(true);        ft.setCycleCount(-1);
        ft.play();
        ft.setOnFinished((ActionEvent event) -> {

        });
    }

    private void soundAlert() {
        if (!alertSnd) {
            alertTimer = new Timer();
            alertTimer.schedule(new CallAlert(),
                    500, //initial delay
                    1 * 2500);  //subsequent rate
            alertSnd = true;
            System.out.println("AlertSnd");
        }
    }

    private class CallAlert extends TimerTask {

        @Override
        public void run() {
            game.alert.play();
            if(progBar.getOpacity()==1){
            progBar.setOpacity(0.5);
            }else{
            progBar.setOpacity(1);
            }
        }
    }

    private void startTimer() {
        barValue = 100;
        timer = new Timer();
        timer.schedule(new RemindTask(),
                500, //initial delay
                1 * 100);  //subsequent rate

    }

    private class RemindTask extends TimerTask {

        @Override
        public void run() {
            if (barValue - decrement > 0) {
                if (barValue < 30) {
                    soundAlert();
                    progBar.setBlendMode(BlendMode.DIFFERENCE);
                    progBar.getStyleClass().add(RED_BAR);
                };
                setProgBar();
                // tick sound
            } else {
                gameOver();
            }
        }
    }

    public MainController() {
        this.isPlaying = false;

        this.game = new Game();
        this.userNumbers = new int[4];
        this.gameNumbers = game.getRandomNumbers();
        this.numCount = 1;
        this.isPlaying = true;
        this.greenCount = 0;
        this.startTimer();

    }

    public void clearAll() {

        Image imgWhite = new Image("code/breaker/images/pill_white.png");
        progBar.setBlendMode(BlendMode.COLOR_DODGE);
        game.alert.stop();
        for (int i = 0; i < 4; i++) {
            pill[i].setImage(imgWhite);
            label[i].setText("");
        }
        this.isPlaying = true;
        greenCount = 0;

    }

    private void clearBoard() {

        Timeline timeline;
        timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> clearAll()));
        game.nextLevel.play();
        timeline.play();

    }

    private void resetLevelValues() {
        game.chirp.play();
        ScaleTransition st = new ScaleTransition(Duration.millis(20), score);
        st.setByX(.5);
        st.setByY(.5);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
        st.setOnFinished((ActionEvent event) -> {
            double width = progBar.getProgress();
            width = (width - 0.01);
            if (width < 0) {
                width = 0;
                progBar.setProgress(width);
                st.stop();
                game.chirp.stop();
                //next Level
                nextLevel();
            } else {
                progBar.setProgress(width);
                myScore++;
                String temp = "" + myScore;
                score.setText(temp);
                st.play();

            }
        });
    }

    private void clearLevel() {
        clearAll();
        timer.cancel();
        resetLevelValues();
    }

    private void nextLevel() {
        gameNumbers = game.getRandomNumbers();
        progBar.setProgress(1);
        startTimer();
    }

    private int step = 0;

    private void doPillFadeUp() {

        FadeTransition ft;
        ft = new FadeTransition(Duration.millis(500), pill[step]);

        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        game.check.play();
        ft.setOnFinished((ActionEvent event) -> {
            if (step < 3) {
                step++;
                doPillFadeUp();
            } else {
                if (greenCount != 4) {
                    clearBoard();
                } else {
                    game.level.play();
                    clearLevel();
                }
                step = 0;
            }
        });
    }

    @SuppressWarnings("empty-statement")
    private void checkNumbers() {
        this.isPlaying = false;

        for (int i = 0; i < 4; i++) {
            pill[i].setOpacity(0);
            if (userNumbers[i] == gameNumbers[i]) {
                Image img = new Image("code/breaker/images/pill_green.png");
                pill[i].setImage(img);
                greenCount++;
            } else if (userNumbers[i] != gameNumbers[i]) {
                Image img = new Image("code/breaker/images/pill_red.png");
                pill[i].setImage(img);

                for (int j = 0; j < 4; j++) {
                    if (userNumbers[i] == gameNumbers[j]) {
                        Image imgYellow = new Image("code/breaker/images/pill_yellow.png");
                        pill[i].setImage(imgYellow);

                    }
                }

            }
        }
        if(greenCount>0){
        game.correct.play();
    };
        doPillFadeUp();
    }

    private void displayKeyEntry(int num) {
        String numToString = String.valueOf(num);

        switch (numCount) {
            case 1: {
                label1.setText(numToString);
                userNumbers[0] = num;
                numCount++;
                break;
            }
            case 2: {
                label2.setText(numToString);
                userNumbers[1] = num;
                numCount++;
                break;
            }
            case 3: {
                label3.setText(numToString);
                userNumbers[2] = num;
                numCount++;
                break;
            }
            case 4: {
                label4.setText(numToString);
                userNumbers[3] = num;
                numCount = 1;
                checkNumbers();
                break;
            }
            default: {
            }
            break;
        }

    }

    @FXML
    private void handleButtonNumbers(MouseEvent event) {
        if (isPlaying) {
            ImageView tempRef = (ImageView) event.getSource();
            String newImage = "code/breaker/images/" + tempRef.getId() + ".png";
            image = new Image(newImage);
            tempRef.setImage(image);

            int id = Integer.parseInt(tempRef.getId());
            //   System.out.println(id);
            displayKeyEntry(id);
            game.press.play();
        }

    }

    @FXML
    private void handleButtonNumbersPressed(MouseEvent event) {
        if (isPlaying) {
            String newImage = "code/breaker/images/hex-over.png";
            Image imageOver;
            imageOver = new Image(newImage);

            ImageView tempRef;
            tempRef = (ImageView) event.getSource();
            tempRef.setImage(imageOver);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Font.loadFont(getClass().getResourceAsStream("/resources/space age.ttf"), 14);

        pill = new ImageView[4];
        label = new Label[4];

        pill[0] = pill1;
        pill[1] = pill2;
        pill[2] = pill3;
        pill[3] = pill4;

        label[0] = label1;
        label[1] = label2;
        label[2] = label3;
        label[3] = label4;
        clearAll();
    }

}
