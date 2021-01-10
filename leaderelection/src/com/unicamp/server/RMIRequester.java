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

    public int send(int id, MessageTypeEnum message) throws RemoteException {
        System.out.println("Sendind message " + message.name() + " to ID= " + id);

        switch message:

        case OK:
            break;
        case PING:
            break;
        case CLAIM_LEADER:
            break;
        case ASK_FOR_LEADER:
            break;
        default:
            System.out.println("Invalid message: " + message.name());
            break;
    }
}
