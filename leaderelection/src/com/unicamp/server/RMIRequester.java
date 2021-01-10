package com.unicamp.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RMIRequester extends UnicastRemoteObject implements RequesterInterface {

    private Integer time;
    private int requestTime;
    private int grantsReceived;
    private boolean accessing;
    private boolean requesting;
    private ArrayList<RequesterInterface> requesterInterfaces;
    private ArrayList<RequesterInterface> requestingList;

    public RMIRequester() throws RemoteException {
        super();
        this.time = new Integer(0);
        this.accessing = false;
        this.requesterInterfaces = new ArrayList<>();
        this.requestingList = new ArrayList<>();
        this.requesting = false;
    }

    public int send(int originId, int receiverId, MessageTypeEnum message) throws RemoteException {
        System.out.println("Sendind message " + message.name() + " to ID= " + id);

        switch message {

            case OK:
                return 1;
            case PING:
                return 1;
            case CLAIM_LEADER:
                return 1;
            case ASK_FOR_LEADER:
                return 1;
            default:
                System.out.println("Invalid message: " + message.name());
                return 0;
        }
    }

    public int receive(int senderId, int receiverId, MessageTypeEnum message) {
        switch message {
            case OK:
                return 1;
            case PING:
                return 1;
            case CLAIM_LEADER:
                return 1;
            case ASK_FOR_LEADER:
                // Responde OK para o processo que chamou
                sendOkMessage(senderId);
                // Cria um array apenas com os ids maiores que o id recebido

                ArrayList<Integer> greaterIds = getGreaterIds(receiverId);
                askForLeader(greaterIds);
                return 1;
            default:
                System.out.println("Invalid message: " + message.name());
                return 0;
        }
    }
}
