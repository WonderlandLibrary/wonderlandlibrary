package net.ccbluex.liquidbounce.features.module.modules.misc;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.util.IIChatComponent;
import net.ccbluex.liquidbounce.chat.Client;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientErrorPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientMessagePacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientNewJWTPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientPrivateMessagePacket;
import net.ccbluex.liquidbounce.chat.packet.packets.ClientSuccessPacket;
import net.ccbluex.liquidbounce.chat.packet.packets.Packet;
import net.ccbluex.liquidbounce.features.module.modules.misc.LiquidChat;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000)\n\u0000\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\u0000*\u0000\b\n\u000020J\b0HJ\b0HJ\b0HJ020\bHJ\t02\n0HJ\b\f0HJ\r020H¨"}, d2={"net/ccbluex/liquidbounce/features/module/modules/misc/LiquidChat$client$1", "Lnet/ccbluex/liquidbounce/chat/Client;", "onConnect", "", "onConnected", "onDisconnect", "onError", "cause", "", "onHandshake", "success", "", "onLogon", "onPacket", "packet", "Lnet/ccbluex/liquidbounce/chat/packet/packets/Packet;", "Pride"})
public static final class LiquidChat$client$1
extends Client {
    final LiquidChat this$0;

    @Override
    public void onConnect() {
        ClientUtils.displayChatMessage("§7[§a§lChat§7] §9Connecting to chat server...");
    }

    @Override
    public void onConnected() {
        ClientUtils.displayChatMessage("§7[§a§lChat§7] §9Connected to chat server!");
    }

    @Override
    public void onHandshake(boolean success) {
    }

    @Override
    public void onDisconnect() {
        ClientUtils.displayChatMessage("§7[§a§lChat§7] §cDisconnected from chat server!");
    }

    @Override
    public void onLogon() {
        ClientUtils.displayChatMessage("§7[§a§lChat§7] §9Logging in...");
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onPacket(@NotNull Packet packet) {
        Intrinsics.checkParameterIsNotNull(packet, "packet");
        Packet packet2 = packet;
        if (packet2 instanceof ClientMessagePacket) {
            IEntityPlayerSP thePlayer = MinecraftInstance.mc.getThePlayer();
            if (thePlayer == null) {
                ClientUtils.getLogger().info("[LiquidChat] " + ((ClientMessagePacket)packet).getUser().getName() + ": " + ((ClientMessagePacket)packet).getContent());
                return;
            }
            IIChatComponent chatComponent = MinecraftInstance.classProvider.createChatComponentText("§7[§a§lChat§7] §9" + ((ClientMessagePacket)packet).getUser().getName() + ": ");
            IIChatComponent messageComponent = this.this$0.toChatComponent(((ClientMessagePacket)packet).getContent());
            chatComponent.appendSibling(messageComponent);
            thePlayer.addChatMessage(chatComponent);
            return;
        }
        if (packet2 instanceof ClientPrivateMessagePacket) {
            ClientUtils.displayChatMessage("§7[§a§lChat§7] §c(P)§9 " + ((ClientPrivateMessagePacket)packet).getUser().getName() + ": §7" + ((ClientPrivateMessagePacket)packet).getContent());
            return;
        }
        if (packet2 instanceof ClientErrorPacket) {
            String string;
            switch (((ClientErrorPacket)packet).getMessage()) {
                case "NotSupported": {
                    string = "This method is not supported!";
                    break;
                }
                case "LoginFailed": {
                    string = "Login Failed!";
                    break;
                }
                case "NotLoggedIn": {
                    string = "You must be logged in to use the chat! Enable LiquidChat.";
                    break;
                }
                case "AlreadyLoggedIn": {
                    string = "You are already logged in!";
                    break;
                }
                case "MojangRequestMissing": {
                    string = "Mojang request missing!";
                    break;
                }
                case "NotPermitted": {
                    string = "You are missing the required permissions!";
                    break;
                }
                case "NotBanned": {
                    string = "You are not banned!";
                    break;
                }
                case "Banned": {
                    string = "You are banned!";
                    break;
                }
                case "RateLimited": {
                    string = "You have been rate limited. Please try again later.";
                    break;
                }
                case "PrivateMessageNotAccepted": {
                    string = "Private message not accepted!";
                    break;
                }
                case "EmptyMessage": {
                    string = "You are trying to send an empty message!";
                    break;
                }
                case "MessageTooLong": {
                    string = "Message is too long!";
                    break;
                }
                case "InvalidCharacter": {
                    string = "Message contains a non-ASCII character!";
                    break;
                }
                case "InvalidId": {
                    string = "The given ID is invalid!";
                    break;
                }
                case "Internal": {
                    string = "An internal server error occurred!";
                    break;
                }
                default: {
                    string = ((ClientErrorPacket)packet).getMessage();
                }
            }
            String message = string;
            ClientUtils.displayChatMessage("§7[§a§lChat§7] §cError: §7" + message);
            return;
        }
        if (!(packet2 instanceof ClientSuccessPacket)) {
            if (!(packet2 instanceof ClientNewJWTPacket)) return;
            LiquidChat.Companion.setJwtToken(((ClientNewJWTPacket)packet).getToken());
            this.this$0.getJwtValue().set(true);
            this.this$0.setState(false);
            this.this$0.setState(true);
            return;
        }
        String string = ((ClientSuccessPacket)packet).getReason();
        int n = -1;
        switch (string.hashCode()) {
            case 81873590: {
                if (!string.equals("Unban")) return;
                n = 1;
                break;
            }
            case 73596745: {
                if (!string.equals("Login")) return;
                n = 2;
                break;
            }
            case 66543: {
                if (!string.equals("Ban")) return;
                n = 3;
            }
        }
        switch (n) {
            case 2: {
                ClientUtils.displayChatMessage("§7[§a§lChat§7] §9Logged in!");
                ClientUtils.displayChatMessage("====================================");
                ClientUtils.displayChatMessage("§c>> §lLiquidChat");
                ClientUtils.displayChatMessage("§7Write message: §a.chat <message>");
                ClientUtils.displayChatMessage("§7Write private message: §a.pchat <user> <message>");
                ClientUtils.displayChatMessage("====================================");
                this.setLoggedIn(true);
                return;
            }
            case 3: {
                ClientUtils.displayChatMessage("§7[§a§lChat§7] §9Successfully banned user!");
                return;
            }
            case 1: {
                ClientUtils.displayChatMessage("§7[§a§lChat§7] §9Successfully unbanned user!");
                return;
            }
        }
    }

    @Override
    public void onError(@NotNull Throwable cause) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        ClientUtils.displayChatMessage("§7[§a§lChat§7] §c§lError: §7" + cause.getClass().getName() + ": " + cause.getMessage());
    }

    LiquidChat$client$1(LiquidChat $outer) {
        this.this$0 = $outer;
    }
}
