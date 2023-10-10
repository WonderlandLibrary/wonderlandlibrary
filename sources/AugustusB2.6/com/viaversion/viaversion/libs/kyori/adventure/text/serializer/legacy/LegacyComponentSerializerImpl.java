// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy;

import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentBuilder;
import com.viaversion.viaversion.libs.kyori.adventure.text.event.ClickEvent;
import java.util.Objects;
import java.util.Iterator;
import java.util.EnumSet;
import java.util.Set;
import java.util.Arrays;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.Style;
import java.util.Map;
import java.util.function.Function;
import com.viaversion.viaversion.libs.kyori.adventure.util.Services;
import java.util.Collection;
import java.util.LinkedHashMap;
import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
import com.viaversion.viaversion.libs.kyori.adventure.text.flattener.FlattenerListener;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
import java.util.Collections;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.TextComponent;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextColor;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.NamedTextColor;
import com.viaversion.viaversion.libs.kyori.adventure.text.flattener.ComponentFlattener;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.libs.kyori.adventure.text.TextReplacementConfig;
import java.util.function.Consumer;
import java.util.Optional;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextFormat;
import java.util.List;
import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextDecoration;
import java.util.regex.Pattern;

final class LegacyComponentSerializerImpl implements LegacyComponentSerializer
{
    static final Pattern DEFAULT_URL_PATTERN;
    static final Pattern URL_SCHEME_PATTERN;
    private static final TextDecoration[] DECORATIONS;
    private static final char LEGACY_BUNGEE_HEX_CHAR = 'x';
    private static final List<TextFormat> FORMATS;
    private static final String LEGACY_CHARS;
    private static final Optional<Provider> SERVICE;
    static final Consumer<Builder> BUILDER;
    private final char character;
    private final char hexCharacter;
    @Nullable
    private final TextReplacementConfig urlReplacementConfig;
    private final boolean hexColours;
    private final boolean useTerriblyStupidHexFormat;
    private final ComponentFlattener flattener;
    
    LegacyComponentSerializerImpl(final char character, final char hexCharacter, @Nullable final TextReplacementConfig urlReplacementConfig, final boolean hexColours, final boolean useTerriblyStupidHexFormat, final ComponentFlattener flattener) {
        this.character = character;
        this.hexCharacter = hexCharacter;
        this.urlReplacementConfig = urlReplacementConfig;
        this.hexColours = hexColours;
        this.useTerriblyStupidHexFormat = useTerriblyStupidHexFormat;
        this.flattener = flattener;
    }
    
    @Nullable
    private FormatCodeType determineFormatType(final char legacy, final String input, final int pos) {
        if (pos >= 14) {
            final int expectedCharacterPosition = pos - 14;
            final int expectedIndicatorPosition = pos - 13;
            if (input.charAt(expectedCharacterPosition) == this.character && input.charAt(expectedIndicatorPosition) == 'x') {
                return FormatCodeType.BUNGEECORD_UNUSUAL_HEX;
            }
        }
        if (legacy == this.hexCharacter && input.length() - pos >= 6) {
            return FormatCodeType.KYORI_HEX;
        }
        if (LegacyComponentSerializerImpl.LEGACY_CHARS.indexOf(legacy) != -1) {
            return FormatCodeType.MOJANG_LEGACY;
        }
        return null;
    }
    
    @Nullable
    static LegacyFormat legacyFormat(final char character) {
        final int index = LegacyComponentSerializerImpl.LEGACY_CHARS.indexOf(character);
        if (index != -1) {
            final TextFormat format = LegacyComponentSerializerImpl.FORMATS.get(index);
            if (format instanceof NamedTextColor) {
                return new LegacyFormat((NamedTextColor)format);
            }
            if (format instanceof TextDecoration) {
                return new LegacyFormat((TextDecoration)format);
            }
            if (format instanceof Reset) {
                return LegacyFormat.RESET;
            }
        }
        return null;
    }
    
