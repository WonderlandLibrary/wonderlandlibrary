package me.crimp.claudius.mod.command.commands;

import me.crimp.claudius.claudius;
import me.crimp.claudius.mod.command.Command;

import static me.crimp.claudius.claudius.configManager;

public class Svae extends Command {
    public Svae() {
        super("Save", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        configManager.saveConfig(claudius.configManager.config.replaceFirst("claudius/", ""));
        mc.player.sendChatMessage(".save");
        mc.player.sendChatMessage("*save");
        Command.sendMessage("Prolly Saved Ur configs");
    }
}

