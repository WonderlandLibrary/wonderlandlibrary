/*
 * Decompiled with CFR 0.152.
 */
package ClickGUIs.clickuiutils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public enum ClientUtil {
    INSTANCE;

    private Minecraft mc;
    private String prefix = String.format("%s%s%s %s", (Object)((Object)EnumChatFormatting.GRAY) + "[", (Object)((Object)EnumChatFormatting.WHITE) + "Client", (Object)((Object)EnumChatFormatting.GRAY) + "]", (Object)((Object)EnumChatFormatting.WHITE) + ": ");

    private ClientUtil() {
        this.mc = Minecraft.getMinecraft();
    }

    public void sendMessage(String string, boolean bl) {
        Minecraft.thePlayer.addChatMessage(new ChatComponentText(bl ? String.valueOf(this.prefix) + string : string));
    }

    public void sendMessage(String string) {
        this.sendMessage(string, true);
    }
}

