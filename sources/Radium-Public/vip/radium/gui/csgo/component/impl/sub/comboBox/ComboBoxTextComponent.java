package vip.radium.gui.csgo.component.impl.sub.comboBox;

import vip.radium.gui.csgo.SkeetUI;
import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.ExpandableComponent;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.gui.csgo.component.impl.sub.text.TextComponent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ComboBoxTextComponent extends Component implements PredicateComponent, ExpandableComponent {

    private static final int COMBO_BOX_HEIGHT = 10;
    private static final int COMBO_BOX_Y_OFFSET = 1;
    private final TextComponent textComponent;
    private final ComboBoxComponent comboBoxComponent;

    public ComboBoxTextComponent(Component parent,
                                 String name,
                                 Supplier<Enum<?>[]> getValues,
                                 Consumer<Integer> setValueByIndex,
                                 Supplier<Enum<?>> getValue,
                                 Supplier<List<Enum<?>>> getValueMultiSelect,
                                 Supplier<Boolean> isVisible,
                                 boolean multiSelect,
                                 float x,
                                 float y) {
        super(parent, x, y, SkeetUI.HALF_GROUP_BOX, 5 + COMBO_BOX_Y_OFFSET + COMBO_BOX_HEIGHT);

        comboBoxComponent = new ComboBoxComponent(this, 0, 5 + COMBO_BOX_Y_OFFSET, getWidth(), COMBO_BOX_HEIGHT) {

            @Override
            public boolean isVisible() {
                return isVisible.get();
            }

            @Override
            public Enum<?> getValue() {
                return getValue.get();
            }

            @Override
            public void setValue(int index) {
                setValueByIndex.accept(index);
            }

            @Override
            public List<Enum<?>> getMultiSelectValues() {
                return getValueMultiSelect.get();
            }

            @Override
            public boolean isMultiSelectable() {
                return multiSelect;
            }

            @Override
            public Enum<?>[] getValues() {
                return getValues.get();
            }
        };
        textComponent = new TextComponent(this, name, 1, 0);

        addChild(comboBoxComponent);
        addChild(textComponent);
    }

    public ComboBoxTextComponent(Component parent,
                                 String name,
                                 Supplier<Enum<?>[]> getValues,
                                 Consumer<Integer> setValueByIndex,
                                 Supplier<Enum<?>> getValue,
                                 Supplier<List<Enum<?>>> getValueMultiSelect,
                                 Supplier<Boolean> isVisible,
                                 boolean multiSelect) {
        this(parent,
                name,
                getValues,
                setValueByIndex,
                getValue,
                getValueMultiSelect,
                isVisible,
                multiSelect,
                0,
                0);
    }

    public ComboBoxTextComponent(Component parent,
                                 String name,
                                 Supplier<Enum<?>[]> getValues,
                                 Consumer<Integer> setValueByIndex,
                                 Supplier<Enum<?>> getValue,
                                 Supplier<List<Enum<?>>> getValueMultiSelect,
                                 boolean multiSelect) {
        this(parent,
                name,
                getValues,
                setValueByIndex,
                getValue,
                getValueMultiSelect,
                () -> true,
                multiSelect,
                0,
                0);
    }

    @Override
    public boolean isVisible() {
        return comboBoxComponent.isVisible();
    }

    @Override
    public float getExpandedX() {
        return comboBoxComponent.getExpandedX();
    }

    @Override
    public float getExpandedY() {
        return getY() + textComponent.getHeight();
    }

    @Override
    public float getExpandedWidth() {
        return comboBoxComponent.getExpandedWidth();
    }

    @Override
    public float getExpandedHeight() {
        return comboBoxComponent.getExpandedHeight();
    }

    @Override
    public void setExpanded(boolean expanded) {
        comboBoxComponent.setExpanded(expanded);
    }

    @Override
    public boolean isExpanded() {
        return comboBoxComponent.isExpanded();
    }
}
