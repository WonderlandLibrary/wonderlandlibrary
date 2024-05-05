package ru.smertnix.celestial.feature.impl.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.util.math.Vec3d;
import ru.smertnix.celestial.Celestial;
import ru.smertnix.celestial.event.EventTarget;
import ru.smertnix.celestial.event.events.impl.player.EventPreMotion;
import ru.smertnix.celestial.event.events.impl.player.EventUpdate;
import ru.smertnix.celestial.event.events.impl.render.EventRender3D;
import ru.smertnix.celestial.feature.Feature;
import ru.smertnix.celestial.feature.impl.FeatureCategory;
import ru.smertnix.celestial.feature.impl.combat.AttackAura;
import ru.smertnix.celestial.friend.Friend;
import ru.smertnix.celestial.ui.settings.impl.BooleanSetting;
import ru.smertnix.celestial.ui.settings.impl.ColorSetting;
import ru.smertnix.celestial.ui.settings.impl.ListSetting;
import ru.smertnix.celestial.ui.settings.impl.MultipleBoolSetting;
import ru.smertnix.celestial.ui.settings.impl.NumberSetting;
import ru.smertnix.celestial.utils.Helper;
import ru.smertnix.celestial.utils.render.ClientHelper;
import ru.smertnix.celestial.utils.render.ColorUtils;
import ru.smertnix.celestial.utils.render.RenderUtils;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ALPHA_TEST;

public class Trails extends Feature {

    ArrayList<Point> points = new ArrayList<Point>();
    public static ListSetting trailsMode = new ListSetting("Trails Mode", "Line", () -> true, "Line");
    private final BooleanSetting timeoutBool = new BooleanSetting("Timeout", true, () -> trailsMode.currentMode.equalsIgnoreCase("Circle"));
    private final NumberSetting timeout = new NumberSetting("Time", 15, 1, 150, 0.1f, () -> true);

    public static ListSetting colorMode = new ListSetting("Trails Color", "Astolfo", () -> true, "Astolfo", "Rainbow", "Pulse", "Custom", "Client", "Static");
    public static ColorSetting onecolor = new ColorSetting("One Color", new Color(255, 255, 255).getRGB(), () -> colorMode.currentMode.equalsIgnoreCase("Static") || colorMode.currentMode.equalsIgnoreCase("Custom"));
    public static ColorSetting twocolor = new ColorSetting("Two Color", new Color(255, 255, 255).getRGB(), () -> colorMode.currentMode.equalsIgnoreCase("Custom"));
    public static NumberSetting removeticks = new NumberSetting("Remove Ticks", "�������� ����� ������� ����� ��������� ������", 100, 1, 500, 1, () -> trailsMode.currentMode.equalsIgnoreCase("Line"));
    public static NumberSetting alpha = new NumberSetting("Alpha Trails", "������������", 255, 1, 255, 1, () -> trailsMode.currentMode.equalsIgnoreCase("Line"));
    public static BooleanSetting smoothending = new BooleanSetting("Smooth Ending", true, () -> trailsMode.currentMode.equalsIgnoreCase("Line"));
    public static NumberSetting saturation = new NumberSetting("Saturation", 0.7f, 0.1f, 1f, 0.1f, () -> colorMode.currentMode.equalsIgnoreCase("Astolfo"));
    private final BooleanSetting onlyMeTrails = new BooleanSetting("Only Me", true, () -> true);

    List<Vec3d> path = new ArrayList<>();

    public Trails() {
        super("Trails", "Очень красиво))", FeatureCategory.Render);
        addSettings(colorMode);
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        if (trailsMode.currentMode.equalsIgnoreCase("Circle")) {

            if (Helper.mc.player.lastTickPosX != Helper.mc.player.posX || Helper.mc.player.lastTickPosY != Helper.mc.player.posY || Helper.mc.player.lastTickPosZ != Helper.mc.player.posZ) {
                path.add(new Vec3d(Helper.mc.player.posX, Helper.mc.player.posY, Helper.mc.player.posZ));
            }

            if (timeoutBool.getCurrentValue())
                while (path.size() > (int) timeout.getNumberValue()) {
                    path.remove(0);
                }
        }
    }

    @EventTarget
    public void onRender3DEvent(EventRender3D event3D) {
        if (trailsMode.currentMode.equalsIgnoreCase("Circle")) {
            RenderUtils.renderBreadCrumbs(path);
        }
    }

