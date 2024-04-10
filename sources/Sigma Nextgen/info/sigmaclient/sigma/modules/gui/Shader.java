package info.sigmaclient.sigma.modules.gui;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.gui.clickgui.JelloClickGui;
import info.sigmaclient.sigma.gui.other.clickgui.NursultanClickGui;
import info.sigmaclient.sigma.sigma5.utils.Sigma5BlurUtils;
import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.blurs.Bloom;
import info.sigmaclient.sigma.utils.render.blurs.GradientGlowing;
import info.sigmaclient.sigma.utils.render.blurs.RoundRectShader;
import net.minecraft.client.shader.Framebuffer;

import java.awt.*;
import java.util.ArrayList;

import static info.sigmaclient.sigma.sigma5.utils.JelloSwapBlur.蓳瀧藸䖼錌;
import top.fl0wowp4rty.phantomshield.annotations.Native;


public class Shader extends Module {
    public Shader() {
        super("Shader", Category.Gui, "Blur something");
    }
    public static ArrayList<Runnable> blooms = new ArrayList<>();
    public static Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);
    public static boolean isEnable(){
        return SigmaNG.getSigmaNG().inGameGuiBlur;
    }
    public static void drawRoundRectWithGlowing(double x, double y, double x2, double y2, Color ignore){
        GradientGlowing.applyGradientCornerRL((float) x, (float) y, (float) (x2 - x), (float) (y2 - y), ignore.getAlpha() / 255f, ColorChanger.getColor(0, 10), ColorChanger.getColor(100, 10), () -> {});
        RoundRectShader.drawRoundRect((float) x, (float) y, (float) (x2 - x), (float) (y2 - y),5,ignore);
    }
    public static void addBlur(double x, double y, double x2, double y2){
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRect(x, y, x2, y2, -1);
        StencilUtil.readStencilBuffer(1);
        蓳瀧藸䖼錌();
        StencilUtil.uninitStencilBuffer();
    }
    public static void addBlur(Runnable runnable){
    }
    public static int clickGUIIter = 0;

    public static void addBloom(Runnable o) {
        blooms.add(o);
    }

    public interface RenderRunnable extends Runnable {
        default void run(){};
    }

    public static void onRender() {
        if (clickGUIIter > 0 && SigmaNG.getSigmaNG().guiBlur && (JelloClickGui.leave.getValue() < 1.6)) {
            Sigma5BlurUtils.vblur(clickGUIIter);
        }
        ScaledResolution sr = new ScaledResolution(mc);
    }
}