    @Nullable
    private DecodedFormat decodeTextFormat(final char legacy, final String input, final int pos) {
        final FormatCodeType foundFormat = this.determineFormatType(legacy, input, pos);
        if (foundFormat == null) {
            return null;
        }
        if (foundFormat == FormatCodeType.KYORI_HEX) {
            final TextColor parsed = tryParseHexColor(input.substring(pos, pos + 6));
            if (parsed != null) {
                return new DecodedFormat(foundFormat, (TextFormat)parsed);
            }
        }
        else {
            if (foundFormat == FormatCodeType.MOJANG_LEGACY) {
                return new DecodedFormat(foundFormat, (TextFormat)LegacyComponentSerializerImpl.FORMATS.get(LegacyComponentSerializerImpl.LEGACY_CHARS.indexOf(legacy)));
            }
            if (foundFormat == FormatCodeType.BUNGEECORD_UNUSUAL_HEX) {
                final StringBuilder foundHex = new StringBuilder(6);
                for (int i = pos - 1; i >= pos - 11; i -= 2) {
                    foundHex.append(input.charAt(i));
                }
                final TextColor parsed2 = tryParseHexColor(foundHex.reverse().toString());
                if (parsed2 != null) {
                    return new DecodedFormat(foundFormat, (TextFormat)parsed2);
                }
            }
        }
        return null;
    }
    
