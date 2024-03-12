// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.settings;

import dev.tenacity.module.settings.impl.BooleanSetting;
import java.util.function.Predicate;

public class ParentAttribute<T extends Setting>
{
    public static final Predicate<BooleanSetting> BOOLEAN_CONDITION;
    private final T parent;
    private final Predicate<T> condition;
    
    public ParentAttribute(final T parent, final Predicate<T> condition) {
        this.parent = parent;
        this.condition = condition;
    }
    
    public boolean isValid() {
        return this.condition.test(this.parent) && this.parent.getParents().stream().allMatch(ParentAttribute::isValid);
    }
    
    public T getParent() {
        return this.parent;
    }
    
    static {
        BOOLEAN_CONDITION = BooleanSetting::isEnabled;
    }
}
