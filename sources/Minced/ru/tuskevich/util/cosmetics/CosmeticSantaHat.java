// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.cosmetics;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class CosmeticSantaHat extends Cosmetic
{
    private static final ResourceLocation witchTextures;
    private final ModelRenderer witchHat;
    
    public CosmeticSantaHat(final RenderPlayer playerRendererIn) {
        super(playerRendererIn);
        final ModelBiped modelBiped = new ModelBiped();
        this.witchHat = new ModelRenderer(modelBiped).setTextureSize(32, 32);
        final ModelRenderer modelrenderer = new ModelRenderer(modelBiped).setTextureSize(32, 32);
        modelrenderer.setRotationPoint(-1.0f, 3.2f, 0.0f);
        modelrenderer.setTextureOffset(0, 8).addBox(-1.5f, -10.0f, -2.0f, 5, 2, 5);
        modelrenderer.setTextureOffset(0, 0).addBox(-1.0f, -12.0f, -1.5f, 4, 3, 4);
        modelrenderer.setTextureOffset(16, 4).addBox(3.0f, -12.5f, -0.5f, 2, 4, 2);
        modelrenderer.setTextureOffset(12, 0).addBox(5.0f, -9.0f, 0.0f, 2, 2, 2);
        this.witchHat.addChild(modelrenderer);
        this.witchHat.isHidden = true;
    }
    
    @Override
    public void render(final AbstractClientPlayer entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float HeadPitch, final float scale) {
        Minecraft.getMinecraft();
        if (entitylivingbaseIn == Minecraft.player) {
            GlStateManager.pushMatrix();
            final float x = 0.0f;
            Minecraft.getMinecraft();
            GlStateManager.translate(x, Minecraft.player.isSneaking() ? 0.77f : 0.0f, 0.0f);
            final float f = 1.695f;
            GlStateManager.scale(f, f, f);
            this.playerRenderer.bindTexture(CosmeticSantaHat.witchTextures);
            if (entitylivingbaseIn.isSneaking()) {
                GL11.glTranslated(0.0, -0.3, 0.0);
            }
            GlStateManager.rotate(netHeadYaw, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(HeadPitch, 1.0f, 0.0f, 0.0f);
            this.witchHat.isHidden = false;
            this.witchHat.render(scale);
            this.witchHat.isHidden = true;
            GlStateManager.popMatrix();
        }
    }
    
    static {
        witchTextures = new ResourceLocation("client/santa.png");
    }
}
