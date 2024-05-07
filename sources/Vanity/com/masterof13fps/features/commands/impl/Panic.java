package com.masterof13fps.features.commands.impl;

import com.masterof13fps.Client;
import com.masterof13fps.features.commands.Command;
import com.masterof13fps.features.modules.Module;

public class Panic extends Command {
    @Override
    public void execute(String[] args) {
        for (Module mod : Client.main().modMgr().getModules()) {
            if (!(mod.name().equalsIgnoreCase("HUD") || mod.name().equalsIgnoreCase("ClickGUI"))) {
                if (mod.state()) {
                    mod.toggle();
                }
            }
        }
    }

    public Panic() {
        super("panic", "pan");
    }
}
