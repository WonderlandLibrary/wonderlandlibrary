// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.renderer.texture.TextureManager;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;

public class EffectRenderer
{
    private static final ResourceLocation particleTextures;
    protected World worldObj;
    private List[][] fxLayers;
    private List field_178933_d;
    private TextureManager renderer;
    private Random rand;
    private Map field_178932_g;
    private static final String __OBFID = "CL_00000915";
    
    public EffectRenderer(final World worldIn, final TextureManager p_i1220_2_) {
        this.fxLayers = new List[4][];
        this.field_178933_d = Lists.newArrayList();
        this.rand = new Random();
        this.field_178932_g = Maps.newHashMap();
        this.worldObj = worldIn;
        this.renderer = p_i1220_2_;
        for (int var3 = 0; var3 < 4; ++var3) {
            this.fxLayers[var3] = new List[2];
            for (int var4 = 0; var4 < 2; ++var4) {
                this.fxLayers[var3][var4] = Lists.newArrayList();
            }
        }
        this.func_178930_c();
    }
    
    private void func_178930_c() {
        this.func_178929_a(EnumParticleTypes.EXPLOSION_NORMAL.func_179348_c(), new EntityExplodeFX.Factory());
        this.func_178929_a(EnumParticleTypes.WATER_BUBBLE.func_179348_c(), new EntityBubbleFX.Factory());
        this.func_178929_a(EnumParticleTypes.WATER_SPLASH.func_179348_c(), new EntitySplashFX.Factory());
        this.func_178929_a(EnumParticleTypes.WATER_WAKE.func_179348_c(), new EntityFishWakeFX.Factory());
        this.func_178929_a(EnumParticleTypes.WATER_DROP.func_179348_c(), new EntityRainFX.Factory());
        this.func_178929_a(EnumParticleTypes.SUSPENDED.func_179348_c(), new EntitySuspendFX.Factory());
        this.func_178929_a(EnumParticleTypes.SUSPENDED_DEPTH.func_179348_c(), new EntityAuraFX.Factory());
        this.func_178929_a(EnumParticleTypes.CRIT.func_179348_c(), new EntityCrit2FX.Factory());
        this.func_178929_a(EnumParticleTypes.CRIT_MAGIC.func_179348_c(), new EntityCrit2FX.MagicFactory());
        this.func_178929_a(EnumParticleTypes.SMOKE_NORMAL.func_179348_c(), new EntitySmokeFX.Factory());
        this.func_178929_a(EnumParticleTypes.SMOKE_LARGE.func_179348_c(), new EntityCritFX.Factory());
        this.func_178929_a(EnumParticleTypes.SPELL.func_179348_c(), new EntitySpellParticleFX.Factory());
        this.func_178929_a(EnumParticleTypes.SPELL_INSTANT.func_179348_c(), new EntitySpellParticleFX.InstantFactory());
        this.func_178929_a(EnumParticleTypes.SPELL_MOB.func_179348_c(), new EntitySpellParticleFX.MobFactory());
        this.func_178929_a(EnumParticleTypes.SPELL_MOB_AMBIENT.func_179348_c(), new EntitySpellParticleFX.AmbientMobFactory());
        this.func_178929_a(EnumParticleTypes.SPELL_WITCH.func_179348_c(), new EntitySpellParticleFX.WitchFactory());
        this.func_178929_a(EnumParticleTypes.DRIP_WATER.func_179348_c(), new EntityDropParticleFX.WaterFactory());
        this.func_178929_a(EnumParticleTypes.DRIP_LAVA.func_179348_c(), new EntityDropParticleFX.LavaFactory());
        this.func_178929_a(EnumParticleTypes.VILLAGER_ANGRY.func_179348_c(), new EntityHeartFX.AngryVillagerFactory());
        this.func_178929_a(EnumParticleTypes.VILLAGER_HAPPY.func_179348_c(), new EntityAuraFX.HappyVillagerFactory());
        this.func_178929_a(EnumParticleTypes.TOWN_AURA.func_179348_c(), new EntityAuraFX.Factory());
        this.func_178929_a(EnumParticleTypes.NOTE.func_179348_c(), new EntityNoteFX.Factory());
        this.func_178929_a(EnumParticleTypes.PORTAL.func_179348_c(), new EntityPortalFX.Factory());
        this.func_178929_a(EnumParticleTypes.ENCHANTMENT_TABLE.func_179348_c(), new EntityEnchantmentTableParticleFX.EnchantmentTable());
        this.func_178929_a(EnumParticleTypes.FLAME.func_179348_c(), new EntityFlameFX.Factory());
        this.func_178929_a(EnumParticleTypes.LAVA.func_179348_c(), new EntityLavaFX.Factory());
        this.func_178929_a(EnumParticleTypes.FOOTSTEP.func_179348_c(), new EntityFootStepFX.Factory());
        this.func_178929_a(EnumParticleTypes.CLOUD.func_179348_c(), new EntityCloudFX.Factory());
        this.func_178929_a(EnumParticleTypes.REDSTONE.func_179348_c(), new EntityReddustFX.Factory());
        this.func_178929_a(EnumParticleTypes.SNOWBALL.func_179348_c(), new EntityBreakingFX.SnowballFactory());
        this.func_178929_a(EnumParticleTypes.SNOW_SHOVEL.func_179348_c(), new EntitySnowShovelFX.Factory());
        this.func_178929_a(EnumParticleTypes.SLIME.func_179348_c(), new EntityBreakingFX.SlimeFactory());
        this.func_178929_a(EnumParticleTypes.HEART.func_179348_c(), new EntityHeartFX.Factory());
        this.func_178929_a(EnumParticleTypes.BARRIER.func_179348_c(), new Barrier.Factory());
        this.func_178929_a(EnumParticleTypes.ITEM_CRACK.func_179348_c(), new EntityBreakingFX.Factory());
        this.func_178929_a(EnumParticleTypes.BLOCK_CRACK.func_179348_c(), new EntityDiggingFX.Factory());
        this.func_178929_a(EnumParticleTypes.BLOCK_DUST.func_179348_c(), new EntityBlockDustFX.Factory());
        this.func_178929_a(EnumParticleTypes.EXPLOSION_HUGE.func_179348_c(), new EntityHugeExplodeFX.Factory());
        this.func_178929_a(EnumParticleTypes.EXPLOSION_LARGE.func_179348_c(), new EntityLargeExplodeFX.Factory());
        this.func_178929_a(EnumParticleTypes.FIREWORKS_SPARK.func_179348_c(), new EntityFireworkStarterFX_Factory());
        this.func_178929_a(EnumParticleTypes.MOB_APPEARANCE.func_179348_c(), new MobAppearance.Factory());
    }
    
