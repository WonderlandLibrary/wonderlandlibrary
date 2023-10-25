/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson;

import com.viaversion.viaversion.libs.gson.Gson;
import com.viaversion.viaversion.libs.gson.GsonBuilder;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.adventure.text.BlockNBTComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.HoverEvent;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.BlockNBTComponentPosSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.ComponentSerializerImpl;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.IndexedSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.KeySerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.LegacyHoverEventSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.ShowEntitySerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.ShowItemSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.StyleSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.TextColorSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.TextColorWrapper;
import com.viaversion.viaversion.libs.kyori.adventure.util.Services;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class GsonComponentSerializerImpl
implements GsonComponentSerializer {
    private static final Optional<GsonComponentSerializer.Provider> SERVICE = Services.service(GsonComponentSerializer.Provider.class);
    static final Consumer<GsonComponentSerializer.Builder> BUILDER = SERVICE.map(GsonComponentSerializer.Provider::builder).orElseGet(() -> builder -> {});
    private final Gson serializer;
    private final UnaryOperator<GsonBuilder> populator;
    private final boolean downsampleColor;
    @Nullable
    private final LegacyHoverEventSerializer legacyHoverSerializer;
    private final boolean emitLegacyHover;

    GsonComponentSerializerImpl(boolean downsampleColor, @Nullable LegacyHoverEventSerializer legacyHoverSerializer, boolean emitLegacyHover) {
        this.downsampleColor = downsampleColor;
        this.legacyHoverSerializer = legacyHoverSerializer;
        this.emitLegacyHover = emitLegacyHover;
        this.populator = builder -> {
            builder.registerTypeHierarchyAdapter(Key.class, KeySerializer.INSTANCE);
            builder.registerTypeHierarchyAdapter(Component.class, new ComponentSerializerImpl());
            builder.registerTypeHierarchyAdapter(Style.class, new StyleSerializer(legacyHoverSerializer, emitLegacyHover));
            builder.registerTypeAdapter((Type)((Object)ClickEvent.Action.class), IndexedSerializer.of("click action", ClickEvent.Action.NAMES));
            builder.registerTypeAdapter((Type)((Object)HoverEvent.Action.class), IndexedSerializer.of("hover action", HoverEvent.Action.NAMES));
            builder.registerTypeAdapter((Type)((Object)HoverEvent.ShowItem.class), new ShowItemSerializer());
            builder.registerTypeAdapter((Type)((Object)HoverEvent.ShowEntity.class), new ShowEntitySerializer());
            builder.registerTypeAdapter((Type)((Object)TextColorWrapper.class), new TextColorWrapper.Serializer());
            builder.registerTypeHierarchyAdapter(TextColor.class, downsampleColor ? TextColorSerializer.DOWNSAMPLE_COLOR : TextColorSerializer.INSTANCE);
            builder.registerTypeAdapter((Type)((Object)TextDecoration.class), IndexedSerializer.of("text decoration", TextDecoration.NAMES));
            builder.registerTypeHierarchyAdapter(BlockNBTComponent.Pos.class, BlockNBTComponentPosSerializer.INSTANCE);
            return builder;
        };
        this.serializer = ((GsonBuilder)this.populator.apply(new GsonBuilder())).create();
    }

    @Override
    @NotNull
    public Gson serializer() {
        return this.serializer;
    }

    @Override
    @NotNull
    public UnaryOperator<GsonBuilder> populator() {
        return this.populator;
    }

    @Override
    @NotNull
    public Component deserialize(@NotNull String string) {
        Component component = this.serializer().fromJson(string, Component.class);
        if (component == null) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(string);
        }
        return component;
    }

    @Override
    @NotNull
    public String serialize(@NotNull Component component) {
        return this.serializer().toJson(component);
    }

    @Override
    @NotNull
    public Component deserializeFromTree(@NotNull JsonElement input) {
        Component component = this.serializer().fromJson(input, Component.class);
        if (component == null) {
            throw ComponentSerializerImpl.notSureHowToDeserialize(input);
        }
        return component;
    }

    @Override
    @NotNull
    public JsonElement serializeToTree(@NotNull Component component) {
        return this.serializer().toJsonTree(component);
    }

    @Override
    @NotNull
    public GsonComponentSerializer.Builder toBuilder() {
        return new BuilderImpl(this);
    }

    static /* synthetic */ Optional access$000() {
        return SERVICE;
    }

    static final class BuilderImpl
    implements GsonComponentSerializer.Builder {
        private boolean downsampleColor = false;
        @Nullable
        private LegacyHoverEventSerializer legacyHoverSerializer;
        private boolean emitLegacyHover = false;

        BuilderImpl() {
            BUILDER.accept(this);
        }

        BuilderImpl(GsonComponentSerializerImpl serializer) {
            this();
            this.downsampleColor = serializer.downsampleColor;
            this.emitLegacyHover = serializer.emitLegacyHover;
            this.legacyHoverSerializer = serializer.legacyHoverSerializer;
        }

        @Override
        @NotNull
        public GsonComponentSerializer.Builder downsampleColors() {
            this.downsampleColor = true;
            return this;
        }

        @Override
        @NotNull
        public GsonComponentSerializer.Builder legacyHoverEventSerializer(@Nullable LegacyHoverEventSerializer serializer) {
            this.legacyHoverSerializer = serializer;
            return this;
        }

        @Override
        @NotNull
        public GsonComponentSerializer.Builder emitLegacyHoverEvent() {
            this.emitLegacyHover = true;
            return this;
        }

        @Override
        @NotNull
        public GsonComponentSerializer build() {
            if (this.legacyHoverSerializer == null) {
                return this.downsampleColor ? Instances.LEGACY_INSTANCE : Instances.INSTANCE;
            }
            return new GsonComponentSerializerImpl(this.downsampleColor, this.legacyHoverSerializer, this.emitLegacyHover);
        }
    }

    static final class Instances {
        static final GsonComponentSerializer INSTANCE = GsonComponentSerializerImpl.access$000().map(GsonComponentSerializer.Provider::gson).orElseGet(() -> new GsonComponentSerializerImpl(false, null, false));
        static final GsonComponentSerializer LEGACY_INSTANCE = GsonComponentSerializerImpl.access$000().map(GsonComponentSerializer.Provider::gsonLegacy).orElseGet(() -> new GsonComponentSerializerImpl(true, null, true));

        Instances() {
        }
    }
}

