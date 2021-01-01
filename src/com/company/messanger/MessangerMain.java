package com.company.messanger;

import com.company.events.Message;
import com.company.server.RMIServer;
import com.company.server.ServerInterface;
import com.sun.security.ntlm.Server;

import java.io.Console;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

public class MessangerMain {

    private static final String NAME = "Messenger";

    public static void main(String argv[]) {
        // Get port to be used
        int port = 6285;
        String name = NAME;
        if (argv.length > 0) {
            port = Integer.parseInt(argv[0]);
        }
        if (argv.length == 2) {
            name = argv[1];
        }
        System.out.println("Using port: " + port);
        System.out.println("Using name: " + name);

        System.out.println("Registering as: " + name);
        RMIServer myServer;
        try {
            myServer = new RMIServer();
            Naming.rebind(name, myServer);
            System.out.println(name + " ready...");
            mainApplication(myServer);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void mainApplication(RMIServer myServer) {
        Console console = System.console();
        List<Connection> connectedServerList = new ArrayList<>();
        boolean exit = false;
        if (console == null) {
            System.out.println("Problem to get console");
            System.exit(1);
        }

        while (!exit) {
            System.out.println("Type (r) to connect to another server\n" +
                    "Type (m) to send another message\n" +
                    "Type (q) to quit application");
            String input = console.readLine();
            switch (input) {
                case "r":
                    registerServer(console, connectedServerList);
                    break;
                case "m":
                    sendMessage(console, myServer, connectedServerList);
                    break;
                case "q":
                    System.out.println("exit...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
    }

    private static void registerServer(Console console, List<Connection> connectedServerList) {
        String host = console.readLine("Type host: ");
        String name = console.readLine("Type name: ");
        String connectionUrl = "rmi://" + host + "/" + name;

        try {
            ServerInterface server = (ServerInterface) Naming.lookup(connectionUrl);
            Connection connection = new Connection(host, name, server);
            connectedServerList.add(connection);
            System.out.println("Server correctly connected!");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(Console console, RMIServer myServer, List<Connection> connectedServerList) {
        System.out.println("Connected servers:");
        int i = 0;
        for (Connection connection : connectedServerList) {
            System.out.println("(" + i + ") " + connection.toString());
            i++;
        }

        String selected = console.readLine("Type the number of the server: ");
        int selectedNumber = 0;
        try {
            selectedNumber = Integer.parseInt(selected);
        } catch (NumberFormatException e) {
            System.out.println("Wrong number input");
            return;
        }

        if (selectedNumber > connectedServerList.size() || selectedNumber < 0) {
            System.out.println("Out of index");
            return;
        }

        String text = console.readLine("Type the message: ");
        try {
            myServer.sendoToAnotherServer(text, connectedServerList.get(selectedNumber).getServer());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
