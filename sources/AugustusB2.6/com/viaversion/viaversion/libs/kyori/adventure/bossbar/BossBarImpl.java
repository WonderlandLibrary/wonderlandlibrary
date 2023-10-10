// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.bossbar;

import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.function.Consumer;
import java.util.Iterator;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.Collections;
import java.util.Collection;
import java.util.Objects;
import java.util.EnumSet;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

final class BossBarImpl extends HackyBossBarPlatformBridge implements BossBar
{
    private static final BiConsumer<BossBarImpl, Set<Flag>> FLAGS_ADDED;
    private static final BiConsumer<BossBarImpl, Set<Flag>> FLAGS_REMOVED;
    private final List<Listener> listeners;
    private Component name;
    private float progress;
    private Color color;
    private Overlay overlay;
    private final Set<Flag> flags;
    
    BossBarImpl(@NotNull final Component name, final float progress, @NotNull final Color color, @NotNull final Overlay overlay) {
        this.listeners = new CopyOnWriteArrayList<Listener>();
        this.flags = EnumSet.noneOf(Flag.class);
        this.name = Objects.requireNonNull(name, "name");
        this.progress = progress;
        this.color = Objects.requireNonNull(color, "color");
        this.overlay = Objects.requireNonNull(overlay, "overlay");
    }
    
    BossBarImpl(@NotNull final Component name, final float progress, @NotNull final Color color, @NotNull final Overlay overlay, @NotNull final Set<Flag> flags) {
        this(name, progress, color, overlay);
        this.flags.addAll(flags);
    }
    
    @NotNull
    @Override
    public Component name() {
        return this.name;
    }
    
    @NotNull
    @Override
    public BossBar name(@NotNull final Component newName) {
        Objects.requireNonNull(newName, "name");
        final Component oldName = this.name;
        if (!Objects.equals(newName, oldName)) {
            this.name = newName;
            this.forEachListener(listener -> listener.bossBarNameChanged(this, oldName, newName));
        }
        return this;
    }
    
    @Override
    public float progress() {
        return this.progress;
    }
    
    @NotNull
    @Override
    public BossBar progress(final float newProgress) {
        checkProgress(newProgress);
        final float oldProgress = this.progress;
        if (newProgress != oldProgress) {
            this.progress = newProgress;
            this.forEachListener(listener -> listener.bossBarProgressChanged(this, oldProgress, newProgress));
        }
        return this;
    }
    
    static void checkProgress(final float progress) {
        if (progress < 0.0f || progress > 1.0f) {
            throw new IllegalArgumentException("progress must be between 0.0 and 1.0, was " + progress);
        }
    }
    
    @NotNull
    @Override
    public Color color() {
        return this.color;
    }
    
    @NotNull
    @Override
    public BossBar color(@NotNull final Color newColor) {
        Objects.requireNonNull(newColor, "color");
        final Color oldColor = this.color;
        if (newColor != oldColor) {
            this.color = newColor;
            this.forEachListener(listener -> listener.bossBarColorChanged(this, oldColor, newColor));
        }
        return this;
    }
    
    @NotNull
    @Override
    public Overlay overlay() {
        return this.overlay;
    }
    
    @NotNull
    @Override
    public BossBar overlay(@NotNull final Overlay newOverlay) {
        Objects.requireNonNull(newOverlay, "overlay");
        final Overlay oldOverlay = this.overlay;
        if (newOverlay != oldOverlay) {
            this.overlay = newOverlay;
            this.forEachListener(listener -> listener.bossBarOverlayChanged(this, oldOverlay, newOverlay));
        }
        return this;
    }
    
    @NotNull
    @Override
    public Set<Flag> flags() {
        return Collections.unmodifiableSet((Set<? extends Flag>)this.flags);
    }
    
    @NotNull
    @Override
    public BossBar flags(@NotNull final Set<Flag> newFlags) {
        if (newFlags.isEmpty()) {
            final Set<Flag> oldFlags = EnumSet.copyOf(this.flags);
            this.flags.clear();
            this.forEachListener(listener -> listener.bossBarFlagsChanged(this, Collections.emptySet(), oldFlags));
        }
        else if (!this.flags.equals(newFlags)) {
            final Set<Flag> oldFlags = EnumSet.copyOf(this.flags);
            this.flags.clear();
            this.flags.addAll(newFlags);
            final EnumSet<Flag> copy;
            final Set<Flag> added = copy = EnumSet.copyOf(newFlags);
            final Set<Flag> obj = oldFlags;
            Objects.requireNonNull(obj);
            copy.removeIf(obj::contains);
            final EnumSet<Flag> copy2;
            final Set<Flag> removed = copy2 = EnumSet.copyOf(oldFlags);
            final Set<Flag> flags = this.flags;
            Objects.requireNonNull(flags);
            copy2.removeIf(flags::contains);
            this.forEachListener(listener -> listener.bossBarFlagsChanged(this, added, removed));
        }
        return this;
    }
    
