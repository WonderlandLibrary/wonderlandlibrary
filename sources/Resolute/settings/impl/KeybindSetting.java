// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.settings.impl;

import java.util.function.Supplier;
import vip.Resolute.settings.Setting;

public class KeybindSetting extends Setting<Integer>
{
    private int code;
    
    public KeybindSetting(final String name, final int code, final Supplier<Boolean> dependency) {
        super(name, code, dependency);
        this.name = name;
        this.code = code;
    }
    
    public KeybindSetting(final String name, final int code) {
        this(name, code, () -> true);
        this.name = name;
        this.code = code;
    }
    
    public int getCode() {
        return (this.code == -1) ? 0 : this.code;
    }
    
    public void setCode(final int code) {
        this.code = code;
    }
}
