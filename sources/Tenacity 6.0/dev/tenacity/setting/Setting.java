package dev.tenacity.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class Setting<T> {

    public String name;

    private final List<ParentAttribute<? extends Setting<?>>> parentAttributeList = new ArrayList<>();

    public final boolean hasParent() {
        return !parentAttributeList.isEmpty();
    }

    public final List<ParentAttribute<? extends Setting<?>>> getParentAttributeList() {
        return parentAttributeList;
    }

    public final <T extends Setting<?>> void addParent(final T parent, final Predicate<T> condition) {
        parentAttributeList.add(new ParentAttribute<>(parent, condition));
    }

    public final boolean isHidden() {
        if(!hasParent()) return false;

        return parentAttributeList.stream().noneMatch(ParentAttribute::isCondition);
    }

    public abstract T getConfigValue();

}
