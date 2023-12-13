// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.render;

import java.util.Iterator;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import vip.Resolute.events.impl.EventRender2D;
import vip.Resolute.util.movement.MovementUtils;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import java.util.Collection;
import java.util.Collections;
import vip.Resolute.settings.impl.NumberSetting;
import java.util.ArrayList;
import vip.Resolute.modules.Module;

public class Graph extends Module
{
    int maxHeight;
    private ArrayList<Double> speeds;
    int ticks;
    public NumberSetting height;
    
    public Graph() {
        super("Graph", 0, "Renders a speed graph", Category.RENDER);
        this.maxHeight = 30;
        this.speeds = new ArrayList<Double>(Collections.nCopies(50, 0.0));
        this.ticks = 5;
        this.height = new NumberSetting("Height", 30.0, 5.0, 50.0, 1.0);
        this.addSettings(this.height);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion) {
            if (!Graph.mc.thePlayer.isEntityAlive()) {
                return;
            }
            final double d = MovementUtils.getSpeed() * 20.0f;
            if (this.speeds.size() > this.ticks) {
                this.speeds.remove(0);
            }
            this.speeds.add(d);
        }
        if (e instanceof EventRender2D) {
            int n = 1;
            final int n2 = 160;
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glBlendFunc(770, 771);
            GL11.glDepthMask(true);
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glHint(3155, 4354);
            GL11.glBegin(7);
            GlStateManager.color(0.015686275f, 0.015686275f, 0.015686275f, 0.0f);
            GL11.glVertex2f(1.0f, (float)(n2 - this.height.getValue() - 2.0));
            GL11.glVertex2f(1.0f, (float)n2);
            GL11.glVertex2f((float)(2 + this.speeds.size()), (float)n2);
            GL11.glVertex2f((float)(2 + this.speeds.size()), (float)(n2 - this.height.getValue() - 2.0));
            GL11.glEnd();
            GL11.glEnable(2848);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glBegin(3);
            float f = (float)n2;
            for (final double d2 : this.speeds) {
                final float f2 = MathHelper.clamp_float((float)(n2 - d2 * 2.0), (float)(160.0 - this.height.getValue()), 160.0f);
                GL11.glVertex2f((float)n, f);
                GL11.glVertex2f((float)n, f2);
                f = f2;
                ++n;
            }
            GL11.glEnd();
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glEnable(2929);
            GL11.glDisable(2848);
            GL11.glHint(3154, 4352);
            GL11.glHint(3155, 4352);
        }
    }
}
