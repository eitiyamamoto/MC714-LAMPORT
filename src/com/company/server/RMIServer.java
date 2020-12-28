package com.company.server;

import com.company.events.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements ServerInterface {

    private Integer time;
    public RMIServer() throws RemoteException {
        super();
        time = new Integer(0);
    }


    @Override
    public Message receive(String message, Integer timestamp) throws RemoteException {
        Message messageEvent = new Message();
        messageEvent.setMessage(message);
        messageEvent.setTime(timestamp);
        if (timestamp > time) {
            time = timestamp;
        }
        time++;
        return messageEvent;
    }


}
