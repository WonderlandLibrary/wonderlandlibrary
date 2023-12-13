package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

public class TileEntityRendererDispatcher {
   public Entity entity;
   private FontRenderer fontRenderer;
   public double entityZ;
   public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
   public static double staticPlayerX;
   public static double staticPlayerY;
   public World worldObj;
   public double entityX;
   public static double staticPlayerZ;
   private Map mapSpecialRenderers = Maps.newHashMap();
   public float entityYaw;
   public float entityPitch;
   public double entityY;
   public TextureManager renderEngine;

   public TileEntitySpecialRenderer getSpecialRenderer(TileEntity var1) {
      return var1 == null ? null : this.getSpecialRendererByClass(var1.getClass());
   }

   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8) {
      this.renderTileEntityAt(var1, var2, var4, var6, var8, -1);
   }

   public TileEntitySpecialRenderer getSpecialRendererByClass(Class var1) {
      TileEntitySpecialRenderer var2 = (TileEntitySpecialRenderer)this.mapSpecialRenderers.get(var1);
      if (var2 == null && var1 != TileEntity.class) {
         var2 = this.getSpecialRendererByClass(var1.getSuperclass());
         this.mapSpecialRenderers.put(var1, var2);
      }

      return var2;
   }

   private TileEntityRendererDispatcher() {
      this.mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRenderer());
      this.mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
      this.mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
      this.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
      this.mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
      this.mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
      this.mapSpecialRenderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
      this.mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
      this.mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
      this.mapSpecialRenderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());
      Iterator var2 = this.mapSpecialRenderers.values().iterator();

      while(var2.hasNext()) {
         TileEntitySpecialRenderer var1 = (TileEntitySpecialRenderer)var2.next();
         var1.setRendererDispatcher(this);
      }

   }

   public void setWorld(World var1) {
      this.worldObj = var1;
   }

   public void renderTileEntity(TileEntity var1, float var2, int var3) {
      if (var1.getDistanceSq(this.entityX, this.entityY, this.entityZ) < var1.getMaxRenderDistanceSquared()) {
         int var4 = this.worldObj.getCombinedLight(var1.getPos(), 0);
         int var5 = var4 % 65536;
         int var6 = var4 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var5 / 1.0F, (float)var6 / 1.0F);
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         BlockPos var7 = var1.getPos();
         this.renderTileEntityAt(var1, (double)var7.getX() - staticPlayerX, (double)var7.getY() - staticPlayerY, (double)var7.getZ() - staticPlayerZ, var2, var3);
      }

   }

   public FontRenderer getFontRenderer() {
      return this.fontRenderer;
   }

   public void cacheActiveRenderInfo(World var1, TextureManager var2, FontRenderer var3, Entity var4, float var5) {
      if (this.worldObj != var1) {
         this.setWorld(var1);
      }

      this.renderEngine = var2;
      this.entity = var4;
      this.fontRenderer = var3;
      this.entityYaw = var4.prevRotationYaw + (var4.rotationYaw - var4.prevRotationYaw) * var5;
      this.entityPitch = var4.prevRotationPitch + (var4.rotationPitch - var4.prevRotationPitch) * var5;
      this.entityX = var4.lastTickPosX + (var4.posX - var4.lastTickPosX) * (double)var5;
      this.entityY = var4.lastTickPosY + (var4.posY - var4.lastTickPosY) * (double)var5;
      this.entityZ = var4.lastTickPosZ + (var4.posZ - var4.lastTickPosZ) * (double)var5;
   }

   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int var9) {
      TileEntitySpecialRenderer var10 = this.getSpecialRenderer(var1);
      if (var10 != null) {
         try {
            var10.renderTileEntityAt(var1, var2, var4, var6, var8, var9);
         } catch (Throwable var14) {
            CrashReport var12 = CrashReport.makeCrashReport(var14, "Rendering Block Entity");
            CrashReportCategory var13 = var12.makeCategory("Block Entity Details");
            var1.addInfoToCrashReport(var13);
            throw new ReportedException(var12);
         }
      }

   }
}
