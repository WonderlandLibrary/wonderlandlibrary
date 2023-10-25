/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.command.Command;
import me.Tengoku.Terror.command.impl.Bind;
import me.Tengoku.Terror.command.impl.ConfigLoad;
import me.Tengoku.Terror.command.impl.ConfigRemove;
import me.Tengoku.Terror.command.impl.ConfigSave;
import me.Tengoku.Terror.command.impl.Help;
import me.Tengoku.Terror.command.impl.Say;
import me.Tengoku.Terror.command.impl.StartBot;
import me.Tengoku.Terror.command.impl.Teams;
import me.Tengoku.Terror.command.impl.Toggle;
import me.Tengoku.Terror.command.impl.VClip;
import me.Tengoku.Terror.event.events.EventChat;

public class CommandManager {
    public Teams teams;
    public List<Command> commands = new ArrayList<Command>();
    public String prefix = ".";

    public Command getCommandByClass(Class clazz) {
        return this.commands.stream().filter(command -> command.getClass().equals(clazz)).findFirst().orElse(null);
    }

    public CommandManager() {
        this.teams = new Teams();
        this.setup();
    }

    public void handleChat(EventChat eventChat) {
        String string = eventChat.getMessage();
        if (!string.startsWith(this.prefix)) {
            return;
        }
        string = string.substring(this.prefix.length());
        boolean bl = false;
        if (string.split(" ").length > 0) {
            String string2 = string.split(" ")[0];
            eventChat.setCancelled(true);
            for (Command command : this.commands) {
                if (!command.aliases.contains(string2) && !command.name.equalsIgnoreCase(string2)) continue;
                command.onCommand(Arrays.copyOfRange(string.split(" "), 1, string.split(" ").length), string);
                bl = true;
                break;
            }
            if (!bl) {
                Exodus.addChatMessage("Command not found!");
            }
        }
    }

    public void setup() {
        this.commands.add(new Bind());
        this.commands.add(new VClip());
        this.commands.add(new Toggle());
        this.commands.add(new ConfigSave());
        this.commands.add(new ConfigRemove());
        this.commands.add(new ConfigLoad());
        this.commands.add(new Say());
        this.commands.add(new StartBot());
        this.commands.add(this.teams);
        this.commands.add(new Help());
    }
}

