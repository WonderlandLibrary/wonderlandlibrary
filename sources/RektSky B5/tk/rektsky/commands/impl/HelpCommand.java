/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.ChatComponentText;
import tk.rektsky.Client;
import tk.rektsky.commands.Command;
import tk.rektsky.commands.CommandsManager;

public class HelpCommand
extends Command {
    public HelpCommand() {
        super("help", "", "Show commands list");
    }

    @Override
    public void onCommand(String label, String[] args) {
        Client.mc.thePlayer.sendMessage(new ChatComponentText((Object)((Object)ChatFormatting.GREEN) + "=============================="));
        Client.mc.thePlayer.sendMessage(new ChatComponentText((Object)((Object)ChatFormatting.GREEN) + " Commands List"));
        for (Command cmd : CommandsManager.COMMANDS) {
            HelpCommand.displayCommandInfomation(cmd);
        }
        Client.mc.thePlayer.sendMessage(new ChatComponentText((Object)((Object)ChatFormatting.GREEN) + "=============================="));
    }

    public static void displayCommandInfomation(Command cmd) {
        Client.mc.thePlayer.sendMessage(new ChatComponentText((Object)((Object)ChatFormatting.YELLOW) + cmd.getName() + " " + cmd.getArgumentDescription() + "  -  " + (Object)((Object)ChatFormatting.GRAY) + cmd.getDescription()));
    }
}