    public void func_178929_a(final int p_178929_1_, final IParticleFactory p_178929_2_) {
        this.field_178932_g.put(p_178929_1_, p_178929_2_);
    }
    
    public void func_178926_a(final Entity p_178926_1_, final EnumParticleTypes p_178926_2_) {
        this.field_178933_d.add(new EntityParticleEmitter(this.worldObj, p_178926_1_, p_178926_2_));
    }
    
    public EntityFX func_178927_a(final int p_178927_1_, final double p_178927_2_, final double p_178927_4_, final double p_178927_6_, final double p_178927_8_, final double p_178927_10_, final double p_178927_12_, final int... p_178927_14_) {
        final IParticleFactory var15 = this.field_178932_g.get(p_178927_1_);
        if (var15 != null) {
            final EntityFX var16 = var15.func_178902_a(p_178927_1_, this.worldObj, p_178927_2_, p_178927_4_, p_178927_6_, p_178927_8_, p_178927_10_, p_178927_12_, p_178927_14_);
            if (var16 != null) {
                this.addEffect(var16);
                return var16;
            }
        }
        return null;
    }
    
    public void addEffect(final EntityFX p_78873_1_) {
        final int var2 = p_78873_1_.getFXLayer();
        final int var3 = (p_78873_1_.func_174838_j() == 1.0f) ? 1 : 0;
        if (this.fxLayers[var2][var3].size() >= 4000) {
            this.fxLayers[var2][var3].remove(0);
        }
        this.fxLayers[var2][var3].add(p_78873_1_);
    }
    
