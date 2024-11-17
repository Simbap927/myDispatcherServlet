package org.example.server;

import java.util.List;
import java.util.Map;

public class ServerApplication {
    public static void main(String[] args) {
        int port = 12345;

        Server server = new Server(port);
        server.setHandlers(Map.of());
        server.start();
    }
}
