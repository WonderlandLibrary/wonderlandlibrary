// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.ui.click.skeet.component.impl.sub.color;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;
import vip.Resolute.ui.click.skeet.component.impl.sub.text.TextComponent;
import vip.Resolute.ui.click.skeet.framework.ExpandableComponent;
import vip.Resolute.ui.click.skeet.framework.PredicateComponent;
import vip.Resolute.ui.click.skeet.framework.Component;

public class ColorPickerTextComponent extends Component implements PredicateComponent, ExpandableComponent
{
    private static final int COLOR_PICKER_HEIGHT = 5;
    private static final int COLOR_PICKER_WIDTH = 11;
    private static final int TEXT_MARGIN = 1;
    private final ColorPickerComponent colorPicker;
    private final TextComponent textComponent;
    
    public ColorPickerTextComponent(final Component parent, final String text, final Supplier<Integer> getColor, final Consumer<Color> setColor, final Supplier<Boolean> isVisible, final float x, final float y) {
        super(parent, x, y, 0.0f, 5.0f);
        this.textComponent = new TextComponent(this, text, 1.0f, 1.0f);
        this.addChild(this.colorPicker = new ColorPickerComponent(this, 29.166668f, 0.0f, 11.0f, 5.0f) {
            @Override
            public int getColor() {
                return getColor.get();
            }
            
            @Override
            public void setColor(final Color color) {
                setColor.accept(color);
            }
            
            @Override
            public boolean isVisible() {
                return isVisible.get();
            }
        });
        this.addChild(this.textComponent);
    }
    
    public ColorPickerTextComponent(final Component parent, final String text, final Supplier<Integer> getColor, final Consumer<Color> setColor, final Supplier<Boolean> isVisible) {
        this(parent, text, getColor, setColor, isVisible, 0.0f, 0.0f);
    }
    
    public ColorPickerTextComponent(final Component parent, final String text, final Supplier<Integer> getColor, final Consumer<Color> setColor) {
        this(parent, text, getColor, setColor, () -> true);
    }
    
    @Override
    public float getWidth() {
        return 13.0f + this.textComponent.getWidth();
    }
    
    @Override
    public boolean isVisible() {
        return this.colorPicker.isVisible();
    }
    
    @Override
    public float getExpandedX() {
        return this.colorPicker.getExpandedX();
    }
    
    @Override
    public float getExpandedY() {
        return this.colorPicker.getY() + this.colorPicker.getHeight();
    }
    
    @Override
    public float getExpandedWidth() {
        return this.colorPicker.getExpandedWidth();
    }
    
    @Override
    public float getExpandedHeight() {
        return this.colorPicker.getExpandedHeight();
    }
    
    @Override
    public void setExpanded(final boolean expanded) {
        this.colorPicker.setExpanded(expanded);
    }
    
    @Override
    public boolean isExpanded() {
        return this.colorPicker.isExpanded();
    }
}
