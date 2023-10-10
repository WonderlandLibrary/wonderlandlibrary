package tech.atani.client.feature.theme.impl.element.modulelist;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.feature.font.storage.FontStorage;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.module.impl.hud.ModuleList;
import tech.atani.client.feature.module.storage.ModuleStorage;
import tech.atani.client.feature.theme.data.ThemeObjectInfo;
import tech.atani.client.feature.theme.data.enums.ElementType;
import tech.atani.client.feature.theme.data.enums.ThemeObjectType;
import tech.atani.client.feature.theme.impl.element.ModuleListElement;
import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.value.impl.SliderValue;
import tech.atani.client.feature.value.storage.ValueStorage;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.animation.advanced.Direction;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;
import tech.atani.client.utility.render.color.ColorUtil;

import java.awt.*;
import java.util.LinkedHashMap;

@ThemeObjectInfo(name = "OHareWare", themeObjectType = ThemeObjectType.ELEMENT, elementType = ElementType.MODULE_LIST)
public class OHareWareModuleList extends ModuleListElement {

    private SliderValue<Integer> red = new SliderValue<>("Red", "What'll be the red of the color?", this, 255, 0, 255, 0);
    private SliderValue<Integer> green = new SliderValue<>("Green", "What'll be the green of the color?", this, 255, 0, 255, 0);
    private SliderValue<Integer> blue = new SliderValue<>("Blue", "What'll be the blue of the color?", this, 255, 0, 255, 0);

    @Override
    public void onEnable() {
        ModuleList moduleList = ModuleStorage.getInstance().getByClass(ModuleList.class);
        for(Value value : ValueStorage.getInstance().getValues(this, false))
            ValueStorage.getInstance().addLinkedValues(moduleList, value);
    }

    @Override
    public void onDisable() {
        ModuleList moduleList = ModuleStorage.getInstance().getByClass(ModuleList.class);
        for(Value value : ValueStorage.getInstance().getValues(this, false))
            ValueStorage.getInstance().removeLinkedValues(moduleList, value);
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY, LinkedHashMap<Module, DecelerateAnimation> moduleHashMap) {
        FontRenderer fontRenderer = FontStorage.getInstance().findFont("Tahoma", 19);
        float moduleY = rightY.get();
        int counter = 0;
        for (Module module : moduleHashMap.keySet()) {
            if (!moduleHashMap.get(module).finished(Direction.BACKWARDS)) {
                float moduleHeight = fontRenderer.FONT_HEIGHT + 1f;
                float rectLength = (float) ((fontRenderer.getStringWidthInt(module.getName()) * moduleHashMap.get(module).getOutput()));
                Color firstColor = new Color(red.getValue(), green.getValue(), blue.getValue());
                fontRenderer.drawStringWithShadow(module.getName(), scaledResolution.getScaledWidth() - rectLength - 1, moduleY + moduleHeight / 2 - fontRenderer.FONT_HEIGHT / 2, ColorUtil.fadeBetween(firstColor.getRGB(), ColorUtil.darken(firstColor.getRGB(), 0.49F), counter * 50L));
                moduleY += moduleHeight;
                counter++;
            }
        }
    }

    @Override
    public boolean shouldAnimate() {
        return false;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return FontStorage.getInstance().findFont("Tahoma", 19);
    }

}
