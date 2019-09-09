package serverDB;

import com.sun.xml.internal.ws.api.message.saaj.SAAJFactory;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import message.MessageException;
import messageServer.messageServer;
import static messageServer.messageServer.PORT;

/**
 *
 * @author Komratov-VA
 */
public class ServerDb implements AutoCloseable {

    public static final String HOST = "localhost";
//    public static final int PORT = 10000;
    public static final int DEFAULT_PORT = 12345;
    public static final String DEFAULT_HOST = "localhost";
    private static String DEFAULT_VALUE = "javaDB.properties";
    private String STATUS_ACTIV = "activ";
    private String STATUS_BLOCK = "block";
    public Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private PreparedStatement blockUser;
    private PreparedStatement addMessage;
    private PreparedStatement getAddMessage;
    private PreparedStatement getMessage;
    private Properties properties;
    private DatagramSocket socket;
    private DatagramPacket packet;
    private byte[] buffer;

    public ServerDb() throws SocketException {
        this(DEFAULT_VALUE);
        Enumeration<Driver> enumirator = DriverManager.getDrivers(); //To change body of generated methods, choose Tools | Templates.
        Driver d;
        System.out.println("Drivers list:");
        while (enumirator.hasMoreElements()) {
            d = enumirator.nextElement();
            System.out.println("-->" + d + new Date(System.currentTimeMillis()));
        }

        String url = properties.getProperty("url");
        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("zbs");
            preparedStatement = connection.prepareStatement(properties.getProperty("update"));
            getMessage = connection.prepareStatement(properties.getProperty("update2"));

        } catch (SQLException ex) {
            System.out.println("Error #");
        }

    }

    public ServerDb(String url) throws SocketException {

//        socket = new DatagramSocket(DEFAULT_PORT);
//        buffer = new  byte[2048];
//        packet = new DatagramPacket(buffer,buffer.length);
//   
//        buffer = new  byte[2048];
//        packet = new DatagramPacket(buffer,buffer.length);
        try (FileReader reader = new FileReader(url)) {

            properties = new Properties();
            properties.load(reader);

        } catch (Exception e) {
        }

//    Enumeration<Driver> enumirator = DriverManager.getDrivers(); //To change body of generated methods, choose Tools | Templates.
//        Driver d;
//        System.out.println("Drivers list:");
//        while (enumirator.hasMoreElements()) {
//            d = enumirator.nextElement();
//            System.out.println("-->" + d + new Date(System.currentTimeMillis()));
//        }
    }

    public static void main(String[] args) throws SocketException, ClassNotFoundException {
       
//        try (ServerDb srv = new ServerDb()) {
//            if (srv.init()) {
//                srv.run();
//                
//            }
//        } catch (Exception e) {
//            System.out.println("Error 1" + e.getMessage());
//        }
        ServerDb sd = new ServerDb();
        try (ServerSocket ss = new ServerSocket(messageServer.PORT1);
                Socket s = ss.accept()) {
            System.out.println("212");
            sd.run(s);
            System.out.println("212");
        } catch (IOException e) {
            System.out.println("Error 1" + e.getMessage());
        }
    }
