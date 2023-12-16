// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics.impl;

import net.minecraft.client.model.ModelBase;
import moonsense.cosmetics.model.base.CosmeticModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import moonsense.cosmetics.base.CosmeticBase;

public class CosmeticSunglasses extends CosmeticBase
{
    private final GlassesRenderer glassesModel;
    
    public CosmeticSunglasses(final RenderPlayer renderPlayer) {
        super(renderPlayer);
        this.glassesModel = new GlassesRenderer(renderPlayer);
    }
    
    @Override
    public void render(final EntityLivingBase player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float headYaw, final float headPitch, final float scale) {
        if (player.isInvisible()) {
            return;
        }
        GL11.glPushMatrix();
        if (player.isSneaking()) {
            GlStateManager.translate(0.0, 0.225, 0.0);
        }
        GlStateManager.rotate(headYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(headPitch, 1.0f, 0.0f, 0.0f);
        this.glassesModel.render(player, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
        GL11.glPopMatrix();
    }
    
    @Override
    protected void setRotation(final ModelRenderer model, final float x, final float y, final float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
    
    public class GlassesRenderer extends CosmeticModelBase
    {
        ModelRenderer Glasses1;
        ModelRenderer Glasses2;
        ModelRenderer Glasses3;
        ModelRenderer Glasses4;
        ModelRenderer Glasses5;
        ModelRenderer Glasses6;
        ModelRenderer Glasses7;
        ModelRenderer Glasses8;
        ModelRenderer Glasses9;
        
        public GlassesRenderer(final RenderPlayer player) {
            super(player);
            this.textureWidth = 64;
            this.textureHeight = 32;
            (this.Glasses1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 2, 1);
            this.Glasses1.setRotationPoint(-5.0f, -3.0f, -5.0f);
            this.Glasses1.setTextureSize(64, 32);
            this.Glasses1.mirror = true;
            CosmeticSunglasses.this.setRotation(this.Glasses1, 0.0f, 0.0f, 0.0f);
            (this.Glasses2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 10, 1, 1);
            this.Glasses2.setRotationPoint(-5.0f, -3.0f, -5.0f);
            this.Glasses2.setTextureSize(64, 32);
            this.Glasses2.mirror = true;
            CosmeticSunglasses.this.setRotation(this.Glasses2, 0.0f, 0.0f, 0.0f);
            (this.Glasses3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 2, 1);
            this.Glasses3.setRotationPoint(1.0f, -3.0f, -5.0f);
            this.Glasses3.setTextureSize(64, 32);
            this.Glasses3.mirror = true;
            CosmeticSunglasses.this.setRotation(this.Glasses3, 0.0f, 0.0f, 0.0f);
            (this.Glasses4 = new ModelRenderer(this, 0, 0)).addBox(-3.0f, 0.0f, -2.0f, 1, 1, 6);
            this.Glasses4.setRotationPoint(-2.0f, -3.0f, -3.0f);
            this.Glasses4.setTextureSize(64, 32);
            this.Glasses4.mirror = true;
            CosmeticSunglasses.this.setRotation(this.Glasses4, 0.0f, 0.0f, 0.0f);
            (this.Glasses5 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 6);
            this.Glasses5.setRotationPoint(4.0f, -3.0f, -5.0f);
            this.Glasses5.setTextureSize(64, 32);
            this.Glasses5.mirror = true;
            CosmeticSunglasses.this.setRotation(this.Glasses5, 0.0f, 0.0f, 0.0f);
            (this.Glasses6 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
            this.Glasses6.setRotationPoint(4.0f, -2.0f, 1.0f);
            this.Glasses6.setTextureSize(64, 32);
            this.Glasses6.mirror = true;
            CosmeticSunglasses.this.setRotation(this.Glasses6, 0.0f, 0.0f, 0.0f);
            (this.Glasses7 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 1);
            this.Glasses7.setRotationPoint(-5.0f, -2.0f, 1.0f);
            this.Glasses7.setTextureSize(64, 32);
            this.Glasses7.mirror = true;
            CosmeticSunglasses.this.setRotation(this.Glasses7, 0.0f, 0.0f, 0.0f);
            (this.Glasses8 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 4, 2, 1);
            this.Glasses8.setRotationPoint(-5.0f, -3.0f, -5.0f);
            this.Glasses8.setTextureSize(64, 32);
            this.Glasses8.mirror = true;
            CosmeticSunglasses.this.setRotation(this.Glasses8, 0.0f, 0.0f, 0.0f);
            (this.Glasses9 = new ModelRenderer(this, 0, 0)).addBox(1.0f, -3.0f, -5.0f, 4, 2, 1);
            this.Glasses9.setRotationPoint(0.0f, 0.0f, 0.0f);
            this.Glasses9.setTextureSize(64, 32);
            this.Glasses9.mirror = true;
            CosmeticSunglasses.this.setRotation(this.Glasses9, 0.0f, 0.0f, 0.0f);
        }
        
        @Override
        public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmout, final float ageInTicks, final float headYaw, final float headPitch, final float scale) {
            GlStateManager.pushMatrix();
            GlStateManager.color(0.0f, 0.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.0625f, 0.0f);
            this.Glasses1.render(scale);
            this.Glasses2.render(scale);
            this.Glasses3.render(scale);
            this.Glasses4.render(scale);
            this.Glasses5.render(scale);
            this.Glasses6.render(scale);
            this.Glasses7.render(scale);
            this.Glasses8.render(scale);
            this.Glasses9.render(scale);
            GlStateManager.popMatrix();
        }
    }
}
