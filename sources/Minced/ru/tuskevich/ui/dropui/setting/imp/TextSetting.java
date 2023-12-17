// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.dropui.setting.imp;

import java.util.function.Supplier;
import ru.tuskevich.ui.dropui.setting.Setting;

public class TextSetting extends Setting
{
    public String text;
    
    public TextSetting(final String name, final String text) {
        super(name, text);
        this.text = text;
        this.setVisible(() -> true);
    }
    
    public TextSetting(final String name, final String text, final Supplier<Boolean> visible) {
        super(name, text);
        this.text = text;
        this.setVisible(visible);
    }
    
    public String get() {
        return this.text;
    }
}
