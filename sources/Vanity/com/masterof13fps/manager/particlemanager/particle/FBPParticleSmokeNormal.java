package com.masterof13fps.manager.particlemanager.particle;

import com.masterof13fps.manager.particlemanager.FBP;
import com.masterof13fps.manager.particlemanager.util.FBPRenderUtil;
import net.minecraft.client.*;
import javax.vecmath.*;
import net.minecraft.world.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import net.minecraft.client.particle.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.client.renderer.*;

public class FBPParticleSmokeNormal extends EntitySmokeFX
{
    Minecraft mc;
    double startScale;
    double scaleAlpha;
    double prevParticleScale;
    double prevParticleAlpha;
    double endMult;
    Vec3[] cube;
    Vector2f par;
    EntitySmokeFX original;

    protected FBPParticleSmokeNormal(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double mX, final double mY, final double mZ, final float scale, final boolean b, final TextureAtlasSprite tex, final EntitySmokeFX original) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, mX, mY, mZ, scale);
        this.endMult = 0.75;
        this.original = original;
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
        this.mc = Minecraft.getInstance();
        this.particleIcon = tex;
        this.scaleAlpha = this.particleScale * 0.85;
        final Block block = worldIn.getBlockState(new BlockPos(this.posX, this.posY, this.posZ)).getBlock();
        if (block == Blocks.fire) {
            this.particleScale *= 0.65f;
            this.particleGravity *= 0.25f;
            this.motionX = FBP.random.nextDouble(-0.05, 0.05);
            this.motionY = FBP.random.nextDouble() * 0.5;
            this.motionZ = FBP.random.nextDouble(-0.05, 0.05);
            this.motionY *= 0.3499999940395355;
            this.scaleAlpha = this.particleScale * 0.5;
            this.particleMaxAge = FBP.random.nextInt(7, 18);
        }
        else if (block == Blocks.torch) {
            this.particleScale *= 0.45f;
            this.motionX = FBP.random.nextDouble(-0.05, 0.05);
            this.motionY = FBP.random.nextDouble() * 0.5;
            this.motionZ = FBP.random.nextDouble(-0.05, 0.05);
            this.motionX *= 0.925000011920929;
            this.motionY = 0.004999999888241291;
            this.motionZ *= 0.925000011920929;
            this.particleRed = 0.275f;
            this.particleGreen = 0.275f;
            this.particleBlue = 0.275f;
            this.scaleAlpha = this.particleScale * 0.75;
            this.particleMaxAge = FBP.random.nextInt(5, 10);
        }
        else {
            this.particleScale = scale;
            this.motionY *= 0.935;
        }
        this.particleScale *= (float)FBP.scaleMult;
        this.startScale = this.particleScale;
        final float angleY = this.rand.nextFloat() * 80.0f;
        this.cube = new Vec3[FBP.CUBE.length];
        for (int i = 0; i < FBP.CUBE.length; ++i) {
            final Vec3 vec = FBP.CUBE[i];
            this.cube[i] = FBPRenderUtil.rotatef_d(vec, 0.0f, angleY, 0.0f);
        }
        this.particleAlpha = 1.0f;
        if (FBP.randomFadingSpeed) {
            this.endMult = MathHelper.clamp_double(FBP.random.nextDouble(0.425, 1.15), 0.5432, 1.0);
        }
        this.multipleParticleScaleBy(1.0f);
    }

    @Override
    public EntityFX multipleParticleScaleBy(final float scale) {
        final EntityFX p = super.multipleParticleScaleBy(scale);
        final float f = this.particleScale / 20.0f;
        this.setEntityBoundingBox(new AxisAlignedBB(this.posX - f, this.posY - f, this.posZ - f, this.posX + f, this.posY + f, this.posZ + f));
        return p;
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
        if (!FBP.fancySmoke) {
            this.isDead = true;
        }
        if (++this.particleAge >= this.particleMaxAge) {
            if (FBP.randomFadingSpeed) {
                this.particleScale *= (float)(0.8876543045043945 * this.endMult);
            }
            else {
                this.particleScale *= 0.8876543f;
            }
            if (this.particleAlpha > 0.01 && this.particleScale <= this.scaleAlpha) {
                if (FBP.randomFadingSpeed) {
                    this.particleAlpha *= (float)(0.7654321193695068 * this.endMult);
                }
                else {
                    this.particleAlpha *= 0.7654321f;
                }
            }
            if (this.particleAlpha <= 0.01) {
                this.setDead();
            }
        }
        this.motionY += 0.004;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
        }
        this.motionX *= 0.9599999785423279;
        this.motionY *= 0.9599999785423279;
        this.motionZ *= 0.9599999785423279;
        if (this.onGround) {
            this.motionX *= 0.899999988079071;
            this.motionZ *= 0.899999988079071;
        }
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
        this.posY = (axisalignedbb2.minY + axisalignedbb2.maxY) / 2.0;
        this.posZ = (axisalignedbb2.minZ + axisalignedbb2.maxZ) / 2.0;
        this.onGround = (y != Y);
    }

    @Override
    public void renderParticle(final WorldRenderer worldRendererIn, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        if (!FBP.isEnabled() && this.particleMaxAge != 0) {
            this.particleMaxAge = 0;
        }
        final float f = this.particleIcon.getInterpolatedU(4.400000095367432);
        final float f2 = this.particleIcon.getInterpolatedV(4.400000095367432);
        final float f3 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - FBPParticleSmokeNormal.interpPosX);
        final float f4 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - FBPParticleSmokeNormal.interpPosY);
        final float f5 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - FBPParticleSmokeNormal.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        final float alpha = (float)(this.prevParticleAlpha + (this.particleAlpha - this.prevParticleAlpha) * partialTicks);
        final float f6 = (float)(this.prevParticleScale + (this.particleScale - this.prevParticleScale) * partialTicks);
        this.par = new Vector2f(f, f2);
        worldRendererIn.setTranslation(f3, f4, f5);
        this.putCube(worldRendererIn, f6 / 20.0f, i >> 16 & 0xFFFF, i & 0xFFFF, this.particleRed, this.particleGreen, this.particleBlue, alpha);
        worldRendererIn.setTranslation(0.0, 0.0, 0.0);
    }

    public void putCube(final WorldRenderer worldRendererIn, final double scale, final int j, final int k, final float r, final float g, final float b, final float a) {
        float brightnessForRender = 1.0f;
        float R = 0.0f;
        float G = 0.0f;
        float B = 0.0f;
        for (int i = 0; i < this.cube.length; i += 4) {
            final Vec3 v1 = this.cube[i];
            final Vec3 v2 = this.cube[i + 1];
            final Vec3 v3 = this.cube[i + 2];
            final Vec3 v4 = this.cube[i + 3];
            R = r * brightnessForRender;
            G = g * brightnessForRender;
            B = b * brightnessForRender;
            brightnessForRender *= 0.875;
            this.addVt(worldRendererIn, scale, v1, this.par.x, this.par.y, j, k, R, G, B, a);
            this.addVt(worldRendererIn, scale, v2, this.par.x, this.par.y, j, k, R, G, B, a);
            this.addVt(worldRendererIn, scale, v3, this.par.x, this.par.y, j, k, R, G, B, a);
            this.addVt(worldRendererIn, scale, v4, this.par.x, this.par.y, j, k, R, G, B, a);
        }
    }

    private void addVt(final WorldRenderer worldRendererIn, final double scale, final Vec3 pos, final double u, final double v, final int j, final int k, final float r, final float g, final float b, final float a) {
        worldRendererIn.pos(pos.xCoord * scale, pos.yCoord * scale, pos.zCoord * scale).tex(u, v).color(r, g, b, a).lightmap(j, k).endVertex();
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

    @Override
    public void setDead() {
        this.isDead = true;
        this.original.setDead();
    }

    public void setMaxAge(final int maxAge) {
        this.particleMaxAge = maxAge;
    }
}
