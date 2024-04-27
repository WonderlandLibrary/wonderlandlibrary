package me.aquavit.liquidsense.command.commands;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.command.Command;
import me.aquavit.liquidsense.ui.client.clickgui.neverlose.Main;
import me.aquavit.liquidsense.ui.font.Fonts;

public class ReloadCommand extends Command
{
    public ReloadCommand() {
        super("reload", "configreload");
    }

    @Override
    public void execute(final String[] args) {
        chat("Reloading...");
        chat("§c§lReloading scripts...");
        LiquidSense.scriptManager.reloadScripts();
        chat("§c§lReloading fonts...");
        Fonts.loadFonts();
        chat("§c§lReloading modules...");
        LiquidSense.fileManager.loadConfig(LiquidSense.fileManager.modulesConfig);
        chat("§c§lReloading values...");
        LiquidSense.fileManager.loadConfig(LiquidSense.fileManager.valuesConfig);
        chat("§c§lReloading accounts...");
        LiquidSense.fileManager.loadConfig(LiquidSense.fileManager.accountsConfig);
        chat("§c§lReloading friends...");
        LiquidSense.fileManager.loadConfig(LiquidSense.fileManager.friendsConfig);
        chat("§c§lReloading xray...");
        LiquidSense.fileManager.loadConfig(LiquidSense.fileManager.xrayConfig);
        chat("§c§lReloading customHud...");
        LiquidSense.fileManager.loadConfig(LiquidSense.fileManager.hudConfig);
        chat("§c§lReloading hud...");
        LiquidSense.fileManager.loadConfig(LiquidSense.fileManager.clickGuiConfig);
        chat("§c§lReloading shortcuts...");
        LiquidSense.fileManager.loadConfig(LiquidSense.fileManager.shortcutsConfig);
        chat("§c§lReloading setName...");
        LiquidSense.fileManager.loadConfig(LiquidSense.fileManager.setNameConfig);
        chat("§c§lReloading clickGUI...");
        LiquidSense.neverlose= new Main();
        chat("Reloaded.");
    }
}
