package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import wtf.evolution.helpers.render.CapeHolder;
import wtf.evolution.helpers.render.SmoothCapeRender;


public class LayerCape implements LayerRenderer<AbstractClientPlayer>
{
    private final RenderPlayer playerRenderer;
    private final SmoothCapeRender smoothCapeRenderer;
    public LayerCape(RenderPlayer playerRendererIn, ModelBase modelBaseIn)
    {
        this.smoothCapeRenderer = new SmoothCapeRender();
        this.playerRenderer = playerRendererIn;
        this.buildMesh(modelBaseIn);
    }
    private void buildMesh(final ModelBase model) {
        for (int i = 0; i < 16; ++i) {
            final ModelRenderer base = new ModelRenderer(model, 0, i);
            base.setTextureSize(64, 32);
        }
    }

    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (! (entitylivingbaseIn instanceof EntityPlayerSP)) return;
        ((CapeHolder) entitylivingbaseIn).updateSimulation(entitylivingbaseIn, 16);
        this.playerRenderer.bindTexture(new ResourceLocation("skidproject/capee_lolz.png"));
        this.smoothCapeRenderer.renderSmoothCape(this, entitylivingbaseIn, partialTicks);
    }


    public float getNatrualWindSwing(int part) {

        long highlightedPart = (System.currentTimeMillis() / 3) % 360;
        float relativePart = (float) (part + 1) / 16;
        return (float) (Math.sin(Math.toRadians((relativePart) * 360 - (highlightedPart))) * 3);


    }


    /**
     * https://easings.net/#easeOutSine
     *
     * @param x
     * @return
     */
    private static float easeOutSine(float x) {
        return (float) Math.sin((x * Math.PI) / 2f);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }


}
