package leaderelection;

import com.unicamp.server.RMIRequester;

import java.io.Console;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import static java.lang.Thread.sleep;

public class LeaderElection {

    private static final String NAME = "Requester";


    /** Inicia eleição */
    public static void startElection(RMIRequester electionCaller) throws RemoteException {
        System.out.println("Election started by ID= " + electionCaller.hashCode() + "!");

        // envia mensagem de inicio de eleição para todos os processos com id maior de quem iniciou
        for (RMIRequester requester : electionCaller.requesters) {
            requester.receive(requester, MessageTypeEnum.ASK_FOR_LEADER);
        }
    }

    private static void registerRequester(Console console, RMIRequester requester) {
        String host = console.readLine("Type host: ");
        String name = console.readLine("Type name: ");
        String isGreater = console.readLine("Is greater: ");
        String connectionUrl = "rmi://" + host + "/" + name;

        try {
            RMIRequester requester1 = (RMIRequester) Naming.lookup(connectionUrl);

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
        requester.isUp = false;
    }

    private static void turnOnInstance(RMIRequester requester) {
        requester.isUp = true;
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
                case "q":
                    System.out.println("bye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid input");
            }
            try {
                if (!requester.leader.isUp) {
                    // Se o lider não esta respondendo, chama eleições
                    System.out.println("Leader not responding. Starting election.");
                    startElection(requester);
                }
                // espera 10s para enviar um ping novamente
                sleep(10000);
                }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}