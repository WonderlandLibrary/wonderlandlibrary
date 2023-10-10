// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.flattener;

import com.viaversion.viaversion.libs.kyori.adventure.text.TranslatableComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.TextComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.SelectorComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.ScoreComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.KeybindComponent;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import java.util.Iterator;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;
import java.util.concurrent.ConcurrentMap;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.Map;

final class ComponentFlattenerImpl implements ComponentFlattener
{
    static final ComponentFlattener BASIC;
    static final ComponentFlattener TEXT_ONLY;
    private static final int MAX_DEPTH = 512;
    private final Map<Class<?>, Function<?, String>> flatteners;
    private final Map<Class<?>, BiConsumer<?, Consumer<Component>>> complexFlatteners;
    private final ConcurrentMap<Class<?>, Handler> propagatedFlatteners;
    private final Function<Component, String> unknownHandler;
    
    ComponentFlattenerImpl(final Map<Class<?>, Function<?, String>> flatteners, final Map<Class<?>, BiConsumer<?, Consumer<Component>>> complexFlatteners, @Nullable final Function<Component, String> unknownHandler) {
        this.propagatedFlatteners = new ConcurrentHashMap<Class<?>, Handler>();
        this.flatteners = Collections.unmodifiableMap((Map<? extends Class<?>, ? extends Function<?, String>>)new HashMap<Class<?>, Function<?, String>>(flatteners));
        this.complexFlatteners = Collections.unmodifiableMap((Map<? extends Class<?>, ? extends BiConsumer<?, Consumer<Component>>>)new HashMap<Class<?>, BiConsumer<?, Consumer<Component>>>(complexFlatteners));
        this.unknownHandler = unknownHandler;
    }
    
    @Override
    public void flatten(@NotNull final Component input, @NotNull final FlattenerListener listener) {
        this.flatten0(input, listener, 0);
    }
    
    private void flatten0(@NotNull final Component input, @NotNull final FlattenerListener listener, final int depth) {
        Objects.requireNonNull(input, "input");
        Objects.requireNonNull(listener, "listener");
        if (input == Component.empty()) {
            return;
        }
        if (depth > 512) {
            throw new IllegalStateException("Exceeded maximum depth of 512 while attempting to flatten components!");
        }
        final Handler flattener = this.flattener(input);
        final Style inputStyle = input.style();
        listener.pushStyle(inputStyle);
        try {
            if (flattener != null) {
                flattener.handle(input, listener, depth + 1);
            }
            if (!input.children().isEmpty()) {
                for (final Component child : input.children()) {
                    this.flatten0(child, listener, depth + 1);
                }
            }
        }
        finally {
            listener.popStyle(inputStyle);
        }
    }
    
