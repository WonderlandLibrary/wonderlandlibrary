// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderMooshroom;
import net.minecraft.entity.passive.EntityMooshroom;

public class LayerMooshroomMushroom implements LayerRenderer<EntityMooshroom>
{
    private final RenderMooshroom mooshroomRenderer;
    private ModelRenderer modelRendererMushroom;
    private static final ResourceLocation LOCATION_MUSHROOM_RED;
    private static boolean hasTextureMushroom;
    
    public static void update() {
        LayerMooshroomMushroom.hasTextureMushroom = Config.hasResource(LayerMooshroomMushroom.LOCATION_MUSHROOM_RED);
    }
    
    public LayerMooshroomMushroom(final RenderMooshroom mooshroomRendererIn) {
        this.mooshroomRenderer = mooshroomRendererIn;
        (this.modelRendererMushroom = new ModelRenderer(this.mooshroomRenderer.mainModel)).setTextureSize(16, 16);
        this.modelRendererMushroom.rotationPointX = -6.0f;
        this.modelRendererMushroom.rotationPointZ = -8.0f;
        this.modelRendererMushroom.rotateAngleY = MathHelper.PI / 4.0f;
        final int[][] aint = { null, null, { 16, 16, 0, 0 }, { 16, 16, 0, 0 }, null, null };
        this.modelRendererMushroom.addBox(aint, 0.0f, 0.0f, 10.0f, 20.0f, 16.0f, 0.0f, 0.0f);
        final int[][] aint2 = { null, null, null, null, { 16, 16, 0, 0 }, { 16, 16, 0, 0 } };
        this.modelRendererMushroom.addBox(aint2, 10.0f, 0.0f, 0.0f, 0.0f, 16.0f, 20.0f, 0.0f);
    }
    
    @Override
    public void doRenderLayer(final EntityMooshroom entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        if (!entitylivingbaseIn.isChild() && !entitylivingbaseIn.isInvisible()) {
            final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            if (LayerMooshroomMushroom.hasTextureMushroom) {
                this.mooshroomRenderer.bindTexture(LayerMooshroomMushroom.LOCATION_MUSHROOM_RED);
            }
            else {
                this.mooshroomRenderer.bindTexture(TextureMap.locationBlocksTexture);
            }
            GlStateManager.enableCull();
            GlStateManager.cullFace(1028);
            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f, -1.0f, 1.0f);
            GlStateManager.translate(0.2f, 0.35f, 0.5f);
            GlStateManager.rotate(42.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5f, -0.5f, 0.5f);
            if (LayerMooshroomMushroom.hasTextureMushroom) {
                this.modelRendererMushroom.render(0.0625f);
            }
            else {
                blockrendererdispatcher.renderBlockBrightness(Blocks.red_mushroom.getDefaultState(), 1.0f);
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.1f, 0.0f, -0.6f);
            GlStateManager.rotate(42.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(-0.5f, -0.5f, 0.5f);
            if (LayerMooshroomMushroom.hasTextureMushroom) {
                this.modelRendererMushroom.render(0.0625f);
            }
            else {
                blockrendererdispatcher.renderBlockBrightness(Blocks.red_mushroom.getDefaultState(), 1.0f);
            }
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            ((ModelQuadruped)this.mooshroomRenderer.getMainModel()).head.postRender(0.0625f);
            GlStateManager.scale(1.0f, -1.0f, 1.0f);
            GlStateManager.translate(0.0f, 0.7f, -0.2f);
            GlStateManager.rotate(12.0f, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(-0.5f, -0.5f, 0.5f);
            if (LayerMooshroomMushroom.hasTextureMushroom) {
                this.modelRendererMushroom.render(0.0625f);
            }
            else {
                blockrendererdispatcher.renderBlockBrightness(Blocks.red_mushroom.getDefaultState(), 1.0f);
            }
            GlStateManager.popMatrix();
            GlStateManager.cullFace(1029);
            GlStateManager.disableCull();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return true;
    }
    
    static {
        LOCATION_MUSHROOM_RED = new ResourceLocation("textures/entity/cow/mushroom_red.png");
        LayerMooshroomMushroom.hasTextureMushroom = false;
    }
}
