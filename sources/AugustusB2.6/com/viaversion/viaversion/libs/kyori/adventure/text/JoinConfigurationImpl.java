// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text;

import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.Contract;
import java.util.Iterator;
import java.util.Objects;
import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.function.Predicate;
import java.util.function.Function;

final class JoinConfigurationImpl implements JoinConfiguration
{
    static final Function<ComponentLike, Component> DEFAULT_CONVERTOR;
    static final Predicate<ComponentLike> DEFAULT_PREDICATE;
    static final JoinConfigurationImpl NULL;
    private final Component prefix;
    private final Component suffix;
    private final Component separator;
    private final Component lastSeparator;
    private final Component lastSeparatorIfSerial;
    private final Function<ComponentLike, Component> convertor;
    private final Predicate<ComponentLike> predicate;
    
    private JoinConfigurationImpl() {
        this.prefix = null;
        this.suffix = null;
        this.separator = null;
        this.lastSeparator = null;
        this.lastSeparatorIfSerial = null;
        this.convertor = JoinConfigurationImpl.DEFAULT_CONVERTOR;
        this.predicate = JoinConfigurationImpl.DEFAULT_PREDICATE;
    }
    
    private JoinConfigurationImpl(@NotNull final BuilderImpl builder) {
        this.prefix = ((builder.prefix == null) ? null : builder.prefix.asComponent());
        this.suffix = ((builder.suffix == null) ? null : builder.suffix.asComponent());
        this.separator = ((builder.separator == null) ? null : builder.separator.asComponent());
        this.lastSeparator = ((builder.lastSeparator == null) ? null : builder.lastSeparator.asComponent());
        this.lastSeparatorIfSerial = ((builder.lastSeparatorIfSerial == null) ? null : builder.lastSeparatorIfSerial.asComponent());
        this.convertor = builder.convertor;
        this.predicate = builder.predicate;
    }
    
    @Nullable
    @Override
    public Component prefix() {
        return this.prefix;
    }
    
    @Nullable
    @Override
    public Component suffix() {
        return this.suffix;
    }
    
    @Nullable
    @Override
    public Component separator() {
        return this.separator;
    }
    
    @Nullable
    @Override
    public Component lastSeparator() {
        return this.lastSeparator;
    }
    
    @Nullable
    @Override
    public Component lastSeparatorIfSerial() {
        return this.lastSeparatorIfSerial;
    }
    
    @NotNull
    @Override
    public Function<ComponentLike, Component> convertor() {
        return this.convertor;
    }
    
