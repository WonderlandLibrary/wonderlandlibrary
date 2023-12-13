/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Barrier;
import net.minecraft.client.particle.EntityAuraFX;
import net.minecraft.client.particle.EntityBlockDustFX;
import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.client.particle.EntityBubbleFX;
import net.minecraft.client.particle.EntityCloudFX;
import net.minecraft.client.particle.EntityCrit2FX;
import net.minecraft.client.particle.EntityCritFX;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityDropParticleFX;
import net.minecraft.client.particle.EntityEnchantmentTableParticleFX;
import net.minecraft.client.particle.EntityExplodeFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.EntityFirework;
import net.minecraft.client.particle.EntityFishWakeFX;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.client.particle.EntityFootStepFX;
import net.minecraft.client.particle.EntityHeartFX;
import net.minecraft.client.particle.EntityHugeExplodeFX;
import net.minecraft.client.particle.EntityLargeExplodeFX;
import net.minecraft.client.particle.EntityLavaFX;
import net.minecraft.client.particle.EntityNoteFX;
import net.minecraft.client.particle.EntityParticleEmitter;
import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.client.particle.EntityRainFX;
import net.minecraft.client.particle.EntityReddustFX;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.particle.EntitySnowShovelFX;
import net.minecraft.client.particle.EntitySpellParticleFX;
import net.minecraft.client.particle.EntitySplashFX;
import net.minecraft.client.particle.EntitySuspendFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.MobAppearance;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import optfine.Config;
import optfine.Reflector;

public class EffectRenderer {
    private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
    protected World worldObj;
    private List[][] fxLayers = new List[4][];
    private List particleEmitters = Lists.newArrayList();
    private TextureManager renderer;
    private Random rand = new Random();
    private Map particleTypes = Maps.newHashMap();
    private static final String __OBFID = "CL_00000915";

    public EffectRenderer(World worldIn, TextureManager rendererIn) {
        this.worldObj = worldIn;
        this.renderer = rendererIn;
        for (int i2 = 0; i2 < 4; ++i2) {
            this.fxLayers[i2] = new List[2];
            for (int j2 = 0; j2 < 2; ++j2) {
                this.fxLayers[i2][j2] = Lists.newArrayList();
            }
        }
        this.registerVanillaParticles();
    }

