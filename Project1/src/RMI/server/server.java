
package RMI.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


/**
 *
 * @author komratov-va
 */
public class server extends UnicastRemoteObject implements irmServer{
    
    public server() throws RemoteException{     
    }
    
   @Override
   public String hello(String user) throws RemoteException{
       return user;
   }
   
   
   public static void main(String[] args) {
      
       try {
            server stub = new server();
           Registry r = LocateRegistry.createRegistry(1099);
           r.rebind("rmi://test-rmi", stub);
       } catch (RemoteException ex) {
           System.out.println("err"+ ex.getMessage());
       }
    }
                    
                            
}
