// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.font.testfontbase;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import net.augustus.font.CustomFontUtil;

public class FontUtil
{
    public static volatile int completed;
    public static CustomFontUtil espHotbar;
    public static CustomFontUtil esp;
    public static CustomFontUtil espTitle;
    public static CustomFontUtil verdana;
    public static CustomFontUtil arial;
    public static CustomFontUtil roboto;
    public static CustomFontUtil comfortaa;
    private static HashMap<String, InputStream> fontInputs;
    
    private static InputStream getInputSteam(final String location) {
        InputStream is = null;
        if (!FontUtil.fontInputs.containsKey(location)) {
            is = FontUtil.class.getClassLoader().getResourceAsStream(location);
            FontUtil.fontInputs.put(location, is);
        }
        else {
            is = FontUtil.fontInputs.get(location);
        }
        return is;
    }
    
    private static Font getFont(final String location, final int size) {
        Font font = null;
        try {
            final InputStream is = FontUtil.class.getClassLoader().getResourceAsStream(location);
            font = Font.createFont(0, is);
            font = font.deriveFont(0, (float)size);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading font");
            font = new Font("default", 0, 10);
        }
        return font;
    }
    
    public static void bootstrap() {
        FontUtil.espHotbar = new CustomFontUtil("Esp", getFont("esp.ttf", 44));
        FontUtil.esp = new CustomFontUtil("Esp", getFont("esp.ttf", 16));
        FontUtil.verdana = new CustomFontUtil("Verdana", getFont("verdana.ttf", 16));
        FontUtil.arial = new CustomFontUtil("Arial", getFont("arial.ttf", 16));
        FontUtil.roboto = new CustomFontUtil("Roboto", getFont("roboto.ttf", 16));
        FontUtil.espTitle = new CustomFontUtil("Esp", getFont("esp.ttf", 60));
        FontUtil.comfortaa = new CustomFontUtil("Comfortaa", getFont("comfortaa.ttf", 16));
    }
    
    static {
        FontUtil.fontInputs = new HashMap<String, InputStream>();
    }
}
