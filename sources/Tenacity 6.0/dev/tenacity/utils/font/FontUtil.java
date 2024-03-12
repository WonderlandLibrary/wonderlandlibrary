// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.font;

import java.io.InputStream;
import net.minecraft.client.Minecraft;
import java.awt.Font;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import java.util.HashMap;
import dev.tenacity.utils.Utils;

public class FontUtil implements Utils
{
    public static final String BUG = "a";
    public static final String LIST = "b";
    public static final String BOMB = "c";
    public static final String EYE = "d";
    public static final String PERSON = "e";
    public static final String WHEELCHAIR = "f";
    public static final String SCRIPT = "g";
    public static final String SKIP_LEFT = "h";
    public static final String PAUSE = "i";
    public static final String PLAY = "j";
    public static final String SKIP_RIGHT = "k";
    public static final String SHUFFLE = "l";
    public static final String INFO = "m";
    public static final String SETTINGS = "n";
    public static final String CHECKMARK = "o";
    public static final String XMARK = "p";
    public static final String TRASH = "q";
    public static final String WARNING = "r";
    public static final String FOLDER = "s";
    public static final String LOAD = "t";
    public static final String SAVE = "u";
    public static final String UPVOTE_OUTLINE = "v";
    public static final String UPVOTE = "w";
    public static final String DOWNVOTE_OUTLINE = "x";
    public static final String DOWNVOTE = "y";
    public static final String DROPDOWN_ARROW = "z";
    public static final String PIN = "s";
    public static final String EDIT = "A";
    public static final String SEARCH = "B";
    public static final String UPLOAD = "C";
    public static final String REFRESH = "D";
    public static final String ADD_FILE = "E";
    public static final String STAR_OUTLINE = "F";
    public static final String STAR = "G";
    private static final HashMap<FontType, Map<Integer, CustomFont>> customFontMap;
    
    public static void setupFonts() {
        for (final FontType type : FontType.values()) {
            type.setup();
            final HashMap<Integer, CustomFont> fontSizes = new HashMap<Integer, CustomFont>();
            if (type.hasBold()) {
                for (final int size : type.getSizes()) {
                    final CustomFont font = new CustomFont(type.fromSize(size));
                    font.setBoldFont(new CustomFont(type.fromBoldSize(size)));
                    fontSizes.put(size, font);
                }
            }
            else {
                for (final int size : type.getSizes()) {
                    fontSizes.put(size, new CustomFont(type.fromSize(size)));
                }
            }
            FontUtil.customFontMap.put(type, fontSizes);
        }
    }
    
    private static Font getFontData(final ResourceLocation location) {
        try {
            final InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
            return Font.createFont(0, is);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            return new Font("default", 0, 10);
        }
    }
    
    static {
        customFontMap = new HashMap<FontType, Map<Integer, CustomFont>>();
    }
    
    public enum FontType
    {
        TENACITY("tenacity", "tenacity-bold", new int[] { 12, 14, 16, 18, 20, 22, 24, 26, 28, 32, 40, 80 }), 
        RISE("rise", "rise-bold", new int[] { 18 }), 
        TAHOMA("tahoma", "tahoma-bold", new int[] { 10, 12, 14, 16, 18, 27 }), 
        RUBIK("rubik", "rubik-bold", new int[] { 13, 18 }), 
        NEVERLOSE("neverlose", new int[] { 12, 18, 22 }), 
        ICON("icon", new int[] { 16, 20, 26, 35, 40 });
        
        private final ResourceLocation location;
        private final ResourceLocation boldLocation;
        private Font font;
        private Font boldFont;
        private final int[] sizes;
        
        private FontType(final String fontName, final String boldName, final int[] sizes) {
            this.location = new ResourceLocation("Tenacity/Fonts/" + fontName + ".ttf");
            this.boldLocation = new ResourceLocation("Tenacity/Fonts/" + boldName + ".ttf");
            this.sizes = sizes;
        }
        
        private FontType(final String fontName, final int[] sizes) {
            this.location = new ResourceLocation("Tenacity/Fonts/" + fontName + ".ttf");
            this.boldLocation = null;
            this.sizes = sizes;
        }
        
        public boolean hasBold() {
            return this.boldLocation != null;
        }
        
        public Font fromSize(final int size) {
            return this.font.deriveFont(0, (float)size);
        }
        
        private Font fromBoldSize(final int size) {
            return this.boldFont.deriveFont(0, (float)size);
        }
        
        public void setup() {
            this.font = getFontData(this.location);
            if (this.boldLocation != null) {
                this.boldFont = getFontData(this.boldLocation);
            }
        }
        
        public CustomFont size(final int size) {
            return FontUtil.customFontMap.get(this).computeIfAbsent(Integer.valueOf(size), k -> null);
        }
        
        public CustomFont boldSize(final int size) {
            return FontUtil.customFontMap.get(this).get(size).getBoldFont();
        }
        
        public ResourceLocation getLocation() {
            return this.location;
        }
        
        public ResourceLocation getBoldLocation() {
            return this.boldLocation;
        }
        
        public Font getFont() {
            return this.font;
        }
        
        public Font getBoldFont() {
            return this.boldFont;
        }
        
        public int[] getSizes() {
            return this.sizes;
        }
    }
}
