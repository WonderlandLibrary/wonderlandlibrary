// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import java.util.function.UnaryOperator;
import com.viaversion.viaversion.libs.kyori.adventure.util.IntFunction2;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.Spliterators;
import java.util.Spliterator;
import com.viaversion.viaversion.libs.kyori.adventure.util.ForwardingIterator;
import com.viaversion.viaversion.libs.kyori.adventure.util.MonkeyBars;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import java.util.Map;
import java.util.Iterator;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.kyori.adventure.translation.Translatable;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import java.util.Set;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import java.util.Collections;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import java.util.function.Consumer;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;
import org.jetbrains.annotations.Contract;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import java.util.function.BiPredicate;
import org.jetbrains.annotations.ApiStatus;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEventSource;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

@ApiStatus.NonExtendable
public interface Component extends ComponentBuilderApplicable, ComponentLike, Examinable, HoverEventSource<Component>
{
    public static final BiPredicate<? super Component, ? super Component> EQUALS = Objects::equals;
    public static final BiPredicate<? super Component, ? super Component> EQUALS_IDENTITY = (a, b) -> a == b;
    
    @NotNull
    default TextComponent empty() {
        return TextComponentImpl.EMPTY;
    }
    
    @NotNull
    default TextComponent newline() {
        return TextComponentImpl.NEWLINE;
    }
    
    @NotNull
    default TextComponent space() {
        return TextComponentImpl.SPACE;
    }
    
    @Deprecated
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent join(@NotNull final ComponentLike separator, @NotNull final ComponentLike... components) {
        return join(separator, Arrays.asList(components));
    }
    
    @Deprecated
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent join(@NotNull final ComponentLike separator, final Iterable<? extends ComponentLike> components) {
        final Component component = join(JoinConfiguration.separator(separator), components);
        if (component instanceof TextComponent) {
            return (TextComponent)component;
        }
        return ((ComponentBuilder<TextComponent, B>)((ComponentBuilder<C, TextComponent.Builder>)text()).append(component)).build();
    }
    
    @Contract(pure = true)
    @NotNull
    default Component join(@NotNull final JoinConfiguration config, @NotNull final ComponentLike... components) {
        return join(config, Arrays.asList(components));
    }
    
    @Contract(pure = true)
    @NotNull
    default Component join(@NotNull final JoinConfiguration config, @NotNull final Iterable<? extends ComponentLike> components) {
        return JoinConfigurationImpl.join(config, components);
    }
    
    @NotNull
    default Collector<Component, ? extends ComponentBuilder<?, ?>, Component> toComponent() {
        return toComponent(empty());
    }
    
    @NotNull
    default Collector<Component, ? extends ComponentBuilder<?, ?>, Component> toComponent(@NotNull final Component separator) {
        final List<Component> aChildren;
        final TextComponent.Builder ret;
        return (Collector<Component, ? extends ComponentBuilder<?, ?>, Component>)Collector.of((Supplier<? extends ComponentBuilder<?, ?>>)Component::text, (builder, add) -> {
            if (separator != empty() && !builder.children().isEmpty()) {
                builder.append(separator);
            }
            builder.append(add);
        }, (a, b) -> {
            aChildren = a.children();
            ret = text().append(aChildren);
            if (!aChildren.isEmpty()) {
                ret.append(separator);
            }
            ret.append(b.children());
            return ret;
        }, ComponentBuilder::build, new Collector.Characteristics[0]);
    }
    
    @Contract(pure = true)
    default BlockNBTComponent.Builder blockNBT() {
        return new BlockNBTComponentImpl.BuilderImpl();
    }
    
