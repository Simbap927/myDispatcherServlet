package org.example.client;

import java.io.IOException;

public class ClientApplication {
    public static void main(String[] args) throws IOException {
        String hostIp = "127.0.0.1";
        int hostPort = 12345;

        Client client = new Client(hostIp, hostPort);

        try {
            client.start();
        } catch (IOException e) {
            System.err.println("Error starting client: " + e.getMessage());
        }
    }
}