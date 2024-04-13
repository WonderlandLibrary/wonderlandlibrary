package dev.tenacity.setting;

import java.util.function.Predicate;

public final class ParentAttribute<T extends Setting<?>> {

    private final T parent;
    private final Predicate<T> condition;

    public ParentAttribute(final T parent, final Predicate<T> condition) {
        this.parent = parent;
        this.condition = condition;
    }

    public T getParent() {
        return parent;
    }

    public boolean isCondition() {
        return condition.test(parent) && parent.getParentAttributeList().stream().allMatch(ParentAttribute::isCondition);
    }

}