    @Contract("_ -> new")
    @NotNull
    default BlockNBTComponent blockNBT(@NotNull final Consumer<? super BlockNBTComponent.Builder> consumer) {
        return Buildable.configureAndBuild(blockNBT(), consumer);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default BlockNBTComponent blockNBT(@NotNull final String nbtPath, final BlockNBTComponent.Pos pos) {
        return blockNBT(nbtPath, false, pos);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default BlockNBTComponent blockNBT(@NotNull final String nbtPath, final boolean interpret, final BlockNBTComponent.Pos pos) {
        return blockNBT(nbtPath, interpret, null, pos);
    }
    
    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    default BlockNBTComponent blockNBT(@NotNull final String nbtPath, final boolean interpret, @Nullable final ComponentLike separator, final BlockNBTComponent.Pos pos) {
        return new BlockNBTComponentImpl(Collections.emptyList(), Style.empty(), nbtPath, interpret, separator, pos);
    }
    
    @Contract(pure = true)
    default EntityNBTComponent.Builder entityNBT() {
        return new EntityNBTComponentImpl.BuilderImpl();
    }
    
    @Contract("_ -> new")
    @NotNull
    default EntityNBTComponent entityNBT(@NotNull final Consumer<? super EntityNBTComponent.Builder> consumer) {
        return Buildable.configureAndBuild(entityNBT(), consumer);
    }
    
    @Contract("_, _ -> new")
    @NotNull
    default EntityNBTComponent entityNBT(@NotNull final String nbtPath, @NotNull final String selector) {
        return ((ComponentBuilder<EntityNBTComponent, B>)((NBTComponentBuilder<C, EntityNBTComponent.Builder>)entityNBT()).nbtPath(nbtPath).selector(selector)).build();
    }
    
    @Contract(pure = true)
    default KeybindComponent.Builder keybind() {
        return new KeybindComponentImpl.BuilderImpl();
    }
    
    @Contract("_ -> new")
    @NotNull
    default KeybindComponent keybind(@NotNull final Consumer<? super KeybindComponent.Builder> consumer) {
        return Buildable.configureAndBuild(keybind(), consumer);
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default KeybindComponent keybind(@NotNull final String keybind) {
        return keybind(keybind, Style.empty());
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default KeybindComponent keybind(final KeybindComponent.KeybindLike keybind) {
        return keybind(Objects.requireNonNull(keybind, "keybind").asKeybind(), Style.empty());
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default KeybindComponent keybind(@NotNull final String keybind, @NotNull final Style style) {
        return new KeybindComponentImpl(Collections.emptyList(), style, keybind);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default KeybindComponent keybind(final KeybindComponent.KeybindLike keybind, @NotNull final Style style) {
        return new KeybindComponentImpl(Collections.emptyList(), style, Objects.requireNonNull(keybind, "keybind").asKeybind());
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default KeybindComponent keybind(@NotNull final String keybind, @Nullable final TextColor color) {
        return keybind(keybind, Style.style(color));
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default KeybindComponent keybind(final KeybindComponent.KeybindLike keybind, @Nullable final TextColor color) {
        return keybind(Objects.requireNonNull(keybind, "keybind").asKeybind(), Style.style(color));
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default KeybindComponent keybind(@NotNull final String keybind, @Nullable final TextColor color, final TextDecoration... decorations) {
        return keybind(keybind, Style.style(color, decorations));
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default KeybindComponent keybind(final KeybindComponent.KeybindLike keybind, @Nullable final TextColor color, final TextDecoration... decorations) {
        return keybind(Objects.requireNonNull(keybind, "keybind").asKeybind(), Style.style(color, decorations));
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default KeybindComponent keybind(@NotNull final String keybind, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return keybind(keybind, Style.style(color, decorations));
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default KeybindComponent keybind(final KeybindComponent.KeybindLike keybind, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return keybind(Objects.requireNonNull(keybind, "keybind").asKeybind(), Style.style(color, decorations));
    }
    
    @Contract(pure = true)
    default ScoreComponent.Builder score() {
        return new ScoreComponentImpl.BuilderImpl();
    }
    
    @Contract("_ -> new")
    @NotNull
    default ScoreComponent score(@NotNull final Consumer<? super ScoreComponent.Builder> consumer) {
        return Buildable.configureAndBuild(score(), consumer);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default ScoreComponent score(@NotNull final String name, @NotNull final String objective) {
        return score(name, objective, null);
    }
    
    @Deprecated
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default ScoreComponent score(@NotNull final String name, @NotNull final String objective, @Nullable final String value) {
        return new ScoreComponentImpl(Collections.emptyList(), Style.empty(), name, objective, value);
    }
    
    @Contract(pure = true)
    default SelectorComponent.Builder selector() {
        return new SelectorComponentImpl.BuilderImpl();
    }
    
    @Contract("_ -> new")
    @NotNull
    default SelectorComponent selector(@NotNull final Consumer<? super SelectorComponent.Builder> consumer) {
        return Buildable.configureAndBuild(selector(), consumer);
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default SelectorComponent selector(@NotNull final String pattern) {
        return selector(pattern, null);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default SelectorComponent selector(@NotNull final String pattern, @Nullable final ComponentLike separator) {
        return new SelectorComponentImpl(Collections.emptyList(), Style.empty(), pattern, separator);
    }
    
    @Contract(pure = true)
    default StorageNBTComponent.Builder storageNBT() {
        return new StorageNBTComponentImpl.BuilderImpl();
    }
    
    @Contract("_ -> new")
    @NotNull
    default StorageNBTComponent storageNBT(@NotNull final Consumer<? super StorageNBTComponent.Builder> consumer) {
        return Buildable.configureAndBuild(storageNBT(), consumer);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default StorageNBTComponent storageNBT(@NotNull final String nbtPath, @NotNull final Key storage) {
        return storageNBT(nbtPath, false, storage);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default StorageNBTComponent storageNBT(@NotNull final String nbtPath, final boolean interpret, @NotNull final Key storage) {
        return storageNBT(nbtPath, interpret, null, storage);
    }
    
    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    default StorageNBTComponent storageNBT(@NotNull final String nbtPath, final boolean interpret, @Nullable final ComponentLike separator, @NotNull final Key storage) {
        return new StorageNBTComponentImpl(Collections.emptyList(), Style.empty(), nbtPath, interpret, separator, storage);
    }
    
    @Contract(pure = true)
    default TextComponent.Builder text() {
        return new TextComponentImpl.BuilderImpl();
    }
    
    @Contract("_ -> new")
    @NotNull
    default TextComponent text(@NotNull final Consumer<? super TextComponent.Builder> consumer) {
        return Buildable.configureAndBuild(text(), consumer);
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default TextComponent text(@NotNull final String content) {
        if (content.isEmpty()) {
            return empty();
        }
        return new TextComponentImpl(Collections.emptyList(), Style.empty(), content);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(@NotNull final String content, @NotNull final Style style) {
        return new TextComponentImpl(Collections.emptyList(), style, content);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(@NotNull final String content, @Nullable final TextColor color) {
        return new TextComponentImpl(Collections.emptyList(), Style.style(color), content);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(@NotNull final String content, @Nullable final TextColor color, final TextDecoration... decorations) {
        return new TextComponentImpl(Collections.emptyList(), Style.style(color, decorations), content);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(@NotNull final String content, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return new TextComponentImpl(Collections.emptyList(), Style.style(color, decorations), content);
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default TextComponent text(final boolean value) {
        return text(String.valueOf(value));
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final boolean value, @NotNull final Style style) {
        return text(String.valueOf(value), style);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final boolean value, @Nullable final TextColor color) {
        return text(String.valueOf(value), color);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final boolean value, @Nullable final TextColor color, final TextDecoration... decorations) {
        return text(String.valueOf(value), color, decorations);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final boolean value, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(String.valueOf(value), color, decorations);
    }
    
    @Contract(pure = true)
    @NotNull
    default TextComponent text(final char value) {
        if (value == '\n') {
            return newline();
        }
        if (value == ' ') {
            return space();
        }
        return text(String.valueOf(value));
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final char value, @NotNull final Style style) {
        return text(String.valueOf(value), style);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final char value, @Nullable final TextColor color) {
        return text(String.valueOf(value), color);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final char value, @Nullable final TextColor color, final TextDecoration... decorations) {
        return text(String.valueOf(value), color, decorations);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final char value, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(String.valueOf(value), color, decorations);
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default TextComponent text(final double value) {
        return text(String.valueOf(value));
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final double value, @NotNull final Style style) {
        return text(String.valueOf(value), style);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final double value, @Nullable final TextColor color) {
        return text(String.valueOf(value), color);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final double value, @Nullable final TextColor color, final TextDecoration... decorations) {
        return text(String.valueOf(value), color, decorations);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final double value, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(String.valueOf(value), color, decorations);
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default TextComponent text(final float value) {
        return text(String.valueOf(value));
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final float value, @NotNull final Style style) {
        return text(String.valueOf(value), style);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final float value, @Nullable final TextColor color) {
        return text(String.valueOf(value), color);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final float value, @Nullable final TextColor color, final TextDecoration... decorations) {
        return text(String.valueOf(value), color, decorations);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final float value, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(String.valueOf(value), color, decorations);
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default TextComponent text(final int value) {
        return text(String.valueOf(value));
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final int value, @NotNull final Style style) {
        return text(String.valueOf(value), style);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final int value, @Nullable final TextColor color) {
        return text(String.valueOf(value), color);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final int value, @Nullable final TextColor color, final TextDecoration... decorations) {
        return text(String.valueOf(value), color, decorations);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final int value, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(String.valueOf(value), color, decorations);
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default TextComponent text(final long value) {
        return text(String.valueOf(value));
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final long value, @NotNull final Style style) {
        return text(String.valueOf(value), style);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final long value, @Nullable final TextColor color) {
        return text(String.valueOf(value), color);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final long value, @Nullable final TextColor color, final TextDecoration... decorations) {
        return text(String.valueOf(value), color, decorations);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TextComponent text(final long value, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return text(String.valueOf(value), color, decorations);
    }
    
    @Contract(pure = true)
    default TranslatableComponent.Builder translatable() {
        return new TranslatableComponentImpl.BuilderImpl();
    }
    
    @Contract("_ -> new")
    @NotNull
    default TranslatableComponent translatable(@NotNull final Consumer<? super TranslatableComponent.Builder> consumer) {
        return Buildable.configureAndBuild(translatable(), consumer);
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final String key) {
        return translatable(key, Style.empty());
    }
    
    @Contract(value = "_ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final Translatable translatable) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), Style.empty());
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final String key, @NotNull final Style style) {
        return new TranslatableComponentImpl(Collections.emptyList(), style, key, Collections.emptyList());
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final Translatable translatable, @NotNull final Style style) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), style);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color) {
        return translatable(key, Style.style(color));
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), color);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color, final TextDecoration... decorations) {
        return translatable(key, Style.style(color, decorations));
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color, final TextDecoration... decorations) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), color, decorations);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return translatable(key, Style.style(color, decorations));
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), color, decorations);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final String key, @NotNull final ComponentLike... args) {
        return translatable(key, Style.empty(), args);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final Translatable translatable, @NotNull final ComponentLike... args) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), args);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final String key, @NotNull final Style style, @NotNull final ComponentLike... args) {
        return new TranslatableComponentImpl(Collections.emptyList(), style, key, args);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final Translatable translatable, @NotNull final Style style, @NotNull final ComponentLike... args) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), style, args);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color, @NotNull final ComponentLike... args) {
        return translatable(key, Style.style(color), args);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color, @NotNull final ComponentLike... args) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), color, args);
    }
    
    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations, @NotNull final ComponentLike... args) {
        return translatable(key, Style.style(color, decorations), args);
    }
    
    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations, @NotNull final ComponentLike... args) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), color, decorations, args);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final String key, @NotNull final List<? extends ComponentLike> args) {
        return new TranslatableComponentImpl(Collections.emptyList(), Style.empty(), key, args);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final Translatable translatable, @NotNull final List<? extends ComponentLike> args) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), args);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final String key, @NotNull final Style style, @NotNull final List<? extends ComponentLike> args) {
        return new TranslatableComponentImpl(Collections.emptyList(), style, key, args);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final Translatable translatable, @NotNull final Style style, @NotNull final List<? extends ComponentLike> args) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), style, args);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    default TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color, @NotNull final List<? extends ComponentLike> args) {
        return translatable(key, Style.style(color), args);
    }
    
    @Contract(value = "_, _, _ -> new", pure = true)
    default TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color, @NotNull final List<? extends ComponentLike> args) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), color, args);
    }
    
    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final String key, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations, @NotNull final List<? extends ComponentLike> args) {
        return translatable(key, Style.style(color, decorations), args);
    }
    
    @Contract(value = "_, _, _, _ -> new", pure = true)
    @NotNull
    default TranslatableComponent translatable(@NotNull final Translatable translatable, @Nullable final TextColor color, @NotNull final Set<TextDecoration> decorations, @NotNull final List<? extends ComponentLike> args) {
        return translatable(Objects.requireNonNull(translatable, "translatable").translationKey(), color, decorations, args);
    }
    
    @NotNull
    List<Component> children();
    
    @Contract(pure = true)
    @NotNull
    Component children(@NotNull final List<? extends ComponentLike> children);
    
    default boolean contains(@NotNull final Component that) {
        return this.contains(that, Component.EQUALS_IDENTITY);
    }
    
    default boolean contains(@NotNull final Component that, @NotNull final BiPredicate<? super Component, ? super Component> equals) {
        if (equals.test(this, that)) {
            return true;
        }
        for (final Component child : this.children()) {
            if (child.contains(that, equals)) {
                return true;
            }
        }
        final HoverEvent<?> hoverEvent = this.hoverEvent();
        if (hoverEvent != null) {
            final Object value = hoverEvent.value();
            Component component = null;
            if (value instanceof Component) {
                component = (Component)hoverEvent.value();
            }
            else if (value instanceof HoverEvent.ShowEntity) {
                component = ((HoverEvent.ShowEntity)value).name();
            }
            if (component != null) {
                if (equals.test(that, component)) {
                    return true;
                }
                for (final Component child2 : component.children()) {
                    if (child2.contains(that, equals)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @Deprecated
    default void detectCycle(@NotNull final Component that) {
        if (that.contains(this)) {
            throw new IllegalStateException("Component cycle detected between " + this + " and " + that);
        }
    }
    
    @Contract(pure = true)
    @NotNull
    Component append(@NotNull final Component component);
    
    @NotNull
    default Component append(@NotNull final ComponentLike component) {
        return this.append(component.asComponent());
    }
    
    @Contract(pure = true)
    @NotNull
    default Component append(@NotNull final ComponentBuilder<?, ?> builder) {
        return this.append((Component)builder.build());
    }
    
    @NotNull
    Style style();
    
    @Contract(pure = true)
    @NotNull
    Component style(@NotNull final Style style);
    
    @Contract(pure = true)
    @NotNull
    default Component style(@NotNull final Consumer<Style.Builder> consumer) {
        return this.style(this.style().edit(consumer));
    }
    
    @Contract(pure = true)
    @NotNull
    default Component style(@NotNull final Consumer<Style.Builder> consumer, final Style.Merge.Strategy strategy) {
        return this.style(this.style().edit(consumer, strategy));
    }
    
    @Contract(pure = true)
    @NotNull
    default Component style(final Style.Builder style) {
        return this.style(style.build());
    }
    
    @Contract(pure = true)
    @NotNull
    default Component mergeStyle(@NotNull final Component that) {
        return this.mergeStyle(that, Style.Merge.all());
    }
    
    @Contract(pure = true)
    @NotNull
    default Component mergeStyle(@NotNull final Component that, final Style.Merge... merges) {
        return this.mergeStyle(that, Style.Merge.of(merges));
    }
    
    @Contract(pure = true)
    @NotNull
    default Component mergeStyle(@NotNull final Component that, @NotNull final Set<Style.Merge> merges) {
        return this.style(this.style().merge(that.style(), merges));
    }
    
    @Nullable
    default TextColor color() {
        return this.style().color();
    }
    
    @Contract(pure = true)
    @NotNull
    default Component color(@Nullable final TextColor color) {
        return this.style(this.style().color(color));
    }
    
    @Contract(pure = true)
    @NotNull
    default Component colorIfAbsent(@Nullable final TextColor color) {
        if (this.color() == null) {
            return this.color(color);
        }
        return this;
    }
    
    default boolean hasDecoration(@NotNull final TextDecoration decoration) {
        return this.decoration(decoration) == TextDecoration.State.TRUE;
    }
    
    @Contract(pure = true)
    @NotNull
    default Component decorate(@NotNull final TextDecoration decoration) {
        return this.decoration(decoration, TextDecoration.State.TRUE);
    }
    
    default TextDecoration.State decoration(@NotNull final TextDecoration decoration) {
        return this.style().decoration(decoration);
    }
    
    @Contract(pure = true)
    @NotNull
    default Component decoration(@NotNull final TextDecoration decoration, final boolean flag) {
        return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
    }
    
    @Contract(pure = true)
    @NotNull
    default Component decoration(@NotNull final TextDecoration decoration, final TextDecoration.State state) {
        return this.style(this.style().decoration(decoration, state));
    }
    
    @NotNull
    default Map<TextDecoration, TextDecoration.State> decorations() {
        return this.style().decorations();
    }
    
    @Contract(pure = true)
    @NotNull
    default Component decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations) {
        return this.style(this.style().decorations(decorations));
    }
    
    @Nullable
    default ClickEvent clickEvent() {
        return this.style().clickEvent();
    }
    
    @Contract(pure = true)
    @NotNull
    default Component clickEvent(@Nullable final ClickEvent event) {
        return this.style(this.style().clickEvent(event));
    }
    
    @Nullable
    default HoverEvent<?> hoverEvent() {
        return this.style().hoverEvent();
    }
    
    @Contract(pure = true)
    @NotNull
    default Component hoverEvent(@Nullable final HoverEventSource<?> source) {
        return this.style(this.style().hoverEvent(source));
    }
    
    @Nullable
    default String insertion() {
        return this.style().insertion();
    }
    
    @Contract(pure = true)
    @NotNull
    default Component insertion(@Nullable final String insertion) {
        return this.style(this.style().insertion(insertion));
    }
    
    default boolean hasStyling() {
        return !this.style().isEmpty();
    }
    
    @Contract(pure = true)
    @NotNull
    Component replaceText(@NotNull final Consumer<TextReplacementConfig.Builder> configurer);
    
    @Contract(pure = true)
    @NotNull
    Component replaceText(@NotNull final TextReplacementConfig config);
    
    @NotNull
    Component compact();
    
    @NotNull
    default Iterable<Component> iterable(@NotNull final ComponentIteratorType type, @NotNull final ComponentIteratorFlag... flags) {
        return this.iterable(type, (flags == null) ? Collections.emptySet() : MonkeyBars.enumSet(ComponentIteratorFlag.class, flags));
    }
    
    @NotNull
    default Iterable<Component> iterable(@NotNull final ComponentIteratorType type, @NotNull final Set<ComponentIteratorFlag> flags) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(flags, "flags");
        return new ForwardingIterator<Component>(() -> this.iterator(type, flags), () -> this.spliterator(type, flags));
    }
    
    @NotNull
    default Iterator<Component> iterator(@NotNull final ComponentIteratorType type, @NotNull final ComponentIteratorFlag... flags) {
        return this.iterator(type, (flags == null) ? Collections.emptySet() : MonkeyBars.enumSet(ComponentIteratorFlag.class, flags));
    }
    
    @NotNull
    default Iterator<Component> iterator(@NotNull final ComponentIteratorType type, @NotNull final Set<ComponentIteratorFlag> flags) {
        return new ComponentIterator(this, Objects.requireNonNull(type, "type"), Objects.requireNonNull(flags, "flags"));
    }
    
    @NotNull
    default Spliterator<Component> spliterator(@NotNull final ComponentIteratorType type, @NotNull final ComponentIteratorFlag... flags) {
        return this.spliterator(type, (flags == null) ? Collections.emptySet() : MonkeyBars.enumSet(ComponentIteratorFlag.class, flags));
    }
    
    @NotNull
    default Spliterator<Component> spliterator(@NotNull final ComponentIteratorType type, @NotNull final Set<ComponentIteratorFlag> flags) {
        return Spliterators.spliteratorUnknownSize((Iterator<? extends Component>)this.iterator(type, flags), 0);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final String search, @Nullable final ComponentLike replacement) {
        return this.replaceText(b -> b.matchLiteral(search).replacement(replacement));
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final Pattern pattern, @NotNull final Function<TextComponent.Builder, ComponentLike> replacement) {
        return this.replaceText(b -> b.match(pattern).replacement(replacement));
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @Contract(pure = true)
    @NotNull
    default Component replaceFirstText(@NotNull final String search, @Nullable final ComponentLike replacement) {
        return this.replaceText(b -> b.matchLiteral(search).once().replacement(replacement));
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @Contract(pure = true)
    @NotNull
    default Component replaceFirstText(@NotNull final Pattern pattern, @NotNull final Function<TextComponent.Builder, ComponentLike> replacement) {
        return this.replaceText(b -> b.match(pattern).once().replacement(replacement));
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final String search, @Nullable final ComponentLike replacement, final int numberOfReplacements) {
        return this.replaceText(b -> b.matchLiteral(search).times(numberOfReplacements).replacement(replacement));
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final Pattern pattern, @NotNull final Function<TextComponent.Builder, ComponentLike> replacement, final int numberOfReplacements) {
        return this.replaceText(b -> b.match(pattern).times(numberOfReplacements).replacement(replacement));
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final String search, @Nullable final ComponentLike replacement, @NotNull final IntFunction2<PatternReplacementResult> fn) {
        return this.replaceText(b -> b.matchLiteral(search).replacement(replacement).condition(fn));
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @Contract(pure = true)
    @NotNull
    default Component replaceText(@NotNull final Pattern pattern, @NotNull final Function<TextComponent.Builder, ComponentLike> replacement, @NotNull final IntFunction2<PatternReplacementResult> fn) {
        return this.replaceText(b -> b.match(pattern).replacement(replacement).condition(fn));
    }
    
    default void componentBuilderApply(@NotNull final ComponentBuilder<?, ?> component) {
        component.append(this);
    }
    
    @NotNull
    default Component asComponent() {
        return this;
    }
    
    @NotNull
    default HoverEvent<Component> asHoverEvent(@NotNull final UnaryOperator<Component> op) {
        return HoverEvent.showText(op.apply(this));
    }
}
