// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.ui.dropui.setting.imp;

import java.util.function.Supplier;
import ru.tuskevich.ui.dropui.setting.Setting;

public class RangeSetting extends Setting
{
    public float first;
    public float second;
    public float min;
    public float max;
    public float increment;
    
    public RangeSetting(final String name, final float first, final float second, final float min, final float max, final float increment) {
        super(name, first);
        this.min = min;
        this.max = max;
        this.first = first;
        this.second = second;
        this.increment = increment;
        this.setVisible(() -> true);
    }
    
    public RangeSetting(final String name, final float first, final float second, final float min, final float max, final float increment, final Supplier<Boolean> visible) {
        super(name, first);
        this.min = min;
        this.max = max;
        this.first = first;
        this.second = second;
        this.increment = increment;
        this.setVisible(visible);
    }
    
    public float getFirst() {
        return this.first;
    }
    
    public float getSecond() {
        return this.second;
    }
    
    public float getMin() {
        return this.min;
    }
    
    public float getMax() {
        return this.max;
    }
    
    public float getIncrement() {
        return this.increment;
    }
}
