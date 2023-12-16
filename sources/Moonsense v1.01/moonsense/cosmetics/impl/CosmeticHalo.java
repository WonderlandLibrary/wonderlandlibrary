// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.cosmetics.impl;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import moonsense.cosmetics.model.base.CosmeticModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import moonsense.cosmetics.base.CosmeticBase;

public class CosmeticHalo extends CosmeticBase
{
    private final HaloRenderer haloModel;
    
    public CosmeticHalo(final RenderPlayer renderPlayer) {
        super(renderPlayer);
        this.haloModel = new HaloRenderer(renderPlayer);
    }
    
    @Override
    public void render(final EntityLivingBase player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (player.isInvisible()) {
            return;
        }
        GL11.glPushMatrix();
        if (player.isSneaking()) {
            GlStateManager.translate(0.0, 0.225, 0.0);
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("streamlined/cosmetics/halo.png"));
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        this.haloModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GL11.glPopMatrix();
    }
    
    public class HaloRenderer extends CosmeticModelBase
    {
        ModelRenderer head;
        ModelRenderer body;
        ModelRenderer rightarm;
        ModelRenderer leftarm;
        ModelRenderer rightleg;
        ModelRenderer leftleg;
        ModelRenderer Halo1;
        ModelRenderer Halo2;
        ModelRenderer Halo3;
        ModelRenderer Halo4;
        
        public HaloRenderer(final RenderPlayer player) {
            super(player);
            this.textureWidth = 64;
            this.textureHeight = 32;
            (this.Halo1 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 10, 1, 1);
            this.Halo1.setRotationPoint(-5.0f, -11.0f, 4.0f);
            this.Halo1.setTextureSize(64, 32);
            this.Halo1.mirror = true;
            (this.Halo2 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 10, 1, 1);
            this.Halo2.setRotationPoint(-5.0f, -11.0f, -5.0f);
            this.Halo2.setTextureSize(64, 32);
            this.Halo2.mirror = true;
            (this.Halo3 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 8);
            this.Halo3.setRotationPoint(4.0f, -11.0f, -4.0f);
            this.Halo3.setTextureSize(64, 32);
            this.Halo3.mirror = true;
            (this.Halo4 = new ModelRenderer(this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 1, 1, 8);
            this.Halo4.setRotationPoint(-5.0f, -11.0f, -4.0f);
            this.Halo4.setTextureSize(64, 32);
            this.Halo4.mirror = true;
        }
        
        @Override
        public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float HeadYaw, final float headPitch, final float scale) {
            GlStateManager.pushMatrix();
            final float f1 = ageInTicks / 100.0f;
            final float f2 = (float)(f1 * 3.141592653589793 * 1.0);
            GlStateManager.translate(0.0f, -(float)(Math.sin(f2 + 2.0f) + 0.5) * 0.08f, 0.0f);
            GlStateManager.scale(0.9, 0.9, 0.9);
            this.Halo1.render(scale);
            this.Halo2.render(scale);
            this.Halo3.render(scale);
            this.Halo4.render(scale);
            GlStateManager.popMatrix();
        }
    }
}
