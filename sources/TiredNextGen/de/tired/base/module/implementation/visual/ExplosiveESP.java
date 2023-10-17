package de.tired.base.module.implementation.visual;

import de.tired.base.annotations.ModuleAnnotation;
import de.tired.base.module.Module;
import de.tired.base.module.ModuleCategory;
import de.tired.util.render.RenderUtil;
import de.tired.base.guis.newclickgui.setting.NumberSetting;
import de.tired.base.event.EventTarget;
import de.tired.base.event.events.Render3DEvent2;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;

import java.awt.*;

@ModuleAnnotation(name = "ExplosiveESP", category = ModuleCategory.RENDER)
public class ExplosiveESP extends Module {

    private final NumberSetting size = new NumberSetting("Size", this, 8, 1, 20, 1);

    @EventTarget
    public void onRender(Render3DEvent2 event2) {
        for (Entity entity : MC.theWorld.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed) {
                final EntityTNTPrimed tntPrimed = (EntityTNTPrimed) entity;
                final double posX = tntPrimed.posX - RenderManager.renderPosX, posY = tntPrimed.posY - RenderManager.renderPosY, posZ = tntPrimed.posZ - RenderManager.renderPosZ;

                GL11.glPushMatrix();
                GL11.glTranslated(posX, posY, posZ);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_BLEND);

                RenderUtil.instance.color(new Color(252, 2, 2, 166));
                Sphere sphere = new Sphere();
                sphere.setDrawStyle(GLU.GLU_FILL);
                sphere.draw(size.getValueInt(), 15, 15);

                Sphere lines = new Sphere();
                lines.setDrawStyle(GLU.GLU_LINE);
                lines.draw(size.getValueInt(), 15, 15);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();

                GlStateManager.resetColor();

            }
        }
    }

    @Override
    public void onState() {

    }

    @Override
    public void onUndo() {

    }
}
