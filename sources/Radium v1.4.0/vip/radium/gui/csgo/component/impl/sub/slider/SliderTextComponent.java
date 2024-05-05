package vip.radium.gui.csgo.component.impl.sub.slider;

import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.gui.csgo.component.impl.sub.text.TextComponent;
import vip.radium.property.impl.Representation;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class SliderTextComponent extends Component implements PredicateComponent {

    private static final float SLIDER_THICKNESS = 4F;
    private static final int SLIDER_Y_OFFSET = 1;
    private final SliderComponent sliderComponent;

    public SliderTextComponent(Component parent, String text,
                               Supplier<Double> getValue,
                               Consumer<Double> setValue,
                               Supplier<Double> getMin,
                               Supplier<Double> getMax,
                               Supplier<Double> getIncrement,
                               Supplier<Representation> getRepresentation,
                               Supplier<Boolean> isVisible,
                               float x, float y) {
        super(parent, x, y, SkeetUI.HALF_GROUP_BOX, SLIDER_THICKNESS);
        sliderComponent = new SliderComponent(this, 0, 5 + SLIDER_Y_OFFSET, getWidth(), SLIDER_THICKNESS) {
            @Override
            public double getValue() {
                return getValue.get();
            }

            @Override
            public void setValue(double value) {
                setValue.accept(value);
            }

            @Override
            public Representation getRepresentation() {
                return getRepresentation.get();
            }

            @Override
            public double getMin() {
                return getMin.get();
            }

            @Override
            public double getMax() {
                return getMax.get();
            }

            @Override
            public double getIncrement() {
                return getIncrement.get();
            }

            @Override
            public boolean isVisible() {
                return isVisible.get();
            }
        };
        addChild(sliderComponent);
        addChild(new TextComponent(this, text, 1, 0));
    }

    public SliderTextComponent(Component parent, String text,
                               Supplier<Double> getValue,
                               Consumer<Double> setValue,
                               Supplier<Double> getMin,
                               Supplier<Double> getMax,
                               Supplier<Double> getIncrement,
                               Supplier<Representation> getRepresentation,
                               Supplier<Boolean> isVisible) {
        this(parent, text, getValue, setValue, getMin, getMax, getIncrement, getRepresentation, isVisible, 0, 0);
    }

    public SliderTextComponent(Component parent, String text,
                               Supplier<Double> getValue,
                               Consumer<Double> setValue,
                               Supplier<Double> getMin,
                               Supplier<Double> getMax,
                               Supplier<Double> getIncrement,
                               Supplier<Representation> getRepresentation) {
        this(parent, text, getValue, setValue, getMin, getMax, getIncrement, getRepresentation, () -> true);
    }

    @Override
    public float getHeight() {
        return 5 + SLIDER_Y_OFFSET + super.getHeight();
    }

    @Override
    public boolean isVisible() {
        return sliderComponent.isVisible();
    }
}
