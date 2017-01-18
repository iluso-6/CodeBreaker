/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code.breaker;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.Parent;




public class Ticker {
  
    Parent root;
    public double bar = 1;
      
    Timer timer;
      
    public Ticker() throws IOException{

        timer = new Timer();
        timer.schedule(new RemindTask(),
                0, //initial delay
                1 * 1000);  //subsequent rate
     
    }

    public void start() {
        System.out.println("Beep!: "+bar);        
        bar = bar - 0.1;

    }

    public void stop() {
        System.out.println("Stop!");
        timer.cancel();
    }


    class RemindTask extends TimerTask {

        @Override
        public void run() {
       
            start();
        }
    }


}
