package leaderelection;

import com.unicamp.server.RMIRequester;
import com.unicamp.server.RemoteInterface;

import java.io.Console;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import static java.lang.Thread.sleep;

public class LeaderElection {

    private static final String NAME = "Requester";

    private static void registerRequester(Console console, RMIRequester requester) {
        String host = console.readLine("Type host: ");
        String name = console.readLine("Type name: ");
        String isGreater = console.readLine("Is greater: ");
        String connectionUrl = "rmi://" + host + "/" + name;

        try {
            RemoteInterface requester1 = (RemoteInterface) Naming.lookup(connectionUrl);

            if (isGreater.equals("y")) {
                requester.addRequester(requester1, true);
            } else if (isGreater.equals("n")) {
                requester.addRequester(requester1, false);
            }
            System.out.println("Server correctly connected!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void shutDownInstance(RMIRequester requester) {
        try {
            System.out.println("Shuting down instance: " + requester.hashCode());
            requester.setIsUp(false);
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

    private static void turnOnInstance(RMIRequester requester) {
        try {
            System.out.println("Turning up instance: " + requester.hashCode());
            requester.setIsUp(true);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

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

    public static void mainApplication(RMIRequester requester) {
        try {
            requester.setLeader(requester);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Console console = System.console();
            if (console == null) {
                System.out.println("Problem to get console");
                System.exit(1);
            }

        while (true) {
            System.out.println("Type (r) to connect to another server\n" +
                    "Type (to) to turn on an instance\n" +
                    "Type (sd) to shut down an instance\n" +
                    "Type (q) to quit application");
            String input = console.readLine();

            switch (input) {
                case "r":
                    registerRequester(console, requester);
                    break;
                case "to":
                    turnOnInstance(requester);
                    break;
                case "sd":
                    shutDownInstance(requester);
                    break;
                case "isup":
                    try {
                        System.out.println(requester.getIsUp());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "le":
                    try{
                        System.out.println(requester.getLeader().hashCode());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "q":
                    System.out.println("bye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid input");
            }
            try {
                if(requester.getLeader() != null) {
                    if (!requester.getLeader().getIsUp()) {
                        // Se o lider não esta respondendo, chama eleições
                        System.out.println("Leader not responding. Starting election.");
                        requester.startElection();
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}