/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.command.impl;

import me.Tengoku.Terror.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class Say
extends Command {
    public Say() {
        super("Say", "Says something in the chat.", "say (MESSAGE)", "say");
    }

    @Override
    public void onCommand(String[] stringArray, String string) {
        if (stringArray.length > 0) {
            String string2 = stringArray[0];
            Minecraft.getMinecraft();
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(String.join((CharSequence)" ", string2)));
        }
    }
}