    @Override
    public boolean hasFlag(@NotNull final Flag flag) {
        return this.flags.contains(flag);
    }
    
    @NotNull
    @Override
    public BossBar addFlag(@NotNull final Flag flag) {
        return this.editFlags(flag, Set::add, BossBarImpl.FLAGS_ADDED);
    }
    
    @NotNull
    @Override
    public BossBar removeFlag(@NotNull final Flag flag) {
        return this.editFlags(flag, Set::remove, BossBarImpl.FLAGS_REMOVED);
    }
    
    @NotNull
    private BossBar editFlags(@NotNull final Flag flag, @NotNull final BiPredicate<Set<Flag>, Flag> predicate, final BiConsumer<BossBarImpl, Set<Flag>> onChange) {
        if (predicate.test(this.flags, flag)) {
            onChange.accept(this, Collections.singleton(flag));
        }
        return this;
    }
    
    @NotNull
    @Override
    public BossBar addFlags(@NotNull final Flag... flags) {
        return this.editFlags(flags, Set::add, BossBarImpl.FLAGS_ADDED);
    }
    
    @NotNull
    @Override
    public BossBar removeFlags(@NotNull final Flag... flags) {
        return this.editFlags(flags, Set::remove, BossBarImpl.FLAGS_REMOVED);
    }
    
    @NotNull
    private BossBar editFlags(final Flag[] flags, final BiPredicate<Set<Flag>, Flag> predicate, final BiConsumer<BossBarImpl, Set<Flag>> onChange) {
        if (flags.length == 0) {
            return this;
        }
        Set<Flag> changes = null;
        for (int i = 0, length = flags.length; i < length; ++i) {
            if (predicate.test(this.flags, flags[i])) {
                if (changes == null) {
                    changes = EnumSet.noneOf(Flag.class);
                }
                changes.add(flags[i]);
            }
        }
        if (changes != null) {
            onChange.accept(this, changes);
        }
        return this;
    }
    
    @NotNull
    @Override
    public BossBar addFlags(@NotNull final Iterable<Flag> flags) {
        return this.editFlags(flags, Set::add, BossBarImpl.FLAGS_ADDED);
    }
    
    @NotNull
    @Override
    public BossBar removeFlags(@NotNull final Iterable<Flag> flags) {
        return this.editFlags(flags, Set::remove, BossBarImpl.FLAGS_REMOVED);
    }
    
    @NotNull
    private BossBar editFlags(final Iterable<Flag> flags, final BiPredicate<Set<Flag>, Flag> predicate, final BiConsumer<BossBarImpl, Set<Flag>> onChange) {
        Set<Flag> changes = null;
        for (final Flag flag : flags) {
            if (predicate.test(this.flags, flag)) {
                if (changes == null) {
                    changes = EnumSet.noneOf(Flag.class);
                }
                changes.add(flag);
            }
        }
        if (changes != null) {
            onChange.accept(this, changes);
        }
        return this;
    }
    
    @NotNull
    @Override
    public BossBar addListener(@NotNull final Listener listener) {
        this.listeners.add(listener);
        return this;
    }
    
    @NotNull
    @Override
    public BossBar removeListener(@NotNull final Listener listener) {
        this.listeners.remove(listener);
        return this;
    }
    
    private void forEachListener(@NotNull final Consumer<Listener> consumer) {
        for (final Listener listener : this.listeners) {
            consumer.accept(listener);
        }
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("name", this.name), ExaminableProperty.of("progress", this.progress), ExaminableProperty.of("color", this.color), ExaminableProperty.of("overlay", this.overlay), ExaminableProperty.of("flags", this.flags) });
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
    
    static {
        FLAGS_ADDED = ((bar, flagsAdded) -> bar.forEachListener(listener -> listener.bossBarFlagsChanged(bar, flagsAdded, Collections.emptySet())));
        FLAGS_REMOVED = ((bar, flagsRemoved) -> bar.forEachListener(listener -> listener.bossBarFlagsChanged(bar, Collections.emptySet(), flagsRemoved)));
    }
}
