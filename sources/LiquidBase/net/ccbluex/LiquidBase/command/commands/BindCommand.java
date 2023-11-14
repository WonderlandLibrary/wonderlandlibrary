package net.ccbluex.LiquidBase.command.commands;

import net.ccbluex.LiquidBase.command.Command;
import net.ccbluex.LiquidBase.module.Module;
import net.ccbluex.LiquidBase.module.ModuleManager;
import net.ccbluex.LiquidBase.utils.ChatUtils;
import org.lwjgl.input.Keyboard;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
public class BindCommand extends Command {

    public BindCommand() {
        super("bind");
    }

    @Override
    public void execute(String[] strings) {
        if(strings.length > 2) {
            final Module module = ModuleManager.getModule(strings[1]);

            if(module == null) {
                ChatUtils.displayMessage("§c§lError: §r§aThe entered module not exist.");
                return;
            }

            final int key = Keyboard.getKeyIndex(strings[2].toUpperCase());

            module.setKeyBind(key);
            ChatUtils.displayMessage("§cThe keybind of §a§l" + module.getModuleName() + " §r§cwas set to §a§l" + Keyboard.getKeyName(key) + "§c.");
            return;
        }

        ChatUtils.displayMessage("§2Usage: §b.bind <module> <key>");
    }
}