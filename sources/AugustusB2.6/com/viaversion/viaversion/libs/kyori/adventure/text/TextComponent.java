// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface TextComponent extends BuildableComponent<TextComponent, Builder>, ScopedComponent<TextComponent>
{
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @NotNull
    default TextComponent ofChildren(@NotNull final ComponentLike... components) {
        final Component joined = Component.join(JoinConfiguration.noSeparators(), components);
        if (joined instanceof TextComponent) {
            return (TextComponent)joined;
        }
        return ((ComponentBuilder<TextComponent, B>)((ComponentBuilder<C, Builder>)Component.text()).append(joined)).build();
    }
    
    @NotNull
    String content();
    
    @Contract(pure = true)
    @NotNull
    TextComponent content(@NotNull final String content);
    
    public interface Builder extends ComponentBuilder<TextComponent, Builder>
    {
        @NotNull
        String content();
        
        @Contract("_ -> this")
        @NotNull
        Builder content(@NotNull final String content);
    }
}