    @EventTarget
    public void onRender(EventRender3D event) {
        if (trailsMode.currentMode.equalsIgnoreCase("Line")) {

            if ((Helper.mc.gameSettings.thirdPersonView == 0 || Helper.mc.gameSettings.thirdPersonView == 1 || Helper.mc.gameSettings.thirdPersonView == 3)) {

                points.removeIf(p -> p.age >= removeticks.getNumberValue());

                float x = (float) (Helper.mc.player.lastTickPosX + (Helper.mc.player.posX - Helper.mc.player.lastTickPosX) * event.getPartialTicks());
                float y = (float) (Helper.mc.player.lastTickPosY + (Helper.mc.player.posY - Helper.mc.player.lastTickPosY) * event.getPartialTicks());
                float z = (float) (Helper.mc.player.lastTickPosZ + (Helper.mc.player.posZ - Helper.mc.player.lastTickPosZ) * event.getPartialTicks());

                points.add(new Point((float) (x), y, (float) (z)));


                GL11.glPushMatrix();
                GL11.glDisable(GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(GL11.GL_CULL_FACE);
                for (final Point t : points) {
                    if (points.indexOf(t) >= points.size() - 1) continue;

                    Point temp = points.get(points.indexOf(t) + 1);

                    float a = alpha.getNumberValue();
                    if (smoothending.getCurrentValue())
                        a = alpha.getNumberValue() * (points.indexOf(t) / (float) points.size());

                    Color color = Color.WHITE;
                    Color firstcolor = new Color(onecolor.getColorValue());
                    switch (colorMode.currentMode) {
                        case "Client":
                            color = ClientHelper.getClientColor(t.age / 16, 5, t.age, 5);
                            break;
                        case "Astolfo":
                            color = ColorUtils.astolfo(t.age  - t.age + 1, t.age, this.saturation.getNumberValue(), 16);
                            break;
                        case "Rainbow":
                            color = ColorUtils.rainbow((int) (t.age * 16), (float) 0.5f, 1.0f);
                            break;
                        case "Pulse":
                            color = ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0F * (t.age / 16) / 60);
                            break;

                        case "Custom":
                            color = ColorUtils.TwoColoreffect(new Color(onecolor.getColorValue()), new Color(twocolor.getColorValue()), Math.abs(System.currentTimeMillis() / 10) / 100.0 + 3.0F * (t.age / 16) / 60);
                            break;
                        case "Static":
                            color = firstcolor;
                            break;
                    }

                    Color c = RenderUtils.injectAlpha(color, (int) a);

                    glBegin(GL_QUAD_STRIP);
                    final double x2 = t.x - Helper.mc.getRenderManager().renderPosX;
                    final double y2 = t.y - Helper.mc.getRenderManager().renderPosY;
                    final double z2 = t.z - Helper.mc.getRenderManager().renderPosZ;

                    final double x1 = temp.x - Helper.mc.getRenderManager().renderPosX;
                    final double y1 = temp.y - Helper.mc.getRenderManager().renderPosY;
                    final double z1 = temp.z - Helper.mc.getRenderManager().renderPosZ;

                    RenderUtils.glColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 0).getRGB());
                    glVertex3d(x2, y2 + Helper.mc.player.height - 0.1, z2);
                    RenderUtils.glColor(c.getRGB());
                    glVertex3d(x2, y2 + 0.2, z2);
                    glVertex3d(x1, y1 + Helper.mc.player.height - 0.1, z1);
                    glVertex3d(x1, y1 + 0.2, z1);
                    glEnd();
                    ++t.age;
                }
                GlStateManager.resetColor();
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL_CULL_FACE);
                GL11.glDisable(GL11.GL_LINE_SMOOTH);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }
    }

    @Override
    public void onDisable() {
        points.clear();
        super.onDisable();
    }

    public static Color getGradientOffset(Color color1, Color color2, double offset, int alpha) {
        if (offset > 1) {
            double left = offset % 1;
            int off = (int) offset;
            offset = off % 2 == 0 ? left : 1 - left;

        }
        double inverse_percent = 1 - offset;
        int redPart = (int) (color1.getRed() * inverse_percent + color2.getRed() * offset);
        int greenPart = (int) (color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        int bluePart = (int) (color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart, alpha);
    }

    class Point {
        public final float x, y, z;

        public float age = 0;

        public Point(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}