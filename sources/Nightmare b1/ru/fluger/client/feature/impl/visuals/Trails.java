// 
// Decompiled by Procyon v0.5.36
// 

package ru.fluger.client.feature.impl.visuals;

import ru.fluger.client.event.EventTarget;
import java.util.Iterator;
import ru.fluger.client.Fluger;
import org.lwjgl.opengl.GL11;
import ru.fluger.client.event.events.impl.render.EventRender3D;
import ru.fluger.client.helpers.palette.PaletteHelper;
import ru.fluger.client.settings.Setting;
import ru.fluger.client.helpers.misc.ClientHelper;
import ru.fluger.client.feature.impl.Type;
import java.util.ArrayList;
import ru.fluger.client.settings.impl.BooleanSetting;
import ru.fluger.client.settings.impl.ColorSetting;
import ru.fluger.client.settings.impl.ListSetting;
import ru.fluger.client.settings.impl.NumberSetting;
import ru.fluger.client.feature.Feature;

public class Trails extends Feature
{
    public static NumberSetting sizeToRemove;
    public static NumberSetting trailsAlpha;
    public static ListSetting trailsMode;
    public static ColorSetting topColor;
    public static ColorSetting bottomColor;
    private final BooleanSetting hideInFirstPerson;
    static final ArrayList<Trail> trails;
    
    public Trails() {
        super("Trails", "\u041e\u0441\u0442\u0430\u0432\u043b\u044f\u0435\u0442 \u043b\u0438\u043d\u0438\u044e \u0445\u043e\u0434\u044c\u0431\u044b", Type.Visuals);
        Trails.sizeToRemove = new NumberSetting("Ticks", 300.0f, 10.0f, 500.0f, 10.0f, () -> true);
        Trails.topColor = new ColorSetting("Top Color", ClientHelper.getClientColor().getRGB(), () -> Trails.trailsMode.currentMode.equals("Custom"));
        Trails.bottomColor = new ColorSetting("Bottom Color", ClientHelper.getClientColor().getRGB(), () -> Trails.trailsMode.currentMode.equals("Custom"));
        Trails.trailsAlpha = new NumberSetting("Trails Alpha", 0.9f, 0.1f, 1.0f, 0.1f, () -> true);
        this.hideInFirstPerson = new BooleanSetting("Hide In First Person", false, () -> true);
        this.addSettings(Trails.sizeToRemove, Trails.trailsMode, Trails.topColor, Trails.bottomColor, Trails.trailsAlpha, this.hideInFirstPerson);
    }
    
    static void createTrail() {
        bhe color = null;
        final String currentMode = Trails.trailsMode.currentMode;
        switch (currentMode) {
            case "Client": {
                color = new bhe(ClientHelper.getClientColor().getRed() / 255.0f, ClientHelper.getClientColor().getGreen() / 255.0f, ClientHelper.getClientColor().getBlue() / 255.0f);
                break;
            }
            case "Custom": {
                final int oneColor = Trails.bottomColor.getColor();
                final float red = (oneColor >> 16 & 0xFF) / 255.0f;
                final float green = (oneColor >> 8 & 0xFF) / 255.0f;
                final float blue = (oneColor & 0xFF) / 255.0f;
                color = new bhe(red, green, blue);
                break;
            }
            case "Astolfo": {
                color = new bhe(PaletteHelper.astolfo(false, 1).getRed() / 255.0f, PaletteHelper.astolfo(false, 1).getGreen() / 255.0f, PaletteHelper.astolfo(false, 1).getBlue() / 255.0f);
                break;
            }
            case "Rainbow": {
                color = new bhe(PaletteHelper.rainbow(300, 1.0f, 1.0f).getRed() / 255.0f, PaletteHelper.rainbow(300, 1.0f, 1.0f).getGreen() / 255.0f, PaletteHelper.rainbow(300, 1.0f, 1.0f).getBlue() / 255.0f);
                break;
            }
        }
        final double x = Trails.mc.h.M + (Trails.mc.h.p - Trails.mc.h.M) * Trails.mc.aj();
        final double y = Trails.mc.h.N + (Trails.mc.h.q - Trails.mc.h.N) * Trails.mc.aj();
        final double z = Trails.mc.h.O + (Trails.mc.h.r - Trails.mc.h.O) * Trails.mc.aj();
        final bhe vector = new bhe(x, y, z);
        Trails.trails.add(new Trail(vector, color));
    }
    
    @Override
    public void onDisable() {
        Trails.trails.clear();
        super.onDisable();
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event) {
        if (Trails.mc.t.aw == 0 && this.hideInFirstPerson.getCurrentValue()) {
            return;
        }
        if (Trails.mc.h.s != 0.0 || Trails.mc.h.u != 0.0) {
            createTrail();
        }
        Trails.trails.removeIf(Trail::remove);
        GL11.glPushMatrix();
        bus.m();
        GL11.glDisable(3008);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glBegin(8);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        final int topColor1 = Trails.topColor.getColor();
        final float red = (topColor1 >> 16 & 0xFF) / 255.0f;
        final float green = (topColor1 >> 8 & 0xFF) / 255.0f;
        final float blue = (topColor1 & 0xFF) / 255.0f;
        final float height = (float)((Fluger.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Amogus")) ? 1.3 : ((Fluger.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equals("Crab") || CustomModel.modelMode.currentMode.equals("Red Panda") || CustomModel.modelMode.currentMode.equals("Chinchilla"))) ? 0.6499999761581421 : (Trails.mc.h.aU() ? 1.5 : 1.8)));
        for (final Trail trail : Trails.trails) {
            final double x = trail.position.b - Trails.mc.ac().o;
            final double y = trail.position.c - Trails.mc.ac().p;
            final double z = trail.position.d - Trails.mc.ac().q;
            final float alpha = Trails.trailsAlpha.getCurrentValue() * (1.0f - trail.existed / Trails.sizeToRemove.getCurrentValue());
            GL11.glColor4f((float)trail.color.b, (float)trail.color.c, (float)trail.color.d, alpha);
            GL11.glVertex3d(x, y, z);
            GL11.glColor4f(red, green, blue, alpha);
            GL11.glVertex3d(x, y + height, z);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        bus.l();
        GL11.glEnable(3008);
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    static {
        Trails.trailsMode = new ListSetting("Trails Mode", "Custom", () -> true, new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
        trails = new ArrayList<Trail>();
    }
    
    static class Trail
    {
        int existed;
        bhe position;
        bhe color;
        
        public Trail(final bhe position, final bhe color) {
            this.position = position;
            this.color = color;
            this.existed = -15;
        }
        
        boolean remove() {
            ++this.existed;
            if (this.existed == 0) {
                this.existed = 1;
            }
            ++this.existed;
            return this.existed > Trails.sizeToRemove.getCurrentValue();
        }
    }
}