    public void updateEffects() {
        for (int var1 = 0; var1 < 4; ++var1) {
            this.func_178922_a(var1);
        }
        final ArrayList var2 = Lists.newArrayList();
        for (final EntityParticleEmitter var4 : this.field_178933_d) {
            var4.onUpdate();
            if (var4.isDead) {
                var2.add(var4);
            }
        }
        this.field_178933_d.removeAll(var2);
    }
    
    private void func_178922_a(final int p_178922_1_) {
        for (int var2 = 0; var2 < 2; ++var2) {
            this.func_178925_a(this.fxLayers[p_178922_1_][var2]);
        }
    }
    
    private void func_178925_a(final List p_178925_1_) {
        final ArrayList var2 = Lists.newArrayList();
        for (int var3 = 0; var3 < p_178925_1_.size(); ++var3) {
            final EntityFX var4 = p_178925_1_.get(var3);
            this.func_178923_d(var4);
            if (var4.isDead) {
                var2.add(var4);
            }
        }
        p_178925_1_.removeAll(var2);
    }
    
    private void func_178923_d(final EntityFX p_178923_1_) {
        try {
            p_178923_1_.onUpdate();
        }
        catch (Throwable var4) {
            final CrashReport var3 = CrashReport.makeCrashReport(var4, "Ticking Particle");
            final CrashReportCategory var5 = var3.makeCategory("Particle being ticked");
            final int var6 = p_178923_1_.getFXLayer();
            var5.addCrashSectionCallable("Particle", new Callable() {
                private static final String __OBFID = "CL_00000916";
                
                @Override
                public String call() {
                    return p_178923_1_.toString();
                }
            });
            var5.addCrashSectionCallable("Particle Type", new Callable() {
                private static final String __OBFID = "CL_00000917";
                
                @Override
                public String call() {
                    return (var6 == 0) ? "MISC_TEXTURE" : ((var6 == 1) ? "TERRAIN_TEXTURE" : ((var6 == 3) ? "ENTITY_PARTICLE_TEXTURE" : ("Unknown - " + var6)));
                }
            });
            throw new ReportedException(var3);
        }
    }
    
