package net.minecraft.client.renderer.entity.layers;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.monster.EntityEnderman;
import org.lwjgl.opengl.GL11;

public class LayerHeldBlock implements LayerRenderer<EntityEnderman>
{
    private final RenderEnderman endermanRenderer;

    public LayerHeldBlock(RenderEnderman endermanRendererIn)
    {
        this.endermanRenderer = endermanRendererIn;
    }

    public void doRenderLayer(EntityEnderman entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        IBlockState iblockstate = entitylivingbaseIn.getHeldBlockState();

        if (iblockstate.getBlock().getMaterial() != Material.air)
        {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.enableRescaleNormal();
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.6875F, -0.75F);
            GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.25F, 0.1875F, 0.25F);
            float f = 0.5F;
            GL11.glScalef(-f, -f, f);
            int i = entitylivingbaseIn.getBrightnessForRender(partialTicks);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.endermanRenderer.bindTexture(TextureMap.locationBlocksTexture);
            blockrendererdispatcher.renderBlockBrightness(iblockstate, 1.0F);
            GL11.glPopMatrix();
            GlStateManager.disableRescaleNormal();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
