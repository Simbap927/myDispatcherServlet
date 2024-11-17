package org.example.handler;

import java.util.Map;
import java.util.function.Function;
import java.time.LocalDateTime;

public class CommandHandler {
    public Map<String, Function<String, String>> getCommandHandlers() {
        return Map.of(
                "hello", this::handleHello,
                "time", this::handleTime,
                "echo", this::handleEcho,
                "bye", this::handleBye
        );
    }

    private String handleHello(String input) {
        return "Hi there!";
    }

    private String handleTime(String input) {
        return "Current time: " + LocalDateTime.now();
    }

    private String handleEcho(String input) {
        return "Echo: " + input;
    }

    private String handleBye(String input) {
        return "Goodbye!";
    }
}
