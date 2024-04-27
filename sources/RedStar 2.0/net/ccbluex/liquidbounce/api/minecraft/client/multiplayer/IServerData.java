package net.ccbluex.liquidbounce.api.minecraft.client.multiplayer;

import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\n\u0000\n\u0000\n\n\b\n\t\n\b\n\b\n\b\bf\u000020R0X¦¢\bR0X¦¢\b\b\tR\n0X¦¢\bR\f0X¦¢\b\rR0X¦¢\bR0X¦¢\bR0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/multiplayer/IServerData;", "", "gameVersion", "", "getGameVersion", "()Ljava/lang/String;", "pingToServer", "", "getPingToServer", "()J", "populationInfo", "getPopulationInfo", "serverIP", "getServerIP", "serverMOTD", "getServerMOTD", "serverName", "getServerName", "version", "", "getVersion", "()I", "Pride"})
public interface IServerData {
    public long getPingToServer();

    public int getVersion();

    @NotNull
    public String getGameVersion();

    @NotNull
    public String getServerMOTD();

    @NotNull
    public String getPopulationInfo();

    @NotNull
    public String getServerName();

    @NotNull
    public String getServerIP();
}
