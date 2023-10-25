/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package me.Tengoku.Terror.command.impl;

import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.command.Command;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class VClip
extends Command {
    @Override
    public void onCommand(String[] stringArray, String string) {
        if (stringArray.length > 0) {
            int n = Keyboard.getKeyIndex((String)stringArray[0].toUpperCase());
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            double d = Minecraft.thePlayer.posX;
            Minecraft.getMinecraft();
            double d2 = Minecraft.thePlayer.posY + (double)n;
            Minecraft.getMinecraft();
            Minecraft.thePlayer.setPosition(d, d2, Minecraft.thePlayer.posZ);
        } else {
            Exodus.addChatMessage("Syntax error!");
        }
    }

    public VClip() {
        super("VClip", "Its vclip.", "VClip", "vclip");
    }
}

