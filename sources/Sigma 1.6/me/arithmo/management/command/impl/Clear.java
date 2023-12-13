/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.management.command.impl;

import me.arithmo.event.Event;
import me.arithmo.management.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiNewChat;

public class Clear
extends Command {
    public Clear(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
    }

    @Override
    public String getUsage() {
        return "Clear";
    }

    public void onEvent(Event event) {
    }
}

