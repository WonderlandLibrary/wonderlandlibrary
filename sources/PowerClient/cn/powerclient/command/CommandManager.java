/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.command;

import java.util.ArrayList;
import me.AveReborn.command.Command;
import me.AveReborn.command.commands.CommandBind;
import me.AveReborn.command.commands.CommandBlock;
import me.AveReborn.command.commands.CommandTiplan;
import me.AveReborn.command.commands.CommandToggle;
import me.AveReborn.command.commands.CommandVersion;
import me.AveReborn.command.commands.CommandWdr;

public class CommandManager {
    private static ArrayList<Command> commands = new ArrayList();

    public CommandManager() {
        this.add(new CommandBind(new String[]{"bind"}));
        this.add(new CommandVersion(new String[]{"version", "v"}));
        this.add(new CommandToggle(new String[]{"toggle", "t"}));
        this.add(new CommandWdr(new String[]{"wdr"}));
        this.add(new CommandBlock(new String[]{"block"}));
        this.add(new CommandTiplan(new String[]{"tiplan"}));
    }

    public void add(Command c2) {
        commands.add(c2);
    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }

    public static String removeSpaces(String message) {
        String space = " ";
        String doubleSpace = "  ";
        while (message.contains(doubleSpace)) {
            message = message.replace(doubleSpace, space);
        }
        return message;
    }
}

