package com.unicamp.server;

import leaderelection.MessageTypeEnum;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RMIRequester extends UnicastRemoteObject implements RemoteInterface {
    private int ansCount = 0;
    private RemoteInterface leader = null;
    private boolean isUp = false;
    private ArrayList<RemoteInterface> requesters = new ArrayList<>();
    private ArrayList<RemoteInterface> allRequesters = new ArrayList<>();

    public RMIRequester() throws RemoteException {
        super();
        this.isUp = true;
    }

    @Override
    public boolean getIsUp() throws  RemoteException {     return this.getIsUp();    }
    public void setIsUp(boolean isUp) throws  RemoteException{ this.isUp = isUp;}

    @Override
    public void setLeader(RemoteInterface leader) throws  RemoteException {
        this.leader = leader;
    }

    public RemoteInterface getLeader() throws  RemoteException { return this.leader;}

    public void addRequester(RemoteInterface requester, boolean isGreater) throws  RemoteException {
        if(isGreater) {
            requesters.add(requester);
        }
         allRequesters.add(requester);

        if(this.leader == null) {
            if(requester.getLeader() == null) {
                this.leader = requester;
            } else {
                this.leader = requester.getLeader();
            }
        }
    }
    /** Inicia eleição */
    public void startElection() throws RemoteException {
        System.out.println("Election started by ID= " + this.hashCode() + "!");

        // envia mensagem de inicio de eleição para todos os processos com id maior de quem iniciou
        for (RemoteInterface requester : this.requesters) {
            requester.receive(requester, MessageTypeEnum.ASK_FOR_LEADER);
        }
    }

    public int send(RemoteInterface receiver, MessageTypeEnum message) throws RemoteException {
        System.out.println("Sendind message " + message.name() + " to ID= " + receiver.hashCode());

        switch (message) {
            case OK:
                receiver.receive(this, MessageTypeEnum.OK);
                return 1;
            case CLAIM_LEADER:
                claimLeader();
                return 1;
            case ASK_FOR_LEADER:
                askForLeader(receiver);
                return 1;
            default:
                System.out.println("Invalid message: " + message.name());
                return 0;
        }
    }

    public int receive(RemoteInterface sender, MessageTypeEnum message) throws RemoteException {
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
    public void claimLeader() throws RemoteException {
        for (RemoteInterface requester : this.allRequesters) {
            requester.receive(this, MessageTypeEnum.CLAIM_LEADER);
        }
    }

    public void sendOkMessage(RemoteInterface receiver, RemoteInterface sender) throws RemoteException {
        sender.send(receiver, MessageTypeEnum.OK);
    }

    /** Envia mensagem buscando um processo de id maior para ser o lider */
    public void askForLeader(RemoteInterface sender) throws RemoteException {
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
