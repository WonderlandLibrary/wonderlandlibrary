package com.masterof13fps.manager.particlemanager.particle;

import com.masterof13fps.manager.particlemanager.FBP;
import com.masterof13fps.manager.particlemanager.util.FBPRenderUtil;
import com.masterof13fps.manager.particlemanager.vector.FBPVector3d;
import net.minecraft.block.state.*;
import net.minecraft.client.*;
import org.lwjgl.util.vector.*;
import net.minecraft.world.*;
import net.minecraft.client.particle.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import java.util.*;
import net.minecraft.client.renderer.*;

public class FBPParticleRain extends EntityDiggingFX
{
    private final IBlockState sourceState;
    Minecraft mc;
    double AngleY;
    double particleHeight;
    double prevParticleScale;
    double prevParticleHeight;
    double prevParticleAlpha;
    double scalar;
    double endMult;
    Vector2f[] par;

    public FBPParticleRain(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final IBlockState state) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, state);
        this.scalar = FBP.scaleMult;
        this.endMult = 1.0;
        try {
            FBP.setSourcePos.invokeExact((EntityDiggingFX)this, new BlockPos(xCoordIn, yCoordIn, zCoordIn));
        }
        catch (Throwable e1) {
            e1.printStackTrace();
        }
        this.AngleY = FBP.random.nextDouble() * 45.0;
        this.motionX = xSpeedIn;
        this.motionY = -ySpeedIn;
        this.motionZ = zSpeedIn;
        this.particleGravity = 0.025f;
        this.sourceState = state;
        this.mc = Minecraft.getInstance();
        this.particleMaxAge = (int)FBP.random.nextDouble(50.0, 70.0);
        this.particleAlpha = 0.0f;
        this.particleScale = 0.0f;
        this.isAirBorne = true;
        if (FBP.randomFadingSpeed) {
            this.endMult *= FBP.random.nextDouble(0.85, 1.0);
        }
    }

    @Override
    public void setParticleTextureIndex(final int particleTextureIndex) {
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
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.prevParticleAlpha = this.particleAlpha;
        this.prevParticleScale = this.particleScale;
        this.prevParticleHeight = this.particleHeight;
        if (!this.mc.isGamePaused()) {
            ++this.particleAge;
            if (this.posY < this.mc.thePlayer.posY - this.mc.gameSettings.renderDistanceChunks * 9) {
                this.setDead();
            }
            if (!this.onGround) {
                if (this.particleAge < this.particleMaxAge) {
                    final double max = this.scalar * 0.5;
                    if (this.particleScale < max) {
                        if (FBP.randomFadingSpeed) {
                            this.particleScale += (float)(0.05000000074505806 * this.endMult);
                        }
                        else {
                            this.particleScale += 0.05f;
                        }
                        if (this.particleScale > max) {
                            this.particleScale = (float)max;
                        }
                        this.particleHeight = this.particleScale;
                    }
                    if (this.particleAlpha < 0.65f) {
                        if (FBP.randomFadingSpeed) {
                            this.particleAlpha += (float)(0.08500000089406967 * this.endMult);
                        }
                        else {
                            this.particleAlpha += 0.085f;
                        }
                        if (this.particleAlpha > 0.65f) {
                            this.particleAlpha = 0.65f;
                        }
                    }
                }
                else {
                    this.setDead();
                }
            }
            if (this.worldObj.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).getBlock().getMaterial().isLiquid()) {
                this.setDead();
            }
            this.motionY -= 0.04 * this.particleGravity;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionY *= 1.0002500019073486;
            if (this.onGround) {
                this.motionX = 0.0;
                this.motionY = -0.25;
                this.motionZ = 0.0;
                if (this.particleHeight > 0.07500000298023224) {
                    this.particleHeight *= 0.7250000238418579;
                }
                final float max2 = (float)this.scalar * 4.25f;
                if (this.particleScale < max2) {
                    this.particleScale += max2 / 10.0f;
                    if (this.particleScale > max2) {
                        this.particleScale = max2;
                    }
                }
                if (this.particleScale >= max2 / 2.0f) {
                    if (FBP.randomFadingSpeed) {
                        this.particleAlpha *= (float)(0.75 * this.endMult);
                    }
                    else {
                        this.particleAlpha *= 0.75f;
                    }
                    if (this.particleAlpha <= 0.001f) {
                        this.setDead();
                    }
                }
            }
        }
        final Vec3 rgb = this.mc.theWorld.getSkyColor(this.mc.thePlayer, 0.0f);
        this.particleRed = (float)rgb.xCoord;
        this.particleGreen = (float)MathHelper.clamp_double(rgb.yCoord + 0.25, 0.25, 1.0);
        this.particleBlue = (float)MathHelper.clamp_double(rgb.zCoord + 0.5, 0.5, 1.0);
        if (this.particleGreen > 1.0f) {
            this.particleGreen = 1.0f;
        }
        if (this.particleBlue > 1.0f) {
            this.particleBlue = 1.0f;
        }
    }

    @Override
    public void moveEntity(double x, double y, double z) {
        final double X = x;
        final double Y = y;
        final double Z = z;
        final List<AxisAlignedBB> list1 = this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox().addCoord(x, y, z));
        for (final AxisAlignedBB axisalignedbb1 : list1) {
            y = axisalignedbb1.calculateYOffset(this.getEntityBoundingBox(), y);
        }
        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, y, 0.0));
        for (final AxisAlignedBB axisalignedbb2 : list1) {
            x = axisalignedbb2.calculateXOffset(this.getEntityBoundingBox(), x);
        }
        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, 0.0, 0.0));
        for (final AxisAlignedBB axisalignedbb3 : list1) {
            z = axisalignedbb3.calculateZOffset(this.getEntityBoundingBox(), z);
        }
        this.setEntityBoundingBox(this.getEntityBoundingBox().offset(0.0, 0.0, z));
        this.resetPositionToBB();
        this.onGround = (y != Y && Y < 0.0);
        if (x != X) {
            this.motionX *= 0.699999988079071;
        }
        if (z != Z) {
            this.motionZ *= 0.699999988079071;
        }
    }

    private void resetPositionToBB() {
        this.posX = (this.getEntityBoundingBox().minX + this.getEntityBoundingBox().maxX) / 2.0;
        this.posY = this.getEntityBoundingBox().minY;
        this.posZ = (this.getEntityBoundingBox().minZ + this.getEntityBoundingBox().maxZ) / 2.0;
    }

    @Override
    public void renderParticle(final WorldRenderer buf, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        if (!FBP.isEnabled() && this.particleMaxAge != 0) {
            this.particleMaxAge = 0;
        }
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        if (this.particleIcon != null) {
            if (!FBP.cartoonMode) {
                f = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0f * 16.0f);
                f3 = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0f * 16.0f);
            }
            f2 = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0f) / 4.0f * 16.0f);
            f4 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0f) / 4.0f * 16.0f);
        }
        else {
            f = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0f) / 16.0f;
            f2 = f + 0.015609375f;
            f3 = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0f) / 16.0f;
            f4 = f3 + 0.015609375f;
        }
        final float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - FBPParticleRain.interpPosX);
        final float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - FBPParticleRain.interpPosY);
        final float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - FBPParticleRain.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        final float alpha = (float)(this.prevParticleAlpha + (this.particleAlpha - this.prevParticleAlpha) * partialTicks);
        final float f8 = (float)(this.prevParticleScale + (this.particleScale - this.prevParticleScale) * partialTicks);
        final float height = (float)(this.prevParticleHeight + (this.particleHeight - this.prevParticleHeight) * partialTicks);
        FBPRenderUtil.renderCubeShaded_WH(buf, this.par = new Vector2f[] { new Vector2f(f2, f4), new Vector2f(f2, f3), new Vector2f(f, f3), new Vector2f(f, f4) }, f5, f6 + height / 10.0f, f7, f8 / 10.0f, height / 10.0f, new FBPVector3d(0.0, this.AngleY, 0.0), i >> 16 & 0xFFFF, i & 0xFFFF, this.particleRed, this.particleGreen, this.particleBlue, alpha);
    }

    @Override
    public int getBrightnessForRender(final float p_189214_1_) {
        final int i = super.getBrightnessForRender(p_189214_1_);
        int j = 0;
        if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, this.posY, this.posZ))) {
            j = this.worldObj.getCombinedLight(new BlockPos(this.posX, this.posY, this.posZ), 0);
        }
        return (i == 0) ? j : i;
    }
}
