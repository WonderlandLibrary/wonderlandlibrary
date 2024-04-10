package info.sigmaclient.sigma.gui.hud;

import info.sigmaclient.sigma.SigmaNG;
import info.sigmaclient.sigma.utils.render.StencilUtil;
import info.sigmaclient.sigma.utils.render.blurs.Bloom;
import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.gui.ActiveMods;
import info.sigmaclient.sigma.modules.gui.ColorChanger;
import info.sigmaclient.sigma.modules.gui.Shader;
import info.sigmaclient.sigma.utils.font.CustomFont;
import info.sigmaclient.sigma.utils.font.FontUtil;
import info.sigmaclient.sigma.utils.key.ClickUtils;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import info.sigmaclient.sigma.utils.render.anims.PartialTicksAnim;
import net.minecraft.potion.EffectInstance;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

import static info.sigmaclient.sigma.modules.Module.mc;
import static info.sigmaclient.sigma.modules.gui.Shader.blooms;
import static info.sigmaclient.sigma.modules.gui.Shader.stencilFramebuffer;
import static info.sigmaclient.sigma.utils.render.RenderUtils.drawTexture;

public class AnotherArrayList extends JelloArrayList{
    PartialTicksAnim offYAnim = new PartialTicksAnim(0);
    public void tickEvent(List<? extends Module> modules){
        int yOff = 0, xOff = 0;
        boolean add = false;
        for(EffectInstance e : mc.player.getActivePotionEffects()){
            if (e.isShowIcon()) {
                add = true;
            }
        }
        if(add){
            yOff = 20;
        }
        offYAnim.interpolate(yOff, 4);

        ScaledResolution sr = new ScaledResolution(mc);
        for (Module module : modules) {
            if(module.category.equals(Category.Gui)) continue;
            if(module.enabled){
                if(ActiveMods.anim.isEnable()) {
                    module.setAlphaTranslate(10, 6);
                    module.setScaleTranslate(10, 6);
                }else{
                    module.setAlphaTranslate(10);
                    module.setScaleTranslate(10);
                }
            }else{
                if(ActiveMods.anim.isEnable()) {
                    module.setAlphaTranslate(0, 6);
                    module.setScaleTranslate(0, 6);
                }else{
                    module.setAlphaTranslate(0);
                    module.setScaleTranslate(5);
                }
            }
        }
    }
    java.util.ArrayList<Module> mods = new java.util.ArrayList<>();
    public boolean hover(double mx, double my){
        CustomFont font = FontUtil.sfuiFontBold18;
        int xOff = 2;
        ScaledResolution sr = new ScaledResolution(mc);
        for (Module mod : mods) {
            if (mod.getTranslate().getValue() == 0)
                continue;
            double calcY = mod.getAlphaTranslate().getValue() / 10;
            float width = (float) ((font.getHeight() + 8) * calcY);
            if(ClickUtils.isClickable((float) mod.animX, (float) (mod.animY + 3),
                    sr.getScaledWidth() - 3 - xOff,
                    (float) (mod.animY) + width,mx,my)){
                return true;
            }
        }
        return false;
    }
    public void drawObject(List<? extends Module> modules) {
        ScaledResolution sr = new ScaledResolution(mc);
        int yOff = (int) offYAnim.getValue(), xOff = 2;
        CustomFont font = FontUtil.sfuiFontBold18;

        CustomFont finalFont = font;
        modules.sort(Comparator.<Module>comparingDouble(m -> finalFont.getStringWidth(m.remapName)).reversed());
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);


        java.util.ArrayList<Module> mods = new java.util.ArrayList<>();
        float ys = 3.5f + yOff;

        for (Module module : modules) {
            if(module.category.equals(Category.Gui)) continue;
            module.animX = ActiveMods.x.getValue().floatValue() - (FontUtil.sfuiFontBold17.getStringWidth(module.remapName)) - 6f - xOff;
            module.animY = ActiveMods.y.getValue().floatValue() + ys;
            if (module.getAlphaTranslate().getValue() == 0) continue;
            double calcY = module.getAlphaTranslate().getValue() / 10;
            ys += (font.getHeight() + 5) * calcY;
            JelloArrayList.lasty = (int) ys;
            mods.add(module);
        }
        Shader.addBloom(()->{
            for (Module mod : mods) {
                if (mod.getTranslate().getValue() == 0)
                    continue;
                Color color = ColorUtils.reAlpha(ColorChanger.getColor((int) mod.animY, 10), 255);
                double calcY = mod.getAlphaTranslate().getValue() / 10;
                float width = (float) ((font.getHeight() + 5) * calcY);
                RenderUtils.drawRect((float) mod.animX - 0.5f, (float) (mod.animY + 3),
                        ActiveMods.x.getValue().floatValue() - 1 - xOff,
                        (float) (mod.animY + 3) + width,
                        color.getRGB());
            }
        });
        // bloom
        RenderUtils.startBlend();
        GlStateManager.resetColor();
        if (SigmaNG.gameMode == SigmaNG.GAME_MODE.dest && mc.getMainWindow().getWidth() != 0 && mc.getMainWindow().getHeight() != 0) {
            stencilFramebuffer = RenderUtils.createFrameBuffer(stencilFramebuffer);
            stencilFramebuffer.framebufferClear();
            stencilFramebuffer.bindFramebuffer(true);
            if (!blooms.isEmpty()) {
                for (Runnable runnable : blooms) {
                    runnable.run();
                }
            }
            stencilFramebuffer.unbindFramebuffer();
            Bloom.renderBlur(stencilFramebuffer.framebufferTexture, 2, 3);
        }
        RenderUtils.startBlend();
        blooms.clear();
        GlStateManager.enableTexture2D();

        // draw modulename
        for (Module mod : mods) {
            if (mod.getTranslate().getValue() == 0)
                continue;
            GL11.glPushMatrix();

            int alp = (int)(255 * (mod.getAlphaTranslate().getValue() / 10.0));
            alp = 255;
            Color color = ColorUtils.reAlpha(ColorChanger.getColor((int) mod.animY, 10), 255);
            GlStateManager.resetColor();
            double calcY = mod.getAlphaTranslate().getValue() / 10;
            float heig = (float) ((font.getHeight() + 5) * calcY);
            StencilUtil.initStencilToWrite();
            RenderUtils.drawRect((float) mod.animX - 0.5f, (float) (mod.animY + 3),
                    ActiveMods.x.getValue().floatValue() - 1 - xOff,
                    (float) (mod.animY + 3) + heig,
                    new Color(0.117647059f,0.117647059f,0.117647059f,alp / 255f).getRGB());
            StencilUtil.readStencilBuffer(1);
            RenderUtils.drawRect(
                    ActiveMods.x.getValue().floatValue() - 3 - xOff,
                    (float) (mod.animY + 3),
                    ActiveMods.x.getValue().floatValue() - 1 - xOff,
                    (float) (mod.animY + 3) + heig,
                    color.getRGB());

            RenderUtils.drawRect((float) mod.animX - 0.5f, (float) (mod.animY + 3),
                    ActiveMods.x.getValue().floatValue() - 3 - xOff,
                    (float) (mod.animY) + font.getHeight() + 8,
                    new Color(0.117647059f,0.117647059f,0.117647059f,alp / 255f).getRGB());
            FontUtil.sfuiFontBold17.drawString(mod.remapName, (float) mod.animX + 1f, (float) (mod.animY + 6.5f), color.getRGB());
            StencilUtil.uninitStencilBuffer();
            GL11.glPopMatrix();
        }

        this.mods = mods;
        GL11.glPopMatrix();
    }
}
