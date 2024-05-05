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
import tech.atani.client.utility.render.animation.advanced.Direction;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;
import tech.atani.client.utility.render.shader.shaders.GradientShader;
import tech.atani.client.utility.render.shader.shaders.RoundedShader;

import java.awt.*;
import java.util.LinkedHashMap;

@ThemeObjectInfo(name = "Icarus", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.MODULE_LIST)
public class IcarusModuleList extends ModuleListElement {

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY, LinkedHashMap<Module, DecelerateAnimation> moduleHashMap) {
        FontRenderer fontRenderer = FontStorage.getInstance().findFont("Pangram Regular", 17);
        float moduleY = rightY.get();
        float gradientWidth = 1f;
        for (Module module : moduleHashMap.keySet()) {
            if (!moduleHashMap.get(module).finished(Direction.BACKWARDS)) {
                float moduleHeight = fontRenderer.FONT_HEIGHT + 4;
                float rectLength = (float) ((fontRenderer.getStringWidthInt(module.getName() + 3) * moduleHashMap.get(module).getOutput()) - gradientWidth);
                RoundedShader.drawRound(scaledResolution.getScaledWidth() - rectLength, moduleY, rectLength + 20, moduleHeight, 2, new Color(21, 21, 21));
                fontRenderer.drawString(module.getName(), scaledResolution.getScaledWidth() - rectLength + 1.5f, moduleY + moduleHeight / 2 - fontRenderer.FONT_HEIGHT / 2, -1);
                // The 20 is there so the rect goes out of the screen and therefore the right part's not rounded
                moduleY += moduleHeight;
            }
        }
        GradientShader.drawGradientTB(scaledResolution.getScaledWidth() - gradientWidth, rightY.get(), gradientWidth, moduleY + 0.5f, 1, new Color(ICARUS_FIRST), new Color(ICARUS_SECOND));
    }

    @Override
    public boolean shouldAnimate() {
        return true;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return FontStorage.getInstance().findFont("Pangram Regular", 17);
    }

}
