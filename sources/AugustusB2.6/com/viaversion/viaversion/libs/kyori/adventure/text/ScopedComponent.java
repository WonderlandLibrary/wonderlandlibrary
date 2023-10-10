// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import java.util.Set;
import com.viaversion.viaversion.libs.kyori.adventure.util.MonkeyBars;
import java.util.Objects;
import java.util.function.Consumer;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public interface ScopedComponent<C extends Component> extends Component
{
    @NotNull
    C children(@NotNull final List<? extends ComponentLike> children);
    
    @NotNull
    C style(@NotNull final Style style);
    
    @NotNull
    default C style(@NotNull final Consumer<Style.Builder> style) {
        return (C)super.style(style);
    }
    
    @NotNull
    default C style(final Style.Builder style) {
        return (C)super.style(style);
    }
    
    @NotNull
    default C mergeStyle(@NotNull final Component that) {
        return (C)super.mergeStyle(that);
    }
    
    @NotNull
    default C mergeStyle(@NotNull final Component that, final Style.Merge... merges) {
        return (C)super.mergeStyle(that, merges);
    }
    
    @NotNull
    default C append(@NotNull final Component component) {
        if (component == Component.empty()) {
            return (C)this;
        }
        final List<Component> oldChildren = this.children();
        return this.children(MonkeyBars.addOne((List<? extends ComponentLike>)oldChildren, (ComponentLike)Objects.requireNonNull((T)component, "component")));
    }
    
    @NotNull
    default C append(@NotNull final ComponentLike component) {
        return (C)super.append(component);
    }
    
    @NotNull
    default C append(@NotNull final ComponentBuilder<?, ?> builder) {
        return (C)super.append(builder);
    }
    
    @NotNull
    default C mergeStyle(@NotNull final Component that, @NotNull final Set<Style.Merge> merges) {
        return (C)super.mergeStyle(that, merges);
    }
    
    @NotNull
    default C color(@Nullable final TextColor color) {
        return (C)super.color(color);
    }
    
    @NotNull
    default C colorIfAbsent(@Nullable final TextColor color) {
        return (C)super.colorIfAbsent(color);
    }
    
    @NotNull
    default Component decorate(@NotNull final TextDecoration decoration) {
        return super.decorate(decoration);
    }
    
    @NotNull
    default C decoration(@NotNull final TextDecoration decoration, final boolean flag) {
        return (C)super.decoration(decoration, flag);
    }
    
    @NotNull
    default C decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        return (C)super.decoration(decoration, state);
    }
    
    @NotNull
    default C clickEvent(@Nullable final ClickEvent event) {
        return (C)super.clickEvent(event);
    }
    
    @NotNull
    default C hoverEvent(@Nullable final HoverEventSource<?> event) {
        return (C)super.hoverEvent(event);
    }
    
    @NotNull
    default C insertion(@Nullable final String insertion) {
        return (C)super.insertion(insertion);
    }
}
