package org.example.dispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Function;

public class Dispatcher implements Runnable {
    private Socket clientSocket;
    private HashMap<String, Function<String, String>> commands;

    public Dispatcher() {
        this.commands = new HashMap<>();
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void putCommand(String command, Function<String, String> function) {
        commands.put(command.toLowerCase(), function);
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String inputLine;
            System.out.println("Processing client: " + clientSocket.getInetAddress());

            while ((inputLine = in.readLine()) != null) {
                System.out.println("Received: " + inputLine);

                if ("bye".equalsIgnoreCase(inputLine)) {
                    out.println("Goodbye!");
                    break;
                }

                Function<String, String> command = commands.get(inputLine.toLowerCase());
                if (command != null) {
                    String response = command.apply(inputLine);
                    out.println(response);
                } else {
                    out.println("Unknown command: " + inputLine);
                }
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