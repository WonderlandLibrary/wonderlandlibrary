package cc.novoline.gui.button;

import cc.novoline.gui.label.Label;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Consumer;

/**
 * @author xDelsy
 */
public final class FunctionalButton extends AbstractButton {

    /* fields */
    @Nullable
    private final Consumer<Integer> click;

    /* constructors */
    public FunctionalButton(@Nullable Label name, int x, int y, int width, int height,
                            @Nullable Consumer<Integer> click) {
        super(name, x, y, width, height);
        this.click = click;
    }

    public FunctionalButton(@Nullable Label name, int x, int y, @Nullable Consumer<Integer> click) {
        super(name, x, y);
        this.click = click;
    }

    public FunctionalButton(@Nullable Label name, int x, int y, int width, int height) {
        this(name, x, y, width, height, null);
    }

    public FunctionalButton(@Nullable Label name, int x, int y) {
        this(name, x, y, null);
    }

    /* methods */
    @Override
    public void click(int mouseKey) {
        if (this.click != null) this.click.accept(mouseKey);
    }

}
