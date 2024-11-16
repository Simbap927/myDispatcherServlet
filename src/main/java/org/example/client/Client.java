package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private String hostIp;
    private int hostPort;

    public Client(String hostIp, int hostPort) {
        this.hostIp = hostIp;
        this.hostPort = hostPort;
    }

    public void start() throws IOException {
        Socket clientSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            clientSocket = new Socket(hostIp, hostPort);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("unknown host: " + hostIp);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("cannot connect to: " + hostIp);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;

        System.out.println("connected to host: \"" + hostIp + "\" port: \"" + hostPort + "\"");
        System.out.println("send message to the server...(\"bye\" to quit)");

        while ((userInput = stdIn.readLine()) != null) {
            out.println(userInput);
            System.out.println(in.readLine());

            // Break when client says Bye.
            if (userInput.equalsIgnoreCase("bye"))
                break;
        }

        out.close();
        in.close();
        stdIn.close();
        clientSocket.close();
    }
}