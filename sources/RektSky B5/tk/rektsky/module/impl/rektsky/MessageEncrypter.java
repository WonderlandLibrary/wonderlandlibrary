/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.rektsky;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.util.Base64;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import tk.rektsky.Client;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.PacketReceiveEvent;
import tk.rektsky.event.impl.PacketSentEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class MessageEncrypter
extends Module {
    private static final String ENCRYPTED_PREFIX = "[REDEENCRYPTED]";

    public MessageEncrypter() {
        super("MessageEncrypter", "Encrypt your message and decrypt meessages from other player so admin can't ban you for talking about illegal topics", Category.REKTSKY, false);
    }

    @Override
    public void onEvent(Event event) {
        try {
            Packet<?> p2;
            if (event instanceof PacketSentEvent && ((PacketSentEvent)event).getPacket() instanceof C01PacketChatMessage) {
                C01PacketChatMessage packet = (C01PacketChatMessage)((PacketSentEvent)event).getPacket();
                String message = ENCRYPTED_PREFIX;
                if (!packet.getMessage().startsWith("/p ") && (packet.getMessage().startsWith("/") || packet.getMessage().startsWith("."))) {
                    return;
                }
                if (packet.getMessage().startsWith("/") && !packet.getMessage().startsWith("/p ")) {
                    return;
                }
                message = packet.getMessage().startsWith("/p ") ? "/p [REDEENCRYPTED]" + Base64.getEncoder().encodeToString(packet.getMessage().replaceFirst("/p ", "").getBytes()) : message + Base64.getEncoder().encodeToString(packet.getMessage().getBytes());
                if (message.length() > 100) {
                    Client.addClientChat("[RektSky message encryptor] Could not encrypt message because the encrypted message is too long for Chat Limit(100 chars)! If you still want to send the message, please disable this module or shorten your message!");
                    return;
                }
                packet.message = message;
            }
            if (event instanceof PacketReceiveEvent && (p2 = ((PacketReceiveEvent)event).getPacket()) instanceof S02PacketChat) {
                S02PacketChat packet = (S02PacketChat)p2;
                if (!packet.isChat()) {
                    return;
                }
                if (packet.getChatComponent().getFormattedText().contains(ENCRYPTED_PREFIX)) {
                    int index = packet.getChatComponent().getUnformattedText().indexOf(ENCRYPTED_PREFIX);
                    String message = packet.getChatComponent().getUnformattedText().substring(index + ENCRYPTED_PREFIX.length());
                    try {
                        index = packet.getChatComponent().getFormattedText().indexOf(ENCRYPTED_PREFIX);
                        packet.chatComponent = new ChatComponentText(packet.getChatComponent().getFormattedText().substring(0, index) + (Object)((Object)ChatFormatting.GREEN) + "[DECRYPTED] " + (Object)((Object)ChatFormatting.GOLD) + new String(Base64.getDecoder().decode(message)));
                    }
                    catch (Exception exception) {}
                }
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}

