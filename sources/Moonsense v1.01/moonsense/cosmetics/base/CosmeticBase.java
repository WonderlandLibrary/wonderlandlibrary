// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics.base;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public abstract class CosmeticBase implements LayerRenderer
{
    protected final RenderPlayer playerRenderer;
    protected final Minecraft mc;
    
    public CosmeticBase(final RenderPlayer playerRenderer) {
        this.mc = Minecraft.getMinecraft();
        this.playerRenderer = playerRenderer;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (!player.isInvisible()) {
            this.render(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    public abstract void render(final EntityLivingBase p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7);
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    private static float Sigmoid(final double value) {
        return 1.0f / (1.0f + (float)Math.exp(-value));
    }
    
    protected float getWingAngle(final boolean isFlying, final float maxAngle, final int totalTime, final int flyingTime, final int offset) {
        float angle = 0.0f;
        int flapTime = totalTime;
        if (isFlying) {
            flapTime = flyingTime;
        }
        final float deltaTime = this.getAnimationTime(flapTime, offset);
        if (deltaTime <= 0.5f) {
            angle = Sigmoid(-4.0f + deltaTime * 2.0f * 8.0f);
        }
        else {
            angle = 1.0f - Sigmoid(-4.0f + (deltaTime * 2.0f - 1.0f) * 8.0f);
        }
        angle *= maxAngle;
        return angle;
    }
    
    protected void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
    
    private float getAnimationTime(final int totalTime, final int offset) {
        final float time = (float)((System.currentTimeMillis() + offset) % totalTime);
        return time / totalTime;
    }
}
