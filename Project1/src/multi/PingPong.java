
package multi;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Komratov-VA
 */
public class PingPong implements Runnable{
    private String player;
    private int delay;
    private Ball ball;
    public PingPong(String player,int delay,Ball ball){
        this.player = player;
        this.delay = delay;
        this.ball = ball;
    }
    public static void main(String[] args) {
        Ball ball = new Ball();
        Thread first = new Thread(new PingPong("Ksenia",300,ball));
        Thread second = new Thread(new PingPong("Seva",400,ball));
        System.out.println("Start game...");
        first.start();
        second.start();
        
        try {
            first.join(5000);
         //   System.out.println("sddd");
            second.join(5000);
            synchronized(ball){
                ball.notify();
            }
        } catch (InterruptedException ex) {
            System.out.println("Thread"+Thread.currentThread().getName()+"Interrupted");
        }
         System.out.println("Game over");
         
    }

    @Override
    public void run() {
        for(int i=0;i<10;++i){
            ball.hit(player, delay);
        }
    }
}
