package wtf.evolution.module.impl.Render;

import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import wtf.evolution.event.EventTarget;
import wtf.evolution.event.events.impl.EventDisplay;
import wtf.evolution.helpers.ScaleUtil;
import wtf.evolution.helpers.font.FontRenderer;
import wtf.evolution.helpers.font.Fonts;
import wtf.evolution.helpers.render.ColorUtil;
import wtf.evolution.helpers.render.RenderUtil;
import wtf.evolution.helpers.render.Translate;
import wtf.evolution.module.Category;
import wtf.evolution.module.Module;
import wtf.evolution.module.ModuleInfo;
import wtf.evolution.settings.options.*;

import java.awt.*;
import java.util.ArrayList;

@ModuleInfo(name = "ArrayList", type = Category.Render)
public class FeatureList extends Module {
    public static ArrayList<Module> modules = new ArrayList<>();

    public static ModeSetting mode = new ModeSetting("Color Mode", "Fade", "Fade", "Rainbow", "Astolfo", "Static");
    public static ModeSetting font = new ModeSetting("Font", "Rubik", "Rubik", "Tenacity");

    public static SliderSetting speed = new SliderSetting("Color Speed", 5, 1, 9.9f, 1).setHidden(() -> mode.get().equalsIgnoreCase("Static"));
    public static SliderSetting animationSpeed = new SliderSetting("Animation Speed", 50, 0, 100, 1);

    public BooleanSetting hide = new BooleanSetting("Hide Render", true);
    public BooleanSetting glow = new BooleanSetting("Text Glow", true);
    public SliderSetting glowRadius = new SliderSetting("Glow Radius", 15, 5, 80, 5).setHidden(() -> !glow.get());
    public SliderSetting glowAlpha = new SliderSetting("Glow Alpha", 150, 0, 255, 1).setHidden(() -> !glow.get());
    public BooleanSetting shadow = new BooleanSetting("Shadow", true);
    public BooleanSetting rightLine = new BooleanSetting("Right Line", true);
    public BooleanSetting rightLineGlow = new BooleanSetting("Right Line Glow", false).setHidden(() -> !rightLine.get());
    public SliderSetting rightLineRadius = new SliderSetting("Right Line Radius", 15, 0, 25, 1).setHidden(() -> !rightLine.get() && !rightLineGlow.get());
    public SliderSetting rightLineAlpha = new SliderSetting("Right Line Alpha", 150, 0, 255, 1).setHidden(() -> !rightLine.get() && !rightLineGlow.get());
    public BooleanSetting lower = new BooleanSetting("to Lower Case", true);
    public ColorSetting s = new ColorSetting("Color", new Color(123, 157, 245).getRGB()).setHidden(() -> !(mode.is("Static") || mode.is("Fade")));

    public FeatureList() {
        addSettings(hide, lower, glow, glowRadius, glowAlpha, shadow, rightLine, rightLineGlow, rightLineRadius, rightLineAlpha, mode, font, speed, animationSpeed, s);
    }
    


    public FontRenderer getFont() {
        if (font.is("Rubik")) {
            return Fonts.RUB14;
        }
        if (font.is("Tenacity")) {
            return Fonts.BOLD14;
        }
        return null;
    }

