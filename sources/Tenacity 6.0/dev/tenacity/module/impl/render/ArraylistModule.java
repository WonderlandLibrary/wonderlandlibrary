package dev.tenacity.module.impl.render;

import dev.tenacity.Tenacity;
import dev.tenacity.event.IEventListener;
import dev.tenacity.event.impl.render.Render2DEvent;
import dev.tenacity.event.impl.render.ShaderEvent;
import dev.tenacity.module.Module;
import dev.tenacity.module.ModuleCategory;
import dev.tenacity.util.render.ColorUtil;
import dev.tenacity.util.render.RenderUtil;
import dev.tenacity.util.render.Theme;
import dev.tenacity.util.render.animation.AnimationDirection;
import dev.tenacity.util.render.font.CustomFont;
import dev.tenacity.util.render.font.FontUtil;
import dev.tenacity.util.tuples.Pair;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ArraylistModule extends Module {

    private List<Module> sortedModules;

    public ArraylistModule() {
        super("Arraylist", "Renders the Module arraylist", ModuleCategory.RENDER);
    }

    private final IEventListener<Render2DEvent> onRender2DEvent = event -> {
        Pair<Color, Color> clientColors = Theme.getThemeColors(HUDModule.theme.getCurrentMode());

        final ScaledResolution scaledResolution = event.getScaledResolution();

        CustomFont font = FontUtil.getFont("OpenSans-Medium", 22);

        sortModules();

        float yOffset = 0;
        int count = 0;
        for (final Module module : sortedModules) {
            module.getArrayListAnimation().setDirection(module.isEnabled() ? AnimationDirection.BACKWARDS : AnimationDirection.FORWARDS);
            if (!module.isEnabled() && module.getArrayListAnimation().isFinished(AnimationDirection.FORWARDS))
                continue;

            final float textWidthMcFont = font.getStringWidth(module.getSuffix() != null ? module.getName() + " " + module.getSuffix() : module.getName());
            final float textHeightMcFont = mc.fontRendererObj.FONT_HEIGHT;

            final int colorIndex = count * 20;
            float finalYOffset = yOffset;


            RenderUtil.drawRect((float) (scaledResolution.getScaledWidth() - textWidthMcFont - 8 + (module.getArrayListAnimation().getOutput() * (textWidthMcFont + 8))), 8 + (yOffset * 12) - 1.5f, textWidthMcFont, textHeightMcFont + 3, new Color(0, 0, 0, 50).getRGB());

            Color textcolor = ColorUtil.interpolateColorsBackAndForth(15, colorIndex, clientColors.getFirst(), clientColors.getSecond(), false);

            font.drawString(module.getName(), (float) (scaledResolution.getScaledWidth() - textWidthMcFont - 8 + (module.getArrayListAnimation().getOutput() * (textWidthMcFont + 8))), 8 + (finalYOffset * 12), textcolor.getRGB());
            if (module.getSuffix() != null) {
                final float suffixWidth = font.getStringWidth(module.getSuffix());
                font.drawString(module.getSuffix(), (float) (scaledResolution.getScaledWidth() - suffixWidth - 8 + (module.getArrayListAnimation().getOutput() * (textWidthMcFont + 8))), 8 + (finalYOffset * 12), new Color(200, 200, 200).getRGB());

            }
            //mc.fontRendererObj.drawStringWithShadow(module.getSuffix() != null ? module.getName() + " §7" + module.getSuffix() : module.getName(), (float) (scaledResolution.getScaledWidth() - textWidthMcFont - 8 + (module.getArrayListAnimation().getOutput() * (textWidthMcFont + 8))), 8 + (yOffset * 12), clientColors.getFirst().getRGB());

            yOffset += 1 - module.getArrayListAnimation().getOutput();
            count++;
        }
    };

    private final IEventListener<ShaderEvent> onShaderEvent = event -> {

        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        CustomFont font = FontUtil.getFont("OpenSans-Medium", 22);

        sortModules();

        float yOffset = 0;
        for (final Module module : sortedModules) {
            module.getArrayListAnimation().setDirection(module.isEnabled() ? AnimationDirection.BACKWARDS : AnimationDirection.FORWARDS);
            if (!module.isEnabled() && module.getArrayListAnimation().isFinished(AnimationDirection.FORWARDS))
                continue;

            final float textWidthMcFont = font.getStringWidth(module.getSuffix() != null ? module.getName() + " " + module.getSuffix() : module.getName());
            final float textHeightMcFont = mc.fontRendererObj.FONT_HEIGHT;

            RenderUtil.drawRect((float) (scaledResolution.getScaledWidth() - textWidthMcFont - 8 + (module.getArrayListAnimation().getOutput() * (textWidthMcFont + 8))), 8 + (yOffset * 12) - 1.5f, textWidthMcFont, textHeightMcFont + 3, new Color(0, 0, 0, 200).getRGB());
            yOffset += 1 - module.getArrayListAnimation().getOutput();
        }
    };

    private void sortModules() {
        if(sortedModules == null) {
            sortedModules = new ArrayList<>(Tenacity.getInstance().getModuleRepository().getModules());
            sortedModules.removeIf(Module::isHidden);
        }
        sortedModules.sort(Comparator.comparingDouble((Module module) -> {
            CustomFont font = FontUtil.getFont("OpenSans-Medium", 22);
            String text = module.getSuffix() != null ? module.getName() + " " + module.getSuffix() : module.getName();
            float textWidth = font.getStringWidth(text);
            float textHeight = mc.fontRendererObj.FONT_HEIGHT;
            return textWidth * textHeight;
        }).reversed());
    }
}
