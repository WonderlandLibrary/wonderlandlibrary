// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "calc", description = "Allows you to calculate")
public class CalcCommand extends CommandAbstract
{
    @Override
    public void execute(final String[] args) {
        if (args.length >= 3) {
            try {
                if (args[2].equals("*")) {
                    this.sendMessage("Result " + ChatFormatting.BLUE + Double.parseDouble(args[1]) * Double.parseDouble(args[3]));
                }
                if (args[2].equals("/")) {
                    this.sendMessage("Result " + ChatFormatting.BLUE + Double.parseDouble(args[1]) / Double.parseDouble(args[3]));
                }
                if (args[2].equals("-")) {
                    this.sendMessage("Result " + ChatFormatting.BLUE + (Double.parseDouble(args[1]) - Double.parseDouble(args[3])));
                }
                if (args[2].equals("+")) {
                    this.sendMessage("Result " + ChatFormatting.BLUE + (Double.parseDouble(args[1]) + Double.parseDouble(args[3])));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (args.length <= 2) {
            this.error();
        }
    }
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.GRAY + "Command use" + ChatFormatting.WHITE + ":");
        this.sendMessage(ChatFormatting.WHITE + ".calc " + ChatFormatting.BLUE + "<number> <+, -, *, /> <number>");
    }
}
