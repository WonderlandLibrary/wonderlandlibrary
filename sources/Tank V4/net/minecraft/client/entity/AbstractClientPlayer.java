package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import java.io.File;
import my.NewSnake.Tank.module.ModuleManager;
import my.NewSnake.Tank.module.modules.PLAYER.Capes;
import my.NewSnake.event.events.SprintEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import optifine.CapeUtils;
import optifine.Config;
import optifine.PlayerConfigurations;
import optifine.Reflector;

public abstract class AbstractClientPlayer extends EntityPlayer {
   private NetworkPlayerInfo playerInfo;
   private ResourceLocation locationOfCape = null;
   private String nameClear = null;
   private static final String __OBFID = "CL_00000935";

   public ResourceLocation getLocationSkin() {
      NetworkPlayerInfo var1 = this.getPlayerInfo();
      return var1 == null ? DefaultPlayerSkin.getDefaultSkin(this.getUniqueID()) : var1.getLocationSkin();
   }

   public static ResourceLocation getLocationSkin(String var0) {
      return new ResourceLocation("skins/" + StringUtils.stripControlCodes(var0));
   }

   public String getSkinType() {
      NetworkPlayerInfo var1 = this.getPlayerInfo();
      return var1 == null ? DefaultPlayerSkin.getSkinType(this.getUniqueID()) : var1.getSkinType();
   }

   public ResourceLocation getLocationCape() {
      Capes var1 = (Capes)ModuleManager.getModule("Capes");
      if (!Config.isShowCapes()) {
         return null;
      } else if (this.locationOfCape != null) {
         return var1.isEnabled() && var1.canRender(this) ? var1.getCapes() : this.locationOfCape;
      } else {
         NetworkPlayerInfo var2 = this.getPlayerInfo();
         return var2 == null ? null : (var1.isEnabled() && var1.canRender(this) ? var1.getCapes() : var2.getLocationCape());
      }
   }

   public AbstractClientPlayer(World var1, GameProfile var2) {
      super(var1, var2);
      this.nameClear = var2.getName();
      if (this.nameClear != null && !this.nameClear.isEmpty()) {
         this.nameClear = StringUtils.stripControlCodes(this.nameClear);
      }

      CapeUtils.downloadCape(this);
      PlayerConfigurations.getPlayerConfiguration(this);
   }

   public float getFovModifier() {
      float var1 = 1.0F;
      if (this.capabilities.isFlying) {
         var1 *= 1.1F;
      }

      IAttributeInstance var2 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
      var1 = (float)((double)var1 * ((var2.getAttributeValue() / (double)this.capabilities.getWalkSpeed() + 1.0D) / 2.0D));
      if (this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(var1) || Float.isInfinite(var1)) {
         var1 = 1.0F;
      }

      if (this.isUsingItem() && this.getItemInUse().getItem() == Items.bow) {
         int var3 = this.getItemInUseDuration();
         float var4 = (float)var3 / 20.0F;
         if (var4 > 1.0F) {
            var4 = 1.0F;
         } else {
            var4 *= var4;
         }

         var1 *= 1.0F - var4 * 0.15F;
      }

      return Reflector.ForgeHooksClient_getOffsetFOV.exists() ? Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, this, var1) : var1;
   }

   public void setSprinting(boolean var1) {
      SprintEvent var2 = new SprintEvent(var1);
      var2.call();
      var1 = var2.isSprinting();
      super.setSprinting(var1);
      IAttributeInstance var3 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
      if (var3.getModifier(sprintingSpeedBoostModifierUUID) != null) {
         var3.removeModifier(sprintingSpeedBoostModifier);
      }

      if (var1) {
         var3.applyModifier(sprintingSpeedBoostModifier);
      }

   }

   public ResourceLocation getLocationOfCape() {
      return this.locationOfCape;
   }

   public boolean isSpectator() {
      NetworkPlayerInfo var1 = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getGameProfile().getId());
      return var1 != null && var1.getGameType() == WorldSettings.GameType.SPECTATOR;
   }

   public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation var0, String var1) {
      TextureManager var2 = Minecraft.getMinecraft().getTextureManager();
      Object var3 = var2.getTexture(var0);
      if (var3 == null) {
         var3 = new ThreadDownloadImageData((File)null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", StringUtils.stripControlCodes(var1)), DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(var1)), new ImageBufferDownload());
         var2.loadTexture(var0, (ITextureObject)var3);
      }

      return (ThreadDownloadImageData)var3;
   }

   protected NetworkPlayerInfo getPlayerInfo() {
      if (this.playerInfo == null) {
         this.playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getUniqueID());
      }

      return this.playerInfo;
   }

   public boolean hasPlayerInfo() {
      return this.getPlayerInfo() != null;
   }

   public void setLocationOfCape(ResourceLocation var1) {
      this.locationOfCape = var1;
   }

   public String getNameClear() {
      return this.nameClear;
   }

   public boolean hasSkin() {
      NetworkPlayerInfo var1 = this.getPlayerInfo();
      return var1 != null && var1.hasLocationSkin();
   }
}
