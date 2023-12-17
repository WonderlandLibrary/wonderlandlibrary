// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import java.util.List;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.MISC.Optimization;
import ru.tuskevich.Minced;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.util.math.AxisAlignedBB;

public class Particle
{
    private static final AxisAlignedBB EMPTY_AABB;
    protected World world;
    protected double prevPosX;
    protected double prevPosY;
    protected double prevPosZ;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected double motionX;
    protected double motionY;
    protected double motionZ;
    private AxisAlignedBB boundingBox;
    protected boolean onGround;
    protected boolean canCollide;
    protected boolean isExpired;
    protected float width;
    protected float height;
    protected Random rand;
    protected int particleTextureIndexX;
    protected int particleTextureIndexY;
    protected float particleTextureJitterX;
    protected float particleTextureJitterY;
    protected int particleAge;
    protected int particleMaxAge;
    protected float particleScale;
    protected float particleGravity;
    protected float particleRed;
    protected float particleGreen;
    protected float particleBlue;
    protected float particleAlpha;
    protected TextureAtlasSprite particleTexture;
    protected float particleAngle;
    protected float prevParticleAngle;
    public static double interpPosX;
    public static double interpPosY;
    public static double interpPosZ;
    public static Vec3d cameraViewDir;
    
    protected Particle(final World worldIn, final double posXIn, final double posYIn, final double posZIn) {
        this.boundingBox = Particle.EMPTY_AABB;
        this.width = 0.6f;
        this.height = 1.8f;
        this.rand = new Random();
        this.particleAlpha = 1.0f;
        this.world = worldIn;
        this.setSize(0.2f, 0.2f);
        this.setPosition(posXIn, posYIn, posZIn);
        this.prevPosX = posXIn;
        this.prevPosY = posYIn;
        this.prevPosZ = posZIn;
        this.particleRed = 1.0f;
        this.particleGreen = 1.0f;
        this.particleBlue = 1.0f;
        this.particleTextureJitterX = this.rand.nextFloat() * 3.0f;
        this.particleTextureJitterY = this.rand.nextFloat() * 3.0f;
        this.particleScale = (this.rand.nextFloat() * 0.5f + 0.5f) * 2.0f;
        this.particleMaxAge = (int)(4.0f / (this.rand.nextFloat() * 0.9f + 0.1f));
        this.particleAge = 0;
        this.canCollide = true;
    }
    
    public Particle(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn) {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.motionX = xSpeedIn + (Math.random() * 2.0 - 1.0) * 0.4000000059604645;
        this.motionY = ySpeedIn + (Math.random() * 2.0 - 1.0) * 0.4000000059604645;
        this.motionZ = zSpeedIn + (Math.random() * 2.0 - 1.0) * 0.4000000059604645;
        final float f = (float)(Math.random() + Math.random() + 1.0) * 0.15f;
        final float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.motionX = this.motionX / f2 * f * 0.4000000059604645;
        this.motionY = this.motionY / f2 * f * 0.4000000059604645 + 0.10000000149011612;
        this.motionZ = this.motionZ / f2 * f * 0.4000000059604645;
    }
    
    public Particle multiplyVelocity(final float multiplier) {
        this.motionX *= multiplier;
        this.motionY = (this.motionY - 0.10000000149011612) * multiplier + 0.10000000149011612;
        this.motionZ *= multiplier;
        return this;
    }
    
    public Particle multipleParticleScaleBy(final float scale) {
        this.setSize(0.2f * scale, 0.2f * scale);
        this.particleScale *= scale;
        return this;
    }
    
    public void setRBGColorF(final float particleRedIn, final float particleGreenIn, final float particleBlueIn) {
        this.particleRed = particleRedIn;
        this.particleGreen = particleGreenIn;
        this.particleBlue = particleBlueIn;
    }
    
    public void setAlphaF(final float alpha) {
        this.particleAlpha = alpha;
    }
    
    public boolean shouldDisableDepth() {
        return false;
    }
    
    public float getRedColorF() {
        return this.particleRed;
    }
    
    public float getGreenColorF() {
        return this.particleGreen;
    }
    
    public float getBlueColorF() {
        return this.particleBlue;
    }
    
