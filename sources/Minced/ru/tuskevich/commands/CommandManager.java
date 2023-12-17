// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands;

import java.util.Iterator;
import java.util.List;
import ru.tuskevich.commands.impl.TeleportCommand;
import ru.tuskevich.commands.impl.GPSCommand;
import ru.tuskevich.commands.impl.CalcCommand;
import ru.tuskevich.commands.impl.StaffCommand;
import ru.tuskevich.commands.impl.PanicCommand;
import ru.tuskevich.commands.impl.EClipCommand;
import ru.tuskevich.commands.impl.ConfigCommand;
import ru.tuskevich.commands.impl.VClipCommand;
import ru.tuskevich.commands.impl.HClipCommand;
import ru.tuskevich.commands.impl.MacroCommand;
import ru.tuskevich.commands.impl.FriendCommand;
import ru.tuskevich.commands.impl.HelpCommand;
import ru.tuskevich.commands.impl.BindCommand;
import ru.tuskevich.event.EventManager;
import java.util.ArrayList;

public class CommandManager
{
    private final ArrayList<CommandAbstract> commands;
    
    public CommandManager() {
        this.commands = new ArrayList<CommandAbstract>();
        EventManager.register(new CommandHandler(this));
        this.commands.add(new BindCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new FriendCommand());
        this.commands.add(new MacroCommand());
        this.commands.add(new HClipCommand());
        this.commands.add(new VClipCommand());
        this.commands.add(new ConfigCommand());
        this.commands.add(new EClipCommand());
        this.commands.add(new PanicCommand());
        this.commands.add(new StaffCommand());
        this.commands.add(new CalcCommand());
        this.commands.add(new GPSCommand());
        this.commands.add(new TeleportCommand());
    }
    
    public List<CommandAbstract> getCommands() {
        return this.commands;
    }
    
    public boolean execute(final String args) {
        for (final CommandAbstract command : this.commands) {
            final String[] split = args.substring(1).split(" ");
            if (split[0].equalsIgnoreCase(command.name)) {
                try {
                    command.execute(split);
                }
                catch (Exception e) {
                    command.error();
                }
                return true;
            }
        }
        return false;
    }
}
