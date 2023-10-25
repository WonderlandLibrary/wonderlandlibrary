/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.feature.impl.visuals;

import java.util.ArrayList;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import org.celestial.client.Celestial;
import org.celestial.client.event.EventTarget;
import org.celestial.client.event.events.impl.render.EventRender3D;
import org.celestial.client.feature.Feature;
import org.celestial.client.feature.impl.Type;
import org.celestial.client.feature.impl.visuals.CustomModel;
import org.celestial.client.helpers.misc.ClientHelper;
import org.celestial.client.helpers.palette.PaletteHelper;
import org.celestial.client.settings.impl.BooleanSetting;
import org.celestial.client.settings.impl.ColorSetting;
import org.celestial.client.settings.impl.ListSetting;
import org.celestial.client.settings.impl.NumberSetting;
import org.lwjgl.opengl.GL11;

public class Trails
extends Feature {
    public static NumberSetting sizeToRemove;
    public static NumberSetting trailsAlpha;
    public static ListSetting trailsMode;
    public static ColorSetting topColor;
    public static ColorSetting bottomColor;
    private final BooleanSetting hideInFirstPerson;
    static final ArrayList<Trail> trails;

    public Trails() {
        super("Trails", "\u041e\u0441\u0442\u0430\u0432\u043b\u044f\u0435\u0442 \u043b\u0438\u043d\u0438\u044e \u0445\u043e\u0434\u044c\u0431\u044b", Type.Visuals);
        sizeToRemove = new NumberSetting("Ticks", 300.0f, 10.0f, 500.0f, 10.0f, () -> true);
        topColor = new ColorSetting("Top Color", ClientHelper.getClientColor().getRGB(), () -> Trails.trailsMode.currentMode.equals("Custom"));
        bottomColor = new ColorSetting("Bottom Color", ClientHelper.getClientColor().getRGB(), () -> Trails.trailsMode.currentMode.equals("Custom"));
        trailsAlpha = new NumberSetting("Trails Alpha", 0.9f, 0.1f, 1.0f, 0.1f, () -> true);
        this.hideInFirstPerson = new BooleanSetting("Hide In First Person", false, () -> true);
        this.addSettings(sizeToRemove, trailsMode, topColor, bottomColor, trailsAlpha, this.hideInFirstPerson);
    }

    static void createTrail() {
        Vec3d color = null;
        int oneColor = bottomColor.getColor();
        float red = (float)(oneColor >> 16 & 0xFF) / 255.0f;
        float green = (float)(oneColor >> 8 & 0xFF) / 255.0f;
        float blue = (float)(oneColor & 0xFF) / 255.0f;
        switch (Trails.trailsMode.currentMode) {
            case "Client": {
                color = new Vec3d((float)ClientHelper.getClientColor().getRed() / 255.0f, (float)ClientHelper.getClientColor().getGreen() / 255.0f, (float)ClientHelper.getClientColor().getBlue() / 255.0f);
                break;
            }
            case "Custom": {
                color = new Vec3d(red, green, blue);
                break;
            }
            case "Astolfo": {
                color = new Vec3d((float)PaletteHelper.astolfo(false, 1).getRed() / 255.0f, (float)PaletteHelper.astolfo(false, 1).getGreen() / 255.0f, (float)PaletteHelper.astolfo(false, 1).getBlue() / 255.0f);
                break;
            }
            case "Rainbow": {
                color = new Vec3d((float)PaletteHelper.rainbow(300, 1.0f, 1.0f).getRed() / 255.0f, (float)PaletteHelper.rainbow(300, 1.0f, 1.0f).getGreen() / 255.0f, (float)PaletteHelper.rainbow(300, 1.0f, 1.0f).getBlue() / 255.0f);
            }
        }
        double x = Trails.mc.player.lastTickPosX + (Trails.mc.player.posX - Trails.mc.player.lastTickPosX) * (double)mc.getRenderPartialTicks();
        double y = Trails.mc.player.lastTickPosY + (Trails.mc.player.posY - Trails.mc.player.lastTickPosY) * (double)mc.getRenderPartialTicks();
        double z = Trails.mc.player.lastTickPosZ + (Trails.mc.player.posZ - Trails.mc.player.lastTickPosZ) * (double)mc.getRenderPartialTicks();
        Vec3d vector = new Vec3d(x, y, z);
        trails.add(new Trail(vector, color));
    }

    @Override
    public void onDisable() {
        trails.clear();
        super.onDisable();
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (Trails.mc.gameSettings.thirdPersonView == 0 && this.hideInFirstPerson.getCurrentValue()) {
            return;
        }
        if (Trails.mc.player.motionX != 0.0 || Trails.mc.player.motionZ != 0.0) {
            Trails.createTrail();
        }
        trails.removeIf(Trail::remove);
        GL11.glPushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GL11.glDisable(3008);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glBegin(8);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        int topColor1 = topColor.getColor();
        float red = (float)(topColor1 >> 16 & 0xFF) / 255.0f;
        float green = (float)(topColor1 >> 8 & 0xFF) / 255.0f;
        float blue = (float)(topColor1 & 0xFF) / 255.0f;
        float height = (float)(Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Amogus") ? 1.3 : (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && (CustomModel.modelMode.currentMode.equals("Crab") || CustomModel.modelMode.currentMode.equals("Red Panda") || CustomModel.modelMode.currentMode.equals("Chinchilla")) ? (double)0.65f : (Trails.mc.player.isSneaking() ? 1.5 : 1.8)));
        for (Trail trail : trails) {
            double x = trail.position.x - Trails.mc.getRenderManager().renderPosX;
            double y = trail.position.y - Trails.mc.getRenderManager().renderPosY;
            double z = trail.position.z - Trails.mc.getRenderManager().renderPosZ;
            float alpha = trailsAlpha.getCurrentValue() * (1.0f - (float)trail.existed / sizeToRemove.getCurrentValue());
            GL11.glColor4f((float)trail.color.x, (float)trail.color.y, (float)trail.color.z, alpha);
            GL11.glVertex3d(x, y, z);
            GL11.glColor4f(red, green, blue, alpha);
            GL11.glVertex3d(x, y + (double)height, z);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
        GL11.glEnable(3008);
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }

    static {
        trailsMode = new ListSetting("Trails Mode", "Custom", () -> true, "Astolfo", "Rainbow", "Client", "Custom");
        trails = new ArrayList();
    }

    static class Trail {
        int existed;
        Vec3d position;
        Vec3d color;

        public Trail(Vec3d position, Vec3d color) {
            this.position = position;
            this.color = color;
            this.existed = -15;
        }

        boolean remove() {
            int n;
            ++this.existed;
            if (this.existed == 0) {
                this.existed = 1;
            }
            ++this.existed;
            return (float)n > sizeToRemove.getCurrentValue();
        }
    }
}

