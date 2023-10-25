package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderWolf;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.optifine.CustomColors;

public class LayerWolfCollar
implements LayerRenderer<EntityWolf> {
    private static final ResourceLocation WOLF_COLLAR = new ResourceLocation("textures/entity/wolf/wolf_collar.png");
    private final RenderWolf wolfRenderer;

    public LayerWolfCollar(RenderWolf wolfRendererIn) {
        this.wolfRenderer = wolfRendererIn;
    }

    public void doRenderLayer(EntityWolf entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale) {
        if (entitylivingbaseIn.isTamed() && !entitylivingbaseIn.isInvisible()) {
            this.wolfRenderer.bindTexture(WOLF_COLLAR);
            EnumDyeColor enumdyecolor = EnumDyeColor.byMetadata((int)entitylivingbaseIn.getCollarColor().getMetadata());
            float[] afloat = EntitySheep.getDyeRgb((EnumDyeColor)enumdyecolor);
            if (Config.isCustomColors()) {
                afloat = CustomColors.getWolfCollarColors((EnumDyeColor)enumdyecolor, (float[])afloat);
            }
            GlStateManager.color((float)afloat[0], (float)afloat[1], (float)afloat[2]);
            this.wolfRenderer.getMainModel().render((Entity)entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        }
    }

    public boolean shouldCombineTextures() {
        return true;
    }
}
