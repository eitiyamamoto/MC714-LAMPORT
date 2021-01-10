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
    int ansCount = 0;
    ArrayList<Integer> ids = new ArrayList<>();

    /** Inicia eleição */
    public void startElection(int id) {
        System.out.println("Election started by ID= " + id + "!");
        // envia mensagem de inicio de eleição para todos os processos com id maior de quem iniciou

        // Cria um array apenas com os ids maiores que o id recebido
        ArrayList<Integer> greaterIds = getGreaterIds(id);
        askForLeader(id, greaterIds);
    }

    /** Envia mensagem buscando um processo de id maior para ser o lider */
    public void askForLeader(int senderId, ArrayList<Integer> ids) {
        // Envia mensagem para todos os ids maiores que quem chamou
        int ret = send(senderId, ids, MessageTypeEnum.ASK_FOR_LEADER);

        if (ret == 1) {
            // Caso ao menos um processo esteja ativo, acabou o trabalho do processo menor
            ansCount++;
            return;
        } else {
            // Caso não haja resposta de um processo de id maior, este é o novo líder
            sendClaimLeaderMessage(id);
        }
    }

    /** Envia mensagem para uma lista de ids */
    public int send(int senderId, ArrayList<Integer> ids, MessageTypeEnum message) {

        for (int i = 0; i<ids.size(); i++) {
            if (send(senderId, i, message) == 1) {
                return 1;
            }
        }
    }

    /** Envia mensagem para um id */
    public int send(int senderId, int receiverId, MessageTypeEnum message);

    /** Retorna um array com os ids maiores do que recebido por parametro */
    public ArrayList<Integer> getGreaterIds(int id) {

        ArrayList<Integer> greaterIds = new ArrayList<Integer>();
        for (int i = 1; i < MAX_INSTANCES - id; i++){
            greaterIds.add(ids.get(id + i));
        }

        return greaterIds;
    }

    public void sendClaimLeaderMessage(int id) {
        this.leaderId = id;
        send(id, MessageTypeEnum.CLAIM_LEADER);
    }

    public void sendOkMessage(int id) {
        send(id, MessageTypeEnum.OK);
    }

    public void main() throws InterruptedException {

        while (true) {

            int isLeaderUp = send(leaderId, MessageTypeEnum.PING);

            if (!isLeaderUp) {
                // Se o lider não esta respondendo, chama eleições
                System.out.println("Leader not responding. Starting election.")
                startElection(this.id);
                boolean atLeatOneAns = ansCount > 0 ? true : false;

                if (!atLeatOneAns) {
                    // Se ninguem respondeu à eleição, quem chamouo é o lider
                    sendClaimLeaderMessage(this.id);
                }

                ansCount = 0;
            }
            // espera 10s para enviar um ping novamente
            sleep(10000);
        }
    }
}