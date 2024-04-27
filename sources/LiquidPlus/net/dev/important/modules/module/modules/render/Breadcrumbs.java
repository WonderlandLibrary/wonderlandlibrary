/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.modules.module.modules.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Render3DEvent;
import net.dev.important.event.UpdateEvent;
import net.dev.important.modules.module.Category;
import net.dev.important.modules.module.Info;
import net.dev.important.modules.module.Module;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.dev.important.value.BoolValue;
import net.dev.important.value.FloatValue;
import net.dev.important.value.IntegerValue;
import org.lwjgl.opengl.GL11;

@Info(name="Breadcrumbs", description="Leaves a trail behind you.", category=Category.RENDER, cnName="\u8f68\u8ff9")
public class Breadcrumbs
extends Module {
    public final BoolValue unlimitedValue = new BoolValue("Unlimited", false);
    public final FloatValue lineWidth = new FloatValue("LineWidth", 0.0f, 1.0f, 10.0f);
    public final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    public final IntegerValue colorGreenValue = new IntegerValue("G", 179, 0, 255);
    public final IntegerValue colorBlueValue = new IntegerValue("B", 72, 0, 255);
    public final IntegerValue fadeSpeedValue = new IntegerValue("Fade-Speed", 25, 0, 255);
    public final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    private final LinkedList<Dot> positions = new LinkedList();
    private double lastX;
    private double lastY;
    private double lastZ = 0.0;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onRender3D(Render3DEvent event) {
        Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get());
        LinkedList<Dot> linkedList = this.positions;
        synchronized (linkedList) {
            GL11.glPushMatrix();
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            Breadcrumbs.mc.field_71460_t.func_175072_h();
            GL11.glLineWidth((float)((Float)this.lineWidth.get()).floatValue());
            GL11.glBegin((int)3);
            double renderPosX = Breadcrumbs.mc.func_175598_ae().field_78730_l;
            double renderPosY = Breadcrumbs.mc.func_175598_ae().field_78731_m;
            double renderPosZ = Breadcrumbs.mc.func_175598_ae().field_78728_n;
            ArrayList<Dot> removeQueue = new ArrayList<Dot>();
            for (Dot dot : this.positions) {
                if (dot.alpha > 0) {
                    dot.render(color, renderPosX, renderPosY, renderPosZ, (Boolean)this.unlimitedValue.get() != false ? 0 : (Integer)this.fadeSpeedValue.get());
                    continue;
                }
                removeQueue.add(dot);
            }
            for (Dot removeDot : removeQueue) {
                this.positions.remove(removeDot);
            }
            GL11.glColor4d((double)1.0, (double)1.0, (double)1.0, (double)1.0);
            GL11.glEnd();
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glDisable((int)3042);
            GL11.glEnable((int)3553);
            GL11.glPopMatrix();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onUpdate(UpdateEvent event) {
        LinkedList<Dot> linkedList = this.positions;
        synchronized (linkedList) {
            if (Breadcrumbs.mc.field_71439_g.field_70165_t != this.lastX || Breadcrumbs.mc.field_71439_g.func_174813_aQ().field_72338_b != this.lastY || Breadcrumbs.mc.field_71439_g.field_70161_v != this.lastZ) {
                this.positions.add(new Dot(new double[]{Breadcrumbs.mc.field_71439_g.field_70165_t, Breadcrumbs.mc.field_71439_g.func_174813_aQ().field_72338_b, Breadcrumbs.mc.field_71439_g.field_70161_v}));
                this.lastX = Breadcrumbs.mc.field_71439_g.field_70165_t;
                this.lastY = Breadcrumbs.mc.field_71439_g.func_174813_aQ().field_72338_b;
                this.lastZ = Breadcrumbs.mc.field_71439_g.field_70161_v;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onEnable() {
        if (Breadcrumbs.mc.field_71439_g == null) {
            return;
        }
        LinkedList<Dot> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.add(new Dot(new double[]{Breadcrumbs.mc.field_71439_g.field_70165_t, Breadcrumbs.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)(Breadcrumbs.mc.field_71439_g.func_70047_e() * 0.5f), Breadcrumbs.mc.field_71439_g.field_70161_v}));
            this.positions.add(new Dot(new double[]{Breadcrumbs.mc.field_71439_g.field_70165_t, Breadcrumbs.mc.field_71439_g.func_174813_aQ().field_72338_b, Breadcrumbs.mc.field_71439_g.field_70161_v}));
        }
        super.onEnable();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onDisable() {
        LinkedList<Dot> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.clear();
        }
        super.onDisable();
    }

    class Dot {
        public int alpha = 255;
        private final double[] pos;

        public Dot(double[] position) {
            this.pos = position;
        }

        public void render(Color color, double renderPosX, double renderPosY, double renderPosZ, int decreaseBy) {
            Color reColor = ColorUtils.reAlpha(color, this.alpha);
            RenderUtils.glColor(reColor);
            GL11.glVertex3d((double)(this.pos[0] - renderPosX), (double)(this.pos[1] - renderPosY), (double)(this.pos[2] - renderPosZ));
            this.alpha -= decreaseBy;
            if (this.alpha < 0) {
                this.alpha = 0;
            }
        }
    }
}

