// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.util.math.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.optifine.EmissiveTextures;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.optifine.reflect.Reflector;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityEndGateway;
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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.gui.FontRenderer;
import java.util.Map;

public class TileEntityRendererDispatcher
{
    public final Map<Class, TileEntitySpecialRenderer> renderers;
    public static TileEntityRendererDispatcher instance;
    public FontRenderer fontRenderer;
    public static double staticPlayerX;
    public static double staticPlayerY;
    public static double staticPlayerZ;
    public TextureManager renderEngine;
    public World world;
    public Entity entity;
    public float entityYaw;
    public float entityPitch;
    public RayTraceResult cameraHitResult;
    public double entityX;
    public double entityY;
    public double entityZ;
    public TileEntity tileEntityRendered;
    private Tessellator batchBuffer;
    private boolean drawingBatch;
    
    private TileEntityRendererDispatcher() {
        this.renderers = (Map<Class, TileEntitySpecialRenderer>)Maps.newHashMap();
        this.batchBuffer = new Tessellator(2097152);
        this.drawingBatch = false;
        this.renderers.put(TileEntitySign.class, new TileEntitySignRenderer());
        this.renderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        this.renderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
        this.renderers.put(TileEntityChest.class, new TileEntityChestRenderer());
        this.renderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
        this.renderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
        this.renderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
        this.renderers.put(TileEntityEndGateway.class, new TileEntityEndGatewayRenderer());
        this.renderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
        this.renderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
        this.renderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());
        this.renderers.put(TileEntityStructure.class, new TileEntityStructureRenderer());
        this.renderers.put(TileEntityShulkerBox.class, new TileEntityShulkerBoxRenderer(new ModelShulker()));
        this.renderers.put(TileEntityBed.class, new TileEntityBedRenderer());
        for (final TileEntitySpecialRenderer<?> tileentityspecialrenderer : this.renderers.values()) {
            tileentityspecialrenderer.setRendererDispatcher(this);
        }
    }
    
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getRenderer(final Class<? extends TileEntity> teClass) {
        TileEntitySpecialRenderer<T> tileentityspecialrenderer = this.renderers.get(teClass);
        if (tileentityspecialrenderer == null && teClass != TileEntity.class) {
            tileentityspecialrenderer = (TileEntitySpecialRenderer<T>)this.getRenderer((Class<? extends TileEntity>)teClass.getSuperclass());
            this.renderers.put(teClass, tileentityspecialrenderer);
        }
        return tileentityspecialrenderer;
    }
    
    @Nullable
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getRenderer(@Nullable final TileEntity tileEntityIn) {
        return (tileEntityIn != null && !tileEntityIn.isInvalid()) ? this.getRenderer(tileEntityIn.getClass()) : null;
    }
    
    public void prepare(final World worldIn, final TextureManager renderEngineIn, final FontRenderer fontRendererIn, final Entity entityIn, final RayTraceResult cameraHitResultIn, final float p_190056_6_) {
        if (this.world != worldIn) {
            this.setWorld(worldIn);
        }
        this.renderEngine = renderEngineIn;
        this.entity = entityIn;
        this.fontRenderer = fontRendererIn;
        this.cameraHitResult = cameraHitResultIn;
        this.entityYaw = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * p_190056_6_;
        this.entityPitch = entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * p_190056_6_;
        this.entityX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * p_190056_6_;
        this.entityY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * p_190056_6_;
        this.entityZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * p_190056_6_;
    }
    
    public void render(final TileEntity tileentityIn, final float partialTicks, final int destroyStage) {
        if (tileentityIn.getDistanceSq(this.entityX, this.entityY, this.entityZ) < tileentityIn.getMaxRenderDistanceSquared()) {
            boolean flag = true;
            if (Reflector.ForgeTileEntity_hasFastRenderer.exists()) {
                flag = (!this.drawingBatch || !Reflector.callBoolean(tileentityIn, Reflector.ForgeTileEntity_hasFastRenderer, new Object[0]));
            }
            if (flag) {
                RenderHelper.enableStandardItemLighting();
                final int i = this.world.getCombinedLight(tileentityIn.getPos(), 0);
                final int j = i % 65536;
                final int k = i / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
            final BlockPos blockpos = tileentityIn.getPos();
            if (!this.world.isBlockLoaded(blockpos, false)) {
                return;
            }
            if (EmissiveTextures.isActive()) {
                EmissiveTextures.beginRender();
            }
            this.render(tileentityIn, blockpos.getX() - TileEntityRendererDispatcher.staticPlayerX, blockpos.getY() - TileEntityRendererDispatcher.staticPlayerY, blockpos.getZ() - TileEntityRendererDispatcher.staticPlayerZ, partialTicks, destroyStage, 1.0f);
            if (EmissiveTextures.isActive()) {
                if (EmissiveTextures.hasEmissive()) {
                    EmissiveTextures.beginRenderEmissive();
                    this.render(tileentityIn, blockpos.getX() - TileEntityRendererDispatcher.staticPlayerX, blockpos.getY() - TileEntityRendererDispatcher.staticPlayerY, blockpos.getZ() - TileEntityRendererDispatcher.staticPlayerZ, partialTicks, destroyStage, 1.0f);
                    EmissiveTextures.endRenderEmissive();
                }
                EmissiveTextures.endRender();
            }
        }
    }
    
    public void render(final TileEntity tileEntityIn, final double x, final double y, final double z, final float partialTicks) {
        this.render(tileEntityIn, x, y, z, partialTicks, 1.0f);
    }
    
    public void render(final TileEntity p_192855_1_, final double p_192855_2_, final double p_192855_4_, final double p_192855_6_, final float p_192855_8_, final float p_192855_9_) {
        this.render(p_192855_1_, p_192855_2_, p_192855_4_, p_192855_6_, p_192855_8_, -1, p_192855_9_);
    }
    
    public void render(final TileEntity tileEntityIn, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float p_192854_10_) {
        final TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = this.getRenderer(tileEntityIn);
        if (tileentityspecialrenderer != null) {
            try {
                this.tileEntityRendered = tileEntityIn;
                if (this.drawingBatch && Reflector.callBoolean(tileEntityIn, Reflector.ForgeTileEntity_hasFastRenderer, new Object[0])) {
                    tileentityspecialrenderer.renderTileEntityFast(tileEntityIn, x, y, z, partialTicks, destroyStage, p_192854_10_, this.batchBuffer.getBuffer());
                }
                else {
                    tileentityspecialrenderer.render(tileEntityIn, x, y, z, partialTicks, destroyStage, p_192854_10_);
                }
                this.tileEntityRendered = null;
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
                tileEntityIn.addInfoToCrashReport(crashreportcategory);
                throw new ReportedException(crashreport);
            }
        }
    }
    
    public void setWorld(@Nullable final World worldIn) {
        this.world = worldIn;
        if (worldIn == null) {
            this.entity = null;
        }
    }
    
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public void preDrawBatch() {
        this.batchBuffer.getBuffer().begin(7, DefaultVertexFormats.BLOCK);
        this.drawingBatch = true;
    }
    
    public void drawBatch(final int p_drawBatch_1_) {
        this.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
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
            final Vec3d vec3d = (Vec3d)Reflector.call(Reflector.ActiveRenderInfo_getCameraPosition, new Object[0]);
            if (vec3d != null) {
                this.batchBuffer.getBuffer().sortVertexData((float)vec3d.x, (float)vec3d.y, (float)vec3d.z);
            }
            else {
                this.batchBuffer.getBuffer().sortVertexData(0.0f, 0.0f, 0.0f);
            }
        }
        this.batchBuffer.draw();
        RenderHelper.enableStandardItemLighting();
        this.drawingBatch = false;
    }
    
    static {
        TileEntityRendererDispatcher.instance = new TileEntityRendererDispatcher();
    }
}
