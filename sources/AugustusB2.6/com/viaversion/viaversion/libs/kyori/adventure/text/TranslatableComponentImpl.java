// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Collections;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.Arrays;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import java.util.List;

final class TranslatableComponentImpl extends AbstractComponent implements TranslatableComponent
{
    private final String key;
    private final List<Component> args;
    
    TranslatableComponentImpl(@NotNull final List<Component> children, @NotNull final Style style, @NotNull final String key, @NotNull final ComponentLike[] args) {
        this(children, style, key, Arrays.asList(args));
    }
    
    TranslatableComponentImpl(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String key, @NotNull final List<? extends ComponentLike> args) {
        super(children, style);
        this.key = Objects.requireNonNull(key, "key");
        this.args = ComponentLike.asComponents(args);
    }
    
    @NotNull
    @Override
    public String key() {
        return this.key;
    }
    
    @NotNull
    @Override
    public TranslatableComponent key(@NotNull final String key) {
        if (Objects.equals(this.key, key)) {
            return this;
        }
        return new TranslatableComponentImpl(this.children, this.style, Objects.requireNonNull(key, "key"), this.args);
    }
    
    @NotNull
    @Override
    public List<Component> args() {
        return this.args;
    }
    
    @NotNull
    @Override
    public TranslatableComponent args(@NotNull final ComponentLike... args) {
        return new TranslatableComponentImpl(this.children, this.style, this.key, args);
    }
    
    @NotNull
    @Override
    public TranslatableComponent args(@NotNull final List<? extends ComponentLike> args) {
        return new TranslatableComponentImpl(this.children, this.style, this.key, args);
    }
    
    @NotNull
    @Override
    public TranslatableComponent children(@NotNull final List<? extends ComponentLike> children) {
        return new TranslatableComponentImpl(children, this.style, this.key, this.args);
    }
    
    @NotNull
    @Override
    public TranslatableComponent style(@NotNull final Style style) {
        return new TranslatableComponentImpl(this.children, style, this.key, this.args);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TranslatableComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final TranslatableComponent that = (TranslatableComponent)other;
        return Objects.equals(this.key, that.key()) && Objects.equals(this.args, that.args());
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.key.hashCode();
        result = 31 * result + this.args.hashCode();
        return result;
    }
    
    @NotNull
    @Override
    protected Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren() {
        return Stream.concat((Stream<? extends ExaminableProperty>)Stream.of((T[])new ExaminableProperty[] { ExaminableProperty.of("key", this.key), ExaminableProperty.of("args", this.args) }), super.examinablePropertiesWithoutChildren());
    }
    
    @NotNull
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static final class BuilderImpl extends AbstractComponentBuilder<TranslatableComponent, TranslatableComponent.Builder> implements TranslatableComponent.Builder
    {
        @Nullable
        private String key;
        private List<? extends Component> args;
        
        BuilderImpl() {
            this.args = Collections.emptyList();
        }
        
        BuilderImpl(@NotNull final TranslatableComponent component) {
            super(component);
            this.args = Collections.emptyList();
            this.key = component.key();
            this.args = component.args();
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder key(@NotNull final String key) {
            this.key = key;
            return this;
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final ComponentBuilder<?, ?> arg) {
            return this.args((List<? extends ComponentLike>)Collections.singletonList(arg.build()));
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final ComponentBuilder<?, ?>... args) {
            if (args.length == 0) {
                return this.args(Collections.emptyList());
            }
            return this.args((List<? extends ComponentLike>)Stream.of(args).map((Function<? super ComponentBuilder<?, ?>, ?>)ComponentBuilder::build).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final Component arg) {
            return this.args(Collections.singletonList(arg));
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final ComponentLike... args) {
            if (args.length == 0) {
                return this.args(Collections.emptyList());
            }
            return this.args(Arrays.asList(args));
        }
        
        @NotNull
        @Override
        public TranslatableComponent.Builder args(@NotNull final List<? extends ComponentLike> args) {
            this.args = ComponentLike.asComponents(args);
            return this;
        }
        
        @NotNull
        @Override
        public TranslatableComponentImpl build() {
            if (this.key == null) {
                throw new IllegalStateException("key must be set");
            }
            return new TranslatableComponentImpl(this.children, this.buildStyle(), this.key, this.args);
        }
    }
}
