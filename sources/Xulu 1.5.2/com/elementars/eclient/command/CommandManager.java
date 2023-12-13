// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command;

import java.util.Iterator;
import com.elementars.eclient.util.Wrapper;
import com.elementars.eclient.command.commands.NicknameCommand;
import com.elementars.eclient.command.commands.DummyCommand;
import com.elementars.eclient.command.commands.AntiVoidCommand;
import com.elementars.eclient.command.commands.WaypointCommand;
import com.elementars.eclient.command.commands.LogspotCommand;
import com.elementars.eclient.command.commands.YawCommand;
import com.elementars.eclient.command.commands.CustomFontCommand;
import com.elementars.eclient.command.commands.EnemyCommand;
import com.elementars.eclient.command.commands.FriendCommand;
import com.elementars.eclient.command.commands.PrefixCommand;
import com.elementars.eclient.command.commands.ReloadCommand;
import com.elementars.eclient.command.commands.SaveCommand;
import com.elementars.eclient.command.commands.SearchCommand;
import com.elementars.eclient.command.commands.XrayCommand;
import com.elementars.eclient.command.commands.DrawnCommand;
import com.elementars.eclient.command.commands.SetStringCommand;
import com.elementars.eclient.command.commands.SetCommand;
import com.elementars.eclient.command.commands.ToggleCommand;
import com.elementars.eclient.command.commands.BindCommand;
import com.elementars.eclient.command.commands.MacroCommand;
import com.elementars.eclient.command.commands.HelpCommand;
import com.elementars.eclient.command.commands.CreditsCommand;
import com.elementars.eclient.command.commands.AboutCommand;
import java.util.ArrayList;

public class CommandManager
{
    public static ArrayList<Command> commands;
    public static ArrayList<String> rcommands;
    
    public void init() {
        CommandManager.commands.add(new AboutCommand());
        CommandManager.commands.add(new CreditsCommand());
        CommandManager.commands.add(new HelpCommand());
        CommandManager.commands.add(new MacroCommand());
        CommandManager.commands.add(new BindCommand());
        CommandManager.commands.add(new ToggleCommand());
        CommandManager.commands.add(new SetCommand());
        CommandManager.commands.add(new SetStringCommand());
        CommandManager.commands.add(new DrawnCommand());
        CommandManager.commands.add(new XrayCommand());
        CommandManager.commands.add(new SearchCommand());
        CommandManager.commands.add(new SaveCommand());
        CommandManager.commands.add(new ReloadCommand());
        CommandManager.commands.add(new PrefixCommand());
        CommandManager.commands.add(new FriendCommand());
        CommandManager.commands.add(new EnemyCommand());
        CommandManager.commands.add(new CustomFontCommand());
        CommandManager.commands.add(new YawCommand());
        CommandManager.commands.add(new LogspotCommand());
        CommandManager.commands.add(new WaypointCommand());
        CommandManager.commands.add(new AntiVoidCommand());
        CommandManager.commands.add(new DummyCommand());
        CommandManager.commands.add(new NicknameCommand());
    }
    
    public static void runCommand(final String message) {
        final String[] args = message.split(" ");
        for (int i = 0; i < args.length; ++i) {
            if (args[i] != null) {
                args[i] = args[i].replaceAll("<>", " ");
            }
        }
        try {
            Wrapper.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(Command.getPrefix() + message);
            for (final Command command : CommandManager.commands) {
                if (command.getName().equalsIgnoreCase(args[0])) {
                    command.syntaxCheck(args);
                    return;
                }
            }
        }
        catch (Exception e) {
            Command.sendChatMessage("Error occured when running command!");
        }
        Command.sendChatMessage("Command not found. Try .help for a list of commands");
    }
    
    public static ArrayList<Command> getCommands() {
        return CommandManager.commands;
    }
    
    static {
        CommandManager.commands = new ArrayList<Command>();
        CommandManager.rcommands = new ArrayList<String>();
    }
}
