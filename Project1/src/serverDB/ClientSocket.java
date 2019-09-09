package serverDB;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import message.Message;
import messageServer.messageServer;

/**
 *
 * @author Komratov-va
 */
public class ClientSocket {

    private String name;

    public ClientSocket() {
        this("seva");
    }

    public ClientSocket(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        ClientSocket tCPClient = new ClientSocket();
        try (Socket socket = new Socket(messageServer.HOST, messageServer.PORT)) {
            tCPClient.run(socket);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error 1" + e.getMessage());
        }

    }

    private void run(Socket socket) throws IOException, ClassNotFoundException {
        OutputStream os = socket.getOutputStream();
        InputStream in = socket.getInputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(getFileName());
        ObjectInputStream objectInputStream = new ObjectInputStream(in);
        Object d = (Message[]) objectInputStream.readObject();
        if (d instanceof Message[]) {
            if(((Message[]) ((Message[])d)).length >1){
               for(Message m: ((Message[]) ((Message[])d))){
                   System.out.println(m.getMessage());
               }
             
            }else
            System.out.println(((Message[])d)[0].getMessage());
        } else {
            System.out.println("ошибка");
        }
    }

    private Message getFileName() {
        //
        Message m = new Message();
        m.setTo(this.name);
        m.setFrom("ira");
        m.setMessage("poidem gyl9tb seva");
        return m;
    }

}
//сообщения адрессованные кому-то
//m.setTo(this.name);
//        m.setFrom(null);
//        m.setMessage("null");
// между людьми
//  m.setTo(this.name);
//        m.setFrom("ira");
//        m.setMessage("null");