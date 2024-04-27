package net.ccbluex.liquidbounce.Gui.cfont;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import java.awt.Font;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class FontManager {
    private static FontManager instance;
    private Map<String, CFontRenderer> fontMap = new HashMap<String, CFontRenderer>();

    static {

    }

    private FontManager() {
        this.registerFonts();
    }

    public static FontManager getInstance() {
        if (instance == null) {
            instance = new FontManager();
        }
        return instance;
    }

    public Map<String, CFontRenderer> getFontMap() {
        return this.fontMap;
    }

    public CFontRenderer getFont(String font) {
        return this.fontMap.get(font);
    }

    private void registerFonts() {
        this.fontMap.put("tabui", new CFontRenderer(new Font("Comfortaa", 0, 18), true, false));
        this.fontMap.put("bariol", new CFontRenderer(new Font("Bariol-Regular", 0, 18), true, false));
        this.fontMap.put("40", new CFontRenderer(new Font("Comfortaa", 0, 40), true, false));
        this.fontMap.put("title", new CFontRenderer(new Font("Comfortaa", 0, 62), true, false));
        this.fontMap.put("10", new CFontRenderer(new Font("Comfortaa", 0, 20), true, false));
        this.fontMap.put("BAROND", new CFontRenderer(new Font("Baron-Neue-Bold", 0, 45), true, false));
    }


}
