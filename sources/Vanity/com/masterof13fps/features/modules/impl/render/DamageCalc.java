package com.masterof13fps.features.modules.impl.render;

import com.masterof13fps.features.modules.Module;
import com.masterof13fps.features.modules.ModuleInfo;
import com.masterof13fps.utils.render.RenderUtils;
import com.masterof13fps.manager.eventmanager.Event;
import com.masterof13fps.manager.eventmanager.impl.EventRender;
import com.masterof13fps.features.modules.Category;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@ModuleInfo(name = "DamageCalc", category = Category.RENDER, description = "Calculates the fall damage and visualises it")
public class DamageCalc extends Module {
    @Override
    public void onToggle() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender) {
            double[] dist = {3.609D, 4.177D, 4.461D, 5.028D, 5.323D, 5.595D, 5.879D, 6.174D, 6.729D, 7.013D};
            double[] damage = {0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.5D, 2.0D, 2.5D, 3.5D, 4.0D};
            int max = 10;

            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glEnable(3553);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 200.0F, 0.0F);
            try {
                for (int y = 0; y <= max; y++) {
                    for (double x = -dist[y]; x <= dist[y]; x += 1.0D) {
                        for (double z = -dist[y]; z <= dist[y]; z += 1.0D) {
                            BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY - y, mc.thePlayer.posZ + z);
                            Block block = mc.theWorld.getBlockState(pos).getBlock();
                            if (block.isFullBlock() && !(block instanceof BlockSlime)) {
                                double xRender = pos.getX() - RenderManager.renderPosX;
                                double yRender = pos.getY() - RenderManager.renderPosY;
                                double zRender = pos.getZ() - RenderManager.renderPosZ;
                                double distBlock = Math.sqrt(x * x + z * z);

                                float red = 0.0F;
                                float green = 1.0F;
                                float blue = 0.0F;

                                red += y / 10.0F;
                                green -= y / 10.0F;

                                GL11.glPushMatrix();
                                GL11.glEnable(3042);
                                GL11.glBlendFunc(770, 771);
                                GL11.glDisable(3553);
                                GL11.glEnable(2848);
                                GL11.glDepthMask(false);

                                GL11.glColor4f(red, green, blue, 0.25F);
                                RenderUtils.drawBoundingBox(new AxisAlignedBB(xRender - 0.05D, yRender - 0.05D, zRender - 0.05D, xRender + 1.0D + 0.05D, yRender + 1.0D + 0.05D, zRender + 1.0D + 0.05D));
                                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                                GL11.glDisable(2848);
                                GL11.glEnable(3553);
                                GL11.glDepthMask(true);
                                GL11.glDisable(3042);
                                GL11.glPopMatrix();

                                GL11.glPushMatrix();
                                GL11.glEnable(3042);
                                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                                GlStateManager.disableLighting();
                                GlStateManager.enableBlend();
                                GL11.glBlendFunc(770, 771);
                                GL11.glDisable(3553);

                                float SCALE = (float) (Math.min(Math.max(1.2000000476837158D * (distBlock * 0.15000000596046448D), 1.25D), 6.0D) * 0.019999999552965164D);
                                GlStateManager.translate((float) xRender + 0.5D, (float) yRender + 1.5D, (float) zRender + 0.5D);
                                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                                GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
                                GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);

                                GL11.glScalef(-SCALE, -SCALE, SCALE);
                                GL11.glEnable(3553);
                                String s = String.valueOf(y - 1) + "m";
                                mc.fontRendererObj.drawStringWithShadow(s, -mc.fontRendererObj.getStringWidth(s) / 2, 0.0F, -1);
                                GlStateManager.translate(0.0F, 10.0F, 0.0F);
                                s = "Dmg : " + damage[y];
                                mc.fontRendererObj.drawStringWithShadow(s, -mc.fontRendererObj.getStringWidth(s) / 2, 0.0F, new Color(red, green, blue).getRGB());

                                GlStateManager.disableBlend();
                                GL11.glDisable(3042);
                                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                                GL11.glNormal3f(1.0F, 1.0F, 1.0F);
                                GL11.glPopMatrix();
                            }
                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
    }
}
