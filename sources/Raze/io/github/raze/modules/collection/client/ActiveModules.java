package io.github.raze.modules.collection.client;

import io.github.raze.Raze;
import io.github.raze.modules.system.AbstractModule;
import io.github.raze.modules.system.information.ModuleCategory;
import io.github.raze.modules.system.ingame.ModuleIngame;
import io.github.raze.settings.collection.ArraySetting;
import io.github.raze.settings.collection.BooleanSetting;
import io.github.raze.settings.collection.NumberSetting;
import io.github.raze.utilities.collection.fonts.CFontUtil;
import io.github.raze.utilities.collection.visual.ColorUtil;
import io.github.raze.utilities.collection.visual.RenderUtil;
import io.github.raze.utilities.system.fonts.CFontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ActiveModules extends ModuleIngame {

    private final ArraySetting font, size, type, textColor, rectColor, fontCase, rectPosition;
    private final NumberSetting textRed, textGreen, textBlue, textAlpha;
    private final NumberSetting rectRed, rectGreen, rectBlue, rectAlpha;
    private final BooleanSetting colorPulseEffect, rect, background, wordSpacing, hideRenderModules;

    public ActiveModules() {
        super("ActiveModules", "Displays active modules as a list.", ModuleCategory.CLIENT);

        this.x = 350;
        this.y = 350;

        Raze.INSTANCE.managerRegistry.settingRegistry.add(

                font = new ArraySetting(this, "Font Mode", "Jello", "Jello", "Minecraft"),
                fontCase = new ArraySetting(this, "Font Case", "Normal", "Normal", "Lower", "Upper"),

                size = new ArraySetting(this, "Font Size", "20", "12", "16", "20", "24", "28", "32", "36", "40")
                        .setHidden(() -> !font.compare("Jello")),

                type = new ArraySetting(this, "Font Type", "Light", "Light", "Regular", "Medium")
                        .setHidden(() -> !font.compare("Jello")),

                textColor = new ArraySetting(this, "Font Color", "Custom", "Custom", "Rainbow", "Astolfo"),

                textRed = new NumberSetting(this, "Text Red", 0, 255, 255)
                        .setHidden(() -> !textColor.compare("Custom")),

                textGreen = new NumberSetting(this, "Text Green", 0, 255, 255)
                        .setHidden(() -> !textColor.compare("Custom")),

                textBlue = new NumberSetting(this, "Text Blue", 0, 255, 255)
                        .setHidden(() -> !textColor.compare("Custom")),

                textAlpha = new NumberSetting(this, "Text Alpha", 0, 255, 185)
                        .setHidden(() -> !textColor.compare("Custom")),

                rect = new BooleanSetting(this, "Rect", false),

                rectPosition = new ArraySetting(this, "Rect Position", "Right", "Right", "Left")
                        .setHidden(() -> !rect.get()),

                rectColor = new ArraySetting(this, "Rect Color", "Custom", "Custom", "Rainbow", "Astolfo")
                        .setHidden(() -> !rect.get()),

                rectRed = new NumberSetting(this, "Rect Red", 0, 255, 255)
                        .setHidden(() -> !(rect.get() && rectColor.compare("Custom"))),

                rectGreen = new NumberSetting(this, "Rect Green", 0, 255, 255)
                        .setHidden(() -> !(rect.get() && rectColor.compare("Custom"))),

                rectBlue = new NumberSetting(this, "Rect Blue", 0, 255, 255)
                        .setHidden(() -> !(rect.get() && rectColor.compare("Custom"))),

                rectAlpha = new NumberSetting(this, "Rect Alpha", 0, 255, 185)
                        .setHidden(() -> !(rect.get() && rectColor.compare("Custom"))),

                background = new BooleanSetting(this, "Background", false),
                colorPulseEffect = new BooleanSetting(this, "Color Pulsing Effect", true),
                wordSpacing = new BooleanSetting(this, "Word Spacing", false),
                hideRenderModules = new BooleanSetting(this, "Hide Visual Modules", false)

        );

        setEnabled(true);
    }

    @Override
    public void renderIngame() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        CFontRenderer fontRenderer = CFontUtil.getFontByName("Jello " + type.get() + " " + size.get()).getRenderer();

        List<AbstractModule> modules = Raze.INSTANCE.managerRegistry.moduleRegistry.getEnabledModules()

                .stream()
                .filter(module ->
                        !module.getName().equalsIgnoreCase("ActiveModules") &&
                        !module.getName().equalsIgnoreCase("Watermark")
                )

                .sorted(Comparator.comparingDouble(module -> {

                    double width = 0;

                    switch (font.get()) {
                        case "Jello": {
                            width = fontRenderer.getStringWidth(module.getName());
                            break;
                        }

                        case "Minecraft": {
                            width = mc.fontRenderer.getStringWidth(module.getName());
                            break;
                        }
                    }

                    return -width;
                })).sorted(Comparator.comparingDouble(module -> {
                    String name = module.getName();
                    if (wordSpacing.get()) {
                        String[] words = name.split("(?=\\p{Upper})");
                        if (words.length > 1) {
                            name = String.join(" ", words);
                        }
                    }

                    double width = 0;

                    switch (font.get()) {
                        case "Jello": {
                            width = fontRenderer.getStringWidth(name);
                            break;
                        }
                        case "Minecraft": {
                            width = mc.fontRenderer.getStringWidth(name);
                            break;
                        }
                    }

                    return -width;
                })).collect(Collectors.toList());

        if (hideRenderModules.get()) {
            List<AbstractModule> visualModules = Raze.INSTANCE.managerRegistry.moduleRegistry.getModulesByCategory(ModuleCategory.VISUAL);
            modules.removeAll(visualModules);
        }

        AtomicInteger counter = new AtomicInteger();

        float textRed, textGreen, textBlue, textAlpha;
        switch (textColor.get()) {
            case "Custom":
                if (colorPulseEffect.get()) {
                    float pulse = (float) Math.sin((System.currentTimeMillis() + counter.get() * 100) / 500.0) * 0.25f + 0.75f;
                    float limit = Math.max(0.40f, Math.min(1.0f, pulse));
                    float brightness = 1.0f + 0.5f * (limit - 1.0f);
                    textRed = (this.textRed.get().floatValue() / 255.0f) * brightness;
                    textGreen = (this.textGreen.get().floatValue() / 255.0f) * brightness;
                    textBlue = (this.textBlue.get().floatValue() / 255.0f) * brightness;
                } else {
                    textRed = this.textRed.get().floatValue() / 255.0f;
                    textGreen = this.textGreen.get().floatValue() / 255.0f;
                    textBlue = this.textBlue.get().floatValue() / 255.0f;
                }
                textAlpha = this.textAlpha.get().floatValue() / 255.0f;
                break;

            case "Rainbow":
                if (colorPulseEffect.get()) {
                    Color rainbowColor = ColorUtil.getRainbow(5.0f, 1.0f, 1.0f);
                    float pulse = (float) Math.sin((System.currentTimeMillis() + counter.get() * 100) / 500.0) * 0.25f + 0.75f;
                    float limit = Math.max(0.70f, Math.min(1.0f, pulse));
                    textRed = rainbowColor.getRed() / 255.0f;
                    textGreen = rainbowColor.getGreen() / 255.0f;
                    textBlue = rainbowColor.getBlue() / 255.0f;
                    textAlpha = limit;
                } else {
                    Color rainbowColor = ColorUtil.getRainbow(5.0f, 1.0f, 1.0f);
                    textRed = rainbowColor.getRed() / 255.0f;
                    textGreen = rainbowColor.getGreen() / 255.0f;
                    textBlue = rainbowColor.getBlue() / 255.0f;
                    textAlpha = 1.0f;
                }
                break;

            default:
                if (colorPulseEffect.get()) {
                    int astolfoColor = ColorUtil.AstolfoRGB(0, 2000);
                    float pulse = (float) Math.sin((System.currentTimeMillis() + counter.get() * 100) / 500.0) * 0.25f + 0.75f;
                    float limit = Math.max(0.70f, Math.min(1.0f, pulse));
                    textRed = ((astolfoColor >> 16) & 0xFF) / 255.0f;
                    textGreen = ((astolfoColor >> 8) & 0xFF) / 255.0f;
                    textBlue = (astolfoColor & 0xFF) / 255.0f;
                    textAlpha = limit;
                } else {
                    int astolfoColor = ColorUtil.AstolfoRGB(0, 2000);
                    textRed = ((astolfoColor >> 16) & 0xFF) / 255.0f;
                    textGreen = ((astolfoColor >> 8) & 0xFF) / 255.0f;
                    textBlue = (astolfoColor & 0xFF) / 255.0f;
                    textAlpha = 1.0f;
                }
                break;
        }

        float rectRed, rectGreen, rectBlue, rectAlpha;
        switch (rectColor.get()) {
            case "Custom":
                if (colorPulseEffect.get()) {
                    float pulse = (float) Math.sin((System.currentTimeMillis() + counter.get() * 100) / 500.0) * 0.25f + 0.75f;
                    float limit = Math.max(0.40f, Math.min(1.0f, pulse));
                    float brightness = 1.0f + 0.5f * (limit - 1.0f);
                    rectRed = (this.rectRed.get().floatValue() / 255.0f) * brightness;
                    rectGreen = (this.rectGreen.get().floatValue() / 255.0f) * brightness;
                    rectBlue = (this.rectBlue.get().floatValue() / 255.0f) * brightness;
                } else {
                    rectRed = this.rectRed.get().floatValue() / 255.0f;
                    rectGreen = this.rectGreen.get().floatValue() / 255.0f;
                    rectBlue = this.rectBlue.get().floatValue() / 255.0f;
                }
                rectAlpha = this.rectAlpha.get().floatValue() / 255.0f;
                break;

            case "Rainbow":
                Color rainbowColor = ColorUtil.getRainbow(5.0f, 1.0f, 1.0f);
                rectRed = rainbowColor.getRed() / 255.0f;
                rectGreen = rainbowColor.getGreen() / 255.0f;
                rectBlue = rainbowColor.getBlue() / 255.0f;
                rectAlpha = 1.0f;
                break;

            default:
                int astolfoColor = ColorUtil.AstolfoRGB(0, 2000);
                rectRed = ((astolfoColor >> 16) & 0xFF) / 255.0f;
                rectGreen = ((astolfoColor >> 8) & 0xFF) / 255.0f;
                rectBlue = (astolfoColor & 0xFF) / 255.0f;
                rectAlpha = 1.0f;
                break;
        }

        modules.forEach(module -> {
            int index = modules.indexOf(module);

            String name = module.getName();

            if (wordSpacing.get()) {
                String[] words = name.split("(?=\\p{Upper})");
                if (words.length > 1) {
                    name = String.join(" ", words);
                }
            }

            switch (fontCase.get()) {
                case "Normal":
                    break;

                case "Lower":
                    name = name.toLowerCase(Locale.ROOT);
                    break;

                case "Upper":
                    name = name.toUpperCase(Locale.ROOT);
                    break;
            }

            double textWidth;
            double x;
            double y;

            switch (font.get()) {
                case "Jello":
                    textWidth = fontRenderer.getStringWidth(name);
                    x = scaledResolution.getScaledWidth() - textWidth;
                    y = 2 + index * (fontRenderer.getHeight() + 2) - 1;
                    break;

                case "Minecraft":
                    textWidth = mc.fontRenderer.getStringWidth(name);
                    x = scaledResolution.getScaledWidth() - textWidth;
                    y = (2 + index * (mc.fontRenderer.FONT_HEIGHT + 2)) - 1;
                    break;

                default:
                    return;
            }

            if (background.get()) {
                double extraWidth = 4;
                double backgroundX = x - 7;
                double backgroundHeight = font.compare("Jello") ? fontRenderer.getHeight() + 2 : mc.fontRenderer.FONT_HEIGHT + 2;
                double backgroundWidth = textWidth + extraWidth;

                RenderUtil.rectangle(backgroundX, y, backgroundWidth, backgroundHeight, true, new Color(0, 0, 0, 0.5f));

                if (rect.get()) {
                    x -= extraWidth;
                }
            }

            if (rect.get()) {
                double rectX;
                switch (rectPosition.get()) {
                    case "Left":
                        RenderUtil.rectangle(x - 5, y, 2, font.compare("Jello") ? fontRenderer.getHeight() + 2 : mc.fontRenderer.FONT_HEIGHT + 2, true, new Color(rectRed, rectGreen, rectBlue, rectAlpha));
                        x -= 2;
                        break;

                    case "Right":
                        rectX = x + textWidth - 2;
                        RenderUtil.rectangle(rectX + 2, y, 2, font.compare("Jello") ? fontRenderer.getHeight() + 2 : mc.fontRenderer.FONT_HEIGHT + 2, true, new Color(rectRed, rectGreen, rectBlue, rectAlpha));
                        x -= 3;
                        break;
                }
            }

            if (font.compare("Jello")) {
                if (!rect.get()) {
                    x -= 7;
                }
                fontRenderer.drawString(name, x, y, new Color(textRed, textGreen, textBlue, textAlpha));
            } else if (font.compare("Minecraft")) {
                if (!rect.get()) {
                    x -= 7;
                }
                mc.fontRenderer.drawString(name, x + 2, y + 2, new Color(textRed, textGreen, textBlue, textAlpha).getRGB());
            }
            counter.getAndIncrement();
        });
    }

    @Override
    public void renderDummy() { renderIngame(); }
}
