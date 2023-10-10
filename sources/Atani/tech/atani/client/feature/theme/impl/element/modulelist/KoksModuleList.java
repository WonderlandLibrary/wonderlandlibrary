package tech.atani.client.feature.theme.impl.element.modulelist;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.feature.theme.impl.element.ModuleListElement;
import tech.atani.client.utility.java.MapUtil;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.animation.advanced.Direction;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@ThemeObjectInfo(name = "Koks", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.MODULE_LIST)
public class KoksModuleList extends ModuleListElement {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY, LinkedHashMap<Module, DecelerateAnimation> moduleHashMap) {
        final FontRenderer roboto18 = FontStorage.getInstance().findFont("Roboto", 18);

        int currentOffset = 0;
        int y = (int) rightY.get();

        final float size = 3.5f;
        final float depth = 1F;
        final int posX = (int) (depth + size) + 1;
        final int posY = ((int) (depth + size) + 2) * -1;
        float addY = 3f;

        int i = 0;
        for(Module module : moduleHashMap.keySet()) {
            if (!moduleHashMap.get(module).finished(Direction.BACKWARDS)) {
                final String displayName = module.getName();
                final float height = (roboto18.FONT_HEIGHT + addY);

                final int l = (int) ((float) (scaledResolution.getScaledWidth() - roboto18.getStringWidthInt(displayName) - 1 - posX) - size - depth);
                final int r = (int) (scaledResolution.getScaledWidth() - posX + size + depth);
                final double distance = r - l;

                if (i == 0) {
                    int lIndex = 1;

                    for (double left = l; left < r; left += distance / displayName.length()) {
                        double nextStep = left + distance / displayName.length();
                        nextStep = MathHelper.clamp_double(nextStep + depth, left, r);
                        Gui.drawRect((int) left, (int) (y - depth - posY), (int) (nextStep), (int) (y - posY), getRainbow(100 * (lIndex + (moduleHashMap.size() - i) + 1), 6000, (float) 0.6f, 1).getRGB());
                        lIndex++;
                    }
                }

                final float bottom = y + height + depth + 1.5F - posY;
                ArrayList<Module> modules = new ArrayList<>(MapUtil.convertMapKeysToList(moduleHashMap));
                modules.removeIf(module1 -> moduleHashMap.get(module1).finished(Direction.BACKWARDS));
                if (i + 1 < modules.size()) {
                    final Module nextModule = modules.get(i + 1);
                    final float difference = (int) (roboto18.getStringWidthInt(displayName) - roboto18.getStringWidthInt(nextModule.getName()));
                    Gui.drawRect((int) (((float) (scaledResolution.getScaledWidth() - roboto18.getStringWidthInt(displayName) - 1 - posX) - size - depth)), (int) (y + height + 1.5F - posY), (int) ((scaledResolution.getScaledWidth() - roboto18.getStringWidthInt(displayName) - 1 - posX) + difference - size), (int) bottom, getRainbow(100 * ((moduleHashMap.size() - i) + 1), 6000, (float) 0.6f, 1).getRGB());
                } else {
                    int lIndex = 1;
                    for (double left = l; left < r; left += distance / displayName.length()) {
                        Gui.drawRect((int) left, (int) (y + height + 1.5f - posY), (int) (left + distance / displayName.length()), (int) bottom, getRainbow(100 * ((lIndex + 1) + (moduleHashMap.size() - i) + 1), 6000, (float) 0.6f, 1).getRGB());
                        lIndex++;
                    }
                }

                Gui.drawRect((int) (scaledResolution.getScaledWidth() - posX + size), (int) (y - posY), (int) (scaledResolution.getScaledWidth() - posX + size + depth), (int) (y + height + 2.5F - posY), getRainbow(100 * (displayName.length() + (moduleHashMap.size() - i) + 2), 6000, (float) 0.6f, 1).getRGB());

                Gui.drawRect((int) ((scaledResolution.getScaledWidth() - roboto18.getStringWidthInt(displayName) - 1 - posX) - size), (int) (y - posY - (i == 0 ? 0 : 1)), (int) (scaledResolution.getScaledWidth() - posX + size), (int) (y + (roboto18.FONT_HEIGHT + addY) + 2 - 1 - posY), Integer.MIN_VALUE);
                Gui.drawRect((int) ((scaledResolution.getScaledWidth() - roboto18.getStringWidthInt(displayName) - 1 - posX) - size - depth), y - posY - 1, (int) ((scaledResolution.getScaledWidth() - roboto18.getStringWidthInt(displayName) - 1 - posX) - size), (int) (y + height + 2.5F - posY), getRainbow(100 * ((moduleHashMap.size() - i) + 1), 6000, (float) 0.6f, 1).getRGB());

                for (int c = 0; c < displayName.length(); c++) {
                    final char character = displayName.charAt(c);
                    roboto18.drawStringWithShadow(character + "", (float) (scaledResolution.getScaledWidth() - roboto18.getStringWidthInt(displayName) - 1 - posX) + roboto18.getStringWidthInt(displayName.substring(0, c)), y + 1.5f - posY + 1, getRainbow(100 * (c + (moduleHashMap.size() - i) + 1), 6000, (float) 0.6f, 1).getRGB());
                }

                y += (roboto18.FONT_HEIGHT + addY) + 2;
                currentOffset += 100;
                i ++;
            }
        }
    }

    // Yea I skidded the method to get the exact same rainbow, SUE ME
    public Color getRainbow(int offset, int speed, float saturation, float brightness) {
        float hue = ((System.currentTimeMillis() + offset) % speed) / (float) speed;
        return Color.getHSBColor(hue, saturation, brightness);
    }

    @Override
    public boolean shouldAnimate() {
        return false;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return FontStorage.getInstance().findFont("Roboto", 18);
    }

}
