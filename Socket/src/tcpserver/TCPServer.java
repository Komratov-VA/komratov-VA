
package tcpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Komratov-VA
 */
public class TCPServer {
    public static final String HOST = "localhost";
    public static final int PORT = 10000;
  
    public TCPServer(){
        
    }
    
    public static void main(String[] args) {
        TCPServer tCPServer = new TCPServer();
        try(ServerSocket ss = new ServerSocket(PORT);
                Socket s = ss.accept()){
            tCPServer.run(s);
            
        }catch(IOException e){
            System.out.println("Error 1"+ e.getMessage());
        }
        
    }
    private void run(Socket s) throws IOException{
        InputStream in = s.getInputStream();
          OutputStream os = s.getOutputStream();
          byte[] buf = new byte[255];
          int n = in.read(buf);
          String file = new String(buf,0,n);
          Document d = readDocument(file);
          ObjectOutputStream oos = new ObjectOutputStream(os);
          if(d !=null){
              oos.writeObject(d);
          }else{
              oos.writeObject("Error: File not found");
          }    
    }
   private Document readDocument(String file){
       
        return new Document("title","Document body");
    }    

}
