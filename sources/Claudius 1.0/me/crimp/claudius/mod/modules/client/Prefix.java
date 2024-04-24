package me.crimp.claudius.mod.modules.client;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.modules.Module;
import me.crimp.claudius.mod.setting.Setting;

public class Prefix extends Module {
    public Setting<String> prefix = register(new Setting("Prefix", "!"));
    public Prefix() {super("prefix", "", Module.Category.Client,  true, true);}
    public static Prefix INSTANCE = new Prefix();

    public void SetPrefix() {
        claudius.commandManager.setPrefix(prefix.getPlannedValue());
    }

    @Override
    public void onLoad() {
        claudius.commandManager.setPrefix(prefix.getValue());
    }
}

// may seem dumb to have but is needed ;)