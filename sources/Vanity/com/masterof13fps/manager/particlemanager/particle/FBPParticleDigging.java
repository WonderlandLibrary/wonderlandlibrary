package com.masterof13fps.manager.particlemanager.particle;

import com.masterof13fps.manager.particlemanager.FBP;
import com.masterof13fps.manager.particlemanager.util.FBPMathUtil;
import com.masterof13fps.manager.particlemanager.util.FBPRenderUtil;
import com.masterof13fps.manager.particlemanager.vector.FBPVector3d;
import net.minecraft.block.state.*;
import net.minecraft.client.*;
import org.lwjgl.util.vector.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.resources.model.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.world.gen.structure.*;
import net.minecraft.block.material.*;
import net.minecraft.block.*;
import net.minecraft.block.properties.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.particle.*;

public class FBPParticleDigging extends EntityDiggingFX
{
    private final IBlockState sourceState;
    Minecraft mc;
    float prevGravity;
    double startY;
    double scaleAlpha;
    double prevParticleScale;
    double prevParticleAlpha;
    double prevMotionX;
    double prevMotionZ;
    double endMult;
    boolean modeDebounce;
    boolean wasFrozen;
    boolean destroyed;
    boolean killToggle;
    EnumFacing facing;
    FBPVector3d rot;
    FBPVector3d prevRot;
    FBPVector3d rotStep;
    Vector2f uvMin;
    Vector2f uvMax;

