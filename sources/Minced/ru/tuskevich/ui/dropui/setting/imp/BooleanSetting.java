// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.dropui.setting.imp;

import java.util.function.Supplier;
import ru.tuskevich.ui.dropui.setting.Setting;

public class BooleanSetting extends Setting
{
    public boolean state;
    
    public BooleanSetting(final String name, final boolean state) {
        super(name, state);
        this.state = state;
        this.setVisible(() -> true);
    }
    
    public BooleanSetting(final String name, final boolean state, final Supplier<Boolean> visible) {
        super(name, state);
        this.state = state;
        this.setVisible(visible);
    }
    
    public boolean get() {
        return this.state;
    }
    
    public void setBoolValue(final boolean state) {
        this.state = state;
    }
}
