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

public class Tracers
extends Module {
    public Tracers() {
        super("Tracers", "Draw lines to all players", 0, Category.RENDER);
    }

    @Override
    public void onEvent(Event ev) {
        if (ev instanceof RenderEvent) {
            for (Object o2 : this.mc.theWorld.loadedEntityList) {
                Entity e2;
                if (!(o2 instanceof Entity) || !((e2 = (Entity)o2) instanceof EntityPlayer) || e2 == this.mc.thePlayer) continue;
                this.tracer(e2, Color.cyan);
            }
        }
    }

    private void tracer(Entity e2, Color color) {
        GlStateManager.pushMatrix();
        GL11.glDisable(2929);
        GL11.glDisable(2896);
        GL11.glDisable(3553);
        GL11.glColor4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        GL11.glLineWidth(1.0f);
        GL11.glBegin(2);
        GL11.glVertex3d(0.0, this.mc.thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d(e2.posX - this.mc.thePlayer.posX, e2.posY - this.mc.thePlayer.posY, e2.posZ - this.mc.thePlayer.posZ);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2896);
        GL11.glEnable(2929);
        GlStateManager.popMatrix();
    }
}