    @NotNull
    @Override
    public Predicate<ComponentLike> predicate() {
        return this.predicate;
    }
    
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("prefix", this.prefix), ExaminableProperty.of("suffix", this.suffix), ExaminableProperty.of("separator", this.separator), ExaminableProperty.of("lastSeparator", this.lastSeparator), ExaminableProperty.of("lastSeparatorIfSerial", this.lastSeparatorIfSerial), ExaminableProperty.of("convertor", this.convertor), ExaminableProperty.of("predicate", this.predicate) });
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
    
    @Contract(pure = true)
    @NotNull
    static Component join(@NotNull final JoinConfiguration config, @NotNull final Iterable<? extends ComponentLike> components) {
        Objects.requireNonNull(config, "config");
        Objects.requireNonNull(components, "components");
        final Iterator<? extends ComponentLike> it = components.iterator();
        final Component prefix = config.prefix();
        final Component suffix = config.suffix();
        final Function<ComponentLike, Component> convertor = config.convertor();
        final Predicate<ComponentLike> predicate = config.predicate();
        if (!it.hasNext()) {
            return singleElementJoin(config, null);
        }
        ComponentLike component = Objects.requireNonNull((ComponentLike)it.next(), "Null elements in \"components\" are not allowed");
        int componentsSeen = 0;
        if (!it.hasNext()) {
            return singleElementJoin(config, component);
        }
        final Component separator = config.separator();
        final boolean hasSeparator = separator != null;
        final TextComponent.Builder builder = Component.text();
        if (prefix != null) {
            builder.append(prefix);
        }
        while (component != null) {
            if (!predicate.test(component)) {
                if (!it.hasNext()) {
                    break;
                }
                component = (ComponentLike)it.next();
            }
            else {
                builder.append(Objects.requireNonNull(convertor.apply(component), "Null output from \"convertor\" is not allowed"));
                ++componentsSeen;
                if (!it.hasNext()) {
                    component = null;
                }
                else {
                    component = Objects.requireNonNull((ComponentLike)it.next(), "Null elements in \"components\" are not allowed");
                    if (it.hasNext()) {
                        if (!hasSeparator) {
                            continue;
                        }
                        builder.append(separator);
                    }
                    else {
                        Component lastSeparator = null;
                        if (componentsSeen > 1) {
                            lastSeparator = config.lastSeparatorIfSerial();
                        }
                        if (lastSeparator == null) {
                            lastSeparator = config.lastSeparator();
                        }
                        if (lastSeparator == null) {
                            lastSeparator = config.separator();
                        }
                        if (lastSeparator == null) {
                            continue;
                        }
                        builder.append(lastSeparator);
                    }
                }
            }
        }
        if (suffix != null) {
            builder.append(suffix);
        }
        return ((ComponentBuilder<Component, B>)builder).build();
    }
    
    @NotNull
    static Component singleElementJoin(@NotNull final JoinConfiguration config, @Nullable final ComponentLike component) {
        final Component prefix = config.prefix();
        final Component suffix = config.suffix();
        final Function<ComponentLike, Component> convertor = config.convertor();
        final Predicate<ComponentLike> predicate = config.predicate();
        if (prefix != null || suffix != null) {
            final TextComponent.Builder builder = Component.text();
            if (prefix != null) {
                builder.append(prefix);
            }
            if (component != null && predicate.test(component)) {
                builder.append(convertor.apply(component));
            }
            if (suffix != null) {
                builder.append(suffix);
            }
            return ((ComponentBuilder<Component, B>)builder).build();
        }
        if (component == null || !predicate.test(component)) {
            return Component.empty();
        }
        return convertor.apply(component);
    }
    
    static {
        DEFAULT_CONVERTOR = ComponentLike::asComponent;
        DEFAULT_PREDICATE = (componentLike -> true);
        NULL = new JoinConfigurationImpl();
    }
    
    static final class BuilderImpl implements Builder
    {
        private ComponentLike prefix;
        private ComponentLike suffix;
        private ComponentLike separator;
        private ComponentLike lastSeparator;
        private ComponentLike lastSeparatorIfSerial;
        private Function<ComponentLike, Component> convertor;
        private Predicate<ComponentLike> predicate;
        
        BuilderImpl() {
            this(JoinConfigurationImpl.NULL);
        }
        
        private BuilderImpl(@NotNull final JoinConfigurationImpl joinConfig) {
            this.separator = joinConfig.separator;
            this.lastSeparator = joinConfig.lastSeparator;
            this.prefix = joinConfig.prefix;
            this.suffix = joinConfig.suffix;
            this.convertor = joinConfig.convertor;
            this.lastSeparatorIfSerial = joinConfig.lastSeparatorIfSerial;
            this.predicate = joinConfig.predicate;
        }
        
        @NotNull
        @Override
        public Builder prefix(@Nullable final ComponentLike prefix) {
            this.prefix = prefix;
            return this;
        }
        
        @NotNull
        @Override
        public Builder suffix(@Nullable final ComponentLike suffix) {
            this.suffix = suffix;
            return this;
        }
        
        @NotNull
        @Override
        public Builder separator(@Nullable final ComponentLike separator) {
            this.separator = separator;
            return this;
        }
        
        @NotNull
        @Override
        public Builder lastSeparator(@Nullable final ComponentLike lastSeparator) {
            this.lastSeparator = lastSeparator;
            return this;
        }
        
        @NotNull
        @Override
        public Builder lastSeparatorIfSerial(@Nullable final ComponentLike lastSeparatorIfSerial) {
            this.lastSeparatorIfSerial = lastSeparatorIfSerial;
            return this;
        }
        
        @NotNull
        @Override
        public Builder convertor(@NotNull final Function<ComponentLike, Component> convertor) {
            this.convertor = Objects.requireNonNull(convertor, "convertor");
            return this;
        }
        
        @NotNull
        @Override
        public Builder predicate(@NotNull final Predicate<ComponentLike> predicate) {
            this.predicate = Objects.requireNonNull(predicate, "predicate");
            return this;
        }
        
        @NotNull
        @Override
        public JoinConfiguration build() {
            return new JoinConfigurationImpl(this, null);
        }
    }
}
