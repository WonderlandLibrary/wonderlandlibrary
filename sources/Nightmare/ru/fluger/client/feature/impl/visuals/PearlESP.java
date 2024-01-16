// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.Fluger;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.event.events.impl.render.EventRender3D;
import java.util.Iterator;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.helpers.render.RenderHelper;
import java.awt.Color;
import ru.fluger.client.helpers.math.MathematicHelper;
import ru.fluger.client.event.events.impl.render.EventRender2D;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import java.util.ArrayList;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import java.util.List;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.feature.Feature;

public class PearlESP extends Feature
{
    public static BooleanSetting pearlPrediction;
    private final BooleanSetting triangleESP;
    public static List<PredictionLine> lines;
    public static aex entityPearl;
    
    public PearlESP() {
        super("PearlESP", "\u0420\u0438\u0441\u0443\u0435\u0442 \u0435\u0441\u043f \u0438 \u043b\u0438\u043d\u0438\u044e \u043a \u044d\u043d\u0434\u0435\u0440-\u043f\u0435\u0440\u043b\u0443", Type.Visuals);
        PearlESP.pearlPrediction = new BooleanSetting("Pearl Prediction", false, () -> true);
        this.triangleESP = new BooleanSetting("Triangle ESP", true, () -> true);
        this.addSettings(this.triangleESP, PearlESP.pearlPrediction);
    }
    
