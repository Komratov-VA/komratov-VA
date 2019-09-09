/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import jdk.management.resource.internal.inst.SocketRMHooks;
import message.Message;

/**
 *
 * @author JAVA
 */
public class UDPServer implements AutoCloseable{
    public static final int DEFAULT_PORT = 12345;
    public static final String DEFAULT_HOST = "localhost";
    private DatagramSocket socket;
    private DatagramPacket packet;
    private byte [] buffer;
    
    public UDPServer(int port) throws SocketException{
        socket = new DatagramSocket(port);
        buffer = new  byte[2048];
        packet = new DatagramPacket(buffer,buffer.length);
    }
    public UDPServer() throws SocketException{
        this(DEFAULT_PORT );
    }
    
    public static void main(String[] args) {
        try(UDPServer udp = new UDPServer()) {
            String msg;
            while (true) {    
            udp.socket.receive(udp.packet);
            msg =  udp.printPacket();
            udp.sendReply();
           if(msg.equalsIgnoreCase("exit")){
               break;
           }
            }
           } catch (SocketException e) {
            
         
        } catch (IOException e) {
            
        }
    }

    @Override
    public void close() throws IOException {
      if(!socket.isClosed()){
          socket.close();
      }
    }

    private String printPacket() {
        String str = new String(packet.getData(),0,packet.getLength());
        System.out.println(packet.getAddress()+":"+packet.getPort()+" >> "+ str);
        return str;
             
    }

    private void sendReply() throws IOException {
        
       packet.setData("Message received".getBytes());
       socket.send(packet);
        packet.setData(buffer);
        
    }
     private Message printPacket(boolean b){
         Message m =null;
         try (ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
                 ObjectInputStream ois = new ObjectInputStream(bais);
                 ){
             m =(Message)ois.readObject();
         } catch (IOException | ClassNotFoundException e) 
         {
              System.out.println("Error 12"+e.getMessage());
         }
            return m;
    }
    
}
