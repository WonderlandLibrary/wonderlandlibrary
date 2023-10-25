package wtf.evolution.module.impl.Render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import wtf.evolution.Main;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventRender;
import wtf.evolution.helpers.render.AntiAliasing;
import wtf.evolution.helpers.render.ColorUtil;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.BooleanSetting;
import wtf.evolution.settings.options.ColorSetting;
import wtf.evolution.settings.options.ModeSetting;
import wtf.evolution.settings.options.SliderSetting;

import java.awt.*;


import static org.lwjgl.opengl.GL11.GL_GREATER;
@ModuleInfo(name = "ChinaHat", type = Category.Render)
public class ChinaHat extends Module {
    public BooleanSetting outline = new BooleanSetting("Outline", true).call(this);
    public SliderSetting lineWidth = new SliderSetting("Width", 1, 0, 5, 0.1f).setHidden(() -> !outline.get()).call(this);
    public ModeSetting mode = new ModeSetting("Mode", "Static", "Static", "Fade", "Astolfo", "Rainbow").call(this);
    public ColorSetting color = new ColorSetting("Color", -1).setHidden(() -> !(mode.is("Static") || mode.is("Fade"))).call(this);


    @EventTarget
    public void onRender(EventRender e) {

        if (mc.gameSettings.thirdPersonView == 0) return;

        double ix = - (mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * e.pt);
        double iy = - (mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * e.pt);
        double iz = - (mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * e.pt);

        float x = (float) ((float) ((float) (mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * e.pt)));
        float y = (float) (mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * e.pt) + mc.player.height - (mc.player.isSneaking() ? (Main.m.getModule(CustomModel.class).state ? 0.02F : 0.2f) : 0) + (Main.m.getModule(CustomModel.class).state ? 0.3F : 0);
        float z = (float) ((float) ((float) (mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * e.pt)));


        GlStateManager.pushMatrix();
        GL11.glDepthMask(false);
        GlStateManager.enableDepth();

        GL11.glRotatef(-(mc.player.rotationYaw), 0, 1, 0);


        GlStateManager.translate(ix, iy, iz);
        GlStateManager.enableBlend();
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        AntiAliasing.hook(true, false, false);
        GlStateManager.alphaFunc(GL_GREATER, 0.0F);
        {
            GL11.glBegin(GL11.GL_TRIANGLE_FAN);
            Color c1 = getColor(1);
            GL11.glColor4f(c1.getRed() / 255f, c1.getGreen() / 255f, c1.getBlue() / 255f, 100 / 255f);
            GL11.glVertex3f(x, y + 0.23f, z);

            for (int i = 0; i <= 360;i++) {
                double x1 = Math.cos(i * Math.PI / 180) * 0.55;
                double z1 = Math.sin(i * Math.PI / 180) * 0.55;
                Color c = getColor(i);
                GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 100 / 255f);
                GL11.glVertex3d(x + x1, y, z + z1);
            }
            GL11.glEnd();
        }

        if (outline.get()) {
            {
                GL11.glLineWidth(lineWidth.get());
                GL11.glBegin(GL11.GL_LINE_LOOP);
                for (int i = 0; i <= 360; i++) {
                    double x1 = Math.cos(i * Math.PI / 180) * 0.55;
                    double z1 = Math.sin(i * Math.PI / 180) * 0.55;
                    Color c = getColor(i);
                    GL11.glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1);
                    GL11.glVertex3d(x + x1, y, z + z1);
                }
                GL11.glEnd();
            }
        }
        AntiAliasing.unhook(true, false, false);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glEnable(GL11.GL_CULL_FACE);
        GlStateManager.resetColor();
        GL11.glDepthMask(true);
        GlStateManager.popMatrix();
    }


    public Color getColor(int index) {
        if (mode.is("Static")) {
           return new Color(color.get());
        }
        else if (mode.is("Fade")) {
            return ColorUtil.fade(10, index * 2, new Color(color.get()), 0.5f);
        }
        else if (mode.is("Astolfo")) {
            return FeatureList.astolfo(1, index / 2f, 0.5f, 10);
        }
        else if (mode.is("Rainbow")) {
            return ColorUtil.rainbow(10, index, 0.5f, 1, 0.5f);
        }
        return Color.WHITE;
    }

}
