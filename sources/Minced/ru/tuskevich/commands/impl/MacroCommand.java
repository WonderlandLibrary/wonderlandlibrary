// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.commands.macro.Macro;
import org.lwjgl.input.Keyboard;
import ru.tuskevich.Minced;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "macro", description = "Allows you to bind chat messages")
public class MacroCommand extends CommandAbstract
{
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length > 1) {
            final String s = args[1];
            switch (s) {
                case "add": {
                    final StringBuilder sb = new StringBuilder();
                    for (int i = 3; i < args.length; ++i) {
                        sb.append(args[i]).append(" ");
                    }
                    Minced.getInstance().macroManager.addMacros(new Macro(sb.toString(), Keyboard.getKeyIndex(args[2].toUpperCase())));
                    this.sendMessage("Macro " + ChatFormatting.BLUE + "sucessfully" + ChatFormatting.WHITE + " added");
                    break;
                }
                case "clear": {
                    if (Minced.getInstance().macroManager.getMacros().isEmpty()) {
                        this.sendMessage("Macro not founded");
                        break;
                    }
                    this.sendMessage("Macro " + ChatFormatting.BLUE + "sucessfully" + ChatFormatting.WHITE + " cleared");
                    Minced.getInstance().macroManager.getMacros().clear();
                    Minced.getInstance().macroManager.updateFile();
                    break;
                }
                case "remove": {
                    Minced.getInstance().macroManager.deleteMacro(Keyboard.getKeyIndex(args[2].toUpperCase()));
                    this.sendMessage("Macro " + ChatFormatting.BLUE + "sucessfully" + ChatFormatting.WHITE + " removed");
                    break;
                }
                case "list": {
                    if (Minced.getInstance().macroManager.getMacros().isEmpty()) {
                        this.sendMessage("Macro not founded");
                        break;
                    }
                    this.sendMessage(ChatFormatting.GRAY + "Macro list:");
                    Minced.getInstance().macroManager.getMacros().forEach(macro -> this.sendMessage(ChatFormatting.WHITE + "message: " + ChatFormatting.RED + macro.getMessage() + ChatFormatting.WHITE + ", key: " + ChatFormatting.RED + Keyboard.getKeyName(macro.getKey())));
                    break;
                }
            }
        }
        else {
            this.error();
        }
    }
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.GRAY + "Command use" + ChatFormatting.WHITE + ":");
        this.sendMessage(ChatFormatting.WHITE + ".macro add " + ChatFormatting.BLUE + "<key>" + ChatFormatting.GRAY + " message");
        this.sendMessage(ChatFormatting.WHITE + ".macro remove " + ChatFormatting.BLUE + "<key>");
        this.sendMessage(ChatFormatting.WHITE + ".macro list");
        this.sendMessage(ChatFormatting.WHITE + ".macro clear");
    }
}
