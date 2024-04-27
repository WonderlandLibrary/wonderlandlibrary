/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.dev.important.modules.command.commands;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.Client;
import net.dev.important.gui.client.clickgui.ClickGui;
import net.dev.important.gui.font.Fonts;
import net.dev.important.modules.command.Command;
import net.dev.important.modules.command.CommandManager;
import net.dev.important.modules.module.Manager;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.misc.sound.TipSoundManager;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001b\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H\u0016\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2={"Lnet/dev/important/modules/command/commands/ReloadCommand;", "Lnet/dev/important/modules/command/Command;", "()V", "execute", "", "args", "", "", "([Ljava/lang/String;)V", "LiquidBounce"})
public final class ReloadCommand
extends Command {
    public ReloadCommand() {
        String[] stringArray = new String[]{"configreload"};
        super("reload", stringArray);
    }

    @Override
    public void execute(@NotNull String[] args2) {
        Intrinsics.checkNotNullParameter(args2, "args");
        this.chat("Reloading...");
        this.chat("\u00a7c\u00a7lReloading commands...");
        Client.INSTANCE.setCommandManager(new CommandManager());
        Client.INSTANCE.getCommandManager().registerCommands();
        Client.INSTANCE.setStarting(true);
        Client.INSTANCE.getScriptManager().disableScripts();
        Client.INSTANCE.getScriptManager().unloadScripts();
        for (Module module2 : Client.INSTANCE.getModuleManager().getModules()) {
            Manager manager = Client.INSTANCE.getModuleManager();
            Intrinsics.checkNotNullExpressionValue(module2, "module");
            manager.generateCommand$LiquidBounce(module2);
        }
        this.chat("\u00a7c\u00a7lReloading scripts...");
        Client.INSTANCE.getScriptManager().loadScripts();
        Client.INSTANCE.getScriptManager().enableScripts();
        this.chat("\u00a7c\u00a7lReloading fonts...");
        Fonts.loadFonts();
        this.chat("\u00a7c\u00a7lReloading toggle audio files...");
        Client.INSTANCE.setTipSoundManager(new TipSoundManager());
        this.chat("\u00a7c\u00a7lReloading modules...");
        Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().modulesConfig);
        Client.INSTANCE.setStarting(false);
        this.chat("\u00a7c\u00a7lReloading values...");
        Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().valuesConfig);
        this.chat("\u00a7c\u00a7lReloading accounts...");
        Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().accountsConfig);
        this.chat("\u00a7c\u00a7lReloading friends...");
        Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().friendsConfig);
        this.chat("\u00a7c\u00a7lReloading xray...");
        Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().xrayConfig);
        this.chat("\u00a7c\u00a7lReloading HUD...");
        Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().hudConfig);
        this.chat("\u00a7c\u00a7lReloading ClickGUI...");
        Client.INSTANCE.setClickGui(new ClickGui());
        Client.INSTANCE.getFileManager().loadConfig(Client.INSTANCE.getFileManager().clickGuiConfig);
        Client.INSTANCE.setStarting(false);
        this.chat("Reloaded.");
    }
}

