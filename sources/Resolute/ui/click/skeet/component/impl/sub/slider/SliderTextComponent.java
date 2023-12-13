// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component.impl.sub.slider;

import vip.Resolute.ui.click.skeet.component.impl.sub.text.TextComponent;
import java.util.function.Consumer;
import java.util.function.Supplier;
import vip.Resolute.ui.click.skeet.framework.PredicateComponent;
import vip.Resolute.ui.click.skeet.framework.Component;

public final class SliderTextComponent extends Component implements PredicateComponent
{
    private static final float SLIDER_THICKNESS = 4.0f;
    private static final int SLIDER_Y_OFFSET = 1;
    private final SliderComponent sliderComponent;
    
    public SliderTextComponent(final Component parent, final String text, final Supplier<Double> getValue, final Consumer<Double> setValue, final Supplier<Double> getMin, final Supplier<Double> getMax, final Supplier<Double> getIncrement, final Supplier<Boolean> isVisible, final float x, final float y) {
        super(parent, x, y, 40.166668f, 4.0f);
        this.addChild(this.sliderComponent = new SliderComponent(this, 0.0f, 6.0f, this.getWidth(), 4.0f) {
            @Override
            public double getValue() {
                return getValue.get();
            }
            
            @Override
            public void setValue(final double value) {
                setValue.accept(value);
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
        });
        this.addChild(new TextComponent(this, text, 1.0f, 0.0f));
    }
    
    public SliderTextComponent(final Component parent, final String text, final Supplier<Double> getValue, final Consumer<Double> setValue, final Supplier<Double> getMin, final Supplier<Double> getMax, final Supplier<Double> getIncrement, final Supplier<Boolean> isVisible) {
        this(parent, text, getValue, setValue, getMin, getMax, getIncrement, isVisible, 0.0f, 0.0f);
    }
    
    public SliderTextComponent(final Component parent, final String text, final Supplier<Double> getValue, final Consumer<Double> setValue, final Supplier<Double> getMin, final Supplier<Double> getMax, final Supplier<Double> getIncrement) {
        this(parent, text, getValue, setValue, getMin, getMax, getIncrement, () -> true);
    }
    
    @Override
    public float getHeight() {
        return 6.0f + super.getHeight();
    }
    
    @Override
    public boolean isVisible() {
        return this.sliderComponent.isVisible();
    }
}
