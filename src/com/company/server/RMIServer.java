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
    public Message send(Message message) throws RemoteException {
        Message messageEvent = new Message();
        messageEvent.setMessage(message.getMessage());
        int timestamp = message.getTime();
        messageEvent.setTime(timestamp);
        if (timestamp > time) {
            time = timestamp;
        }
        time++;
        System.out.println("Received message: " + messageEvent.getMessage() + " at time : " + messageEvent.getTime());
        return messageEvent;
    }

    public Message sendoToAnotherServer(String text, ServerInterface anotherServer) throws RemoteException {
        time++;
        Message message = new Message(time, text);
        Message messageSent = anotherServer.send(message);
        System.out.println("Message sent: " + messageSent.getMessage() + " at time: " + messageSent.getTime());
        return messageSent;
    }
}
