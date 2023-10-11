package com.initial.modules.impl.visual;

import com.initial.modules.*;
import com.initial.events.impl.*;
import com.initial.utils.astolfo.*;
import net.minecraft.entity.*;
import com.initial.events.*;
import org.lwjgl.opengl.*;
import com.initial.utils.render.*;
import net.minecraft.client.renderer.entity.*;
import java.awt.*;

public class ChinaHat extends Module
{
    public ChinaHat() {
        super("ChinaHat", 0, Category.VISUAL);
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event) {
        if (this.mc.gameSettings.thirdPersonView != 0) {
            for (int i = 0; i < 400; ++i) {
                drawHat(this.mc.thePlayer, 0.009 + i * 0.0014, this.mc.timer.elapsedPartialTicks, 12, 2.0f, 2.2f - i * 7.85E-4f - 0.03f, AstolfoUtils.rainbow(-100, 1.0f, 0.47f));
            }
        }
    }
    
    public static void drawHat(final Entity entity, final double radius, final float partialTicks, final int points, final float width, final float yAdd, final int color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDepthMask(false);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2929);
        GL11.glBegin(3);
        final double x = Render2DUtils.interpolate(entity.prevPosX, entity.posX, partialTicks) - RenderManager.viewerPosX;
        final double y = Render2DUtils.interpolate(entity.prevPosY + yAdd, entity.posY + yAdd, partialTicks) - RenderManager.viewerPosY;
        final double z = Render2DUtils.interpolate(entity.prevPosZ, entity.posZ, partialTicks) - RenderManager.viewerPosZ;
        GL11.glColor4f(new Color(color).getRed() / 255.0f, new Color(color).getGreen() / 255.0f, new Color(color).getBlue() / 255.0f, 0.15f);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos(i * 3.141592653589793 * 2.0 / points), y, z + radius * Math.sin(i * 3.141592653589793 * 2.0 / points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
}
