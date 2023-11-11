package me.nyan.flush.command;

import me.nyan.flush.command.impl.*;
import me.nyan.flush.event.impl.EventChat;
import me.nyan.flush.utils.other.ChatUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager {
    public List<Command> commands = new ArrayList<>();
    public String prefix = ".";

    public CommandManager() {
        commands.add(new Toggle());
        commands.add(new Bind());
        commands.add(new Help());
        commands.add(new Info());
        commands.add(new Teleport());
        commands.add(new HClip());
        commands.add(new VClip());
        commands.add(new Friend());
        commands.add(new Target());
        commands.add(new Say());
        commands.add(new Panic());
        commands.add(new Config());
        commands.add(new Find());
        commands.add(new CommandKillSults());
        commands.add(new DiscordRPC());
        commands.add(new CommandSpammer());
        commands.add(new Sigmaname());
        commands.add(new Username());
        commands.add(new CommandNameProtect());
        commands.add(new CommandSearch());
        commands.add(new ImageESP());
        commands.add(new CommandCape());
        commands.add(new Placeholders());
    }

    public void onChat(EventChat e) {
        String message = e.getMessage();
        if (!message.startsWith(prefix)) return;
        e.setCancelled(true);

        message = message.substring(prefix.length());

        if (message.split(" ").length == 0)
            return;

        String commandName = message.split(" ")[0];
        Command command = commands.stream().filter(command1 -> command1.alliases.contains(commandName) ||
                command1.name.equalsIgnoreCase(commandName)).findFirst().orElse(null);
        if (command != null) {
            command.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
            return;
        }

        ChatUtils.println("§4Unknown command.");
    }

    public List<Command> getCommands() {
        return commands;
    }
}
