package com.company.messanger;

import com.company.server.ServerInterface;

public class Connection {
    String host;
    String name;
    ServerInterface server;

    public Connection(String host, String name, ServerInterface server) {
        this.host = host;
        this.name = name;
        this.server = server;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServerInterface getServer() {
        return server;
    }

    public void setServer(ServerInterface server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "Connection{" +
                "host='" + host + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
