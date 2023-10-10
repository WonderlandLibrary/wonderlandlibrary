/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util.fontRenderer;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import me.AveReborn.util.fontRenderer.UnicodeFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.LanguageManager;

public class FontManager {
    private HashMap<String, HashMap<Float, UnicodeFontRenderer>> fonts = new HashMap();
    public UnicodeFontRenderer tahoma5 = this.getFont("tahoma", 5.0f);
    public UnicodeFontRenderer tahoma10 = this.getFont("tahoma", 10.0f);
    public UnicodeFontRenderer tahoma11 = this.getFont("tahoma", 11.0f);
    public UnicodeFontRenderer tahoma12 = this.getFont("tahoma", 12.0f);
    public UnicodeFontRenderer tahoma13 = this.getFont("tahoma", 13.0f);
    public UnicodeFontRenderer tahoma14 = this.getFont("tahoma", 14.0f);
    public UnicodeFontRenderer tahoma15 = this.getFont("tahoma", 15.0f);
    public UnicodeFontRenderer tahoma16 = this.getFont("tahoma", 16.0f);
    public UnicodeFontRenderer tahoma17 = this.getFont("tahoma", 17.0f);
    public UnicodeFontRenderer tahoma18 = this.getFont("tahoma", 18.0f);
    public UnicodeFontRenderer tahoma19 = this.getFont("tahoma", 19.0f);
    public UnicodeFontRenderer tahoma20 = this.getFont("tahoma", 20.0f);
    public UnicodeFontRenderer tahoma25 = this.getFont("tahoma", 25.0f);
    public UnicodeFontRenderer tahoma30 = this.getFont("tahoma", 30.0f);
    public UnicodeFontRenderer tahoma35 = this.getFont("tahoma", 35.0f);
    public UnicodeFontRenderer tahoma40 = this.getFont("tahoma", 40.0f);
    public UnicodeFontRenderer tahoma45 = this.getFont("tahoma", 45.0f);
    public UnicodeFontRenderer tahoma50 = this.getFont("tahoma", 50.0f);
    public UnicodeFontRenderer tahoma55 = this.getFont("tahoma", 55.0f);
    public UnicodeFontRenderer tahoma60 = this.getFont("tahoma", 60.0f);
    public UnicodeFontRenderer tahoma65 = this.getFont("tahoma", 65.0f);
    public UnicodeFontRenderer tahoma70 = this.getFont("tahoma", 70.0f);
    public UnicodeFontRenderer sansation5 = this.getFont("sansation", 5.0f);
    public UnicodeFontRenderer sansation10 = this.getFont("sansation", 10.0f);
    public UnicodeFontRenderer sansation11 = this.getFont("sansation", 11.0f);
    public UnicodeFontRenderer sansation12 = this.getFont("sansation", 12.0f);
    public UnicodeFontRenderer sansation13 = this.getFont("sansation", 13.0f);
    public UnicodeFontRenderer sansation14 = this.getFont("sansation", 14.0f);
    public UnicodeFontRenderer sansation15 = this.getFont("sansation", 15.0f);
    public UnicodeFontRenderer sansation16 = this.getFont("sansation", 16.0f);
    public UnicodeFontRenderer sansation17 = this.getFont("sansation", 17.0f);
    public UnicodeFontRenderer sansation18 = this.getFont("sansation", 18.0f);
    public UnicodeFontRenderer sansation19 = this.getFont("sansation", 19.0f);
    public UnicodeFontRenderer sansation20 = this.getFont("sansation", 20.0f);
    public UnicodeFontRenderer sansation25 = this.getFont("sansation", 25.0f);
    public UnicodeFontRenderer sansation30 = this.getFont("sansation", 30.0f);
    public UnicodeFontRenderer sansation35 = this.getFont("sansation", 35.0f);
    public UnicodeFontRenderer sansation40 = this.getFont("sansation", 40.0f);
    public UnicodeFontRenderer sansation45 = this.getFont("sansation", 45.0f);
    public UnicodeFontRenderer sansation50 = this.getFont("sansation", 50.0f);
    public UnicodeFontRenderer sansation55 = this.getFont("sansation", 55.0f);
    public UnicodeFontRenderer sansation60 = this.getFont("sansation", 60.0f);
    public UnicodeFontRenderer sansation65 = this.getFont("sansation", 65.0f);
    public UnicodeFontRenderer sansation70 = this.getFont("sansation", 70.0f);
    public UnicodeFontRenderer simpleton10 = this.getFont("simpleton", 10.0f, true);
    public UnicodeFontRenderer simpleton11 = this.getFont("simpleton", 11.0f, true);
    public UnicodeFontRenderer simpleton12 = this.getFont("simpleton", 12.0f, true);
    public UnicodeFontRenderer simpleton13 = this.getFont("simpleton", 13.0f, true);
    public UnicodeFontRenderer simpleton14 = this.getFont("simpleton", 14.0f, true);
    public UnicodeFontRenderer simpleton15 = this.getFont("simpleton", 15.0f, true);
    public UnicodeFontRenderer simpleton16 = this.getFont("simpleton", 16.0f, true);
    public UnicodeFontRenderer simpleton17 = this.getFont("simpleton", 17.0f, true);
    public UnicodeFontRenderer simpleton18 = this.getFont("simpleton", 18.0f, true);
    public UnicodeFontRenderer simpleton20 = this.getFont("simpleton", 20.0f, true);
    public UnicodeFontRenderer simpleton25 = this.getFont("simpleton", 25.0f, true);
    public UnicodeFontRenderer simpleton30 = this.getFont("simpleton", 30.0f, true);
    public UnicodeFontRenderer simpleton35 = this.getFont("simpleton", 35.0f, true);
    public UnicodeFontRenderer simpleton40 = this.getFont("simpleton", 40.0f, true);
    public UnicodeFontRenderer simpleton45 = this.getFont("simpleton", 45.0f, true);
    public UnicodeFontRenderer simpleton50 = this.getFont("simpleton", 50.0f, true);
    public UnicodeFontRenderer simpleton70 = this.getFont("simpleton", 70.0f, true);
    public UnicodeFontRenderer consolasbold14 = this.getFont("consolasbold", 14.0f);
    public UnicodeFontRenderer consolasbold18 = this.getFont("consolasbold", 18.0f);
    public UnicodeFontRenderer consolasbold20 = this.getFont("consolasbold", 20.0f);
    public UnicodeFontRenderer verdana10 = this.getFont("verdana", 10.0f);
    public UnicodeFontRenderer verdana11 = this.getFont("verdana", 11.0f);
    public UnicodeFontRenderer verdana12 = this.getFont("verdana", 12.0f);
    public UnicodeFontRenderer verdana13 = this.getFont("verdana", 13.0f);
    public UnicodeFontRenderer verdana15 = this.getFont("verdana", 15.0f);
    public UnicodeFontRenderer verdana16 = this.getFont("verdana", 16.0f);
    public UnicodeFontRenderer verdana17 = this.getFont("verdana", 17.0f);
    public UnicodeFontRenderer verdana18 = this.getFont("verdana", 18.0f);
    public UnicodeFontRenderer verdana20 = this.getFont("verdana", 20.0f);
    public UnicodeFontRenderer verdana25 = this.getFont("verdana", 25.0f);
    public UnicodeFontRenderer verdana30 = this.getFont("verdana", 30.0f);
    public UnicodeFontRenderer verdana35 = this.getFont("verdana", 35.0f);
    public UnicodeFontRenderer verdana40 = this.getFont("verdana", 40.0f);
    public UnicodeFontRenderer verdana45 = this.getFont("verdana", 45.0f);
    public UnicodeFontRenderer verdana50 = this.getFont("verdana", 50.0f);

