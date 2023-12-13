// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.command.commands;

import com.elementars.eclient.macro.Macro;
import com.elementars.eclient.Xulu;
import org.lwjgl.input.Keyboard;
import com.elementars.eclient.command.Command;

public class MacroCommand extends Command
{
    public MacroCommand() {
        super("macro", "Manages macros", new String[] { "add" });
    }
    
    @Override
    public void execute(final String[] args) {
        if (args.length > 1 && args[1].equalsIgnoreCase("help")) {
            this.showSyntax(args[0]);
            return;
        }
        if (args.length < 2) {
            Command.sendChatMessage("Specify an option. Try doing .macro help to see command options");
            return;
        }
        if (args[1].equalsIgnoreCase("add")) {
            if (args.length < 3) {
                Command.sendChatMessage("Please specify a key.");
                return;
            }
            if (args.length < 4) {
                Command.sendChatMessage("Needs more arguments!");
                return;
            }
            try {
                final String isOn = args[2];
                String name = "";
                for (final String arg : args) {
                    if (!arg.equalsIgnoreCase("macro") && !arg.equalsIgnoreCase("add") && !arg.equalsIgnoreCase(isOn)) {
                        name = name + " " + arg;
                    }
                }
                name = name.substring(1);
                final int key = Keyboard.getKeyIndex(isOn.toUpperCase());
                Command.sendChatMessage("Message = " + name + ":Key = " + isOn + ":actual key = " + key);
                if (Keyboard.getKeyName(key) != null) {
                    if (!Xulu.MACRO_MANAGER.getMacros().contains(new Macro(name, key))) {
                        Xulu.MACRO_MANAGER.addMacro(name, key);
                    }
                    Command.sendChatMessage("Added Macro with the key " + Keyboard.getKeyName(key));
                }
            }
            catch (StringIndexOutOfBoundsException e) {
                Command.sendChatMessage("Unknown arguments!");
            }
        }
        else if (args[1].equalsIgnoreCase("del")) {
            if (args.length < 3) {
                Command.sendChatMessage("Please specify a key.");
                return;
            }
            try {
                final int key2 = Keyboard.getKeyIndex(args[2].toUpperCase());
                Xulu.MACRO_MANAGER.delMacro(key2);
                Command.sendChatMessage("Deleted Macro with the key " + args[2].toUpperCase());
            }
            catch (Exception e2) {
                Command.sendChatMessage("Error occured while removing macro!");
            }
        }
        else {
            Command.sendChatMessage("Unknown arguments!");
        }
    }
}
