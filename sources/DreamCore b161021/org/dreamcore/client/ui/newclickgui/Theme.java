package org.dreamcore.client.ui.newclickgui;

import java.awt.*;

public class Theme {

    public boolean dark = true;
    public Color bgColor;
    public Color elementColor;
    public Color opposite;
    public Color panelColor;
    public Color guiColor1;
    public Color guiColor2;
    public Color guiColor3;
    public Color guiColor4;
    public Color textColor;

    public Theme() {
        int difference = 16;
        if (dark) {
            bgColor = new Color(14, 14, 14, 255);
            elementColor = new Color(0, 0, 0, 255);
            opposite = new Color(255, 255, 255, 255);
            panelColor = new Color(42, 42, 42, 255);
            guiColor1 = new Color(0, 0, 0, 255);
            guiColor2 = new Color(difference, difference, difference, 255);
            guiColor3 = new Color(difference * 2, difference * 2, difference * 2, 255);
            guiColor4 = new Color(26, 26, 26, 255);
            textColor = new Color(241, 241, 241, 255);
        } else {
            bgColor = new Color(241, 241, 241, 255);
            elementColor = new Color(255, 255, 255, 255);
            opposite = new Color(0, 0, 0, 255);
            panelColor = new Color(213, 213, 213, 255);
            guiColor1 = new Color(255, 255, 255, 255);
            guiColor2 = new Color(255 - difference, 255 - difference, 255 - difference, 255);
            guiColor3 = new Color(255 - difference * 2, 255 - difference * 2, 255 - difference * 2, 255);
            guiColor4 = new Color(229, 229, 229, 255);
            textColor = new Color(14, 14, 14, 255);
        }
    }

}
