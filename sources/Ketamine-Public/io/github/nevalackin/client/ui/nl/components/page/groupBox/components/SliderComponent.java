package io.github.nevalackin.client.ui.nl.components.page.groupBox.components;

import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.ui.framework.Animated;
import io.github.nevalackin.client.ui.framework.Component;
import io.github.nevalackin.client.ui.framework.Predicated;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.property.DoubleProperty;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class SliderComponent extends Component implements Predicated, Animated {

    // TODO :: SliderComponent

    private final String label;

    private final Supplier<String> displayStringGetter;
    private final Consumer<Double> setter;
    private final Supplier<Double> getter;
    private final Supplier<Double> minGetter;
    private final Supplier<Double> maxGetter;
    private final Supplier<Double> incGetter;

    private final Supplier<Boolean> dependency;

    private double progress;

    private double hoveredFadeInProgress;

    private boolean sliding;

    public SliderComponent(Component parent, String label,
                           Supplier<String> displayStringGetter,
                           Consumer<Double> setter, Supplier<Double> getter, Supplier<Double> minGetter,
                           Supplier<Double> maxGetter, Supplier<Double> incGetter, Supplier<Boolean> dependency,
                           double height) {
        super(parent, 0, 0, 0, height);

        this.label = label;

        if (displayStringGetter == null) {
            displayStringGetter = () -> DoubleProperty.DECIMAL_FORMAT.format(getter.get());
        }

        this.displayStringGetter = displayStringGetter;
        this.setter = setter;
        this.getter = getter;
        this.minGetter = minGetter;
        this.maxGetter = maxGetter;
        this.incGetter = incGetter;

        this.dependency = dependency;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        this.hoveredFadeInProgress = DrawUtil.animateProgress(this.hoveredFadeInProgress, this.isHovered(mouseX, mouseY) ? 1.0 : 0.0, 5);
        final double smoothHovered = DrawUtil.bezierBlendAnimation(this.hoveredFadeInProgress);

        final CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();

        // Draw label
        {
            fontRenderer.draw(this.label, x, y + height / 2.0 - fontRenderer.getHeight(this.label) / 2.0, 0.23,
                              ColourUtil.fadeBetween(this.getTheme().getTextColour(), this.getTheme().getHighlightedTextColour(), smoothHovered));
        }

        // Draw slider
        {
            final double min = this.minGetter.get();
            final double max = this.maxGetter.get();
            final double range = max - min;

            final String sValue = this.displayStringGetter.get();
            final double sValueWidth = fontRenderer.getWidth(sValue, 0.2, 700);

            final double calculatedSliderWidth = Math.min(width - (sValueWidth + 4 + 2 + 2) - (fontRenderer.getWidth(this.label, 0.23) + 4 + 2), width / 2.5);

            final double sliderThickness = 1;

            final double sliderPos = y + height / 2.0 - sliderThickness / 2.0;

            final double right = x + width - 4;

            if (this.sliding) {
                final double left = right - calculatedSliderWidth;
                final double leftOffset = mouseX - left;
                final double sliderPercentage = Math.min(1.0, Math.max(0.0, leftOffset / calculatedSliderWidth));

                this.setter.accept(min + range * sliderPercentage);
            }

            final double value = this.getter.get();
            final double percentage = (value - min) / range;

            if (this.shouldPlayAnimation()) {
                this.progress = DrawUtil.animateProgress(this.progress, percentage, 5);
            }

            final int mainColour = this.getTheme().getMainColour();

            DrawUtil.glDrawFilledQuad(right - calculatedSliderWidth, sliderPos, calculatedSliderWidth, sliderThickness, this.getTheme().getSliderBackgroundColour());
            DrawUtil.glDrawFilledQuad(right - calculatedSliderWidth, sliderPos, calculatedSliderWidth * this.progress, sliderThickness, mainColour);

            DrawUtil.glDrawPoint(right - calculatedSliderWidth * (1.0 - this.progress), sliderPos, 8.f, scaledResolution, mainColour);

            fontRenderer.draw(sValue, right - calculatedSliderWidth - sValueWidth - 4 - 2,
                              y + height / 2.0 - fontRenderer.getHeight(sValue, 700) / 2.0,
                              0.2, 700,
                              this.getTheme().getTextColour());
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (!this.sliding) {
            final double x = this.getX();
            final double y = this.getY();
            final double width = this.getWidth();
            final double height = this.getHeight();

            final CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();
            final String sValue = this.displayStringGetter.get();
            final double sValueWidth = fontRenderer.getWidth(sValue, 0.2, 700);
            final double calculatedSliderWidth = Math.min(width - (sValueWidth + 4 + 2 + 2) - (fontRenderer.getWidth(this.label, 0.23) + 4 + 2), width / 2.5);
            final double right = x + width - 4;

            if (mouseX > right - calculatedSliderWidth && mouseY > y && mouseX < right && mouseY < y + height) {
                this.sliding = true;
            }
        }
    }

    @Override
    public void onMouseRelease(int button) {
        if (this.sliding && button == 0) this.sliding = false;
    }

    @Override
    public void onKeyPress(int keyCode) {

    }

    @Override
    public boolean isVisible() {
        return this.dependency != null ? this.dependency.get() : true;
    }

    @Override
    public void resetAnimationState() {
        this.progress = 0.0;
        this.hoveredFadeInProgress = 0.0;
        this.sliding = false;
    }
}
