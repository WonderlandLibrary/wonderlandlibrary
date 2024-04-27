/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.features.command.commands;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.command.CommandManager;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.font.Fonts;

public final class ReloadCommand
extends Command {
    @Override
    public void execute(String[] args) {
        this.chat("Reloading...");
        this.chat("\u00a7c\u00a7lReloading commands...");
        LiquidBounce.INSTANCE.setCommandManager(new CommandManager());
        LiquidBounce.INSTANCE.getCommandManager().registerCommands();
        LiquidBounce.INSTANCE.setStarting(true);
        LiquidBounce.INSTANCE.getScriptManager().disableScripts();
        LiquidBounce.INSTANCE.getScriptManager().unloadScripts();
        for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
            LiquidBounce.INSTANCE.getModuleManager().generateCommand$LiquidSense(module);
        }
        this.chat("\u00a7c\u00a7lReloading scripts...");
        LiquidBounce.INSTANCE.getScriptManager().loadScripts();
        LiquidBounce.INSTANCE.getScriptManager().enableScripts();
        this.chat("\u00a7c\u00a7lReloading fonts...");
        Fonts.loadFonts();
        this.chat("\u00a7c\u00a7lReloading modules...");
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().modulesConfig);
        LiquidBounce.INSTANCE.setStarting(false);
        this.chat("\u00a7c\u00a7lReloading values...");
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().valuesConfig);
        this.chat("\u00a7c\u00a7lReloading accounts...");
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().accountsConfig);
        this.chat("\u00a7c\u00a7lReloading friends...");
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().friendsConfig);
        this.chat("\u00a7c\u00a7lReloading xray...");
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().xrayConfig);
        this.chat("\u00a7c\u00a7lReloading HUD...");
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().hudConfig);
        this.chat("\u00a7c\u00a7lReloading ClickGUI...");
        LiquidBounce.INSTANCE.setClickGui(new ClickGui());
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().clickGuiConfig);
        this.chat("Reloaded.");
    }

    public ReloadCommand() {
        super("reload", "configreload");
    }
}

