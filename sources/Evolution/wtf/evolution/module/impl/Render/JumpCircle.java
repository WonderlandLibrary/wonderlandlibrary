package wtf.evolution.module.impl.Render;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.event.events.impl.EventUpdate;
import wtf.evolution.event.events.impl.JumpEvent;
import wtf.evolution.helpers.math.MathHelper;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.ColorSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.GL_GREATER;

@ModuleInfo(name = "JumpCircle", type = Category.Render)
public class JumpCircle extends Module {

    public ArrayList<Circle> circles = new ArrayList<Circle>();
    public ModeSetting mode = new ModeSetting("Mode", "Akrien", "Celestial", "Akrien").call(this);
    public SliderSetting speed = new SliderSetting("Speed", 1, 0, 10, 1).call(this);
    public ColorSetting rgb = new ColorSetting("Color", -1).call(this);

    @EventTarget
    public void jump(JumpEvent e) {
        circles.add(new Circle(mc.player.posX, mc.player.posY + 0.1, mc.player.posZ));
    }

    @EventTarget
    public void render(EventUpdate e) {
        circles.removeIf(circle -> circle.age > 1000);
    }

    @EventTarget
    public void onRender(EventRender e) {
        for (Circle c : circles) {
            renderCircle(c, e.pt);
            c.age+=speed.get();
        }
    }
    public void renderCircle(Circle c, float partialTicks) {

        if (c.age >= 1000) return;
        double ix = - (mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * partialTicks);
        double iy = - (mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * partialTicks);
        double iz = - (mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * partialTicks);
        GlStateManager.pushMatrix();
        GL11.glDepthMask(false);
        GlStateManager.enableDepth();
        GlStateManager.translate(ix, iy, iz);
        GlStateManager.enableBlend();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GlStateManager.alphaFunc(GL_GREATER, 0.0F);

        if (mode.is("Celestial")) {
            GL11.glBegin(GL11.GL_QUAD_STRIP);
            for (int i = 0; i <= 360; i += 1) {
                double x = Math.cos(i * Math.PI / 180);
                double z = Math.sin(i * Math.PI / 180);
                int color = rgb.get();
                GL11.glColor4d(new Color(color).getRed() / 255f, new Color(color).getGreen() / 255f, new Color(color).getBlue() / 255f, 0);
                GL11.glVertex3d(c.x + x * ((c.age / 1000) * 1.5), c.y, c.z + z * ((c.age / 1000) * 1.5));
                GL11.glColor4d(new Color(color).getRed() / 255f, new Color(color).getGreen() / 255f, new Color(color).getBlue() / 255f, 0.5 - c.age / 2000f);
                GL11.glVertex3d(c.x + x * (c.age / 1000), c.y, c.z + z * (c.age / 1000));
            }

            GL11.glEnd();
            GL11.glBegin(GL11.GL_QUAD_STRIP);
            for (int i = 0; i <= 360; i += 1) {
                double x = Math.cos(i * Math.PI / 180);
                double z = Math.sin(i * Math.PI / 180);
                int color = rgb.get();
                GL11.glColor4d(new Color(color).getRed() / 255f, new Color(color).getGreen() / 255f, new Color(color).getBlue() / 255f, 0.9 - c.age / 1100f);
                GL11.glVertex3d(c.x + x * (c.age / 1000), c.y, c.z + z * (c.age / 1000));
                GL11.glColor4d(new Color(color).getRed() / 255f, new Color(color).getGreen() / 255f, new Color(color).getBlue() / 255f, 0);
                GL11.glVertex3d(c.x + x * ((c.age / 1000) * 0.5), c.y, c.z + z * ((c.age / 1000) * 0.5));
            }
            GL11.glEnd();
        }
        else {
            GL11.glBegin(GL11.GL_QUAD_STRIP);
            for (int i = 0; i <= 360; i += 1) {
                double x = Math.cos(i * Math.PI / 180);
                double z = Math.sin(i * Math.PI / 180);
                int color = rgb.get();
                GL11.glColor4d(new Color(color).getRed() / 255f, new Color(color).getGreen() / 255f, new Color(color).getBlue() / 255f, 0.9 - c.age / 1100f);
                GL11.glVertex3d(c.x + x * (c.age / 1000), c.y, c.z + z * (c.age / 1000));
                GL11.glColor4d(new Color(color).getRed() / 255f, new Color(color).getGreen() / 255f, new Color(color).getBlue() / 255f, 0);
                GL11.glVertex3d(c.x + x * ((c.age / 1000) * 0.5), c.y, c.z + z * ((c.age / 1000) * 0.5));
            }
            GL11.glEnd();
        }
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GlStateManager.resetColor();
        GL11.glDepthMask(true);
        GlStateManager.popMatrix();

    }

    public class Circle {
        double x, y, z;

        double age;

        double alpha;

        public Circle(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.alpha = 255;
            this.age = 0;
        }
    }

}
