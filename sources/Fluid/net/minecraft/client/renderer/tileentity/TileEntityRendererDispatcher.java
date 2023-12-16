// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.BlockPos;
import net.optifine.EmissiveTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.optifine.reflect.Reflector;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.gui.FontRenderer;
import java.util.Map;

public class TileEntityRendererDispatcher
{
    public Map<Class, TileEntitySpecialRenderer> mapSpecialRenderers;
    public static TileEntityRendererDispatcher instance;
    public FontRenderer fontRenderer;
    public static double staticPlayerX;
    public static double staticPlayerY;
    public static double staticPlayerZ;
    public TextureManager renderEngine;
    public World worldObj;
    public Entity entity;
    public float entityYaw;
    public float entityPitch;
    public double entityX;
    public double entityY;
    public double entityZ;
    public TileEntity tileEntityRendered;
    private Tessellator batchBuffer;
    private boolean drawingBatch;
    
    static {
        TileEntityRendererDispatcher.instance = new TileEntityRendererDispatcher();
    }
    
    private TileEntityRendererDispatcher() {
        this.mapSpecialRenderers = Maps.newHashMap();
        this.batchBuffer = new Tessellator(2097152);
        this.drawingBatch = false;
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
        for (final TileEntitySpecialRenderer<?> tileentityspecialrenderer : this.mapSpecialRenderers.values()) {
            tileentityspecialrenderer.setRendererDispatcher(this);
        }
    }
    
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(final Class<? extends TileEntity> teClass) {
        TileEntitySpecialRenderer<? extends TileEntity> tileentityspecialrenderer = this.mapSpecialRenderers.get(teClass);
        if (tileentityspecialrenderer == null && teClass != TileEntity.class) {
            tileentityspecialrenderer = this.getSpecialRendererByClass((Class<? extends TileEntity>)teClass.getSuperclass());
            this.mapSpecialRenderers.put(teClass, tileentityspecialrenderer);
        }
        return (TileEntitySpecialRenderer<T>)tileentityspecialrenderer;
    }
    
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(final TileEntity tileEntityIn) {
        return (tileEntityIn != null && !tileEntityIn.isInvalid()) ? this.getSpecialRendererByClass(tileEntityIn.getClass()) : null;
    }
    
    public void cacheActiveRenderInfo(final World worldIn, final TextureManager textureManagerIn, final FontRenderer fontrendererIn, final Entity entityIn, final float partialTicks) {
        if (this.worldObj != worldIn) {
            this.setWorld(worldIn);
        }
        this.renderEngine = textureManagerIn;
        this.entity = entityIn;
        this.fontRenderer = fontrendererIn;
        this.entityYaw = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        this.entityPitch = entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks;
        this.entityX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        this.entityY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
        this.entityZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
    }
    
    public void renderTileEntity(final TileEntity tileentityIn, final float partialTicks, final int destroyStage) {
        if (tileentityIn.getDistanceSq(this.entityX, this.entityY, this.entityZ) < tileentityIn.getMaxRenderDistanceSquared()) {
            boolean flag = true;
            if (Reflector.ForgeTileEntity_hasFastRenderer.exists()) {
                flag = (!this.drawingBatch || !Reflector.callBoolean(tileentityIn, Reflector.ForgeTileEntity_hasFastRenderer, new Object[0]));
            }
            if (flag) {
                RenderHelper.enableStandardItemLighting();
                final int i = this.worldObj.getCombinedLight(tileentityIn.getPos(), 0);
                final int j = i % 65536;
                final int k = i / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0f, k / 1.0f);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
            final BlockPos blockpos = tileentityIn.getPos();
            if (!this.worldObj.isBlockLoaded(blockpos, false)) {
                return;
            }
            if (EmissiveTextures.isActive()) {
                EmissiveTextures.beginRender();
            }
            this.renderTileEntityAt(tileentityIn, blockpos.getX() - TileEntityRendererDispatcher.staticPlayerX, blockpos.getY() - TileEntityRendererDispatcher.staticPlayerY, blockpos.getZ() - TileEntityRendererDispatcher.staticPlayerZ, partialTicks, destroyStage);
            if (EmissiveTextures.isActive()) {
                if (EmissiveTextures.hasEmissive()) {
                    EmissiveTextures.beginRenderEmissive();
                    this.renderTileEntityAt(tileentityIn, blockpos.getX() - TileEntityRendererDispatcher.staticPlayerX, blockpos.getY() - TileEntityRendererDispatcher.staticPlayerY, blockpos.getZ() - TileEntityRendererDispatcher.staticPlayerZ, partialTicks, destroyStage);
                    EmissiveTextures.endRenderEmissive();
                }
                EmissiveTextures.endRender();
            }
        }
    }
    
    public void renderTileEntityAt(final TileEntity tileEntityIn, final double x, final double y, final double z, final float partialTicks) {
        this.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, -1);
    }
    
    public void renderTileEntityAt(final TileEntity tileEntityIn, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        final TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = this.getSpecialRenderer(tileEntityIn);
        if (tileentityspecialrenderer != null) {
            try {
                this.tileEntityRendered = tileEntityIn;
                if (this.drawingBatch && Reflector.callBoolean(tileEntityIn, Reflector.ForgeTileEntity_hasFastRenderer, new Object[0])) {
                    tileentityspecialrenderer.renderTileEntityFast(tileEntityIn, x, y, z, partialTicks, destroyStage, this.batchBuffer.getWorldRenderer());
                }
                else {
                    tileentityspecialrenderer.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, destroyStage);
                }
                this.tileEntityRendered = null;
            }
            catch (final Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
                tileEntityIn.addInfoToCrashReport(crashreportcategory);
                throw new ReportedException(crashreport);
            }
        }
    }
    
    public void setWorld(final World worldIn) {
        this.worldObj = worldIn;
    }
    
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public void preDrawBatch() {
        this.batchBuffer.getWorldRenderer().begin(7, DefaultVertexFormats.BLOCK);
        this.drawingBatch = true;
    }
    
    public void drawBatch(final int p_drawBatch_1_) {
        this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(7425);
        }
        else {
            GlStateManager.shadeModel(7424);
        }
        if (p_drawBatch_1_ > 0) {
            this.batchBuffer.getWorldRenderer().sortVertexData((float)TileEntityRendererDispatcher.staticPlayerX, (float)TileEntityRendererDispatcher.staticPlayerY, (float)TileEntityRendererDispatcher.staticPlayerZ);
        }
        this.batchBuffer.draw();
        RenderHelper.enableStandardItemLighting();
        this.drawingBatch = false;
    }
}
