/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.newVer;

import java.awt.Color;
import kotlin.Metadata;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b'\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0011\u0010\t\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0011\u0010\u000b\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R\u0011\u0010\r\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006R\u0011\u0010\u000f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u0011\u0010\u0011\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0006R\u0011\u0010\u0013\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0006R\u0011\u0010\u0015\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0006R\u0011\u0010\u0017\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0006R\u0011\u0010\u0019\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0006R\u0011\u0010\u001b\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0006R\u0011\u0010\u001d\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0006R\u0011\u0010\u001f\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010\u0006R\u0011\u0010!\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0006R\u0011\u0010#\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0006R\u0011\u0010%\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0006R\u0011\u0010'\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b(\u0010\u0006R\u0011\u0010)\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b*\u0010\u0006\u00a8\u0006+"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/ColorManager;", "", "()V", "ColorUiBackground", "Ljava/awt/Color;", "getColorUiBackground", "()Ljava/awt/Color;", "background", "getBackground", "background1", "getBackground1", "border", "getBorder", "border1", "getBorder1", "button", "getButton", "button1", "getButton1", "buttonOutline", "getButtonOutline", "buttonOutline1", "getButtonOutline1", "dropDown", "getDropDown", "dropDown1", "getDropDown1", "moduleBackground", "getModuleBackground", "moduleBackground1", "getModuleBackground1", "sliderButton", "getSliderButton", "sliderButton1", "getSliderButton1", "textBox", "getTextBox", "textBox1", "getTextBox1", "unusedSlider", "getUnusedSlider", "unusedSlider1", "getUnusedSlider1", "KyinoClient"})
public final class ColorManager {
    @NotNull
    private static final Color background;
    @NotNull
    private static final Color textBox;
    @NotNull
    private static final Color dropDown;
    @NotNull
    private static final Color button;
    @NotNull
    private static final Color moduleBackground;
    @NotNull
    private static final Color unusedSlider;
    @NotNull
    private static final Color sliderButton;
    @NotNull
    private static final Color border;
    @NotNull
    private static final Color buttonOutline;
    @NotNull
    private static final Color ColorUiBackground;
    @NotNull
    private static final Color background1;
    @NotNull
    private static final Color textBox1;
    @NotNull
    private static final Color dropDown1;
    @NotNull
    private static final Color button1;
    @NotNull
    private static final Color moduleBackground1;
    @NotNull
    private static final Color unusedSlider1;
    @NotNull
    private static final Color sliderButton1;
    @NotNull
    private static final Color border1;
    @NotNull
    private static final Color buttonOutline1;
    public static final ColorManager INSTANCE;

    @NotNull
    public final Color getBackground() {
        return background;
    }

    @NotNull
    public final Color getTextBox() {
        return textBox;
    }

    @NotNull
    public final Color getDropDown() {
        return dropDown;
    }

    @NotNull
    public final Color getButton() {
        return button;
    }

    @NotNull
    public final Color getModuleBackground() {
        return moduleBackground;
    }

    @NotNull
    public final Color getUnusedSlider() {
        return unusedSlider;
    }

    @NotNull
    public final Color getSliderButton() {
        return sliderButton;
    }

    @NotNull
    public final Color getBorder() {
        return border;
    }

    @NotNull
    public final Color getButtonOutline() {
        return buttonOutline;
    }

    @NotNull
    public final Color getColorUiBackground() {
        return ColorUiBackground;
    }

    @NotNull
    public final Color getBackground1() {
        return background1;
    }

    @NotNull
    public final Color getTextBox1() {
        return textBox1;
    }

    @NotNull
    public final Color getDropDown1() {
        return dropDown1;
    }

    @NotNull
    public final Color getButton1() {
        return button1;
    }

    @NotNull
    public final Color getModuleBackground1() {
        return moduleBackground1;
    }

    @NotNull
    public final Color getUnusedSlider1() {
        return unusedSlider1;
    }

    @NotNull
    public final Color getSliderButton1() {
        return sliderButton1;
    }

    @NotNull
    public final Color getBorder1() {
        return border1;
    }

    @NotNull
    public final Color getButtonOutline1() {
        return buttonOutline1;
    }

    private ColorManager() {
    }

    static {
        ColorManager colorManager;
        INSTANCE = colorManager = new ColorManager();
        background = new Color(32, 32, 32);
        textBox = new Color(31, 31, 31);
        dropDown = new Color(45, 45, 45);
        button = new Color(52, 52, 52);
        moduleBackground = new Color(39, 39, 39);
        unusedSlider = new Color(154, 154, 154);
        sliderButton = new Color(69, 69, 69);
        border = new Color(25, 25, 25);
        buttonOutline = new Color(59, 59, 59);
        ColorUiBackground = new Color(255, 255, 255);
        background1 = new Color(32, 32, 32);
        textBox1 = new Color(31, 31, 31);
        dropDown1 = new Color(45, 45, 45);
        button1 = new Color(52, 52, 52);
        moduleBackground1 = new Color(39, 39, 39);
        unusedSlider1 = new Color(154, 154, 154);
        sliderButton1 = new Color(69, 69, 69);
        border1 = new Color(25, 25, 25);
        buttonOutline1 = new Color(59, 59, 59);
    }
}

