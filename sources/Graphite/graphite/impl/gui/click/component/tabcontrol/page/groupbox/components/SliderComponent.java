package graphite.impl.gui.click.component.tabcontrol.page.groupbox.components;

import graphite.api.property.impl.DoubleProperty;
import graphite.api.property.impl.Representation;
import graphite.api.util.render.ColourUtil;
import graphite.api.util.render.RenderUtil;
import graphite.api.util.render.font.TTFFontRenderer;
import graphite.impl.Graphite;
import graphite.impl.gui.click.component.Theme;
import graphite.impl.gui.click.framework.Component;
import graphite.impl.gui.click.framework.Predicated;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class SliderComponent extends Component implements Predicated {
    private final String label;

    private final Supplier<String> displayStringGetter;
    private final Consumer<Double> setter;
    private final Supplier<Double> getter;
    private final Supplier<Double> minGetter;
    private final Supplier<Double> maxGetter;

    private final Supplier<Boolean> dependency;
    private final Supplier<Representation> representationGetter;

    private boolean sliding;

    public SliderComponent(Component parent, String label,
                           Supplier<String> displayStringGetter,
                           Consumer<Double> setter, Supplier<Double> getter, Supplier<Double> minGetter,
                           Supplier<Double> maxGetter, Supplier<Boolean> dependency, Supplier<Representation> representationGetter) {
        super(parent, 0, 0, 0, 8.5);

        this.label = label;

        if (displayStringGetter == null) {
            displayStringGetter = () -> DoubleProperty.DECIMAL_FORMAT.format(getter.get());
        }

        this.displayStringGetter = displayStringGetter;
        this.setter = setter;
        this.getter = getter;
        this.minGetter = minGetter;
        this.maxGetter = maxGetter;

        this.dependency = dependency;
        this.representationGetter = representationGetter;
    }

    @Override
    public void onDraw(ScaledResolution scaledResolution, int mouseX, int mouseY) {
        final double x = this.getX();
        final double y = this.getY();
        final double width = this.getWidth();
        final double height = this.getHeight();

        final TTFFontRenderer fontRenderer = Graphite.INSTANCE.getFontManager().getFontRenderer("VERDANA_9");
        final TTFFontRenderer valueFontRenderer = Graphite.INSTANCE.getFontManager().getFontRenderer("VERDANA_BOLD_9");

        {
            final double min = this.minGetter.get();
            final double max = this.maxGetter.get();
            final double range = max - min;

            String sValue = this.displayStringGetter.get();
            final float sValueWidth = valueFontRenderer.getWidth(sValue);

            switch (representationGetter.get()) {
                case PERCENTAGE:
                    sValue += '%';
                    break;
                case MILLISECONDS:
                    sValue += "ms";
                    break;
                case DISTANCE:
                    sValue += 'm';
            }

            if (this.sliding) {
                final double left = x - .5;
                final double leftOffset = mouseX - left;
                final double sliderPercentage = Math.min(1.0, Math.max(0.0, leftOffset / (x + width - x - .5)));

                this.setter.accept(min + range * sliderPercentage);
            }

            final double value = this.getter.get();
            final double percentage = (value - min) / range;

            RenderUtil.drawRect(x, y + 5, x + width, y + height, new Color(18,18,24).getRGB());

            RenderUtil.drawGradientRect(x + .5, y + 5.5, x + width - .5, y + height - .5, false, new Color(38,38,48).getRGB(), new Color(53,53,60).getRGB());
            RenderUtil.drawGradientRect(x + .5, y + 5.5, x + width * percentage - .5, y + height - .5, false, Theme.getUIColor(), ColourUtil.darker(Theme.getUIColor(), 0.65));

            valueFontRenderer.drawStringWithOutline(sValue, (float) ((float) x + width * percentage - (sValueWidth / 1.8F)), (float) ((float) y + height), new Color(205, 205, 205).getRGB(), new Color(0, 0, 0, 130).getRGB());
        }

        {
            fontRenderer.drawStringWithShadow(this.label, (float) x, (float) y + .5F, new Color(205, 205, 205).getRGB());
        }


    }

    @Override
    public void onMouseClick(int mouseX, int mouseY, int button) {
        if (button == 0 && !this.sliding) {
            final double x = this.getX();
            final double y = this.getY();
            final double width = this.getWidth();
            final double height = this.getHeight();

            if (mouseX > x + .5 && mouseY > y + 5.5 && mouseX < x + width - .5 && mouseY < y + height - .5) {
                this.sliding = true;
            }
        }
    }

    @Override
    public void onMouseRelease(int button) {
        if (this.sliding && button == 0) this.sliding = false;
    }

    @Override
    public boolean isVisible() {
        return this.dependency != null ? this.dependency.get() : true;
    }
}