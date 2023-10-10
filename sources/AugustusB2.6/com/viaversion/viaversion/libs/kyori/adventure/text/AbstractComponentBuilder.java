// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import java.util.Set;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import java.util.function.Function;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.Iterator;
import java.util.Objects;
import java.util.Collection;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import java.util.List;

abstract class AbstractComponentBuilder<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> implements ComponentBuilder<C, B>
{
    protected List<Component> children;
    @Nullable
    private Style style;
    private Style.Builder styleBuilder;
    
    protected AbstractComponentBuilder() {
        this.children = Collections.emptyList();
    }
    
    protected AbstractComponentBuilder(@NotNull final C component) {
        this.children = Collections.emptyList();
        final List<Component> children = component.children();
        if (!children.isEmpty()) {
            this.children = new ArrayList<Component>(children);
        }
        if (component.hasStyling()) {
            this.style = component.style();
        }
    }
    
    @NotNull
    @Override
    public B append(@NotNull final Component component) {
        if (component == Component.empty()) {
            return (B)this;
        }
        this.prepareChildren();
        this.children.add(Objects.requireNonNull(component, "component"));
        return (B)this;
    }
    
    @NotNull
    @Override
    public B append(@NotNull final Component... components) {
        Objects.requireNonNull(components, "components");
        boolean prepared = false;
        for (int i = 0, length = components.length; i < length; ++i) {
            final Component component = components[i];
            if (component != Component.empty()) {
                if (!prepared) {
                    this.prepareChildren();
                    prepared = true;
                }
                this.children.add(Objects.requireNonNull(component, "components[?]"));
            }
        }
        return (B)this;
    }
    
    @NotNull
    @Override
    public B append(@NotNull final ComponentLike... components) {
        Objects.requireNonNull(components, "components");
        boolean prepared = false;
        for (int i = 0, length = components.length; i < length; ++i) {
            final Component component = components[i].asComponent();
            if (component != Component.empty()) {
                if (!prepared) {
                    this.prepareChildren();
                    prepared = true;
                }
                this.children.add(Objects.requireNonNull(component, "components[?]"));
            }
        }
        return (B)this;
    }
    
    @NotNull
    @Override
    public B append(@NotNull final Iterable<? extends ComponentLike> components) {
        Objects.requireNonNull(components, "components");
        boolean prepared = false;
        for (final ComponentLike like : components) {
            final Component component = like.asComponent();
            if (component != Component.empty()) {
                if (!prepared) {
                    this.prepareChildren();
                    prepared = true;
                }
                this.children.add(Objects.requireNonNull(component, "components[?]"));
            }
        }
        return (B)this;
    }
    
    private void prepareChildren() {
        if (this.children == Collections.emptyList()) {
            this.children = new ArrayList<Component>();
        }
    }
    
    @NotNull
    @Override
    public B applyDeep(@NotNull final Consumer<? super ComponentBuilder<?, ?>> consumer) {
        this.apply(consumer);
        if (this.children == Collections.emptyList()) {
            return (B)this;
        }
        final ListIterator<Component> it = this.children.listIterator();
        while (it.hasNext()) {
            final Component child = it.next();
            if (!(child instanceof BuildableComponent)) {
                continue;
            }
            final ComponentBuilder<?, ?> childBuilder = ((BuildableComponent)child).toBuilder();
            childBuilder.applyDeep(consumer);
            it.set((Component)childBuilder.build());
        }
        return (B)this;
    }
    
    @NotNull
    @Override
    public B mapChildren(@NotNull final Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
        if (this.children == Collections.emptyList()) {
            return (B)this;
        }
        final ListIterator<Component> it = this.children.listIterator();
        while (it.hasNext()) {
            final Component child = it.next();
            if (!(child instanceof BuildableComponent)) {
                continue;
            }
            final BuildableComponent<?, ?> mappedChild = Objects.requireNonNull((BuildableComponent<?, ?>)function.apply((BuildableComponent<?, ?>)child), "mappedChild");
            if (child == mappedChild) {
                continue;
            }
            it.set(mappedChild);
        }
        return (B)this;
    }
    
    @NotNull
    @Override
    public B mapChildrenDeep(@NotNull final Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
        if (this.children == Collections.emptyList()) {
            return (B)this;
        }
        final ListIterator<Component> it = this.children.listIterator();
        while (it.hasNext()) {
            final Component child = it.next();
            if (!(child instanceof BuildableComponent)) {
                continue;
            }
            final BuildableComponent<?, ?> mappedChild = Objects.requireNonNull((BuildableComponent<?, ?>)function.apply((BuildableComponent<?, ?>)child), "mappedChild");
            if (mappedChild.children().isEmpty()) {
                if (child == mappedChild) {
                    continue;
                }
                it.set(mappedChild);
            }
            else {
                final ComponentBuilder<?, ?> builder = (ComponentBuilder<?, ?>)mappedChild.toBuilder();
                builder.mapChildrenDeep(function);
                it.set((Component)builder.build());
            }
        }
        return (B)this;
    }
    
    @NotNull
    @Override
    public List<Component> children() {
        return Collections.unmodifiableList((List<? extends Component>)this.children);
    }
    
    @NotNull
    @Override
    public B style(@NotNull final Style style) {
        this.style = style;
        this.styleBuilder = null;
        return (B)this;
    }
    
    @NotNull
    @Override
    public B style(@NotNull final Consumer<Style.Builder> consumer) {
        consumer.accept(this.styleBuilder());
        return (B)this;
    }
    
    @NotNull
    @Override
    public B font(@Nullable final Key font) {
        this.styleBuilder().font(font);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B color(@Nullable final TextColor color) {
        this.styleBuilder().color(color);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B colorIfAbsent(@Nullable final TextColor color) {
        this.styleBuilder().colorIfAbsent(color);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        this.styleBuilder().decoration(decoration, state);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B clickEvent(@Nullable final ClickEvent event) {
        this.styleBuilder().clickEvent(event);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B hoverEvent(@Nullable final HoverEventSource<?> source) {
        this.styleBuilder().hoverEvent(source);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B insertion(@Nullable final String insertion) {
        this.styleBuilder().insertion(insertion);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B mergeStyle(@NotNull final Component that, @NotNull final Set<Style.Merge> merges) {
        this.styleBuilder().merge(that.style(), merges);
        return (B)this;
    }
    
    @NotNull
    @Override
    public B resetStyle() {
        this.style = null;
        this.styleBuilder = null;
        return (B)this;
    }
    
    private Style.Builder styleBuilder() {
        if (this.styleBuilder == null) {
            if (this.style != null) {
                this.styleBuilder = this.style.toBuilder();
                this.style = null;
            }
            else {
                this.styleBuilder = Style.style();
            }
        }
        return this.styleBuilder;
    }
    
    protected final boolean hasStyle() {
        return this.styleBuilder != null || this.style != null;
    }
    
    @NotNull
    protected Style buildStyle() {
        if (this.styleBuilder != null) {
            return this.styleBuilder.build();
        }
        if (this.style != null) {
            return this.style;
        }
        return Style.empty();
    }
}
