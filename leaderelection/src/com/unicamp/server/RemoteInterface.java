package com.unicamp.server;
import leaderelection.MessageTypeEnum;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {

    public void addRequester(RemoteInterface requester, boolean isGreater) throws  RemoteException;
    public int send(RemoteInterface receiver, MessageTypeEnum message) throws RemoteException;
    public int receive(RemoteInterface sender, MessageTypeEnum message) throws RemoteException;
    public void claimLeader() throws RemoteException;
    public int receiveOkMessage(RemoteInterface sender) throws RemoteException;
    public void askedForLeader(RemoteInterface sender) throws RemoteException;
    public boolean getIsUp() throws  RemoteException;
    public void setIsUp(boolean isUp) throws  RemoteException;
    public void setLeader(RemoteInterface leader) throws  RemoteException;
    public RemoteInterface getLeader() throws  RemoteException;
    }
