package tech.atani.client.feature.font;

import net.minecraft.client.gui.FontRenderer;

public class FontEntry {

    private final String name;
    private final float size;
    private final FontRenderer classicFontRenderer;
    private final FontRenderer modernFontRenderer;

    public FontEntry(String name, float size, FontRenderer classicFontRenderer, FontRenderer modernFontRenderer) {
        this.name = name;
        this.size = size;
        this.classicFontRenderer = classicFontRenderer;
        this.modernFontRenderer = modernFontRenderer;
    }

    public String getName() {
        return name;
    }

    public float getSize() {
        return size;
    }

    public FontRenderer getClassicFontRenderer() {
        return classicFontRenderer;
    }

    public FontRenderer getModernFontRenderer() {
        return modernFontRenderer;
    }
}
