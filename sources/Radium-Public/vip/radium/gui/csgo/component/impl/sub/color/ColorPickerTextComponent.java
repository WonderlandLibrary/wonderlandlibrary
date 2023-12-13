package vip.radium.gui.csgo.component.impl.sub.color;

import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.ExpandableComponent;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.gui.csgo.component.impl.sub.text.TextComponent;
import vip.radium.property.ValueChangeListener;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ColorPickerTextComponent extends Component implements PredicateComponent, ExpandableComponent {

    private static final int COLOR_PICKER_HEIGHT = 5;
    private static final int COLOR_PICKER_WIDTH = 11;
    private static final int TEXT_MARGIN = 1;
    private final ColorPickerComponent colorPicker;
    private final TextComponent textComponent;

    public ColorPickerTextComponent(Component parent, String text,
                                    Supplier<Integer> getColor, Consumer<Integer> setColor,
                                    Consumer<ValueChangeListener<Integer>> addValueChangeListener,
                                    Supplier<Boolean> isVisible,
                                    float x, float y) {
        super(parent, x, y, 0, COLOR_PICKER_HEIGHT);

        textComponent = new TextComponent(this, text, TEXT_MARGIN, TEXT_MARGIN);

        colorPicker = new ColorPickerComponent(this, SkeetUI.HALF_GROUP_BOX - COLOR_PICKER_WIDTH, 0, COLOR_PICKER_WIDTH, COLOR_PICKER_HEIGHT) {
            @Override
            public int getColor() {
                return getColor.get();
            }

            @Override
            public void setColor(int color) {
                setColor.accept(color);
            }

            @Override
            public void addValueChangeListener(ValueChangeListener<Integer> onValueChange) {
                addValueChangeListener.accept(onValueChange);
            }

            @Override
            public boolean isVisible() {
                return isVisible.get();
            }
        };

        addChild(colorPicker);
        addChild(textComponent);
    }

    public ColorPickerTextComponent(Component parent, String text,
                                    Supplier<Integer> getColor, Consumer<Integer> setColor, Consumer<ValueChangeListener<Integer>> addValueChangeListener, Supplier<Boolean> isVisible) {
        this(parent, text, getColor, setColor, addValueChangeListener, isVisible, 0, 0);
    }

    public ColorPickerTextComponent(Component parent, String text,
                                    Supplier<Integer> getColor, Consumer<Integer> setColor, Consumer<ValueChangeListener<Integer>> addValueChangeListener) {
        this(parent, text, getColor, setColor, addValueChangeListener, () -> true);
    }

    @Override
    public float getWidth() {
        return TEXT_MARGIN * 2 + COLOR_PICKER_WIDTH + textComponent.getWidth();
    }

    @Override
    public boolean isVisible() {
        return colorPicker.isVisible();
    }

    @Override
    public float getExpandedX() {
        return colorPicker.getExpandedX();
    }

    @Override
    public float getExpandedY() {
        return colorPicker.getY() + colorPicker.getHeight();
    }

    @Override
    public float getExpandedWidth() {
        return colorPicker.getExpandedWidth();
    }

    @Override
    public float getExpandedHeight() {
        return colorPicker.getExpandedHeight();
    }

    @Override
    public void setExpanded(boolean expanded) {
        colorPicker.setExpanded(expanded);
    }

    @Override
    public boolean isExpanded() {
        return colorPicker.isExpanded();
    }
}
