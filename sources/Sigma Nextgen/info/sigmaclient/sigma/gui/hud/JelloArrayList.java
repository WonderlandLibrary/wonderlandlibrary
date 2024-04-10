package info.sigmaclient.sigma.gui.hud;

import info.sigmaclient.sigma.utils.render.rendermanagers.GlStateManager;
import info.sigmaclient.sigma.utils.render.rendermanagers.ScaledResolution;
import info.sigmaclient.sigma.sigma5.utils.Sigma5AnimationUtil;
import info.sigmaclient.sigma.modules.Category;
import info.sigmaclient.sigma.modules.Module;
import info.sigmaclient.sigma.modules.gui.ActiveMods;
import info.sigmaclient.sigma.gui.font.JelloFontUtil;
import info.sigmaclient.sigma.gui.font.JelloFontRenderer;
import info.sigmaclient.sigma.utils.render.ColorUtils;
import info.sigmaclient.sigma.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static info.sigmaclient.sigma.modules.Module.mc;
import static info.sigmaclient.sigma.utils.render.RenderUtils.drawTexture;

public class JelloArrayList {

    public void drawShadow(int x, int y, String name, float alpha){
        drawTexture(x * 1F - 11F - 2 - JelloFontUtil.jelloFont20.getStringWidth(name) * 0.5f,
                y * 1F + 2 - 2.5f - 1.5f - 1.5f - 1.5f - 6f - 1f - 10f,
                (int)(((float) JelloFontUtil.jelloFont20.getStringWidth(name)) + 20f + 10f + 55f - 4 + 7 + JelloFontUtil.jelloFont20.getStringWidth(name) * 0.5f),
                (int)(18.5f + 6f + 10f + 3f + 14f),"arraylistshadow",alpha * 0.15f);
        drawTexture(x * 1F - 11F,
                y * 1F + 2 - 2.5f - 1.5f - 1.5f - 1.5f - 6f - 1f - 10f,
                (int)(((float) JelloFontUtil.jelloFont20.getStringWidth(name)) + 20f + 10f + 55f - 4),
                (int)(18.5f + 6f + 10f + 3f + 14f),"arraylistshadow",alpha * 0.55f);
    }
    HashMap<Module, Sigma5AnimationUtil> animationUtilHashMap = new HashMap<>();
    public void tickEvent(List<? extends Module> modules){
    }
    public static float ᢻ웎觯硙曞Ꮀ(float n, final float n2, final float n3, final float n4) {
        if ((n /= n4 / 2.0f) >= 1.0f) {
            return -n3 / 2.0f * (--n * (n - 2.0f) - 1.0f) + n2;
        }
        return n3 / 2.0f * n * n + n2;
    }
    public static int lasty = 0;
    public boolean hover(double mx, double my) {
         return false;
    }
    public void drawObject(List<? extends Module> modules) {
        for (Module module : modules) {
            if(module.category.equals(Category.Gui)) continue;
            if(!animationUtilHashMap.containsKey(module)){
                animationUtilHashMap.put(module, new Sigma5AnimationUtil(150, 150));
            }
            animationUtilHashMap.get(module).animTo(module.enabled ? Sigma5AnimationUtil.AnimState.ANIMING : Sigma5AnimationUtil.AnimState.SLEEPING);
        }
        int yOff = 0;
        if (Minecraft.getInstance().isF3Enabled()) {
            return;
        }

        modules.sort(Comparator.<Module>comparingDouble(m -> JelloFontUtil.jelloFont20.getStringWidth(m.name)).reversed());
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        JelloFontRenderer font = JelloFontUtil.jelloFont20;
        ScaledResolution sr = new ScaledResolution(mc);
        java.util.ArrayList<Module> mods = new java.util.ArrayList<>();
        float ys = 3f;

        for (Module module : modules) {
            if(module.category.equals(Category.Gui)) continue;
            if (animationUtilHashMap.get(module).getAnim() == 0) continue;
            float calcY = animationUtilHashMap.get(module).getAnim();
            module.animY = ys;
            module.animX = sr.getScaledWidth() - font.getStringWidth(module.name) - 5f;
            ys += (int)(13 * ᢻ웎觯硙曞Ꮀ(calcY, 0.0f, 1.0f, 1.0f));
            mods.add(module);
        }
        // from sigma5
        int width = sr.getScaledWidth();
        int n = 10;
        final int n2 = 1;

        ys = 4f;
        final int 䕦ꪕⰛ䁞ᢻ眓 = sr.getScaledWidth();
        // simple
        // reset y pos
        // itz draw shadow
        for (Module mod : mods) {
            float anim = animationUtilHashMap.get(mod).getAnim();
            float scale = 0.86f + 0.14f * anim;
            if(!ActiveMods.anim.isEnable()){
                scale = 1;
                anim = 1;
            }
            float alpha = 0.36f * anim * (float)Math.sqrt(Math.min(1.2f, font.getStringWidth(mod.name) * 2 / 63.0f));
            ys += 13;
            lasty = (int) ys;
            GL11.glPushMatrix();
            float yy = yOff + (float) (mod.animY);
            final float n6 = (float) (mod.animY + 12);
            final float n5 = (float) (mod.animX + font.getStringWidth(mod.name) / 2);
            GL11.glTranslatef(n5, n6, 0.0f);
            GL11.glScalef(scale, scale, 1.0f);
            GL11.glTranslatef(-n5, -n6, 0.0f);

//            GlStateManager.translate(mod.animX + font.getStringWidth(mod.name) / 2.0, yOff +  mod.animY + 3 + font.getHeight() / 2.0,
//                    0);
//            GlStateManager.scale(mod.getTranslate().getValue() / 10,
//                    mod.getTranslate().getValue() / 10, 1);
//            GlStateManager.translate(-(mod.animX + font.getStringWidth(mod.name) / 2.0), -yOff  -(mod.animY + 3 + font.getHeight() / 2.0),
//                    0);

            GlStateManager.resetColor();

            RenderUtils.drawTextureLocationZoom(
                    䕦ꪕⰛ䁞ᢻ眓 - font.getStringWidthNoScale(mod.name) / 2 * 1.5f - 10.5f,
                    (float)(mod.animY - 16 / 2),
                    font.getStringWidthNoScale(mod.name) / 2 * 3,
                    (float)(font.getHeight() + n2 / 2f + 40 / 2),
                    "esp/shadow",
                    ColorUtils.reAlpha(
                            new Color(-65794),
                            alpha));

//            drawShadow((int) mod.animX - 10, yOff + (int) mod.animY, mod.name, alpha);

            GL11.glPopMatrix();
        }
        // draw modulename
        for (Module mod : mods) {
            float anim = animationUtilHashMap.get(mod).getAnim();
            float scale = 0.86f + 0.14f * anim;
            if(!ActiveMods.anim.isEnable()){
                scale = 1;
                anim = 1;
            }
//            float alpha = 0.36f * anim * (float)Math.sqrt(Math.min(1.2f, font.getStringWidth(mod.name) / 63.0f));

            GL11.glPushMatrix();
            float yy = yOff + (float) (mod.animY + 3);
            final float n6 = (float) (mod.animY + 12);
            final float n5 = (float) (mod.animX + font.getStringWidth(mod.name) / 2);
            GL11.glTranslatef(n5, n6, 0.0f);
            GL11.glScalef(scale, scale, 1.0f);
            GL11.glTranslatef(-n5, -n6, 0.0f);
            float alp = 0.95f * anim;
            Color color = new Color(1,1,1, alp);
            GlStateManager.resetColor();
            font.drawNoBSString(mod.name, (float) mod.animX, yy, color.getRGB());
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }
}
