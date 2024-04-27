package net.ccbluex.liquidbounce.features.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.command.Command;
import net.ccbluex.liquidbounce.features.command.CommandManager;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleManager;
import net.ccbluex.liquidbounce.ui.client.clickgui.ClickGui;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\b\n\n\u0000\n\n\n\b\u000020B¢J02\f\b00H¢\b¨\t"}, d2={"Lnet/ccbluex/liquidbounce/features/command/commands/ReloadCommand;", "Lnet/ccbluex/liquidbounce/features/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "Pride"})
public final class ReloadCommand
extends Command {
    @Override
    public void execute(@NotNull String[] args) {
        Intrinsics.checkParameterIsNotNull(args, "args");
        this.chat("Reloading...");
        this.chat("§c§lReloading commands...");
        LiquidBounce.INSTANCE.setCommandManager(new CommandManager());
        LiquidBounce.INSTANCE.getCommandManager().registerCommands();
        LiquidBounce.INSTANCE.setStarting(true);
        LiquidBounce.INSTANCE.getScriptManager().disableScripts();
        LiquidBounce.INSTANCE.getScriptManager().unloadScripts();
        for (Module module : LiquidBounce.INSTANCE.getModuleManager().getModules()) {
            ModuleManager moduleManager = LiquidBounce.INSTANCE.getModuleManager();
            Module module2 = module;
            Intrinsics.checkExpressionValueIsNotNull(module2, "module");
            moduleManager.generateCommand$Pride(module2);
        }
        this.chat("§c§lReloading scripts...");
        LiquidBounce.INSTANCE.getScriptManager().reloadScripts();
        this.chat("§c§lReloading fonts...");
        Fonts.loadFonts();
        this.chat("§c§lReloading modules...");
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().modulesConfig);
        LiquidBounce.INSTANCE.setStarting(false);
        this.chat("§c§lReloading values...");
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().valuesConfig);
        this.chat("§c§lReloading accounts...");
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().accountsConfig);
        this.chat("§c§lReloading friends...");
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().friendsConfig);
        this.chat("§c§lReloading xray...");
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().xrayConfig);
        this.chat("§c§lReloading HUD...");
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().hudConfig);
        this.chat("§c§lReloading ClickGUI...");
        LiquidBounce.INSTANCE.setClickGui(new ClickGui());
        LiquidBounce.INSTANCE.getFileManager().loadConfig(LiquidBounce.INSTANCE.getFileManager().clickGuiConfig);
        LiquidBounce.INSTANCE.setStarting(false);
        this.chat("Reloaded.");
    }

    public ReloadCommand() {
        super("reload", "configreload");
    }
}
