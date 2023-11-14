package net.ccbluex.LiquidBase.command.commands;

import net.ccbluex.LiquidBase.command.Command;
import net.ccbluex.LiquidBase.module.Module;
import net.ccbluex.LiquidBase.module.ModuleManager;
import net.ccbluex.LiquidBase.utils.ChatUtils;
import net.ccbluex.LiquidBase.valuesystem.Value;
import org.lwjgl.input.Keyboard;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
public class ValueCommand extends Command {

    public ValueCommand() {
        super("val");
    }

    @Override
    public void execute(String[] strings) {
        if(strings.length > 3) {
            final Module module = ModuleManager.getModule(strings[1]);

            if(module == null) {
                ChatUtils.displayMessage("§c§lError: §r§aThe entered module not exist.");
                return;
            }

            final Value value = module.getValue(strings[2]);

            if(value == null) {
                ChatUtils.displayMessage("§c§lError: §r§aThe entered value not exist.");
                return;
            }

            if(value.getObject() instanceof Float) {
                final float newValue = Float.parseFloat(strings[3]);
                value.setObject(newValue);
                ChatUtils.displayMessage("§cThe value of §a§l" + module.getModuleName() + " §8(§a§l" + value.getValueName() + ") §c was set to §a§l" + newValue + "§c.");
            }
            return;
        }

        ChatUtils.displayMessage("§2Usage: §b.val <module> <valuename> <new_value>");
    }
}