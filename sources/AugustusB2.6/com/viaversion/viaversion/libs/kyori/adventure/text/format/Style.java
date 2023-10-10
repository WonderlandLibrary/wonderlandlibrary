// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import org.jetbrains.annotations.Contract;
import com.viaversion.viaversion.libs.kyori.adventure.util.MonkeyBars;
import java.util.Collections;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;

@ApiStatus.NonExtendable
public interface Style extends Buildable<Style, Builder>, Examinable
{
    public static final Key DEFAULT_FONT = Key.key("default");
    
    @NotNull
    default Style empty() {
        return StyleImpl.EMPTY;
    }
    
    @NotNull
    default Builder style() {
        return new StyleImpl.BuilderImpl();
    }
    
    @NotNull
    default Style style(@NotNull final Consumer<Builder> consumer) {
        return Buildable.configureAndBuild(style(), consumer);
    }
    
    @NotNull
    default Style style(@Nullable final TextColor color) {
        if (color == null) {
            return empty();
        }
        return new StyleImpl(null, color, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, null, null, null);
    }
    
    @NotNull
    default Style style(@NotNull final TextDecoration decoration) {
        return style().decoration(decoration, true).build();
    }
    
    @NotNull
    default Style style(@Nullable final TextColor color, final TextDecoration... decorations) {
        final Builder builder = style();
        builder.color(color);
        StyleImpl.decorate(builder, decorations);
        return builder.build();
    }
    
    @NotNull
    default Style style(@Nullable final TextColor color, final Set<TextDecoration> decorations) {
        final Builder builder = style();
        builder.color(color);
        if (!decorations.isEmpty()) {
            for (final TextDecoration decoration : decorations) {
                builder.decoration(decoration, true);
            }
        }
        return builder.build();
    }
    
    @NotNull
    default Style style(final StyleBuilderApplicable... applicables) {
        if (applicables.length == 0) {
            return empty();
        }
        final Builder builder = style();
        for (int i = 0, length = applicables.length; i < length; ++i) {
            applicables[i].styleApply(builder);
        }
        return builder.build();
    }
    
    @NotNull
    default Style style(@NotNull final Iterable<? extends StyleBuilderApplicable> applicables) {
        final Builder builder = style();
        for (final StyleBuilderApplicable applicable : applicables) {
            applicable.styleApply(builder);
        }
        return builder.build();
    }
    
    @NotNull
    default Style edit(@NotNull final Consumer<Builder> consumer) {
        return this.edit(consumer, Merge.Strategy.ALWAYS);
    }
    
    @NotNull
    default Style edit(@NotNull final Consumer<Builder> consumer, final Merge.Strategy strategy) {
        return style(style -> {
            if (strategy == Merge.Strategy.ALWAYS) {
                style.merge(this, strategy);
            }
            consumer.accept(style);
            if (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET) {
                style.merge(this, strategy);
            }
        });
    }
    
    @Nullable
    Key font();
    
    @NotNull
    Style font(@Nullable final Key font);
    
    @Nullable
    TextColor color();
    
    @NotNull
    Style color(@Nullable final TextColor color);
    
    @NotNull
    Style colorIfAbsent(@Nullable final TextColor color);
    
    default boolean hasDecoration(@NotNull final TextDecoration decoration) {
        return this.decoration(decoration) == TextDecoration.State.TRUE;
    }
    
    TextDecoration.State decoration(@NotNull final TextDecoration decoration);
    
    @NotNull
    default Style decorate(@NotNull final TextDecoration decoration) {
        return this.decoration(decoration, TextDecoration.State.TRUE);
    }
    
    @NotNull
    default Style decoration(@NotNull final TextDecoration decoration, final boolean flag) {
        return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
    }
    
