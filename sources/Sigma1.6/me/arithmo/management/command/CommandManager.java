/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command;

import java.util.Collection;
import java.util.HashMap;
import me.arithmo.management.command.Command;
import me.arithmo.management.command.impl.Bind;
import me.arithmo.management.command.impl.Clear;
import me.arithmo.management.command.impl.Color;
import me.arithmo.management.command.impl.Damage;
import me.arithmo.management.command.impl.Friend;
import me.arithmo.management.command.impl.Help;
import me.arithmo.management.command.impl.Insult;
import me.arithmo.management.command.impl.Janitor;
import me.arithmo.management.command.impl.LoadConfig;
import me.arithmo.management.command.impl.NotificationTest;
import me.arithmo.management.command.impl.PluginFinder;
import me.arithmo.management.command.impl.Save;
import me.arithmo.management.command.impl.Say;
import me.arithmo.management.command.impl.Settings;
import me.arithmo.management.command.impl.Target;
import me.arithmo.management.command.impl.Toggle;
import me.arithmo.management.command.impl.VClip;
import me.arithmo.management.command.impl.Waypoint;

public class CommandManager {
    public static final HashMap<String, Command> commandMap = new HashMap();

    public void addCommand(String name, Command command) {
        commandMap.put(name, command);
    }

    public Collection<Command> getCommands() {
        return commandMap.values();
    }

    public Command getCommand(String name) {
        return commandMap.get(name.toLowerCase());
    }

    public void setup() {
        new Waypoint(new String[]{"Waypoints", "waypoint", "wp", "marker", "mark", "w"}, "Add/Remove waypoints.").register(this);
        new PluginFinder(new String[]{"PluginFinder", "pf", "plugins"}, "").register(this);
        new Janitor(new String[]{"Janitor", "cleaner", "inventory", "ic"}, "Add/Remove items from the blacklist.").register(this);
        new Color(new String[]{"Color", "c", "colors"}, "Change customizable colors.").register(this);
        new Save(new String[]{"Save", "sv"}, "Save config").register(this);
        new Insult(new String[]{"Insult", "i"}, "Insult those faggot nodus users.").register(this);
        new Damage(new String[]{"Damage", "dmg", "kys", "suicide", "amandatodd"}, "Damages exploit.").register(this);
        new LoadConfig(new String[]{"Load", "load"}, "Loads config").register(this);
        new Toggle(new String[]{"Toggle", "t"}, "Toggles the module.").register(this);
        new Settings(new String[]{"Setting", "set", "s"}, "Changing and listing settings for modules.").register(this);
        new Help(new String[]{"Help", "halp", "h"}, "Help for commands.").register(this);
        new Say(new String[]{"Say", "talk", "chat"}, "Send a message with your chat prefix.").register(this);
        new Bind(new String[]{"Bind", "key", "b"}, "Send a message with your chat prefix.").register(this);
        new Friend(new String[]{"Friend", "fr", "f"}, "Add and remove friends.").register(this);
        new Target(new String[]{"Target", "focus", "target", "vip"}, "Set the target for the Aura and AutoIon.").register(this);
        new Clear(new String[]{"Clear", "cl"}, "Clears chat for you.").register(this);
        new NotificationTest(new String[]{"Test", "nt"}, "Notifications test command.").register(this);
        new VClip(new String[]{"VClip", "vc", "clip"}, "Clips you vertically.").register(this);
    }
}

