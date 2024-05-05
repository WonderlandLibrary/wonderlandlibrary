package net.minecraft.network;

import net.minecraft.network.ServerStatusResponse;
import net.minecraft.util.IChatComponent;

public class ServerStatusResponse {
    private IChatComponent serverMotd;
    private PlayerCountData playerCount;
    private MinecraftProtocolVersionIdentifier protocolVersion;
    private String favicon;

    public IChatComponent getServerDescription() {
        return this.serverMotd;
    }

    public void setServerDescription(IChatComponent motd) {
        this.serverMotd = motd;
    }

    public PlayerCountData getPlayerCountData() {
        return this.playerCount;
    }

    public void setPlayerCountData(PlayerCountData countData) {
        this.playerCount = countData;
    }

    public MinecraftProtocolVersionIdentifier getProtocolVersionInfo() {
        return this.protocolVersion;
    }

    public void setProtocolVersionInfo(MinecraftProtocolVersionIdentifier protocolVersionData) {
        this.protocolVersion = protocolVersionData;
    }

    public void setFavicon(String faviconBlob) {
        this.favicon = faviconBlob;
    }

    public String getFavicon() {
        return this.favicon;
    }
}
