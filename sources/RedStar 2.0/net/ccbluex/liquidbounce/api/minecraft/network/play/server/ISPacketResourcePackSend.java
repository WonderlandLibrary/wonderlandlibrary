package net.ccbluex.liquidbounce.api.minecraft.network.play.server;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\b\bf\u000020R0X¦¢\bR0X¦¢\b¨\b"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/network/play/server/ISPacketResourcePackSend;", "", "hash", "", "getHash", "()Ljava/lang/String;", "url", "getUrl", "Pride"})
public interface ISPacketResourcePackSend {
    @NotNull
    public String getUrl();

    @NotNull
    public String getHash();
}
