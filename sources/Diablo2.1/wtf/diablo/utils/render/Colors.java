package wtf.diablo.utils.render;

import java.awt.*;

public enum Colors {

    RED(new Color(244, 67, 54, 45)),
    PINK(new Color(233, 30, 99)),
    PURPLE(new Color(156, 39, 176)),
    DARK_PURPLE(new Color(103, 58, 183));




    private final Color color;

    Colors(Color var) {
        color =  var;
    }


    public final Color getColor() {
        return color;
    }

}