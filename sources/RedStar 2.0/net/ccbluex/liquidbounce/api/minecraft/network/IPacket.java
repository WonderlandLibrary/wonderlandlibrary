package net.ccbluex.liquidbounce.api.minecraft.network;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.handshake.client.ICPacketHandshake;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketChatMessage;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCustomPayload;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketHeldItemChange;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayer;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketPlayerDigging;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketUseEntity;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ISPacketCloseWindow;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketAnimation;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketChat;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntity;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketEntityVelocity;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketPosLook;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketResourcePackSend;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketTabComplete;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketWindowItems;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000j\n\n\u0000\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\bf\u000020J\b0H&J\b0H&J\b0H&J\b\b0\tH&J\b\n0H&J\b\f0\rH&J\b0H&J\b0H&J\b0H&J\b0H&J\b0H&J\b0H&J\b0H&J\b0H&J\b0H&J\b 0!H&¨\""}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "", "asCPacketChatMessage", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketChatMessage;", "asCPacketCustomPayload", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketCustomPayload;", "asCPacketHandshake", "Lnet/ccbluex/liquidbounce/api/minecraft/network/handshake/client/ICPacketHandshake;", "asCPacketHeldItemChange", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketHeldItemChange;", "asCPacketPlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayer;", "asCPacketPlayerDigging", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging;", "asCPacketUseEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity;", "asSPacketAnimation", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketAnimation;", "asSPacketChat", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketChat;", "asSPacketCloseWindow", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ISPacketCloseWindow;", "asSPacketEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketEntity;", "asSPacketEntityVelocity", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketEntityVelocity;", "asSPacketPosLook", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketPosLook;", "asSPacketResourcePackSend", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketResourcePackSend;", "asSPacketTabComplete", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketTabComplete;", "asSPacketWindowItems", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketWindowItems;", "Pride"})
public interface IPacket {
    @NotNull
    public ISPacketChat asSPacketChat();

    @NotNull
    public ISPacketAnimation asSPacketAnimation();

    @NotNull
    public ISPacketEntity asSPacketEntity();

    @NotNull
    public ICPacketPlayer asCPacketPlayer();

    @NotNull
    public ICPacketUseEntity asCPacketUseEntity();

    @NotNull
    public ISPacketEntityVelocity asSPacketEntityVelocity();

    @NotNull
    public ICPacketChatMessage asCPacketChatMessage();

    @NotNull
    public ISPacketCloseWindow asSPacketCloseWindow();

    @NotNull
    public ISPacketTabComplete asSPacketTabComplete();

    @NotNull
    public ISPacketPosLook asSPacketPosLook();

    @NotNull
    public ISPacketResourcePackSend asSPacketResourcePackSend();

    @NotNull
    public ICPacketHeldItemChange asCPacketHeldItemChange();

    @NotNull
    public ISPacketWindowItems asSPacketWindowItems();

    @NotNull
    public ICPacketCustomPayload asCPacketCustomPayload();

    @NotNull
    public ICPacketHandshake asCPacketHandshake();

    @NotNull
    public ICPacketPlayerDigging asCPacketPlayerDigging();
}
