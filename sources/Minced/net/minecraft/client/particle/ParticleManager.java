// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.optifine.reflect.Reflector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReport;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import com.google.common.collect.Lists;
import net.minecraft.src.Config;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.Map;
import java.util.Random;
import net.minecraft.client.renderer.texture.TextureManager;
import java.util.Queue;
import java.util.ArrayDeque;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;

public class ParticleManager
{
    private static final ResourceLocation PARTICLE_TEXTURES;
    protected World worldObj;
    private final ArrayDeque<Particle>[][] fxLayers;
    private final Queue<ParticleEmitter> particleEmitters;
    private final TextureManager renderer;
    private final Random rand;
    private final Map<Integer, IParticleFactory> particleTypes;
    private final Queue<Particle> queueEntityFX;
    
    public ParticleManager(final World worldIn, final TextureManager rendererIn) {
        this.fxLayers = (ArrayDeque<Particle>[][])new ArrayDeque[4][];
        this.particleEmitters = (Queue<ParticleEmitter>)Queues.newArrayDeque();
        this.rand = new Random();
        this.particleTypes = (Map<Integer, IParticleFactory>)Maps.newHashMap();
        this.queueEntityFX = (Queue<Particle>)Queues.newArrayDeque();
        this.worldObj = worldIn;
        this.renderer = rendererIn;
        for (int i = 0; i < 4; ++i) {
            this.fxLayers[i] = (ArrayDeque<Particle>[])new ArrayDeque[2];
            for (int j = 0; j < 2; ++j) {
                this.fxLayers[i][j] = (ArrayDeque<Particle>)Queues.newArrayDeque();
            }
        }
        this.registerVanillaParticles();
    }
    
