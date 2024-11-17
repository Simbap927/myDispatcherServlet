package org.example.dispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class Dispatcher implements Runnable {
    private Socket clientSocket;
    private HashMap<String, Runnable> commands;

    public Dispatcher() {
        this.commands = new HashMap<>();
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public void putCommand(String command, Runnable runnable) {
        commands.put(command, runnable);
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

                Runnable command = commands.get(inputLine.toLowerCase());
                if (command != null) {
                    command.run();
                } else
                    out.println("Unknown command: " + inputLine);
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

    public void sendCurrentTime() {
        String currentTime = java.time.LocalTime.now().toString();
        sendResponse("Current time: " + currentTime);
    }
    public void sendCurrentDate() {
        String currentDate = java.time.LocalDate.now().toString();
        sendResponse("Current date: " + currentDate);
    }
    public void sendHello() {
        sendResponse("Hello! How can I help you?");
    }

    private void sendResponse(String message) {
        try {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(message);
        } catch (IOException e) {
            System.err.println("Error sending response: " + e.getMessage());
        }
    }
}