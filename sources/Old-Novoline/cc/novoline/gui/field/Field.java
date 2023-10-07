package cc.novoline.gui.field;

import cc.novoline.gui.Element;
import cc.novoline.gui.label.Label;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author xDelsy
 */
public interface Field extends Element {

    void setHint(@Nullable Label label);

    @Nullable Label getLabel();

}
