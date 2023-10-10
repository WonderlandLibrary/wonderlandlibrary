// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.inventory;

import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;

final class BookImpl implements Book
{
    private final Component title;
    private final Component author;
    private final List<Component> pages;
    
    BookImpl(@NotNull final Component title, @NotNull final Component author, @NotNull final List<Component> pages) {
        this.title = Objects.requireNonNull(title, "title");
        this.author = Objects.requireNonNull(author, "author");
        this.pages = Collections.unmodifiableList((List<? extends Component>)Objects.requireNonNull((List<? extends T>)pages, "pages"));
    }
    
    @NotNull
    @Override
    public Component title() {
        return this.title;
    }
    
    @NotNull
    @Override
    public Book title(@NotNull final Component title) {
        return new BookImpl(Objects.requireNonNull(title, "title"), this.author, this.pages);
    }
    
    @NotNull
    @Override
    public Component author() {
        return this.author;
    }
    
    @NotNull
    @Override
    public Book author(@NotNull final Component author) {
        return new BookImpl(this.title, Objects.requireNonNull(author, "author"), this.pages);
    }
    
    @NotNull
    @Override
    public List<Component> pages() {
        return this.pages;
    }
    
    @NotNull
    @Override
    public Book pages(@NotNull final List<Component> pages) {
        return new BookImpl(this.title, this.author, new ArrayList<Component>(Objects.requireNonNull(pages, "pages")));
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("title", this.title), ExaminableProperty.of("author", this.author), ExaminableProperty.of("pages", this.pages) });
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookImpl)) {
            return false;
        }
        final BookImpl that = (BookImpl)o;
        return this.title.equals(that.title) && this.author.equals(that.author) && this.pages.equals(that.pages);
    }
    
    @Override
    public int hashCode() {
        int result = this.title.hashCode();
        result = 31 * result + this.author.hashCode();
        result = 31 * result + this.pages.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
    
    static final class BuilderImpl implements Builder
    {
        private Component title;
        private Component author;
        private final List<Component> pages;
        
        BuilderImpl() {
            this.title = Component.empty();
            this.author = Component.empty();
            this.pages = new ArrayList<Component>();
        }
        
        @NotNull
        @Override
        public Builder title(@NotNull final Component title) {
            this.title = Objects.requireNonNull(title, "title");
            return this;
        }
        
        @NotNull
        @Override
        public Builder author(@NotNull final Component author) {
            this.author = Objects.requireNonNull(author, "author");
            return this;
        }
        
        @NotNull
        @Override
        public Builder addPage(@NotNull final Component page) {
            this.pages.add(Objects.requireNonNull(page, "page"));
            return this;
        }
        
        @NotNull
        @Override
        public Builder pages(@NotNull final Collection<Component> pages) {
            this.pages.addAll(Objects.requireNonNull(pages, "pages"));
            return this;
        }
        
        @NotNull
        @Override
        public Builder pages(@NotNull final Component... pages) {
            Collections.addAll(this.pages, pages);
            return this;
        }
        
        @NotNull
        @Override
        public Book build() {
            return new BookImpl(this.title, this.author, new ArrayList<Component>(this.pages));
        }
    }
}
