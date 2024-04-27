/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  net.minecraft.util.text.ChatType
 *  net.minecraft.util.text.ITextComponent
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.api.minecraft.network.play.server;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005R\u0012\u0010\b\u001a\u00020\tX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\f"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketChat;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "chatComponent", "Lnet/minecraft/util/text/ITextComponent;", "getChatComponent", "()Lnet/minecraft/util/text/ITextComponent;", "getChat", "getGetChat", "type", "Lnet/minecraft/util/text/ChatType;", "getType", "()Lnet/minecraft/util/text/ChatType;", "AtField"})
public interface ISPacketChat
extends IPacket {
    @NotNull
    public ChatType getType();

    @NotNull
    public ITextComponent getChatComponent();

    @NotNull
    public ITextComponent getGetChat();
}

