package net.ccbluex.LiquidBase.command;

import net.ccbluex.LiquidBase.command.commands.BindCommand;
import net.ccbluex.LiquidBase.command.commands.ToggleCommand;
import net.ccbluex.LiquidBase.command.commands.ValueCommand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
@SideOnly(Side.CLIENT)
public class CommandManager {

    private final List<Command> commands = new ArrayList<>();

    public void registerCommands() {
        registerCommand(new BindCommand());
        registerCommand(new ValueCommand());
        registerCommand(new ToggleCommand());
    }

    public void registerCommand(final Command command) {
        commands.add(command);
    }

    public void callCommand(final String s) {
        final String[] strings = s.split(" ");
        commands.stream().filter(command -> strings[0].equalsIgnoreCase("." + command.getName())).forEach(command -> command.execute(strings));
    }
}