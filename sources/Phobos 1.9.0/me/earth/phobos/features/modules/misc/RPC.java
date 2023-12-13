package me.earth.phobos.features.modules.misc;

import me.earth.phobos.DiscordPresence;
import me.earth.phobos.features.modules.Module;
import me.earth.phobos.features.setting.Setting;

public class RPC
        extends Module {
    public static RPC INSTANCE;
    public Setting<Boolean> catMode = this.register(new Setting<Boolean>("CatMode", false));
    public Setting<Boolean> showIP = this.register(new Setting<Boolean>("ShowIP", Boolean.valueOf(true), "Shows the server IP in your discord presence."));
    public Setting<String> state = this.register(new Setting<String>("State", "Phobos 1.9.0", "Sets the state of the DiscordRPC."));

    public RPC() {
        super("RPC", "Discord rich presence", Module.Category.MISC, false, false, false);
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        DiscordPresence.start();
    }

    @Override
    public void onDisable() {
        DiscordPresence.stop();
    }
}

