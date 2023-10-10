package net.minecraft.tileentity;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;

public class TileEntitySkull extends TileEntity {
   private int skullRotation;
   private int skullType;
   private GameProfile playerProfile = null;

   public Packet getDescriptionPacket() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.writeToNBT(var1);
      return new S35PacketUpdateTileEntity(this.pos, 4, var1);
   }

   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      this.skullType = var1.getByte("SkullType");
      this.skullRotation = var1.getByte("Rot");
      if (this.skullType == 3) {
         if (var1.hasKey("Owner", 10)) {
            this.playerProfile = NBTUtil.readGameProfileFromNBT(var1.getCompoundTag("Owner"));
         } else if (var1.hasKey("ExtraType", 8)) {
            String var2 = var1.getString("ExtraType");
            if (!StringUtils.isNullOrEmpty(var2)) {
               this.playerProfile = new GameProfile((UUID)null, var2);
               this.updatePlayerProfile();
            }
         }
      }

   }

   public int getSkullType() {
      return this.skullType;
   }

   public void setSkullRotation(int var1) {
      this.skullRotation = var1;
   }

   public int getSkullRotation() {
      return this.skullRotation;
   }

   private void updatePlayerProfile() {
      this.playerProfile = updateGameprofile(this.playerProfile);
      this.markDirty();
   }

   public void setType(int var1) {
      this.skullType = var1;
      this.playerProfile = null;
   }

   public void setPlayerProfile(GameProfile var1) {
      this.skullType = 3;
      this.playerProfile = var1;
      this.updatePlayerProfile();
   }

   public static GameProfile updateGameprofile(GameProfile var0) {
      if (var0 != null && !StringUtils.isNullOrEmpty(var0.getName())) {
         if (var0.isComplete() && var0.getProperties().containsKey("textures")) {
            return var0;
         } else if (MinecraftServer.getServer() == null) {
            return var0;
         } else {
            GameProfile var1 = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(var0.getName());
            if (var1 == null) {
               return var0;
            } else {
               Property var2 = (Property)Iterables.getFirst(var1.getProperties().get("textures"), (Object)null);
               if (var2 == null) {
                  var1 = MinecraftServer.getServer().getMinecraftSessionService().fillProfileProperties(var1, true);
               }

               return var1;
            }
         }
      } else {
         return var0;
      }
   }

   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      var1.setByte("SkullType", (byte)(this.skullType & 255));
      var1.setByte("Rot", (byte)(this.skullRotation & 255));
      if (this.playerProfile != null) {
         NBTTagCompound var2 = new NBTTagCompound();
         NBTUtil.writeGameProfile(var2, this.playerProfile);
         var1.setTag("Owner", var2);
      }

   }

   public GameProfile getPlayerProfile() {
      return this.playerProfile;
   }
}
