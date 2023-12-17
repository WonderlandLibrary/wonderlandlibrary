// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class ParticleFootStep extends Particle
{
    private static final ResourceLocation FOOTPRINT_TEXTURE;
    private int footstepAge;
    private final int footstepMaxAge;
    private final TextureManager currentFootSteps;
    
    protected ParticleFootStep(final TextureManager currentFootStepsIn, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.currentFootSteps = currentFootStepsIn;
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.footstepMaxAge = 200;
    }
    
    @Override
    public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        float f = (this.footstepAge + partialTicks) / this.footstepMaxAge;
        f *= f;
        float f2 = 2.0f - f * 2.0f;
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        f2 *= 0.2f;
        GlStateManager.disableLighting();
        final float f3 = 0.125f;
        final float f4 = (float)(this.posX - ParticleFootStep.interpPosX);
        final float f5 = (float)(this.posY - ParticleFootStep.interpPosY);
        final float f6 = (float)(this.posZ - ParticleFootStep.interpPosZ);
        final float f7 = this.world.getLightBrightness(new BlockPos(this.posX, this.posY, this.posZ));
        this.currentFootSteps.bindTexture(ParticleFootStep.FOOTPRINT_TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(f4 - 0.125f, f5, f6 + 0.125f).tex(0.0, 1.0).color(f7, f7, f7, f2).endVertex();
        buffer.pos(f4 + 0.125f, f5, f6 + 0.125f).tex(1.0, 1.0).color(f7, f7, f7, f2).endVertex();
        buffer.pos(f4 + 0.125f, f5, f6 - 0.125f).tex(1.0, 0.0).color(f7, f7, f7, f2).endVertex();
        buffer.pos(f4 - 0.125f, f5, f6 - 0.125f).tex(0.0, 0.0).color(f7, f7, f7, f2).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
    }
    
    @Override
    public void onUpdate() {
        ++this.footstepAge;
        if (this.footstepAge == this.footstepMaxAge) {
            this.setExpired();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 3;
    }
    
    static {
        FOOTPRINT_TEXTURE = new ResourceLocation("textures/particle/footprint.png");
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleFootStep(Minecraft.getMinecraft().getTextureManager(), worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}
