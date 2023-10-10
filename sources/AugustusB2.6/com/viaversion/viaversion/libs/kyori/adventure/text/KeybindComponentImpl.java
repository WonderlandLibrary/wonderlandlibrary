// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import java.util.List;

final class KeybindComponentImpl extends AbstractComponent implements KeybindComponent
{
    private final String keybind;
    
    KeybindComponentImpl(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, @NotNull final String keybind) {
        super(children, style);
        this.keybind = Objects.requireNonNull(keybind, "keybind");
    }
    
    @NotNull
    @Override
    public String keybind() {
        return this.keybind;
    }
    
    @NotNull
    @Override
    public KeybindComponent keybind(@NotNull final String keybind) {
        if (Objects.equals(this.keybind, keybind)) {
            return this;
        }
        return new KeybindComponentImpl(this.children, this.style, Objects.requireNonNull(keybind, "keybind"));
    }
    
    @NotNull
    @Override
    public KeybindComponent children(@NotNull final List<? extends ComponentLike> children) {
        return new KeybindComponentImpl(children, this.style, this.keybind);
    }
    
    @NotNull
    @Override
    public KeybindComponent style(@NotNull final Style style) {
        return new KeybindComponentImpl(this.children, style, this.keybind);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof KeybindComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final KeybindComponent that = (KeybindComponent)other;
        return Objects.equals(this.keybind, that.keybind());
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.keybind.hashCode();
        return result;
    }
    
    @NotNull
    @Override
    protected Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren() {
        return Stream.concat((Stream<? extends ExaminableProperty>)Stream.of((T)ExaminableProperty.of("keybind", this.keybind)), super.examinablePropertiesWithoutChildren());
    }
    
    @NotNull
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static final class BuilderImpl extends AbstractComponentBuilder<KeybindComponent, KeybindComponent.Builder> implements KeybindComponent.Builder
    {
        @Nullable
        private String keybind;
        
        BuilderImpl() {
        }
        
        BuilderImpl(@NotNull final KeybindComponent component) {
            super(component);
            this.keybind = component.keybind();
        }
        
        @NotNull
        @Override
        public KeybindComponent.Builder keybind(@NotNull final String keybind) {
            this.keybind = keybind;
            return this;
        }
        
        @NotNull
        @Override
        public KeybindComponent build() {
            if (this.keybind == null) {
                throw new IllegalStateException("keybind must be set");
            }
            return new KeybindComponentImpl(this.children, this.buildStyle(), this.keybind);
        }
    }
}