    @NotNull
    Style decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state);
    
    @NotNull
    Map<TextDecoration, TextDecoration.State> decorations();
    
    @NotNull
    Style decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations);
    
    @Nullable
    ClickEvent clickEvent();
    
    @NotNull
    Style clickEvent(@Nullable final ClickEvent event);
    
    @Nullable
    HoverEvent<?> hoverEvent();
    
    @NotNull
    Style hoverEvent(@Nullable final HoverEventSource<?> source);
    
    @Nullable
    String insertion();
    
    @NotNull
    Style insertion(@Nullable final String insertion);
    
    @NotNull
    default Style merge(@NotNull final Style that) {
        return this.merge(that, Merge.all());
    }
    
    @NotNull
    default Style merge(@NotNull final Style that, final Merge.Strategy strategy) {
        return this.merge(that, strategy, Merge.all());
    }
    
    @NotNull
    default Style merge(@NotNull final Style that, @NotNull final Merge merge) {
        return this.merge(that, Collections.singleton(merge));
    }
    
    @NotNull
    default Style merge(@NotNull final Style that, final Merge.Strategy strategy, @NotNull final Merge merge) {
        return this.merge(that, strategy, Collections.singleton(merge));
    }
    
    @NotNull
    default Style merge(@NotNull final Style that, @NotNull final Merge... merges) {
        return this.merge(that, Merge.of(merges));
    }
    
    @NotNull
    default Style merge(@NotNull final Style that, final Merge.Strategy strategy, @NotNull final Merge... merges) {
        return this.merge(that, strategy, Merge.of(merges));
    }
    
    @NotNull
    default Style merge(@NotNull final Style that, @NotNull final Set<Merge> merges) {
        return this.merge(that, Merge.Strategy.ALWAYS, merges);
    }
    
    @NotNull
    Style merge(@NotNull final Style that, final Merge.Strategy strategy, @NotNull final Set<Merge> merges);
    
    boolean isEmpty();
    
    @NotNull
    Builder toBuilder();
    
    public enum Merge
    {
        COLOR, 
        DECORATIONS, 
        EVENTS, 
        INSERTION, 
        FONT;
        
        static final Set<Merge> ALL;
        static final Set<Merge> COLOR_AND_DECORATIONS;
        
        @NotNull
        public static Set<Merge> all() {
            return Merge.ALL;
        }
        
        @NotNull
        public static Set<Merge> colorAndDecorations() {
            return Merge.COLOR_AND_DECORATIONS;
        }
        
        @NotNull
        public static Set<Merge> of(final Merge... merges) {
            return MonkeyBars.enumSet(Merge.class, merges);
        }
        
        static boolean hasAll(@NotNull final Set<Merge> merges) {
            return merges.size() == Merge.ALL.size();
        }
        
        static {
            ALL = of(values());
            COLOR_AND_DECORATIONS = of(Merge.COLOR, Merge.DECORATIONS);
        }
        
        public enum Strategy
        {
            ALWAYS, 
            NEVER, 
            IF_ABSENT_ON_TARGET;
        }
    }
    
    public interface Builder extends Buildable.Builder<Style>
    {
        @Contract("_ -> this")
        @NotNull
        Builder font(@Nullable final Key font);
        
        @Contract("_ -> this")
        @NotNull
        Builder color(@Nullable final TextColor color);
        
        @Contract("_ -> this")
        @NotNull
        Builder colorIfAbsent(@Nullable final TextColor color);
        
        @Contract("_ -> this")
        @NotNull
        default Builder decorate(@NotNull final TextDecoration decoration) {
            return this.decoration(decoration, TextDecoration.State.TRUE);
        }
        
        @Contract("_ -> this")
        @NotNull
        default Builder decorate(@NotNull final TextDecoration... decorations) {
            for (int i = 0, length = decorations.length; i < length; ++i) {
                this.decorate(decorations[i]);
            }
            return this;
        }
        
        @Contract("_, _ -> this")
        @NotNull
        default Builder decoration(@NotNull final TextDecoration decoration, final boolean flag) {
            return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
        }
        
        @Contract("_, _ -> this")
        @NotNull
        Builder decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state);
        
        @Contract("_ -> this")
        @NotNull
        Builder clickEvent(@Nullable final ClickEvent event);
        
        @Contract("_ -> this")
        @NotNull
        Builder hoverEvent(@Nullable final HoverEventSource<?> source);
        
        @Contract("_ -> this")
        @NotNull
        Builder insertion(@Nullable final String insertion);
        
        @Contract("_ -> this")
        @NotNull
        default Builder merge(@NotNull final Style that) {
            return this.merge(that, Merge.all());
        }
        
        @Contract("_, _ -> this")
        @NotNull
        default Builder merge(@NotNull final Style that, final Merge.Strategy strategy) {
            return this.merge(that, strategy, Merge.all());
        }
        
        @Contract("_, _ -> this")
        @NotNull
        default Builder merge(@NotNull final Style that, @NotNull final Merge... merges) {
            if (merges.length == 0) {
                return this;
            }
            return this.merge(that, Merge.of(merges));
        }
        
        @Contract("_, _, _ -> this")
        @NotNull
        default Builder merge(@NotNull final Style that, final Merge.Strategy strategy, @NotNull final Merge... merges) {
            if (merges.length == 0) {
                return this;
            }
            return this.merge(that, strategy, Merge.of(merges));
        }
        
        @Contract("_, _ -> this")
        @NotNull
        default Builder merge(@NotNull final Style that, @NotNull final Set<Merge> merges) {
            return this.merge(that, Merge.Strategy.ALWAYS, merges);
        }
        
        @Contract("_, _, _ -> this")
        @NotNull
        Builder merge(@NotNull final Style that, final Merge.Strategy strategy, @NotNull final Set<Merge> merges);
        
        @Contract("_ -> this")
        @NotNull
        default Builder apply(@NotNull final StyleBuilderApplicable applicable) {
            applicable.styleApply(this);
            return this;
        }
        
        @NotNull
        Style build();
    }
}
