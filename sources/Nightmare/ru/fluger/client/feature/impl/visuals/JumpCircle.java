// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.event.events.impl.render.EventRender3D;
import ru.fluger.client.event.EventTarget;
import ru.fluger.client.event.events.impl.player.EventUpdate;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.ListSetting;
import java.util.List;
import ru.fluger.client.feature.Feature;

public class JumpCircle extends Feature
{
    static final int TYPE = 0;
    static final byte MAX_JC_TIME = 20;
    static List<Circle> circles;
    private ListSetting jumpcircleMode;
    public static ColorSetting jumpCircleColor;
    static float pt;
    
    public JumpCircle() {
        super("JumpCircles", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u043a\u0440\u0443\u0433\u0438 \u043f\u043e\u0441\u043b\u0435 \u043f\u0440\u044b\u0436\u043a\u0430", Type.Visuals);
        this.jumpcircleMode = new ListSetting("JumpCircle Mode", "Default", () -> true, new String[] { "Default", "Disc" });
        this.addSettings(this.jumpcircleMode, JumpCircle.jumpCircleColor);
    }
    
    @EventTarget
    public void onJump(final EventUpdate event) {
        if (JumpCircle.mc.h.t == 0.33319999363422365 && !JumpCircle.mc.h.otherCheck()) {
            handleEntityJump(JumpCircle.mc.h);
        }
        onLocalPlayerUpdate(JumpCircle.mc.h);
    }
    
    @EventTarget
    public void onRender(final EventRender3D event) {
        final String mode = this.jumpcircleMode.getOptions();
        final bud client = bib.z().h;
        final bib mc = bib.z();
        final double ix = -(client.M + (client.p - client.M) * mc.aj());
        final double iy = -(client.N + (client.q - client.N) * mc.aj());
        final double iz = -(client.O + (client.r - client.O) * mc.aj());
        if (mode.equalsIgnoreCase("Disc")) {
            GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(2884);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(3008);
            GL11.glDisable(2929);
            GL11.glBlendFunc(770, 771);
            GL11.glShadeModel(7425);
            Collections.reverse(JumpCircle.circles);
            try {
                for (final Circle c : JumpCircle.circles) {
                    final float k = c.existed / 20.0f;
                    final double x = c.position().b;
                    final double y = c.position().c - k * 0.5;
                    final double z = c.position().d;
                    final float start = k;
                    final float end = start + 1.0f - k;
                    GL11.glBegin(8);
                    for (int i = 0; i <= 360; i += 5) {
                        GL11.glColor4f((float)c.color().b, (float)c.color().c, (float)c.color().d, 0.2f * (1.0f - c.existed / 20.0f));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 4)) * start, y, z + Math.sin(Math.toRadians(i * 4)) * start);
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.01f * (1.0f - c.existed / 20.0f));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y + Math.sin(k * 8.0f) * 0.5, z + Math.sin(Math.toRadians(i) * end));
                    }
                    GL11.glEnd();
                }
            }
            catch (Exception ex) {}
            Collections.reverse(JumpCircle.circles);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glShadeModel(7424);
            GL11.glEnable(2884);
            GL11.glEnable(2929);
            GL11.glEnable(3008);
            bus.I();
            GL11.glPopMatrix();
        }
        else if (mode.equalsIgnoreCase("Default")) {
            GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(2884);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(3008);
            GL11.glBlendFunc(770, 771);
            GL11.glShadeModel(7425);
            Collections.reverse(JumpCircle.circles);
            for (final Circle c : JumpCircle.circles) {
                final double x2 = c.position().b;
                final double y2 = c.position().c;
                final double z2 = c.position().d;
                final float j = c.existed / 20.0f;
                final float start = j * 1.5f;
                final float end = start + 0.5f - j;
                GL11.glBegin(8);
                for (int i = 0; i <= 360; i += 5) {
                    GL11.glColor4f((float)c.color().b, (float)c.color().c, (float)c.color().d, 0.7f * (1.0f - c.existed / 20.0f));
                    switch (false) {
                        case 0: {
                            GL11.glVertex3d(x2 + Math.cos(Math.toRadians(i)) * start, y2, z2 + Math.sin(Math.toRadians(i)) * start);
                            break;
                        }
                        case 1: {
                            GL11.glVertex3d(x2 + Math.cos(Math.toRadians(i * 2)) * start, y2, z2 + Math.sin(Math.toRadians(i * 2)) * start);
                            break;
                        }
                    }
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.01f * (1.0f - c.existed / 20.0f));
                    switch (false) {
                        case 0: {
                            GL11.glVertex3d(x2 + Math.cos(Math.toRadians(i)) * end, y2, z2 + Math.sin(Math.toRadians(i)) * end);
                            break;
                        }
                        case 1: {
                            GL11.glVertex3d(x2 + Math.cos(Math.toRadians(-i)) * end, y2, z2 + Math.sin(Math.toRadians(-i)) * end);
                            break;
                        }
                    }
                }
                GL11.glEnd();
            }
            Collections.reverse(JumpCircle.circles);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glShadeModel(7424);
            GL11.glEnable(2884);
            GL11.glEnable(3008);
            bus.I();
            GL11.glPopMatrix();
        }
    }
    
    public static void onLocalPlayerUpdate(final bud instance) {
        JumpCircle.circles.removeIf(Circle::update);
    }
    
    public static void handleEntityJump(final vg entity) {
        final int red = (int)((JumpCircle.jumpCircleColor.getColor() >> 16 & 0xFF) / 100.0f);
        final int green = (int)((JumpCircle.jumpCircleColor.getColor() >> 8 & 0xFF) / 100.0f);
        final int blue = (int)((JumpCircle.jumpCircleColor.getColor() & 0xFF) / 100.0f);
        final bhe color = new bhe(red, green, blue);
        JumpCircle.circles.add(new Circle(entity.d(), color));
    }
    
    static {
        JumpCircle.circles = new ArrayList<Circle>();
        JumpCircle.jumpCircleColor = new ColorSetting("JumpCircle Color", new Color(16777215).getRGB(), () -> true);
    }
    
    static class Circle
    {
        private final bhe vec;
        private final bhe color;
        byte existed;
        
        Circle(final bhe vec, final bhe color) {
            this.vec = vec;
            this.color = color;
        }
        
        bhe position() {
            return this.vec;
        }
        
        bhe color() {
            return this.color;
        }
        
        boolean update() {
            final byte existed = (byte)(this.existed + 1);
            this.existed = existed;
            return existed > 20;
        }
    }
}
