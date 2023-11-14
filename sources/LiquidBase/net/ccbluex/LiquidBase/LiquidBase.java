package net.ccbluex.LiquidBase;

import net.ccbluex.LiquidBase.clickgui.ClickGUI;
import net.ccbluex.LiquidBase.command.CommandManager;
import net.ccbluex.LiquidBase.event.EventManager;
import net.ccbluex.LiquidBase.filesystem.FileManager;
import net.ccbluex.LiquidBase.module.ModuleManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * Project: LiquidBase
 * -----------------------------------------------------------
 * Copyright © 2017 | CCBlueX | All rights reserved.
 */
@SideOnly(Side.CLIENT)
public class LiquidBase {

    public static LiquidBase CLIENT_INSTANCE;

    public static final String CLIENT_NAME = "LiquidBase";
    public static final int CLIENT_VERSION = 1;
    public static final String CLIENT_AUTHOR = "CCBlueX";

    public final Logger LOGGER = LogManager.getLogger(CLIENT_NAME);

    public final ModuleManager moduleManager = new ModuleManager();
    public final EventManager eventManager = new EventManager();
    public final CommandManager commandManager = new CommandManager();
    public final FileManager fileManager = new FileManager();

    public ClickGUI clickGUI;

    public LiquidBase() {
        CLIENT_INSTANCE = this;
    }

    public void startClient() {
        LOGGER.info(String.format("Starting %s b%d by %s", CLIENT_NAME, CLIENT_VERSION, CLIENT_AUTHOR));

        commandManager.registerCommands();
        moduleManager.registerModules();
        clickGUI = new ClickGUI();
        eventManager.registerListener(moduleManager);

        try{
            fileManager.loadModules();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void stopClient() {
        try{
            fileManager.saveModules();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
}