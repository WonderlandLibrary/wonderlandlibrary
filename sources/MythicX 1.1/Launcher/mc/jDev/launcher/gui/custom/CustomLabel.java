package mc.jDev.launcher.gui.custom;

import mc.jDev.launcher.util.MathUtil;

import javax.swing.*;
import java.awt.*;

public class CustomLabel extends JLabel {
    public static String normaldl;
    public CustomLabel(String label, int size, boolean bold, boolean centered, Color textColor) {
        super(label);
        setFont(new Font("Arial", bold ? Font.BOLD : Font.PLAIN, size));
        setForeground(textColor);
        if (centered) {
            setHorizontalAlignment(CENTER);
        }
    }
    public CustomLabel(String label, int size, boolean bold) {
        this(label, size, bold, false, new Color(255, 255, 255));
    }
}
