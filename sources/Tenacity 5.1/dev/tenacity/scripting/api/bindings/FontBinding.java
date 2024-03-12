package dev.tenacity.scripting.api.bindings;

import dev.tenacity.utils.Utils;
import dev.tenacity.utils.font.AbstractFontRenderer;
import dev.tenacity.utils.font.FontUtil;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.util.Arrays;

@Exclude(Strategy.NAME_REMAPPING)
public class FontBinding implements Utils {

    public AbstractFontRenderer getCustomFont(String fontName, int fontSize) {
        FontUtil.FontType fontType = Arrays.stream(FontUtil.FontType.values()).filter(fontType1 -> fontType1.name().equals(fontName)).findFirst().orElse(FontUtil.FontType.TENACITY);
        return fontType.size(fontSize);
    }

    public AbstractFontRenderer getMinecraftFontRenderer() {
        return mc.fontRendererObj;
    }


    public AbstractFontRenderer getTenacityFont14() {return tenacityFont14; }
    public AbstractFontRenderer getTenacityFont16() {return tenacityFont16; }
    public AbstractFontRenderer getTenacityFont18() {return tenacityFont18; }
    public AbstractFontRenderer getTenacityFont20() {return tenacityFont20; }
    public AbstractFontRenderer getTenacityFont22() {return tenacityFont22; }
    public AbstractFontRenderer getTenacityFont24() {return tenacityFont24; }
    public AbstractFontRenderer getTenacityFont26() {return tenacityFont26; }
    public AbstractFontRenderer getTenacityFont28() {return tenacityFont28; }
    public AbstractFontRenderer getTenacityFont32() {return tenacityFont32; }
    public AbstractFontRenderer getTenacityFont40() {return tenacityFont40; }
    public AbstractFontRenderer getTenacityFont80() {return tenacityFont80; }
}
