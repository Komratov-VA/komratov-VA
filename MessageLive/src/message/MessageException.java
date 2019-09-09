
package message;

/**
 *
 * @author Komratov-VA
 */
public class MessageException extends Exception{
    private String to;
    private String from;
    public MessageException(String to,String from,String msg){
        super(msg);
        this.from= from;
        this.to = to;
    }
   @Override
   public String getMessage(){
       return "To:"+to+"\nFrom: "+from+"\nError:"+super.getMessage();
   }
}