// if (connection.isValid(5)) {
//                statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);     
//        }
//        }catch (Exception ex) {
//            System.out.println("Error #4run:" + ex.getMessage());
//        }

    private boolean init() throws IOException, SQLException {
        //  socket = new DatagramSocket(DEFAULT_PORT);
        //коментировал
//        try {
//            
//       
//           socket.receive(packet);//заблокирует программу до получения потока.
//             printPacket();
//            sendReply();} catch (Exception e) {
//        }finally{
//            socket.close();
//        }

        return true;
    }

    private void run(Socket s) throws IOException, ClassNotFoundException {
        try {
            System.out.println("11111");
            Message[] m = null;
            InputStream in = s.getInputStream();
            OutputStream os = s.getOutputStream();
            System.out.println("11111");
            ObjectInputStream objectInputStream = new ObjectInputStream(in);
            //ошибка
            System.out.println("11111");
            Message d = (Message) objectInputStream.readObject();
            System.out.println("tyt1");
            System.out.println(d.getMessage());
            if (connection.isValid(5)) {
                statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                String msg = "false";
                //  while (true) {  

                if (d.getMessage().equals("add")) {
                    System.out.println("comand");
                    boolean rez = addUser(d.getFrom());
                    m = new Message[1];
                    if (rez) {
                        msg = "true";
                        System.out.println(rez);
                    }

                    Message m1 = new Message();
                    m1.setMessage(msg);
                    m[0] = m1;
                } else if (d.getMessage().equals("block")) {
                    boolean rez = blockUser(d.getFrom());
                    m = new Message[1];
                    if (rez) {
                        msg = "true";
                        System.out.println(rez);
                    }

                    Message m1 = new Message();
                    m1.setMessage(msg);
                    m[0] = m1;
                    
                    

                } else if (d.getMessage().equals("null") ) {
                    if (d.getFrom() == null) {
                        m = getAllMessage(d.getTo());
                        
                        
                    } else {
                        //String login, String from
                m = getMessage(d.getTo(),d.getFrom());
                    }
                } else {
                      
                        boolean rez =  addMessage(d);;
                    m = new Message[1];
                    if (rez) {
                        msg = "true";
                        System.out.println(rez);
                    }

                    Message m1 = new Message();
                    m1.setMessage(msg);
                    m[0] = m1;
                }
                ObjectOutputStream oos = new ObjectOutputStream(os);
                //   oos.writeObject(new Message());
                if (m != null) {
                    oos.writeObject(m);
                } else {
                    oos.writeObject("Error: File not found");
                }

                //  }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServerDb.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private String printPacket() {
        String str = new String(packet.getData(), 0, packet.getLength());
        System.out.println(packet.getAddress() + ":" + packet.getPort() + " >> " + str);
        return str;

    }

    private void sendReply() throws IOException, SQLException {
        if (connection.isValid(5)) {
            packet.setData("ok".getBytes());
        } else {
            packet.setData("exit".getBytes());
        }
        socket.send(packet);
        packet.setData(buffer);

    }

    @Override
    public void close() {
        if (!socket.isClosed()) {
            socket.disconnect();
        }
        // connection.close();
    }

    private boolean addUser(String login) {
        try {

            String add_user = properties.getProperty("add_user");
            statement.execute(add_user + "'" + login + "'," + "'" + new Date(System.currentTimeMillis()) + "','" + STATUS_ACTIV + "')");
        } catch (SQLException ex) {
            System.out.println("polsobatel estb");;
        }
        return true;
    }

    private boolean blockUser(String login) {
        try {

            String selectUsersForBlock = properties.getProperty("block_user_select");
            ResultSet result = statement.executeQuery(selectUsersForBlock + "'" + login + "'");

            boolean flag = result.next();
            if (flag == false || result.getString(3).equals(STATUS_BLOCK)) {
                return false;
            } else {
                result.updateString("status", STATUS_BLOCK);
                result.updateRow();

            }
        } catch (SQLException ex) {
            System.out.println("error");;
        }
        return true;
    }
    //messegFrom_user=Select * FROM MESSAGES Where toL= 
//Create TABLE Messages (fromL CHAR(8),toL CHAR(8), message CHAR(40),dateM DATE NOT NULL, foreign key(fromL) references Users(login),foreign key(toL) references Users(login));

    private Message[] getMessage(String login, String from) {//

        String messeg_user = properties.getProperty("messege_user");
//            statement.execute(add_user+"'"+login+"',"+"'"+new Date(System.currentTimeMillis())+"','"+STATUS_ACTIV+"')");
        List<Message> list = new ArrayList<>();

        try {
            ResultSet result = statement.executeQuery(messeg_user + "'" + login + "'AND FromL='" + from + "'");
            ResultSetMetaData resultSetMetaData = result.getMetaData();
//                Message[] messages = new Message[resultSetMetaData.getColumnCount()];

            while (result.next()) {

                list.add(new Message(result.getString(2), result.getString(1), result.getString(3)));
            }
            Message[] myArray = (list.toArray(new Message[0]));

            return myArray;

        } catch (SQLException ex) {
            Logger.getLogger(ServerDb.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private Message[] getAllMessage(String login) {//возвращаем не строку.
        //Select * FROM MESSAGES Where fromL=
        String messegAll_user = properties.getProperty("messege_user");
        List<Message> list = new ArrayList<>();

        try {
            ResultSet result = statement.executeQuery(messegAll_user + "'" + login + "'");
            ResultSetMetaData resultSetMetaData = result.getMetaData();
            //     Message[] messages = new Message[resultSetMetaData.getColumnCount()];
            System.out.println(resultSetMetaData.getColumnCount());
            //    for(int i=0;i<resultSetMetaData.getColumnCount();i++){

            while (result.next()) {

                list.add(new Message(result.getString(2), result.getString(1), result.getString(3)));
            }
            Message[] myArray = (list.toArray(new Message[0]));

            return myArray;

        } catch (SQLException ex) {
            Logger.getLogger(ServerDb.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private boolean addMessage(Message message) {
        //  String add_message = properties.getProperty("add_message");
        try {
            getMessage.setString(1, message.getTo());
            getMessage.setString(2, message.getFrom());
            getMessage.setString(3, message.getMessage());
            getMessage.setDate(4, new Date(System.currentTimeMillis()));
            // getMessage.addBatch();
            getMessage.execute();
            // statement.execute(add_message + "'" + message.getTo() + "'," + "'" + message.getFrom() + "','" + message.getMessage() + "','" + new Date(System.currentTimeMillis()) + "')");
            //preparedStatement.setString(2, result.getString(1));
//                        preparedStatement.setDate(1, new Date(t+delta*i));
//                        preparedStatement.addBatch();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ServerDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
        //Create TABLE Messages (fromL CHAR(8),toL CHAR(8), message CHAR(40),dateM DATE NOT NULL, foreign key(fromL) references Users(login),foreign key(toL) references Users(login));
        //String to,String from,String message
    }
}
