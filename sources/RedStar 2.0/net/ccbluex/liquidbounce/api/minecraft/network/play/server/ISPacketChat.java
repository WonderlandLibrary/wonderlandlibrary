package net.ccbluex.liquidbounce.api.minecraft.network.play.server;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\n\u0000\n\n\b\n\n\b\bf\u000020R0X¦¢\bR0X¦¢\bR\b0\tX¦¢\b\n¨\f"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketChat;", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "chatComponent", "Lnet/minecraft/util/text/ITextComponent;", "getChatComponent", "()Lnet/minecraft/util/text/ITextComponent;", "getChat", "getGetChat", "type", "Lnet/minecraft/util/text/ChatType;", "getType", "()Lnet/minecraft/util/text/ChatType;", "Pride"})
public interface ISPacketChat
extends IPacket {
    @NotNull
    public ITextComponent getChatComponent();

    @NotNull
    public ChatType getType();

    @NotNull
    public ITextComponent getGetChat();
}
