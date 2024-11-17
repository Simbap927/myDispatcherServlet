package org.example.server;

import org.example.dispatcher.Dispatcher;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        System.out.println("Starting server on port: " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running. Waiting for connections...");
            Dispatcher dispatcher = new Dispatcher();

            while (true) {
                // 클라이언트 연결 대기
                Socket clientSocket = serverSocket.accept();
                dispatcher.setClientSocket(clientSocket);
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                dispatcher.putCommand("time", dispatcher::sendCurrentTime);
                dispatcher.putCommand("date", dispatcher::sendCurrentDate);
                dispatcher.putCommand("hello", dispatcher::sendHello);

                // 클라이언트 처리
                new Thread(dispatcher).start();
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
        }
    }
}