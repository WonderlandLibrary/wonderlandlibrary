/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.server.SPacketChat
 *  net.minecraft.util.text.ChatType
 *  net.minecraft.util.text.ITextComponent
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.network.play.server.ISPacketChat;
import net.ccbluex.liquidbounce.injection.backend.PacketImpl;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0014\u0010\u000b\u001a\u00020\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\nR\u0014\u0010\r\u001a\u00020\u000e8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0011"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/SPacketChatImpl;", "T", "Lnet/minecraft/network/play/server/SPacketChat;", "Lnet/ccbluex/liquidbounce/injection/backend/PacketImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketChat;", "wrapped", "(Lnet/minecraft/network/play/server/SPacketChat;)V", "chatComponent", "Lnet/minecraft/util/text/ITextComponent;", "getChatComponent", "()Lnet/minecraft/util/text/ITextComponent;", "getChat", "getGetChat", "type", "Lnet/minecraft/util/text/ChatType;", "getType", "()Lnet/minecraft/util/text/ChatType;", "LiKingSense"})
public final class SPacketChatImpl<T extends SPacketChat>
extends PacketImpl<T>
implements ISPacketChat {
    @Override
    @NotNull
    public ITextComponent getChatComponent() {
        ITextComponent iTextComponent = ((SPacketChat)this.getWrapped()).func_148915_c();
        Intrinsics.checkExpressionValueIsNotNull((Object)iTextComponent, (String)"wrapped.chatComponent");
        return iTextComponent;
    }

    @Override
    @NotNull
    public ChatType getType() {
        ChatType chatType = ((SPacketChat)this.getWrapped()).func_192590_c();
        Intrinsics.checkExpressionValueIsNotNull((Object)chatType, (String)"wrapped.type");
        return chatType;
    }

    @Override
    @NotNull
    public ITextComponent getGetChat() {
        ITextComponent iTextComponent = ((SPacketChat)this.getWrapped()).func_148915_c();
        Intrinsics.checkExpressionValueIsNotNull((Object)iTextComponent, (String)"wrapped.getChatComponent()");
        return iTextComponent;
    }

    public SPacketChatImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((Packet)wrapped);
    }
}

