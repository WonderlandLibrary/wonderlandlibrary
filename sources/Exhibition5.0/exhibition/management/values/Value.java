// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.values;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;
import java.util.List;
import exhibition.module.Module;

public class Value<T>
{
    String name;
    Module module;
    T value;
    List<Value> values;
    
    public Value(final String name, final T value, final Module module) {
        this.name = name;
        this.value = value;
        this.module = module;
    }
    
    public void setValue(final T value) {
        this.value = value;
    }
    
    public T getValue() {
        return this.value;
    }
    
    public String getValueName() {
        return this.name;
    }
    
    public void setValueName(final String name) {
        this.name = name;
    }
    
    public Module getModule() {
        return this.module;
    }
    
    public List<Value> getValues() {
        return this.values;
    }
    
    @Target({ ElementType.FIELD })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Val {
        String name() default "null";
        
        double minVal() default 1.0;
        
        double maxVal() default 5.0;
        
        double valInc() default 0.5;
    }
}
