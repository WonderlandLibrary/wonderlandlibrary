// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.module.data;

import com.google.gson.annotations.Expose;
import java.lang.reflect.Type;

public class Setting<E>
{
    private final String name;
    private final String desc;
    private final Type type;
    private final double inc;
    private final double min;
    private final double max;
    @Expose
    private E value;
    
    public String getDesc() {
        return this.desc;
    }
    
    public Setting(final String name, final E value, final String desc) {
        this.name = name;
        this.value = value;
        this.type = value.getClass().getGenericSuperclass();
        this.desc = desc;
        if (value instanceof Number) {
            this.inc = 0.5;
            this.min = 1.0;
            this.max = 20.0;
        }
        else {
            this.inc = 0.0;
            this.min = 0.0;
            this.max = 0.0;
        }
    }
    
    public Setting(final String name, final E value, final String desc, final double inc, final double min, final double max) {
        this.name = name;
        this.value = value;
        this.type = value.getClass().getGenericSuperclass();
        this.desc = desc;
        this.inc = inc;
        this.min = min;
        this.max = max;
    }
    
    public void setValue(final E value) {
        this.value = value;
    }
    
    public E getValue() {
        return this.value;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public double getInc() {
        return this.inc;
    }
    
    public double getMin() {
        return this.min;
    }
    
    public double getMax() {
        return this.max;
    }
    
    public void update(final Setting setting) {
        this.value = setting.value;
    }
}
