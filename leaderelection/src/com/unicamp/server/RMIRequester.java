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

    public void addRequester(RequesterInterface requesterInterface) {
        requesterInterfaces.add(requesterInterface);
    }

    @Override
    public void requestAccess(int timestamp, RequesterInterface requesterInterface) throws RemoteException {
        updateTime(timestamp);
        // If not accessing and not requesting, we can grant permission
        if (!accessing && !requesting) {
            requesterInterface.grantAccess(time);
            System.out.println("Granted access at time: " + time);
            return;
        }

        // If we are already accessing, put requester in the queue
        if (accessing) {
            requestingList.add(requesterInterface);
            System.out.println("Request received, but I'm already accessing at time: " + time);
            return;
        }

        // If we are also requesting, the oldest win
        if (requesting) {
            if (timestamp < requestTime) {
                // Incoming request is older, so I will grant permission
                requesterInterface.grantAccess(time);
                System.out.println("Older request received, so I'm granting access at time: " + time);
            } else {
                // I'm older than the incoming request, so I put request in the queue
                requestingList.add(requesterInterface);
                System.out.println("Newer request received, so I'm putting it in a queue at time: " + time);
            }
        }
    }

    @Override
    public void grantAccess(int timestamp) throws RemoteException {
        updateTime(timestamp);
        grantsReceived++;
        if (grantsReceived == requesterInterfaces.size()) {
            accessing = true;
            requesting = false;
            System.out.println("Access granted with " + grantsReceived + " grants received at time: " + time);
            grantsReceived = 0;
        } else {
            System.out.println("Received " + grantsReceived + " grants at time: " + time);
        }
    }

    public void requestAccess() throws RemoteException {
        time++;
        if (accessing) {
            System.out.println("Access already granted!");
            return;
        }
        requestTime = time;
        requesting = true;
        grantsReceived = 0;
        for (RequesterInterface requesterInterface : requesterInterfaces) {
            requesterInterface.requestAccess(requestTime, this);
        }
    }

    public void releaseAccess() throws RemoteException {
        time++;
        if (!accessing) {
            System.out.println("No access granted");
            return;
        }

        // Remove access and grant access to all request in the que
        accessing = false;
        for (RequesterInterface requesterInterface : requestingList) {
            requesterInterface.grantAccess(time);
        }
        requestingList.clear();
        System.out.println("Access released at time: " + time);
    }

    private void updateTime(int timestamp) {
        time++;
        if (timestamp > time) {
            time = timestamp + 1;
        }
    }
}