    private void registerVanillaParticles() {
        this.registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new ParticleExplosion.Factory());
        this.registerParticle(EnumParticleTypes.SPIT.getParticleID(), new ParticleSpit.Factory());
        this.registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new ParticleBubble.Factory());
        this.registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new ParticleSplash.Factory());
        this.registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new ParticleWaterWake.Factory());
        this.registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new ParticleRain.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new ParticleSuspend.Factory());
        this.registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new ParticleSuspendedTown.Factory());
        this.registerParticle(EnumParticleTypes.CRIT.getParticleID(), new ParticleCrit.Factory());
        this.registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new ParticleCrit.MagicFactory());
        this.registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new ParticleSmokeNormal.Factory());
        this.registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new ParticleSmokeLarge.Factory());
        this.registerParticle(EnumParticleTypes.SPELL.getParticleID(), new ParticleSpell.Factory());
        this.registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new ParticleSpell.InstantFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new ParticleSpell.MobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new ParticleSpell.AmbientMobFactory());
        this.registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new ParticleSpell.WitchFactory());
        this.registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new ParticleDrip.WaterFactory());
        this.registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new ParticleDrip.LavaFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new ParticleHeart.AngryVillagerFactory());
        this.registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new ParticleSuspendedTown.HappyVillagerFactory());
        this.registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new ParticleSuspendedTown.Factory());
        this.registerParticle(EnumParticleTypes.NOTE.getParticleID(), new ParticleNote.Factory());
        this.registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new ParticlePortal.Factory());
        this.registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new ParticleEnchantmentTable.EnchantmentTable());
        this.registerParticle(EnumParticleTypes.FLAME.getParticleID(), new ParticleFlame.Factory());
        this.registerParticle(EnumParticleTypes.LAVA.getParticleID(), new ParticleLava.Factory());
        this.registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new ParticleFootStep.Factory());
        this.registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new ParticleCloud.Factory());
        this.registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new ParticleRedstone.Factory());
        this.registerParticle(EnumParticleTypes.FALLING_DUST.getParticleID(), new ParticleFallingDust.Factory());
        this.registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new ParticleBreaking.SnowballFactory());
        this.registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new ParticleSnowShovel.Factory());
        this.registerParticle(EnumParticleTypes.SLIME.getParticleID(), new ParticleBreaking.SlimeFactory());
        this.registerParticle(EnumParticleTypes.HEART.getParticleID(), new ParticleHeart.Factory());
        this.registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
        this.registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new ParticleBreaking.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new ParticleDigging.Factory());
        this.registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new ParticleBlockDust.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new ParticleExplosionHuge.Factory());
        this.registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new ParticleExplosionLarge.Factory());
        this.registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new ParticleFirework.Factory());
        this.registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new ParticleMobAppearance.Factory());
        this.registerParticle(EnumParticleTypes.DRAGON_BREATH.getParticleID(), new ParticleDragonBreath.Factory());
        this.registerParticle(EnumParticleTypes.END_ROD.getParticleID(), new ParticleEndRod.Factory());
        this.registerParticle(EnumParticleTypes.DAMAGE_INDICATOR.getParticleID(), new ParticleCrit.DamageIndicatorFactory());
        this.registerParticle(EnumParticleTypes.SWEEP_ATTACK.getParticleID(), new ParticleSweepAttack.Factory());
        this.registerParticle(EnumParticleTypes.TOTEM.getParticleID(), new ParticleTotem.Factory());
    }
    
    public void registerParticle(final int id, final IParticleFactory particleFactory) {
        this.particleTypes.put(id, particleFactory);
    }
    
    public void emitParticleAtEntity(final Entity entityIn, final EnumParticleTypes particleTypes) {
        this.particleEmitters.add(new ParticleEmitter(this.worldObj, entityIn, particleTypes));
    }
    
    public void emitParticleAtEntity(final Entity p_191271_1_, final EnumParticleTypes p_191271_2_, final int p_191271_3_) {
        this.particleEmitters.add(new ParticleEmitter(this.worldObj, p_191271_1_, p_191271_2_, p_191271_3_));
    }
    
    @Nullable
    public Particle spawnEffectParticle(final int particleId, final double xCoord, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed, final int... parameters) {
        final IParticleFactory iparticlefactory = this.particleTypes.get(particleId);
        if (iparticlefactory != null) {
            final Particle particle = iparticlefactory.createParticle(particleId, this.worldObj, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
            if (particle != null) {
                this.addEffect(particle);
                return particle;
            }
        }
        return null;
    }
    
    public void addEffect(final Particle effect) {
        if (effect != null && (!(effect instanceof ParticleFirework.Spark) || Config.isFireworkParticles())) {
            this.queueEntityFX.add(effect);
        }
    }
    
    public void updateEffects() {
        for (int i = 0; i < 4; ++i) {
            this.updateEffectLayer(i);
        }
        if (!this.particleEmitters.isEmpty()) {
            final List<ParticleEmitter> list = (List<ParticleEmitter>)Lists.newArrayList();
            for (final ParticleEmitter particleemitter : this.particleEmitters) {
                particleemitter.onUpdate();
                if (!particleemitter.isAlive()) {
                    list.add(particleemitter);
                }
            }
            this.particleEmitters.removeAll(list);
        }
        if (!this.queueEntityFX.isEmpty()) {
            for (Particle particle = this.queueEntityFX.poll(); particle != null; particle = this.queueEntityFX.poll()) {
                final int j = particle.getFXLayer();
                final int k = particle.shouldDisableDepth() ? 0 : 1;
                if (this.fxLayers[j][k].size() >= 16384) {
                    this.fxLayers[j][k].removeFirst();
                }
                if (!(particle instanceof Barrier) || !this.reuseBarrierParticle(particle, this.fxLayers[j][k])) {
                    this.fxLayers[j][k].add(particle);
                }
            }
        }
    }
    
    private void updateEffectLayer(final int layer) {
        this.worldObj.profiler.startSection(String.valueOf(layer));
        for (int i = 0; i < 2; ++i) {
            this.worldObj.profiler.startSection(String.valueOf(i));
            this.tickParticleList(this.fxLayers[layer][i]);
            this.worldObj.profiler.endSection();
        }
        this.worldObj.profiler.endSection();
    }
    
    private void tickParticleList(final Queue<Particle> p_187240_1_) {
        if (!p_187240_1_.isEmpty()) {
            final Iterator<Particle> iterator = p_187240_1_.iterator();
            while (iterator.hasNext()) {
                final Particle particle = iterator.next();
                this.tickParticle(particle);
                if (!particle.isAlive()) {
                    iterator.remove();
                }
            }
        }
    }
    
    private void tickParticle(final Particle particle) {
        try {
            particle.onUpdate();
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
            final int i = particle.getFXLayer();
            crashreportcategory.addDetail("Particle", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    return particle.toString();
                }
            });
            crashreportcategory.addDetail("Particle Type", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    if (i == 0) {
                        return "MISC_TEXTURE";
                    }
                    if (i == 1) {
                        return "TERRAIN_TEXTURE";
                    }
                    return (i == 3) ? "ENTITY_PARTICLE_TEXTURE" : ("Unknown - " + i);
                }
            });
            throw new ReportedException(crashreport);
        }
    }
    
    public void renderParticles(final Entity entityIn, final float partialTicks) {
        final float f = ActiveRenderInfo.getRotationX();
        final float f2 = ActiveRenderInfo.getRotationZ();
        final float f3 = ActiveRenderInfo.getRotationYZ();
        final float f4 = ActiveRenderInfo.getRotationXY();
        final float f5 = ActiveRenderInfo.getRotationXZ();
        Particle.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        Particle.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
        Particle.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
        Particle.cameraViewDir = entityIn.getLook(partialTicks);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(516, 0.003921569f);
        for (int i = 0; i < 3; ++i) {
            final int j = i;
            for (int k = 0; k < 2; ++k) {
                if (!this.fxLayers[j][k].isEmpty()) {
                    switch (k) {
                        case 0: {
                            GlStateManager.depthMask(false);
                            break;
                        }
                        case 1: {
                            GlStateManager.depthMask(true);
                            break;
                        }
                    }
                    switch (j) {
                        default: {
                            this.renderer.bindTexture(ParticleManager.PARTICLE_TEXTURES);
                            break;
                        }
                        case 1: {
                            this.renderer.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                            break;
                        }
                    }
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    final Tessellator tessellator = Tessellator.getInstance();
                    final BufferBuilder bufferbuilder = tessellator.getBuffer();
                    bufferbuilder.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
                    for (final Particle particle : this.fxLayers[j][k]) {
                        try {
                            particle.renderParticle(bufferbuilder, entityIn, partialTicks, f, f5, f2, f3, f4);
                        }
                        catch (Throwable throwable) {
                            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
                            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
                            crashreportcategory.addDetail("Particle", new ICrashReportDetail<String>() {
                                @Override
                                public String call() throws Exception {
                                    return particle.toString();
                                }
                            });
                            crashreportcategory.addDetail("Particle Type", new ICrashReportDetail<String>() {
                                @Override
                                public String call() throws Exception {
                                    if (j == 0) {
                                        return "MISC_TEXTURE";
                                    }
                                    if (j == 1) {
                                        return "TERRAIN_TEXTURE";
                                    }
                                    return (j == 3) ? "ENTITY_PARTICLE_TEXTURE" : ("Unknown - " + j);
                                }
                            });
                            throw new ReportedException(crashreport);
                        }
                    }
                    tessellator.draw();
                }
            }
        }
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.alphaFunc(516, 0.1f);
    }
    
    public void renderLitParticles(final Entity entityIn, final float partialTick) {
        final float f = 0.017453292f;
        final float f2 = MathHelper.cos(entityIn.rotationYaw * 0.017453292f);
        final float f3 = MathHelper.sin(entityIn.rotationYaw * 0.017453292f);
        final float f4 = -f3 * MathHelper.sin(entityIn.rotationPitch * 0.017453292f);
        final float f5 = f2 * MathHelper.sin(entityIn.rotationPitch * 0.017453292f);
        final float f6 = MathHelper.cos(entityIn.rotationPitch * 0.017453292f);
        for (int i = 0; i < 2; ++i) {
            final Queue<Particle> queue = this.fxLayers[3][i];
            if (!queue.isEmpty()) {
                final Tessellator tessellator = Tessellator.getInstance();
                final BufferBuilder bufferbuilder = tessellator.getBuffer();
                for (final Particle particle : queue) {
                    particle.renderParticle(bufferbuilder, entityIn, partialTick, f2, f6, f3, f4, f5);
                }
            }
        }
    }
    
    public void clearEffects(@Nullable final World worldIn) {
        this.worldObj = worldIn;
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.fxLayers[i][j].clear();
            }
        }
        this.particleEmitters.clear();
    }
    
    public void addBlockDestroyEffects(final BlockPos pos, IBlockState state) {
        boolean flag;
        if (Reflector.ForgeBlock_addDestroyEffects.exists() && Reflector.ForgeBlock_isAir.exists()) {
            final Block block = state.getBlock();
            flag = (!Reflector.callBoolean(block, Reflector.ForgeBlock_isAir, state, this.worldObj, pos) && !Reflector.callBoolean(block, Reflector.ForgeBlock_addDestroyEffects, this.worldObj, pos, this));
        }
        else {
            flag = (state.getMaterial() != Material.AIR);
        }
        if (flag) {
            state = state.getActualState(this.worldObj, pos);
            final int l = 4;
            for (int i = 0; i < 4; ++i) {
                for (int j = 0; j < 4; ++j) {
                    for (int k = 0; k < 4; ++k) {
                        final double d0 = (i + 0.5) / 4.0;
                        final double d2 = (j + 0.5) / 4.0;
                        final double d3 = (k + 0.5) / 4.0;
                        this.addEffect(new ParticleDigging(this.worldObj, pos.getX() + d0, pos.getY() + d2, pos.getZ() + d3, d0 - 0.5, d2 - 0.5, d3 - 0.5, state).setBlockPos(pos));
                    }
                }
            }
        }
    }
    
    public void addBlockHitEffects(final BlockPos pos, final EnumFacing side) {
        final IBlockState iblockstate = this.worldObj.getBlockState(pos);
        if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
            final int i = pos.getX();
            final int j = pos.getY();
            final int k = pos.getZ();
            final float f = 0.1f;
            final AxisAlignedBB axisalignedbb = iblockstate.getBoundingBox(this.worldObj, pos);
            double d0 = i + this.rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224) + 0.10000000149011612 + axisalignedbb.minX;
            double d2 = j + this.rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224) + 0.10000000149011612 + axisalignedbb.minY;
            double d3 = k + this.rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224) + 0.10000000149011612 + axisalignedbb.minZ;
            if (side == EnumFacing.DOWN) {
                d2 = j + axisalignedbb.minY - 0.10000000149011612;
            }
            if (side == EnumFacing.UP) {
                d2 = j + axisalignedbb.maxY + 0.10000000149011612;
            }
            if (side == EnumFacing.NORTH) {
                d3 = k + axisalignedbb.minZ - 0.10000000149011612;
            }
            if (side == EnumFacing.SOUTH) {
                d3 = k + axisalignedbb.maxZ + 0.10000000149011612;
            }
            if (side == EnumFacing.WEST) {
                d0 = i + axisalignedbb.minX - 0.10000000149011612;
            }
            if (side == EnumFacing.EAST) {
                d0 = i + axisalignedbb.maxX + 0.10000000149011612;
            }
            this.addEffect(new ParticleDigging(this.worldObj, d0, d2, d3, 0.0, 0.0, 0.0, iblockstate).setBlockPos(pos).multiplyVelocity(0.2f).multipleParticleScaleBy(0.6f));
        }
    }
    
    public String getStatistics() {
        int i = 0;
        for (int j = 0; j < 4; ++j) {
            for (int k = 0; k < 2; ++k) {
                i += this.fxLayers[j][k].size();
            }
        }
        return "" + i;
    }
    
    private boolean reuseBarrierParticle(final Particle p_reuseBarrierParticle_1_, final ArrayDeque<Particle> p_reuseBarrierParticle_2_) {
        for (final Particle particle : p_reuseBarrierParticle_2_) {
            if (particle instanceof Barrier && p_reuseBarrierParticle_1_.prevPosX == particle.prevPosX && p_reuseBarrierParticle_1_.prevPosY == particle.prevPosY && p_reuseBarrierParticle_1_.prevPosZ == particle.prevPosZ) {
                particle.particleAge = 0;
                return true;
            }
        }
        return false;
    }
    
    public void addBlockHitEffects(final BlockPos p_addBlockHitEffects_1_, final RayTraceResult p_addBlockHitEffects_2_) {
        final IBlockState iblockstate = this.worldObj.getBlockState(p_addBlockHitEffects_1_);
        if (iblockstate != null) {
            final boolean flag = Reflector.callBoolean(iblockstate.getBlock(), Reflector.ForgeBlock_addHitEffects, iblockstate, this.worldObj, p_addBlockHitEffects_2_, this);
            if (iblockstate != null && !flag) {
                this.addBlockHitEffects(p_addBlockHitEffects_1_, p_addBlockHitEffects_2_.sideHit);
            }
        }
    }
    
    static {
        PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
    }
}
