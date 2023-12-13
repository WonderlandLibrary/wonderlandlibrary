package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.setting.impl.*;
import me.nyan.flush.module.Module;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.other.MathUtils;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class TabGui extends Component {
    private ModeSetting theme, mode;
    private NumberSetting saturation, speed;
    private ColorSetting color;
    private FontSetting font;
    private BooleanSetting fontShadow;
    private GlyphPageFontRenderer fr;

    private float animatedModuleIndex;
    private float animatedCurrentTab;
    public int currentTab;
    public boolean expanded;
    private float expandedLevel;
    private Module.Category category;

    @Override
    public void onAdded() {
        settings.add(mode = new ModeSetting("Mode", "Dark", "Light", "Dark"));
        settings.add(font = new FontSetting("Font", "Roboto Light", 18));
        settings.add(fontShadow = new BooleanSetting("Font Shadow", true));
        settings.add(theme = new ModeSetting("Theme", "Astolfo", "Rainbow", "Pulsing", "Astolfo", "Custom"));
        settings.add(saturation = new NumberSetting("Color Saturation", 0.8, 0.3, 1, 0.1,
                () -> theme.is("rainbow") || theme.is("astolfo")));
        settings.add(speed = new NumberSetting("Color Speed", 2, 0.4, 3, 0.1,
                () -> theme.is("rainbow")));
        settings.add(color = new ColorSetting("Color", 0xFFFF0000,
                () -> theme.is("pulsing") || theme.is("custom")));
    }

    @Override
    public void draw(float x, float y) {
        int width = width();
        fr = Flush.getFont(font.getFont(), font.getSize());

        category = Module.Category.values()[currentTab];

        if (currentTab > animatedCurrentTab) {
            animatedCurrentTab += RenderUtils.calculateSpeed(0.2F);
            if (animatedCurrentTab > currentTab) {
                animatedCurrentTab = currentTab;
            }
        } else if (currentTab < animatedCurrentTab) {
            animatedCurrentTab -= RenderUtils.calculateSpeed(0.2F);
            if (animatedCurrentTab < currentTab) {
                animatedCurrentTab = currentTab;
            }
        }

        if (category.moduleIndex > animatedModuleIndex) {
            animatedModuleIndex += RenderUtils.calculateSpeed(0.2F);
            if (animatedModuleIndex > category.moduleIndex) {
                animatedModuleIndex = category.moduleIndex;
            }
        } else if (category.moduleIndex < animatedModuleIndex) {
            animatedModuleIndex -= RenderUtils.calculateSpeed(0.2F);
            if (animatedModuleIndex < category.moduleIndex) {
                animatedModuleIndex = category.moduleIndex;
            }
        }

        if (expanded) {
            expandedLevel += RenderUtils.calculateSpeed(0.1F);
            if (expandedLevel > 1) {
                expandedLevel = 1;
            }
        } else {
            expandedLevel -= RenderUtils.calculateSpeed(0.1F);
            if (expandedLevel < 0) {
                expandedLevel = 0;
            }
        }

        GlStateManager.pushMatrix();

        int color = mode.is("dark") ? 0xFF232323 : 0xFFF0F0F0;

        Gui.drawRect(x, y, x + width, y + Module.Category.values().length * getOffset() + 2, color);
        Gui.drawRect(x, y + animatedCurrentTab * getOffset(), x + width,
                y + getOffset() + 2 + animatedCurrentTab * getOffset(), getColor());

        int i = 0;
        for (Module.Category c : Module.Category.values()) {
            RenderUtils.drawString(font.isMinecraftFont() ? mc.fontRendererObj : fr,
                    c.name,
                    x + 5 + (8 * MathUtils.clamp((animatedCurrentTab > i ? 1 - animatedCurrentTab + i :
                            1 + animatedCurrentTab - i), 0, 1)),
                    y + i * getOffset() + getOffset() / 2F - fr.getFontHeight() / 2f + 1 + (font.isMinecraftFont() ? 2 : 0),
                    -1,
                    fontShadow.getValue());
            i++;
        }

        if (expanded) {
            Module.Category category = Module.Category.values()[currentTab];
            ArrayList<Module> modules = new ArrayList<>(flush.getModuleManager().getModulesByCategory(category));
            modules.sort((module1, module2) -> module1.getName().compareToIgnoreCase(module2.getName()));

            if (modules.size() == 0) {
                return;
            }

            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtils.glScissor(x + width, y, x + width * 2 + 8, y + (modules.size() * getOffset() + 2) * expandedLevel);

            Gui.drawRect(x + width, y, x + width * 2 + 8, y + modules.size() * getOffset() + 2, color);
            Gui.drawRect(x + width, y + animatedModuleIndex * getOffset(), x + width * 2 + 8, y + getOffset() + 2 + animatedModuleIndex * getOffset(),
                    getColor());

            i = 0;
            for (Module m : modules) {
                RenderUtils.drawString(font.isMinecraftFont() ? mc.fontRendererObj : fr,
                        m.getName(),
                        x + width + 5 + (8 * MathUtils.clamp((animatedModuleIndex > i ? 1 - animatedModuleIndex + i :
                                1 + animatedModuleIndex - i), 0, 1)),
                        y + i * getOffset() + getOffset() / 2f - fr.getFontHeight() / 2F + 1 + (font.isMinecraftFont() ? 2 : 0),
                        m.isEnabled() ? 0xFFAAAAAA : -1,
                        fontShadow.getValue());
                i++;
            }

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }

    private int getOffset() {
        if (fr == null) {
            return 0;
        }
        return font.isMinecraftFont() ? mc.fontRendererObj.FONT_HEIGHT : fr.getFontHeight();
    }

    public int getColor(int offset) {
        switch (theme.getValue().toLowerCase()) {
            case "custom":
                return color.getRGB();
            case "pulsing":
                return ColorUtils.fade(color.getRGB(), 100, offset);
            case "astolfo":
                return ColorUtils.getAstolfo(saturation.getValueFloat(), 3000F * 0.75F, offset);
            default:
                return ColorUtils.getRainbow(offset, saturation.getValueFloat(), speed.getValueFloat());
        }
    }

    @Override
    public void onKey(int key) {
        category = Module.Category.values()[currentTab];
        ArrayList<Module> modules = new ArrayList<>(flush.getModuleManager().getModulesByCategory(category));
        modules.sort((module1, module2) -> module1.getName().compareToIgnoreCase(module2.getName()));

        if (key == Keyboard.KEY_UP) {
            if (expanded) {
                if (category.moduleIndex <= 0) {
                    category.moduleIndex = modules.size() - 1;
                } else {
                    category.moduleIndex--;
                }
            } else {
                if (currentTab <= 0) {
                    currentTab = Module.Category.values().length - 1;
                } else {
                    currentTab--;
                }
            }
        }

        if (key == Keyboard.KEY_DOWN) {
            if (expanded) {
                if (category.moduleIndex >= modules.size() - 1) {
                    category.moduleIndex = 0;
                } else {
                    category.moduleIndex++;
                }
            } else {
                if (currentTab >= Module.Category.values().length - 1) {
                    currentTab = 0;
                } else {
                    currentTab++;
                }
            }
        }

        if (key == Keyboard.KEY_LEFT) {
            expanded = false;
            category.moduleIndex = 0;
        }

        if (key == Keyboard.KEY_RIGHT) {
            if (modules.size() != 0) {
                if (expanded) {
                    modules.get(category.moduleIndex).toggle();
                } else {
                    expanded = true;
                }
            }
        }
    }

    public int getColor() {
        return getColor(1);
    }

    @Override
    public int width() {
        if (fr == null) {
            return 0;
        }

        int i = 0;
        for (Module.Category category : Module.Category.values()) {
            int j = font.isMinecraftFont() ? mc.fontRendererObj.getStringWidth(category.name) : fr.getStringWidth(category.name);
            if (j > i) {
                i = j;
            }
        }
        return i + 25;
    }

    @Override
    public int height() {
        if (fr == null) {
            return 0;
        }
        return Module.Category.values().length * (font.isMinecraftFont() ? mc.fontRendererObj.FONT_HEIGHT : fr.getFontHeight()) + 2;
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.CUSTOM;
    }
}
