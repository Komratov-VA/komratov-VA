package tcpserver;

import java.io.Serializable;

/**
 *
 * @author Komratov-VA
 */
public class Document implements Serializable{
    private String head;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
    private String body;
    
     public Document(){
       
    }
    
    public Document(String head,String body){
        this.body = body;
        this.head = head;
    }
    
    public void print(){
        System.out.println("Head: "+ head);
        System.out.println(body);
    }
  
}