    protected FBPParticleDigging(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final float scale, final float R, final float G, final float B, final IBlockState state, final EnumFacing facing, final TextureAtlasSprite texture) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
        this.endMult = 0.75;
        this.particleRed = R;
        this.particleGreen = G;
        this.particleBlue = B;
        this.mc = Minecraft.getInstance();
        this.rot = new FBPVector3d();
        this.prevRot = new FBPVector3d();
        this.facing = facing;
        this.createRotationMatrix();
        try {
            FBP.setSourcePos.invokeExact((EntityDiggingFX)this, new BlockPos(xCoordIn, yCoordIn, zCoordIn));
        }
        catch (Throwable e1) {
            e1.printStackTrace();
        }
        if (scale > -1.0f) {
            this.particleScale = scale;
        }
        if (scale < -1.0f && facing != null && facing == EnumFacing.UP && FBP.smartBreaking) {
            this.motionX *= 1.5;
            this.motionY *= 0.1;
            this.motionZ *= 1.5;
            final double particleSpeed = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            final Vec3 vec = this.mc.thePlayer.getLookVec();
            final double x = FBPMathUtil.add(vec.xCoord, 0.01);
            final double z = FBPMathUtil.add(vec.zCoord, 0.01);
            this.motionX = x * particleSpeed;
            this.motionZ = z * particleSpeed;
        }
        final boolean modeDebounce = !FBP.randomRotation;
        this.modeDebounce = modeDebounce;
        if (modeDebounce) {
            this.rot.zero();
            this.calculateYAngle();
        }
        this.sourceState = state;
        final Block b = state.getBlock();
        this.particleGravity = (float)(b.blockParticleGravity * FBP.gravityMult);
        this.particleScale = (float)(FBP.scaleMult * (FBP.randomizedScale ? this.particleScale : 1.0f));
        this.particleMaxAge = (int)FBP.random.nextDouble(FBP.minAge, FBP.maxAge + 0.5);
        this.scaleAlpha = this.particleScale * 0.82;
        this.destroyed = (facing == null);
        if (texture == null) {
            final BlockModelShapes blockModelShapes = this.mc.getBlockRendererDispatcher().getBlockModelShapes();
            if (!this.destroyed) {
                try {
                    final IBakedModel model = blockModelShapes.getModelForState(state);
                    this.particleIcon = model.getParticleTexture();
                    final List<BakedQuad> quads = model.getFaceQuads(facing);
                    if (quads != null && !quads.isEmpty()) {
                        final int[] data = quads.get(0).getVertexData();
                        final float u1 = Float.intBitsToFloat(data[4]);
                        final float v1 = Float.intBitsToFloat(data[5]);
                        final float u2 = Float.intBitsToFloat(data[18]);
                        final float v2 = Float.intBitsToFloat(data[19]);
                        this.uvMin = new Vector2f(u1, v1);
                        this.uvMax = new Vector2f(u2, v2);
                    }
                }
                catch (Exception ex) {}
            }
            if (this.particleIcon == null || this.particleIcon.getIconName().equals("missingno")) {
                this.particleIcon = blockModelShapes.getTexture(state);
                if (this.particleIcon != null) {
                    this.uvMin = new Vector2f(this.particleIcon.getMinU(), this.particleIcon.getMinV());
                    this.uvMax = new Vector2f(this.particleIcon.getMaxU(), this.particleIcon.getMaxV());
                }
            }
        }
        else {
            this.particleIcon = texture;
        }
        if (FBP.randomFadingSpeed) {
            this.endMult = MathHelper.clamp_double(FBP.random.nextDouble(0.5, 0.9), 0.55, 0.8);
        }
        this.prevGravity = this.particleGravity;
        this.startY = this.posY;
        this.multipleParticleScaleBy(1.0f);
    }

    @Override
    public FBPParticleDigging setBlockPos(final BlockPos pos) {
        if (this.sourceState.getBlock() == Blocks.grass && this.facing != EnumFacing.UP) {
            return this;
        }
        final int i = this.sourceState.getBlock().colorMultiplier(this.worldObj, pos);
        this.particleRed *= (i >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (i >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (i & 0xFF) / 255.0f;
        return this;
    }

    @Override
    public FBPParticleDigging func_174845_l() {
        if (this.sourceState.getBlock() == Blocks.grass && this.facing != EnumFacing.UP) {
            return this;
        }
        final int i = this.sourceState.getBlock().colorMultiplier(this.worldObj, new BlockPos(this.posX, this.posY, this.posZ));
        this.particleRed *= (i >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (i >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (i & 0xFF) / 255.0f;
        return this;
    }

    @Override
    public EntityFX multipleParticleScaleBy(final float scale) {
        final EntityFX p = super.multipleParticleScaleBy(scale);
        final float f = this.particleScale / 10.0f;
        if (FBP.restOnFloor && this.destroyed) {
            final double n = this.startY - f;
            this.prevPosY = n;
            this.posY = n;
        }
        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - f, this.posY, this.posZ - f, this.posX + f, this.posY + 2.0f * f, this.posZ + f));
        return p;
    }

    public EntityFX MultiplyVelocity(final float multiplier) {
        this.motionX *= multiplier;
        this.motionY = (this.motionY - 0.10000000149011612) * (multiplier / 2.0f) + 0.10000000149011612;
        this.motionZ *= multiplier;
        return this;
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public void onUpdate() {
        final boolean allowedToMove = MathHelper.abs((float)this.motionX) > 1.0E-4 || MathHelper.abs((float)this.motionZ) > 1.0E-4;
        if (!FBP.frozen && FBP.bounceOffWalls && !this.mc.isGamePaused() && this.particleAge > 0) {
            if (!this.wasFrozen && allowedToMove) {
                final boolean xCollided = this.prevPosX == this.posX;
                final boolean zCollided = this.prevPosZ == this.posZ;
                if (xCollided) {
                    this.motionX = -this.prevMotionX * 0.625;
                }
                if (zCollided) {
                    this.motionZ = -this.prevMotionZ * 0.625;
                }
                if (!FBP.randomRotation && (xCollided || zCollided)) {
                    this.calculateYAngle();
                }
            }
            else {
                this.wasFrozen = false;
            }
        }
        if (FBP.frozen && FBP.bounceOffWalls && !this.wasFrozen) {
            this.wasFrozen = true;
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevRot.copyFrom(this.rot);
        this.prevParticleAlpha = this.particleAlpha;
        this.prevParticleScale = this.particleScale;
        if (!this.mc.isGamePaused() && (!FBP.frozen || this.killToggle)) {
            if (!this.killToggle) {
                if (!FBP.randomRotation) {
                    if (!this.modeDebounce) {
                        this.modeDebounce = true;
                        this.rot.z = 0.0;
                        this.calculateYAngle();
                    }
                    if (allowedToMove) {
                        final double x = MathHelper.abs((float)(this.rotStep.x * this.getMult()));
                        if (this.motionX > 0.0) {
                            if (this.motionZ > 0.0) {
                                final FBPVector3d rot = this.rot;
                                rot.x -= x;
                            }
                            else if (this.motionZ < 0.0) {
                                final FBPVector3d rot2 = this.rot;
                                rot2.x += x;
                            }
                        }
                        else if (this.motionX < 0.0) {
                            if (this.motionZ < 0.0) {
                                final FBPVector3d rot3 = this.rot;
                                rot3.x += x;
                            }
                            else if (this.motionZ > 0.0) {
                                final FBPVector3d rot4 = this.rot;
                                rot4.x -= x;
                            }
                        }
                    }
                }
                else {
                    if (this.modeDebounce) {
                        this.modeDebounce = false;
                        this.rot.z = FBP.random.nextDouble(30.0, 400.0);
                    }
                    if (allowedToMove) {
                        this.rot.add(this.rotStep.multiply(this.getMult()));
                    }
                }
            }
            if (!FBP.infiniteDuration) {
                ++this.particleAge;
            }
            if (this.particleAge >= this.particleMaxAge || this.killToggle) {
                this.particleScale *= (float)(0.8876543045043945 * this.endMult);
                if (this.particleAlpha > 0.01 && this.particleScale <= this.scaleAlpha) {
                    this.particleAlpha *= (float)(0.6875200271606445 * this.endMult);
                }
                if (this.particleAlpha <= 0.01) {
                    this.setDead();
                }
            }
            if (!this.killToggle) {
                if (!this.isCollided) {
                    this.motionY -= 0.04 * this.particleGravity;
                }
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                if (this.isCollided && FBP.restOnFloor) {
                    this.rot.x = Math.round(this.rot.x / 90.0) * 90.0f;
                    this.rot.z = Math.round(this.rot.z / 90.0) * 90.0f;
                }
                if (MathHelper.abs((float)this.motionX) > 1.0E-5) {
                    this.prevMotionX = this.motionX;
                }
                if (MathHelper.abs((float)this.motionZ) > 1.0E-5) {
                    this.prevMotionZ = this.motionZ;
                }
                if (allowedToMove) {
                    this.motionX *= 0.9800000190734863;
                    this.motionZ *= 0.9800000190734863;
                }
                this.motionY *= 0.9800000190734863;
                if (FBP.entityCollision) {
                    final List<Entity> list = this.worldObj.getEntitiesWithinAABB((Class<? extends Entity>)Entity.class, this.getEntityBoundingBox());
                    for (final Entity entityIn : list) {
                        if (!entityIn.noClip) {
                            double d0 = this.posX - entityIn.posX;
                            double d2 = this.posZ - entityIn.posZ;
                            double d3 = MathHelper.abs_max(d0, d2);
                            if (d3 < 0.009999999776482582) {
                                continue;
                            }
                            d3 = Math.sqrt(d3);
                            d0 /= d3;
                            d2 /= d3;
                            double d4 = 1.0 / d3;
                            if (d4 > 1.0) {
                                d4 = 1.0;
                            }
                            this.motionX += d0 * d4 / 20.0;
                            this.motionZ += d2 * d4 / 20.0;
                            if (!FBP.randomRotation) {
                                this.calculateYAngle();
                            }
                            if (FBP.frozen) {
                                continue;
                            }
                            this.isCollided = false;
                        }
                    }
                }
                if (FBP.waterPhysics) {
                    if (this.isInWater()) {
                        this.handleWaterMovement();
                        if (FBP.INSTANCE.doesMaterialFloat(this.sourceState.getBlock().getMaterial())) {
                            this.motionY = 0.11f + this.particleScale / 1.25f * 0.02f;
                        }
                        else {
                            this.motionX *= 0.932515086137662;
                            this.motionZ *= 0.932515086137662;
                            this.particleGravity = 0.35f;
                            this.motionY *= 0.8500000238418579;
                        }
                        if (!FBP.randomRotation) {
                            this.calculateYAngle();
                        }
                        if (this.isCollided) {
                            this.isCollided = false;
                        }
                    }
                    else {
                        this.particleGravity = this.prevGravity;
                    }
                }
                if (this.isCollided) {
                    if (FBP.lowTraction) {
                        this.motionX *= 0.932515086137662;
                        this.motionZ *= 0.932515086137662;
                    }
                    else {
                        this.motionX *= 0.6654999988079071;
                        this.motionZ *= 0.6654999988079071;
                    }
                }
            }
        }
    }

    @Override
    public boolean isInWater() {
        final double scale = this.particleScale / 20.0f;
        final int minX = MathHelper.floor_double(this.posX - scale);
        final int maxX = MathHelper.ceiling_double_int(this.posX + scale);
        final int minY = MathHelper.floor_double(this.posY - scale);
        final int maxY = MathHelper.ceiling_double_int(this.posY + scale);
        final int minZ = MathHelper.floor_double(this.posZ - scale);
        final int maxZ = MathHelper.ceiling_double_int(this.posZ + scale);
        if (this.worldObj.isAreaLoaded(new StructureBoundingBox(minX, minY, minZ, maxX, maxY, maxZ), true)) {
            for (int x = minX; x < maxX; ++x) {
                for (int y = minY; y < maxY; ++y) {
                    for (int z = minZ; z < maxZ; ++z) {
                        final IBlockState block = this.worldObj.getBlockState(new BlockPos(x, y, z));
                        if (block.getBlock().getMaterial() == Material.water) {
                            final double d0 = y + 1 - BlockLiquid.getLiquidHeightPercent(block.getValue((IProperty<Integer>)BlockLiquid.LEVEL));
                            if (this.posY <= d0) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        final double X = x;
        final double Y = y;
        final double Z = z;
        final List<AxisAlignedBB> list = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(x, y, z));
        for (final AxisAlignedBB axisalignedbb : list) {
            y = axisalignedbb.calculateYOffset(this.getEntityBoundingBox(), y);
        }
        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
        for (final AxisAlignedBB axisalignedbb : list) {
            x = axisalignedbb.calculateXOffset(this.getEntityBoundingBox(), x);
        }
        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0, 0.0));
        for (final AxisAlignedBB axisalignedbb : list) {
            z = axisalignedbb.calculateZOffset(this.getEntityBoundingBox(), z);
        }
        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, 0.0, z));
        final AxisAlignedBB axisalignedbb2 = this.getEntityBoundingBox();
        this.posX = (axisalignedbb2.minX + axisalignedbb2.maxX) / 2.0;
        this.posY = axisalignedbb2.minY;
        this.posZ = (axisalignedbb2.minZ + axisalignedbb2.maxZ) / 2.0;
        this.isCollided = (y != Y && Y < 0.0);
        if (!FBP.lowTraction && !FBP.bounceOffWalls) {
            if (x != X) {
                this.motionX *= 0.699999988079071;
            }
            if (z != Z) {
                this.motionZ *= 0.699999988079071;
            }
        }
    }

    @Override
    public void renderParticle(final WorldRenderer buf, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
            if (!FBP.isEnabled() && this.particleMaxAge != 0) {
                this.particleMaxAge = 0;
            }
            float minX = 0.0f;
            float maxX = 0.0f;
            float minY = 0.0f;
            float maxY = 0.0f;
            final float f4 = (float)(this.prevParticleScale + (this.particleScale - this.prevParticleScale) * partialTicks);
            if (this.particleIcon != null) {
                if (this.uvMin == null && this.uvMax == null) {
                    minX = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0f * 16.0f);
                    minY = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0f * 16.0f);
                    maxX = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0f) / 4.0f * 16.0f);
                    maxY = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0f) / 4.0f * 16.0f);
                }
                else {
                    final int size = 4;
                    final float sizeX = this.uvMax.x - this.uvMin.x;
                    final float sizeY = this.uvMax.y - this.uvMin.y;
                    final float startX = (this.particleTextureJitterX + 1.0f) * 4.0f - size;
                    final float startY = (this.particleTextureJitterY + 1.0f) * 4.0f - size;
                    minX = this.uvMin.x + sizeX / 16.0f * startX;
                    minY = this.uvMin.y + sizeY / 16.0f * startY;
                    maxX = this.uvMax.x - sizeX / 16.0f * (16.0f - startX - size);
                    maxY = this.uvMax.y - sizeY / 16.0f * (16.0f - startY - size);
                }
            }
            else {
                minX = (0.0f + this.particleTextureJitterX / 4.0f) / 16.0f;
                minY = (0.0f + this.particleTextureJitterY / 4.0f) / 16.0f;
                maxX = minX + 0.015609375f;
                maxY = minY + 0.015609375f;
            }
            final float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - FBPParticleDigging.interpPosX);
            float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - FBPParticleDigging.interpPosY);
            final float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - FBPParticleDigging.interpPosZ);
            final int i = this.getBrightnessForRender(partialTicks);
            final float alpha = (float)(this.prevParticleAlpha + (this.particleAlpha - this.prevParticleAlpha) * partialTicks);
            if (FBP.restOnFloor) {
                f6 += f4 / 10.0f;
            }
            final FBPVector3d smoothRot = new FBPVector3d(0.0, 0.0, 0.0);
            if (FBP.rotationMult > 0.0) {
                smoothRot.y = this.rot.y;
                smoothRot.z = this.rot.z;
                if (!FBP.randomRotation) {
                    smoothRot.x = this.rot.x;
                }
                if (!FBP.frozen) {
                    final FBPVector3d vec = this.rot.partialVec(this.prevRot, partialTicks);
                    if (FBP.randomRotation) {
                        smoothRot.y = vec.y;
                        smoothRot.z = vec.z;
                    }
                    else {
                        smoothRot.x = vec.x;
                    }
                }
            }
            FBPRenderUtil.renderCubeShaded_S(buf, new Vector2f[] { new Vector2f(maxX, maxY), new Vector2f(maxX, minY), new Vector2f(minX, minY), new Vector2f(minX, maxY) }, f5, f6, f7, f4 / 10.0f, smoothRot, i >> 16 & 0xFFFF, i & 0xFFFF, this.particleRed, this.particleGreen, this.particleBlue, alpha);

    }

    private void createRotationMatrix() {
        final double rx0 = FBP.random.nextDouble();
        final double ry0 = FBP.random.nextDouble();
        final double rz0 = FBP.random.nextDouble();
        this.rotStep = new FBPVector3d((rx0 > 0.5) ? 1.0 : -1.0, (ry0 > 0.5) ? 1.0 : -1.0, (rz0 > 0.5) ? 1.0 : -1.0);
        this.rot.copyFrom(this.rotStep);
    }

    @Override
    public int getBrightnessForRender(final float partialTicks) {
        final AxisAlignedBB box = this.getEntityBoundingBox();
        if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0, this.posZ))) {
            final double d0 = (box.maxY - box.minY) * 0.66;
            final double k = this.posY + d0 + 0.01 - (FBP.restOnFloor ? (this.particleScale / 10.0f) : 0.0f);
            return this.worldObj.getCombinedLight(new BlockPos(this.posX, k, this.posZ), 0);
        }
        return 0;
    }

    private void calculateYAngle() {
        final double angleSin = Math.toDegrees(Math.asin(this.motionX / Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ)));
        if (this.motionX > 0.0) {
            if (this.motionZ > 0.0) {
                this.rot.y = -angleSin;
            }
            else {
                this.rot.y = angleSin;
            }
        }
        else if (this.motionZ > 0.0) {
            this.rot.y = -angleSin;
        }
        else {
            this.rot.y = angleSin;
        }
    }

    double getMult() {
        return Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * (FBP.randomRotation ? 200 : 500) * FBP.rotationMult;
    }

    public static class Factory implements IParticleFactory
    {
        @Override
        public EntityFX getEntityFX(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new FBPParticleDigging(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, -1.0f, 1.0f, 1.0f, 1.0f, Block.getStateById(p_178902_15_[0]), null, null);
        }
    }
}
