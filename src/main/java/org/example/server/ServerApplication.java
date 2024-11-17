package org.example.server;

import org.example.handler.CommandHandler;

public class ServerApplication {
    public static void main(String[] args) {
        int port = 12345;

        Server server = new Server(port);

        CommandHandler commandHandler = new CommandHandler();
        server.setHandlers(commandHandler.getCommandHandlers());

        server.start();
    }
}
