/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.features.module.modules.render;

import java.awt.Color;
import java.util.LinkedList;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Render3DEvent;
import net.ccbluex.liquidbounce.event.UpdateEvent;
import net.ccbluex.liquidbounce.features.module.Module;
import net.ccbluex.liquidbounce.features.module.ModuleCategory;
import net.ccbluex.liquidbounce.features.module.ModuleInfo;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.ccbluex.liquidbounce.value.BoolValue;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.lwjgl.opengl.GL11;

@ModuleInfo(name="Breadcrumbs", description="Leaves a trail behind you.", category=ModuleCategory.RENDER)
public class Breadcrumbs
extends Module {
    public final IntegerValue colorRedValue = new IntegerValue("R", 255, 0, 255);
    public final IntegerValue colorGreenValue = new IntegerValue("G", 179, 0, 255);
    public final IntegerValue colorBlueValue = new IntegerValue("B", 72, 0, 255);
    public final BoolValue colorRainbow = new BoolValue("Rainbow", false);
    private final LinkedList<double[]> positions = new LinkedList();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onRender3D(Render3DEvent event) {
        Color color = (Boolean)this.colorRainbow.get() != false ? ColorUtils.rainbow() : new Color((Integer)this.colorRedValue.get(), (Integer)this.colorGreenValue.get(), (Integer)this.colorBlueValue.get());
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            GL11.glPushMatrix();
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glEnable((int)2848);
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            Breadcrumbs.mc.field_71460_t.func_175072_h();
            GL11.glBegin((int)3);
            RenderUtils.glColor(color);
            double renderPosX = Breadcrumbs.mc.func_175598_ae().field_78730_l;
            double renderPosY = Breadcrumbs.mc.func_175598_ae().field_78731_m;
            double renderPosZ = Breadcrumbs.mc.func_175598_ae().field_78728_n;
            for (double[] pos : this.positions) {
                GL11.glVertex3d((double)(pos[0] - renderPosX), (double)(pos[1] - renderPosY), (double)(pos[2] - renderPosZ));
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
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.add(new double[]{Breadcrumbs.mc.field_71439_g.field_70165_t, Breadcrumbs.mc.field_71439_g.func_174813_aQ().field_72338_b, Breadcrumbs.mc.field_71439_g.field_70161_v});
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
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.add(new double[]{Breadcrumbs.mc.field_71439_g.field_70165_t, Breadcrumbs.mc.field_71439_g.func_174813_aQ().field_72338_b + (double)(Breadcrumbs.mc.field_71439_g.func_70047_e() / 2.0f), Breadcrumbs.mc.field_71439_g.field_70161_v});
            this.positions.add(new double[]{Breadcrumbs.mc.field_71439_g.field_70165_t, Breadcrumbs.mc.field_71439_g.func_174813_aQ().field_72338_b, Breadcrumbs.mc.field_71439_g.field_70161_v});
        }
        super.onEnable();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onDisable() {
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.clear();
        }
        super.onDisable();
    }
}

