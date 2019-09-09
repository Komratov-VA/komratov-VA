
package server;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javafx.beans.binding.StringBinding;
import javax.imageio.IIOException;
import message.Message;

/**
 *
 * @author Komratov-VA
 */
public class UDPClient implements AutoCloseable
{
     private DatagramSocket socket;
     private DatagramPacket packet;
     private byte[] buffer;
     private int serverPort;
     private InetAddress serverAddress;
         
     
     public UDPClient() throws UnknownHostException,IOException{
         this(UDPServer.DEFAULT_HOST, UDPServer.DEFAULT_PORT);
     }
     public UDPClient(String host, int serverPort) throws UnknownHostException,IOException{
         if(host !=null){
         serverAddress = InetAddress.getByName(host);
         this.serverPort =serverPort;
         socket = new DatagramSocket();
         buffer = new byte[2048];
         packet = new DatagramPacket(buffer, buffer.length);
         }else{
             throw new IOException("Invalid host name");
         }
     }
     
     public static void main(String[] args) {
         try (UDPClient client = new UDPClient("192.168.18.144",UDPServer.DEFAULT_PORT)){
             String str;
                   while (true) {
                 str = client.readMessage();
                 client.sendMessage(str);
                 client.getReply();
                 if(str.equalsIgnoreCase("exit\\q")){
                     break;
                 }
             }
             
             
         } catch (UnknownHostException e) {
             System.out.println("Error 1:"+e.getMessage());
         }
         catch (IOException e) {
             System.out.println("Error 2:"+e.getMessage());
         }
    }

    @Override
    public void close() throws IOException {
         if(!socket.isClosed()){
          socket.close();
      }
    }

    private String readMessage() throws IOException{
        byte[] b= new byte[255];
        int n;
        StringBuilder stringBinding = new StringBuilder() ;
        while (true) {            
            n = System.in.read(b);
            stringBinding.append(new String(b,0,n));
            if(stringBinding.toString().trim().endsWith("\\q")){
                break;
            }
            
        }
        return  stringBinding.toString().trim();
    }

    private void sendMessage(String msg) throws IOException {
        packet.setData(msg.getBytes());
        packet.setLength(msg.length());
        packet.setAddress(serverAddress);
        packet.setPort(serverPort);
        socket.send(packet);
        packet.setData(buffer);
    }
    private void sendMessage(Message msg) throws IOException {
        byte[] b = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos =new ObjectOutputStream(baos)){
            if(msg !=null){
                oos.writeObject(msg);
                    b = baos.toByteArray();
            }else{
                throw  new IOException("Null message");
            }
            
            
        } catch (IOException e) {
        }
        packet.setData(b);
        packet.setLength(b.length);
        packet.setAddress(serverAddress);
        packet.setPort(serverPort);
        socket.send(packet);
        packet.setData(buffer);
    }

    private void getReply() throws IOException {
       socket.receive(packet);
        System.out.println(packet.getAddress()+":"+ packet.getPort()+">>"+new String(packet.getData(),0,packet.getLength()));
    }
    
}
