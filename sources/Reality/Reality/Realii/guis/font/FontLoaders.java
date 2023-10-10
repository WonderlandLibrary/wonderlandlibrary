
package Reality.Realii.guis.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FontLoaders {

    private static HashMap fonts = new HashMap();

    public static CFontRenderer arial16 = new CFontRenderer(FontLoaders.getArial(16), true, true);
    public static CFontRenderer arial14 = new CFontRenderer(FontLoaders.getArial(14), true, true);
    
    public static CFontRenderer arial18 = new CFontRenderer(FontLoaders.getArial(18), true, true);
    public static CFontRenderer waterarial18 = new CFontRenderer(FontLoaders.getArial(34), true, true);
    public static CFontRenderer JelloFont49 = new CFontRenderer(FontLoaders.getJello(49), true, true);
    public static CFontRenderer JelloFont30 = new CFontRenderer(FontLoaders.getJello(26), true, true);
    public static CFontRenderer ArialMainMeniu = new CFontRenderer(FontLoaders.getArial(45), true, true);
    public static CFontRenderer Bpsarial18 = new CFontRenderer(FontLoaders.getArial(20), true, true);
    public static CFontRenderer New17 = new CFontRenderer(FontLoaders.getNew(17), true, true);
    public static CFontRenderer product18 = new CFontRenderer(FontLoaders.getProduct(18), true, true);
    public static CFontRenderer product12 = new CFontRenderer(FontLoaders.getProduct(12), true, true);
    public static CFontRenderer product23 = new CFontRenderer(FontLoaders.getProduct(23), true, true);
    public static CFontRenderer product16 = new CFontRenderer(FontLoaders.getProduct(16), true, true);
    public static CFontRenderer product14 = new CFontRenderer(FontLoaders.getProduct(14), true, true);
    public static CFontRenderer Codec_Cold18 = new CFontRenderer(FontLoaders.getRise(17), true, true);
    public static CFontRenderer Codec_Cold35 = new CFontRenderer(FontLoaders.getRise(35), true, true);
    public static CFontRenderer Codec_Cold19 = new CFontRenderer(FontLoaders.getRise(19), true, true);
    public static CFontRenderer product22 = new CFontRenderer(FontLoaders.getProduct(22), true, true);
    public static CFontRenderer product30 = new CFontRenderer(FontLoaders.getProduct(30), true, true);
  
//    public static CFontRenderer arial20 = new CFontRenderer(FontLoaders.getArial(20), true, true);
    public static CFontRenderer arial22 = new CFontRenderer(FontLoaders.getArial(22), true, true);
    public static CFontRenderer arial24 = new CFontRenderer(FontLoaders.getArial(24), true, true);
    public static CFontRenderer arial30 = new CFontRenderer(FontLoaders.getArial(30), true, true);
//    public static CFontRenderer arial26 = new CFontRenderer(FontLoaders.getArial(26), true, true);
//    public static CFontRenderer arial28 = new CFontRenderer(FontLoaders.getArial(28), true, true);

    public static CFontRenderer arial16B = new CFontRenderer(FontLoaders.getArialBold(17), true, true);
    public static CFontRenderer arial15B = new CFontRenderer(FontLoaders.getArialBold(15), true, true);
//    public static CFontRenderer arial18B = new CFontRenderer(FontLoaders.getArialBold(18), true, true);
//    public static CFontRenderer arial20B = new CFontRenderer(FontLoaders.getArialBold(20), true, true);
//    public static CFontRenderer arial22B = new CFontRenderer(FontLoaders.getArialBold(22), true, true);
//    public static CFontRenderer arial24B = new CFontRenderer(FontLoaders.getArialBold(24), true, true);
//    public static CFontRenderer arial26B = new CFontRenderer(FontLoaders.getArialBold(26), true, true);
//    public static CFontRenderer arial28B = new CFontRenderer(FontLoaders.getArialBold(28), true, true);


    public static CFontRenderer roboto16 = new CFontRenderer(FontLoaders.getroboto(16), true, true);
    public static CFontRenderer roboto15 = new CFontRenderer(FontLoaders.getroboto(15), true, true);
    
  

    

    //    public static UnicodeFontRenderer msFont14 = getUniFont("msyh",14.0F,false);
//    public static UnicodeFontRenderer msFont16 = getUniFont("msyh",16.0F,false);
    public UnicodeFontRenderer msFont18;

    public FontLoaders(){
        msFont18 = getUniFont("msyh", 18.0F, false);
    }

//    public static UnicodeFontRenderer msFont18;

//    static {
//        msFont18 = getUniFont("msyh", 18.0F, false);
//    }

    private static Font getDefault(int size) {
        return new Font("default", 0, size);
    }

    public static UnicodeFontRenderer getUniFont(String s, float size, boolean b2) {
        UnicodeFontRenderer UnicodeFontRenderer = null;

        try {
            if (fonts.containsKey(s) && ((HashMap) fonts.get(s)).containsKey(size)) {
                return (UnicodeFontRenderer) ((HashMap) fonts.get(s)).get(size);
            }

            Class class1 = FontLoaders.class;
            StringBuilder append = (new StringBuilder()).append("fonts/").append(s);
            String s2;
            if (b2) {
                s2 = ".otf";
            } else {
                s2 = ".ttf";
            }


            UnicodeFontRenderer = new UnicodeFontRenderer(Font.createFont(0, Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/msyh.ttf")).getInputStream()).deriveFont(size), size, -1, -1, false);
            UnicodeFontRenderer.setUnicodeFlag(true);
            UnicodeFontRenderer.setBidiFlag(Minecraft.getMinecraft().getLanguageManager().isCurrentLanguageBidirectional());
            HashMap hashMap = new HashMap();
            if (fonts.containsKey(s)) {
                hashMap.putAll((Map) fonts.get(s));
            }

            hashMap.put(size, UnicodeFontRenderer);
            fonts.put(s, hashMap);
        } catch (Exception var9) {
            var9.printStackTrace();
        }

        return UnicodeFontRenderer;
    }

    private static Font getArial(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Arial.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    
   


    private static Font getArialBold(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/ArialBold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    




    private static Font getroboto(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Roboto-Medium.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    
    private static Font getJello(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Helvetica-Bold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    
    private static Font getNew(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/New.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    
    private static Font getProduct(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Product Sans Bold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    
    private static Font getRise(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Araylist.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    
    
    private static Font getRiseW(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Product Sans Bold.ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    
    private static Font getCold(int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/Sex.otf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    
    
    
    
    

}

