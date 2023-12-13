/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands.impl;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import tk.rektsky.Client;
import tk.rektsky.commands.Command;
import tk.rektsky.commands.impl.HelpCommand;
import tk.rektsky.gui.GuiKeybind;
import tk.rektsky.module.Module;
import tk.rektsky.module.ModulesManager;
import tk.rektsky.utils.obf.wrapper.DisplayGuiScreen;

public class BindCommand
extends Command {
    public BindCommand() {
        super("bind", new String[]{"b"}, "<Module>", "Set keybinds for modules");
    }

    @Override
    public void onCommand(String label, String[] args) {
        if (args.length != 1) {
            HelpCommand.displayCommandInfomation(this);
            return;
        }
        Module module = ModulesManager.getModuleByName(args[0]);
        if (module == null) {
            Client.addClientChat((Object)((Object)ChatFormatting.RED) + "Invalid Module!");
            return;
        }
        DisplayGuiScreen.displayGuiScreen(new GuiKeybind(Minecraft.getMinecraft().currentScreen, module));
    }
}

