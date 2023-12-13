package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.Blocks;
import org.lwjgl.opengl.GL11;

public class LayerIronGolemFlower implements LayerRenderer<EntityIronGolem>
{
    private final RenderIronGolem ironGolemRenderer;

    public LayerIronGolemFlower(RenderIronGolem ironGolemRendererIn)
    {
        this.ironGolemRenderer = ironGolemRendererIn;
    }

    public void doRenderLayer(EntityIronGolem entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (entitylivingbaseIn.getHoldRoseTick() != 0)
        {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.enableRescaleNormal();
            GL11.glPushMatrix();
            GL11.glRotatef(5.0F + 180.0F * ((ModelIronGolem)this.ironGolemRenderer.getMainModel()).ironGolemRightArm.rotateAngleX / (float)Math.PI, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(-0.9375F, -0.625F, -0.9375F);
            float f = 0.5F;
            GL11.glScalef(f, -f, f);
            int i = entitylivingbaseIn.getBrightnessForRender(partialTicks);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.ironGolemRenderer.bindTexture(TextureMap.locationBlocksTexture);
            blockrendererdispatcher.renderBlockBrightness(Blocks.red_flower.getDefaultState(), 1.0F);
            GL11.glPopMatrix();
            GlStateManager.disableRescaleNormal();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
