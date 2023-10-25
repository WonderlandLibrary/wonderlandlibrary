/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
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
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EffectRenderer {
    private Map<Integer, IParticleFactory> particleTypes;
    protected World worldObj;
    private List<EntityFX>[][] fxLayers = new List[4][];
    private Random rand;
    private List<EntityParticleEmitter> particleEmitters = Lists.newArrayList();
    private TextureManager renderer;
    private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");

    public void updateEffects() {
        int n = 0;
        while (n < 4) {
            this.updateEffectLayer(n);
            ++n;
        }
        ArrayList arrayList = Lists.newArrayList();
        for (EntityParticleEmitter entityParticleEmitter : this.particleEmitters) {
            entityParticleEmitter.onUpdate();
            if (!entityParticleEmitter.isDead) continue;
            arrayList.add(entityParticleEmitter);
        }
        this.particleEmitters.removeAll(arrayList);
    }

    public void clearEffects(World world) {
        this.worldObj = world;
        int n = 0;
        while (n < 4) {
            int n2 = 0;
            while (n2 < 2) {
                this.fxLayers[n][n2].clear();
                ++n2;
            }
            ++n;
        }
        this.particleEmitters.clear();
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

    public EffectRenderer(World world, TextureManager textureManager) {
        this.rand = new Random();
        this.particleTypes = Maps.newHashMap();
        this.worldObj = world;
        this.renderer = textureManager;
        int n = 0;
        while (n < 4) {
            this.fxLayers[n] = new List[2];
            int n2 = 0;
            while (n2 < 2) {
                this.fxLayers[n][n2] = Lists.newArrayList();
                ++n2;
            }
            ++n;
        }
        this.registerVanillaParticles();
    }

    private void tickParticle(final EntityFX entityFX) {
        try {
            entityFX.onUpdate();
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Particle being ticked");
            final int n = entityFX.getFXLayer();
            crashReportCategory.addCrashSectionCallable("Particle", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return entityFX.toString();
                }
            });
            crashReportCategory.addCrashSectionCallable("Particle Type", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return n == 0 ? "MISC_TEXTURE" : (n == 1 ? "TERRAIN_TEXTURE" : (n == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + n));
                }
            });
            throw new ReportedException(crashReport);
        }
    }

    private void moveToLayer(EntityFX entityFX, int n, int n2) {
        int n3 = 0;
        while (n3 < 4) {
            if (this.fxLayers[n3][n].contains(entityFX)) {
                this.fxLayers[n3][n].remove(entityFX);
                this.fxLayers[n3][n2].add(entityFX);
            }
            ++n3;
        }
    }

    public void moveToAlphaLayer(EntityFX entityFX) {
        this.moveToLayer(entityFX, 1, 0);
    }

    public void moveToNoAlphaLayer(EntityFX entityFX) {
        this.moveToLayer(entityFX, 0, 1);
    }

    public String getStatistics() {
        int n = 0;
        int n2 = 0;
        while (n2 < 4) {
            int n3 = 0;
            while (n3 < 2) {
                n += this.fxLayers[n2][n3].size();
                ++n3;
            }
            ++n2;
        }
        return "" + n;
    }

    public void emitParticleAtEntity(Entity entity, EnumParticleTypes enumParticleTypes) {
        this.particleEmitters.add(new EntityParticleEmitter(this.worldObj, entity, enumParticleTypes));
    }

    private void updateEffectLayer(int n) {
        int n2 = 0;
        while (n2 < 2) {
            this.updateEffectAlphaLayer(this.fxLayers[n][n2]);
            ++n2;
        }
    }

    private void updateEffectAlphaLayer(List<EntityFX> list) {
        ArrayList arrayList = Lists.newArrayList();
        int n = 0;
        while (n < list.size()) {
            EntityFX entityFX = list.get(n);
            this.tickParticle(entityFX);
            if (entityFX.isDead) {
                arrayList.add(entityFX);
            }
            ++n;
        }
        list.removeAll(arrayList);
    }

    public void addEffect(EntityFX entityFX) {
        int n;
        int n2 = entityFX.getFXLayer();
        int n3 = n = entityFX.getAlpha() != 1.0f ? 0 : 1;
        if (this.fxLayers[n2][n].size() >= 4000) {
            this.fxLayers[n2][n].remove(0);
        }
        this.fxLayers[n2][n].add(entityFX);
    }

    public void renderParticles(Entity entity, float f) {
        float f2 = ActiveRenderInfo.getRotationX();
        float f3 = ActiveRenderInfo.getRotationZ();
        float f4 = ActiveRenderInfo.getRotationYZ();
        float f5 = ActiveRenderInfo.getRotationXY();
        float f6 = ActiveRenderInfo.getRotationXZ();
        EntityFX.interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)f;
        EntityFX.interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)f;
        EntityFX.interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)f;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.003921569f);
        int n = 0;
        while (n < 3) {
            int n2 = 0;
            while (n2 < 2) {
                final int n3 = n;
                if (!this.fxLayers[n][n2].isEmpty()) {
                    switch (n2) {
                        case 0: {
                            GlStateManager.depthMask(false);
                            break;
                        }
                        case 1: {
                            GlStateManager.depthMask(true);
                        }
                    }
                    switch (n) {
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
                    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                    worldRenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                    int n4 = 0;
                    while (n4 < this.fxLayers[n][n2].size()) {
                        final EntityFX entityFX = this.fxLayers[n][n2].get(n4);
                        try {
                            entityFX.renderParticle(worldRenderer, entity, f, f2, f6, f3, f4, f5);
                        }
                        catch (Throwable throwable) {
                            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                            CrashReportCategory crashReportCategory = crashReport.makeCategory("Particle being rendered");
                            crashReportCategory.addCrashSectionCallable("Particle", new Callable<String>(){

                                @Override
                                public String call() throws Exception {
                                    return entityFX.toString();
                                }
                            });
                            crashReportCategory.addCrashSectionCallable("Particle Type", new Callable<String>(){

                                @Override
                                public String call() throws Exception {
                                    return n3 == 0 ? "MISC_TEXTURE" : (n3 == 1 ? "TERRAIN_TEXTURE" : (n3 == 3 ? "ENTITY_PARTICLE_TEXTURE" : "Unknown - " + n3));
                                }
                            });
                            throw new ReportedException(crashReport);
                        }
                        ++n4;
                    }
                    tessellator.draw();
                }
                ++n2;
            }
            ++n;
        }
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1f);
    }

    public EntityFX spawnEffectParticle(int n, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
        EntityFX entityFX;
        IParticleFactory iParticleFactory = this.particleTypes.get(n);
        if (iParticleFactory != null && (entityFX = iParticleFactory.getEntityFX(n, this.worldObj, d, d2, d3, d4, d5, d6, nArray)) != null) {
            this.addEffect(entityFX);
            return entityFX;
        }
        return null;
    }

    public void renderLitParticles(Entity entity, float f) {
        float f2 = (float)Math.PI / 180;
        float f3 = MathHelper.cos(entity.rotationYaw * ((float)Math.PI / 180));
        float f4 = MathHelper.sin(entity.rotationYaw * ((float)Math.PI / 180));
        float f5 = -f4 * MathHelper.sin(entity.rotationPitch * ((float)Math.PI / 180));
        float f6 = f3 * MathHelper.sin(entity.rotationPitch * ((float)Math.PI / 180));
        float f7 = MathHelper.cos(entity.rotationPitch * ((float)Math.PI / 180));
        int n = 0;
        while (n < 2) {
            List<EntityFX> list = this.fxLayers[3][n];
            if (!list.isEmpty()) {
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                int n2 = 0;
                while (n2 < list.size()) {
                    EntityFX entityFX = list.get(n2);
                    entityFX.renderParticle(worldRenderer, entity, f, f3, f7, f4, f5, f6);
                    ++n2;
                }
            }
            ++n;
        }
    }

    public void registerParticle(int n, IParticleFactory iParticleFactory) {
        this.particleTypes.put(n, iParticleFactory);
    }

    public void addBlockDestroyEffects(BlockPos blockPos, IBlockState iBlockState) {
        if (iBlockState.getBlock().getMaterial() != Material.air) {
            iBlockState = iBlockState.getBlock().getActualState(iBlockState, this.worldObj, blockPos);
            int n = 4;
            int n2 = 0;
            while (n2 < n) {
                int n3 = 0;
                while (n3 < n) {
                    int n4 = 0;
                    while (n4 < n) {
                        double d = (double)blockPos.getX() + ((double)n2 + 0.5) / (double)n;
                        double d2 = (double)blockPos.getY() + ((double)n3 + 0.5) / (double)n;
                        double d3 = (double)blockPos.getZ() + ((double)n4 + 0.5) / (double)n;
                        this.addEffect(new EntityDiggingFX(this.worldObj, d, d2, d3, d - (double)blockPos.getX() - 0.5, d2 - (double)blockPos.getY() - 0.5, d3 - (double)blockPos.getZ() - 0.5, iBlockState).func_174846_a(blockPos));
                        ++n4;
                    }
                    ++n3;
                }
                ++n2;
            }
        }
    }

    public void addBlockHitEffects(BlockPos blockPos, EnumFacing enumFacing) {
        IBlockState iBlockState = this.worldObj.getBlockState(blockPos);
        Block block = iBlockState.getBlock();
        if (block.getRenderType() != -1) {
            int n = blockPos.getX();
            int n2 = blockPos.getY();
            int n3 = blockPos.getZ();
            float f = 0.1f;
            double d = (double)n + this.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double)(f * 2.0f)) + (double)f + block.getBlockBoundsMinX();
            double d2 = (double)n2 + this.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double)(f * 2.0f)) + (double)f + block.getBlockBoundsMinY();
            double d3 = (double)n3 + this.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double)(f * 2.0f)) + (double)f + block.getBlockBoundsMinZ();
            if (enumFacing == EnumFacing.DOWN) {
                d2 = (double)n2 + block.getBlockBoundsMinY() - (double)f;
            }
            if (enumFacing == EnumFacing.UP) {
                d2 = (double)n2 + block.getBlockBoundsMaxY() + (double)f;
            }
            if (enumFacing == EnumFacing.NORTH) {
                d3 = (double)n3 + block.getBlockBoundsMinZ() - (double)f;
            }
            if (enumFacing == EnumFacing.SOUTH) {
                d3 = (double)n3 + block.getBlockBoundsMaxZ() + (double)f;
            }
            if (enumFacing == EnumFacing.WEST) {
                d = (double)n + block.getBlockBoundsMinX() - (double)f;
            }
            if (enumFacing == EnumFacing.EAST) {
                d = (double)n + block.getBlockBoundsMaxX() + (double)f;
            }
            this.addEffect(new EntityDiggingFX(this.worldObj, d, d2, d3, 0.0, 0.0, 0.0, iBlockState).func_174846_a(blockPos).multiplyVelocity(0.2f).multipleParticleScaleBy(0.6f));
        }
    }
}

