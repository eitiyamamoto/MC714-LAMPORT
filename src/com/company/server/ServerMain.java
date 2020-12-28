package com.company.server;

import com.company.server.RMIServer;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;

public class ServerMain {

    public static void main(String[] args) {
	    try {
            //System.setSecurityManager(new RMISecurityManager());
            LocateRegistry.createRegistry(52365);
	        String name = "RMIServer";
	        System.out.println("Registering as: " + name);
            RMIServer server = new RMIServer();
            Naming.rebind(name, server);
            System.out.println(name + " ready...");
        } catch (Exception e) {
	        System.out.println("Exception while registering " + e);
        }
    }
}
