package Velo.api.Util.fontRenderer;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import Velo.api.Util.fontRenderer.Utils.Tff;
import Velo.impl.Modules.visuals.hud.HUD;
import net.minecraft.client.Minecraft;

public class Fonts {
	public static Tff Hud;
	public static Tff Hud2;
	public static Tff MM;
	public static Tff mm1;
	public static Tff noti;
	public static Tff notides;
	public static Tff mm2;
	public static Tff targethud;
	public static Tff small;
	
	public static Tff targethudName;
	public static Tff targethudNameMoon;
	public static Tff clickguitest;
	public static Tff targethudHealthMoon;
	public static Tff nametags;
	public static Tff badCache;
	public static Tff fss;
	public static Tff fs;
	public static Tff f;
	
    public static void loadFonts() {
        InputStream is = Fonts.class.getResourceAsStream("fonts/Product Sans Regular.ttf");
        InputStream is1 = Fonts.class.getResourceAsStream("fonts/Product Sans Bold.ttf");
        Font font = null;
        Font font1 = null;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            font1 = Font.createFont(Font.TRUETYPE_FONT, is1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    	catch (FontFormatException e) {
            e.printStackTrace();
        }
        
        Hud = new Tff(font.deriveFont((float) HUD.fontsize.getValue()));
        MM = new Tff(font.deriveFont((float) 100));
        mm1 = new Tff(font.deriveFont((float) 20));
        mm2 = new Tff(font.deriveFont((float) 35));
        targethud = new Tff(font.deriveFont((float) 18));
        targethudName = new Tff(font.deriveFont((float) 25));
        nametags = new Tff(font.deriveFont((float) 30));
        noti = new Tff(font.deriveFont((float) 15));
        notides = new Tff(font.deriveFont((float) 22));
        Hud2 = new Tff(font.deriveFont((float) HUD.fontsize2.getValue()));
        small = new Tff(font.deriveFont(11f));
        badCache = new Tff(font1.deriveFont(36f));
        fss = new Tff(font.deriveFont(18f));
        fs = new Tff(font.deriveFont(18f));
        f = new Tff(font.deriveFont(24f));
        
        targethudHealthMoon = new Tff(font.deriveFont(14f));
        targethudNameMoon = new Tff(font.deriveFont(20f));
        clickguitest = new Tff(font.deriveFont(35f));
        if(Minecraft.getMinecraft().gameSettings.language != null) {
            Fonts.Hud.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
        }
    }
    public static enum FontType{
        EMBOSS_BOTTOM, EMBOSS_TOP, NORMAL, OUTLINE_THIN, SHADOW_THICK, SHADOW_THIN;
    }
}
