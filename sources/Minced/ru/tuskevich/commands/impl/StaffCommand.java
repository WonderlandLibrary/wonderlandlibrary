// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import java.util.ArrayList;
import java.util.Iterator;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.List;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "staff", description = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd \ufffd Staff List")
public class StaffCommand extends CommandAbstract
{
    public static List<String> staffNames;
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.GRAY + "\u0414\u043e\u0441\u0442\u0443\u043f\u043d\u044b\u0435 \u043a\u043e\u043c\u0430\u043d\u0434\u044b" + ChatFormatting.WHITE + ":");
        this.sendMessage(ChatFormatting.WHITE + ".staff " + ChatFormatting.GRAY + "<" + ChatFormatting.RED + "add; remove; clear; list." + ChatFormatting.GRAY + ">");
    }
    
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("add")) {
                if (StaffCommand.staffNames.contains(args[2])) {
                    this.sendMessage(ChatFormatting.RED + "\u0442\u0430\u043a\u043e\u0433\u043e \u0430\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0442\u043e\u0440 \u043d\u0435\u0442\u0443 \u0432  Staff List!");
                }
                else {
                    StaffCommand.staffNames.add(args[2]);
                    this.sendMessage(ChatFormatting.GREEN + "\u0410\u0434\u043c\u0438\u043d " + ChatFormatting.WHITE + args[2] + ChatFormatting.GREEN + " \u0434\u043e\u0431\u0430\u0432\u043b\u0435\u043d \u0432 Staff List");
                }
            }
            if (args[1].equalsIgnoreCase("remove")) {
                if (StaffCommand.staffNames.contains(args[2])) {
                    StaffCommand.staffNames.remove(args[2]);
                    this.sendMessage(ChatFormatting.GREEN + "\u0430\u0434\u043c\u0438\u043d " + ChatFormatting.WHITE + args[2] + ChatFormatting.GREEN + " \u0443\u0434\u0430\u043b\u0435\u043d \u0438\u0437 Staff List");
                }
                else {
                    this.sendMessage(ChatFormatting.RED + "\u0442\u0430\u043a\u043e\u0433\u043e \u0430\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0442\u043e\u0440 \u043d\u0435\u0442\u0443 \u0432  Staff List!");
                }
            }
            if (args[1].equalsIgnoreCase("clear")) {
                if (StaffCommand.staffNames.isEmpty()) {
                    this.sendMessage(ChatFormatting.RED + "Staff List \u043f\u0443\u0441\u0442!");
                }
                else {
                    StaffCommand.staffNames.clear();
                    this.sendMessage(ChatFormatting.GREEN + "Staff List \u043e\u0447\u0438\u0449\u0435\u043d!");
                }
            }
            if (args[1].equalsIgnoreCase("list")) {
                this.sendMessage(ChatFormatting.GRAY + "\u0414\u043e\u0431\u0430\u0432\u043b\u0435\u043d\u043d\u044b\u0439 Staff:");
                for (final String name : StaffCommand.staffNames) {
                    this.sendMessage(ChatFormatting.WHITE + name);
                }
            }
        }
        else {
            this.error();
        }
    }
    
    static {
        StaffCommand.staffNames = new ArrayList<String>();
    }
}
