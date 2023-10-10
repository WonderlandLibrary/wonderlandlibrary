// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import java.util.regex.MatchResult;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

final class TextReplacementConfigImpl implements TextReplacementConfig
{
    private final Pattern matchPattern;
    private final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement;
    private final Condition continuer;
    
    TextReplacementConfigImpl(final Builder builder) {
        this.matchPattern = builder.matchPattern;
        this.replacement = builder.replacement;
        this.continuer = builder.continuer;
    }
    
    @NotNull
    @Override
    public Pattern matchPattern() {
        return this.matchPattern;
    }
    
    TextReplacementRenderer.State createState() {
        return new TextReplacementRenderer.State(this.matchPattern, this.replacement, this.continuer);
    }
    
    @Override
    public TextReplacementConfig.Builder toBuilder() {
        return new Builder(this);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("matchPattern", this.matchPattern), ExaminableProperty.of("replacement", this.replacement), ExaminableProperty.of("continuer", this.continuer) });
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
    
    static final class Builder implements TextReplacementConfig.Builder
    {
        @Nullable
        Pattern matchPattern;
        @Nullable
        BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement;
        Condition continuer;
        
        Builder() {
            this.continuer = ((matchResult, index, replacement) -> PatternReplacementResult.REPLACE);
        }
        
        Builder(final TextReplacementConfigImpl instance) {
            this.continuer = ((matchResult, index, replacement) -> PatternReplacementResult.REPLACE);
            this.matchPattern = instance.matchPattern;
            this.replacement = instance.replacement;
            this.continuer = instance.continuer;
        }
        
        @NotNull
        @Override
        public Builder match(@NotNull final Pattern pattern) {
            this.matchPattern = Objects.requireNonNull(pattern, "pattern");
            return this;
        }
        
        @NotNull
        @Override
        public Builder condition(final Condition condition) {
            this.continuer = Objects.requireNonNull(condition, "continuation");
            return this;
        }
        
        @NotNull
        @Override
        public Builder replacement(@NotNull final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement) {
            this.replacement = Objects.requireNonNull(replacement, "replacement");
            return this;
        }
        
        @NotNull
        @Override
        public TextReplacementConfig build() {
            if (this.matchPattern == null) {
                throw new IllegalStateException("A pattern must be provided to match against");
            }
            if (this.replacement == null) {
                throw new IllegalStateException("A replacement action must be provided");
            }
            return new TextReplacementConfigImpl(this);
        }
    }
}
