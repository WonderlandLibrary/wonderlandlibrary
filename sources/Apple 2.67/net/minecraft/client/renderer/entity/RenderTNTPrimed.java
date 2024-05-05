package net.minecraft.client.renderer.entity;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import appu26j.Apple;
import appu26j.mods.visuals.TNTCountdown;
import appu26j.utils.ServerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderTNTPrimed extends Render<EntityTNTPrimed>
{
    public RenderTNTPrimed(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
        this.shadowSize = 0.5F;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityTNTPrimed entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + 0.5F, (float)z);

        if ((float)entity.fuse - partialTicks + 1.0F < 10.0F)
        {
            float f = 1.0F - ((float)entity.fuse - partialTicks + 1.0F) / 10.0F;
            f = MathHelper.clamp_float(f, 0.0F, 1.0F);
            f = f * f;
            f = f * f;
            float f1 = 1.0F + f * 0.3F;
            GlStateManager.scale(f1, f1, f1);
        }

        float f2 = (1.0F - ((float)entity.fuse - partialTicks + 1.0F) / 100.0F) * 0.8F;
        this.bindEntityTexture(entity);
        GlStateManager.translate(-0.5F, -0.5F, 0.5F);
        blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), entity.getBrightness(partialTicks));
        GlStateManager.translate(0.0F, 0.0F, 1.0F);

        if (entity.fuse / 5 % 2 == 0)
        {
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0F, 1.0F, 1.0F, f2);
            GlStateManager.doPolygonOffset(-3.0F, -3.0F);
            GlStateManager.enablePolygonOffset();
            blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0F);
            GlStateManager.doPolygonOffset(0.0F, 0.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }

        GlStateManager.popMatrix();
        TNTCountdown tntCountdown = (TNTCountdown) Apple.CLIENT.getModsManager().getMod("TNT Countdown");
        
        if (tntCountdown.isEnabled())
        {
            int fuse = ServerUtil.tntExplodesEarly() ? entity.fuse - 28 : entity.fuse;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.0F, (float) y + entity.height + 0.5F, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            int xMultiplier = 1;
            
            if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2)
            {
                xMultiplier = -1;
            }
            
            float scale = 0.02666667F;
            GlStateManager.rotate(this.getRenderManager().playerViewX * xMultiplier, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-scale, -scale, scale);
            boolean lighting = GL11.glIsEnabled(GL11.GL_LIGHTING);
            boolean depth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
            
            if (lighting)
            {
                GlStateManager.disableLighting();
            }
            
            if (depth)
            {
                GlStateManager.disableDepth();
            }
            
            int seconds = ((fuse / 20) + 1);
            seconds = seconds > 4 ? 4 : seconds;
            Gui.drawRect(-getFontRendererFromRenderManager().getStringWidth("Explodes in " + seconds + "s") / 2 - 3, -3, getFontRendererFromRenderManager().getStringWidth("Explodes in " + seconds + "s") / 2 + 3, 10, new Color(25, 25, 40, 128).getRGB());
            this.getFontRendererFromRenderManager().drawString("Explodes in " + seconds + "s", -getFontRendererFromRenderManager().getStringWidth("Explodes in " + seconds + "s") / 2, 0, seconds == 4 ? new Color(0, 255, 128).getRGB() : seconds == 3 ? new Color(255, 255, 75).getRGB() : seconds == 2 ? new Color(255, 128, 25).getRGB() : new Color(255, 25, 25).getRGB());
            
            if (lighting)
            {
                GlStateManager.enableLighting();
            }
            
            if (depth)
            {
                GlStateManager.enableDepth();
            }
            
            GlStateManager.popMatrix();
        }
        
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityTNTPrimed entity)
    {
        return TextureMap.locationBlocksTexture;
    }
}
