package com.unicamp.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RequesterInterface extends Remote {

    public int send(int senderId, int receiverId, MessageTypeEnum message) throws RemoteException;
    public int receive(int senderId, int receiverId, MessageTypeEnum message) throws RemoteException;
}