    private void registerVanillaParticles() {
        this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new EntityExplodeFX.Factory());
        this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new EntityBubbleFX.Factory());
        this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new EntitySplashFX.Factory());
        this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new EntityFishWakeFX.Factory());
        this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new EntityRainFX.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new EntitySuspendFX.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new EntityAuraFX.Factory());
        this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), new EntityCrit2FX.Factory());
        this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new EntityCrit2FX.MagicFactory());
        this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new EntitySmokeFX.Factory());
        this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new EntityCritFX.Factory());
        this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), new EntitySpellParticleFX.Factory());
        this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new EntitySpellParticleFX.InstantFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new EntitySpellParticleFX.MobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new EntitySpellParticleFX.AmbientMobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new EntitySpellParticleFX.WitchFactory());
        this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new EntityDropParticleFX.WaterFactory());
        this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new EntityDropParticleFX.LavaFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new EntityHeartFX.AngryVillagerFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new EntityAuraFX.HappyVillagerFactory());
        this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new EntityAuraFX.Factory());
        this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), new EntityNoteFX.Factory());
        this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new EntityPortalFX.Factory());
        this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new EntityEnchantmentTableParticleFX.EnchantmentTable());
        this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), new EntityFlameFX.Factory());
        this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), new EntityLavaFX.Factory());
        this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new EntityFootStepFX.Factory());
        this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new EntityCloudFX.Factory());
        this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new EntityReddustFX.Factory());
        this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new EntityBreakingFX.SnowballFactory());
        this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new EntitySnowShovelFX.Factory());
        this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), new EntityBreakingFX.SlimeFactory());
        this.registerParticle(EnumParticleTypes.HEART.getParticleID(), new EntityHeartFX.Factory());
        this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
        this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new EntityBreakingFX.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new EntityDiggingFX.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new EntityBlockDustFX.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new EntityHugeExplodeFX.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new EntityLargeExplodeFX.Factory());
        this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new EntityFirework.Factory());
        this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new MobAppearance.Factory());
    }

    public void registerParticle(int id, IParticleFactory particleFactory) {
        this.particleTypes.put(id, particleFactory);
    }

    public void emitParticleAtEntity(Entity entityIn, EnumParticleTypes particleTypes) {
        this.particleEmitters.add(new EntityParticleEmitter(this.worldObj, entityIn, particleTypes));
    }

    public EntityFX spawnEffectParticle(int particleId, double p_178927_2_, double p_178927_4_, double p_178927_6_, double p_178927_8_, double p_178927_10_, double p_178927_12_, int ... p_178927_14_) {
        EntityFX entityfx;
        IParticleFactory iparticlefactory = (IParticleFactory)this.particleTypes.get(particleId);
        if (iparticlefactory != null && (entityfx = iparticlefactory.getEntityFX(particleId, this.worldObj, p_178927_2_, p_178927_4_, p_178927_6_, p_178927_8_, p_178927_10_, p_178927_12_, p_178927_14_)) != null) {
            this.addEffect(entityfx);
            return entityfx;
        }
        return null;
    }

    public void addEffect(EntityFX effect) {
        if (effect != null && (!(effect instanceof EntityFirework.SparkFX) || Config.isFireworkParticles())) {
            int j2;
            int i2 = effect.getFXLayer();
            int n2 = j2 = effect.getAlpha() != 1.0f ? 0 : 1;
            if (this.fxLayers[i2][j2].size() >= 4000) {
                this.fxLayers[i2][j2].remove(0);
            }
            this.fxLayers[i2][j2].add(effect);
        }
    }

    public void updateEffects() {
        for (int i2 = 0; i2 < 4; ++i2) {
            this.updateEffectLayer(i2);
        }
        ArrayList<EntityParticleEmitter> arraylist = Lists.newArrayList();
        for (Object entityparticleemitter0 : this.particleEmitters) {
            EntityParticleEmitter entityparticleemitter = (EntityParticleEmitter)entityparticleemitter0;
            entityparticleemitter.onUpdate();
            if (!entityparticleemitter.isDead) continue;
            arraylist.add(entityparticleemitter);
        }
        this.particleEmitters.removeAll(arraylist);
    }

    private void updateEffectLayer(int p_178922_1_) {
        for (int i2 = 0; i2 < 2; ++i2) {
            this.updateEffectAlphaLayer(this.fxLayers[p_178922_1_][i2]);
        }
    }

    private void updateEffectAlphaLayer(List p_178925_1_) {
        ArrayList<EntityFX> arraylist = Lists.newArrayList();
        for (int i2 = 0; i2 < p_178925_1_.size(); ++i2) {
            EntityFX entityfx = (EntityFX)p_178925_1_.get(i2);
            this.tickParticle(entityfx);
            if (!entityfx.isDead) continue;
            arraylist.add(entityfx);
        }
        p_178925_1_.removeAll(arraylist);
    }

    private void tickParticle(final EntityFX p_178923_1_) {
        try {
            p_178923_1_.onUpdate();
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
            final int i2 = p_178923_1_.getFXLayer();
            crashreportcategory.addCrashSectionCallable("Particle", new Callable(){
                private static final String __OBFID = "CL_00000916";

                public String call() throws Exception {
                    return p_178923_1_.toString();
                }
            });
            crashreportcategory.addCrashSectionCallable("Particle Type", new Callable(){
                private static final String __OBFID = "CL_00000917";

                public String call() throws Exception {
                    return i2 == 0 ? "MISC_TEXTURE" : (i2 == 1 ? "TERRAIN_TEXTURE" : (i2 == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + i2));
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    public void renderParticles(Entity entityIn, float partialTicks) {
        float f2 = ActiveRenderInfo.getRotationX();
        float f1 = ActiveRenderInfo.getRotationZ();
        float f22 = ActiveRenderInfo.getRotationYZ();
        float f3 = ActiveRenderInfo.getRotationXY();
        float f4 = ActiveRenderInfo.getRotationXZ();
        EntityFX.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        EntityFX.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        EntityFX.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.003921569f);
        for (int i2 = 0; i2 < 3; ++i2) {
            final int j2 = i2;
            for (int k2 = 0; k2 < 2; ++k2) {
                if (this.fxLayers[j2][k2].isEmpty()) continue;
                switch (k2) {
                    case 0: {
                        GlStateManager.depthMask(false);
                        break;
                    }
                    case 1: {
                        GlStateManager.depthMask(true);
                    }
                }
                switch (j2) {
                    default: {
                        this.renderer.bindTexture(particleTextures);
                        break;
                    }
                    case 1: {
                        this.renderer.bindTexture(TextureMap.locationBlocksTexture);
                    }
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                for (int l2 = 0; l2 < this.fxLayers[j2][k2].size(); ++l2) {
                    final EntityFX entityfx = (EntityFX)this.fxLayers[j2][k2].get(l2);
                    try {
                        entityfx.renderParticle(worldrenderer, entityIn, partialTicks, f2, f4, f1, f22, f3);
                        continue;
                    }
                    catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                        CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                        crashreportcategory.addCrashSectionCallable("Particle", new Callable(){
                            private static final String __OBFID = "CL_00000918";

                            public String call() throws Exception {
                                return entityfx.toString();
                            }
                        });
                        crashreportcategory.addCrashSectionCallable("Particle Type", new Callable(){
                            private static final String __OBFID = "CL_00000919";

                            public String call() throws Exception {
                                return j2 == 0 ? "MISC_TEXTURE" : (j2 == 1 ? "TERRAIN_TEXTURE" : (j2 == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + j2));
                            }
                        });
                        throw new ReportedException(crashreport);
                    }
                }
                tessellator.draw();
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1f);
    }

    public void renderLitParticles(Entity entityIn, float p_78872_2_) {
        float f2 = (float)Math.PI / 180;
        float f1 = MathHelper.cos(entityIn.rotationYaw * ((float)Math.PI / 180));
        float f22 = MathHelper.sin(entityIn.rotationYaw * ((float)Math.PI / 180));
        float f3 = -f22 * MathHelper.sin(entityIn.rotationPitch * ((float)Math.PI / 180));
        float f4 = f1 * MathHelper.sin(entityIn.rotationPitch * ((float)Math.PI / 180));
        float f5 = MathHelper.cos(entityIn.rotationPitch * ((float)Math.PI / 180));
        for (int i2 = 0; i2 < 2; ++i2) {
            List list = this.fxLayers[3][i2];
            if (list.isEmpty()) continue;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            for (int j2 = 0; j2 < list.size(); ++j2) {
                EntityFX entityfx = (EntityFX)list.get(j2);
                entityfx.renderParticle(worldrenderer, entityIn, p_78872_2_, f1, f5, f22, f3, f4);
            }
        }
    }

    public void clearEffects(World worldIn) {
        this.worldObj = worldIn;
        for (int i2 = 0; i2 < 4; ++i2) {
            for (int j2 = 0; j2 < 2; ++j2) {
                this.fxLayers[i2][j2].clear();
            }
        }
        this.particleEmitters.clear();
    }

    public void addBlockDestroyEffects(BlockPos pos, IBlockState state) {
        boolean flag;
        if (Reflector.ForgeBlock_addDestroyEffects.exists() && Reflector.ForgeBlock_isAir.exists()) {
            Block block = state.getBlock();
            Reflector.callBoolean(block, Reflector.ForgeBlock_isAir, this.worldObj, pos);
            flag = !Reflector.callBoolean(block, Reflector.ForgeBlock_isAir, this.worldObj, pos) && !Reflector.callBoolean(block, Reflector.ForgeBlock_addDestroyEffects, this.worldObj, pos, this);
        } else {
            boolean bl = flag = state.getBlock().getMaterial() != Material.air;
        }
        if (flag) {
            state = state.getBlock().getActualState(state, this.worldObj, pos);
            int b0 = 4;
            for (int i2 = 0; i2 < b0; ++i2) {
                for (int j2 = 0; j2 < b0; ++j2) {
                    for (int k2 = 0; k2 < b0; ++k2) {
                        double d0 = (double)pos.getX() + ((double)i2 + 0.5) / (double)b0;
                        double d1 = (double)pos.getY() + ((double)j2 + 0.5) / (double)b0;
                        double d2 = (double)pos.getZ() + ((double)k2 + 0.5) / (double)b0;
                        this.addEffect(new EntityDiggingFX(this.worldObj, d0, d1, d2, d0 - (double)pos.getX() - 0.5, d1 - (double)pos.getY() - 0.5, d2 - (double)pos.getZ() - 0.5, state).func_174846_a(pos));
                    }
                }
            }
        }
    }

    public void addBlockHitEffects(BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = this.worldObj.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if (block.getRenderType() != -1) {
            int i2 = pos.getX();
            int j2 = pos.getY();
            int k2 = pos.getZ();
            float f2 = 0.1f;
            double d0 = (double)i2 + this.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double)(f2 * 2.0f)) + (double)f2 + block.getBlockBoundsMinX();
            double d1 = (double)j2 + this.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double)(f2 * 2.0f)) + (double)f2 + block.getBlockBoundsMinY();
            double d2 = (double)k2 + this.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double)(f2 * 2.0f)) + (double)f2 + block.getBlockBoundsMinZ();
            if (side == EnumFacing.DOWN) {
                d1 = (double)j2 + block.getBlockBoundsMinY() - (double)f2;
            }
            if (side == EnumFacing.UP) {
                d1 = (double)j2 + block.getBlockBoundsMaxY() + (double)f2;
            }
            if (side == EnumFacing.NORTH) {
                d2 = (double)k2 + block.getBlockBoundsMinZ() - (double)f2;
            }
            if (side == EnumFacing.SOUTH) {
                d2 = (double)k2 + block.getBlockBoundsMaxZ() + (double)f2;
            }
            if (side == EnumFacing.WEST) {
                d0 = (double)i2 + block.getBlockBoundsMinX() - (double)f2;
            }
            if (side == EnumFacing.EAST) {
                d0 = (double)i2 + block.getBlockBoundsMaxX() + (double)f2;
            }
            this.addEffect(new EntityDiggingFX(this.worldObj, d0, d1, d2, 0.0, 0.0, 0.0, iblockstate).func_174846_a(pos).multiplyVelocity(0.2f).multipleParticleScaleBy(0.6f));
        }
    }

    public void moveToAlphaLayer(EntityFX effect) {
        this.moveToLayer(effect, 1, 0);
    }

    public void moveToNoAlphaLayer(EntityFX effect) {
        this.moveToLayer(effect, 0, 1);
    }

    private void moveToLayer(EntityFX effect, int p_178924_2_, int p_178924_3_) {
        for (int i2 = 0; i2 < 4; ++i2) {
            if (!this.fxLayers[i2][p_178924_2_].contains(effect)) continue;
            this.fxLayers[i2][p_178924_2_].remove(effect);
            this.fxLayers[i2][p_178924_3_].add(effect);
        }
    }

    public String getStatistics() {
        int i2 = 0;
        for (int j2 = 0; j2 < 4; ++j2) {
            for (int k2 = 0; k2 < 2; ++k2) {
                i2 += this.fxLayers[j2][k2].size();
            }
        }
        return "" + i2;
    }

    public void addBlockHitEffects(BlockPos p_addBlockHitEffects_1_, MovingObjectPosition p_addBlockHitEffects_2_) {
        Block block = this.worldObj.getBlockState(p_addBlockHitEffects_1_).getBlock();
        boolean flag = Reflector.callBoolean(block, Reflector.ForgeBlock_addHitEffects, this.worldObj, p_addBlockHitEffects_2_, this);
        if (block != null && !flag) {
            this.addBlockHitEffects(p_addBlockHitEffects_1_, p_addBlockHitEffects_2_.sideHit);
        }
    }
}

