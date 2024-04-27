package net.ccbluex.liquidbounce.api.minecraft.network.play.client;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\b\bf\u000020R0X¦¢\f\b\"\b¨\b"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/client/ICPacketChatMessage;", "", "message", "", "getMessage", "()Ljava/lang/String;", "setMessage", "(Ljava/lang/String;)V", "Pride"})
public interface ICPacketChatMessage {
    @NotNull
    public String getMessage();

    public void setMessage(@NotNull String var1);
}