    public void renderParticles(final Entity p_78874_1_, final float p_78874_2_) {
        final float var3 = ActiveRenderInfo.func_178808_b();
        final float var4 = ActiveRenderInfo.func_178803_d();
        final float var5 = ActiveRenderInfo.func_178805_e();
        final float var6 = ActiveRenderInfo.func_178807_f();
        final float var7 = ActiveRenderInfo.func_178809_c();
        EntityFX.interpPosX = p_78874_1_.lastTickPosX + (p_78874_1_.posX - p_78874_1_.lastTickPosX) * p_78874_2_;
        EntityFX.interpPosY = p_78874_1_.lastTickPosY + (p_78874_1_.posY - p_78874_1_.lastTickPosY) * p_78874_2_;
        EntityFX.interpPosZ = p_78874_1_.lastTickPosZ + (p_78874_1_.posZ - p_78874_1_.lastTickPosZ) * p_78874_2_;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.003921569f);
        for (int var8_nf = 0; var8_nf < 3; ++var8_nf) {
            final int var8 = var8_nf;
            for (int var9 = 0; var9 < 2; ++var9) {
                if (!this.fxLayers[var8][var9].isEmpty()) {
                    switch (var9) {
                        case 0: {
                            GlStateManager.depthMask(false);
                            break;
                        }
                        case 1: {
                            GlStateManager.depthMask(true);
                            break;
                        }
                    }
                    switch (var8) {
                        default: {
                            this.renderer.bindTexture(EffectRenderer.particleTextures);
                            break;
                        }
                        case 1: {
                            this.renderer.bindTexture(TextureMap.locationBlocksTexture);
                            break;
                        }
                    }
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    final Tessellator var10 = Tessellator.getInstance();
                    final WorldRenderer var11 = var10.getWorldRenderer();
                    var11.startDrawingQuads();
                    for (int var12 = 0; var12 < this.fxLayers[var8][var9].size(); ++var12) {
                        final EntityFX var13 = this.fxLayers[var8][var9].get(var12);
                        var11.func_178963_b(var13.getBrightnessForRender(p_78874_2_));
                        try {
                            var13.func_180434_a(var11, p_78874_1_, p_78874_2_, var3, var7, var4, var5, var6);
                        }
                        catch (Throwable var15) {
                            final CrashReport var14 = CrashReport.makeCrashReport(var15, "Rendering Particle");
                            final CrashReportCategory var16 = var14.makeCategory("Particle being rendered");
                            var16.addCrashSectionCallable("Particle", new Callable() {
                                private static final String __OBFID = "CL_00000918";
                                
                                @Override
                                public String call() {
                                    return var13.toString();
                                }
                            });
                            var16.addCrashSectionCallable("Particle Type", new Callable() {
                                private static final String __OBFID = "CL_00000919";
                                
                                @Override
                                public String call() {
                                    return (var8 == 0) ? "MISC_TEXTURE" : ((var8 == 1) ? "TERRAIN_TEXTURE" : ((var8 == 3) ? "ENTITY_PARTICLE_TEXTURE" : ("Unknown - " + var8)));
                                }
                            });
                            throw new ReportedException(var14);
                        }
                    }
                    var10.draw();
                }
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1f);
    }
    
    public void renderLitParticles(final Entity p_78872_1_, final float p_78872_2_) {
        final float var3 = 0.017453292f;
        final float var4 = MathHelper.cos(p_78872_1_.rotationYaw * 0.017453292f);
        final float var5 = MathHelper.sin(p_78872_1_.rotationYaw * 0.017453292f);
        final float var6 = -var5 * MathHelper.sin(p_78872_1_.rotationPitch * 0.017453292f);
        final float var7 = var4 * MathHelper.sin(p_78872_1_.rotationPitch * 0.017453292f);
        final float var8 = MathHelper.cos(p_78872_1_.rotationPitch * 0.017453292f);
        for (int var9 = 0; var9 < 2; ++var9) {
            final List var10 = this.fxLayers[3][var9];
            if (!var10.isEmpty()) {
                final Tessellator var11 = Tessellator.getInstance();
                final WorldRenderer var12 = var11.getWorldRenderer();
                for (int var13 = 0; var13 < var10.size(); ++var13) {
                    final EntityFX var14 = var10.get(var13);
                    var12.func_178963_b(var14.getBrightnessForRender(p_78872_2_));
                    var14.func_180434_a(var12, p_78872_1_, p_78872_2_, var4, var8, var5, var6, var7);
                }
            }
        }
    }
    
    public void clearEffects(final World worldIn) {
        this.worldObj = worldIn;
        for (int var2 = 0; var2 < 4; ++var2) {
            for (int var3 = 0; var3 < 2; ++var3) {
                this.fxLayers[var2][var3].clear();
            }
        }
        this.field_178933_d.clear();
    }
    
    public void func_180533_a(final BlockPos p_180533_1_, IBlockState p_180533_2_) {
        if (p_180533_2_.getBlock().getMaterial() != Material.air) {
            p_180533_2_ = p_180533_2_.getBlock().getActualState(p_180533_2_, this.worldObj, p_180533_1_);
            final byte var3 = 4;
            for (int var4 = 0; var4 < var3; ++var4) {
                for (int var5 = 0; var5 < var3; ++var5) {
                    for (int var6 = 0; var6 < var3; ++var6) {
                        final double var7 = p_180533_1_.getX() + (var4 + 0.5) / var3;
                        final double var8 = p_180533_1_.getY() + (var5 + 0.5) / var3;
                        final double var9 = p_180533_1_.getZ() + (var6 + 0.5) / var3;
                        this.addEffect(new EntityDiggingFX(this.worldObj, var7, var8, var9, var7 - p_180533_1_.getX() - 0.5, var8 - p_180533_1_.getY() - 0.5, var9 - p_180533_1_.getZ() - 0.5, p_180533_2_).func_174846_a(p_180533_1_));
                    }
                }
            }
        }
    }
    
