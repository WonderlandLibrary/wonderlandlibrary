package tech.atani.client.feature.theme.impl.element.modulelist;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.feature.theme.impl.element.ModuleListElement;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.animation.advanced.Direction;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;
import tech.atani.client.utility.render.color.ColorUtil;

import java.awt.*;
import java.util.LinkedHashMap;

@ThemeObjectInfo(name = "Xave", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.MODULE_LIST)
public class XaveModuleList extends ModuleListElement {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    private LinkedHashMap<Module, Color> moduleColorHashMap = new LinkedHashMap<>();

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY, LinkedHashMap<Module, DecelerateAnimation> moduleHashMap) {
        if (leftY.get() == 0)
            leftY.set(3);
        FontRenderer roboto18 = FontStorage.getInstance().findFont("Roboto", 18);
        float moduleY = leftY.get();
        for (Module module : moduleHashMap.keySet()) {
            float moduleHeight = roboto18.FONT_HEIGHT + 1;
            if (!moduleHashMap.get(module).finished(Direction.BACKWARDS)) {
                if(!moduleColorHashMap.containsKey(module)) {
                    int baseHue = 15;
                    int minValue = 150;
                    int maxValue = 255;
                    int alpha = 255;
                    moduleColorHashMap.put(module, ColorUtil.generateRandomTonedColor(baseHue, minValue, maxValue, alpha));
                }
                int color = moduleColorHashMap.get(module).getRGB();
                String name = module.getName();
                float rectWidth = (roboto18.getStringWidthInt(name) + 4);
                float moduleX = 2 - rectWidth + (float) (moduleHashMap.get(module).getOutput() * rectWidth) - 2;
                RenderUtil.drawRect(moduleX, moduleY, rectWidth, moduleHeight, new Color(0, 0, 0, 180).getRGB());
                roboto18.drawTotalCenteredStringWithShadow(name, moduleX + rectWidth / 2, moduleY + moduleHeight / 2 + 0.5f, color);
                moduleY += moduleHeight;
            }
        }
    }

    @Override
    public boolean shouldAnimate() {
        return true;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return FontStorage.getInstance().findFont("Roboto", 18);
    }

}
