package de.tired.base.command;

import de.tired.base.annotations.CommandAnnotation;
import de.tired.util.log.IngameChatLog;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class CommandManager {

    private final ArrayList<Command> COMMANDS;

    public CommandManager() {
        COMMANDS = new ArrayList<>();
        final Reflections reflections = new Reflections("de.tired.base.command");
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(CommandAnnotation.class);
        for (Class<?> aClass : classes) {
            try {
                final Command module = (Command) aClass.newInstance();
                COMMANDS.add(module);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public ArrayList<Command> getCommands() {
        return COMMANDS;
    }


    private void addCommands(Command... commands) {
        COMMANDS.addAll(Arrays.asList(commands));
    }

    public void execute(String input) {

        String[] split = input.split(" ");
        String command = split[0];
        String args = input.substring(command.length()).trim();

        for(Command c : getCommands()) {
            for(String alias : c.alias) {
                if(command.equalsIgnoreCase(alias)) {
                    try {
                        c.doCommand(args.split(" "));
                    } catch (Exception e) {
                        c.getHelp();
                    }
                    return;
                }
            }
            if(command.equalsIgnoreCase(c.getName())) {
                try {
                    c.doCommand(args.split(" "));
                } catch (Exception e) {
                    c.getHelp();
                }
                return;
            }
        }

        IngameChatLog.INGAME_CHAT_LOG.doLog("Cant find command!");
    }

}