    public void setMaxAge(final int particleLifeTime) {
        this.particleMaxAge = particleLifeTime;
    }
    
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
        this.motionY -= 0.04 * this.particleGravity;
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863;
        this.motionY *= 0.9800000190734863;
        this.motionZ *= 0.9800000190734863;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        if (Minced.getInstance().manager.getModule(Optimization.class).state) {
            final Optimization optimizationModule = (Optimization)Minced.getInstance().manager.getModule(Optimization.class);
            if (optimizationModule.particles.get()) {
                return;
            }
        }
        float f = this.particleTextureIndexX / 16.0f;
        float f2 = f + 0.0624375f;
        float f3 = this.particleTextureIndexY / 16.0f;
        float f4 = f3 + 0.0624375f;
        final float f5 = 0.1f * this.particleScale;
        if (this.particleTexture != null) {
            f = this.particleTexture.getMinU();
            f2 = this.particleTexture.getMaxU();
            f3 = this.particleTexture.getMinV();
            f4 = this.particleTexture.getMaxV();
        }
        final float f6 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - Particle.interpPosX);
        final float f7 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - Particle.interpPosY);
        final float f8 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - Particle.interpPosZ);
        final int i = this.getBrightnessForRender(partialTicks);
        final int j = i >> 16 & 0xFFFF;
        final int k = i & 0xFFFF;
        final Vec3d[] avec3d = { new Vec3d(-rotationX * f5 - rotationXY * f5, -rotationZ * f5, -rotationYZ * f5 - rotationXZ * f5), new Vec3d(-rotationX * f5 + rotationXY * f5, rotationZ * f5, -rotationYZ * f5 + rotationXZ * f5), new Vec3d(rotationX * f5 + rotationXY * f5, rotationZ * f5, rotationYZ * f5 + rotationXZ * f5), new Vec3d(rotationX * f5 - rotationXY * f5, -rotationZ * f5, rotationYZ * f5 - rotationXZ * f5) };
        if (this.particleAngle != 0.0f) {
            final float f9 = this.particleAngle + (this.particleAngle - this.prevParticleAngle) * partialTicks;
            final float f10 = MathHelper.cos(f9 * 0.5f);
            final float f11 = MathHelper.sin(f9 * 0.5f) * (float)Particle.cameraViewDir.x;
            final float f12 = MathHelper.sin(f9 * 0.5f) * (float)Particle.cameraViewDir.y;
            final float f13 = MathHelper.sin(f9 * 0.5f) * (float)Particle.cameraViewDir.z;
            final Vec3d vec3d = new Vec3d(f11, f12, f13);
            for (int l = 0; l < 4; ++l) {
                avec3d[l] = vec3d.scale(2.0 * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale(f10 * f10 - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale(2.0f * f10));
            }
        }
        buffer.pos(f6 + avec3d[0].x, f7 + avec3d[0].y, f8 + avec3d[0].z).tex(f2, f4).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos(f6 + avec3d[1].x, f7 + avec3d[1].y, f8 + avec3d[1].z).tex(f2, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos(f6 + avec3d[2].x, f7 + avec3d[2].y, f8 + avec3d[2].z).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
        buffer.pos(f6 + avec3d[3].x, f7 + avec3d[3].y, f8 + avec3d[3].z).tex(f, f4).color(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha).lightmap(j, k).endVertex();
    }
    
    public int getFXLayer() {
        return 0;
    }
    
    public void setParticleTexture(final TextureAtlasSprite texture) {
        final int i = this.getFXLayer();
        if (i == 1) {
            this.particleTexture = texture;
            return;
        }
        throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");
    }
    
    public void setParticleTextureIndex(final int particleTextureIndex) {
        if (this.getFXLayer() != 0) {
            throw new RuntimeException("Invalid call to Particle.setMiscTex");
        }
        this.particleTextureIndexX = particleTextureIndex % 16;
        this.particleTextureIndexY = particleTextureIndex / 16;
    }
    
    public void nextTextureIndexX() {
        ++this.particleTextureIndexX;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ", Pos (" + this.posX + "," + this.posY + "," + this.posZ + "), RGBA (" + this.particleRed + "," + this.particleGreen + "," + this.particleBlue + "," + this.particleAlpha + "), Age " + this.particleAge;
    }
    
    public void setExpired() {
        this.isExpired = true;
    }
    
    protected void setSize(final float particleWidth, final float particleHeight) {
        if (particleWidth != this.width || particleHeight != this.height) {
            this.width = particleWidth;
            this.height = particleHeight;
            final AxisAlignedBB axisalignedbb = this.getBoundingBox();
            this.setBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + this.width, axisalignedbb.minY + this.height, axisalignedbb.minZ + this.width));
        }
    }
    
    public void setPosition(final double x, final double y, final double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        final float f = this.width / 2.0f;
        final float f2 = this.height;
        this.setBoundingBox(new AxisAlignedBB(x - f, y, z - f, x + f, y + f2, z + f));
    }
    
    public void move(double x, double y, double z) {
        final double d0 = y;
        if (this.canCollide) {
            final List<AxisAlignedBB> list = this.world.getCollisionBoxes(null, this.getBoundingBox().expand(x, y, z));
            for (final AxisAlignedBB axisalignedbb : list) {
                y = axisalignedbb.calculateYOffset(this.getBoundingBox(), y);
            }
            this.setBoundingBox(this.getBoundingBox().offset(0.0, y, 0.0));
            for (final AxisAlignedBB axisalignedbb2 : list) {
                x = axisalignedbb2.calculateXOffset(this.getBoundingBox(), x);
            }
            this.setBoundingBox(this.getBoundingBox().offset(x, 0.0, 0.0));
            for (final AxisAlignedBB axisalignedbb3 : list) {
                z = axisalignedbb3.calculateZOffset(this.getBoundingBox(), z);
            }
            this.setBoundingBox(this.getBoundingBox().offset(0.0, 0.0, z));
        }
        else {
            this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        }
        this.resetPositionToBB();
        this.onGround = (y != y && d0 < 0.0);
        if (x != x) {
            this.motionX = 0.0;
        }
        if (z != z) {
            this.motionZ = 0.0;
        }
    }
    
    protected void resetPositionToBB() {
        final AxisAlignedBB axisalignedbb = this.getBoundingBox();
        this.posX = (axisalignedbb.minX + axisalignedbb.maxX) / 2.0;
        this.posY = axisalignedbb.minY;
        this.posZ = (axisalignedbb.minZ + axisalignedbb.maxZ) / 2.0;
    }
    
    public int getBrightnessForRender(final float partialTick) {
        final BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
        return this.world.isBlockLoaded(blockpos) ? this.world.getCombinedLight(blockpos, 0) : 0;
    }
    
    public boolean isAlive() {
        return !this.isExpired;
    }
    
    public AxisAlignedBB getBoundingBox() {
        return this.boundingBox;
    }
    
    public void setBoundingBox(final AxisAlignedBB bb) {
        this.boundingBox = bb;
    }
    
    static {
        EMPTY_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }
}
