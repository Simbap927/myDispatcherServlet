package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        System.out.println("Starting server on port: " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running. Waiting for connections...");

            while (true) {
                // 클라이언트 연결 대기
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // 클라이언트 처리
                new Thread(new Dispatcher(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }

    // 클라이언트 요청 처리
    class Dispatcher implements Runnable {
        private Socket clientSocket;
        private HashMap<String, Runnable> commands;

        public Dispatcher(Socket clientSocket) {
            this.clientSocket = clientSocket;
            this.commands = new HashMap<>();

            commands.put("time", this::sendCurrentTime);
            commands.put("date", this::sendCurrentDate);
            commands.put("hello", this::sendHello);
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

        private void sendCurrentTime() {
            String currentTime = java.time.LocalTime.now().toString();
            sendResponse("Current time: " + currentTime);
        }
        private void sendCurrentDate() {
            String currentDate = java.time.LocalDate.now().toString();
            sendResponse("Current date: " + currentDate);
        }
        private void sendHello() {
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
}