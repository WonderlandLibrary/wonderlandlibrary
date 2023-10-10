package net.minecraft.client.multiplayer;

import net.minecraft.client.multiplayer.ServerData.ServerResourceMode;
import net.minecraft.nbt.NBTTagCompound;

public class ServerData {
   public String serverName;
   public String serverIP;
   public String populationInfo;
   public String serverMOTD;
   public long pingToServer;
   public int version = 47;
   public String gameVersion = "1.8.9";
   public boolean field_78841_f;
   public String playerList;
   private ServerResourceMode resourceMode = ServerResourceMode.PROMPT;
   private String serverIcon;
   private boolean field_181042_l;

   public ServerData(String p_i46420_1_, String p_i46420_2_, boolean p_i46420_3_) {
      this.serverName = p_i46420_1_;
      this.serverIP = p_i46420_2_;
      this.field_181042_l = p_i46420_3_;
   }

   public boolean func_181041_d() {
      return this.field_181042_l;
   }

   public void setResourceMode(ServerResourceMode mode) {
      this.resourceMode = mode;
   }

   public NBTTagCompound getNBTCompound() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      nbttagcompound.setString("name", this.serverName);
      nbttagcompound.setString("ip", this.serverIP);
      if(this.serverIcon != null) {
         nbttagcompound.setString("icon", this.serverIcon);
      }

      if(this.resourceMode == ServerResourceMode.ENABLED) {
         nbttagcompound.setBoolean("acceptTextures", true);
      } else if(this.resourceMode == ServerResourceMode.DISABLED) {
         nbttagcompound.setBoolean("acceptTextures", false);
      }

      return nbttagcompound;
   }

   public ServerResourceMode getResourceMode() {
      return this.resourceMode;
   }

   public void copyFrom(ServerData serverDataIn) {
      this.serverIP = serverDataIn.serverIP;
      this.serverName = serverDataIn.serverName;
      this.setResourceMode(serverDataIn.getResourceMode());
      this.serverIcon = serverDataIn.serverIcon;
      this.field_181042_l = serverDataIn.field_181042_l;
   }

   public void setBase64EncodedIconData(String icon) {
      this.serverIcon = icon;
   }

   public String getBase64EncodedIconData() {
      return this.serverIcon;
   }

   public static ServerData getServerDataFromNBTCompound(NBTTagCompound nbtCompound) {
      ServerData serverdata = new ServerData(nbtCompound.getString("name"), nbtCompound.getString("ip"), false);
      if(nbtCompound.hasKey("icon", 8)) {
         serverdata.setBase64EncodedIconData(nbtCompound.getString("icon"));
      }

      if(nbtCompound.hasKey("acceptTextures", 1)) {
         if(nbtCompound.getBoolean("acceptTextures")) {
            serverdata.setResourceMode(ServerResourceMode.ENABLED);
         } else {
            serverdata.setResourceMode(ServerResourceMode.DISABLED);
         }
      } else {
         serverdata.setResourceMode(ServerResourceMode.PROMPT);
      }

      return serverdata;
   }
}
