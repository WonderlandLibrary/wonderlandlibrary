package wtf.dawn.settings;

import wtf.dawn.settings.impl.BooleanSetting;
import java.util.function.Predicate;

public class ParentAttribute<T extends Setting> {
    public static final Predicate<BooleanSetting> BOOLEAN_CONDITION = BooleanSetting::isEnabled;
    private final T parent;
    private final Predicate<T> condition;

    public ParentAttribute(T parent, Predicate<T> condition) {
        this.parent = parent;
        this.condition = condition;
    }

    public boolean isValid() {
        return this.condition.test(this.parent) && this.parent.getParents().stream().allMatch(ParentAttribute::isValid);
    }

    public T getParent() {
        return this.parent;
    }
}

