package vip.radium.gui.csgo.component.impl.sub.checkBox;

import vip.radium.gui.csgo.component.Component;
import vip.radium.gui.csgo.component.PredicateComponent;
import vip.radium.gui.csgo.component.impl.sub.text.TextComponent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class CheckBoxTextComponent extends Component implements PredicateComponent {

    private final CheckBoxComponent checkBox;
    private final TextComponent textComponent;

    private static final int CHECK_BOX_SIZE = 5;
    private static final int TEXT_OFFSET = 3;

    public CheckBoxTextComponent(Component parent, String text,
                                 Supplier<Boolean> isChecked, Consumer<Boolean> onChecked, Supplier<Boolean> isVisible,
                                 float x, float y) {
        super(parent, x, y, 0, 5);
        checkBox = new CheckBoxComponent(this, 0, 0, CHECK_BOX_SIZE, CHECK_BOX_SIZE) {
            @Override
            public boolean isChecked() {
                return isChecked.get();
            }

            @Override
            public void setChecked(boolean checked) {
                onChecked.accept(checked);
            }

            @Override
            public boolean isVisible() {
                return isVisible.get();
            }
        };
        textComponent = new TextComponent(this, text, CHECK_BOX_SIZE + TEXT_OFFSET, 1);
        addChild(checkBox);
        addChild(textComponent);
    }

    public CheckBoxTextComponent(Component parent, String text,
                                 Supplier<Boolean> isChecked, Consumer<Boolean> onChecked, Supplier<Boolean> isVisible) {
        this(parent, text, isChecked, onChecked, isVisible, 0, 0);
    }

    public CheckBoxTextComponent(Component parent, String text,
                                 Supplier<Boolean> isChecked, Consumer<Boolean> onChecked) {
        this(parent, text, isChecked, onChecked, () -> true);
    }

    @Override
    public float getWidth() {
        return CHECK_BOX_SIZE + TEXT_OFFSET + textComponent.getWidth();
    }

    @Override
    public boolean isVisible() {
        return checkBox.isVisible();
    }
}
