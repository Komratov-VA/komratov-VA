
package multi;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Komratov-VA
 */
public  class Ball {
    public synchronized void hit(String player, int delay){
        System.out.println(player+"->");
        try {
            Thread.sleep(delay);
            notify();
            wait();
        } catch (InterruptedException ex) {
            System.out.println("Thread"+Thread.currentThread().getName()+"Interrupted");
        }
        
    }
    
    
}
