package com.unicamp.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RequesterInterface extends Remote {

    public int send(int id, MessageTypeEnum message) throws RemoteException;
    public int receive(int id, MessageTypeEnum message) throws RemoteException;
}
