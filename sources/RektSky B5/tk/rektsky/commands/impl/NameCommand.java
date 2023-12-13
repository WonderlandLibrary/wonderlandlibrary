/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.commands.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import tk.rektsky.commands.Command;

public class NameCommand
extends Command {
    public NameCommand() {
        super("name", "", "Copy your name");
    }

    @Override
    public void onCommand(String label, String[] args) {
        Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiChat(this.mc.session.getUsername())));
    }
}

