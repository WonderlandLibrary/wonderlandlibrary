package cc.novoline.gui.button;

import cc.novoline.gui.Element;
import cc.novoline.gui.label.Label;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author xDelsy
 */
public interface Button extends Element {

    void click(int mouseKey);

    @Nullable Label getName();

    void setName(@Nullable Label label);

}
