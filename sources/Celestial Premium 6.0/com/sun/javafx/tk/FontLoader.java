/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.scene.text.Font
 *  javafx.scene.text.FontPosture
 *  javafx.scene.text.FontWeight
 */
package com.sun.javafx.tk;

import com.sun.javafx.tk.FontMetrics;
import java.io.InputStream;
import java.util.List;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public abstract class FontLoader {
    public abstract void loadFont(Font var1);

    public abstract List<String> getFamilies();

    public abstract List<String> getFontNames();

    public abstract List<String> getFontNames(String var1);

    public abstract Font font(String var1, FontWeight var2, FontPosture var3, float var4);

    public abstract Font[] loadFont(InputStream var1, double var2, boolean var4);

    public abstract Font[] loadFont(String var1, double var2, boolean var4);

    public abstract FontMetrics getFontMetrics(Font var1);

    public abstract float getCharWidth(char var1, Font var2);

    public abstract float getSystemFontSize();
}

