// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import org.jetbrains.annotations.Nullable;

abstract class NBTComponentImpl<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponent implements NBTComponent<C, B>
{
    static final boolean INTERPRET_DEFAULT = false;
    final String nbtPath;
    final boolean interpret;
    @Nullable
    final Component separator;
    
    NBTComponentImpl(@NotNull final List<? extends ComponentLike> children, @NotNull final Style style, final String nbtPath, final boolean interpret, @Nullable final ComponentLike separator) {
        super(children, style);
        this.nbtPath = nbtPath;
        this.interpret = interpret;
        this.separator = ComponentLike.unbox(separator);
    }
    
    @NotNull
    @Override
    public String nbtPath() {
        return this.nbtPath;
    }
    
    @Override
    public boolean interpret() {
        return this.interpret;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NBTComponent)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        final NBTComponent<?, ?> that = (NBTComponent<?, ?>)other;
        return Objects.equals(this.nbtPath, that.nbtPath()) && this.interpret == that.interpret() && Objects.equals(this.separator, that.separator());
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.nbtPath.hashCode();
        result = 31 * result + Boolean.hashCode(this.interpret);
        result = 31 * result + Objects.hashCode(this.separator);
        return result;
    }
    
    @NotNull
    @Override
    protected Stream<? extends ExaminableProperty> examinablePropertiesWithoutChildren() {
        return Stream.concat((Stream<? extends ExaminableProperty>)Stream.of((T[])new ExaminableProperty[] { ExaminableProperty.of("nbtPath", this.nbtPath), ExaminableProperty.of("interpret", this.interpret), ExaminableProperty.of("separator", this.separator) }), super.examinablePropertiesWithoutChildren());
    }
    
    abstract static class BuilderImpl<C extends NBTComponent<C, B>, B extends NBTComponentBuilder<C, B>> extends AbstractComponentBuilder<C, B> implements NBTComponentBuilder<C, B>
    {
        @Nullable
        protected String nbtPath;
        protected boolean interpret;
        @Nullable
        protected Component separator;
        
        BuilderImpl() {
            this.interpret = false;
        }
        
        BuilderImpl(@NotNull final C component) {
            super(component);
            this.interpret = false;
            this.nbtPath = component.nbtPath();
            this.interpret = component.interpret();
        }
        
        @NotNull
        @Override
        public B nbtPath(@NotNull final String nbtPath) {
            this.nbtPath = nbtPath;
            return (B)this;
        }
        
        @NotNull
        @Override
        public B interpret(final boolean interpret) {
            this.interpret = interpret;
            return (B)this;
        }
        
        @NotNull
        @Override
        public B separator(@Nullable final ComponentLike separator) {
            this.separator = ComponentLike.unbox(separator);
            return (B)this;
        }
    }
}
