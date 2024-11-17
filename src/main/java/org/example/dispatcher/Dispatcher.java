package org.example.dispatcher;

import java.util.HashMap;
import java.util.function.Function;

public class Dispatcher {
    private final HashMap<String, Function<String, String>> commands;

    public Dispatcher() {
        this.commands = new HashMap<>();
    }

    public void putCommand(String command, Function<String, String> function) {
        commands.put(command.toLowerCase(), function);
    }

    public String handleRequest(String inputLine) {
        Function<String, String> command = commands.get(inputLine.toLowerCase());
        if (command != null) {
            return command.apply(inputLine);
        } else {
            return "Unknown command: " + inputLine;
        }
    }
}
