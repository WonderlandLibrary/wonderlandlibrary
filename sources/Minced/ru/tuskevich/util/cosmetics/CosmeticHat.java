// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.cosmetics;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;

public class CosmeticHat extends CosmeticBase
{
    private final ModelHat modelHat;
    private static final ResourceLocation TEXTURE;
    
    public CosmeticHat(final RenderPlayer renderPlayer) {
        super(renderPlayer);
        this.modelHat = new ModelHat(renderPlayer);
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float climbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.pushMatrix();
        this.playerRenderer.bindTexture(CosmeticHat.TEXTURE);
        GL11.glColor3f(1.0f, 0.0f, 0.0f);
        this.modelHat.render(player, climbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    static {
        TEXTURE = new ResourceLocation("client/hat.png");
    }
    
    public class ModelHat extends CosmeticModelBase
    {
        private ModelRenderer rim;
        private ModelRenderer pointy;
        
        public ModelHat(final RenderPlayer player) {
            super(player);
            (this.rim = new ModelRenderer(this.playerModel)).addBox(-5.5f, -9.0f, -5.5f, 11, 2, 11);
            (this.pointy = new ModelRenderer(this.playerModel, 0, 13)).addBox(-3.5f, -17.0f, -3.5f, 7, 8, 7);
        }
        
        @Override
        public void render(final Entity entityIn, final float climbSwing, final float limbSwingAmount, final float ageInTicks, final float headYaw, final float headPitch, final float scale) {
            this.rim.rotateAngleX = this.playerModel.bipedHead.rotateAngleX;
            this.rim.rotateAngleY = this.playerModel.bipedHead.rotateAngleY;
            this.rim.rotationPointX = 0.0f;
            this.rim.rotationPointY = 0.0f;
            this.rim.render(scale);
            this.pointy.rotateAngleX = this.playerModel.bipedHead.rotateAngleX;
            this.pointy.rotateAngleY = this.playerModel.bipedHead.rotateAngleY;
            this.pointy.rotationPointX = 0.0f;
            this.pointy.rotationPointY = 0.0f;
            this.pointy.render(scale);
        }
    }
}
