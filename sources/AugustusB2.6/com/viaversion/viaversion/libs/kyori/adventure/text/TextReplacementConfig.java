// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import java.util.regex.MatchResult;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.util.IntFunction2;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.Contract;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;

public interface TextReplacementConfig extends Buildable<TextReplacementConfig, Builder>, Examinable
{
    @NotNull
    default Builder builder() {
        return new TextReplacementConfigImpl.Builder();
    }
    
    @NotNull
    Pattern matchPattern();
    
    public interface Builder extends Buildable.Builder<TextReplacementConfig>
    {
        @Contract("_ -> this")
        default Builder matchLiteral(final String literal) {
            return this.match(Pattern.compile(literal, 16));
        }
        
        @Contract("_ -> this")
        @NotNull
        default Builder match(@NotNull @RegExp final String pattern) {
            return this.match(Pattern.compile(pattern));
        }
        
        @Contract("_ -> this")
        @NotNull
        Builder match(@NotNull final Pattern pattern);
        
        @NotNull
        default Builder once() {
            return this.times(1);
        }
        
        @Contract("_ -> this")
        @NotNull
        default Builder times(final int times) {
            return this.condition((index, replaced) -> (replaced < times) ? PatternReplacementResult.REPLACE : PatternReplacementResult.STOP);
        }
        
        @Contract("_ -> this")
        @NotNull
        default Builder condition(@NotNull final IntFunction2<PatternReplacementResult> condition) {
            return this.condition((result, matchCount, replaced) -> condition.apply(matchCount, replaced));
        }
        
        @Contract("_ -> this")
        @NotNull
        Builder condition(@NotNull final Condition condition);
        
        @Contract("_ -> this")
        @NotNull
        default Builder replacement(@NotNull final String replacement) {
            Objects.requireNonNull(replacement, "replacement");
            return this.replacement(builder -> builder.content(replacement));
        }
        
        @Contract("_ -> this")
        @NotNull
        default Builder replacement(@Nullable final ComponentLike replacement) {
            final Component baked = (replacement == null) ? null : replacement.asComponent();
            return this.replacement((result, input) -> baked);
        }
        
        @Contract("_ -> this")
        @NotNull
        default Builder replacement(@NotNull final Function<TextComponent.Builder, ComponentLike> replacement) {
            Objects.requireNonNull(replacement, "replacement");
            return this.replacement((result, input) -> replacement.apply(input));
        }
        
        @Contract("_ -> this")
        @NotNull
        Builder replacement(@NotNull final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement);
    }
    
    @FunctionalInterface
    public interface Condition
    {
        @NotNull
        PatternReplacementResult shouldReplace(@NotNull final MatchResult result, final int matchCount, final int replaced);
    }
}
