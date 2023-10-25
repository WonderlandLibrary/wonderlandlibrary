/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnchantmentTableRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEndPortalRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityMobSpawnerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityPistonRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
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
    public static double staticPlayerX;
    public Entity entity;
    private Map<Class<? extends TileEntity>, TileEntitySpecialRenderer<? extends TileEntity>> mapSpecialRenderers = Maps.newHashMap();
    public static TileEntityRendererDispatcher instance;
    public static double staticPlayerZ;
    private FontRenderer fontRenderer;
    public float entityPitch;
    public World worldObj;
    public double entityY;
    public double entityX;
    public TextureManager renderEngine;
    public double entityZ;
    public static double staticPlayerY;
    public float entityYaw;

    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(Class<? extends TileEntity> clazz) {
        TileEntitySpecialRenderer<TileEntity> tileEntitySpecialRenderer = this.mapSpecialRenderers.get(clazz);
        if (tileEntitySpecialRenderer == null && clazz != TileEntity.class) {
            tileEntitySpecialRenderer = this.getSpecialRendererByClass(clazz.getSuperclass());
            this.mapSpecialRenderers.put(clazz, tileEntitySpecialRenderer);
        }
        return tileEntitySpecialRenderer;
    }

    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(TileEntity tileEntity) {
        return tileEntity == null ? null : this.getSpecialRendererByClass(tileEntity.getClass());
    }

    public void setWorld(World world) {
        this.worldObj = world;
    }

    static {
        instance = new TileEntityRendererDispatcher();
    }

    public void renderTileEntityAt(TileEntity tileEntity, double d, double d2, double d3, float f) {
        this.renderTileEntityAt(tileEntity, d, d2, d3, f, -1);
    }

    public void renderTileEntity(TileEntity tileEntity, float f, int n) {
        if (tileEntity.getDistanceSq(this.entityX, this.entityY, this.entityZ) < tileEntity.getMaxRenderDistanceSquared()) {
            int n2 = this.worldObj.getCombinedLight(tileEntity.getPos(), 0);
            int n3 = n2 % 65536;
            int n4 = n2 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)n3 / 1.0f, (float)n4 / 1.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            BlockPos blockPos = tileEntity.getPos();
            this.renderTileEntityAt(tileEntity, (double)blockPos.getX() - staticPlayerX, (double)blockPos.getY() - staticPlayerY, (double)blockPos.getZ() - staticPlayerZ, f, n);
        }
    }

    public void cacheActiveRenderInfo(World world, TextureManager textureManager, FontRenderer fontRenderer, Entity entity, float f) {
        if (this.worldObj != world) {
            this.setWorld(world);
        }
        this.renderEngine = textureManager;
        this.entity = entity;
        this.fontRenderer = fontRenderer;
        this.entityYaw = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f;
        this.entityPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f;
        this.entityX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
        this.entityY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
        this.entityZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
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
        for (TileEntitySpecialRenderer<? extends TileEntity> tileEntitySpecialRenderer : this.mapSpecialRenderers.values()) {
            tileEntitySpecialRenderer.setRendererDispatcher(this);
        }
    }

    public void renderTileEntityAt(TileEntity tileEntity, double d, double d2, double d3, float f, int n) {
        TileEntitySpecialRenderer<TileEntity> tileEntitySpecialRenderer = this.getSpecialRenderer(tileEntity);
        if (tileEntitySpecialRenderer != null) {
            try {
                tileEntitySpecialRenderer.renderTileEntityAt(tileEntity, d, d2, d3, f, n);
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
                CrashReportCategory crashReportCategory = crashReport.makeCategory("Block Entity Details");
                tileEntity.addInfoToCrashReport(crashReportCategory);
                throw new ReportedException(crashReport);
            }
        }
    }
}

