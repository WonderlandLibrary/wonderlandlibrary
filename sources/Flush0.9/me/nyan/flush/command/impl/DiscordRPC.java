package me.nyan.flush.command.impl;

import me.nyan.flush.command.Command;
import me.nyan.flush.ui.discord.DiscordRP;
import me.nyan.flush.utils.other.ChatUtils;

public class DiscordRPC extends Command {
    private final DiscordRP discordRP = flush.getDiscordRP();

    public DiscordRPC() {
        super("DiscordRPC", "Disables or enables discord rpc", "DiscordRPC on | off", "discordrp", "drpc");
    }

    @Override
    public void onCommand(String[] args, String message) {
        if (args.length == 0) {
            ChatUtils.println("§aDiscord RPC is currently " + (discordRP.isRunning() ? "enabled" : "disabled") + ".");
            sendSyntaxHelpMessage();
            return;
        }

        switch (args[0].toLowerCase()) {
            case "on":
            case "enable":
            case "true":
                if (!discordRP.isRunning()) {
                    discordRP.start();
                }
                ChatUtils.println("§aEnabled Discord RPC.");
                break;

            case "off":
            case "disable":
            case "false":
                if (discordRP.isRunning()) {
                    discordRP.shutdown();
                }
                ChatUtils.println("§aDisabled Discord RPC.");
                break;
        }
    }
}