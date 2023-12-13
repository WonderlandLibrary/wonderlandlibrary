package io.github.nevalackin.client.module.render.overlay;

import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.event.render.overlay.RenderGameOverlayEvent;
import io.github.nevalackin.client.module.Category;
import io.github.nevalackin.client.module.Module;
import io.github.nevalackin.client.property.BooleanProperty;
import io.github.nevalackin.client.property.ColourProperty;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.property.EnumProperty;
import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.util.render.BlurUtil;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import io.github.nevalackin.homoBus.Listener;
import io.github.nevalackin.homoBus.annotations.EventLink;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

import java.util.List;
import java.util.stream.Collectors;

public final class Arraylist extends Module {

    private final EnumProperty<FontMode> fontModeProperty = new EnumProperty<>("Font", FontMode.ARIAL);
    private final EnumProperty<Position> positionProperty = new EnumProperty<>("Position", Position.TOP);
    private final BooleanProperty lineProperty = new BooleanProperty("Line", false);
    private final BooleanProperty outlineProperty = new BooleanProperty("Outline", true);
    private final BooleanProperty backgroundProperty = new BooleanProperty("Background", true);
    private final DoubleProperty backgroundOpacityProperty = new DoubleProperty("Background Opacity", 50, this.backgroundProperty::getValue, 0, 100, 1);
    private final DoubleProperty moduleSpacingProperty = new DoubleProperty("Module Spacing", 0, 0, 5, 0.5);

    private final EnumProperty<Colour> colourProperty = new EnumProperty<>("Colour", Colour.BLEND);
    private final ColourProperty startColourProperty = new ColourProperty("Start Colour", 0xFFEA8F91, this::isBlendColours);
    private final ColourProperty endColourProperty = new ColourProperty("End Colour", 0xFF39BAF1, this::isBlendColours);
    @EventLink
    private final Listener<RenderGameOverlayEvent> onRenderOverlay = event -> {
        final ScaledResolution scaledResolution = event.getScaledResolution();
        final CustomFontRenderer fontRenderer = this.getFontRenderer();

        final int screenRight = scaledResolution.getScaledWidth();
        final int bottom = scaledResolution.getScaledHeight();

        final int startColour = this.colourProperty.getValue().getModColour(this.startColourProperty.getValue(), this.endColourProperty.getValue(), 0);

        final List<Module> enabledModules = KetamineClient.getInstance().getModuleManager().getModules().stream()
                .filter(Module::isDisplayed)
                .sorted((m1, m2) -> Double.compare(fontRenderer.getWidth(m2.getDisplayName()), fontRenderer.getWidth(m1.getDisplayName())))
                .collect(Collectors.toList());

        double lastModuleWidth = 0.0;
        int lastModuleColour = startColour;

        for (int i = 0, size = enabledModules.size(); i < size; i++) {
            final Module module = enabledModules.get(i);

            final String moduleName = module.getDisplayName();

            final double nameHeight = fontRenderer.getHeight(moduleName);
            final double nameWidth = fontRenderer.getWidth(moduleName);
            final double moduleSpacing = nameHeight + this.moduleSpacingProperty.getValue();

            final double lineThickness = this.lineProperty.getValue() ? 1 : 0;
            final double outlineThickness = this.outlineProperty.getValue() ? 1 : 0;

            final int xBuffer = 2;

            final int colour = this.colourProperty.getValue().getModColour(this.startColourProperty.getValue(),
                    this.endColourProperty.getValue(), i);

            final boolean top = this.positionProperty.getValue() == Position.TOP;

            final double y = top ? i * moduleSpacing : bottom - (i + 1) * moduleSpacing;

            if (this.backgroundProperty.getValue()) {
                final double backgroundLeft = screenRight - nameWidth - xBuffer * 2 - lineThickness;
                final double backgroundWidth = nameWidth + xBuffer * 2 + lineThickness;

                BlurUtil.blurArea(backgroundLeft, y, backgroundLeft, moduleSpacing);

                DrawUtil.glDrawFilledQuad(backgroundLeft, y, backgroundWidth, moduleSpacing,
                        (int) (0xFF * (this.backgroundOpacityProperty.getValue() / 100.0)) << 24);
            }

            if (lineThickness != 0) {
                if (top) {
                    DrawUtil.glDrawFilledQuad(screenRight - lineThickness, y,
                            lineThickness, moduleSpacing,
                            lastModuleColour, colour);
                } else {
                    DrawUtil.glDrawFilledQuad(screenRight - lineThickness, y,
                            lineThickness, moduleSpacing,
                            colour, lastModuleColour);
                }
            }

            if (outlineThickness != 0) {
                // Left line
                if (top) {
                    DrawUtil.glDrawFilledQuad(screenRight - nameWidth - xBuffer * 2 - outlineThickness - lineThickness, y,
                            outlineThickness, moduleSpacing,
                            lastModuleColour, colour);
                } else {
                    DrawUtil.glDrawFilledQuad(screenRight - nameWidth - xBuffer * 2 - outlineThickness - lineThickness, y,
                            outlineThickness, moduleSpacing,
                            colour, lastModuleColour);
                }

                if (lastModuleWidth != 0) {
                    if (lastModuleWidth - nameWidth > 0) {
                        // Top line
                        DrawUtil.glDrawFilledQuad(screenRight - lastModuleWidth - xBuffer * 2 - lineThickness - outlineThickness,
                                top ? i * moduleSpacing : bottom - i * moduleSpacing,
                                lastModuleWidth - nameWidth + outlineThickness, outlineThickness,
                                lastModuleColour);
                    }
                }

                if (i == size - 1) {
                    DrawUtil.glDrawFilledQuad(screenRight - nameWidth - xBuffer * 2 - lineThickness - outlineThickness, top ? y + moduleSpacing : y,
                            nameWidth + lineThickness + outlineThickness + xBuffer * 2, outlineThickness,
                            colour);
                }
            }

            fontRenderer.draw(moduleName,
                    screenRight - nameWidth - xBuffer - lineThickness + .5,
                    y + this.fontModeProperty.getValue().textOffset + this.moduleSpacingProperty.getValue() / 2.0,
                    colour);

            lastModuleColour = colour;
            lastModuleWidth = nameWidth;
        }
    };