    public UnicodeFontRenderer getFont(String name, float size) {
        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && this.fonts.get(name).containsKey(Float.valueOf(size))) {
                return this.fonts.get(name).get(Float.valueOf(size));
            }
            InputStream inputStream = this.getClass().getResourceAsStream("fonts/" + name + ".ttf");
            Font font = null;
            font = Font.createFont(0, inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            HashMap<Float, UnicodeFontRenderer> map = new HashMap<Float, UnicodeFontRenderer>();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map)this.fonts.get(name));
            }
            map.put(Float.valueOf(size), unicodeFont);
            this.fonts.put(name, map);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return unicodeFont;
    }

    public UnicodeFontRenderer getFont(String name, float size, boolean b2) {
        UnicodeFontRenderer unicodeFont = null;
        try {
            if (this.fonts.containsKey(name) && this.fonts.get(name).containsKey(Float.valueOf(size))) {
                return this.fonts.get(name).get(Float.valueOf(size));
            }
            InputStream inputStream = this.getClass().getResourceAsStream("fonts/" + name + ".otf");
            Font font = null;
            font = Font.createFont(0, inputStream);
            unicodeFont = new UnicodeFontRenderer(font.deriveFont(size));
            unicodeFont.setUnicodeFlag(true);
            unicodeFont.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            HashMap<Float, UnicodeFontRenderer> map = new HashMap<Float, UnicodeFontRenderer>();
            if (this.fonts.containsKey(name)) {
                map.putAll((Map)this.fonts.get(name));
            }
            map.put(Float.valueOf(size), unicodeFont);
            this.fonts.put(name, map);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return unicodeFont;
    }
}

