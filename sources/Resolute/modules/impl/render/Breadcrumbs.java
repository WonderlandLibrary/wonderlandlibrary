// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import vip.Resolute.util.render.RenderUtils;
import net.minecraft.client.renderer.entity.RenderManager;
import vip.Resolute.events.impl.EventRender3D;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.awt.Color;
import java.util.ArrayList;
import vip.Resolute.settings.impl.ColorSetting;
import vip.Resolute.settings.impl.NumberSetting;
import net.minecraft.util.Vec3;
import java.util.List;
import vip.Resolute.modules.Module;

public class Breadcrumbs extends Module
{
    private final List<Vec3> breadcrumbs;
    public NumberSetting size;
    public NumberSetting length;
    public ColorSetting color;
    
    public Breadcrumbs() {
        super("Breadcrumbs", 0, "Renders a line where you've been", Category.RENDER);
        this.breadcrumbs = new ArrayList<Vec3>();
        this.size = new NumberSetting("Size", 1.0, 1.0, 4.0, 1.0);
        this.length = new NumberSetting("Length", 200.0, 100.0, 1000.0, 10.0);
        this.color = new ColorSetting("Color", new Color(255, 255, 255));
        this.addSettings(this.length, this.color);
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.breadcrumbs.clear();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.breadcrumbs.clear();
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventRender3D) {
            final EventRender3D event = (EventRender3D)e;
            final RenderManager manager = Breadcrumbs.mc.getRenderManager();
            final double rx = RenderManager.renderPosX;
            final double ry = RenderManager.renderPosY;
            final double rz = RenderManager.renderPosZ;
            if (this.breadcrumbs.size() >= this.length.getValue()) {
                this.breadcrumbs.remove(0);
            }
            final double x = RenderUtils.interpolate(Breadcrumbs.mc.thePlayer.prevPosX, Breadcrumbs.mc.thePlayer.posX, event.getPartialTicks());
            final double y = RenderUtils.interpolate(Breadcrumbs.mc.thePlayer.prevPosY, Breadcrumbs.mc.thePlayer.posY, event.getPartialTicks());
            final double z = RenderUtils.interpolate(Breadcrumbs.mc.thePlayer.prevPosZ, Breadcrumbs.mc.thePlayer.posZ, event.getPartialTicks());
            this.breadcrumbs.add(new Vec3(x, y, z));
            GL11.glTranslated(-rx, -ry, -rz);
            GL11.glDisable(3553);
            GL11.glDisable(2929);
            RenderUtils.enableBlending();
            GL11.glEnable(2848);
            GL11.glDepthMask(false);
            GL11.glLineWidth((float)this.size.getValue());
            RenderUtils.color(this.color.getColor());
            GL11.glBegin(3);
            Vec3 lastCrumb = null;
            for (final Vec3 breadcrumb : this.breadcrumbs) {
                if (lastCrumb != null && lastCrumb.distanceTo(breadcrumb) > Math.sqrt(3.0)) {
                    GL11.glEnd();
                    GL11.glBegin(3);
                }
                GL11.glVertex3d(breadcrumb.xCoord, breadcrumb.yCoord, breadcrumb.zCoord);
                lastCrumb = breadcrumb;
            }
            GL11.glEnd();
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glDisable(3042);
            GL11.glDepthMask(true);
            GL11.glTranslated(rx, ry, rz);
        }
    }
}