    public static void handleEntityPrediction(final aep proj) {
        if (proj instanceof aex) {
            final aex ent = PearlESP.entityPearl = (aex)proj;
            double sx = ent.p;
            double sy = ent.q;
            double sz = ent.r;
            double mx = ent.s;
            double my = ent.t;
            double mz = ent.u;
            mx += ent.k().s;
            mz += ent.k().u;
            if (!ent.k().z) {
                my += ent.k().t;
            }
            final int maxUpdateTicks = 250;
            int updateTicks = 250;
            final ArrayList<PredictionPosition> positions = new ArrayList<PredictionPosition>();
            while (updateTicks > 0) {
                final bhe vec3d = new bhe(sx, sy, sz);
                if (--updateTicks != 250) {
                    final int cnt = updateTicks % 83;
                    final float p = cnt / 83.333336f;
                    float trg = 0.0f;
                    trg = ((p > 0.5f) ? (1.0f - p * 2.0f) : (p * 2.0f));
                    final bhe color = new bhe(0.3f + 0.4f * trg, 0.5f - 0.4f * trg, 0.8999999761581421);
                    final PredictionPosition pos = new PredictionPosition(vec3d, color);
                    positions.add(pos);
                }
                final bhe vec3d2 = new bhe(sx + mx, sy + my, sz + mz);
                final bhc raytraceresult = ent.l.a(vec3d, vec3d2);
                sx += mx;
                sy += my;
                sz += mz;
                final float f1 = 0.99f;
                final float f2 = ent.j();
                mx *= f1;
                my *= f1;
                mz *= f1;
                if (!ent.aj()) {
                    my -= f2;
                }
                if (raytraceresult == null) {
                    continue;
                }
                final bhe color2 = new bhe(1.0, 1.0, 1.0);
                final PredictionPosition pos2 = new PredictionPosition(new bhe(sx + mx, sy + my, sz + mz), color2);
                positions.add(pos2);
                break;
            }
            addLine(positions, ent);
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (!PearlESP.pearlPrediction.getCurrentValue()) {
            return;
        }
        PearlESP.lines.removeIf(PredictionLine::remove);
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D event) {
        if (!this.triangleESP.getCurrentValue()) {
            return;
        }
        final bit sr = new bit(PearlESP.mc);
        final float size = 50.0f;
        final float xOffset = sr.a() / 2.0f - 24.5f;
        final float yOffset = sr.b() / 2.0f - 25.2f;
        for (final vg entity : PearlESP.mc.f.e) {
            if (entity != null) {
                if (!(entity instanceof aex)) {
                    continue;
                }
                bus.G();
                bus.l();
                final double x = entity.M + (entity.p - entity.M) * PearlESP.mc.Y.renderPartialTicks - PearlESP.mc.ac().o;
                final double z = entity.O + (entity.r - entity.O) * PearlESP.mc.Y.renderPartialTicks - PearlESP.mc.ac().q;
                final double cos = Math.cos(PearlESP.mc.h.v * 0.017453292519943295);
                final double sin = Math.sin(PearlESP.mc.h.v * 0.017453292519943295);
                final double rotY = -(z * cos - x * sin);
                final double rotX = -(x * cos + z * sin);
                final float angle = (float)(Math.atan2(rotY - 0.0, rotX - 0.0) * 180.0 / 3.141592653589793);
                final double xPos = size / 2.0f * Math.cos(Math.toRadians(angle)) + xOffset + size / 2.0f;
                final double y = size / 2.0f * Math.sin(Math.toRadians(angle)) + yOffset + size / 2.0f;
                bus.b(xPos, y, 0.0);
                bus.b(angle, 0.0f, 0.0f, 1.0f);
                final String distance = MathematicHelper.round(PearlESP.mc.h.g(entity), 2) + "m";
                RenderHelper.drawTriangle(5.0f, 1.0f, 7.0f, 90.0f, new Color(5, 5, 5, 150).getRGB());
                RenderHelper.drawTriangle(5.0f, 1.0f, 6.0f, 90.0f, ClientHelper.getClientColor().getRGB());
                PearlESP.mc.k.a(distance, -2.0f, 9.0f, -1);
                bus.H();
            }
        }
    }
    
    @EventTarget
    public void onRender(final EventRender3D event) {
        if (!PearlESP.pearlPrediction.getCurrentValue()) {
            return;
        }
        final double ix = -(PearlESP.mc.h.M + (PearlESP.mc.h.p - PearlESP.mc.h.M) * event.getPartialTicks());
        final double iy = -(PearlESP.mc.h.N + (PearlESP.mc.h.q - PearlESP.mc.h.N) * event.getPartialTicks());
        final double iz = -(PearlESP.mc.h.O + (PearlESP.mc.h.r - PearlESP.mc.h.O) * event.getPartialTicks());
        GL11.glPushMatrix();
        GL11.glTranslated(ix, iy, iz);
        GL11.glDisable(3008);
        GL11.glDisable(2884);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(1);
        for (final PredictionLine line : PearlESP.lines) {
            final List<PredictionPosition> positions = line.positions;
            for (int i = 0; i < positions.size(); ++i) {
                if (positions.size() > i + 1) {
                    final PredictionPosition c = positions.get(i);
                    final PredictionPosition n = positions.get(i + 1);
                    final int color = ClientHelper.getClientColor().getRGB();
                    bus.c(new Color(color).getRed() / 255.0f, new Color(color).getGreen() / 255.0f, new Color(color).getBlue() / 255.0f, new Color(color).getAlpha() / 255.0f);
                    GL11.glVertex3d(c.vector.b, c.vector.c, c.vector.d);
                    GL11.glVertex3d(n.vector.b, n.vector.c, n.vector.d);
                    bus.c(1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    static void addLine(final List<PredictionPosition> positions, final vg predictable) {
        PearlESP.lines.add(new PredictionLine(positions, predictable));
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D eventRender3D) {
        if (PearlESP.mc.h == null || PearlESP.mc.f == null) {
            return;
        }
        for (final vg entity : PearlESP.mc.f.e) {
            if (entity != null) {
                if (!(entity instanceof aex)) {
                    continue;
                }
                bus.G();
                RenderHelper.drawEntityBox(entity, ClientHelper.getClientColor(), true, 1.0f);
                if (!PearlESP.pearlPrediction.getCurrentValue()) {
                    tracersEsp(entity, eventRender3D.getPartialTicks(), ClientHelper.getClientColor().getRGB());
                }
                bus.H();
            }
        }
    }
    
    public static void tracersEsp(final vg entity, final float partialTicks, final int color) {
        final boolean old = PearlESP.mc.t.f;
        PearlESP.mc.t.f = false;
        PearlESP.mc.o.a(partialTicks, 2);
        PearlESP.mc.t.f = old;
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.5f);
        bus.l();
        bus.c(1.0f, 1.0f, 1.0f, 1.0f);
        final double x = entity.M + (entity.p - entity.M) * partialTicks - PearlESP.mc.ac().h;
        final double y = entity.N + (entity.q - entity.N) * partialTicks - PearlESP.mc.ac().i;
        final double z = entity.O + (entity.r - entity.O) * partialTicks - PearlESP.mc.ac().j;
        if (Fluger.instance.friendManager.isFriend(entity.h_()) && Tracers.friend.getCurrentValue()) {
            bus.c(0.0f, 255.0f, 0.0f, 255.0f);
        }
        else {
            bus.c(new Color(color).getRed() / 255.0f, new Color(color).getGreen() / 255.0f, new Color(color).getBlue() / 255.0f, new Color(color).getAlpha() / 255.0f);
        }
        bhe vec3d = new bhe(0.0, 0.0, 1.0);
        vec3d = vec3d.a((float)(-Math.toRadians(PearlESP.mc.h.w)));
        final bhe vec3d2 = vec3d.b(-(float)Math.toRadians(PearlESP.mc.h.v));
        GL11.glBegin(2);
        GL11.glVertex3d(vec3d2.b, PearlESP.mc.h.by() + vec3d2.c, vec3d2.d);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
    }
    
    static {
        PearlESP.lines = new ArrayList<PredictionLine>();
    }
    
    static class PredictionPosition
    {
        bhe vector;
        bhe color;
        
        PredictionPosition(final bhe vector, final bhe color) {
            this.vector = vector;
            this.color = color;
        }
    }
    
    static class PredictionLine
    {
        List<PredictionPosition> positions;
        int ownerID;
        
        PredictionLine(final List<PredictionPosition> positions, final vg predictable) {
            this.positions = positions;
            this.ownerID = predictable.S();
        }
        
        boolean remove() {
            final vg target = bib.z().f.a(this.ownerID);
            if (!this.positions.isEmpty()) {
                this.positions.remove(0);
            }
            return this.positions.isEmpty() || target == null || target.F;
        }
    }
}
