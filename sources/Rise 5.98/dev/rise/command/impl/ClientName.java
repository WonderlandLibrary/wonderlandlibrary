package dev.rise.command.impl;

import dev.rise.Rise;
import dev.rise.command.Command;
import dev.rise.command.api.CommandInfo;
import dev.rise.util.render.theme.ThemeUtil;

@CommandInfo(name = "ClientName", description = "Sets the custom client name", syntax = ".clientname <name>", aliases = "clientname")
public final class ClientName extends Command {

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        ThemeUtil.setCustomClientName(String.join(" ", args));
        Rise.INSTANCE.getNotificationManager().registerNotification("Successfully set the client name.");
    }
}
