// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import ru.tuskevich.modules.impl.HUD.Hud;
import ru.tuskevich.event.EventTarget;
import java.util.Iterator;
import ru.tuskevich.util.color.ColorUtility;
import java.awt.Color;
import ru.tuskevich.util.aliasing.AntiAliasingUtility;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventRender;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import java.util.ArrayList;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "Trails", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd", type = Type.RENDER)
public class Trails extends Module
{
    public ArrayList<Point> p;
    private final SliderSetting removeTicks;
    
    public Trails() {
        this.p = new ArrayList<Point>();
        this.removeTicks = new SliderSetting("Delete through", 100.0f, 1.0f, 500.0f, 1.0f);
        this.add(this.removeTicks);
    }
    
    @EventTarget
    public void onRender(final EventRender eventRender) {
        if (Trails.mc.gameSettings.thirdPersonView == 1 || Trails.mc.gameSettings.thirdPersonView == 2) {
            final boolean old = Trails.mc.gameSettings.viewBobbing;
            Trails.mc.gameSettings.viewBobbing = false;
            Trails.mc.entityRenderer.setupCameraTransform(eventRender.pt, 2);
            Trails.mc.gameSettings.viewBobbing = old;
            if (Trails.mc.gameSettings.showDebugInfo) {
                return;
            }
            final Minecraft mc = Trails.mc;
            final double lastTickPosX = Minecraft.player.lastTickPosX;
            final Minecraft mc2 = Trails.mc;
            final double posX = Minecraft.player.posX;
            final Minecraft mc3 = Trails.mc;
            final double ix = -(lastTickPosX + (posX - Minecraft.player.lastTickPosX) * eventRender.pt);
            final Minecraft mc4 = Trails.mc;
            final double lastTickPosY = Minecraft.player.lastTickPosY;
            final Minecraft mc5 = Trails.mc;
            final double posY = Minecraft.player.posY;
            final Minecraft mc6 = Trails.mc;
            final double iy = -(lastTickPosY + (posY - Minecraft.player.lastTickPosY) * eventRender.pt);
            final Minecraft mc7 = Trails.mc;
            final double lastTickPosZ = Minecraft.player.lastTickPosZ;
            final Minecraft mc8 = Trails.mc;
            final double posZ = Minecraft.player.posZ;
            final Minecraft mc9 = Trails.mc;
            final double iz = -(lastTickPosZ + (posZ - Minecraft.player.lastTickPosZ) * eventRender.pt);
            final Minecraft mc10 = Trails.mc;
            final double lastTickPosX2 = Minecraft.player.lastTickPosX;
            final Minecraft mc11 = Trails.mc;
            final double posX2 = Minecraft.player.posX;
            final Minecraft mc12 = Trails.mc;
            final float x = (float)(lastTickPosX2 + (posX2 - Minecraft.player.lastTickPosX) * eventRender.pt);
            final Minecraft mc13 = Trails.mc;
            final double lastTickPosY2 = Minecraft.player.lastTickPosY;
            final Minecraft mc14 = Trails.mc;
            final double posY2 = Minecraft.player.posY;
            final Minecraft mc15 = Trails.mc;
            final float y = (float)(lastTickPosY2 + (posY2 - Minecraft.player.lastTickPosY) * eventRender.pt);
            final Minecraft mc16 = Trails.mc;
            final double lastTickPosZ2 = Minecraft.player.lastTickPosZ;
            final Minecraft mc17 = Trails.mc;
            final double posZ2 = Minecraft.player.posZ;
            final Minecraft mc18 = Trails.mc;
            final float z = (float)(lastTickPosZ2 + (posZ2 - Minecraft.player.lastTickPosZ) * eventRender.pt);
            final Minecraft mc19 = Trails.mc;
            if (Minecraft.player.motionX != 0.0) {
                final Minecraft mc20 = Trails.mc;
                if (Minecraft.player.motionZ != 0.0) {
                    this.p.add(new Point(new Vec3d(x, y, z)));
                }
            }
            this.p.removeIf(point -> point.time >= this.removeTicks.getFloatValue());
            GlStateManager.pushMatrix();
            GL11.glDepthMask(false);
            GlStateManager.translate(ix, iy, iz);
            GlStateManager.enableBlend();
            GL11.glBlendFunc(770, 771);
            GlStateManager.disableTexture2D();
            GL11.glDisable(2884);
            GL11.glShadeModel(7425);
            GL11.glDisable(3008);
            GlStateManager.alphaFunc(516, 0.0f);
            AntiAliasingUtility.hook(true, false, false);
            GL11.glBegin(8);
            for (final Point point2 : this.p) {
                final Color color = this.getColor(this.p.indexOf(point2));
                if (this.p.indexOf(point2) >= this.p.size() - 1) {
                    continue;
                }
                final float alpha = 100.0f * (this.p.indexOf(point2) / (float)this.p.size());
                final Point temp = this.p.get(this.p.indexOf(point2) + 1);
                final int color2 = ColorUtility.setAlpha(new Color(color.getRGB()).getRGB(), (int)alpha);
                ColorUtility.setColor(color2);
                GL11.glVertex3d(temp.pos.x, temp.pos.y, temp.pos.z);
                final double x2 = temp.pos.x;
                final double y2 = temp.pos.y;
                final Minecraft mc21 = Trails.mc;
                GL11.glVertex3d(x2, y2 + Minecraft.player.height - 0.1, temp.pos.z);
                final Point point3 = point2;
                ++point3.time;
            }
            GL11.glEnd();
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (final Point point2 : this.p) {
                final Color color = this.getColor(this.p.indexOf(point2));
                if (this.p.indexOf(point2) >= this.p.size() - 1) {
                    continue;
                }
                final float alpha = new Color(color.getRGB()).getAlpha() * (this.p.indexOf(point2) / (float)this.p.size());
                final Point temp = this.p.get(this.p.indexOf(point2) + 1);
                final int color2 = ColorUtility.setAlpha(ColorUtility.fade(5, this.p.indexOf(point2) * 1, new Color(color.getRGB()), 1.0f).brighter().getRGB(), (int)alpha);
                ColorUtility.setColor(color2);
                GL11.glVertex3d(temp.pos.x, temp.pos.y, temp.pos.z);
                final Point point4 = point2;
                ++point4.time;
            }
            GL11.glEnd();
            GL11.glBegin(3);
            for (final Point point2 : this.p) {
                final Color color = this.getColor(this.p.indexOf(point2));
                if (this.p.indexOf(point2) >= this.p.size() - 1) {
                    continue;
                }
                final float alpha = new Color(color.getRGB()).getAlpha() * (this.p.indexOf(point2) / (float)this.p.size());
                final Point temp = this.p.get(this.p.indexOf(point2) + 1);
                final int color2 = ColorUtility.setAlpha(ColorUtility.fade(5, this.p.indexOf(point2) * 1, new Color(color.getRGB()), 1.0f).brighter().getRGB(), (int)alpha);
                ColorUtility.setColor(color2);
                final double x3 = temp.pos.x;
                final double y3 = temp.pos.y;
                final Minecraft mc22 = Trails.mc;
                GL11.glVertex3d(x3, y3 + Minecraft.player.height - 0.1, temp.pos.z);
                final Point point5 = point2;
                ++point5.time;
            }
            GL11.glEnd();
            AntiAliasingUtility.unhook(true, false, false);
            GL11.glEnable(3008);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GL11.glEnable(2884);
            GlStateManager.resetColor();
            GL11.glDepthMask(true);
            GlStateManager.popMatrix();
        }
    }
    
    private Color getColor(final int count) {
        final Color colorOne = Hud.onecolor.getColorValueColor();
        final Color colorTwo = Hud.twocolor.getColorValueColor();
        return ColorUtility.interpolateColorsBackAndForth(5, count, colorOne, colorTwo, false);
    }
    
    public static class Point
    {
        public Vec3d pos;
        public long time;
        
        public Point(final Vec3d pos) {
            this.pos = pos;
        }
    }
}
