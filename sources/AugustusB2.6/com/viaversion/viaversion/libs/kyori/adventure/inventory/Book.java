// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.inventory;

import org.jetbrains.annotations.Contract;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;

@ApiStatus.NonExtendable
public interface Book extends Buildable<Book, Builder>, Examinable
{
    @NotNull
    default Book book(@NotNull final Component title, @NotNull final Component author, @NotNull final Collection<Component> pages) {
        return new BookImpl(title, author, new ArrayList<Component>(pages));
    }
    
    @NotNull
    default Book book(@NotNull final Component title, @NotNull final Component author, @NotNull final Component... pages) {
        return book(title, author, Arrays.asList(pages));
    }
    
    @NotNull
    default Builder builder() {
        return new BookImpl.BuilderImpl();
    }
    
    @NotNull
    Component title();
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    Book title(@NotNull final Component title);
    
    @NotNull
    Component author();
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    Book author(@NotNull final Component author);
    
    @NotNull
    List<Component> pages();
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default Book pages(@NotNull final Component... pages) {
        return this.pages(Arrays.asList(pages));
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    Book pages(@NotNull final List<Component> pages);
    
    @NotNull
    default Builder toBuilder() {
        return builder().title(this.title()).author(this.author()).pages(this.pages());
    }
    
    public interface Builder extends Buildable.Builder<Book>
    {
        @Contract("_ -> this")
        @NotNull
        Builder title(@NotNull final Component title);
        
        @Contract("_ -> this")
        @NotNull
        Builder author(@NotNull final Component author);
        
        @Contract("_ -> this")
        @NotNull
        Builder addPage(@NotNull final Component page);
        
        @Contract("_ -> this")
        @NotNull
        Builder pages(@NotNull final Component... pages);
        
        @Contract("_ -> this")
        @NotNull
        Builder pages(@NotNull final Collection<Component> pages);
        
        @NotNull
        Book build();
    }
}
