// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.format;

import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Set;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
import java.util.EnumMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;

final class StyleImpl implements Style
{
    static final StyleImpl EMPTY;
    private static final TextDecoration[] DECORATIONS;
    @Nullable
    final Key font;
    @Nullable
    final TextColor color;
    final TextDecoration.State obfuscated;
    final TextDecoration.State bold;
    final TextDecoration.State strikethrough;
    final TextDecoration.State underlined;
    final TextDecoration.State italic;
    @Nullable
    final ClickEvent clickEvent;
    @Nullable
    final HoverEvent<?> hoverEvent;
    @Nullable
    final String insertion;
    
    static void decorate(final Builder builder, final TextDecoration[] decorations) {
        for (int i = 0, length = decorations.length; i < length; ++i) {
            final TextDecoration decoration = decorations[i];
            builder.decoration(decoration, true);
        }
    }
    
    StyleImpl(@Nullable final Key font, @Nullable final TextColor color, final TextDecoration.State obfuscated, final TextDecoration.State bold, final TextDecoration.State strikethrough, final TextDecoration.State underlined, final TextDecoration.State italic, @Nullable final ClickEvent clickEvent, @Nullable final HoverEvent<?> hoverEvent, @Nullable final String insertion) {
        this.font = font;
        this.color = color;
        this.obfuscated = obfuscated;
        this.bold = bold;
        this.strikethrough = strikethrough;
        this.underlined = underlined;
        this.italic = italic;
        this.clickEvent = clickEvent;
        this.hoverEvent = hoverEvent;
        this.insertion = insertion;
    }
    
    @Nullable
    @Override
    public Key font() {
        return this.font;
    }
    
