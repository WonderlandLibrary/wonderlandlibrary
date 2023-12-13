/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;
import tk.rektsky.event.Event;
import tk.rektsky.event.impl.RenderEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;

public class Esp2d
extends Module {
    public static String highlighted = "";
    public BooleanSetting rainbow = new BooleanSetting("Rainbow", true);
    public BooleanSetting fdp = new BooleanSetting("FDP ESP", true);

    public Esp2d() {
        super("ESP", "Shows where players are", 0, Category.RENDER);
    }

    @Override
    public void onEvent(Event e2) {
        if (e2 instanceof RenderEvent) {
            for (Entity o2 : this.mc.theWorld.getLoadedEntityList()) {
                String ree;
                if (!(o2 instanceof EntityPlayer) || o2 == this.mc.thePlayer) continue;
                Color color = Color.WHITE;
                if (this.fdp.getValue().booleanValue() && o2.getName().length() > "F1D1P1".length() && (ree = o2.getName().toCharArray()[0] + "" + o2.getName().toCharArray()[2] + o2.getName().toCharArray()[4]).equals("FDP")) {
                    this.esp2d(o2, Color.RED, 104);
                    return;
                }
                if (this.rainbow.getValue().booleanValue()) {
                    int sec = 4;
                    float hue = (float)(System.currentTimeMillis() % (long)(sec * 1000)) / (float)(sec * 1000);
                    color = Color.getHSBColor(hue, 1.0f, 1.0f);
                }
                this.esp2d(o2, color);
            }
        }
    }

    public void esp2d(Entity e2, Color color) {
        this.esp2d(e2, color, 2);
    }

    public void esp2d(Entity e2, Color color, int w2) {
        GlStateManager.pushMatrix();
        GL11.glTranslated(e2.posX - this.mc.renderManager.viewerPosX, e2.posY - this.mc.renderManager.viewerPosY, e2.posZ - this.mc.renderManager.viewerPosZ);
        GL11.glRotatef(-this.mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glTranslated(-(e2.posX - this.mc.renderManager.viewerPosX), -(e2.posY - this.mc.renderManager.viewerPosY), -(e2.posZ - this.mc.renderManager.viewerPosZ));
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDisable(2912);
        GlStateManager.color(255.0f, 255.0f, 255.0f);
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), 255.0f);
        GL11.glLineWidth(w2);
        GL11.glBegin(2);
        GL11.glVertex3d(e2.posX - this.mc.renderManager.viewerPosX - (double)e2.width, e2.posY - this.mc.renderManager.viewerPosY, e2.posZ - this.mc.renderManager.viewerPosZ);
        GL11.glVertex3d(e2.posX - this.mc.renderManager.viewerPosX + (double)e2.width, e2.posY - this.mc.renderManager.viewerPosY, e2.posZ - this.mc.renderManager.viewerPosZ);
        GL11.glVertex3d(e2.posX - this.mc.renderManager.viewerPosX + (double)e2.width, e2.posY - this.mc.renderManager.viewerPosY + (double)e2.height, e2.posZ - this.mc.renderManager.viewerPosZ);
        GL11.glVertex3d(e2.posX - this.mc.renderManager.viewerPosX - (double)e2.width, e2.posY - this.mc.renderManager.viewerPosY + (double)e2.height, e2.posZ - this.mc.renderManager.viewerPosZ);
        GL11.glEnd();
        GL11.glRotatef(-this.mc.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(this.mc.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glEnable(2929);
        GL11.glEnable(2912);
        GL11.glEnable(3553);
        GlStateManager.popMatrix();
    }
}

