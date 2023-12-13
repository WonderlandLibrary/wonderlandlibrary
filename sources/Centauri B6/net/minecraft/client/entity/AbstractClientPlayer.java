package net.minecraft.client.entity;

import com.mojang.authlib.GameProfile;
import java.io.File;
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
import net.minecraft.src.CapeUtils;
import net.minecraft.src.Config;
import net.minecraft.src.PlayerConfigurations;
import net.minecraft.src.Reflector;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;

public abstract class AbstractClientPlayer extends EntityPlayer {
   private NetworkPlayerInfo playerInfo;
   private ResourceLocation locationOfCape = null;
   private String nameClear = null;
   private static final String __OBFID = "CL_00000935";

   public AbstractClientPlayer(World worldIn, GameProfile playerProfile) {
      super(worldIn, playerProfile);
      this.nameClear = playerProfile.getName();
      if(this.nameClear != null && !this.nameClear.isEmpty()) {
         this.nameClear = StringUtils.stripControlCodes(this.nameClear);
      }

      CapeUtils.downloadCape(this);
      CapeUtils.downloadCapeAC(this);
      PlayerConfigurations.getPlayerConfiguration(this);
   }

   public boolean hasSkin() {
      NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
      return networkplayerinfo != null && networkplayerinfo.hasLocationSkin();
   }

   public boolean hasPlayerInfo() {
      return this.getPlayerInfo() != null;
   }

   public String getNameClear() {
      return this.nameClear;
   }

   public NetworkPlayerInfo getPlayerInfo() {
      if(this.playerInfo == null) {
         this.playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getUniqueID());
      }

      return this.playerInfo;
   }

   public static ResourceLocation getLocationSkin(String username) {
      return new ResourceLocation("skins/" + StringUtils.stripControlCodes(username));
   }

   public ResourceLocation getLocationSkin() {
      NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
      return networkplayerinfo == null?DefaultPlayerSkin.getDefaultSkin(this.getUniqueID()):networkplayerinfo.getLocationSkin();
   }

   public ResourceLocation getLocationCape() {
      if(!Config.isShowCapes()) {
         return null;
      } else if(this.locationOfCape != null) {
         return this.locationOfCape;
      } else {
         NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
         return networkplayerinfo == null?null:networkplayerinfo.getLocationCape();
      }
   }

   public float getFovModifier() {
      float f = 1.0F;
      if(this.capabilities.isFlying) {
         f *= 1.1F;
      }

      IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
      f = (float)((double)f * ((iattributeinstance.getAttributeValue() / (double)this.capabilities.getWalkSpeed() + 1.0D) / 2.0D));
      if(this.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
         f = 1.0F;
      }

      if(this.isUsingItem() && this.getItemInUse().getItem() == Items.bow) {
         int i = this.getItemInUseDuration();
         float f1 = (float)i / 20.0F;
         if(f1 > 1.0F) {
            f1 = 1.0F;
         } else {
            f1 = f1 * f1;
         }

         f *= 1.0F - f1 * 0.15F;
      }

      return Reflector.ForgeHooksClient_getOffsetFOV.exists()?Reflector.callFloat(Reflector.ForgeHooksClient_getOffsetFOV, new Object[]{this, Float.valueOf(f)}):f;
   }

   public String getSkinType() {
      NetworkPlayerInfo networkplayerinfo = this.getPlayerInfo();
      return networkplayerinfo == null?DefaultPlayerSkin.getSkinType(this.getUniqueID()):networkplayerinfo.getSkinType();
   }

   public static ThreadDownloadImageData getDownloadImageSkin(ResourceLocation resourceLocationIn, String username) {
      TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
      Object object = texturemanager.getTexture(resourceLocationIn);
      if(object == null) {
         object = new ThreadDownloadImageData((File)null, String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", new Object[]{StringUtils.stripControlCodes(username)}), DefaultPlayerSkin.getDefaultSkin(getOfflineUUID(username)), new ImageBufferDownload());
         texturemanager.loadTexture(resourceLocationIn, (ITextureObject)object);
      }

      return (ThreadDownloadImageData)object;
   }

   public void setLocationOfCape(ResourceLocation p_setLocationOfCape_1_) {
      this.locationOfCape = p_setLocationOfCape_1_;
   }

   public ResourceLocation getLocationOfCape() {
      return this.locationOfCape;
   }

   public boolean isSpectator() {
      NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(this.getGameProfile().getId());
      return networkplayerinfo != null && networkplayerinfo.getGameType() == GameType.SPECTATOR;
   }
}
