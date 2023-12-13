package wtf.diablo.utils.font;

import net.minecraft.util.ResourceLocation;

import java.awt.*;

import static wtf.diablo.utils.Util.mc;

public class Fonts {
    public static final MCFontRenderer IconFont = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/guiicons.ttf"),24), true, true);
    public static final MCFontRenderer IconFontBig = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/guiicons.ttf"),38), true, true);

    public static final MCFontRenderer SFBold30 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/sfsemibold.ttf"),30), true, true);
    public static final MCFontRenderer SFBold18 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/sfsemibold.ttf"),18), true, true);
    public static final MCFontRenderer SFReg18 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/sfregular.ttf"),18), true, true);
    public static final MCFontRenderer SFReg24 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/sfregular.ttf"),24), true, true);
    public static final MCFontRenderer SFReg45 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/sfregular.ttf"),45), true, true);
    public static final MCFontRenderer axi24 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/axi.ttf"),24), true, false);
    public static final MCFontRenderer axi12 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/axi.ttf"),12), true, false);
    public static final MCFontRenderer axi16 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/axi.ttf"),16), true, false);
    public static final MCFontRenderer axi18 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/axi.ttf"),18), true, false);
    public static final MCFontRenderer axi45 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/axi.ttf"),45), true, false);

    public static final MCFontRenderer apple18 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/apple.ttf"),18), true, false);
    public static final MCFontRenderer apple24 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/apple.ttf"),24 ), true, false);
    //public static final MCFontRenderer hearts18 = new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/hearts.ttf"),18), true, true);
    public static final MCFontRenderer Arial18 = new MCFontRenderer(new Font("Arial", Font.PLAIN,18),true,true);
    public static final MCFontRenderer Arial45 = new MCFontRenderer(new Font("Arial", Font.PLAIN,45),true,false);
    public static final MCFontRenderer Arial65 = new MCFontRenderer(new Font("Arial", Font.PLAIN,65),true,false);
    public static final MCFontRenderer Checkmark =  new MCFontRenderer(fontFromTTF(new ResourceLocation("diablo/fonts/checkmark.ttf"),24), true, false);

    private static Font fontFromTTF(ResourceLocation fontLocation, float fontSize) {
        System.out.println("Loading font: " + fontLocation.toString() + " with size: " + fontSize + " and type: " + 0);
        Font output = null;
        try {
            output = Font.createFont(0, mc.getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

}
