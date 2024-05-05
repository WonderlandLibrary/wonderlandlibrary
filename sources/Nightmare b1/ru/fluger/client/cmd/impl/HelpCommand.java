// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.cmd.impl;

import ru.fluger.client.helpers.misc.ChatHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import ru.fluger.client.cmd.CommandAbstract;

public class HelpCommand extends CommandAbstract
{
    public HelpCommand() {
        super("help", "help", ".help", new String[] { "help" });
    }
    
    @Override
    public void execute(final String... args) {
        if (args.length == 1) {
            if (args[0].equals("help")) {
                ChatHelper.addChatMessage(ChatFormatting.RED + "All Commands:");
                ChatHelper.addChatMessage(ChatFormatting.WHITE + ".bind");
                ChatHelper.addChatMessage(ChatFormatting.WHITE + ".macro");
                ChatHelper.addChatMessage(ChatFormatting.WHITE + ".vclip | .hclip");
                ChatHelper.addChatMessage(ChatFormatting.WHITE + ".friend");
                ChatHelper.addChatMessage(ChatFormatting.WHITE + ".panic");
                ChatHelper.addChatMessage(ChatFormatting.WHITE + ".cfg");
            }
        }
        else {
            ChatHelper.addChatMessage(this.getUsage());
        }
    }
}
