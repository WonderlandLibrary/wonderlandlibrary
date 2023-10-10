// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import org.jetbrains.annotations.Nullable;

final class SelectorComponentImpl extends AbstractComponent implements SelectorComponent
{
    private final String pattern;
    @Nullable
    private final Component separator;
    
    SelectorComponentImpl(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String pattern, @Nullable final ComponentLike separator) {
        super(children, style);
        this.pattern = pattern;
        this.separator = ComponentLike.unbox(separator);
    }
    
    @NotNull
    @Override
    public String pattern() {
        return this.pattern;
    }
    
    @NotNull
    @Override
    public SelectorComponent pattern(@NotNull final String pattern) {
        if (Objects.equals(this.pattern, pattern)) {
            return this;
        }
        return new SelectorComponentImpl(this.children, this.style, Objects.requireNonNull(pattern, "pattern"), this.separator);
    }
    
    @Nullable
    @Override
    public Component separator() {
        return this.separator;
    }
    
    @NotNull
    @Override
    public SelectorComponent separator(@Nullable final ComponentLike separator) {
        return new SelectorComponentImpl(this.children, this.style, this.pattern, separator);
    }
    
    @NotNull
    @Override
    public SelectorComponent children(@NotNull final List<? extends ComponentLike> children) {
        return new SelectorComponentImpl(children, this.style, this.pattern, this.separator);
    }
    
    @NotNull
    @Override
    public SelectorComponent style(@NotNull final Style style) {
        return new SelectorComponentImpl(this.children, style, this.pattern, this.separator);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SelectorComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final SelectorComponent that = (SelectorComponent)other;
        return Objects.equals(this.pattern, that.pattern()) && Objects.equals(this.separator, that.separator());
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.pattern.hashCode();
        result = 31 * result + Objects.hashCode(this.separator);
        return result;
    }
    
    @NotNull
    @Override
    protected Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren() {
        return Stream.concat((Stream<? extends ExaminableProperty>)Stream.of((T[])new ExaminableProperty[] { ExaminableProperty.of("pattern", this.pattern), ExaminableProperty.of("separator", this.separator) }), super.examinablePropertiesWithoutChildren());
    }
    
    @NotNull
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static final class BuilderImpl extends AbstractComponentBuilder<SelectorComponent, SelectorComponent.Builder> implements SelectorComponent.Builder
    {
        @Nullable
        private String pattern;
        @Nullable
        private Component separator;
        
        BuilderImpl() {
        }
        
        BuilderImpl(@NotNull final SelectorComponent component) {
            super(component);
            this.pattern = component.pattern();
        }
        
        @NotNull
        @Override
        public SelectorComponent.Builder pattern(@NotNull final String pattern) {
            this.pattern = pattern;
            return this;
        }
        
        @NotNull
        @Override
        public SelectorComponent.Builder separator(@Nullable final ComponentLike separator) {
            this.separator = ComponentLike.unbox(separator);
            return this;
        }
        
        @NotNull
        @Override
        public SelectorComponent build() {
            if (this.pattern == null) {
                throw new IllegalStateException("pattern must be set");
            }
            return new SelectorComponentImpl(this.children, this.buildStyle(), this.pattern, this.separator);
        }
    }
}
