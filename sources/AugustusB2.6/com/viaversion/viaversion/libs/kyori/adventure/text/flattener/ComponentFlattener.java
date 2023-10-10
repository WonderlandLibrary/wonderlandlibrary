// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.flattener;

import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import java.util.function.Function;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;

public interface ComponentFlattener extends Buildable<ComponentFlattener, Builder>
{
    @NotNull
    default Builder builder() {
        return new ComponentFlattenerImpl.BuilderImpl();
    }
    
    @NotNull
    default ComponentFlattener basic() {
        return ComponentFlattenerImpl.BASIC;
    }
    
    @NotNull
    default ComponentFlattener textOnly() {
        return ComponentFlattenerImpl.TEXT_ONLY;
    }
    
    void flatten(@NotNull final Component input, @NotNull final FlattenerListener listener);
    
    public interface Builder extends Buildable.Builder<ComponentFlattener>
    {
        @NotNull
         <T extends Component> Builder mapper(@NotNull final Class<T> type, @NotNull final Function<T, String> converter);
        
        @NotNull
         <T extends Component> Builder complexMapper(@NotNull final Class<T> type, @NotNull final BiConsumer<T, Consumer<Component>> converter);
        
        @NotNull
        Builder unknownMapper(@Nullable final Function<Component, String> converter);
    }
}
