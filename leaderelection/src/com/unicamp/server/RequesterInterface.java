package com.unicamp.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RequesterInterface extends Remote {

    public int send(int id) throws RemoteException;
    public int ping(int id) throws RemoteException;


    public void grantAccess(int timestamp) throws RemoteException;
}
