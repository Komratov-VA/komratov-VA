
package tcpclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import tcpserver.Document;
import tcpserver.TCPServer;
/**
 *
 * @author Komratov-VA
 */
public class TCPClient {
    
    public TCPClient(){
        
    }
    
    public static void main(String[] args) {
        TCPClient tCPClient = new TCPClient();
        try (Socket socket = new Socket(TCPServer.HOST,TCPServer.PORT)){
            tCPClient.run(socket);
            
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error 1"+e.getMessage());
        }
    }
    
    private void run(Socket socket) throws IOException, ClassNotFoundException{
        OutputStream os = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        os.write(getFileName().getBytes());
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        Object d =(Document) objectInputStream.readObject();
        if (d instanceof Document)
            
       ((Document) d).print();
        else 
            System.out.println(d);
    }
    private String getFileName(){
        return "first.txt";
    }
}