    @Nullable
    private <T extends Component> Handler flattener(final T test) {
        final Function<Component, String> value;
        final Iterator<Map.Entry<Class<?>, Function<?, String>>> iterator;
        Map.Entry<Class<?>, Function<?, String>> entry;
        BiConsumer<Component, Consumer<Component>> complexValue;
        final Iterator<Map.Entry<Class<?>, BiConsumer<?, Consumer<Component>>>> iterator2;
        Map.Entry<Class<?>, BiConsumer<?, Consumer<Component>>> entry2;
        final Handler flattener = this.propagatedFlatteners.computeIfAbsent(test.getClass(), key -> {
            value = (Function<Component, String>)this.flatteners.get(key);
            if (value != null) {
                return (component, listener, depth) -> listener.component(value.apply(component));
            }
            else {
                this.flatteners.entrySet().iterator();
                while (iterator.hasNext()) {
                    entry = iterator.next();
                    if (entry.getKey().isAssignableFrom(key)) {
                        return (component, listener, depth) -> listener.component(entry.getValue().apply(component));
                    }
                }
                complexValue = (BiConsumer<Component, Consumer<Component>>)this.complexFlatteners.get(key);
                if (complexValue != null) {
                    return (component, listener, depth) -> complexValue.accept(component, c -> this.flatten0(c, listener, depth));
                }
                else {
                    this.complexFlatteners.entrySet().iterator();
                    while (iterator2.hasNext()) {
                        entry2 = iterator2.next();
                        if (entry2.getKey().isAssignableFrom(key)) {
                            return (component, listener, depth) -> entry2.getValue().accept(component, c -> this.flatten0(c, listener, depth));
                        }
                    }
                    return Handler.NONE;
                }
            }
        });
        if (flattener == Handler.NONE) {
            return (this.unknownHandler == null) ? null : ((component, listener, depth) -> this.unknownHandler.apply(component));
        }
        return flattener;
    }
    
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this.flatteners, this.complexFlatteners, this.unknownHandler);
    }
    
    static {
        BASIC = new BuilderImpl().mapper(KeybindComponent.class, component -> component.keybind()).mapper(ScoreComponent.class, ScoreComponent::value).mapper(SelectorComponent.class, SelectorComponent::pattern).mapper(TextComponent.class, TextComponent::content).mapper(TranslatableComponent.class, TranslatableComponent::key).build();
        TEXT_ONLY = new BuilderImpl().mapper(TextComponent.class, TextComponent::content).build();
    }
    
    @FunctionalInterface
    interface Handler
    {
        public static final Handler NONE = (input, listener, depth) -> {};
        
        void handle(final Component input, final FlattenerListener listener, final int depth);
    }
    
    static final class BuilderImpl implements Builder
    {
        private final Map<Class<?>, Function<?, String>> flatteners;
        private final Map<Class<?>, BiConsumer<?, Consumer<Component>>> complexFlatteners;
        @Nullable
        private Function<Component, String> unknownHandler;
        
        BuilderImpl() {
            this.flatteners = new HashMap<Class<?>, Function<?, String>>();
            this.complexFlatteners = new HashMap<Class<?>, BiConsumer<?, Consumer<Component>>>();
        }
        
        BuilderImpl(final Map<Class<?>, Function<?, String>> flatteners, final Map<Class<?>, BiConsumer<?, Consumer<Component>>> complexFlatteners, @Nullable final Function<Component, String> unknownHandler) {
            this.flatteners = new HashMap<Class<?>, Function<?, String>>(flatteners);
            this.complexFlatteners = new HashMap<Class<?>, BiConsumer<?, Consumer<Component>>>(complexFlatteners);
            this.unknownHandler = unknownHandler;
        }
        
        @NotNull
        @Override
        public ComponentFlattener build() {
            return new ComponentFlattenerImpl(this.flatteners, this.complexFlatteners, this.unknownHandler);
        }
        
        @Override
        public <T extends Component> Builder mapper(@NotNull final Class<T> type, @NotNull final Function<T, String> converter) {
            this.validateNoneInHierarchy(Objects.requireNonNull(type, "type"));
            this.flatteners.put(type, Objects.requireNonNull(converter, "converter"));
            this.complexFlatteners.remove(type);
            return this;
        }
        
        @Override
        public <T extends Component> Builder complexMapper(@NotNull final Class<T> type, @NotNull final BiConsumer<T, Consumer<Component>> converter) {
            this.validateNoneInHierarchy(Objects.requireNonNull(type, "type"));
            this.complexFlatteners.put(type, Objects.requireNonNull(converter, "converter"));
            this.flatteners.remove(type);
            return this;
        }
        
        private void validateNoneInHierarchy(final Class<? extends Component> beingRegistered) {
            for (final Class<?> clazz : this.flatteners.keySet()) {
                testHierarchy(clazz, beingRegistered);
            }
            for (final Class<?> clazz : this.complexFlatteners.keySet()) {
                testHierarchy(clazz, beingRegistered);
            }
        }
        
        private static void testHierarchy(final Class<?> existing, final Class<?> beingRegistered) {
            if (!existing.equals(beingRegistered) && (existing.isAssignableFrom(beingRegistered) || beingRegistered.isAssignableFrom(existing))) {
                throw new IllegalArgumentException("Conflict detected between already registered type " + existing + " and newly registered type " + beingRegistered + "! Types in a component flattener must not share a common hierachy!");
            }
        }
        
        @Override
        public Builder unknownMapper(@Nullable final Function<Component, String> converter) {
            this.unknownHandler = converter;
            return this;
        }
    }
}
