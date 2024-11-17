package org.example.server;

import org.example.dispatcher.Dispatcher;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.function.Function;

public class Server {
    private final int port;
    private final Dispatcher dispatcher;

    public Server(int port) {
        this.port = port;
        this.dispatcher = new Dispatcher();
    }

    public void setHandlers(Map<String, Function<String, String>> handlers) {
        handlers.entrySet().forEach(entry -> {
            this.dispatcher.putCommand(entry.getKey(), entry.getValue());
        });
    }

    public void start() {

        System.out.println("Starting server on port: " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running. Waiting for connections...");
            while (true) {
                Socket clientSocket = serverSocket.accept();

                dispatcher.setClientSocket(clientSocket);
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new Thread(dispatcher).start();
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }
}