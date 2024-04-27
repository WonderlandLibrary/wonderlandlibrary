package net.ccbluex.liquidbounce.api.minecraft.client.network;

import java.util.Collection;
import java.util.UUID;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.INetworkManager;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\n\u0000\n\u0000\n\n\b\n\n\n\b\n\n\u0000\n\n\b\n\n\u0000\bf\u000020J0\f2\r0H&J0\b20H&R0X¦¢\bR\b0\b0X¦¢\b\t\n¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "", "networkManager", "Lnet/ccbluex/liquidbounce/api/minecraft/INetworkManager;", "getNetworkManager", "()Lnet/ccbluex/liquidbounce/api/minecraft/INetworkManager;", "playerInfoMap", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/network/INetworkPlayerInfo;", "getPlayerInfoMap", "()Ljava/util/Collection;", "addToSendQueue", "", "classProviderCPacketHeldItemChange", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "getPlayerInfo", "uuid", "Ljava/util/UUID;", "Pride"})
public interface IINetHandlerPlayClient {
    @NotNull
    public INetworkManager getNetworkManager();

    @NotNull
    public Collection<INetworkPlayerInfo> getPlayerInfoMap();

    @Nullable
    public INetworkPlayerInfo getPlayerInfo(@NotNull UUID var1);

    public void addToSendQueue(@NotNull IPacket var1);
}
