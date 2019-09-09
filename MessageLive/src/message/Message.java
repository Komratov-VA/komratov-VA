
package message;

import java.io.Serializable;

/**
 *
 * @author Komratov-VA
 */
public class Message implements Serializable{
    private String to;
    private String from;
    private String message;

    
    public Message(){}
    public Message(String to,String from,String message){
      this.from = from;
      this.to = to;
      this.message = message;
      }
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
      
  
}
