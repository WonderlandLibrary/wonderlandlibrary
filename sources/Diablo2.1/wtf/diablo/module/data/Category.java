package wtf.diablo.module.data;

import java.awt.*;

public enum Category {
    /*
    COMBAT("Combat", new Color(184, 55, 55)),
    MOVEMENT("Movement",new Color(63, 117, 187)),
    PLAYER("Player", new Color(83, 208, 84)),
    RENDER("Render", new Color(137, 33, 203)),
    EXPLOIT("Exploit", new Color(234, 203, 62)),
    SETTINGS("Settings", new Color(184, 55, 55));

     */

    COMBAT("Combat", new Color(128,89,178)),
    MOVEMENT("Movement",new Color(89,119,178)),
    PLAYER("Player", new Color(89,164,178)),
    RENDER("Render", new Color(89,94,178)),
    EXPLOIT("Exploit", new Color(154,89,178));

    private String name;
    private Color color;

    Category(String s, Color color) {
        this.name = s;
        this.color = color;
    }

    public String getName(){
        return name;
    }

    public Color getColor(){
        return this.color;
    }
}
