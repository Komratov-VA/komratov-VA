/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RMI.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import RMI.server.server;


/**
 *
 * @author JAVA
 */
public class client {
    public static void main(String[] args) throws RemoteException{
        try {
//            Registry r = LocateRegistry.getRegistry("198.168.18.141",1099);
            Registry r = LocateRegistry.getRegistry(1099);
             irmServer s = (irmServer) r.lookup("rmi://test-rmi");
            System.out.println(s.hello("test user"));
            
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Erro"+ex.getMessage());
        }
    }
}
