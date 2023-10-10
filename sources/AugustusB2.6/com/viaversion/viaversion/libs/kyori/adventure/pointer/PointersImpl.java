// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.pointer;

import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import java.util.Objects;
import java.util.Optional;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;
import java.util.function.Supplier;
import java.util.Map;

final class PointersImpl implements Pointers
{
    static final Pointers EMPTY;
    private final Map<Pointer<?>, Supplier<?>> pointers;
    
    PointersImpl(@NotNull final BuilderImpl builder) {
        this.pointers = new HashMap<Pointer<?>, Supplier<?>>(builder.pointers);
    }
    
    @NotNull
    @Override
    public <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
        Objects.requireNonNull(pointer, "pointer");
        final Supplier<?> supplier = this.pointers.get(pointer);
        if (supplier == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(supplier.get());
    }
    
    @Override
    public <T> boolean supports(@NotNull final Pointer<T> pointer) {
        Objects.requireNonNull(pointer, "pointer");
        return this.pointers.containsKey(pointer);
    }
    
    @NotNull
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static {
        EMPTY = new Pointers() {
            @NotNull
            @Override
            public <T> Optional<T> get(@NotNull final Pointer<T> pointer) {
                return Optional.empty();
            }
            
            @Override
            public <T> boolean supports(@NotNull final Pointer<T> pointer) {
                return false;
            }
            
            @Override
            public Builder toBuilder() {
                return new BuilderImpl();
            }
            
            @Override
            public String toString() {
                return "EmptyPointers";
            }
        };
    }
    
    static final class BuilderImpl implements Builder
    {
        private final Map<Pointer<?>, Supplier<?>> pointers;
        
        BuilderImpl() {
            this.pointers = new HashMap<Pointer<?>, Supplier<?>>();
        }
        
        BuilderImpl(@NotNull final PointersImpl pointers) {
            this.pointers = new HashMap<Pointer<?>, Supplier<?>>(pointers.pointers);
        }
        
        @NotNull
        @Override
        public <T> Builder withDynamic(@NotNull final Pointer<T> pointer, @NotNull final Supplier<T> value) {
            this.pointers.put(Objects.requireNonNull(pointer, "pointer"), Objects.requireNonNull(value, "value"));
            return this;
        }
        
        @NotNull
        @Override
        public Pointers build() {
            return new PointersImpl(this);
        }
    }
}
