package tech.atani.client.feature.theme.impl.element;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import tech.atani.client.feature.module.Module;
import tech.atani.client.feature.theme.ThemeObject;
import tech.atani.client.utility.math.atomic.AtomicFloat;
import tech.atani.client.utility.render.animation.advanced.impl.DecelerateAnimation;

import java.util.LinkedHashMap;

public abstract class ModuleListElement extends ThemeObject {

    @Override
    public void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY, Object[] params) {
        onDraw(scaledResolution, partialTicks, leftY, rightY, (LinkedHashMap<Module, DecelerateAnimation>) params[0]);
    }

    public abstract void onDraw(ScaledResolution scaledResolution, float partialTicks, AtomicFloat leftY, AtomicFloat rightY, LinkedHashMap<Module, DecelerateAnimation> moduleMap);

    public abstract boolean shouldAnimate();

    public abstract FontRenderer getFontRenderer();

}
