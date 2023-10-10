package net.minecraft.client.multiplayer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class ServerData {
   public boolean field_78841_f;
   public String gameVersion = "1.8.8";
   public int version = 47;
   public String serverMOTD;
   public String populationInfo;
   private ServerData.ServerResourceMode resourceMode;
   private boolean field_181042_l;
   public long pingToServer;
   public String playerList;
   public String serverName;
   public String serverIP;
   private String serverIcon;

   public String getBase64EncodedIconData() {
      return this.serverIcon;
   }

   public void copyFrom(ServerData var1) {
      this.serverIP = var1.serverIP;
      this.serverName = var1.serverName;
      this.setResourceMode(var1.getResourceMode());
      this.serverIcon = var1.serverIcon;
      this.field_181042_l = var1.field_181042_l;
   }

   public void setResourceMode(ServerData.ServerResourceMode var1) {
      this.resourceMode = var1;
   }

   public ServerData.ServerResourceMode getResourceMode() {
      return this.resourceMode;
   }

   public boolean func_181041_d() {
      return this.field_181042_l;
   }

   public NBTTagCompound getNBTCompound() {
      NBTTagCompound var1 = new NBTTagCompound();
      var1.setString("name", this.serverName);
      var1.setString("ip", this.serverIP);
      if (this.serverIcon != null) {
         var1.setString("icon", this.serverIcon);
      }

      if (this.resourceMode == ServerData.ServerResourceMode.ENABLED) {
         var1.setBoolean("acceptTextures", true);
      } else if (this.resourceMode == ServerData.ServerResourceMode.DISABLED) {
         var1.setBoolean("acceptTextures", false);
      }

      return var1;
   }

   public void setBase64EncodedIconData(String var1) {
      this.serverIcon = var1;
   }

   public static ServerData getServerDataFromNBTCompound(NBTTagCompound var0) {
      ServerData var1 = new ServerData(var0.getString("name"), var0.getString("ip"), false);
      if (var0.hasKey("icon", 8)) {
         var1.setBase64EncodedIconData(var0.getString("icon"));
      }

      if (var0.hasKey("acceptTextures", 1)) {
         if (var0.getBoolean("acceptTextures")) {
            var1.setResourceMode(ServerData.ServerResourceMode.ENABLED);
         } else {
            var1.setResourceMode(ServerData.ServerResourceMode.DISABLED);
         }
      } else {
         var1.setResourceMode(ServerData.ServerResourceMode.PROMPT);
      }

      return var1;
   }

   public ServerData(String var1, String var2, boolean var3) {
      this.resourceMode = ServerData.ServerResourceMode.PROMPT;
      this.serverName = var1;
      this.serverIP = var2;
      this.field_181042_l = var3;
   }

   public static enum ServerResourceMode {
      DISABLED("disabled");

      private static final ServerData.ServerResourceMode[] ENUM$VALUES = new ServerData.ServerResourceMode[]{ENABLED, DISABLED, PROMPT};
      private final IChatComponent motd;
      ENABLED("enabled"),
      PROMPT("prompt");

      public IChatComponent getMotd() {
         return this.motd;
      }

      private ServerResourceMode(String var3) {
         this.motd = new ChatComponentTranslation("addServer.resourcePack." + var3, new Object[0]);
      }
   }
}
