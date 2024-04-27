/*
 * Decompiled with CFR 0.152.
 */
package cc.paimon.skid.lbp.newVer;

import java.awt.Color;

public final class ColorManager {
    private static final Color unusedSlider;
    private static final Color buttonOutline;
    private static final Color moduleBackground;
    private static final Color sliderButton;
    private static final Color background;
    private static final Color border;
    private static final Color dropDown;
    private static final Color textBox;
    public static final ColorManager INSTANCE;
    private static final Color button;

    public final Color getTextBox() {
        return textBox;
    }

    static {
        ColorManager colorManager;
        INSTANCE = colorManager = new ColorManager();
        background = new Color(241, 243, 247);
        textBox = new Color(170, 170, 170);
        dropDown = new Color(238, 238, 238);
        button = new Color(238, 238, 238);
        moduleBackground = new Color(251, 251, 251);
        unusedSlider = new Color(154, 154, 154);
        sliderButton = new Color(69, 69, 69);
        border = new Color(241, 243, 247);
        buttonOutline = new Color(241, 243, 247);
    }

    public final Color getModuleBackground() {
        return moduleBackground;
    }

    public final Color getSliderButton() {
        return sliderButton;
    }

    public final Color getButtonOutline() {
        return buttonOutline;
    }

    public final Color getButton() {
        return button;
    }

    private ColorManager() {
    }

    public final Color getBorder() {
        return border;
    }

    public final Color getDropDown() {
        return dropDown;
    }

    public final Color getBackground() {
        return background;
    }

    public final Color getUnusedSlider() {
        return unusedSlider;
    }
}

