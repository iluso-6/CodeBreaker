package code.breaker;

import java.util.Random;
import javafx.scene.media.AudioClip;

public class Game {

    
    private final int[] gameNumbers;
    private final int[] randNumbers;

    
    final AudioClip level = new AudioClip(getClass().getResource("/code/breaker/resources/level.mp3").toString());
    final AudioClip nextLevel = new AudioClip(getClass().getResource("/code/breaker/resources/level_end.mp3").toString());
    final AudioClip press = new AudioClip(getClass().getResource("/code/breaker/resources/press1.mp3").toString());
    final AudioClip correct = new AudioClip(getClass().getResource("/code/breaker/resources/correct.mp3").toString());
    final AudioClip check = new AudioClip(getClass().getResource("/code/breaker/resources/check.mp3").toString()); 
    final AudioClip chirp = new AudioClip(getClass().getResource("/code/breaker/resources/test.mp3").toString()); 
    final AudioClip alert = new AudioClip(getClass().getResource("/code/breaker/resources/alert1.mp3").toString()); 
    final AudioClip correct1 = new AudioClip(getClass().getResource("/code/breaker/resources/1.mp3").toString()); 
    final AudioClip correct2 = new AudioClip(getClass().getResource("/code/breaker/resources/2.mp3").toString()); 
    final AudioClip correct3 = new AudioClip(getClass().getResource("/code/breaker/resources/3.mp3").toString()); 
    Game() {
        this.randNumbers = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        this.gameNumbers = new int[4];
    }

    public int[] getRandomNumbers() {

        Random rnd = new Random();
        for (int i = randNumbers.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = randNumbers[index];
            randNumbers[index] = randNumbers[i];
            randNumbers[i] = a;
        }

        for (int i = 0; i < 4; i++) {
            gameNumbers[i] = randNumbers[i];
            System.out.println(gameNumbers[i]);
        }
        
        return gameNumbers;
    }

}
