package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LayerCreeperCharge extends LayerRenderer<EntityCreeper>
{
    private static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation("textures/entity/creeper/creeper_armor.png");
    private final RenderCreeper creeperRenderer;
    private final ModelCreeper creeperModel = new ModelCreeper(2.0F);

    public LayerCreeperCharge(RenderCreeper creeperRendererIn)
    {
        this.creeperRenderer = creeperRendererIn;
    }

    public void doRenderLayer(EntityCreeper entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
    {
        if (entitylivingbaseIn.getPowered())
        {
            boolean flag = entitylivingbaseIn.isInvisible();
            GL11.glDepthMask(!flag);
            this.creeperRenderer.bindTexture(LIGHTNING_TEXTURE);
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            float f = (float)entitylivingbaseIn.ticksExisted + partialTicks;
            GL11.glTranslatef(f * 0.01F, f * 0.01F, 0.0F);
            GL11.glMatrixMode(5888);
            GL11.glEnable(GL11.GL_BLEND);
            float f1 = 0.5F;
            GL11.glColor4f(f1, f1, f1, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glBlendFunc(1, 1);
            this.creeperModel.setModelAttributes(this.creeperRenderer.getMainModel());
            this.creeperModel.render(entitylivingbaseIn, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
            GL11.glMatrixMode(5890);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(5888);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(flag);
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}
