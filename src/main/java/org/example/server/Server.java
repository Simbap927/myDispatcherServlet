package org.example.server;

import org.example.dispatcher.Dispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    private void handleClient(Socket clientSocket) {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);

                if ("bye".equalsIgnoreCase(inputLine)) {
                    out.println("Goodbye!");
                    break;
                }

                // Dispatcher를 통해 요청 처리
                String response = dispatcher.handleRequest(inputLine);
                out.println(response);
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected.");
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}