// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import java.util.function.Function;
import com.viaversion.viaversion.libs.kyori.adventure.util.Services;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.gson.TypeAdapterFactory;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.gson.GsonBuilder;
import java.util.function.UnaryOperator;
import com.viaversion.viaversion.libs.gson.Gson;
import java.util.function.Consumer;
import java.util.Optional;

final class GsonComponentSerializerImpl implements GsonComponentSerializer
{
    private static final Optional<Provider> SERVICE;
    static final Consumer<Builder> BUILDER;
    private final Gson serializer;
    private final UnaryOperator<GsonBuilder> populator;
    private final boolean downsampleColor;
    @Nullable
    private final LegacyHoverEventSerializer legacyHoverSerializer;
    private final boolean emitLegacyHover;
    
    GsonComponentSerializerImpl(final boolean downsampleColor, @Nullable final LegacyHoverEventSerializer legacyHoverSerializer, final boolean emitLegacyHover) {
        this.downsampleColor = downsampleColor;
        this.legacyHoverSerializer = legacyHoverSerializer;
        this.emitLegacyHover = emitLegacyHover;
        this.populator = (UnaryOperator<GsonBuilder>)(builder -> {
            builder.registerTypeAdapterFactory(new SerializerFactory(downsampleColor, legacyHoverSerializer, emitLegacyHover));
            return builder;
        });
        this.serializer = this.populator.apply(new GsonBuilder()).create();
    }
    
    @NotNull
    @Override
    public Gson serializer() {
        return this.serializer;
    }
    
    @NotNull
    @Override
    public UnaryOperator<GsonBuilder> populator() {
        return this.populator;
    }
    
    @NotNull
    @Override
    public Component deserialize(@NotNull final String string) {
        final Component component = this.serializer().fromJson(string, Component.class);
        if (component == null) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(string);
        }
        return component;
    }
    
    @NotNull
    @Override
    public String serialize(@NotNull final Component component) {
        return this.serializer().toJson(component);
    }
    
    @NotNull
    @Override
    public Component deserializeFromTree(@NotNull final JsonElement input) {
        final Component component = this.serializer().fromJson(input, Component.class);
        if (component == null) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(input);
        }
        return component;
    }
    
    @NotNull
    @Override
    public JsonElement serializeToTree(@NotNull final Component component) {
        return this.serializer().toJsonTree(component);
    }
    
    @NotNull
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static {
        SERVICE = Services.service(Provider.class);
        BUILDER = GsonComponentSerializerImpl.SERVICE.map((Function<? super Provider, ? extends Consumer<Builder>>)Provider::builder).orElseGet(() -> builder -> {});
    }
    
    static final class Instances
    {
        static final GsonComponentSerializer INSTANCE;
        static final GsonComponentSerializer LEGACY_INSTANCE;
        
        static {
            INSTANCE = GsonComponentSerializerImpl.SERVICE.map(Provider::gson).orElseGet(() -> new GsonComponentSerializerImpl(false, null, false));
            LEGACY_INSTANCE = GsonComponentSerializerImpl.SERVICE.map(Provider::gsonLegacy).orElseGet(() -> new GsonComponentSerializerImpl(true, null, true));
        }
    }
    
    static final class BuilderImpl implements Builder
    {
        private boolean downsampleColor;
        @Nullable
        private LegacyHoverEventSerializer legacyHoverSerializer;
        private boolean emitLegacyHover;
        
        BuilderImpl() {
            this.downsampleColor = false;
            this.emitLegacyHover = false;
            GsonComponentSerializerImpl.BUILDER.accept(this);
        }
        
        BuilderImpl(final GsonComponentSerializerImpl serializer) {
            this();
            this.downsampleColor = serializer.downsampleColor;
            this.emitLegacyHover = serializer.emitLegacyHover;
            this.legacyHoverSerializer = serializer.legacyHoverSerializer;
        }
        
        @NotNull
        @Override
        public Builder downsampleColors() {
            this.downsampleColor = true;
            return this;
        }
        
        @NotNull
        @Override
        public Builder legacyHoverEventSerializer(@Nullable final LegacyHoverEventSerializer serializer) {
            this.legacyHoverSerializer = serializer;
            return this;
        }
        
        @NotNull
        @Override
        public Builder emitLegacyHoverEvent() {
            this.emitLegacyHover = true;
            return this;
        }
        
        @NotNull
        @Override
        public GsonComponentSerializer build() {
            if (this.legacyHoverSerializer == null) {
                return this.downsampleColor ? Instances.LEGACY_INSTANCE : Instances.INSTANCE;
            }
            return new GsonComponentSerializerImpl(this.downsampleColor, this.legacyHoverSerializer, this.emitLegacyHover);
        }
    }
}
