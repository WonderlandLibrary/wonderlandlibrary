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
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;

final class StorageNBTComponentImpl extends NBTComponentImpl<StorageNBTComponent, StorageNBTComponent.Builder> implements StorageNBTComponent
{
    private final Key storage;
    
    StorageNBTComponentImpl(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final ComponentLike separator, final Key storage) {
        super(children, style, nbtPath, interpret, separator);
        this.storage = storage;
    }
    
    @NotNull
    @Override
    public StorageNBTComponent nbtPath(@NotNull final String nbtPath) {
        if (Objects.equals(this.nbtPath, nbtPath)) {
            return this;
        }
        return new StorageNBTComponentImpl(this.children, this.style, nbtPath, this.interpret, this.separator, this.storage);
    }
    
    @NotNull
    @Override
    public StorageNBTComponent interpret(final boolean interpret) {
        if (this.interpret == interpret) {
            return this;
        }
        return new StorageNBTComponentImpl(this.children, this.style, this.nbtPath, interpret, this.separator, this.storage);
    }
    
    @Nullable
    @Override
    public Component separator() {
        return this.separator;
    }
    
    @NotNull
    @Override
    public StorageNBTComponent separator(@Nullable final ComponentLike separator) {
        return new StorageNBTComponentImpl(this.children, this.style, this.nbtPath, this.interpret, separator, this.storage);
    }
    
    @NotNull
    @Override
    public Key storage() {
        return this.storage;
    }
    
    @NotNull
    @Override
    public StorageNBTComponent storage(@NotNull final Key storage) {
        if (Objects.equals(this.storage, storage)) {
            return this;
        }
        return new StorageNBTComponentImpl(this.children, this.style, this.nbtPath, this.interpret, this.separator, storage);
    }
    
    @NotNull
    @Override
    public StorageNBTComponent children(@NotNull final List<? extends ComponentLike> children) {
        return new StorageNBTComponentImpl(children, this.style, this.nbtPath, this.interpret, this.separator, this.storage);
    }
    
    @NotNull
    @Override
    public StorageNBTComponent style(@NotNull final Style style) {
        return new StorageNBTComponentImpl(this.children, style, this.nbtPath, this.interpret, this.separator, this.storage);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StorageNBTComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final StorageNBTComponentImpl that = (StorageNBTComponentImpl)other;
        return Objects.equals(this.storage, that.storage());
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.storage.hashCode();
        return result;
    }
    
    @NotNull
    @Override
    protected Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren() {
        return Stream.concat((Stream<? extends ExaminableProperty>)Stream.of((T)ExaminableProperty.of("storage", this.storage)), super.examinablePropertiesWithoutChildren());
    }
    
    @Override
    public StorageNBTComponent.Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static class BuilderImpl extends NBTComponentImpl.BuilderImpl<StorageNBTComponent, StorageNBTComponent.Builder> implements StorageNBTComponent.Builder
    {
        @Nullable
        private Key storage;
        
        BuilderImpl() {
        }
        
        BuilderImpl(@NotNull final StorageNBTComponent component) {
            super(component);
            this.storage = component.storage();
        }
        
        @Override
        public StorageNBTComponent.Builder storage(@NotNull final Key storage) {
            this.storage = storage;
            return this;
        }
        
        @NotNull
        @Override
        public StorageNBTComponent build() {
            if (this.nbtPath == null) {
                throw new IllegalStateException("nbt path must be set");
            }
            if (this.storage == null) {
                throw new IllegalStateException("storage must be set");
            }
            return new StorageNBTComponentImpl(this.children, this.buildStyle(), this.nbtPath, this.interpret, this.separator, this.storage);
        }
    }
}
