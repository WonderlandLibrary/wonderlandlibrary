package io.github.nevalackin.client.ui.nl.components.page.groupBox.components;

import io.github.nevalackin.client.ui.cfont.CustomFontRenderer;
import io.github.nevalackin.client.ui.framework.Animated;
import io.github.nevalackin.client.ui.framework.Component;
import io.github.nevalackin.client.ui.framework.Predicated;
import io.github.nevalackin.client.core.KetamineClient;
import io.github.nevalackin.client.util.render.ColourUtil;
import io.github.nevalackin.client.util.render.DrawUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CheckBoxComponent extends Component implements Predicated, Animated {

    private final String label;

    private final Consumer<Boolean> setter;
    private final Supplier<Boolean> getter;
    private final Supplier<Boolean> dependency;

    private double progress;

    public CheckBoxComponent(Component parent, String label, Supplier<Boolean> getter, Consumer<Boolean> setter, Supplier<Boolean> dependency, double height) {
        super(parent, 0, 0, 0, height);

        this.label = label;

        this.getter = getter;
        this.setter = setter;
        this.dependency = dependency;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        if (this.shouldPlayAnimation())
            this.progress = DrawUtil.animateProgress(this.progress, this.getter.get() ? 1.0 : 0.0, 5);

        final double smooth = DrawUtil.bezierBlendAnimation(this.progress);

        // Draw label
        if (this.label != null) {
            final CustomFontRenderer fontRenderer = KetamineClient.getInstance().getFontRenderer();

            fontRenderer.draw(this.label, x, y + height / 2.0 - fontRenderer.getHeight(this.label) / 2.0, 0.22,
                              ColourUtil.fadeBetween(this.getTheme().getTextColour(), this.getTheme().getHighlightedTextColour(), smooth));
        }

        // Draw check slider
        {
            final int backgroundColour = ColourUtil.fadeBetween(this.getTheme().getCheckBoxBackgroundDisabledColour(),
                                                                this.getTheme().getCheckBoxBackgroundEnabledColour(), smooth);
            final int knobColour = ColourUtil.fadeBetween(this.getTheme().getCheckBoxDisabledColour(), this.getTheme().getCheckBoxEnabledColour(), smooth);

            final float smallRadius = 5.f;
            final float largeRadius = 8.f;

            final double right = x + width - largeRadius;
            final double left = right - 10.f;

            final double middle = y + height / 2.0;

            DrawUtil.glDrawPoint(right, middle, smallRadius, scaledResolution, backgroundColour);
            DrawUtil.glDrawFilledQuad(left, middle - smallRadius / 2.0, right - left, smallRadius, backgroundColour);
            DrawUtil.glDrawPoint(left, middle, smallRadius, scaledResolution, backgroundColour);

            DrawUtil.glDrawPoint(DrawUtil.interpolate(left, right, smooth),
                                 y + height / 2.0, largeRadius, scaledResolution, knobColour);
        }
    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0) this.setter.accept(!this.getter.get());
    }

    @Override
    public void onMouseRelease(int button) {

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
    }
}
