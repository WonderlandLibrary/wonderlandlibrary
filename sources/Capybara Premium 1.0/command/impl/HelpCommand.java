package fun.rich.client.command.impl;


import com.mojang.realmsclient.gui.ChatFormatting;
import fun.rich.client.command.CommandAbstract;
import fun.rich.client.utils.other.ChatUtils;

public class HelpCommand
        extends CommandAbstract {
    public HelpCommand() {
        super("help", "help", ".help", "help");
    }

    @Override
    public void execute(String... args) {
        if (args.length == 1) {
            if (args[0].equals("help")) {
                ChatUtils.addChatMessage(ChatFormatting.RED + "All Commands:");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".bind");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".macro");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".gps");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".parser");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".vclip | .hclip");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".fakename");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".friend");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".cfg");
            }
        } else {
            ChatUtils.addChatMessage(this.getUsage());
        }
    }
}