    @Nullable
    private static TextColor tryParseHexColor(final String hexDigits) {
        try {
            final int color = Integer.parseInt(hexDigits, 16);
            return TextColor.color(color);
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }
    
    private static boolean isHexTextColor(final TextFormat format) {
        return format instanceof TextColor && !(format instanceof NamedTextColor);
    }
    
    private String toLegacyCode(TextFormat format) {
        if (isHexTextColor(format)) {
            final TextColor color = (TextColor)format;
            if (this.hexColours) {
                final String hex = String.format("%06x", color.value());
                if (this.useTerriblyStupidHexFormat) {
                    final StringBuilder legacy = new StringBuilder(String.valueOf('x'));
                    for (final char character : hex.toCharArray()) {
                        legacy.append(this.character).append(character);
                    }
                    return legacy.toString();
                }
                return this.hexCharacter + hex;
            }
            else {
                format = NamedTextColor.nearestTo(color);
            }
        }
        final int index = LegacyComponentSerializerImpl.FORMATS.indexOf(format);
        return Character.toString(LegacyComponentSerializerImpl.LEGACY_CHARS.charAt(index));
    }
    
    private TextComponent extractUrl(final TextComponent component) {
        if (this.urlReplacementConfig == null) {
            return component;
        }
        final Component newComponent = component.replaceText(this.urlReplacementConfig);
        if (newComponent instanceof TextComponent) {
            return (TextComponent)newComponent;
        }
        return ((ComponentBuilder<TextComponent, B>)((ComponentBuilder<C, TextComponent.Builder>)Component.text()).append(newComponent)).build();
    }
    
    @NotNull
    @Override
    public TextComponent deserialize(@NotNull final String input) {
        int next = input.lastIndexOf(this.character, input.length() - 2);
        if (next == -1) {
            return this.extractUrl(Component.text(input));
        }
        final List<TextComponent> parts = new ArrayList<TextComponent>();
        TextComponent.Builder current = null;
        boolean reset = false;
        int pos = input.length();
        do {
            final DecodedFormat decoded = this.decodeTextFormat(input.charAt(next + 1), input, next + 2);
            if (decoded != null) {
                final int from = next + ((decoded.encodedFormat == FormatCodeType.KYORI_HEX) ? 8 : 2);
                if (from != pos) {
                    if (current != null) {
                        if (reset) {
                            parts.add(((ComponentBuilder<TextComponent, B>)current).build());
                            reset = false;
                            current = Component.text();
                        }
                        else {
                            current = ((ComponentBuilder<C, TextComponent.Builder>)Component.text()).append(((ComponentBuilder<Component, B>)current).build());
                        }
                    }
                    else {
                        current = Component.text();
                    }
                    current.content(input.substring(from, pos));
                }
                else if (current == null) {
                    current = Component.text();
                }
                if (!reset) {
                    reset = applyFormat(current, decoded.format);
                }
                if (decoded.encodedFormat == FormatCodeType.BUNGEECORD_UNUSUAL_HEX) {
                    next -= 12;
                }
                pos = next;
            }
            next = input.lastIndexOf(this.character, next - 1);
        } while (next != -1);
        if (current != null) {
            parts.add(((ComponentBuilder<TextComponent, B>)current).build());
        }
        final String remaining = (pos > 0) ? input.substring(0, pos) : "";
        if (parts.size() == 1 && remaining.isEmpty()) {
            return this.extractUrl(parts.get(0));
        }
        Collections.reverse(parts);
        return this.extractUrl(((ComponentBuilder<TextComponent, B>)((ComponentBuilder<C, TextComponent.Builder>)Component.text().content(remaining)).append(parts)).build());
    }
    
    @NotNull
    @Override
    public String serialize(@NotNull final Component component) {
        final Cereal state = new Cereal();
        this.flattener.flatten(component, state);
        return state.toString();
    }
    
    private static boolean applyFormat(final TextComponent.Builder builder, @NotNull final TextFormat format) {
        if (format instanceof TextColor) {
            builder.colorIfAbsent((TextColor)format);
            return true;
        }
        if (format instanceof TextDecoration) {
            builder.decoration((TextDecoration)format, TextDecoration.State.TRUE);
            return false;
        }
        if (format instanceof Reset) {
            return true;
        }
        throw new IllegalArgumentException(String.format("unknown format '%s'", format.getClass()));
    }
    
    @NotNull
    @Override
    public Builder toBuilder() {
        return new BuilderImpl(this);
    }
    
    static {
        DEFAULT_URL_PATTERN = Pattern.compile("(?:(https?)://)?([-\\w_.]+\\.\\w{2,})(/\\S*)?");
        URL_SCHEME_PATTERN = Pattern.compile("^[a-z][a-z0-9+\\-.]*:");
        DECORATIONS = TextDecoration.values();
        final Map<TextFormat, String> formats = new LinkedHashMap<TextFormat, String>(22);
        formats.put(NamedTextColor.BLACK, "0");
        formats.put(NamedTextColor.DARK_BLUE, "1");
        formats.put(NamedTextColor.DARK_GREEN, "2");
        formats.put(NamedTextColor.DARK_AQUA, "3");
        formats.put(NamedTextColor.DARK_RED, "4");
        formats.put(NamedTextColor.DARK_PURPLE, "5");
        formats.put(NamedTextColor.GOLD, "6");
        formats.put(NamedTextColor.GRAY, "7");
        formats.put(NamedTextColor.DARK_GRAY, "8");
        formats.put(NamedTextColor.BLUE, "9");
        formats.put(NamedTextColor.GREEN, "a");
        formats.put(NamedTextColor.AQUA, "b");
        formats.put(NamedTextColor.RED, "c");
        formats.put(NamedTextColor.LIGHT_PURPLE, "d");
        formats.put(NamedTextColor.YELLOW, "e");
        formats.put(NamedTextColor.WHITE, "f");
        formats.put(TextDecoration.OBFUSCATED, "k");
        formats.put(TextDecoration.BOLD, "l");
        formats.put(TextDecoration.STRIKETHROUGH, "m");
        formats.put(TextDecoration.UNDERLINED, "n");
        formats.put(TextDecoration.ITALIC, "o");
        formats.put(Reset.INSTANCE, "r");
        FORMATS = Collections.unmodifiableList((List<? extends TextFormat>)new ArrayList<TextFormat>(formats.keySet()));
        LEGACY_CHARS = String.join("", formats.values());
        if (LegacyComponentSerializerImpl.FORMATS.size() != LegacyComponentSerializerImpl.LEGACY_CHARS.length()) {
            throw new IllegalStateException("FORMATS length differs from LEGACY_CHARS length");
        }
        SERVICE = Services.service(Provider.class);
        BUILDER = LegacyComponentSerializerImpl.SERVICE.map((Function<? super Provider, ? extends Consumer<Builder>>)Provider::legacy).orElseGet(() -> builder -> {});
    }
    
    static final class Instances
    {
        static final LegacyComponentSerializer SECTION;
        static final LegacyComponentSerializer AMPERSAND;
        
        static {
            SECTION = LegacyComponentSerializerImpl.SERVICE.map(Provider::legacySection).orElseGet(() -> new LegacyComponentSerializerImpl('§', '#', null, false, false, ComponentFlattener.basic()));
            AMPERSAND = LegacyComponentSerializerImpl.SERVICE.map(Provider::legacyAmpersand).orElseGet(() -> new LegacyComponentSerializerImpl('&', '#', null, false, false, ComponentFlattener.basic()));
        }
    }
    
    private enum Reset implements TextFormat
    {
        INSTANCE;
    }
    
    private final class Cereal implements FlattenerListener
    {
        private final StringBuilder sb;
        private final StyleState style;
        @Nullable
        private TextFormat lastWritten;
        private StyleState[] styles;
        private int head;
        
        private Cereal() {
            this.sb = new StringBuilder();
            this.style = new StyleState();
            this.styles = new StyleState[8];
            this.head = -1;
        }
        
        @Override
        public void pushStyle(@NotNull final Style pushed) {
            final int idx = ++this.head;
            if (idx >= this.styles.length) {
                this.styles = Arrays.copyOf(this.styles, this.styles.length * 2);
            }
            StyleState state = this.styles[idx];
            if (state == null) {
                state = (this.styles[idx] = new StyleState());
            }
            if (idx > 0) {
                state.set(this.styles[idx - 1]);
            }
            else {
                state.clear();
            }
            state.apply(pushed);
        }
        
        @Override
        public void component(@NotNull final String text) {
            if (!text.isEmpty()) {
                if (this.head < 0) {
                    throw new IllegalStateException("No style has been pushed!");
                }
                this.styles[this.head].applyFormat();
                this.sb.append(text);
            }
        }
        
        @Override
        public void popStyle(@NotNull final Style style) {
            if (this.head-- < 0) {
                throw new IllegalStateException("Tried to pop beyond what was pushed!");
            }
        }
        
        void append(@NotNull final TextFormat format) {
            if (this.lastWritten != format) {
                this.sb.append(LegacyComponentSerializerImpl.this.character).append(LegacyComponentSerializerImpl.this.toLegacyCode(format));
            }
            this.lastWritten = format;
        }
        
        @Override
        public String toString() {
            return this.sb.toString();
        }
        
        private final class StyleState
        {
            @Nullable
            private TextColor color;
            private final Set<TextDecoration> decorations;
            private boolean needsReset;
            
            StyleState() {
                this.decorations = EnumSet.noneOf(TextDecoration.class);
            }
            
            void set(@NotNull final StyleState that) {
                this.color = that.color;
                this.decorations.clear();
                this.decorations.addAll(that.decorations);
            }
            
            public void clear() {
                this.color = null;
                this.decorations.clear();
            }
            
            void apply(@NotNull final Style component) {
                final TextColor color = component.color();
                if (color != null) {
                    this.color = color;
                }
                for (int i = 0, length = LegacyComponentSerializerImpl.DECORATIONS.length; i < length; ++i) {
                    final TextDecoration decoration = LegacyComponentSerializerImpl.DECORATIONS[i];
                    switch (component.decoration(decoration)) {
                        case TRUE: {
                            this.decorations.add(decoration);
                            break;
                        }
                        case FALSE: {
                            if (this.decorations.remove(decoration)) {
                                this.needsReset = true;
                                break;
                            }
                            break;
                        }
                    }
                }
            }
            
            void applyFormat() {
                final boolean colorChanged = this.color != Cereal.this.style.color;
                if (this.needsReset) {
                    if (!colorChanged) {
                        Cereal.this.append(Reset.INSTANCE);
                    }
                    this.needsReset = false;
                }
                if (colorChanged || Cereal.this.lastWritten == Reset.INSTANCE) {
                    this.applyFullFormat();
                    return;
                }
                if (!this.decorations.containsAll(Cereal.this.style.decorations)) {
                    this.applyFullFormat();
                    return;
                }
                for (final TextDecoration decoration : this.decorations) {
                    if (Cereal.this.style.decorations.add(decoration)) {
                        Cereal.this.append(decoration);
                    }
                }
            }
            
            private void applyFullFormat() {
                if (this.color != null) {
                    Cereal.this.append(this.color);
                }
                else {
                    Cereal.this.append(Reset.INSTANCE);
                }
                Cereal.this.style.color = this.color;
                for (final TextDecoration decoration : this.decorations) {
                    Cereal.this.append(decoration);
                }
                Cereal.this.style.decorations.clear();
                Cereal.this.style.decorations.addAll(this.decorations);
            }
        }
    }
    
    static final class BuilderImpl implements Builder
    {
        private char character;
        private char hexCharacter;
        private TextReplacementConfig urlReplacementConfig;
        private boolean hexColours;
        private boolean useTerriblyStupidHexFormat;
        private ComponentFlattener flattener;
        
        BuilderImpl() {
            this.character = '§';
            this.hexCharacter = '#';
            this.urlReplacementConfig = null;
            this.hexColours = false;
            this.useTerriblyStupidHexFormat = false;
            this.flattener = ComponentFlattener.basic();
            LegacyComponentSerializerImpl.BUILDER.accept(this);
        }
        
        BuilderImpl(@NotNull final LegacyComponentSerializerImpl serializer) {
            this();
            this.character = serializer.character;
            this.hexCharacter = serializer.hexCharacter;
            this.urlReplacementConfig = serializer.urlReplacementConfig;
            this.hexColours = serializer.hexColours;
            this.useTerriblyStupidHexFormat = serializer.useTerriblyStupidHexFormat;
            this.flattener = serializer.flattener;
        }
        
        @NotNull
        @Override
        public Builder character(final char legacyCharacter) {
            this.character = legacyCharacter;
            return this;
        }
        
        @NotNull
        @Override
        public Builder hexCharacter(final char legacyHexCharacter) {
            this.hexCharacter = legacyHexCharacter;
            return this;
        }
        
        @NotNull
        @Override
        public Builder extractUrls() {
            return this.extractUrls(LegacyComponentSerializerImpl.DEFAULT_URL_PATTERN, null);
        }
        
        @NotNull
        @Override
        public Builder extractUrls(@NotNull final Pattern pattern) {
            return this.extractUrls(pattern, null);
        }
        
        @NotNull
        @Override
        public Builder extractUrls(@Nullable final Style style) {
            return this.extractUrls(LegacyComponentSerializerImpl.DEFAULT_URL_PATTERN, style);
        }
        
        @NotNull
        @Override
        public Builder extractUrls(@NotNull final Pattern pattern, @Nullable final Style style) {
            Objects.requireNonNull(pattern, "pattern");
            String clickUrl;
            this.urlReplacementConfig = TextReplacementConfig.builder().match(pattern).replacement(url -> {
                clickUrl = url.content();
                if (!LegacyComponentSerializerImpl.URL_SCHEME_PATTERN.matcher(clickUrl).find()) {
                    clickUrl = "http://" + clickUrl;
                }
                return ((style == null) ? url : ((ComponentBuilder<C, TextComponent.Builder>)url).style(style)).clickEvent(ClickEvent.openUrl(clickUrl));
            }).build();
            return this;
        }
        
        @NotNull
        @Override
        public Builder hexColors() {
            this.hexColours = true;
            return this;
        }
        
        @NotNull
        @Override
        public Builder useUnusualXRepeatedCharacterHexFormat() {
            this.useTerriblyStupidHexFormat = true;
            return this;
        }
        
        @NotNull
        @Override
        public Builder flattener(@NotNull final ComponentFlattener flattener) {
            this.flattener = Objects.requireNonNull(flattener, "flattener");
            return this;
        }
        
        @NotNull
        @Override
        public LegacyComponentSerializer build() {
            return new LegacyComponentSerializerImpl(this.character, this.hexCharacter, this.urlReplacementConfig, this.hexColours, this.useTerriblyStupidHexFormat, this.flattener);
        }
    }
    
    enum FormatCodeType
    {
        MOJANG_LEGACY, 
        KYORI_HEX, 
        BUNGEECORD_UNUSUAL_HEX;
    }
    
    static final class DecodedFormat
    {
        final FormatCodeType encodedFormat;
        final TextFormat format;
        
        private DecodedFormat(final FormatCodeType encodedFormat, final TextFormat format) {
            if (format == null) {
                throw new IllegalStateException("No format found");
            }
            this.encodedFormat = encodedFormat;
            this.format = format;
        }
    }
}
