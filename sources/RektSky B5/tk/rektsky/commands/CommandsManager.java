/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.ArrayList;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.Scanner;
import tk.rektsky.Client;
import tk.rektsky.commands.Command;
import tk.rektsky.commands.impl.BindCommand;
import tk.rektsky.event.impl.ChatEvent;

public class CommandsManager {
    public static final ArrayList<Command> COMMANDS = new ArrayList();

    public static void reloadCommands() {
        COMMANDS.clear();
        String prefix = "";
        if (!BindCommand.class.getName().startsWith(prefix)) {
            prefix = "";
        }
        Reflections reflections = new Reflections(prefix, new Scanner[0]);
        Set<Class<Command>> commands = reflections.getSubTypesOf(Command.class);
        for (Class<Command> cmd : commands) {
            try {
                Command m2 = cmd.newInstance();
                if (Client.finishedSetup) {
                    Client.addClientChat((Object)((Object)ChatFormatting.GREEN) + "Reloading Command: " + (Object)((Object)ChatFormatting.YELLOW) + m2.getName());
                }
                COMMANDS.add(m2);
            }
            catch (IllegalAccessException | InstantiationException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void init() {
        CommandsManager.reloadCommands();
    }

    public static void processCommand(ChatEvent event) {
        if (event.getMessage().startsWith(".")) {
            event.setCanceled(true);
            for (Command cmd : COMMANDS) {
                if (cmd.getName().equalsIgnoreCase(event.getMessage().split(" ")[0].replaceFirst(".", ""))) {
                    ArrayList<String> arguments = new ArrayList<String>();
                    for (String arg : event.getMessage().split(" ")) {
                        arguments.add(arg);
                    }
                    arguments.remove(0);
                    cmd.onCommand(event.getMessage(), arguments.toArray(new String[0]));
                    return;
                }
                for (String s2 : cmd.getAliases()) {
                    if (!s2.equalsIgnoreCase(event.getMessage().split(" ")[0].replaceFirst(".", ""))) continue;
                    ArrayList<String> arguments = new ArrayList<String>();
                    for (String arg : event.getMessage().split(" ")) {
                        arguments.add(arg);
                    }
                    arguments.remove(0);
                    cmd.onCommand(event.getMessage(), arguments.toArray(new String[0]));
                    return;
                }
            }
            Client.addClientChat((Object)((Object)ChatFormatting.RED) + "Unknown Command! Try .help");
        }
    }
}

