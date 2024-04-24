package me.crimp.claudius.mod.modules.client;

import me.crimp.claudius.DiscordPresence;
import me.crimp.claudius.mod.command.Command;
import me.crimp.claudius.mod.modules.Module;

public class RPC
        extends Module {
    public static RPC INSTANCE;
    //public final Setting<Boolean> IP = register(new Setting<>("id",true));

    public RPC() {
        super("DiscordRichP", "Discord rich presence", Module.Category.Client, false, false);
        this.enabled.setValue(true);
        this.drawn.setValue(false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        DiscordPresence.start();
        Command.sendOverwriteClientMessage("Discord Rpc Started");
    }

    @Override
    public void onDisable() {
        DiscordPresence.stop();
        Command.sendOverwriteClientMessage("Discord Rpc Stopped");
    }
}

