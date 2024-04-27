/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.api.minecraft.client.network;

import java.util.Collection;
import java.util.UUID;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.INetworkManager;
import net.ccbluex.liquidbounce.api.minecraft.client.network.INetworkPlayerInfo;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH&J\u0012\u0010\u000f\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0010\u001a\u00020\u0011H&R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0018\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/network/IINetHandlerPlayClient;", "", "networkManager", "Lnet/ccbluex/liquidbounce/api/minecraft/INetworkManager;", "getNetworkManager", "()Lnet/ccbluex/liquidbounce/api/minecraft/INetworkManager;", "playerInfoMap", "", "Lnet/ccbluex/liquidbounce/api/minecraft/client/network/INetworkPlayerInfo;", "getPlayerInfoMap", "()Ljava/util/Collection;", "addToSendQueue", "", "classProviderCPacketHeldItemChange", "Lnet/ccbluex/liquidbounce/api/minecraft/network/IPacket;", "getPlayerInfo", "uuid", "Ljava/util/UUID;", "AtField"})
public interface IINetHandlerPlayClient {
    @NotNull
    public INetworkManager getNetworkManager();

    public void addToSendQueue(@NotNull IPacket var1);

    @NotNull
    public Collection getPlayerInfoMap();

    @Nullable
    public INetworkPlayerInfo getPlayerInfo(@NotNull UUID var1);
}

