/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.play.client.C01PacketChatMessage
 */
package net.ccbluex.liquidbounce.features.module.modules.fun;

import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.minecraft.network.play.client.C01PacketChatMessage;

@ModuleInfo(name="ForceUnicodeChat", description="Allows you to send unicode messages in chat.", category=ModuleCategory.FUN)
public class ForceUnicodeChat
extends Module {
    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C01PacketChatMessage) {
            C01PacketChatMessage chatMessage = (C01PacketChatMessage)event.getPacket();
            String message = chatMessage.func_149439_c();
            StringBuilder stringBuilder = new StringBuilder();
            for (char c : message.toCharArray()) {
                if (c >= '!' && c <= '\u0080') {
                    stringBuilder.append(Character.toChars(c + 65248));
                    continue;
                }
                stringBuilder.append(c);
            }
            chatMessage.field_149440_a = stringBuilder.toString();
        }
    }
}

