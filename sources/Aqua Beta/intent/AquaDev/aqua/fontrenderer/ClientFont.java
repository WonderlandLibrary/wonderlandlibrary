package intent.AquaDev.aqua.fontrenderer;

import intent.AquaDev.aqua.fontrenderer.GlyphPage;
import intent.AquaDev.aqua.fontrenderer.GlyphPageFontRenderer;
import java.awt.Font;
import java.io.InputStream;

public class ClientFont {
    public static GlyphPageFontRenderer font(int size, String fontName, boolean ttf) {
        try {
            InputStream istream = ClientFont.class.getResourceAsStream("/resources/" + fontName + ".ttf");
            Font myFont = Font.createFont((int)0, (InputStream)istream).deriveFont((float)size);
            GlyphPage fontPage = new GlyphPage(ttf ? myFont : new Font(fontName, 0, size), true, true);
            char[] chars = new char[256];
            for (int i = 0; i < chars.length; ++i) {
                chars[i] = (char)i;
            }
            fontPage.generateGlyphPage(chars);
            fontPage.setupTexture();
            GlyphPageFontRenderer fontrenderer = new GlyphPageFontRenderer(fontPage, fontPage, fontPage, fontPage);
            return fontrenderer;
        }
        catch (Exception e) {
            GlyphPage fontPage = new GlyphPage(new Font("Arial", 0, size), true, true);
            char[] chars = new char[256];
            for (int i = 0; i < chars.length; ++i) {
                chars[i] = (char)i;
            }
            fontPage.generateGlyphPage(chars);
            try {
                fontPage.setupTexture();
            }
            catch (Exception i) {
                // empty catch block
            }
            GlyphPageFontRenderer fontrenderer = new GlyphPageFontRenderer(fontPage, fontPage, fontPage, fontPage);
            return fontrenderer;
        }
    }
}
