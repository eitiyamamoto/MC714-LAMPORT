package com.company.client;

import com.company.events.Message;
import com.company.server.ServerInterface;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class RMIClient {
    public static void main(String argv[]) {
        // Getting reference to host
        String host = "";
        if (argv.length == 1) {
            host = argv[0];
        } else {
            System.out.println("Usage: RMIClient server");
            System.exit(1);
        }

        // Set security manager
        //System.setSecurityManager(new RMISecurityManager());
        //System.setProperty("java.security.policy","com/company/security/grant.policy");

        // Create reference to the server
        String name = "rmi://" + host + "/RMIServer";
        System.out.println("Looking up: " + name);

        // Connect to the server
        ServerInterface server = null;
        try {
            server = (ServerInterface) Naming.lookup(name);
        } catch (Exception e) {
            System.out.println("Exception " + e);
            System.exit(1);
        }

        try {
            Message message = new Message(1, "Test");
            Message messageReceived = server.send(message);
            System.out.println("Message sent: " + messageReceived.getMessage() + " Time: " + messageReceived.getTime());
        } catch (Exception e) {
            System.out.println("Exception " + e);
            System.exit(0);
        }
    }
}
