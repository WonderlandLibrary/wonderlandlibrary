// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import java.util.Iterator;
import com.elementars.eclient.module.Module;
import com.elementars.eclient.util.Wrapper;
import dev.xulu.settings.Value;
import com.elementars.eclient.Xulu;
import com.elementars.eclient.command.Command;

public class SetCommand extends Command
{
    public SetCommand() {
        super("set", "Sets the settings of a module", new String[] { Xulu.MODULE_MANAGER.getModules().toString() });
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length < 2) {
            Command.sendChatMessage("Please specify a module");
            return;
        }
        if (args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            return;
        }
        if (args.length < 3) {
            Command.sendChatMessage("Please specify which setting you would like to change");
            return;
        }
        if (args.length < 4) {
            Command.sendChatMessage("Please enter a value you would like to set");
            return;
        }
        final Module m = Xulu.MODULE_MANAGER.getModuleByName(args[1]);
        if (m == null) {
            Command.sendChatMessage("Module not found!");
            return;
        }
        Value s = null;
        for (final Value v : Xulu.VALUE_MANAGER.getValuesByMod(m)) {
            if (v.getName().equalsIgnoreCase(args[2])) {
                s = v;
            }
        }
        if (s == null) {
            Command.sendChatMessage("Setting not found!");
            return;
        }
        if (s.getParentMod() != m) {
            Command.sendChatMessage(m.getDisplayName() + " has no setting " + s.getName());
            return;
        }
        try {
            if (s.isToggle()) {
                s.setValue(Boolean.parseBoolean(args[3]));
                Command.sendChatMessage("Set " + s.getName() + " to " + args[3].toUpperCase());
            }
            else if (s.isMode()) {
                if (s.getOptions().contains(args[3])) {
                    s.setValue(args[3]);
                    Command.sendChatMessage("Set " + s.getName() + " to " + args[3].toUpperCase());
                }
                else {
                    Command.sendChatMessage("Option " + args[3] + " not found!");
                }
            }
            else if (s.isNumber()) {
                if (Wrapper.getFileManager().determineNumber(s.getValue()).equalsIgnoreCase("INTEGER")) {
                    s.setValue(Integer.parseInt(args[3]));
                }
                else if (Wrapper.getFileManager().determineNumber(s.getValue()).equalsIgnoreCase("FLOAT")) {
                    s.setValue(Float.parseFloat(args[3]));
                }
                else if (Wrapper.getFileManager().determineNumber(s.getValue()).equalsIgnoreCase("DOUBLE")) {
                    s.setValue(Double.parseDouble(args[3]));
                }
                else {
                    Command.sendChatMessage("UNKNOWN NUMBER VALUE");
                }
                Command.sendChatMessage("Set " + s.getName() + " to " + args[3]);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Command.sendChatMessage("Error occured when setting value.");
        }
    }
}
