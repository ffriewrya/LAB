package ru.itmo.moona.commands.base;

public class InputParser {
    private String commandName;
    private String arg;
    private String key;
    private String keyValue;

    public InputParser(String input) {
        String[] tokens = input.trim().split("\\s+");

        this.commandName = tokens[0].toLowerCase();

        for (int i = 1; i < tokens.length; i++) {
            String currentToken = tokens[i];

            if (currentToken.startsWith("--")) {
                this.key = currentToken;

                if (i + 1 < tokens.length) {
                    this.keyValue = tokens[i + 1];
                    i++;
                }
            } else {
                if (this.arg == null) {
                    this.arg = currentToken;
                }
            }
        }
    }

    public String getCommandName() {
        return commandName;
    }

    public String getArg() {
        return arg;
    }

    public String getKey() {
        return key;
    }

    public String getKeyValue() {
        return keyValue;
    }
}
