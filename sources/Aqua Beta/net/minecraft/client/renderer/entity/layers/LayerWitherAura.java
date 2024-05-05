package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class LayerWitherAura
implements LayerRenderer<EntityWither> {
    private static final ResourceLocation WITHER_ARMOR = new ResourceLocation("textures/entity/wither/wither_armor.png");
    private final RenderWither witherRenderer;
    private final ModelWither witherModel = new ModelWither(0.5f);

    public LayerWitherAura(RenderWither witherRendererIn) {
        this.witherRenderer = witherRendererIn;
    }

    public void doRenderLayer(EntityWither entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        if (entitylivingbaseIn.isArmored()) {
            GlStateManager.depthMask((!entitylivingbaseIn.isInvisible() ? 1 : 0) != 0);
            this.witherRenderer.bindTexture(WITHER_ARMOR);
            GlStateManager.matrixMode((int)5890);
            GlStateManager.loadIdentity();
            float f = (float)entitylivingbaseIn.ticksExisted + partialTicks;
            float f1 = MathHelper.cos((float)(f * 0.02f)) * 3.0f;
            float f2 = f * 0.01f;
            GlStateManager.translate((float)f1, (float)f2, (float)0.0f);
            GlStateManager.matrixMode((int)5888);
            GlStateManager.enableBlend();
            float f3 = 0.5f;
            GlStateManager.color((float)f3, (float)f3, (float)f3, (float)1.0f);
            GlStateManager.disableLighting();
            GlStateManager.blendFunc((int)1, (int)1);
            this.witherModel.setLivingAnimations((EntityLivingBase)entitylivingbaseIn, p_177141_2_, p_177141_3_, partialTicks);
            this.witherModel.setModelAttributes(this.witherRenderer.getMainModel());
            this.witherModel.render((Entity)entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
            GlStateManager.matrixMode((int)5890);
            GlStateManager.loadIdentity();
            GlStateManager.matrixMode((int)5888);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
        }
    }

    public boolean shouldCombineTextures() {
        return false;
    }
}
