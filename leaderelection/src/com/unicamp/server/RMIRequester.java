package com.unicamp.server;

import leaderelection.MessageTypeEnum;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RMIRequester extends UnicastRemoteObject {
    int ansCount = 0;
    public RMIRequester leader;
    public boolean isUp;
    public ArrayList<RMIRequester> requesters = new ArrayList<>();
    public ArrayList<RMIRequester> allRequesters = new ArrayList<>();

    public RMIRequester() throws RemoteException {
        super();
        this.isUp = true;
    }

    public void addRequester(RMIRequester requester, boolean isGreater) {
        if(isGreater) {
            requesters.add(requester);
        }
         allRequesters.add(requester);
    }

    public int send(RMIRequester receiver, MessageTypeEnum message) throws RemoteException {
        System.out.println("Sendind message " + message.name() + " to ID= " + receiver.hashCode());

        switch (message) {
            case OK:
                receiver.receive(this, MessageTypeEnum.OK);
                return 1;
            case CLAIM_LEADER:
                claimLeader(this);
                return 1;
            case ASK_FOR_LEADER:
                askForLeader(receiver);
                return 1;
            default:
                System.out.println("Invalid message: " + message.name());
                return 0;
        }
    }

    public int receive(RMIRequester sender, MessageTypeEnum message) throws RemoteException {
        System.out.println("Message received:" + message.name() + " from ID= " + sender.hashCode());
        switch (message) {
            case OK:
                return 1;
            case CLAIM_LEADER:
                this.leader = sender;
                return 1;
            case ASK_FOR_LEADER:
                // Responde OK para o processo que chamou
                sendOkMessage(sender, this);
                askForLeader(this);
                return 1;
            default:
                System.out.println("Invalid message: " + message.name());
                return 0;
        }
    }

    /** Envia para todos os ids da lista a mensagem dizendo que é o lider */
    private void claimLeader(RMIRequester leader) throws RemoteException {
        for (RMIRequester requester : leader.allRequesters) {
            requester.receive(this, MessageTypeEnum.CLAIM_LEADER);
        }
    }

    public void sendOkMessage(RMIRequester receiver, RMIRequester sender) throws RemoteException {
        sender.send(receiver, MessageTypeEnum.OK);
    }

    /** Retorna um array com os ids maiores do que recebido por parametro */
    public ArrayList<RMIRequester> getGreaterIds(RMIRequester requester) {

        return requester.requesters;
    }

    /** Envia mensagem buscando um processo de id maior para ser o lider */
    public void askForLeader(RMIRequester sender) throws RemoteException {
        // Envia mensagem para todos os ids maiores que quem chamou
        int ret = receive(sender, MessageTypeEnum.ASK_FOR_LEADER);

        if (ret == 1) {
            // Caso ao menos um processo esteja ativo, acabou o trabalho do processo menor
            ansCount++;
        } else {
            // Caso não haja resposta de um processo de id maior, este é o novo líder
            send(this, MessageTypeEnum.CLAIM_LEADER);
        }
    }

}
