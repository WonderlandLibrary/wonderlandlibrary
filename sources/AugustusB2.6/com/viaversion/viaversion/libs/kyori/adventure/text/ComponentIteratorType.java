// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import java.util.List;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import java.util.Collection;
import java.util.Set;
import java.util.Deque;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

@FunctionalInterface
@ApiStatus.NonExtendable
public interface ComponentIteratorType
{
    public static final ComponentIteratorType DEPTH_FIRST = (component, deque, flags) -> {
        if (flags.contains(ComponentIteratorFlag.INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS) && component instanceof TranslatableComponent) {
            translatable = component;
            args = translatable.args();
            for (i = args.size() - 1; i >= 0; --i) {
                deque.addFirst(args.get(i));
            }
        }
        hoverEvent = component.hoverEvent();
        if (hoverEvent != null) {
            action = hoverEvent.action();
            if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_ENTITY_NAME) && action == HoverEvent.Action.SHOW_ENTITY) {
                deque.addFirst(((HoverEvent.ShowEntity)hoverEvent.value()).name());
            }
            else if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_TEXT_COMPONENT) && action == HoverEvent.Action.SHOW_TEXT) {
                deque.addFirst((Component)hoverEvent.value());
            }
        }
        children = component.children();
        for (j = children.size() - 1; j >= 0; --j) {
            deque.addFirst(children.get(j));
        }
        return;
    };
    public static final ComponentIteratorType BREADTH_FIRST = (component, deque, flags) -> {
        if (flags.contains(ComponentIteratorFlag.INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS) && component instanceof TranslatableComponent) {
            deque.addAll(component.args());
        }
        hoverEvent2 = component.hoverEvent();
        if (hoverEvent2 != null) {
            action2 = hoverEvent2.action();
            if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_ENTITY_NAME) && action2 == HoverEvent.Action.SHOW_ENTITY) {
                deque.addLast(((HoverEvent.ShowEntity)hoverEvent2.value()).name());
            }
            else if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_TEXT_COMPONENT) && action2 == HoverEvent.Action.SHOW_TEXT) {
                deque.addLast((Component)hoverEvent2.value());
            }
        }
        deque.addAll(component.children());
    };
    
    void populate(@NotNull final Component component, @NotNull final Deque<Component> deque, @NotNull final Set<ComponentIteratorFlag> flags);
    
    default static {
        TranslatableComponent translatable;
        List<Component> args;
        int i;
        final HoverEvent<?> hoverEvent;
        HoverEvent.Action<?> action;
        final List<Component> children;
        int j;
        final HoverEvent<?> hoverEvent2;
        HoverEvent.Action<?> action2;
    }
}
