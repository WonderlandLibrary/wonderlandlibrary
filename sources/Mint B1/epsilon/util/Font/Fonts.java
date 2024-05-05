package epsilon.util.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;


public class Fonts {
    
    public static final MCFontRenderer Segation18 = new MCFontRenderer(new Font("Segation", Font.PLAIN,18),true,true);
    public static final MCFontRenderer Segation23 = new MCFontRenderer(new Font("Segation", Font.PLAIN,23),true,true);
    public static final MCFontRenderer Segation34 = new MCFontRenderer(new Font("Segation", Font.PLAIN,34),true,true);
    public static final MCFontRenderer Segation45 = new MCFontRenderer(new Font("Segation", Font.PLAIN,45),true,false);
    public static final MCFontRenderer Segation65 = new MCFontRenderer(new Font("Segation", Font.PLAIN,65),true,false);

    public static final MCFontRenderer MontserratReg12 = new MCFontRenderer(new Font("MontserratReg", Font.PLAIN,12),true,false);
    public static final MCFontRenderer MontserratReg18 = new MCFontRenderer(new Font("MontserratReg", Font.PLAIN,18),true,false);
    public static final MCFontRenderer MontserratReg24 = new MCFontRenderer(new Font("MontserratReg", Font.PLAIN,24),true,false);
    public static final MCFontRenderer MontserratReg30 = new MCFontRenderer(new Font("MontserratReg", Font.PLAIN,30),true,false);
    public static final MCFontRenderer MontserratReg74 = new MCFontRenderer(new Font("MontserratReg", Font.PLAIN,74),true,false);

    private static Font fontFromTTF(ResourceLocation fontLocation, float fontSize) {
        System.out.println("Loading font: " + fontLocation.toString() + " with size: " + fontSize + " and type: " + 0);
        Font output = null;
        try {
            output = Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

}
