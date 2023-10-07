package cc.novoline.gui.group2;

import cc.novoline.gui.label.Label;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author xDelsy
 */
public interface GroupWithTitle extends Group {

    @Nullable Label getTitle();

    void setTitle(@Nullable Label title);

}