    @EventTarget
    public void onDisplay(EventDisplay e) {
        ScaleUtil.scale_pre();
        modules.sort((f1, f2) -> getFont().getStringWidth(lower.get() ? f1.getDisplayName().toLowerCase() : f1.getDisplayName()) > getFont().getStringWidth(lower.get() ? f2.getDisplayName().toLowerCase() : f2.getDisplayName()) ? -1 : 1);
        int count = mc.player.getActivePotionEffects().size() > 0 ? ScaledResolution.getScaleFactor() > 2 ? 6 : 3 : 0;
        int x = 1;
        int y = 0;


        for (Module m : modules) {
            if (hide.get() && m.category == Category.Render) continue;
            float width = ScaleUtil.calc(e.sr.getScaledWidth()) - getFont().getStringWidth(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) - 3;
            Translate translate = m.a;
            final int offset = (count * (getFont().getFontHeight() + 4));
            translate.interpolate(m.state ? width : ScaleUtil.calc(e.sr.getScaledWidth()) + 3d, offset, (animationSpeed.get() / 5) * mc.deltaTime());
            if (m.state && translate.getX() <= ScaleUtil.calc(e.sr.getScaledWidth() + 3)) {
                m.isRender = true;
            }
            if (translate.getX() >= ScaleUtil.calc(e.sr.getScaledWidth()) + 3) {
                m.isRender = false;
            }

            if (m.isRender) {
                GL11.glPushMatrix();
                GL11.glTranslated(-2 - (rightLine.get() ? 1 : 0), 2, 0);
                if (shadow.get())
                    RenderUtil.drawBlurredShadow((float) translate.getX(), (float) translate.getY(), (float) (width - ScaleUtil.calc(e.sr.getScaledWidth()) + getFont().getStringWidth(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) * 2 + 6),  9, 15, new Color(10, 10, 10, 200));
                GL11.glPopMatrix();
                count++;
            }
        }
        count = mc.player.getActivePotionEffects().size() > 0 ? ScaledResolution.getScaleFactor() > 2 ? 6 : 3 : 0;
        for (Module m : modules) {
            if (hide.get() && m.category == Category.Render) continue;
            float width = ScaleUtil.calc(e.sr.getScaledWidth()) - getFont().getStringWidth(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) - 3;
            Translate translate = m.a;
            if (m.isRender) {
                int color = getColor(count);
                GL11.glPushMatrix();
                GL11.glTranslated(-2 - (rightLine.get() ? 1 : 0), 2, 0);
                RenderUtil.drawRectWH((float) translate.getX(), (float) translate.getY(), (float) (width - ScaleUtil.calc(e.sr.getScaledWidth()) +getFont().getStringWidth(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) * 2 + 6), 9, new Color(10, 10, 10, 150).getRGB());
                if (rightLine.get())
                    RenderUtil.drawRectWH((float) translate.getX() - width + ScaleUtil.calc(e.sr.getScaledWidth()), (float) translate.getY(), 1, 9, color);
                getFont().drawString(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName(), (float) translate.getX() + 1.5f, (float) translate.getY() + 3 - (getFont() == Fonts.REG14 ? 1 : 0), color);
                GL11.glPopMatrix();

                count++;
            }
        }

        count = mc.player.getActivePotionEffects().size() > 0 ? ScaledResolution.getScaleFactor() > 2 ? 6 : 3 : 0;

        for (Module m : modules) {
            if (hide.get() && m.category == Category.Render) continue;
            float width = ScaleUtil.calc(e.sr.getScaledWidth()) - getFont().getStringWidth(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) - 3;
            Translate translate = m.a;
            if (m.isRender) {
                int color = getColor(count);
                GL11.glPushMatrix();
                GL11.glTranslated(-2 - (rightLine.get() ? 1 : 0), 2, 0);
                if (rightLine.get() && rightLineGlow.get())
                    RenderUtil.drawBlurredShadow(Math.round((float) translate.getX() - width + ScaleUtil.calc(e.sr.getScaledWidth())), Math.round((float) translate.getY()), 3F, 9, (int) rightLineRadius.get(), new Color(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), (int) rightLineAlpha.get()));
                if (glow.get()) {
                    RenderUtil.drawBlurredShadow(Math.round((float) translate.getX()), Math.round((float) translate.getY()) - 0.5f, (float) (width - ScaleUtil.calc(e.sr.getScaledWidth()) + getFont().getStringWidth(lower.get() ? m.getDisplayName().toLowerCase() : m.getDisplayName()) * 2 + 6), 9, (int) glowRadius.get(), new Color(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), (int) glowAlpha.get()));
                }
                GL11.glPopMatrix();
                count++;
            }
        }
        ScaleUtil.scale_post();
    }

    public int getColor(int index) {
        if (mode.get().equalsIgnoreCase("Fade"))
            return ColorUtil.fade(10 - (int) speed.get(), index * 50, new Color(s.get()), 1).getRGB();
        if (mode.get().equalsIgnoreCase("Rainbow"))
            return ColorUtil.rainbow(10 - (int) speed.get(), index * 40, 0.7f, 1, 1).getRGB();
        if (mode.get().equalsIgnoreCase("Astolfo"))
            return astolfo(1, index * 25, 0.5f, 10 - speed.get()).getRGB();
        if (mode.get().equalsIgnoreCase("Static"))
            return s.get();
        return -1;
    }

    public static Color astolfo(float yDist, float yTotal, float saturation, float speedt) {
        float speed = 1800f;
        float hue = (System.currentTimeMillis() % (int) speed) + (yTotal - yDist) * speedt;
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.getHSBColor(hue, saturation, 1F);
    }

    //get de

}
