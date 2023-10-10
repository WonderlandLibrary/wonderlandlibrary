package tech.atani.client.feature.theme.impl.element.modulelist;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.feature.theme.impl.element.ModuleListElement;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.RenderUtil;
import tech.atani.client.utility.render.animation.advanced.Direction;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;

import java.awt.*;
import java.util.LinkedHashMap;

@ThemeObjectInfo(name = "Augustus 2.6", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.MODULE_LIST)
public class OldAugustusModuleList extends ModuleListElement {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY, LinkedHashMap<Module, DecelerateAnimation> moduleHashMap) {
        if (rightY.get() == 0)
            rightY.set(1);
        FontRenderer fontRenderer = mc.fontRendererObj;
        float moduleY = rightY.get();
        for (Module module : moduleHashMap.keySet()) {
            if (!moduleHashMap.get(module).finished(Direction.BACKWARDS)) {
                float moduleHeight = fontRenderer.FONT_HEIGHT + 2;
                float rectLength = (float) ((fontRenderer.getStringWidthInt(module.getName()) + 1) * moduleHashMap.get(module).getOutput());
                RenderUtil.drawRect(scaledResolution.getScaledWidth() - rectLength, moduleY, rectLength, moduleHeight, new Color(0, 0, 0, 100).getRGB());
                fontRenderer.drawStringWithShadow(module.getName(), scaledResolution.getScaledWidth() - rectLength + 0.5f, moduleY + moduleHeight / 2 - fontRenderer.FONT_HEIGHT / 2, new Color(0, 0, 255).getRGB());
                // The 20 is there so the rect goes out of the screen and therefore the right part's not rounded
                moduleY += moduleHeight;
            }
        }
    }

    @Override
    public boolean shouldAnimate() {
        return false;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return mc.fontRendererObj;
    }

}
