package me.nyan.flush.module.impl.render;

import me.nyan.flush.Flush;
import me.nyan.flush.event.SubscribeEvent;
import me.nyan.flush.event.impl.Event2D;
import me.nyan.flush.event.impl.EventKey;
import me.nyan.flush.module.Module;
import me.nyan.flush.module.settings.BooleanSetting;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.movement.MovementUtils;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.PboUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class SigmaHUD extends Module {
    private String sigmaname = "Gello";

    private float aModuleIndex;
    private float aCurrentTab;
    public int currentTab;
    public boolean expanded;
    private Category category;

    private int startColorA;
    private int endColorA;

    private int mStartColorA;
    private int mEndColorA;

    private final PboUtils pbos = new PboUtils(8);

    public final BooleanSetting modList = new BooleanSetting("Mod List", this, true),
            tabGui = new BooleanSetting("TabGUI", this, true),
            info = new BooleanSetting("HUD Info", this, true);

    public SigmaHUD() {
        super("SigmaHUD", Category.RENDER);
    }

    public String getSigmaname() {
        return sigmaname;
    }

    public void setSigmaname(String sigmaname) {
        this.sigmaname = sigmaname;
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @SubscribeEvent
    public void onRender2D(Event2D e) {
        //logo
        if (!mc.gameSettings.showDebugInfo) {
            GlStateManager.pushMatrix();

            if (mc.gameSettings.ofShowFps) {
                GlStateManager.translate(0, 12, 0);
            }

            if (tabGui.getValue()) {
                drawTabGui();
            }

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.5, 1.5, 1);
            Flush.getFont("Roboto Light", 40).drawString(Flush.NAME, 7 / 1.5F, 4 / 1.5F, 0xAAFFFFFF);
            GlStateManager.popMatrix();

            Flush.getFont("GoogleSansDisplay", 18).drawString(sigmaname, 9, 4
                    + Flush.getFont("Roboto Light", 40).getFontHeight() + 5, 0xAAFFFFFF);

            //ModList
            if (modList.getValue()) {
                final ArrayList<Module> modules = new ArrayList<>(flush.getModuleManager().getModules());
                GlyphPageFontRenderer modsFont = Flush.getFont("Roboto Light", 20);

                float index = 0;

                modules.sort((m1, m2) -> Integer.compare(modsFont.getStringWidth(m2.getName()), modsFont.getStringWidth(m1.getName())));

                GlStateManager.pushMatrix();

                for (Module m : modules) {
                    if (m.isHidden() || m.getSlidingLevel() == 0) {
                        continue;
                    }

                    float offset = index * (modsFont.getFontHeight() + 1);
                    modsFont.drawString(m.getName(), e.getWidth() - (modsFont.getStringWidth(m.getName()) + 5), offset + 2.5f,
                            ColorUtils.alpha(-1, m.getSlidingLevel() * 255));

                    if (m.getSlidingLevel() > 0) {
                        index = index + 1 * m.getSlidingLevel();
                    }
                }
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
        }

        //Information
        if (info.getValue()) {
            if (!(mc.gameSettings.showDebugInfo && !mc.thePlayer.hasReducedDebug() && !mc.gameSettings.reducedDebugInfo)) {
                float animation = MathUtils.clamp((float) MovementUtils.getSpeed() * 20F, 0, 1);
                String pos = Math.round(mc.thePlayer.posX) + " " + Math.round(mc.thePlayer.posY) + " " + Math.round(mc.thePlayer.posZ);

                GlStateManager.pushMatrix();
                float scale = 0.9F + 0.1F * animation;
                GlStateManager.scale(scale, scale, 1);
                Flush.getFont("Roboto Light", 18).drawXCenteredString(pos, 43 / scale,
                        (tabGui.getValue() ? 152 : 46) / scale, ColorUtils.alpha(-1, (int) (170 + 85 * animation)));
                GlStateManager.popMatrix();
            }
        }
    }

    @SubscribeEvent
    public void onKey(EventKey e) {
        if (!tabGui.getValue()) {
            return;
        }

        category = Category.values()[currentTab % Category.values().length];
        ArrayList<Module> modules = new ArrayList<>(flush.getModuleManager().getModulesByCategory(category));
        modules.sort((module1, module2) -> module1.getName().compareToIgnoreCase(module2.getName()));

        if (e.getKey() == Keyboard.KEY_UP) {
            if (expanded) {
                category.moduleIndex--;
                if (category.moduleIndex < 0) {
                    category.moduleIndex = modules.size() - 1;
                    aModuleIndex = modules.size() - 1;
                }
            } else {
                currentTab--;
                if (currentTab < 0) {
                    currentTab = Category.values().length - 1;
                    aCurrentTab = Category.values().length - 1;
                }
            }
        }

        if (e.getKey() == Keyboard.KEY_DOWN) {
            if (expanded) {
                category.moduleIndex++;
                if (category.moduleIndex >= modules.size()) {
                    category.moduleIndex = 0;
                    aModuleIndex = 0;
                }
            } else {
                currentTab++;
                if (currentTab >= Category.values().length) {
                    currentTab = 0;
                    aCurrentTab = 0;
                }
            }
        }

        if (e.getKey() == Keyboard.KEY_LEFT) {
            expanded = false;
            category.moduleIndex = 0;
        }

        if (e.getKey() == Keyboard.KEY_RIGHT) {
            if (modules.size() != 0) {
                if (expanded) {
                    modules.get(category.moduleIndex % flush.getModuleManager().getModulesByCategory(category).size()).toggle();
                } else {
                    expanded = true;
                }
            }
        }
    }

    private void drawTabGui() {
        int x = 8;
        int y = 47;
        int width = 75;
        int offset = 17;
        GlyphPageFontRenderer font = Flush.getFont("Roboto Light", 20);

        category = Category.values()[currentTab % Category.values().length];

        if (currentTab > aCurrentTab) {
            aCurrentTab += 0.1F / 10F * Flush.getFrameTime();
            if (aCurrentTab > currentTab) {
                aCurrentTab = currentTab;
            }
        } else if (currentTab < aCurrentTab) {
            aCurrentTab -= 0.1F / 10F * Flush.getFrameTime();
            if (aCurrentTab < currentTab) {
                aCurrentTab = currentTab;
            }
        }

        if (category.moduleIndex > aModuleIndex) {
            aModuleIndex += 0.1F / 10F * Flush.getFrameTime();
            if (aModuleIndex > category.moduleIndex) {
                aModuleIndex = category.moduleIndex;
            }
        } else if (category.moduleIndex < aModuleIndex) {
            aModuleIndex -= 0.1F / 10F * Flush.getFrameTime();
            if (aModuleIndex < category.moduleIndex) {
                aModuleIndex = category.moduleIndex;
            }
        }

        int currentTab = this.currentTab % Category.values().length;
        float aCurrentTab = this.aCurrentTab % Category.values().length;
        float readWidth = width / 2F;

        float factor = new ScaledResolution(mc).getScaleFactor();

        pbos.init(0, readWidth, 1 / factor);
        pbos.init(1, readWidth, 1 / factor);
        int startColor = pbos.getAverageColor(0);
        int endColor = pbos.getAverageColor(1);
        pbos.update(0, x + width / 2F - readWidth / 2F, y - 1);
        pbos.update(1, x + width / 2F - readWidth / 2F, y + Category.values().length * offset + 1);

        startColorA = ColorUtils.animateColor2(startColorA, startColor, 4);
        endColorA = ColorUtils.animateColor2(endColorA, endColor, 4);

        RenderUtils.drawGradientRect(x, y, x + width, y + Category.values().length * offset,
                ColorUtils.brightness(startColorA, 0.98F),
                ColorUtils.brightness(endColorA, 0.98F));

        pbos.init(2, 1 / factor, 1 / factor);
        pbos.init(3, 1 / factor, 1 / factor);
        int startColor2 = pbos.getPixelColor(2, 0, 0);
        int endColor2 = pbos.getPixelColor(3, 0, 0);
        pbos.update(2, x + width / 2F, y + aCurrentTab * offset + 1);
        pbos.update(3, x + width / 2F, y + aCurrentTab * offset + offset - 1);

        RenderUtils.drawGradientRect(x, y + aCurrentTab * offset,
                x + width, y + aCurrentTab * offset + offset,
                ColorUtils.brightness(startColor2, 0.93F),
                ColorUtils.brightness(endColor2, 0.93F));

        int i = 0;
        for (Category c : Module.Category.values()) {
            font.drawString(
                    c.name,
                    x + 5 + (8 * MathUtils.clamp((aCurrentTab > i ? 1 - aCurrentTab + i : 1 + aCurrentTab - i), 0, 1)),
                    y + i * offset + offset / 2f - font.getFontHeight() / 2f,
                    -1
            );
            i++;
        }

        if (expanded) {
            int modulesWidth = 85;
            Category category = Module.Category.values()[currentTab];
            ArrayList<Module> modules = new ArrayList<>(flush.getModuleManager().getModulesByCategory(category));
            modules.sort((module1, module2) -> module1.getName().compareToIgnoreCase(module2.getName()));

            float aModuleIndex = this.aModuleIndex % modules.size();

            if (modules.size() == 0) {
                return;
            }

            pbos.init(4, readWidth, 1 / factor);
            pbos.init(5, readWidth, 1 / factor);
            startColor = pbos.getAverageColor(4);
            endColor = pbos.getAverageColor(5);
            pbos.update(4, x + width + 2 + modulesWidth / 2F - readWidth / 2F, y);
            pbos.update(5, x + width + 2 + modulesWidth / 2F - readWidth / 2F,
                    y + modules.size() * offset);

            mStartColorA = ColorUtils.animateColor2(mStartColorA, startColor, 4);
            mEndColorA = ColorUtils.animateColor2(mEndColorA, endColor, 4);

            RenderUtils.drawGradientRect(x + width + 2, y,
                    x + 2 + width + modulesWidth, y + modules.size() * offset,
                    ColorUtils.brightness(mStartColorA, 0.98F),
                    ColorUtils.brightness(mEndColorA, 0.98F));

            pbos.init(6, 1 / factor, 1 / factor);
            pbos.init(7, 1 / factor, 1 / factor);
            startColor2 = pbos.getPixelColor(6, 0, 0);
            endColor2 = pbos.getPixelColor(7, 0, 0);
            pbos.update(6, x + width + 2, y + aModuleIndex * offset + 1);
            pbos.update(7, x + width + 2, y + offset + aModuleIndex * offset - 1);

            RenderUtils.drawGradientRect(x + width + 2, y + aModuleIndex * offset,
                    x + width + 2 + modulesWidth, y + aModuleIndex * offset + offset,
                    ColorUtils.brightness(startColor2, 0.93F),
                    ColorUtils.brightness(endColor2, 0.93F));

            i = 0;
            for (Module m : modules) {
                GlyphPageFontRenderer mfont = m.isEnabled() ? Flush.getFont("GoogleSansDisplay", 20) : font;
                mfont.drawString(
                        m.getName(),
                        x + width + 7 + (8 * MathUtils.clamp((aModuleIndex > i ? 1 - aModuleIndex + i : 1 + aModuleIndex - i), 0, 1)),
                        y + i * offset + offset / 2f - font.getFontHeight() / 2f,
                        -1
                );
                i++;
            }
        }
    }
}