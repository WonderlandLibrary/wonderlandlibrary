// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.event;

import com.viaversion.viaversion.libs.kyori.adventure.util.Index;
import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import java.util.function.UnaryOperator;
import com.viaversion.viaversion.libs.kyori.adventure.text.renderer.ComponentRenderer;
import java.util.Objects;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.key.Keyed;
import com.viaversion.viaversion.libs.kyori.adventure.nbt.api.BinaryTagHolder;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.StyleBuilderApplicable;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

public final class HoverEvent<V> implements Examinable, HoverEventSource<V>, StyleBuilderApplicable
{
    private final Action<V> action;
    private final V value;
    
    @NotNull
    public static HoverEvent<Component> showText(@NotNull final ComponentLike text) {
        return showText(text.asComponent());
    }
    
    @NotNull
    public static HoverEvent<Component> showText(@NotNull final Component text) {
        return new HoverEvent<Component>(Action.SHOW_TEXT, text);
    }
    
    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull final Key item, final int count) {
        return showItem(item, count, null);
    }
    
    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull final Keyed item, final int count) {
        return showItem(item, count, null);
    }
    
    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull final Key item, final int count, @Nullable final BinaryTagHolder nbt) {
        return showItem(ShowItem.of(item, count, nbt));
    }
    
    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull final Keyed item, final int count, @Nullable final BinaryTagHolder nbt) {
        return showItem(ShowItem.of(item, count, nbt));
    }
    
    @NotNull
    public static HoverEvent<ShowItem> showItem(@NotNull final ShowItem item) {
        return new HoverEvent<ShowItem>(Action.SHOW_ITEM, item);
    }
    
    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull final Key type, @NotNull final UUID id) {
        return showEntity(type, id, null);
    }
    
    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull final Keyed type, @NotNull final UUID id) {
        return showEntity(type, id, null);
    }
    
    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull final Key type, @NotNull final UUID id, @Nullable final Component name) {
        return showEntity(ShowEntity.of(type, id, name));
    }
    
    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull final Keyed type, @NotNull final UUID id, @Nullable final Component name) {
        return showEntity(ShowEntity.of(type, id, name));
    }
    
    @NotNull
    public static HoverEvent<ShowEntity> showEntity(@NotNull final ShowEntity entity) {
        return new HoverEvent<ShowEntity>(Action.SHOW_ENTITY, entity);
    }
    
    @NotNull
    public static <V> HoverEvent<V> hoverEvent(@NotNull final Action<V> action, @NotNull final V value) {
        return new HoverEvent<V>(action, value);
    }
    
    private HoverEvent(@NotNull final Action<V> action, @NotNull final V value) {
        this.action = Objects.requireNonNull(action, "action");
        this.value = Objects.requireNonNull(value, "value");
    }
    
    @NotNull
    public Action<V> action() {
        return this.action;
    }
    
    @NotNull
    public V value() {
        return this.value;
    }
    
    @NotNull
    public HoverEvent<V> value(@NotNull final V value) {
        return new HoverEvent<V>(this.action, value);
    }
    
    @NotNull
    public <C> HoverEvent<V> withRenderedValue(@NotNull final ComponentRenderer<C> renderer, @NotNull final C context) {
        final V oldValue = this.value;
        final V newValue = ((Action<Object>)this.action).renderer.render(renderer, context, oldValue);
        if (newValue != oldValue) {
            return new HoverEvent<V>(this.action, newValue);
        }
        return this;
    }
    
    @NotNull
    @Override
    public HoverEvent<V> asHoverEvent() {
        return this;
    }
    
    @NotNull
    @Override
    public HoverEvent<V> asHoverEvent(@NotNull final UnaryOperator<V> op) {
        if (op == UnaryOperator.identity()) {
            return this;
        }
        return new HoverEvent<V>(this.action, op.apply(this.value));
    }
    
    @Override
    public void styleApply(final Style.Builder style) {
        style.hoverEvent(this);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final HoverEvent<?> that = (HoverEvent<?>)other;
        return this.action == that.action && this.value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        int result = this.action.hashCode();
        result = 31 * result + this.value.hashCode();
        return result;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("action", this.action), ExaminableProperty.of("value", this.value) });
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
    
    public static final class ShowItem implements Examinable
    {
        private final Key item;
        private final int count;
        @Nullable
        private final BinaryTagHolder nbt;
        
        @NotNull
        public static ShowItem of(@NotNull final Key item, final int count) {
            return of(item, count, null);
        }
        
        @NotNull
        public static ShowItem of(@NotNull final Keyed item, final int count) {
            return of(item, count, null);
        }
        
        @NotNull
        public static ShowItem of(@NotNull final Key item, final int count, @Nullable final BinaryTagHolder nbt) {
            return new ShowItem(Objects.requireNonNull(item, "item"), count, nbt);
        }
        
        @NotNull
        public static ShowItem of(@NotNull final Keyed item, final int count, @Nullable final BinaryTagHolder nbt) {
            return new ShowItem(Objects.requireNonNull(item, "item").key(), count, nbt);
        }
        
        private ShowItem(@NotNull final Key item, final int count, @Nullable final BinaryTagHolder nbt) {
            this.item = item;
            this.count = count;
            this.nbt = nbt;
        }
        
        @NotNull
        public Key item() {
            return this.item;
        }
        
        @NotNull
        public ShowItem item(@NotNull final Key item) {
            if (Objects.requireNonNull(item, "item").equals(this.item)) {
                return this;
            }
            return new ShowItem(item, this.count, this.nbt);
        }
        
        public int count() {
            return this.count;
        }
        
        @NotNull
        public ShowItem count(final int count) {
            if (count == this.count) {
                return this;
            }
            return new ShowItem(this.item, count, this.nbt);
        }
        
        @Nullable
        public BinaryTagHolder nbt() {
            return this.nbt;
        }
        
        @NotNull
        public ShowItem nbt(@Nullable final BinaryTagHolder nbt) {
            if (Objects.equals(nbt, this.nbt)) {
                return this;
            }
            return new ShowItem(this.item, this.count, nbt);
        }
        
        @Override
        public boolean equals(@Nullable final Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || this.getClass() != other.getClass()) {
                return false;
            }
            final ShowItem that = (ShowItem)other;
            return this.item.equals(that.item) && this.count == that.count && Objects.equals(this.nbt, that.nbt);
        }
        
        @Override
        public int hashCode() {
            int result = this.item.hashCode();
            result = 31 * result + Integer.hashCode(this.count);
            result = 31 * result + Objects.hashCode(this.nbt);
            return result;
        }
        
        @NotNull
        @Override
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("item", this.item), ExaminableProperty.of("count", this.count), ExaminableProperty.of("nbt", this.nbt) });
        }
        
        @Override
        public String toString() {
            return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
        }
    }
    
    public static final class ShowEntity implements Examinable
    {
        private final Key type;
        private final UUID id;
        private final Component name;
        
        @NotNull
        public static ShowEntity of(@NotNull final Key type, @NotNull final UUID id) {
            return of(type, id, null);
        }
        
        @NotNull
        public static ShowEntity of(@NotNull final Keyed type, @NotNull final UUID id) {
            return of(type, id, null);
        }
        
        @NotNull
        public static ShowEntity of(@NotNull final Key type, @NotNull final UUID id, @Nullable final Component name) {
            return new ShowEntity(Objects.requireNonNull(type, "type"), Objects.requireNonNull(id, "id"), name);
        }
        
        @NotNull
        public static ShowEntity of(@NotNull final Keyed type, @NotNull final UUID id, @Nullable final Component name) {
            return new ShowEntity(Objects.requireNonNull(type, "type").key(), Objects.requireNonNull(id, "id"), name);
        }
        
        private ShowEntity(@NotNull final Key type, @NotNull final UUID id, @Nullable final Component name) {
            this.type = type;
            this.id = id;
            this.name = name;
        }
        
        @NotNull
        public Key type() {
            return this.type;
        }
        
        @NotNull
        public ShowEntity type(@NotNull final Key type) {
            if (Objects.requireNonNull(type, "type").equals(this.type)) {
                return this;
            }
            return new ShowEntity(type, this.id, this.name);
        }
        
        @NotNull
        public ShowEntity type(@NotNull final Keyed type) {
            return this.type(Objects.requireNonNull(type, "type").key());
        }
        
        @NotNull
        public UUID id() {
            return this.id;
        }
        
        @NotNull
        public ShowEntity id(@NotNull final UUID id) {
            if (Objects.requireNonNull(id).equals(this.id)) {
                return this;
            }
            return new ShowEntity(this.type, id, this.name);
        }
        
        @Nullable
        public Component name() {
            return this.name;
        }
        
        @NotNull
        public ShowEntity name(@Nullable final Component name) {
            if (Objects.equals(name, this.name)) {
                return this;
            }
            return new ShowEntity(this.type, this.id, name);
        }
        
        @Override
        public boolean equals(@Nullable final Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || this.getClass() != other.getClass()) {
                return false;
            }
            final ShowEntity that = (ShowEntity)other;
            return this.type.equals(that.type) && this.id.equals(that.id) && Objects.equals(this.name, that.name);
        }
        
        @Override
        public int hashCode() {
            int result = this.type.hashCode();
            result = 31 * result + this.id.hashCode();
            result = 31 * result + Objects.hashCode(this.name);
            return result;
        }
        
        @NotNull
        @Override
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("type", this.type), ExaminableProperty.of("id", this.id), ExaminableProperty.of("name", this.name) });
        }
        
        @Override
        public String toString() {
            return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
        }
    }
    
    public static final class Action<V>
    {
        public static final Action<Component> SHOW_TEXT;
        public static final Action<ShowItem> SHOW_ITEM;
        public static final Action<ShowEntity> SHOW_ENTITY;
        public static final Index<String, Action<?>> NAMES;
        private final String name;
        private final Class<V> type;
        private final boolean readable;
        private final Renderer<V> renderer;
        
        Action(final String name, final Class<V> type, final boolean readable, final Renderer<V> renderer) {
            this.name = name;
            this.type = type;
            this.readable = readable;
            this.renderer = renderer;
        }
        
        @NotNull
        public Class<V> type() {
            return this.type;
        }
        
        public boolean readable() {
            return this.readable;
        }
        
        @NotNull
        @Override
        public String toString() {
            return this.name;
        }
        
        static {
            SHOW_TEXT = new Action<Component>("show_text", Component.class, true, new Renderer<Component>() {
                @NotNull
                @Override
                public <C> Component render(@NotNull final ComponentRenderer<C> renderer, @NotNull final C context, @NotNull final Component value) {
                    return renderer.render(value, context);
                }
            });
            SHOW_ITEM = new Action<ShowItem>("show_item", ShowItem.class, true, new Renderer<ShowItem>() {
                @NotNull
                @Override
                public <C> ShowItem render(@NotNull final ComponentRenderer<C> renderer, @NotNull final C context, @NotNull final ShowItem value) {
                    return value;
                }
            });
            SHOW_ENTITY = new Action<ShowEntity>("show_entity", ShowEntity.class, true, new Renderer<ShowEntity>() {
                @NotNull
                @Override
                public <C> ShowEntity render(@NotNull final ComponentRenderer<C> renderer, @NotNull final C context, @NotNull final ShowEntity value) {
                    if (value.name == null) {
                        return value;
                    }
                    return value.name(renderer.render(value.name, context));
                }
            });
            NAMES = Index.create(constant -> constant.name, (Action<?>[])new Action[] { Action.SHOW_TEXT, Action.SHOW_ITEM, Action.SHOW_ENTITY });
        }
        
        @FunctionalInterface
        interface Renderer<V>
        {
            @NotNull
             <C> V render(@NotNull final ComponentRenderer<C> renderer, @NotNull final C context, @NotNull final V value);
        }
    }
}
