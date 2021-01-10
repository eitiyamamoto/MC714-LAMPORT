package leaderelection;

import com.unicamp.server.RMIRequester;
import com.unicamp.server.RequesterInterface;

import java.io.Console;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;
import java.util.ArrayList;
import static java.lang.Thread.sleep;

public class Election {

    int MAX_INSTANCES = 3;
    int leaderId;
    ArrayList<Integer> ids = new ArrayList<>();

    public void startElection(int id) {
        System.out.println("Election started!");
        // envia mensagem de inicio de eleição para todos os processos com id maior de quem iniciou

        // Cria um array apenas com os ids maiores que o id recebido
        ArrayList<Integer> greaterIds = getGreaterIds(id);
        askForLeader(greaterIds);
    }

    public void askForLeader(ArrayList<Integer> ids) {

        // Envia mensagem para todos os ids maiores que quem chamou
        int ret = send(ids, MessageTypeEnum.ASK_FOR_LEADER);

        if (ret == 1) {
            // Caso ao menos um processo esteja ativo, acabou o trabalho do processo menor
            return;
        } else {
            // Caso não haja resposta de um processo de id maior, este é o novo líder
            leaderId = id;
            sendClaimLeaderMessage(id);
        }
    }

    public void main() throws InterruptedException {

        while (true) {

            boolean isLeaderUp = ping(leaderId);

            if (!isLeaderUp) {
                // Se o lider não esta respondendo, chama eleições
                System.out.println("Leader not responding. Starting election.")
                boolean atLeatOneAns = startElection(this.id);

                if (!atLeatOneAns) {
                    // Se ninguem respondeu à eleição, quem chamouo é o lider
                    leaderId = this.id;
                    sendClaimLeaderMessage(this.id);
                }
            }
            // espera 10s para enviar um ping novamente
            sleep(10000);
        }
    }

    /** Envia mensagem para uma lista de ids */
    public int send(ArrayList<Integer> ids, MessageTypeEnum message) {

        for (int i = 0; i<ids.size(); i++) {
            int ret = send(i, message);

            if (ret == 1) {
                return 1;
            }
        }
    }

    /** Envia mensagem para um id */
    public int send(int id, MessageTypeEnum message);

    public int receivePing() {
        return 1;
    }

    public void receiveMessage(int id) {
        // Responde OK para o processo que chamou
        sendOkMessage(id);
        // Cria um array apenas com os ids maiores que o id recebido

        ArrayList<Integer> greaterIds = getGreaterIds(id);
        askForLeader(greaterIds);
    }

    /** Retorna um array com os ids maiores do que recebido por parametro */
    public ArrayList<Integer> getGreaterIds(int id) {

        ArrayList<Integer> greaterIds = new ArrayList<Integer>();
        for (int i = 1; i<MAX_INSTANCES - id; i++){
            greaterIds.add(ids.get(id + i));
        }

        return greaterIds;
    }

    public void sendClaimLeaderMessage(int id);

    public void sendOkMessage(int id);

}