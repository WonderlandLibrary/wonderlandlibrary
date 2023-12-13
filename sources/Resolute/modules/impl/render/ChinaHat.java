// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import net.minecraft.client.renderer.entity.RenderManager;
import vip.Resolute.util.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.awt.Color;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.modules.Module;

public class ChinaHat extends Module
{
    public ColorSetting colorset;
    public NumberSetting pointsProp;
    public NumberSetting widthProp;
    public NumberSetting repeatProp;
    
    public ChinaHat() {
        super("ChinaHat", 0, "Epic hat", Category.RENDER);
        this.colorset = new ColorSetting("Color", new Color(2, 207, 167));
        this.pointsProp = new NumberSetting("Points", 20.0, 3.0, 100.0, 1.0);
        this.widthProp = new NumberSetting("Width", 5.0, 1.0, 10.0, 1.0);
        this.repeatProp = new NumberSetting("Repeat", 200.0, 10.0, 500.0, 5.0);
        this.addSettings(this.colorset, this.pointsProp, this.widthProp, this.repeatProp);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRender3D) {
            final EventRender3D eventRender3D = (EventRender3D)e;
            if (ChinaHat.mc.gameSettings.thirdPersonView != 0) {
                for (int i = 0; i < this.repeatProp.getValue(); ++i) {
                    this.drawHat(ChinaHat.mc.thePlayer, 0.001 + i * 0.004f, eventRender3D.getPartialTicks(), (int)this.pointsProp.getValue(), (float)this.widthProp.getValue(), (ChinaHat.mc.thePlayer.isSneaking() ? 2.0f : 2.15f) - i * 0.002f, this.colorset.getColor());
                }
            }
        }
    }
    
    public void drawHat(final Entity entity, final double radius, final float partialTicks, final int points, final float width, final float yAdd, final int color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        final double x = RenderUtils.interpolate(entity.prevPosX, entity.posX, partialTicks) - RenderManager.viewerPosX;
        final double y = RenderUtils.interpolate(entity.prevPosY + yAdd, entity.posY + yAdd, partialTicks) - RenderManager.viewerPosY;
        final double z = RenderUtils.interpolate(entity.prevPosZ, entity.posZ, partialTicks) - RenderManager.viewerPosZ;
        GL11.glColor4f(new Color(color).getRed() / 255.0f, new Color(color).getGreen() / 255.0f, new Color(color).getBlue() / 255.0f, 0.15f);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos(i * 6.283185307179586 / points), y, z + radius * Math.sin(i * 6.283185307179586 / points));
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
