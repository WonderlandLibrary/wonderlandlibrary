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
import tech.atani.client.utility.render.shader.render.ingame.RenderableShaders;

import java.awt.*;
import java.util.LinkedHashMap;

@ThemeObjectInfo(name = "Ryu", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.MODULE_LIST)
public class RyuModuleList extends ModuleListElement {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY, LinkedHashMap<Module, DecelerateAnimation> moduleHashMap) {
        if (leftY.get() == 0)
            leftY.set(6);
        FontRenderer roboto17 = FontStorage.getInstance().findFont("Roboto Medium", 17);
        // This is disgusting
        RenderableShaders.renderAndRun(false, true, () -> {
            float moduleY = leftY.get();
            for (Module module : moduleHashMap.keySet()) {
                float moduleHeight = roboto17.FONT_HEIGHT + 3;
                if (!moduleHashMap.get(module).finished(Direction.BACKWARDS)) {
                    String name = module.getName();
                    float rectWidth = (roboto17.getStringWidthInt(name) + 4);
                    float moduleX = 2 - rectWidth + (float) (moduleHashMap.get(module).getOutput() * rectWidth);
                    RenderUtil.drawRect(moduleX, moduleY, rectWidth, moduleHeight, new Color(0, 0, 0, 80).getRGB());
                    moduleY += moduleHeight;
                }
            }
        });
        RenderableShaders.renderAndRun(true, false, () -> {
            float moduleY = leftY.get();
            for (Module module : moduleHashMap.keySet()) {
                float moduleHeight = roboto17.FONT_HEIGHT + 3;
                if (!moduleHashMap.get(module).finished(Direction.BACKWARDS)) {
                    String name = module.getName();
                    float rectWidth = (roboto17.getStringWidthInt(name) + 4);
                    float moduleX = 2 - rectWidth + (float) (moduleHashMap.get(module).getOutput() * rectWidth);
                    FontStorage.getInstance().findFont("Roboto", 17).drawTotalCenteredString(name, moduleX + rectWidth / 2, moduleY + moduleHeight / 2 + 0.5f, new Color(0, 0, 0).getRGB());
                    moduleY += moduleHeight;
                }
            }
        });
        float moduleY = leftY.get();
        for (Module module : moduleHashMap.keySet()) {
            float moduleHeight = roboto17.FONT_HEIGHT + 3;
            if (!moduleHashMap.get(module).finished(Direction.BACKWARDS)) {
                String name = module.getName();
                float rectWidth = (roboto17.getStringWidthInt(name) + 4);
                float moduleX = 2 - rectWidth + (float) (moduleHashMap.get(module).getOutput() * rectWidth);
                roboto17.drawTotalCenteredString(name, moduleX + rectWidth / 2, moduleY + moduleHeight / 2 + 0.5f, -1);
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
        return FontStorage.getInstance().findFont("Roboto Medium", 17);
    }

}
