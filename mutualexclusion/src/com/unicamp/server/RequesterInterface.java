package com.unicamp.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RequesterInterface extends Remote {

    public void requestAccess(int timestamp, RequesterInterface requesterInterface) throws RemoteException;

    public void grantAccess(int timestamp) throws RemoteException;
}
