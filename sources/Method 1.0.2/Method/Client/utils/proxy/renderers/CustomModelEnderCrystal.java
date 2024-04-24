package Method.Client.utils.proxy.renderers;

import Method.Client.module.combat.CrystalRender;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class CustomModelEnderCrystal extends ModelBase {
    private final ModelRenderer cube;
    private final ModelRenderer glass = new ModelRenderer(this, "glass");
    private ModelRenderer base;

    public CustomModelEnderCrystal(float p_i1170_1_, boolean renderBase) {
        this.glass.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
        this.cube = new ModelRenderer(this, "cube");
        this.cube.setTextureOffset(32, 0).addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);

        if (renderBase) {
            this.base = new ModelRenderer(this, "base");
            this.base.setTextureOffset(0, 16).addBox(-6.0F, 0.0F, -6.0F, 12, 4, 12);
        }
    }

    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        GlStateManager.translate(0.0F, -0.5F, 0.0F);

        if (this.base != null) {
            if (CrystalRender.RenderBase.getValBoolean())
                this.base.render(scale);
        }

        if (CrystalRender.NoSpin.getValBoolean())
            limbSwingAmount = 0;
        GlStateManager.rotate(limbSwingAmount, 0.0F, 1.0F, 0.0F);
        if (CrystalRender.NoMotion.getValBoolean())
            ageInTicks = .2F;
        GlStateManager.translate(0.0F, 0.8F + ageInTicks, 0.0F);
        GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
        if (CrystalRender.RenderGlass.getValBoolean())
            this.glass.render(scale);
        float f = 0.875F;
        GlStateManager.scale(0.875F, 0.875F, 0.875F);
        GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
        GlStateManager.rotate(limbSwingAmount, 0.0F, 1.0F, 0.0F);
        if (CrystalRender.RenderGlass.getValBoolean())
            this.glass.render(scale);
        GlStateManager.scale(0.875F, 0.875F, 0.875F);
        GlStateManager.rotate(60.0F, 0.7071F, 0.0F, 0.7071F);
        GlStateManager.rotate(limbSwingAmount, 0.0F, 1.0F, 0.0F);
        if (CrystalRender.RenderCube.getValBoolean())
            this.cube.render(scale);
        GlStateManager.popMatrix();
    }
}
