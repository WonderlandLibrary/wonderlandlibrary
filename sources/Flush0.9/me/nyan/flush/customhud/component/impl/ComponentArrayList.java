package me.nyan.flush.customhud.component.impl;

import me.nyan.flush.Flush;
import me.nyan.flush.customhud.component.Component;
import me.nyan.flush.customhud.setting.impl.*;
import me.nyan.flush.module.Module;
import me.nyan.flush.ui.fontrenderer.GlyphPageFontRenderer;
import me.nyan.flush.utils.render.ColorUtils;
import me.nyan.flush.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentArrayList extends Component {
    private ModeSetting theme, animation, sorting, type, sideBar, settingsColor;
    private NumberSetting border, saturation, speed, alpha;
    private ColorSetting color, secondColor;
    private FontSetting font;
    private BooleanSetting fontShadow;
    private GlyphPageFontRenderer fr;

    private float index;

    @Override
    public void onAdded() {
        settings.add(sorting = new ModeSetting("Sorting", "Normal", "None", "Normal", "Reversed", "Alphabetic"));
        settings.add(animation = new ModeSetting("Animation", "Both", "None", "Slide", "Fade", "Both"));
        settings.add(type = new ModeSetting("Type", "Normal", "Normal", "Category", "Random"));
        settings.add(sideBar = new ModeSetting("SideBar", "Right", "None", "Left", "Right", "Outline"));
        settings.add(settingsColor = new ModeSetting("Settings Color", "Gray", "None", "Gray", "Dark Gray", "White"));
        settings.add(border = new NumberSetting("Border", 0, 0, 2, 0.5));
        settings.add(font = new FontSetting("Font", "GoogleSansDisplay", 18));
        settings.add(fontShadow = new BooleanSetting("Font Shadow", true));

        settings.add(theme = new ModeSetting("Theme", () -> type.is("normal"),
                "Astolfo", "Rainbow", "Gradient", "Pulsing", "Astolfo", "Custom"));

        settings.add(saturation = new NumberSetting("Color Saturation", 0.8, 0.3, 1, 0.1,
                () -> type.is("normal") && (theme.is("rainbow") || theme.is("astolfo"))));
        settings.add(speed = new NumberSetting("Color Speed", 2, 0.4, 3, 0.1,
                () -> type.is("normal") && (theme.is("rainbow") || theme.is("gradient"))));

        settings.add(color = new ColorSetting("Color", 0xFFFF0000,
                () -> type.is("normal") && (theme.is("gradient") || theme.is("pulsing") || theme.is("custom"))));
        settings.add(secondColor = new ColorSetting("Second Color", 0xFFFFFF00,
                () -> type.is("normal") && (theme.is("gradient"))));

        settings.add(alpha = new NumberSetting("Alpha", 120, 0, 255, 1));
    }

    @Override
    public void draw(float x2, float y) {
        final ArrayList<Module> modules = new ArrayList<>(Flush.getInstance().getModuleManager().getModules());
        fr = Flush.getFont(font.getFont(), font.getSize());
        boolean onLeft = getXPosition() == Position.LEFT;
        float maxWidth = width();

        index = 0;

        final Comparator<Module> comparator = (m1, m2) -> {
            String nameAndSetting = m1.getName() + (m1.getSuffix() == null ? "" : " " + m1.getSuffix());
            String nameAndSetting1 = m2.getName() + (m2.getSuffix() == null ? "" : " " + m2.getSuffix());
            if (!font.isMinecraftFont()) {
                return Integer.compare(fr.getStringWidth(nameAndSetting1), fr.getStringWidth(nameAndSetting));
            }
            return Integer.compare(mc.fontRendererObj.getStringWidth(nameAndSetting1), mc.fontRendererObj.getStringWidth(nameAndSetting));
        };

        switch (sorting.getValue().toUpperCase()) {
            case "NORMAL":
                modules.sort(comparator);
                break;
            case "REVERSED":
                modules.sort(comparator.reversed());
                break;
            case "ALPHABETIC":
                modules.sort((module1, module2) -> module1.getName().compareToIgnoreCase(module2.getName()));
                break;
        }

        List<Module> modulesDrawn = modules.stream().filter(m -> !m.isHidden() && m.getSlidingLevel() != 0 &&
                        (m.isEnabled() || !animation.is("none")))
                .collect(Collectors.toList());

        float moduleHeight = getModuleHeight();
        int i = 0;
        float lastWidth = 0;
        float lastX = x2;
        int lastAlpha = 255;
        GlStateManager.enableBlend();
        for (Module m : modulesDrawn) {
            String text = getModuleText(m);
            int stringWidth = getMStringWidth(text);

            float mWidth = getModuleWidth(stringWidth);

            float x = x2 + getModuleX(m, onLeft, mWidth, maxWidth);

            boolean first = i == 0;
            boolean second = i == 1;
            boolean last = i + 1 == modulesDrawn.size();
            int alpha = getModuleAlpha(m);

            int moduleColor = getModuleColor(m, alpha, modulesDrawn.size());

            int nextAlpha = 255;
            Module next = null;
            if (i + 1 < modulesDrawn.size()) {
                next = modulesDrawn.get(i + 1);
                nextAlpha = getModuleAlpha(next);
            }

            float offset = index * moduleHeight;

            //background
            GlStateManager.enableBlend();
            RenderUtils.fillRectangle(x, y + offset, mWidth, moduleHeight,
                    ColorUtils.alpha(0xFF000000, (int) (this.alpha.getValueInt() * alpha / 255F)));

            GlStateManager.enableBlend();
            switch (sideBar.getValue().toUpperCase()) {
                case "LEFT":
                    RenderUtils.fillRectangle(x, y + offset, 1.5F, moduleHeight, moduleColor);
                    break;

                case "RIGHT":
                    RenderUtils.fillRectangle(x + mWidth - 1.5F,
                            y + offset, 1.5F, moduleHeight, moduleColor);
                    break;

                case "OUTLINE":
                    if (lastWidth == 0) {
                        lastX = x2 + maxWidth;
                        lastAlpha = alpha;
                    }

                    int nextColor = ColorUtils.alpha(moduleColor, nextAlpha);
                    int lastColor = ColorUtils.alpha(moduleColor, lastAlpha);

                    GL11.glDisable(GL11.GL_LINE_SMOOTH);
                    GL11.glLineWidth(2);

                    RenderUtils.drawLine(
                            first ? x : last ? onLeft ? x + mWidth : x : !onLeft ? x2 + maxWidth - mWidth : x2 + mWidth,
                            y + offset + 0.5F,
                            second ? onLeft ? lastX + lastWidth : lastX : first ? x + mWidth : !onLeft ? x2 +
                                    maxWidth - lastWidth : x2 + lastWidth,
                            y + offset + 0.5F,
                            next != null && next.getSlidingLevel() < 1 ? lastColor : nextColor
                    );

                    RenderUtils.drawLine(
                            x + 0.5F,
                            y + offset,
                            x + 0.5F,
                            y + offset + moduleHeight,
                            moduleColor
                    );

                    RenderUtils.drawLine(
                            x + mWidth - 0.5F,
                            y + offset,
                            x + mWidth - 0.5F,
                            y + offset + moduleHeight,
                            moduleColor
                    );

                    if (last) {
                        RenderUtils.drawLine(
                                x, y + offset + moduleHeight - 0.5F,
                                x + mWidth - 0.5F,
                                y + offset + moduleHeight - 0.5F,
                                moduleColor
                        );
                    }

                    GL11.glEnable(GL11.GL_LINE_SMOOTH);
                    break;
            }

            lastX = x;
            lastWidth = mWidth;
            lastAlpha = alpha;

            GlStateManager.enableBlend();

            float f = sideBar.is("left") || sideBar.is("outline") ? 1.5F : 0;

            if (!font.isMinecraftFont()) {
                fr.drawString(text,
                        x + f + 0.5F,
                        y + offset + getBorder(),
                        moduleColor,
                        fontShadow.getValue()
                );
            } else {
                mc.fontRendererObj.drawString(text,
                        x + f + 2,
                        y + offset + 1 + getBorder(),
                        moduleColor,
                        fontShadow.getValue()
                );
            }

            index += (animation.is("none") ? 1 : m.getSlidingLevel());
            i++;
        }
    }

    private float getModuleHeight() {
        return getMFontHeight() + getBorder() * 2;
    }

    private float getBorder() {
        return border.getValueFloat() + (font.isMinecraftFont() ? 1 : 0);
    }

    private String getModuleText(Module m) {
        return m.getName() + (m.getSuffix() != null ? " " + (settingsColor.is("Dark Gray") ?
                EnumChatFormatting.DARK_GRAY : settingsColor.is("White") ? EnumChatFormatting.WHITE :
                settingsColor.is("Gray") ? EnumChatFormatting.GRAY : "") +
                m.getSuffix() : "");
    }

    private int getMStringWidth(String text) {
        return font.isMinecraftFont() ? mc.fontRendererObj.getStringWidth(text) : fr.getStringWidth(text);
    }

    private int getMFontHeight() {
        return font.isMinecraftFont() ? mc.fontRendererObj.FONT_HEIGHT : fr.getFontHeight();
    }

    private float getModuleWidth(int stringWidth) {
        return stringWidth + (!sideBar.is("none") ? sideBar.is("outline") ? 3 : 1.5F : 0) + 3;
    }

    private float getModuleX(Module m, boolean onLeft, float mWidth, float maxWidth) {
        return (animation.is("slide") || animation.is("both") ?
                (onLeft ? -mWidth * (1 - m.getSlidingLevel()) : maxWidth - mWidth * m.getSlidingLevel()) :
                onLeft ? 0 : maxWidth - mWidth);
    }

    private int getModuleColor(Module m, int alpha, int size) {
        return ColorUtils.alpha(type.is("random") ? m.getColor() :
                type.is("category") ? m.getCategory().remixColor :
                        getColor((int) index, size), alpha);
    }

    private int getModuleAlpha(Module m) {
        return (int) ((animation.is("fade") || animation.is("both") ? m.getSlidingLevel() : 1) * 255);
    }

    public int getColor(int offset, int total) {
        switch (theme.getValue().toLowerCase()) {
            case "custom":
                return color.getRGB();
            case "pulsing":
                return ColorUtils.fade(color.getRGB(), 100, offset);
            case "astolfo":
                return ColorUtils.getAstolfo(saturation.getValueFloat(), 3000F * 0.75F, offset);
            case "gradient":
                return ColorUtils.getGradient(color.getRGB(), secondColor.getRGB(),
                        (int) index, total, speed.getMin() + speed.getMax() - speed.getValue());
            default:
                return ColorUtils.getRainbow(offset, saturation.getValueFloat(), speed.getValueFloat());
        }
    }

    @Override
    public int width() {
        if (fr == null) {
            return 0;
        }
        float i = 0;
        for (Module m : flush.getModuleManager().getModules()) {
            if (m.isHidden() || m.getSlidingLevel() == 0) {
                continue;
            }
            String text = m.getName() + (m.getSuffix() != null ? " " + m.getSuffix() : "");
            int stringWidth = font.isMinecraftFont() ? mc.fontRendererObj.getStringWidth(text) : fr.getStringWidth(text);
            float mWidth = stringWidth + (!sideBar.is("none") ? sideBar.is("outline") ? 3 : 1.5F : 0) + 3;
            if (i < mWidth) {
                i = mWidth;
            }
        }
        return (int) i;
    }

    @Override
    public int height() {
        if (fr == null) {
            return 0;
        }
        return (int) (index * getModuleHeight());
    }

    @Override
    public ResizeType getResizeType() {
        return ResizeType.CUSTOM;
    }
}
