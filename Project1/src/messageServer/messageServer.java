package messageServer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.MessageException;
import serverDB.ClientSocket;
import serverDB.ServerDb;
import static serverDB.ServerDb.DEFAULT_HOST;
import static serverDB.ServerDb.DEFAULT_PORT;

/**
 *
 * @author Komratov-VA
 */
public class messageServer implements AutoCloseable {

    public static final String HOST = "localhost";
    public static final int PORT = 10000;
    public static final int PORT1 = 10001;
    private final static String DEFAULT_CONFIC = "default.properties";
    private DatagramSocket socket;
    private DatagramPacket packet;
    private InetAddress serverAddress;
    private int serverPort;
    private byte[] buffer;
    private Message m;

    public messageServer() throws UnknownHostException, IOException {
        this(DEFAULT_CONFIC);
    }

    public messageServer(String config) throws UnknownHostException, IOException {

        if (DEFAULT_HOST != null) {
            serverAddress = InetAddress.getByName(DEFAULT_HOST);
            this.serverPort = DEFAULT_PORT;
            socket = new DatagramSocket();
            buffer = new byte[2048];
            packet = new DatagramPacket(buffer, buffer.length);
        } else {
            throw new IOException("Invalid host name");
        }
//         try(messageServer srv = new messageServer()) {
//             srv.init();
//             
//         } catch (IOException e) {
//             System.out.println("Error 1:"+e.getMessage());
//         }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        messageServer tCPServer = new messageServer();
        try (ServerSocket ss = new ServerSocket(PORT);
                Socket s = ss.accept()) {
            tCPServer.run(s);

        } catch (IOException e) {
            System.out.println("Error 1" + e.getMessage());
        }

//        try (messageServer message = new messageServer()) {
//            String str;
//            while (message.init()) {
//                // str = message.readMessage();
//                str = "ok";
//                message.sendMessage(str);
//                message.getReply();
//                str = "exit\\q";
//                if (str.equalsIgnoreCase("exit\\q")) {
//                    break;
//                }
//            }
//
//        } catch (IOException e) {
//            System.out.println("Error 1" + e.getMessage());
//        }

    }

    private ObjectOutputStream dbOut;
    private ObjectInputStream dbInp;
    
    private void clientSession(ServerSocket ss){
        try
        {
            Socket s = ss.accept();
             new Thread(new ClientThread(s)).start();
            
        }catch(IOException e){
            System.out.println("err"+e.getMessage());
                
        }
    
    }
    
    private void run(Socket s) throws IOException, ClassNotFoundException {
        ///
        
        
        
        ///
        InputStream in = s.getInputStream();
        OutputStream os = s.getOutputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
Object d1=null;
        Message d = (Message) objectInputStream.readObject();
        m = d;
        System.out.println(d.getMessage());
        messageServer mServer = new messageServer();

        try (Socket socket = new Socket(messageServer.HOST, messageServer.PORT1)) {
           new Thread(new ClientThread(socket)).start();
//            OutputStream os1 = socket.getOutputStream();
//            InputStream in1 = socket.getInputStream();
//            dbOut = new ObjectOutputStream(os1);
//
//            dbOut.writeObject(d);//отправка на дб
//            dbInp = new ObjectInputStream(in1);
//           d1 = (Message[]) dbInp.readObject();

        } catch (IOException e) {//| ClassNotFoundException
            System.out.println("Error 1нетут" + e.getMessage());
        }

        ObjectOutputStream oos = new ObjectOutputStream(os);
        //   oos.writeObject(new Message());
        if (d1 != null) {
            oos.writeObject(d1);
        } else {
            oos.writeObject("Error: File not found");
        }
    }
//   private Message readDocument(String file){
//        
//        return new Document("title","Document body");
//    }    

    private class ClientThread implements Runnable,AutoCloseable{

        
        private Socket socket;
                public  ClientThread(Socket socket){
                    this.socket = socket;
                }
        @Override
        public void run() {
            try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ){
                while (true) {                    
                    Message m =(Message) ois.readObject();
                    if(m.getFrom() == null && m.getTo() == null && m.getMessage() == null)
                        break;
                    synchronized(dbOut){
                    dbOut.writeObject(m);
                    synchronized(dbInp){
                  
                  oos.writeObject(dbInp.readObject());//отправка на дб
                    }
                        while (true) {                            
                            
                        }
                    
                    }
                    
                }
                
                
                
            } catch (Exception e) {
                System.out.println("error 21"+e.getMessage());
            }
        }

        @Override
        public void close() throws Exception {
            if(!socket.isClosed()){
                socket.close();
            }
        }
    }
    
    private boolean init() throws IOException {
        try (messageServer message = new messageServer()) {
            String str;

            str = "ok";
            message.sendMessage(str);
            str = message.getReply();
            if (str.equals("ok")) {
                return false;
            }

            return true;
        }
    }

    private void sendMessage(String msg) throws IOException {
        packet.setData(msg.getBytes());
        packet.setLength(msg.length());
        packet.setAddress(serverAddress);
        packet.setPort(serverPort);
        socket.send(packet);
        packet.setData(buffer);
    }

    private String readMessage() throws IOException {
        byte[] b = new byte[255];
        int n;
        StringBuilder stringBinding = new StringBuilder();
        while (true) {
            n = System.in.read(b);
            stringBinding.append(new String(b, 0, n));
            if (stringBinding.toString().trim().endsWith("\\q")) {
                break;
            }

        }
        return stringBinding.toString().trim();
    }

    @Override
    public void close() throws IOException {

    }

    private String getReply() throws IOException {
        socket.receive(packet);
        System.out.println(packet.getAddress() + ":" + packet.getPort() + ">>" + new String(packet.getData(), 0, packet.getLength()));
        return new String(packet.getData(), 0, packet.getLength());
    }
}
