// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.tileentity.TileEntity;
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
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.gui.FontRenderer;
import java.util.Map;

public class TileEntityRendererDispatcher
{
    private Map mapSpecialRenderers;
    public static TileEntityRendererDispatcher instance;
    private FontRenderer field_147557_n;
    public static double staticPlayerX;
    public static double staticPlayerY;
    public static double staticPlayerZ;
    public TextureManager renderEngine;
    public World worldObj;
    public Entity field_147551_g;
    public float field_147562_h;
    public float field_147563_i;
    public double field_147560_j;
    public double field_147561_k;
    public double field_147558_l;
    private static final String __OBFID = "CL_00000963";
    
    private TileEntityRendererDispatcher() {
        (this.mapSpecialRenderers = Maps.newHashMap()).put(TileEntitySign.class, new TileEntitySignRenderer());
        this.mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        this.mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
        this.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
        this.mapSpecialRenderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
        this.mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
        this.mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
        this.mapSpecialRenderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());
        for (final TileEntitySpecialRenderer var2 : this.mapSpecialRenderers.values()) {
            var2.setRendererDispatcher(this);
        }
    }
    
    public TileEntitySpecialRenderer getSpecialRendererByClass(final Class p_147546_1_) {
        TileEntitySpecialRenderer var2 = (TileEntitySpecialRenderer)this.mapSpecialRenderers.get(p_147546_1_);
        if (var2 == null && p_147546_1_ != TileEntity.class) {
            var2 = this.getSpecialRendererByClass(p_147546_1_.getSuperclass());
            this.mapSpecialRenderers.put(p_147546_1_, var2);
        }
        return var2;
    }
    
    public boolean hasSpecialRenderer(final TileEntity p_147545_1_) {
        return this.getSpecialRenderer(p_147545_1_) != null;
    }
    
    public TileEntitySpecialRenderer getSpecialRenderer(final TileEntity p_147547_1_) {
        return (p_147547_1_ == null) ? null : this.getSpecialRendererByClass(p_147547_1_.getClass());
    }
    
    public void func_178470_a(final World worldIn, final TextureManager p_178470_2_, final FontRenderer p_178470_3_, final Entity p_178470_4_, final float p_178470_5_) {
        if (this.worldObj != worldIn) {
            this.func_147543_a(worldIn);
        }
        this.renderEngine = p_178470_2_;
        this.field_147551_g = p_178470_4_;
        this.field_147557_n = p_178470_3_;
        this.field_147562_h = p_178470_4_.prevRotationYaw + (p_178470_4_.rotationYaw - p_178470_4_.prevRotationYaw) * p_178470_5_;
        this.field_147563_i = p_178470_4_.prevRotationPitch + (p_178470_4_.rotationPitch - p_178470_4_.prevRotationPitch) * p_178470_5_;
        this.field_147560_j = p_178470_4_.lastTickPosX + (p_178470_4_.posX - p_178470_4_.lastTickPosX) * p_178470_5_;
        this.field_147561_k = p_178470_4_.lastTickPosY + (p_178470_4_.posY - p_178470_4_.lastTickPosY) * p_178470_5_;
        this.field_147558_l = p_178470_4_.lastTickPosZ + (p_178470_4_.posZ - p_178470_4_.lastTickPosZ) * p_178470_5_;
    }
    
    public void func_180546_a(final TileEntity p_180546_1_, final float p_180546_2_, final int p_180546_3_) {
        if (p_180546_1_.getDistanceSq(this.field_147560_j, this.field_147561_k, this.field_147558_l) < p_180546_1_.getMaxRenderDistanceSquared()) {
            final int var4 = this.worldObj.getCombinedLight(p_180546_1_.getPos(), 0);
            final int var5 = var4 % 65536;
            final int var6 = var4 / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var5 / 1.0f, var6 / 1.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final BlockPos var7 = p_180546_1_.getPos();
            this.func_178469_a(p_180546_1_, var7.getX() - TileEntityRendererDispatcher.staticPlayerX, var7.getY() - TileEntityRendererDispatcher.staticPlayerY, var7.getZ() - TileEntityRendererDispatcher.staticPlayerZ, p_180546_2_, p_180546_3_);
        }
    }
    
    public void renderTileEntityAt(final TileEntity p_147549_1_, final double p_147549_2_, final double p_147549_4_, final double p_147549_6_, final float p_147549_8_) {
        this.func_178469_a(p_147549_1_, p_147549_2_, p_147549_4_, p_147549_6_, p_147549_8_, -1);
    }
    
    public void func_178469_a(final TileEntity p_178469_1_, final double p_178469_2_, final double p_178469_4_, final double p_178469_6_, final float p_178469_8_, final int p_178469_9_) {
        final TileEntitySpecialRenderer var10 = this.getSpecialRenderer(p_178469_1_);
        if (var10 != null) {
            try {
                var10.renderTileEntityAt(p_178469_1_, p_178469_2_, p_178469_4_, p_178469_6_, p_178469_8_, p_178469_9_);
            }
            catch (Throwable var12) {
                final CrashReport var11 = CrashReport.makeCrashReport(var12, "Rendering Block Entity");
                final CrashReportCategory var13 = var11.makeCategory("Block Entity Details");
                p_178469_1_.addInfoToCrashReport(var13);
                throw new ReportedException(var11);
            }
        }
    }
    
    public void func_147543_a(final World worldIn) {
        this.worldObj = worldIn;
    }
    
    public FontRenderer getFontRenderer() {
        return this.field_147557_n;
    }
    
    static {
        TileEntityRendererDispatcher.instance = new TileEntityRendererDispatcher();
    }
}
