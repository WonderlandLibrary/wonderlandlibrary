package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;

@CommandInfo(name = "PlayerHacks", description = "Sets the custom PlayerHacks target", syntax = ".playerhacks <name>", aliases = "playerhacks")
public final class PlayerHacks extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        dev.rise.module.impl.other.PlayerHacks.master = String.join(" ", args);
        Rise.INSTANCE.getNotificationManager().registerNotification("Successfully set the PlayerHacks target.");
    }
}
