// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.RENDER;

import ru.tuskevich.util.color.ColorUtility;
import ru.tuskevich.modules.impl.HUD.Hud;
import java.awt.Color;
import ru.tuskevich.util.aliasing.AntiAliasingUtility;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import java.util.Iterator;
import ru.tuskevich.util.animations.AnimationMath;
import ru.tuskevich.event.events.impl.EventRender;
import ru.tuskevich.event.EventTarget;
import ru.tuskevich.event.events.impl.EventUpdate;
import java.util.ArrayList;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "JumpCircle", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.RENDER)
public class JumpCircle extends Module
{
    public static ArrayList<Circle> circles;
    
    @EventTarget
    public void render(final EventUpdate e) {
        JumpCircle.circles.removeIf(circle -> circle.age > 950.0);
    }
    
    @EventTarget
    public void onRender(final EventRender eventRender) {
        for (final Circle c : JumpCircle.circles) {
            this.renderCircle(c, eventRender.pt);
            c.age = AnimationMath.fast((float)c.age, 1000.0f, 0.5f);
        }
    }
    
    public void renderCircle(final Circle c, final float partialTicks) {
        if (c.age >= 1000.0) {
            return;
        }
        final Minecraft mc = JumpCircle.mc;
        final double lastTickPosX = Minecraft.player.lastTickPosX;
        final Minecraft mc2 = JumpCircle.mc;
        final double posX = Minecraft.player.posX;
        final Minecraft mc3 = JumpCircle.mc;
        final double ix = -(lastTickPosX + (posX - Minecraft.player.lastTickPosX) * partialTicks);
        final Minecraft mc4 = JumpCircle.mc;
        final double lastTickPosY = Minecraft.player.lastTickPosY;
        final Minecraft mc5 = JumpCircle.mc;
        final double posY = Minecraft.player.posY;
        final Minecraft mc6 = JumpCircle.mc;
        final double iy = -(lastTickPosY + (posY - Minecraft.player.lastTickPosY) * partialTicks);
        final Minecraft mc7 = JumpCircle.mc;
        final double lastTickPosZ = Minecraft.player.lastTickPosZ;
        final Minecraft mc8 = JumpCircle.mc;
        final double posZ = Minecraft.player.posZ;
        final Minecraft mc9 = JumpCircle.mc;
        final double iz = -(lastTickPosZ + (posZ - Minecraft.player.lastTickPosZ) * partialTicks);
        GlStateManager.pushMatrix();
        GL11.glDepthMask(false);
        GlStateManager.enableDepth();
        GlStateManager.translate(ix, iy, iz);
        GlStateManager.enableBlend();
        GL11.glBlendFunc(770, 771);
        GlStateManager.disableTexture2D();
        GL11.glDisable(2884);
        GL11.glShadeModel(7425);
        GL11.glDisable(3008);
        AntiAliasingUtility.hook(true, false, false);
        GlStateManager.alphaFunc(516, 0.0f);
        GL11.glBegin(8);
        for (int i = 0; i <= 360; ++i) {
            final Color color = this.getColor(i);
            final double x = Math.cos(i * 3.141592653589793 / 180.0);
            final double z = Math.sin(i * 3.141592653589793 / 180.0);
            GL11.glColor4d((double)(new Color(color.getRGB()).getRed() / 255.0f), (double)(new Color(color.getRGB()).getGreen() / 255.0f), (double)(new Color(color.getRGB()).getBlue() / 255.0f), -0.35 - c.age / 800.0);
            GL11.glVertex3d(c.x + x * (c.age / 1100.0 * 1.5499999523162842), c.y, c.z + z * (c.age / 1100.0 * 1.5499999523162842));
            GL11.glColor4d((double)(new Color(color.getRGB()).getRed() / 255.0f), (double)(new Color(color.getRGB()).getGreen() / 255.0f), (double)(new Color(color.getRGB()).getBlue() / 255.0f), 0.75 - c.age / 1200.0);
            GL11.glVertex3d(c.x + x * (c.age / 1100.0), c.y, c.z + z * (c.age / 1100.0));
        }
        GL11.glEnd();
        GL11.glBegin(8);
        for (int i = 0; i <= 360; ++i) {
            final Color color = this.getColor(i);
            final double x = Math.cos(i * 3.141592653589793 / 180.0);
            final double z = Math.sin(i * 3.141592653589793 / 180.0);
            GL11.glColor4d((double)(new Color(color.getRGB()).getRed() / 255.0f), (double)(new Color(color.getRGB()).getGreen() / 255.0f), (double)(new Color(color.getRGB()).getBlue() / 255.0f), 0.75 - c.age / 1200.0);
            GL11.glVertex3d(c.x + x * (c.age / 1100.0), c.y, c.z + z * (c.age / 1100.0));
            GL11.glColor4d((double)(new Color(color.getRGB()).getRed() / 255.0f), (double)(new Color(color.getRGB()).getGreen() / 255.0f), (double)(new Color(color.getRGB()).getBlue() / 255.0f), -0.35 - c.age / 800.0);
            GL11.glVertex3d(c.x + x * (c.age / 1100.0 * 0.4), c.y, c.z + z * (c.age / 1100.0 * 0.4));
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
    
    private Color getColor(final int count) {
        final Color colorOne = Hud.onecolor.getColorValueColor();
        final Color colorTwo = Hud.twocolor.getColorValueColor();
        return ColorUtility.interpolateColorsBackAndForth(5, count, colorOne, colorTwo, false);
    }
    
    static {
        JumpCircle.circles = new ArrayList<Circle>();
    }
    
    public static class Circle
    {
        double x;
        double y;
        double z;
        double age;
        double alpha;
        
        public Circle(final double x, final double y, final double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.alpha = 255.0;
            this.age = 0.0;
        }
    }
}
