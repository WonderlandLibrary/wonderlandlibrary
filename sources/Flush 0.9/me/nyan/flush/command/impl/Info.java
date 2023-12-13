package me.nyan.flush.command.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.command.Command;
import me.nyan.flush.utils.other.ChatUtils;

public class Info extends Command {
    public Info() {
        super("Info", "Shows information about the client.", "info");
    }

    @Override
    public void onCommand(String[] args, String message) {
        ChatUtils.println("§9Client version:§7 " + Flush.VERSION + ".");
        ChatUtils.println("§9Discord server:§7 https://discord.gg/bDWVudB4Nk");
    }
}
