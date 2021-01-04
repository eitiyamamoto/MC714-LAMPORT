package mutualexclusion;

import com.unicamp.server.RMIRequester;
import com.unicamp.server.RequesterInterface;

import java.io.Console;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

public class MutualExclusionMain {

    private static final String NAME = "Requester";

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
        RMIRequester requester;
        try {
            LocateRegistry.createRegistry(port);
            requester = new RMIRequester();
            Naming.rebind(name, requester);
            System.out.println(name + " ready...");
            mainApplication(requester);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void mainApplication(RMIRequester requester) {
        Console console = System.console();
        boolean exit = false;
        if (console == null) {
            System.out.println("Problem to get console");
            System.exit(1);
        }

        while (!exit) {
            System.out.println("Type (r) to connect to another server\n" +
                    "Type (a) to request access resource\n" +
                    "Type (t) to release access to resource\n" +
                    "Type (q) to quit application");
            String input = console.readLine();
            switch (input) {
                case "r":
                    registerRequester(console, requester);
                    break;
                case "a":
                    requestAccess(requester);
                    break;
                case "t":
                    releaseAccess(requester);
                    break;
                case "q":
                    System.out.println("bye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid input");
            }
        }
    }

    private static void registerRequester(Console console, RMIRequester requester) {
        String host = console.readLine("Type host: ");
        String name = console.readLine("Type name: ");
        String connectionUrl = "rmi://" + host + "/" + name;

        try {
            RequesterInterface requesterInterface = (RequesterInterface) Naming.lookup(connectionUrl);
            requester.addRequester(requesterInterface);
            System.out.println("Server correctly connected!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void requestAccess(RMIRequester requester) {
        try {
            requester.requestAccess();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void releaseAccess(RMIRequester requester) {
        try {
            requester.releaseAccess();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
