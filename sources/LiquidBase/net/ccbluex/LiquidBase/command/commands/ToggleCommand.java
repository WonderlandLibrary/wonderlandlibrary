package net.ccbluex.LiquidBase.command.commands;

import net.ccbluex.LiquidBase.command.Command;
import net.ccbluex.LiquidBase.module.Module;
import net.ccbluex.LiquidBase.module.ModuleManager;
import net.ccbluex.LiquidBase.utils.ChatUtils;
import org.lwjgl.input.Keyboard;

/**
 * Copyright © 2015 - 2017 | CCBlueX | All rights reserved.
 * <p>
 * LiquidBase - By CCBlueX(Marco)
 */
public class ToggleCommand extends Command {

    public ToggleCommand() {
        super("t");
    }

    @Override
    public void execute(String[] strings) {
        if(strings.length > 1) {
            final Module module = ModuleManager.getModule(strings[1]);

            if(module == null) {
                ChatUtils.displayMessage("§c§lError: §r§aThe entered module not exist.");
                return;
            }

            module.setState(!module.getState());
            ChatUtils.displayMessage("§cToggled module.");
            return;
        }

        ChatUtils.displayMessage("§2Usage: §b.t <module>");
    }
}
