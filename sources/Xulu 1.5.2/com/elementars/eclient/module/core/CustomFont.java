// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.core;

import dev.xulu.settings.OnChangedValue;
import com.elementars.eclient.font.XFontRenderer;
import com.elementars.eclient.font.CFontManager;
import java.awt.Font;
import com.elementars.eclient.Xulu;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import com.elementars.eclient.module.Category;
import dev.xulu.settings.TextBox;
import dev.xulu.settings.Value;
import com.elementars.eclient.module.Module;

public class CustomFont extends Module
{
    public static Value<String> customFontMode;
    public static Value<TextBox> FONT;
    public static Value<Integer> FONT_STYLE;
    public static Value<Integer> FONT_SIZE;
    public static Value<Boolean> antiAlias;
    public static Value<Boolean> metrics;
    public static Value<Boolean> shadow;
    public static Value<Integer> fontOffset;
    
    public CustomFont() {
        super("CustomFont", "Custom in game text rendering", 0, Category.CORE, false);
        CustomFont.customFontMode = this.register(new Value<String>("Mode", this, "Normal", new ArrayList<String>(Arrays.asList("Normal", "Xdolf", "Rainbow"))));
        CustomFont.FONT = this.register(new Value<TextBox>("Font", this, new TextBox("Verdana"))).newValueFilter(textBox -> Xulu.getCorrectFont(textBox.getText()) != null).withFilterError("Not a font! (Case-sensitive)").onChanged(textBoxOnChangedValue -> updateFont(textBoxOnChangedValue.getNew().getText(), CustomFont.FONT_STYLE.getValue(), CustomFont.FONT_SIZE.getValue(), CustomFont.antiAlias.getValue(), CustomFont.metrics.getValue())).visibleWhen(textBox -> !CustomFont.customFontMode.getValue().equalsIgnoreCase("Rainbow"));
        CustomFont.FONT_STYLE = this.register(new Value<Integer>("Font Style", this, 0, 0, 2)).onChanged(integerOnChangedValue -> updateFont(CustomFont.FONT.getValue().getText(), integerOnChangedValue.getNew(), CustomFont.FONT_SIZE.getValue(), CustomFont.antiAlias.getValue(), CustomFont.metrics.getValue())).visibleWhen(integer -> !CustomFont.customFontMode.getValue().equalsIgnoreCase("Rainbow"));
        CustomFont.FONT_SIZE = this.register(new Value<Integer>("Font Size", this, 18, 5, 50)).onChanged(integerOnChangedValue -> updateFont(CustomFont.FONT.getValue().getText(), CustomFont.FONT_STYLE.getValue(), integerOnChangedValue.getNew(), CustomFont.antiAlias.getValue(), CustomFont.metrics.getValue())).visibleWhen(integer -> !CustomFont.customFontMode.getValue().equalsIgnoreCase("Rainbow"));
        CustomFont.antiAlias = this.register(new Value<Boolean>("Anti Alias", this, true)).visibleWhen(integer -> CustomFont.customFontMode.getValue().equalsIgnoreCase("Normal") || CustomFont.customFontMode.getValue().equalsIgnoreCase("Xdolf")).onChanged(booleanOnChangedValue -> updateFont(CustomFont.FONT.getValue().getText(), CustomFont.FONT_STYLE.getValue(), CustomFont.FONT_SIZE.getValue(), booleanOnChangedValue.getNew(), CustomFont.metrics.getValue()));
        CustomFont.metrics = this.register(new Value<Boolean>("Metrics", this, true)).visibleWhen(integer -> CustomFont.customFontMode.getValue().equalsIgnoreCase("Normal")).onChanged(booleanOnChangedValue -> updateFont(CustomFont.FONT.getValue().getText(), CustomFont.FONT_STYLE.getValue(), CustomFont.FONT_SIZE.getValue(), CustomFont.antiAlias.getValue(), booleanOnChangedValue.getNew()));
        CustomFont.shadow = this.register(new Value<Boolean>("Shadow", this, true)).visibleWhen(aBoolean -> CustomFont.customFontMode.getValue().equalsIgnoreCase("Normal"));
        CustomFont.fontOffset = this.register(new Value<Integer>("Font Offset", this, 0, -5, 5)).visibleWhen(integer -> !CustomFont.customFontMode.getValue().equalsIgnoreCase("Rainbow"));
    }
    
    public static void updateFont(final String newName, final int style, final int size, final boolean antialias, final boolean metrics) {
        final String s = CustomFont.customFontMode.getValue();
        switch (s) {
            case "Normal": {
                try {
                    if (newName.equalsIgnoreCase("Comfortaa Regular")) {
                        CFontManager.customFont = new com.elementars.eclient.font.custom.CustomFont(new Font("Comfortaa Regular", style, size), antialias, metrics);
                        return;
                    }
                    CFontManager.customFont = new com.elementars.eclient.font.custom.CustomFont(new Font(Xulu.getCorrectFont(newName), style, size), antialias, metrics);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case "Xdolf": {
                try {
                    if (newName.equalsIgnoreCase("Comfortaa Regular")) {
                        CFontManager.xFontRenderer = new XFontRenderer(new Font("Comfortaa Regular", style, size * 2), antialias, 8);
                        return;
                    }
                    CFontManager.xFontRenderer = new XFontRenderer(new Font(Xulu.getCorrectFont(newName), style, size * 2), antialias, 8);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
