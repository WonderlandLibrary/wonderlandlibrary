package me.aquavit.liquidsense.command;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.utils.data.Pair;
import me.aquavit.liquidsense.command.commands.*;
import me.aquavit.liquidsense.command.shortcuts.Shortcut;
import me.aquavit.liquidsense.command.shortcuts.ShortcutParser;
import me.aquavit.liquidsense.utils.client.ClientUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandManager {
    private final List<Command> commands = new ArrayList<>();
    private String[] latestAutoComplete = new String[0];
    private char prefix = '.';

    public char getPrefix() {
        return prefix;
    }

    public void setPrefix(char prefix) {
        this.prefix = prefix;
    }

    public String[] getLatestAutoComplete() {
        return latestAutoComplete;
    }

    public List<Command> getCommands() {
        return commands;
    }

    public void registerCommands() {
        List<Command> commandList = Arrays.asList(
                new BindCommand(),
                new HelpCommand(),
                new SayCommand(),
                new FriendCommand(),
                new AutoSettingsCommand(),
                new LocalAutoSettingsCommand(),
                new ServerInfoCommand(),
                new ToggleCommand(),
                new UsernameCommand(),
                new TargetCommand(),
                new BindsCommand(),
                new PanicCommand(),
                new PingCommand(),
                new RenameCommand(),
                new ReloadCommand(),
                new LoginCommand(),
                new RemoteViewCommand(),
                new PrefixCommand(),
                new ShortcutCommand(),
                new HideCommand(),
                new SetNameCommand()
        );

        for (Command command : commandList) {
            registerCommand(command);
        }
    }


    public Command getCommand(String name) {
        return commands.stream()
                .filter(it -> it.getCommand().equalsIgnoreCase(name.toLowerCase())
                        || Arrays.stream(it.getAlias()).anyMatch(alias -> alias.equalsIgnoreCase(name.toLowerCase())))
                .findFirst()
                .orElse(null);
    }

    public void executeCommands(String input) {
        String[] args = input.split(" ");

        for (Command command : commands) {
            if (args[0].equalsIgnoreCase(prefix + command.getCommand())) {
                command.execute(args);
                return;
            }

            for (String alias : command.getAlias()) {
                if (!args[0].equalsIgnoreCase(prefix + alias))
                    continue;

                command.execute(args);
                return;
            }
        }

        ClientUtils.displayChatMessage("§cCommand not found. Type " + prefix + "help to view all commands.");
    }


    public boolean autoComplete(String input) {
        this.latestAutoComplete = getCompletions(input);
        return input.startsWith(String.valueOf(this.prefix)) && this.latestAutoComplete != null && this.latestAutoComplete.length > 0;
    }

    private String[] getCompletions(String input) {
        if (input.length() > 0 && input.toCharArray()[0] == this.prefix) {
            String[] args = input.split(" ");

            if (args.length > 1) {
                Command command = getCommand(args[0].substring(1));
                if (command == null) return null;
                List<String> tabCompletions = command.tabComplete(Arrays.copyOfRange(args, 1, args.length));

                return tabCompletions.toArray(new String[0]);
            } else {
                String rawInput = input.substring(1);

                return commands.stream()
                        .filter(it -> it.getCommand().toLowerCase().startsWith(rawInput.toLowerCase())
                                || Arrays.stream(it.getAlias()).anyMatch(alias -> alias.toLowerCase().startsWith(rawInput.toLowerCase())))
                        .map(it -> {
                            String alias;
                            if (it.getCommand().toLowerCase().startsWith(rawInput.toLowerCase())) {
                                alias = it.getCommand();
                            } else {
                                alias = Arrays.stream(it.getAlias())
                                        .filter(temp -> temp.toLowerCase()
                                                .startsWith(rawInput.toLowerCase()))
                                        .findFirst().orElse(null);
                            }

                            return this.prefix + alias;
                        }).toArray(String[]::new);
            }
        }
        return null;
    }

    public void registerShortcut(String name, String script) {
        if (getCommand(name) == null) {
            List<Pair<Command, String[]>> shortcutCommands = ShortcutParser.parse(script).stream()
                    .map(it -> {
                        Command command = getCommand(it.get(0));
                        if (command != null) {
                            return new Pair<>(command, it.toArray(new String[0]));
                        } else {
                            throw new IllegalArgumentException("Command " + it.get(0) + " not found!");
                        }
                    }).collect(Collectors.toCollection(ArrayList::new));

            registerCommand(new Shortcut(name, shortcutCommands));
            LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.shortcutsConfig);
        } else {
            throw new IllegalArgumentException("Command already exists!");
        }
    }

    public void registerCommand(Command command) {
        commands.add(command);
    }

    public boolean unregisterShortcut(String name) {
        boolean removed = commands.removeIf(it -> it instanceof Shortcut && it.getCommand().equals(name));

        LiquidSense.fileManager.saveConfig(LiquidSense.fileManager.shortcutsConfig);

        return removed;
    }

    public void unregisterCommand(Command command) {
        commands.remove(command);
    }
}