    public void func_180532_a(final BlockPos p_180532_1_, final EnumFacing p_180532_2_) {
        final IBlockState var3 = this.worldObj.getBlockState(p_180532_1_);
        final Block var4 = var3.getBlock();
        if (var4.getRenderType() != -1) {
            final int var5 = p_180532_1_.getX();
            final int var6 = p_180532_1_.getY();
            final int var7 = p_180532_1_.getZ();
            final float var8 = 0.1f;
            double var9 = var5 + this.rand.nextDouble() * (var4.getBlockBoundsMaxX() - var4.getBlockBoundsMinX() - var8 * 2.0f) + var8 + var4.getBlockBoundsMinX();
            double var10 = var6 + this.rand.nextDouble() * (var4.getBlockBoundsMaxY() - var4.getBlockBoundsMinY() - var8 * 2.0f) + var8 + var4.getBlockBoundsMinY();
            double var11 = var7 + this.rand.nextDouble() * (var4.getBlockBoundsMaxZ() - var4.getBlockBoundsMinZ() - var8 * 2.0f) + var8 + var4.getBlockBoundsMinZ();
            if (p_180532_2_ == EnumFacing.DOWN) {
                var10 = var6 + var4.getBlockBoundsMinY() - var8;
            }
            if (p_180532_2_ == EnumFacing.UP) {
                var10 = var6 + var4.getBlockBoundsMaxY() + var8;
            }
            if (p_180532_2_ == EnumFacing.NORTH) {
                var11 = var7 + var4.getBlockBoundsMinZ() - var8;
            }
            if (p_180532_2_ == EnumFacing.SOUTH) {
                var11 = var7 + var4.getBlockBoundsMaxZ() + var8;
            }
            if (p_180532_2_ == EnumFacing.WEST) {
                var9 = var5 + var4.getBlockBoundsMinX() - var8;
            }
            if (p_180532_2_ == EnumFacing.EAST) {
                var9 = var5 + var4.getBlockBoundsMaxX() + var8;
            }
            this.addEffect(new EntityDiggingFX(this.worldObj, var9, var10, var11, 0.0, 0.0, 0.0, var3).func_174846_a(p_180532_1_).multiplyVelocity(0.2f).multipleParticleScaleBy(0.6f));
        }
    }
    
    public void func_178928_b(final EntityFX p_178928_1_) {
        this.func_178924_a(p_178928_1_, 1, 0);
    }
    
    public void func_178931_c(final EntityFX p_178931_1_) {
        this.func_178924_a(p_178931_1_, 0, 1);
    }
    
    private void func_178924_a(final EntityFX p_178924_1_, final int p_178924_2_, final int p_178924_3_) {
        for (int var4 = 0; var4 < 4; ++var4) {
            if (this.fxLayers[var4][p_178924_2_].contains(p_178924_1_)) {
                this.fxLayers[var4][p_178924_2_].remove(p_178924_1_);
                this.fxLayers[var4][p_178924_3_].add(p_178924_1_);
            }
        }
    }
    
    public String getStatistics() {
        int var1 = 0;
        for (int var2 = 0; var2 < 4; ++var2) {
            for (int var3 = 0; var3 < 2; ++var3) {
                var1 += this.fxLayers[var2][var3].size();
            }
        }
        return "" + var1;
    }
    
    static {
        particleTextures = new ResourceLocation("textures/particle/particles.png");
    }
}
