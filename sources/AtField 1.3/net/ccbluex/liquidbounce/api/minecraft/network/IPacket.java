/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 */
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

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&J\b\u0010\u0004\u001a\u00020\u0005H&J\b\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\tH&J\b\u0010\n\u001a\u00020\u000bH&J\b\u0010\f\u001a\u00020\rH&J\b\u0010\u000e\u001a\u00020\u000fH&J\b\u0010\u0010\u001a\u00020\u0011H&J\b\u0010\u0012\u001a\u00020\u0013H&J\b\u0010\u0014\u001a\u00020\u0015H&J\b\u0010\u0016\u001a\u00020\u0017H&J\b\u0010\u0018\u001a\u00020\u0019H&J\b\u0010\u001a\u001a\u00020\u001bH&J\b\u0010\u001c\u001a\u00020\u001dH&J\b\u0010\u001e\u001a\u00020\u001fH&J\b\u0010 \u001a\u00020!H&\u00a8\u0006\""}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "", "asCPacketChatMessage", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketChatMessage;", "asCPacketCustomPayload", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketCustomPayload;", "asCPacketHandshake", "Lnet/ccbluex/liquidbounce/api/minecraft/network/handshake/client/ICPacketHandshake;", "asCPacketHeldItemChange", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketHeldItemChange;", "asCPacketPlayer", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayer;", "asCPacketPlayerDigging", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketPlayerDigging;", "asCPacketUseEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketUseEntity;", "asSPacketAnimation", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketAnimation;", "asSPacketChat", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketChat;", "asSPacketCloseWindow", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ISPacketCloseWindow;", "asSPacketEntity", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketEntity;", "asSPacketEntityVelocity", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketEntityVelocity;", "asSPacketPosLook", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketPosLook;", "asSPacketResourcePackSend", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketResourcePackSend;", "asSPacketTabComplete", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketTabComplete;", "asSPacketWindowItems", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketWindowItems;", "AtField"})
public interface IPacket {
    @NotNull
    public ICPacketPlayer asCPacketPlayer();

    @NotNull
    public ISPacketResourcePackSend asSPacketResourcePackSend();

    @NotNull
    public ISPacketCloseWindow asSPacketCloseWindow();

    @NotNull
    public ICPacketHeldItemChange asCPacketHeldItemChange();

    @NotNull
    public ICPacketHandshake asCPacketHandshake();

    @NotNull
    public ISPacketWindowItems asSPacketWindowItems();

    @NotNull
    public ICPacketCustomPayload asCPacketCustomPayload();

    @NotNull
    public ISPacketEntity asSPacketEntity();

    @NotNull
    public ISPacketTabComplete asSPacketTabComplete();

    @NotNull
    public ICPacketChatMessage asCPacketChatMessage();

    @NotNull
    public ISPacketChat asSPacketChat();

    @NotNull
    public ICPacketPlayerDigging asCPacketPlayerDigging();

    @NotNull
    public ICPacketUseEntity asCPacketUseEntity();

    @NotNull
    public ISPacketEntityVelocity asSPacketEntityVelocity();

    @NotNull
    public ISPacketPosLook asSPacketPosLook();

    @NotNull
    public ISPacketAnimation asSPacketAnimation();
}

