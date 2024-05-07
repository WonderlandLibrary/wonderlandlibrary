package com.masterof13fps.manager.particlemanager.particle;

import com.masterof13fps.manager.particlemanager.FBP;
import com.masterof13fps.manager.particlemanager.ReflectionHelper;
import net.minecraft.block.state.*;
import java.lang.invoke.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.init.*;
import net.minecraft.entity.*;
import com.google.common.base.*;
import java.lang.reflect.*;
import net.minecraft.client.particle.*;
import net.minecraft.block.*;
import net.minecraft.world.*;
import net.minecraft.client.renderer.*;
import net.minecraft.util.*;

import java.util.*;

public class FBPParticleManager extends EffectRenderer
{
    private static MethodHandle getBlockDamage;
    private static MethodHandle getParticleScale;
    private static MethodHandle getParticleTexture;
    private static MethodHandle getParticleTypes;
    private static MethodHandle getSourceState;
    private static MethodHandle getParticleMaxAge;

    private static MethodHandle X, Y, Z;
    private static MethodHandle mX, mY, mZ;

    private static IParticleFactory particleFactory;
    private static IBlockState blockState;
    private static TextureAtlasSprite white;

    private static MethodHandles.Lookup lookup;

    Minecraft mc;

    public FBPParticleManager(final World worldIn, final TextureManager rendererIn, final IParticleFactory particleFactory) {
        super(worldIn, rendererIn);
        this.mc = Minecraft.getInstance();
        this.particleFactory = particleFactory;
        white = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.snow.getDefaultState());
        lookup = MethodHandles.publicLookup();
        try {
            getParticleTypes = lookup.unreflectGetter(ReflectionHelper.findField(EffectRenderer.class, "particleTypes", "particleTypes"));
            X = lookup.unreflectGetter(ReflectionHelper.findField(Entity.class, "posX", "posX"));
            Y = lookup.unreflectGetter(ReflectionHelper.findField(Entity.class, "posY", "posY"));
            Z = lookup.unreflectGetter(ReflectionHelper.findField(Entity.class, "posZ", "posZ"));
            mX = lookup.unreflectGetter(ReflectionHelper.findField(Entity.class, "motionX", "motionX"));
            mY = lookup.unreflectGetter(ReflectionHelper.findField(Entity.class, "motionY", "motionY"));
            mZ = lookup.unreflectGetter(ReflectionHelper.findField(Entity.class, "motionZ", "motionZ"));
            getParticleScale = lookup.unreflectGetter(ReflectionHelper.findField(EntityFX.class, "particleScale", "particleScale"));
            getParticleTexture = lookup.unreflectGetter(ReflectionHelper.findField(EntityFX.class, "particleIcon", "particleIcon"));
            getParticleMaxAge = lookup.unreflectGetter(ReflectionHelper.findField(EntityFX.class, "particleMaxAge", "particleMaxAge"));
            getSourceState = lookup.unreflectGetter(ReflectionHelper.findField(EntityDiggingFX.class, "sourceState"));
            getBlockDamage = lookup.unreflectGetter(ReflectionHelper.findField(RenderGlobal.class, "damagedBlocks", "damagedBlocks"));
        }
        catch (Throwable e) {
            throw Throwables.propagate(e);
        }
    }

    public void carryOver() {
        if (Minecraft.getInstance().effectRenderer == this) {
            return;
        }
        final Field f1 = ReflectionHelper.findField(EffectRenderer.class, "fxLayers", "fxLayers");
        final Field f2 = ReflectionHelper.findField(EffectRenderer.class, "particleEmitters", "particleEmitters");
        try {
            final MethodHandle getF1 = lookup.unreflectGetter(f1);
            final MethodHandle setF1 = lookup.unreflectSetter(f1);
            final MethodHandle getF2 = lookup.unreflectGetter(f2);
            final MethodHandle setF2 = lookup.unreflectSetter(f2);
            setF1.invokeExact((EffectRenderer)this, (List[][])getF1.invokeExact(this.mc.effectRenderer));
            setF2.invokeExact((EffectRenderer)this, getF2.invokeExact(this.mc.effectRenderer));
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addEffect(final EntityFX efx) {
        Entity toAdd;
        final Entity effect = toAdd = efx;
        if (toAdd != null && !(toAdd instanceof FBPParticleSnow) && !(toAdd instanceof FBPParticleRain)) {
            if (FBP.fancyFlame && toAdd instanceof EntityFlameFX && !(toAdd instanceof FBPParticleFlame) && Minecraft.getInstance().gameSettings.particleSetting < 2) {
                try {
                    toAdd = new FBPParticleFlame(worldObj, (double) X.invokeExact(effect),
                            (double) Y.invokeExact(effect), (double) Z.invokeExact(effect), 0,
                            FBP.random.nextDouble() * 0.25, 0, true);
                    effect.setDead();
                }
                catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            else if (FBP.fancySmoke && toAdd instanceof EntitySmokeFX && !(toAdd instanceof FBPParticleSmokeNormal) && Minecraft.getInstance().gameSettings.particleSetting < 2) {
                final EntitySmokeFX p = (EntitySmokeFX)effect;
                try {
                    toAdd = new FBPParticleSmokeNormal(worldObj, (double) X.invokeExact(effect),
                            (double) Y.invokeExact(effect), (double) Z.invokeExact(effect),
                            (double) mX.invokeExact(effect), (double) mY.invokeExact(effect),
                            (double) mZ.invokeExact(effect), (float) getParticleScale.invokeExact((EntityFX) effect),
                            true, white, p);
                }
                catch (Throwable t2) {
                    t2.printStackTrace();
                }
            }
            else if (FBP.fancyRain && toAdd instanceof EntityRainFX) {
                efx.setAlphaF(0.0f);
            }
            else if (toAdd instanceof EntityDiggingFX && !(toAdd instanceof FBPParticleDigging)) {
                try {
                    blockState = (IBlockState) getSourceState.invokeExact((EntityDiggingFX) effect);
                    if (blockState != null && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || blockState.getBlock() != Blocks.redstone_block)) {
                        effect.setDead();
                        if (blockState.getBlock() instanceof BlockLiquid || FBP.INSTANCE.isBlacklisted(blockState.getBlock())) {
                            return;
                        }
                        toAdd = new FBPParticleDigging(worldObj, (double) X.invokeExact(effect),
                                (double) Y.invokeExact(effect), (double) Z.invokeExact(effect), 0, 0, 0,
                                (float) getParticleScale.invokeExact((EntityFX) effect),
                                ((EntityFX) toAdd).getRedColorF(), ((EntityFX) toAdd).getGreenColorF(),
                                ((EntityFX) toAdd).getBlueColorF(), blockState, null,
                                (TextureAtlasSprite) getParticleTexture.invokeExact((EntityFX) effect));                    }
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            else if (toAdd instanceof FBPParticleDigging) {
                try {
                    blockState = (IBlockState) getSourceState.invokeExact((EntityDiggingFX) effect);
                    if (blockState != null && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || blockState.getBlock() != Blocks.redstone_block) && (blockState.getBlock() instanceof BlockLiquid || FBP.INSTANCE.isBlacklisted(blockState.getBlock()))) {
                        effect.setDead();
                        return;
                    }
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
        super.addEffect((EntityFX)toAdd);
    }

    @Override
    public EntityFX spawnEffectParticle(final int particleId, final double xCoord, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed, final int... parameters) {
        IParticleFactory iparticlefactory = null;
        try {
            iparticlefactory = ((Map<Integer, IParticleFactory>) getParticleTypes.invokeExact((EffectRenderer) this))
                    .get(Integer.valueOf(particleId));        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        if (iparticlefactory != null) {
            EntityFX toSpawn;
            final EntityFX particle = toSpawn = iparticlefactory.getEntityFX(particleId, this.mc.theWorld, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
            if (particle instanceof EntityDiggingFX && !(particle instanceof FBPParticleDigging)) {
                blockState = Block.getStateById(parameters[0]);
                if (blockState != null && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || blockState.getBlock() != Blocks.redstone_block)) {
                    if (!(blockState.getBlock() instanceof BlockLiquid) && !FBP.INSTANCE.isBlacklisted(blockState.getBlock())) {
                        toSpawn = new FBPParticleDigging(this.mc.theWorld, xCoord, yCoord + 0.10000000149011612, zCoord, xSpeed, ySpeed, zSpeed, -1.0f, 1.0f, 1.0f, 1.0f, blockState, null, null).func_174845_l().multipleParticleScaleBy(0.6f);
                    }
                    else {
                        toSpawn = particle;
                    }
                }
            }
            this.addEffect(toSpawn);
            return toSpawn;
        }
        return null;
    }

    @Override
    public void addBlockDestroyEffects(final BlockPos pos, IBlockState state) {
        Block b = state.getBlock();
        state = state.getBlock().getActualState(state, this.worldObj, pos);
        b = state.getBlock();
        final int i = 4;
        final TextureAtlasSprite texture = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
        for (int j = 0; j < FBP.particlesPerAxis; ++j) {
            for (int k = 0; k < FBP.particlesPerAxis; ++k) {
                for (int l = 0; l < FBP.particlesPerAxis; ++l) {
                    final double d0 = pos.getX() + (j + 0.5) / FBP.particlesPerAxis;
                    final double d2 = pos.getY() + (k + 0.5) / FBP.particlesPerAxis;
                    final double d3 = pos.getZ() + (l + 0.5) / FBP.particlesPerAxis;
                    if (state != null && !(b instanceof BlockLiquid) && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || b != Blocks.redstone_block) && !FBP.INSTANCE.isBlacklisted(b)) {
                        final float scale = (float)FBP.random.nextDouble(0.75, 1.0);
                        final FBPParticleDigging toSpawn = new FBPParticleDigging(this.worldObj, d0, d2, d3, d0 - pos.getX() - 0.5, -0.001, d3 - pos.getZ() - 0.5, scale, 1.0f, 1.0f, 1.0f, state, null, texture).setBlockPos(pos);
                        this.addEffect(toSpawn);
                    }
                }
            }
        }
    }

    @Override
    public void addBlockHitEffects(final BlockPos pos, final EnumFacing side) {
        final IBlockState iblockstate = this.worldObj.getBlockState(pos);
        if (iblockstate.getBlock().getRenderType() != -1) {
            final int i = pos.getX();
            final int j = pos.getY();
            final int k = pos.getZ();
            final float f = 0.1f;
            final AxisAlignedBB axisalignedbb = iblockstate.getBlock().getSelectedBoundingBox(this.worldObj, pos);
            double d0 = 0.0;
            double d2 = 0.0;
            double d3 = 0.0;
            MovingObjectPosition obj = Minecraft.getInstance().objectMouseOver;
            if (obj == null || obj.hitVec == null) {
                obj = new MovingObjectPosition(null, new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
            }
            if (FBP.smartBreaking && iblockstate != null && !(iblockstate.getBlock() instanceof BlockLiquid) && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || iblockstate.getBlock() != Blocks.redstone_block)) {
                d0 = obj.hitVec.xCoord + FBP.random.nextDouble(-0.21, 0.21) * Math.abs(axisalignedbb.maxX - axisalignedbb.minX);
                d2 = obj.hitVec.yCoord + FBP.random.nextDouble(-0.21, 0.21) * Math.abs(axisalignedbb.maxY - axisalignedbb.minY);
                d3 = obj.hitVec.zCoord + FBP.random.nextDouble(-0.21, 0.21) * Math.abs(axisalignedbb.maxZ - axisalignedbb.minZ);
            }
            else {
                d0 = i + this.worldObj.rand.nextDouble() * (axisalignedbb.maxX - axisalignedbb.minX - 0.20000000298023224) + 0.10000000149011612 + axisalignedbb.minX;
                d2 = j + this.worldObj.rand.nextDouble() * (axisalignedbb.maxY - axisalignedbb.minY - 0.20000000298023224) + 0.10000000149011612 + axisalignedbb.minY;
                d3 = k + this.worldObj.rand.nextDouble() * (axisalignedbb.maxZ - axisalignedbb.minZ - 0.20000000298023224) + 0.10000000149011612 + axisalignedbb.minZ;
            }
            switch (side) {
                case DOWN: {
                    d2 = j + iblockstate.getBlock().getBlockBoundsMinY() - f;
                    break;
                }
                case EAST: {
                    d0 = i + iblockstate.getBlock().getBlockBoundsMaxX() + f;
                    break;
                }
                case NORTH: {
                    d3 = k + iblockstate.getBlock().getBlockBoundsMinZ() - f;
                    break;
                }
                case SOUTH: {
                    d3 = k + iblockstate.getBlock().getBlockBoundsMaxZ() + f;
                    break;
                }
                case UP: {
                    d2 = j + iblockstate.getBlock().getBlockBoundsMaxY() + f;
                    break;
                }
                case WEST: {
                    d0 = i + iblockstate.getBlock().getBlockBoundsMinX() - f;
                    break;
                }
            }
            if (iblockstate != null && !(iblockstate.getBlock() instanceof BlockLiquid) && (!FBP.frozen || FBP.spawnWhileFrozen) && (FBP.spawnRedstoneBlockParticles || iblockstate.getBlock() != Blocks.redstone_block)) {
                int damage = 0;
                try {
                    DestroyBlockProgress progress = null;
                    Map mp = (Map<Integer, DestroyBlockProgress>) getBlockDamage
                            .invokeExact(Minecraft.mc().renderGlobal);
                    if (!mp.isEmpty()) {
                        final Iterator it = mp.values().iterator();
                        while (it.hasNext()) {
                            progress = (DestroyBlockProgress) it.next();
                            if (progress.getPosition().equals(pos)) {
                                damage = progress.getPartialBlockDamage();
                                break;
                            }
                        }
                    }
                }
                catch (Throwable t) {}
                if (!FBP.INSTANCE.isBlacklisted(iblockstate.getBlock())) {
                    EntityFX toSpawn = new FBPParticleDigging(this.worldObj, d0, d2, d3, 0.0, 0.0, 0.0, -2.0f, 1.0f, 1.0f, 1.0f, iblockstate, side, null).setBlockPos(pos);
                    if (FBP.smartBreaking) {
                        toSpawn = ((FBPParticleDigging)toSpawn).MultiplyVelocity((side == EnumFacing.UP) ? 0.7f : 0.15f);
                        toSpawn = toSpawn.multipleParticleScaleBy(0.325f + damage / 10.0f * 0.5f);
                    }
                    else {
                        toSpawn = ((FBPParticleDigging)toSpawn).MultiplyVelocity(0.2f);
                        toSpawn = toSpawn.multipleParticleScaleBy(0.6f);
                    }
                    this.addEffect(toSpawn);
                }
            }
        }
    }
}