    public Arraylist() {
        super("Array List", Category.RENDER, Category.SubCategory.RENDER_OVERLAY);

        this.setHidden(true);

        this.register(this.fontModeProperty, this.positionProperty,
                this.lineProperty, this.outlineProperty,
                this.moduleSpacingProperty,
                this.backgroundProperty, this.backgroundOpacityProperty,
                this.colourProperty,
                this.startColourProperty, this.endColourProperty);
    }

    private boolean isBlendColours() {
        return this.colourProperty.getValue() == Colour.BLEND;
    }

    private CustomFontRenderer getFontRenderer() {
        return this.fontModeProperty.getValue().getRenderer();
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {

    }

    private enum Colour {
        CZECIA("Czechia", ((startColour, endColour, index) -> {
            return ColourUtil.blendCzechiaColours(index * 100L);
        })),
        GERMAN("Lennox", ((startColour, endColour, index) -> {
            return ColourUtil.blendGermanColours(index * 200L);
        })),
        RAINBOW("Rainbow", ((startColour, endColour, index) -> {
            return ColourUtil.blendRainbowColours(index * 150L);
        })),
        RAINBOW_SPECIAL("Fading Rainbow", ((startColour, endColour, index) -> {
            final int colour = ColourUtil.blendSpecialRainbow(index * 50L);
            return ColourUtil.fadeBetween(colour,
                    ColourUtil.darker(colour, 0.2f),
                    index * 200L);
        })),
        BLEND("Blend", ((startColour, endColour, index) -> {
            return ColourUtil.fadeBetween(startColour, endColour, index * 300L);
        }));

        private final String name;
        private final ModColourFunc modColourFunc;

        Colour(String name, ModColourFunc modColourFunc) {
            this.name = name;
            this.modColourFunc = modColourFunc;
        }

        public int getModColour(final int startColour,
                                final int endColour,
                                final int index) {
            return modColourFunc.getColour(startColour, endColour, index);
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum Position {
        TOP("Top"),
        BOTTOM("Bottom");

        private final String name;

        Position(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private enum FontMode {
        ARIAL("Arial", KetamineClient.getInstance().getFontRenderer(), 0);

        private final String name;
        private final CustomFontRenderer renderer;
        private final double textOffset;

        FontMode(String name, CustomFontRenderer renderer, double textOffset) {
            this.name = name;
            this.renderer = renderer;
            this.textOffset = textOffset;
        }

        public CustomFontRenderer getRenderer() {
            return renderer;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    @FunctionalInterface
    private interface ModColourFunc {
        int getColour(final int startColour, final int endColour, final int index);
    }
}