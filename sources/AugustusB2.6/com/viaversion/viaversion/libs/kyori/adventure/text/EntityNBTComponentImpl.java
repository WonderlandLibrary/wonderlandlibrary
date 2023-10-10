// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import java.util.List;

final class EntityNBTComponentImpl extends NBTComponentImpl<EntityNBTComponent, EntityNBTComponent.Builder> implements EntityNBTComponent
{
    private final String selector;
    
    EntityNBTComponentImpl(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final ComponentLike separator, final String selector) {
        super(children, style, nbtPath, interpret, separator);
        this.selector = selector;
    }
    
    @NotNull
    @Override
    public EntityNBTComponent nbtPath(@NotNull final String nbtPath) {
        if (Objects.equals(this.nbtPath, nbtPath)) {
            return this;
        }
        return new EntityNBTComponentImpl(this.children, this.style, nbtPath, this.interpret, this.separator, this.selector);
    }
    
    @NotNull
    @Override
    public EntityNBTComponent interpret(final boolean interpret) {
        if (this.interpret == interpret) {
            return this;
        }
        return new EntityNBTComponentImpl(this.children, this.style, this.nbtPath, interpret, this.separator, this.selector);
    }
    
    @Nullable
    @Override
    public Component separator() {
        return this.separator;
    }
    
    @NotNull
    @Override
    public EntityNBTComponent separator(@Nullable final ComponentLike separator) {
        return new EntityNBTComponentImpl(this.children, this.style, this.nbtPath, this.interpret, separator, this.selector);
    }
    
    @NotNull
    @Override
    public String selector() {
        return this.selector;
    }
    
    @NotNull
    @Override
    public EntityNBTComponent selector(@NotNull final String selector) {
        if (Objects.equals(this.selector, selector)) {
            return this;
        }
        return new EntityNBTComponentImpl(this.children, this.style, this.nbtPath, this.interpret, this.separator, selector);
    }
    
    @NotNull
    @Override
    public EntityNBTComponent children(@NotNull final List<? extends ComponentLike> children) {
        return new EntityNBTComponentImpl(children, this.style, this.nbtPath, this.interpret, this.separator, this.selector);
    }
    
    @NotNull
    @Override
    public EntityNBTComponent style(@NotNull final Style style) {
        return new EntityNBTComponentImpl(this.children, style, this.nbtPath, this.interpret, this.separator, this.selector);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EntityNBTComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final EntityNBTComponentImpl that = (EntityNBTComponentImpl)other;
        return Objects.equals(this.selector, that.selector());
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.selector.hashCode();
        return result;
    }
    
    @NotNull
    @Override
    protected Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren() {
        return Stream.concat((Stream<? extends ExaminableProperty>)Stream.of((T)ExaminableProperty.of("selector", this.selector)), super.examinablePropertiesWithoutChildren());
    }
    
    @Override
    public EntityNBTComponent.Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static final class BuilderImpl extends NBTComponentImpl.BuilderImpl<EntityNBTComponent, EntityNBTComponent.Builder> implements EntityNBTComponent.Builder
    {
        @Nullable
        private String selector;
        
        BuilderImpl() {
        }
        
        BuilderImpl(@NotNull final EntityNBTComponent component) {
            super(component);
            this.selector = component.selector();
        }
        
        @Override
        public EntityNBTComponent.Builder selector(@NotNull final String selector) {
            this.selector = selector;
            return this;
        }
        
        @NotNull
        @Override
        public EntityNBTComponent build() {
            if (this.nbtPath == null) {
                throw new IllegalStateException("nbt path must be set");
            }
            if (this.selector == null) {
                throw new IllegalStateException("selector must be set");
            }
            return new EntityNBTComponentImpl(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.separator, this.selector);
        }
    }
}
