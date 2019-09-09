package RMI.server;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Komratov-VA
 */
public interface irmServer extends Remote{
    
    String hello(String user) throws RemoteException;
    
}
