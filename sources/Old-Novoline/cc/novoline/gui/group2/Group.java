package cc.novoline.gui.group2;

import cc.novoline.gui.Element;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.awt.*;

/**
 * @author xDelsy
 */
public interface Group extends Element {

    int getColor();

    void setColor(int color);

    default void setColor(@NonNull Color color) {
        setColor(color.getRGB());
    }

}
