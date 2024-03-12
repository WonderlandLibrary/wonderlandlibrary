// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.bindings;

import java.util.Arrays;
import dev.tenacity.utils.font.FontUtil;
import dev.tenacity.utils.font.AbstractFontRenderer;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;
import dev.tenacity.utils.Utils;

@Exclude({ Strategy.NAME_REMAPPING })
public class FontBinding implements Utils
{
    public AbstractFontRenderer getCustomFont(final String fontName, final int fontSize) {
        final FontUtil.FontType fontType2 = Arrays.stream(FontUtil.FontType.values()).filter(fontType1 -> fontType1.name().equals(fontName)).findFirst().orElse(FontUtil.FontType.TENACITY);
        return fontType2.size(fontSize);
    }
    
    public AbstractFontRenderer getMinecraftFontRenderer() {
        return FontBinding.mc.fontRendererObj;
    }
    
    public AbstractFontRenderer getTenacityFont14() {
        return FontBinding.tenacityFont14;
    }
    
    public AbstractFontRenderer getTenacityFont16() {
        return FontBinding.tenacityFont16;
    }
    
    public AbstractFontRenderer getTenacityFont18() {
        return FontBinding.tenacityFont18;
    }
    
    public AbstractFontRenderer getTenacityFont20() {
        return FontBinding.tenacityFont20;
    }
    
    public AbstractFontRenderer getTenacityFont22() {
        return FontBinding.tenacityFont22;
    }
    
    public AbstractFontRenderer getTenacityFont24() {
        return FontBinding.tenacityFont24;
    }
    
    public AbstractFontRenderer getTenacityFont26() {
        return FontBinding.tenacityFont26;
    }
    
    public AbstractFontRenderer getTenacityFont28() {
        return FontBinding.tenacityFont28;
    }
    
    public AbstractFontRenderer getTenacityFont32() {
        return FontBinding.tenacityFont32;
    }
    
    public AbstractFontRenderer getTenacityFont40() {
        return FontBinding.tenacityFont40;
    }
    
    public AbstractFontRenderer getTenacityFont80() {
        return FontBinding.tenacityFont80;
    }
}
