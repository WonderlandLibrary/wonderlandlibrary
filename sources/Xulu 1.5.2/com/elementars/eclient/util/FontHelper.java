// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import com.elementars.eclient.font.XFontRenderer;
import com.elementars.eclient.font.CFontManager;
import com.elementars.eclient.font.custom.CustomFont;
import java.awt.Font;
import com.elementars.eclient.command.Command;
import com.elementars.eclient.Xulu;

public class FontHelper
{
    public static void setCFontRenderer(final String stringIn, final int style, final int size, final boolean antialias, final boolean metrics) {
        try {
            if (Xulu.getCorrectFont(stringIn) == null) {
                Command.sendChatMessage("Invalid font!");
                return;
            }
            if (stringIn.equalsIgnoreCase("Comfortaa Regular")) {
                CFontManager.customFont = new CustomFont(new Font("Comfortaa Regular", style, size), antialias, metrics);
                return;
            }
            CFontManager.customFont = new CustomFont(new Font(Xulu.getCorrectFont(stringIn), style, size), antialias, metrics);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void setXdolfFontRenderer(final String stringIn, final int style, final int size, final boolean antialias) {
        try {
            if (Xulu.getCorrectFont(stringIn) == null && !stringIn.equalsIgnoreCase("Xulu")) {
                Command.sendChatMessage("Invalid font!");
                return;
            }
            CFontManager.xFontRenderer = new XFontRenderer(new Font(Xulu.getCorrectFont(stringIn), style, size), antialias, 8);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