    @NotNull
    @Override
    public Style font(@Nullable final Key font) {
        if (Objects.equals(this.font, font)) {
            return this;
        }
        return new StyleImpl(font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
    }
    
    @Nullable
    @Override
    public TextColor color() {
        return this.color;
    }
    
    @NotNull
    @Override
    public Style color(@Nullable final TextColor color) {
        if (Objects.equals(this.color, color)) {
            return this;
        }
        return new StyleImpl(this.font, color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
    }
    
    @NotNull
    @Override
    public Style colorIfAbsent(@Nullable final TextColor color) {
        if (this.color == null) {
            return this.color(color);
        }
        return this;
    }
    
    @Override
    public TextDecoration.State decoration(@NotNull final TextDecoration decoration) {
        if (decoration == TextDecoration.BOLD) {
            return this.bold;
        }
        if (decoration == TextDecoration.ITALIC) {
            return this.italic;
        }
        if (decoration == TextDecoration.UNDERLINED) {
            return this.underlined;
        }
        if (decoration == TextDecoration.STRIKETHROUGH) {
            return this.strikethrough;
        }
        if (decoration == TextDecoration.OBFUSCATED) {
            return this.obfuscated;
        }
        throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
    
    @NotNull
    @Override
    public Style decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        Objects.requireNonNull(state, "state");
        if (decoration == TextDecoration.BOLD) {
            return new StyleImpl(this.font, this.color, this.obfuscated, state, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
        }
        if (decoration == TextDecoration.ITALIC) {
            return new StyleImpl(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, state, this.clickEvent, this.hoverEvent, this.insertion);
        }
        if (decoration == TextDecoration.UNDERLINED) {
            return new StyleImpl(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, state, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
        }
        if (decoration == TextDecoration.STRIKETHROUGH) {
            return new StyleImpl(this.font, this.color, this.obfuscated, this.bold, state, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
        }
        if (decoration == TextDecoration.OBFUSCATED) {
            return new StyleImpl(this.font, this.color, state, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
        }
        throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
    }
    
    @NotNull
    @Override
    public Map<TextDecoration, TextDecoration.State> decorations() {
        final Map<TextDecoration, TextDecoration.State> decorations = new EnumMap<TextDecoration, TextDecoration.State>(TextDecoration.class);
        for (int i = 0, length = StyleImpl.DECORATIONS.length; i < length; ++i) {
            final TextDecoration decoration = StyleImpl.DECORATIONS[i];
            final TextDecoration.State value = this.decoration(decoration);
            decorations.put(decoration, value);
        }
        return decorations;
    }
    
    @NotNull
    @Override
    public Style decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations) {
        final TextDecoration.State obfuscated = decorations.getOrDefault(TextDecoration.OBFUSCATED, this.obfuscated);
        final TextDecoration.State bold = decorations.getOrDefault(TextDecoration.BOLD, this.bold);
        final TextDecoration.State strikethrough = decorations.getOrDefault(TextDecoration.STRIKETHROUGH, this.strikethrough);
        final TextDecoration.State underlined = decorations.getOrDefault(TextDecoration.UNDERLINED, this.underlined);
        final TextDecoration.State italic = decorations.getOrDefault(TextDecoration.ITALIC, this.italic);
        return new StyleImpl(this.font, this.color, obfuscated, bold, strikethrough, underlined, italic, this.clickEvent, this.hoverEvent, this.insertion);
    }
    
    @Nullable
    @Override
    public ClickEvent clickEvent() {
        return this.clickEvent;
    }
    
    @NotNull
    @Override
    public Style clickEvent(@Nullable final ClickEvent event) {
        return new StyleImpl(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, event, this.hoverEvent, this.insertion);
    }
    
    @Nullable
    @Override
    public HoverEvent<?> hoverEvent() {
        return this.hoverEvent;
    }
    
    @NotNull
    @Override
    public Style hoverEvent(@Nullable final HoverEventSource<?> source) {
        return new StyleImpl(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, HoverEventSource.unbox(source), this.insertion);
    }
    
    @Nullable
    @Override
    public String insertion() {
        return this.insertion;
    }
    
    @NotNull
    @Override
    public Style insertion(@Nullable final String insertion) {
        if (Objects.equals(this.insertion, insertion)) {
            return this;
        }
        return new StyleImpl(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, insertion);
    }
    
    @NotNull
    @Override
    public Style merge(@NotNull final Style that, final Merge.Strategy strategy, @NotNull final Set<Merge> merges) {
        if (that.isEmpty() || strategy == Merge.Strategy.NEVER || merges.isEmpty()) {
            return this;
        }
        if (this.isEmpty() && Merge.hasAll(merges)) {
            return that;
        }
        final Builder builder = this.toBuilder();
        builder.merge(that, strategy, merges);
        return builder.build();
    }
    
    @Override
    public boolean isEmpty() {
        return this == StyleImpl.EMPTY;
    }
    
    @NotNull
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("color", this.color), ExaminableProperty.of("obfuscated", this.obfuscated), ExaminableProperty.of("bold", this.bold), ExaminableProperty.of("strikethrough", this.strikethrough), ExaminableProperty.of("underlined", this.underlined), ExaminableProperty.of("italic", this.italic), ExaminableProperty.of("clickEvent", this.clickEvent), ExaminableProperty.of("hoverEvent", this.hoverEvent), ExaminableProperty.of("insertion", this.insertion), ExaminableProperty.of("font", this.font) });
    }
    
    @NotNull
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StyleImpl)) {
            return false;
        }
        final StyleImpl that = (StyleImpl)other;
        return Objects.equals(this.color, that.color) && this.obfuscated == that.obfuscated && this.bold == that.bold && this.strikethrough == that.strikethrough && this.underlined == that.underlined && this.italic == that.italic && Objects.equals(this.clickEvent, that.clickEvent) && Objects.equals(this.hoverEvent, that.hoverEvent) && Objects.equals(this.insertion, that.insertion) && Objects.equals(this.font, that.font);
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hashCode(this.color);
        result = 31 * result + this.obfuscated.hashCode();
        result = 31 * result + this.bold.hashCode();
        result = 31 * result + this.strikethrough.hashCode();
        result = 31 * result + this.underlined.hashCode();
        result = 31 * result + this.italic.hashCode();
        result = 31 * result + Objects.hashCode(this.clickEvent);
        result = 31 * result + Objects.hashCode(this.hoverEvent);
        result = 31 * result + Objects.hashCode(this.insertion);
        result = 31 * result + Objects.hashCode(this.font);
        return result;
    }
    
    static {
        EMPTY = new StyleImpl(null, null, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, TextDecoration.State.NOT_SET, null, null, null);
        DECORATIONS = TextDecoration.values();
    }
    
    static final class BuilderImpl implements Builder
    {
        @Nullable
        Key font;
        @Nullable
        TextColor color;
        TextDecoration.State obfuscated;
        TextDecoration.State bold;
        TextDecoration.State strikethrough;
        TextDecoration.State underlined;
        TextDecoration.State italic;
        @Nullable
        ClickEvent clickEvent;
        @Nullable
        HoverEvent<?> hoverEvent;
        @Nullable
        String insertion;
        
        BuilderImpl() {
            this.obfuscated = TextDecoration.State.NOT_SET;
            this.bold = TextDecoration.State.NOT_SET;
            this.strikethrough = TextDecoration.State.NOT_SET;
            this.underlined = TextDecoration.State.NOT_SET;
            this.italic = TextDecoration.State.NOT_SET;
        }
        
        BuilderImpl(@NotNull final StyleImpl style) {
            this.obfuscated = TextDecoration.State.NOT_SET;
            this.bold = TextDecoration.State.NOT_SET;
            this.strikethrough = TextDecoration.State.NOT_SET;
            this.underlined = TextDecoration.State.NOT_SET;
            this.italic = TextDecoration.State.NOT_SET;
            this.color = style.color;
            this.obfuscated = style.obfuscated;
            this.bold = style.bold;
            this.strikethrough = style.strikethrough;
            this.underlined = style.underlined;
            this.italic = style.italic;
            this.clickEvent = style.clickEvent;
            this.hoverEvent = style.hoverEvent;
            this.insertion = style.insertion;
            this.font = style.font;
        }
        
        @NotNull
        @Override
        public Builder font(@Nullable final Key font) {
            this.font = font;
            return this;
        }
        
        @NotNull
        @Override
        public Builder color(@Nullable final TextColor color) {
            this.color = color;
            return this;
        }
        
        @NotNull
        @Override
        public Builder colorIfAbsent(@Nullable final TextColor color) {
            if (this.color == null) {
                this.color = color;
            }
            return this;
        }
        
        @NotNull
        @Override
        public Builder decorate(@NotNull final TextDecoration decoration) {
            return this.decoration(decoration, TextDecoration.State.TRUE);
        }
        
        @NotNull
        @Override
        public Builder decorate(@NotNull final TextDecoration... decorations) {
            for (int i = 0, length = decorations.length; i < length; ++i) {
                this.decorate(decorations[i]);
            }
            return this;
        }
        
        @NotNull
        @Override
        public Builder decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
            Objects.requireNonNull(state, "state");
            if (decoration == TextDecoration.BOLD) {
                this.bold = state;
                return this;
            }
            if (decoration == TextDecoration.ITALIC) {
                this.italic = state;
                return this;
            }
            if (decoration == TextDecoration.UNDERLINED) {
                this.underlined = state;
                return this;
            }
            if (decoration == TextDecoration.STRIKETHROUGH) {
                this.strikethrough = state;
                return this;
            }
            if (decoration == TextDecoration.OBFUSCATED) {
                this.obfuscated = state;
                return this;
            }
            throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
        }
        
        @NotNull
        Builder decorationIfAbsent(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
            Objects.requireNonNull(state, "state");
            if (decoration == TextDecoration.BOLD) {
                if (this.bold == TextDecoration.State.NOT_SET) {
                    this.bold = state;
                }
                return this;
            }
            if (decoration == TextDecoration.ITALIC) {
                if (this.italic == TextDecoration.State.NOT_SET) {
                    this.italic = state;
                }
                return this;
            }
            if (decoration == TextDecoration.UNDERLINED) {
                if (this.underlined == TextDecoration.State.NOT_SET) {
                    this.underlined = state;
                }
                return this;
            }
            if (decoration == TextDecoration.STRIKETHROUGH) {
                if (this.strikethrough == TextDecoration.State.NOT_SET) {
                    this.strikethrough = state;
                }
                return this;
            }
            if (decoration == TextDecoration.OBFUSCATED) {
                if (this.obfuscated == TextDecoration.State.NOT_SET) {
                    this.obfuscated = state;
                }
                return this;
            }
            throw new IllegalArgumentException(String.format("unknown decoration '%s'", decoration));
        }
        
        @NotNull
        @Override
        public Builder clickEvent(@Nullable final ClickEvent event) {
            this.clickEvent = event;
            return this;
        }
        
        @NotNull
        @Override
        public Builder hoverEvent(@Nullable final HoverEventSource<?> source) {
            this.hoverEvent = HoverEventSource.unbox(source);
            return this;
        }
        
        @NotNull
        @Override
        public Builder insertion(@Nullable final String insertion) {
            this.insertion = insertion;
            return this;
        }
        
        @NotNull
        @Override
        public Builder merge(@NotNull final Style that, final Merge.Strategy strategy, @NotNull final Set<Merge> merges) {
            if (strategy == Merge.Strategy.NEVER || that.isEmpty() || merges.isEmpty()) {
                return this;
            }
            final Merger merger = merger(strategy);
            if (merges.contains(Merge.COLOR)) {
                final TextColor color = that.color();
                if (color != null) {
                    merger.mergeColor(this, color);
                }
            }
            if (merges.contains(Merge.DECORATIONS)) {
                for (int i = 0, length = StyleImpl.DECORATIONS.length; i < length; ++i) {
                    final TextDecoration decoration = StyleImpl.DECORATIONS[i];
                    final TextDecoration.State state = that.decoration(decoration);
                    if (state != TextDecoration.State.NOT_SET) {
                        merger.mergeDecoration(this, decoration, state);
                    }
                }
            }
            if (merges.contains(Merge.EVENTS)) {
                final ClickEvent clickEvent = that.clickEvent();
                if (clickEvent != null) {
                    merger.mergeClickEvent(this, clickEvent);
                }
                final HoverEvent<?> hoverEvent = that.hoverEvent();
                if (hoverEvent != null) {
                    merger.mergeHoverEvent(this, hoverEvent);
                }
            }
            if (merges.contains(Merge.INSERTION)) {
                final String insertion = that.insertion();
                if (insertion != null) {
                    merger.mergeInsertion(this, insertion);
                }
            }
            if (merges.contains(Merge.FONT)) {
                final Key font = that.font();
                if (font != null) {
                    merger.mergeFont(this, font);
                }
            }
            return this;
        }
        
        private static Merger merger(final Merge.Strategy strategy) {
            if (strategy == Merge.Strategy.ALWAYS) {
                return AlwaysMerger.INSTANCE;
            }
            if (strategy == Merge.Strategy.NEVER) {
                throw new UnsupportedOperationException();
            }
            if (strategy == Merge.Strategy.IF_ABSENT_ON_TARGET) {
                return IfAbsentOnTargetMerger.INSTANCE;
            }
            throw new IllegalArgumentException(strategy.name());
        }
        
        @NotNull
        @Override
        public StyleImpl build() {
            if (this.isEmpty()) {
                return StyleImpl.EMPTY;
            }
            return new StyleImpl(this.font, this.color, this.obfuscated, this.bold, this.strikethrough, this.underlined, this.italic, this.clickEvent, this.hoverEvent, this.insertion);
        }
        
        private boolean isEmpty() {
            return this.color == null && this.obfuscated == TextDecoration.State.NOT_SET && this.bold == TextDecoration.State.NOT_SET && this.strikethrough == TextDecoration.State.NOT_SET && this.underlined == TextDecoration.State.NOT_SET && this.italic == TextDecoration.State.NOT_SET && this.clickEvent == null && this.hoverEvent == null && this.insertion == null && this.font == null;
        }
    }
}
