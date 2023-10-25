/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;
import org.celestial.client.helpers.Helper;

public class ChatHelper
implements Helper {
    public static String chatPrefix = "\u00a77[" + (Object)((Object)ChatFormatting.LIGHT_PURPLE) + "C" + (Object)((Object)ChatFormatting.WHITE) + "elestial" + (Object)((Object)ChatFormatting.RESET) + "\u00a77] \u00a78>> \u00a7f";

    public static void addChatMessage(Object message) {
        ChatHelper.mc.player.addChatMessage(new TextComponentString(chatPrefix + message));
    }
}

