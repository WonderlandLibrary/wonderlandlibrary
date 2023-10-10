/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
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
    private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
    protected World worldObj;
    private List<EntityFX>[][] fxLayers = new List[4][];
    private List<EntityParticleEmitter> particleEmitters = Lists.newArrayList();
    private TextureManager renderer;
    private Random rand = new Random();
    private Map<Integer, IParticleFactory> particleTypes = Maps.newHashMap();

    public EffectRenderer(World worldIn, TextureManager rendererIn) {
        this.worldObj = worldIn;
        this.renderer = rendererIn;
        int i = 0;
        while (true) {
            if (i >= 4) {
                this.registerVanillaParticles();
                return;
            }
            this.fxLayers[i] = new List[2];
            for (int j = 0; j < 2; ++j) {
                this.fxLayers[i][j] = Lists.newArrayList();
            }
            ++i;
        }
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
        IParticleFactory iparticlefactory = this.particleTypes.get(particleId);
        if (iparticlefactory == null) return null;
        EntityFX entityfx = iparticlefactory.getEntityFX(particleId, this.worldObj, p_178927_2_, p_178927_4_, p_178927_6_, p_178927_8_, p_178927_10_, p_178927_12_, p_178927_14_);
        if (entityfx == null) return null;
        this.addEffect(entityfx);
        return entityfx;
    }

    public void addEffect(EntityFX effect) {
        int j;
        int i = effect.getFXLayer();
        int n = j = effect.getAlpha() != 1.0f ? 0 : 1;
        if (this.fxLayers[i][j].size() >= 4000) {
            this.fxLayers[i][j].remove(0);
        }
        this.fxLayers[i][j].add(effect);
    }

    public void updateEffects() {
        for (int i = 0; i < 4; ++i) {
            this.updateEffectLayer(i);
        }
        ArrayList<EntityParticleEmitter> list = Lists.newArrayList();
        Iterator<EntityParticleEmitter> iterator = this.particleEmitters.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.particleEmitters.removeAll(list);
                return;
            }
            EntityParticleEmitter entityparticleemitter = iterator.next();
            entityparticleemitter.onUpdate();
            if (!entityparticleemitter.isDead) continue;
            list.add(entityparticleemitter);
        }
    }

    private void updateEffectLayer(int p_178922_1_) {
        int i = 0;
        while (i < 2) {
            this.updateEffectAlphaLayer(this.fxLayers[p_178922_1_][i]);
            ++i;
        }
    }

    private void updateEffectAlphaLayer(List<EntityFX> p_178925_1_) {
        ArrayList<EntityFX> list = Lists.newArrayList();
        int i = 0;
        while (true) {
            if (i >= p_178925_1_.size()) {
                p_178925_1_.removeAll(list);
                return;
            }
            EntityFX entityfx = p_178925_1_.get(i);
            this.tickParticle(entityfx);
            if (entityfx.isDead) {
                list.add(entityfx);
            }
            ++i;
        }
    }

    private void tickParticle(final EntityFX p_178923_1_) {
        try {
            p_178923_1_.onUpdate();
            return;
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
            final int i = p_178923_1_.getFXLayer();
            crashreportcategory.addCrashSectionCallable("Particle", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return p_178923_1_.toString();
                }
            });
            crashreportcategory.addCrashSectionCallable("Particle Type", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    if (i == 0) {
                        return "MISC_TEXTURE";
                    }
                    if (i == 1) {
                        return "TERRAIN_TEXTURE";
                    }
                    if (i == 3) {
                        return "ENTITY_PARTICLE_TEXTURE";
                    }
                    String string = "Unknown - " + i;
                    return string;
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    public void renderParticles(Entity entityIn, float partialTicks) {
        float f = ActiveRenderInfo.getRotationX();
        float f1 = ActiveRenderInfo.getRotationZ();
        float f2 = ActiveRenderInfo.getRotationYZ();
        float f3 = ActiveRenderInfo.getRotationXY();
        float f4 = ActiveRenderInfo.getRotationXZ();
        EntityFX.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        EntityFX.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks;
        EntityFX.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.alphaFunc(516, 0.003921569f);
        int i = 0;
        block9: while (true) {
            if (i >= 3) {
                GlStateManager.depthMask(true);
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1f);
                return;
            }
            int j = 0;
            while (true) {
                block14: {
                    WorldRenderer worldrenderer;
                    Tessellator tessellator;
                    int i_f;
                    block15: {
                        block13: {
                            if (j >= 2) break block13;
                            i_f = i;
                            if (this.fxLayers[i][j].isEmpty()) break block14;
                            switch (j) {
                                case 0: {
                                    GlStateManager.depthMask(false);
                                    break;
                                }
                                case 1: {
                                    GlStateManager.depthMask(true);
                                    break;
                                }
                            }
                            switch (i) {
                                default: {
                                    this.renderer.bindTexture(particleTextures);
                                    break;
                                }
                                case 1: {
                                    this.renderer.bindTexture(TextureMap.locationBlocksTexture);
                                }
                            }
                            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                            tessellator = Tessellator.getInstance();
                            worldrenderer = tessellator.getWorldRenderer();
                            worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                            break block15;
                        }
                        ++i;
                        continue block9;
                    }
                    for (int k = 0; k < this.fxLayers[i][j].size(); ++k) {
                        final EntityFX entityfx = this.fxLayers[i][j].get(k);
                        try {
                            entityfx.renderParticle(worldrenderer, entityIn, partialTicks, f, f4, f1, f2, f3);
                            continue;
                        }
                        catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                            crashreportcategory.addCrashSectionCallable("Particle", new Callable<String>(){

                                @Override
                                public String call() throws Exception {
                                    return entityfx.toString();
                                }
                            });
                            crashreportcategory.addCrashSectionCallable("Particle Type", new Callable<String>(){

                                @Override
                                public String call() throws Exception {
                                    if (i_f == 0) {
                                        return "MISC_TEXTURE";
                                    }
                                    if (i_f == 1) {
                                        return "TERRAIN_TEXTURE";
                                    }
                                    if (i_f == 3) {
                                        return "ENTITY_PARTICLE_TEXTURE";
                                    }
                                    String string = "Unknown - " + i_f;
                                    return string;
                                }
                            });
                            throw new ReportedException(crashreport);
                        }
                    }
                    tessellator.draw();
                }
                ++j;
            }
            break;
        }
    }

    public void renderLitParticles(Entity entityIn, float p_78872_2_) {
        float f = (float)Math.PI / 180;
        float f1 = MathHelper.cos(entityIn.rotationYaw * ((float)Math.PI / 180));
        float f2 = MathHelper.sin(entityIn.rotationYaw * ((float)Math.PI / 180));
        float f3 = -f2 * MathHelper.sin(entityIn.rotationPitch * ((float)Math.PI / 180));
        float f4 = f1 * MathHelper.sin(entityIn.rotationPitch * ((float)Math.PI / 180));
        float f5 = MathHelper.cos(entityIn.rotationPitch * ((float)Math.PI / 180));
        int i = 0;
        while (i < 2) {
            List<EntityFX> list = this.fxLayers[3][i];
            if (!list.isEmpty()) {
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                for (int j = 0; j < list.size(); ++j) {
                    EntityFX entityfx = list.get(j);
                    entityfx.renderParticle(worldrenderer, entityIn, p_78872_2_, f1, f5, f2, f3, f4);
                }
            }
            ++i;
        }
    }

    public void clearEffects(World worldIn) {
        this.worldObj = worldIn;
        int i = 0;
        while (true) {
            if (i >= 4) {
                this.particleEmitters.clear();
                return;
            }
            for (int j = 0; j < 2; ++j) {
                this.fxLayers[i][j].clear();
            }
            ++i;
        }
    }

    public void addBlockDestroyEffects(BlockPos pos, IBlockState state) {
        if (state.getBlock().getMaterial() == Material.air) return;
        state = state.getBlock().getActualState(state, this.worldObj, pos);
        int i = 4;
        int j = 0;
        while (j < i) {
            for (int k = 0; k < i; ++k) {
                for (int l = 0; l < i; ++l) {
                    double d0 = (double)pos.getX() + ((double)j + 0.5) / (double)i;
                    double d1 = (double)pos.getY() + ((double)k + 0.5) / (double)i;
                    double d2 = (double)pos.getZ() + ((double)l + 0.5) / (double)i;
                    this.addEffect(new EntityDiggingFX(this.worldObj, d0, d1, d2, d0 - (double)pos.getX() - 0.5, d1 - (double)pos.getY() - 0.5, d2 - (double)pos.getZ() - 0.5, state).func_174846_a(pos));
                }
            }
            ++j;
        }
    }

    public void addBlockHitEffects(BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = this.worldObj.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if (block.getRenderType() == -1) return;
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        float f = 0.1f;
        double d0 = (double)i + this.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double)(f * 2.0f)) + (double)f + block.getBlockBoundsMinX();
        double d1 = (double)j + this.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double)(f * 2.0f)) + (double)f + block.getBlockBoundsMinY();
        double d2 = (double)k + this.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double)(f * 2.0f)) + (double)f + block.getBlockBoundsMinZ();
        if (side == EnumFacing.DOWN) {
            d1 = (double)j + block.getBlockBoundsMinY() - (double)f;
        }
        if (side == EnumFacing.UP) {
            d1 = (double)j + block.getBlockBoundsMaxY() + (double)f;
        }
        if (side == EnumFacing.NORTH) {
            d2 = (double)k + block.getBlockBoundsMinZ() - (double)f;
        }
        if (side == EnumFacing.SOUTH) {
            d2 = (double)k + block.getBlockBoundsMaxZ() + (double)f;
        }
        if (side == EnumFacing.WEST) {
            d0 = (double)i + block.getBlockBoundsMinX() - (double)f;
        }
        if (side == EnumFacing.EAST) {
            d0 = (double)i + block.getBlockBoundsMaxX() + (double)f;
        }
        this.addEffect(new EntityDiggingFX(this.worldObj, d0, d1, d2, 0.0, 0.0, 0.0, iblockstate).func_174846_a(pos).multiplyVelocity(0.2f).multipleParticleScaleBy(0.6f));
    }

    public void moveToAlphaLayer(EntityFX effect) {
        this.moveToLayer(effect, 1, 0);
    }

    public void moveToNoAlphaLayer(EntityFX effect) {
        this.moveToLayer(effect, 0, 1);
    }

    private void moveToLayer(EntityFX effect, int p_178924_2_, int p_178924_3_) {
        int i = 0;
        while (i < 4) {
            if (this.fxLayers[i][p_178924_2_].contains(effect)) {
                this.fxLayers[i][p_178924_2_].remove(effect);
                this.fxLayers[i][p_178924_3_].add(effect);
            }
            ++i;
        }
    }

    public String getStatistics() {
        int i = 0;
        int j = 0;
        while (j < 4) {
            for (int k = 0; k < 2; i += this.fxLayers[j][k].size(), ++k) {
            }
            ++j;
        }
        return "" + i;
    }
}

