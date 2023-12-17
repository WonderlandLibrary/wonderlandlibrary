// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import ru.tuskevich.commands.Command;
import ru.tuskevich.commands.CommandAbstract;

@Command(name = "gps", description = "Shows you the path to the coordinates")
public class GPSCommand extends CommandAbstract
{
    public static int x;
    public static int z;
    public static boolean gps;
    
    @Override
    public void execute(final String[] args) throws Exception {
        if (args.length > 1) {
            if (args[1].equals("off")) {
                GPSCommand.x = 0;
                GPSCommand.z = 0;
                GPSCommand.gps = false;
            }
            else {
                GPSCommand.gps = true;
                GPSCommand.x = Integer.parseInt(args[1]);
                GPSCommand.z = Integer.parseInt(args[2]);
            }
        }
        else {
            this.error();
        }
    }
    
    @Override
    public void error() {
        this.sendMessage(ChatFormatting.GRAY + "Command use" + ChatFormatting.WHITE + ":");
        this.sendMessage(ChatFormatting.WHITE + ".gps " + ChatFormatting.BLUE + "<x, z>");
        this.sendMessage(ChatFormatting.WHITE + ".gps " + ChatFormatting.BLUE + "<off>");
    }
    
    static {
        GPSCommand.gps = false;
    }
}
