package dev.tenacity.command;

import dev.tenacity.util.misc.ChatUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class CommandRepository {

    private final Map<String, AbstractCommand> commandMap;

    private CommandRepository(final Builder builder) {
        this.commandMap = builder.commandMap;
    }

    public void handleCommand(final String text) {
        // Everything that is needed to find the command and execute it by the text provided.
        final String[] splitText = text.substring(1).split(" ");
        final AbstractCommand command = commandMap.get(splitText[0].toLowerCase());
        final String[] arguments = Arrays.copyOfRange(splitText, 1, splitText.length);

        // If the command can be found, and it was used correctly, execute it.
        if(command != null) {
            if(arguments.length >= command.getNecessaryArguments())
                command.onCommand(arguments);
            else
                ChatUtil.error("Usage: " + command.getUsage());
        } else {
            ChatUtil.error("Command not found!");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Map<String, AbstractCommand> commandMap = new HashMap<>();

        public Builder putAll(final AbstractCommand... commands) {
            for(final AbstractCommand command : commands)
                commandMap.put(command.getName(), command);
            return this;
        }

        public CommandRepository build() {
            return new CommandRepository(this);
        }

    }

}
