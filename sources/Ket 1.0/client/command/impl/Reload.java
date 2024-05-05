package client.command.impl;

import client.Client;
import client.command.Command;
import client.module.DevelopmentFeature;
import client.util.ChatUtil;

@DevelopmentFeature
public final class Reload extends Command {

    public Reload() {
        super("Reloads the client", "reload", "r");
    }

    @Override
    public void execute(String[] args) {
        Client.INSTANCE.shutdown();
        Client.INSTANCE.init();
        ChatUtil.display("Reloaded " + Client.NAME);
    }
}