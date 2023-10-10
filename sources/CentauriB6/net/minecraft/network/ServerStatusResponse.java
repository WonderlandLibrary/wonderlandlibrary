package net.minecraft.network;

import net.minecraft.network.ServerStatusResponse.MinecraftProtocolVersionIdentifier;
import net.minecraft.network.ServerStatusResponse.PlayerCountData;
import net.minecraft.util.IChatComponent;

public class ServerStatusResponse {
   private IChatComponent serverMotd;
   private PlayerCountData playerCount;
   private MinecraftProtocolVersionIdentifier protocolVersion;
   private String favicon;

   public void setServerDescription(IChatComponent motd) {
      this.serverMotd = motd;
   }

   public void setProtocolVersionInfo(MinecraftProtocolVersionIdentifier protocolVersionData) {
      this.protocolVersion = protocolVersionData;
   }

   public PlayerCountData getPlayerCountData() {
      return this.playerCount;
   }

   public void setPlayerCountData(PlayerCountData countData) {
      this.playerCount = countData;
   }

   public IChatComponent getServerDescription() {
      return this.serverMotd;
   }

   public MinecraftProtocolVersionIdentifier getProtocolVersionInfo() {
      return this.protocolVersion;
   }

   public String getFavicon() {
      return this.favicon;
   }

   public void setFavicon(String faviconBlob) {
      this.favicon = faviconBlob;
   }
}
