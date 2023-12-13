// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.font;

import java.awt.FontFormatException;
import java.io.IOException;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class FontInit
{
    public void initFonts() {
        try {
            final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(0, FontInit.class.getResourceAsStream("/fonts/Comfortaa-Regular.ttf")));
            ge.registerFont(Font.createFont(0, FontInit.class.getResourceAsStream("/fonts/GOTHIC.TTF")));
            ge.registerFont(Font.createFont(0, FontInit.class.getResourceAsStream("/fonts/MODERN SPACE.ttf")));
        }
        catch (IOException | FontFormatException ex2) {
            final Exception ex;
            final Exception e = ex;
            e.printStackTrace();
        }
    }
}
