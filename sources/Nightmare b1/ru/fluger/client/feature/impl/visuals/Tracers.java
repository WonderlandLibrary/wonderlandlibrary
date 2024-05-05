// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.EventTarget;
import java.util.Iterator;
import ru.fluger.client.helpers.render.RenderHelper;
import ru.fluger.client.Fluger;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.event.events.impl.render.EventRender3D;
import ru.fluger.client.settings.Setting;
import java.awt.Color;
import ru.fluger.client.feature.impl.Type;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.feature.Feature;

public class Tracers extends Feature
{
    private final ColorSetting colorGlobal;
    private final ColorSetting friendColor;
    public static BooleanSetting friend;
    private final BooleanSetting onlyPlayer;
    private final NumberSetting width;
    private final BooleanSetting seeOnly;
    
    public Tracers() {
        super("Tracers", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u043b\u0438\u043d\u0438\u044e \u043a \u0438\u0433\u0440\u043e\u043a\u0430\u043c", Type.Visuals);
        this.onlyPlayer = new BooleanSetting("Only Player", true, () -> true);
        this.seeOnly = new BooleanSetting("See Only", false, () -> true);
        Tracers.friend = new BooleanSetting("Friend Highlight", true, () -> true);
        this.friendColor = new ColorSetting("Friend Color", new Color(0, 255, 0).getRGB(), Tracers.friend::getCurrentValue);
        this.colorGlobal = new ColorSetting("Tracers Color", new Color(16777215).getRGB(), () -> true);
        this.width = new NumberSetting("Tracers Width", 1.5f, 0.1f, 5.0f, 0.1f, () -> true);
        this.addSettings(this.colorGlobal, Tracers.friend, this.friendColor, this.seeOnly, this.onlyPlayer, this.width);
    }
    
    public static boolean canSeeEntityAtFov(final vg entityLiving, final float scope) {
        final double diffX = entityLiving.p - Tracers.mc.h.p;
        final double diffZ = entityLiving.r - Tracers.mc.h.r;
        final float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        final double difference = angleDifference(yaw, Tracers.mc.h.v);
        return difference <= scope;
    }
    
    public static double angleDifference(final float oldYaw, final float newYaw) {
        float yaw = Math.abs(oldYaw - newYaw) % 360.0f;
        if (yaw > 180.0f) {
            yaw = 360.0f - yaw;
        }
        return yaw;
    }
    
    @EventTarget
    public void onEvent3D(final EventRender3D event) {
        for (final vg entity : Tracers.mc.f.e) {
            if (entity != Tracers.mc.h) {
                if (this.onlyPlayer.getCurrentValue() && !(entity instanceof aed)) {
                    continue;
                }
                if (this.seeOnly.getCurrentValue() && !canSeeEntityAtFov(entity, 150.0f)) {
                    return;
                }
                final boolean old = Tracers.mc.t.f;
                Tracers.mc.t.f = false;
                Tracers.mc.o.a(event.getPartialTicks(), 0);
                Tracers.mc.t.f = old;
                final double x = entity.M + (entity.p - entity.M) * event.getPartialTicks() - Tracers.mc.ac().o;
                final double y = entity.N + (entity.q - entity.N) * event.getPartialTicks() - Tracers.mc.ac().p - 1.0;
                final double z = entity.O + (entity.r - entity.O) * event.getPartialTicks() - Tracers.mc.ac().q;
                bus.G();
                bus.b(770, 771);
                GL11.glEnable(3042);
                GL11.glEnable(2848);
                bus.d(this.width.getCurrentValue());
                GL11.glDisable(3553);
                GL11.glDisable(2929);
                bus.a(false);
                if (Fluger.instance.friendManager.isFriend(entity.h_()) && Tracers.friend.getCurrentValue()) {
                    RenderHelper.color(new Color(this.friendColor.getColor()));
                }
                else {
                    RenderHelper.color(new Color(this.colorGlobal.getColor()));
                }
                bus.r(3);
                final bhe vec = new bhe(0.0, 0.0, 1.0).a((float)(-Math.toRadians(Tracers.mc.h.w))).b((float)(-Math.toRadians(Tracers.mc.h.v)));
                GL11.glVertex3d(vec.b, Tracers.mc.h.by() + vec.c, vec.d);
                GL11.glVertex3d(x, y + 1.1, z);
                bus.J();
                GL11.glEnable(3553);
                GL11.glDisable(2848);
                GL11.glEnable(2929);
                bus.a(true);
                GL11.glDisable(3042);
                bus.I();
                bus.H();
            }
        }
    }
}
