package org.example.server;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ServerApplication {
    public static void main(String[] args) {
        int port = 12345;

        Server server = new Server(port);
        server.setHandlers(Map.of(
                "hello", input -> "Hi there!",
                "time", input -> "Current time: " + LocalDateTime.now(),
                "echo", input -> "Echo: " + input,
                "bye", input -> "Goodbye!"
        ));
        server.start();
    }
}
