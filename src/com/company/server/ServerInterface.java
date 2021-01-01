package com.company.server;

import com.company.events.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {

    public Message send(Message message) throws RemoteException;
}
