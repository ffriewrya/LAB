package ru.itmo.moona.cli.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputParser {
    private String commandName;
    private String arg;
    private String key;
    private String keyValue;
    private List<String> adArgs = new ArrayList<>();

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
                } else {
                    this.adArgs.add(currentToken);
                }
            }
        }
    }

    public static Map<String, String> parseAdArgs(List<String> input) {
        Map<String, String> pairs = new HashMap<>();
        if (input.isEmpty()) {
            throw new IllegalArgumentException("field=value can't be null");
        }
        for (String pair : input) {
            int splitter = pair.indexOf('=');
            if (splitter == 0 || splitter == pair.length() - 1) {
                throw new IllegalArgumentException("invalid arguments. expected field=value");
            } else {
                String field = pair.substring(0, splitter).trim();
                String value = pair.substring(splitter + 1).trim();
                pairs.put(field, value);
            }
        }
        return pairs;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getArg() {
        return arg;
    }

    public List<String> getAdArgs() {
        return adArgs;
    }

    public String getKey() {
        return key;
    }

    public String getKeyValue() {
        return keyValue;
    }
}
