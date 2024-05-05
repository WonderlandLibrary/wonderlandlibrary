package club.async.command;

import club.async.command.impl.*;
import club.async.util.ChatUtil;

import java.util.ArrayList;
import java.util.Collections;

public class CommandRegistry {

    private ArrayList<Command> COMMANDS;

    public CommandRegistry() {

        COMMANDS = new ArrayList<>();

        addCommands(
                new ToggleCommand(),
                new BindCommand(),
                new FolderCommand(),
                new HelpCommand()
        );

    }

    private void addCommands(Command... commands) {
        Collections.addAll(COMMANDS, commands);
    }

    public ArrayList<Command> getCommands() {
        return COMMANDS;
    }

    public void execute(String input) {

        String[] split = input.split(" ");
        String command = split[0];
        String args = input.substring(command.length()).trim();

        for(Command c : getCommands()) {
            for(String alias : c.getAliases()) {
                if(command.equalsIgnoreCase(alias)) {
                    try {
                        c.execute(args.split(" "));
                    } catch (Exception e) {
                        c.usage();
                        e.printStackTrace();
                    }
                    return;
                }
            }
        }
        ChatUtil.addChatMessage("Invalid Command!");
    }

    public <T extends Command> T commandBy(String name) {
        return (T) COMMANDS.stream().filter(command -> command.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}