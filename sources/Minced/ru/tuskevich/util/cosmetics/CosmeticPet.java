// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.cosmetics;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;

public class CosmeticPet extends CosmeticBase
{
    private final ModelHat modelHat;
    
    public CosmeticPet(final RenderPlayer renderPlayer) {
        super(renderPlayer);
        this.modelHat = new ModelHat(renderPlayer);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float climbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.pushMatrix();
        GL11.glScaled(0.7, 0.7, 0.7);
        GlStateManager.translate(-0.3f, 0.0f, 0.0f);
        this.playerRenderer.bindTexture(new ResourceLocation("client/amogus.png"));
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        this.modelHat.render(player, climbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        this.playerRenderer.bindTexture(new ResourceLocation("client/amogus2.png"));
        this.modelHat.render2(player, climbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
}
