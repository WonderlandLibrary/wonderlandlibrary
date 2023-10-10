// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.kyori.adventure.translation;

import com.viaversion.viaversion.libs.kyori.examination.Examiner;
import com.viaversion.viaversion.libs.kyori.examination.string.StringExaminer;
import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import java.text.MessageFormat;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Locale;
import java.util.Map;
import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
import com.viaversion.viaversion.libs.kyori.examination.Examinable;

final class TranslationRegistryImpl implements Examinable, TranslationRegistry
{
    private final Key name;
    private final Map<String, Translation> translations;
    private Locale defaultLocale;
    
    TranslationRegistryImpl(final Key name) {
        this.translations = new ConcurrentHashMap<String, Translation>();
        this.defaultLocale = Locale.US;
        this.name = name;
    }
    
    @Override
    public void register(@NotNull final String key, @NotNull final Locale locale, @NotNull final MessageFormat format) {
        this.translations.computeIfAbsent(key, x$0 -> new Translation(x$0)).register(locale, format);
    }
    
    @Override
    public void unregister(@NotNull final String key) {
        this.translations.remove(key);
    }
    
    @NotNull
    @Override
    public Key name() {
        return this.name;
    }
    
    @Override
    public boolean contains(@NotNull final String key) {
        return this.translations.containsKey(key);
    }
    
    @Nullable
    @Override
    public MessageFormat translate(@NotNull final String key, @NotNull final Locale locale) {
        final Translation translation = this.translations.get(key);
        if (translation == null) {
            return null;
        }
        return translation.translate(locale);
    }
    
    @Override
    public void defaultLocale(@NotNull final Locale defaultLocale) {
        this.defaultLocale = Objects.requireNonNull(defaultLocale, "defaultLocale");
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return Stream.of(ExaminableProperty.of("translations", this.translations));
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TranslationRegistryImpl)) {
            return false;
        }
        final TranslationRegistryImpl that = (TranslationRegistryImpl)other;
        return this.name.equals(that.name) && this.translations.equals(that.translations) && this.defaultLocale.equals(that.defaultLocale);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.translations, this.defaultLocale);
    }
    
    @Override
    public String toString() {
        return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
    }
    
    final class Translation implements Examinable
    {
        private final String key;
        private final Map<Locale, MessageFormat> formats;
        
        Translation(final String key) {
            this.key = Objects.requireNonNull(key, "translation key");
            this.formats = new ConcurrentHashMap<Locale, MessageFormat>();
        }
        
        void register(@NotNull final Locale locale, @NotNull final MessageFormat format) {
            if (this.formats.putIfAbsent(Objects.requireNonNull(locale, "locale"), Objects.requireNonNull(format, "message format")) != null) {
                throw new IllegalArgumentException(String.format("Translation already exists: %s for %s", this.key, locale));
            }
        }
        
        @Nullable
        MessageFormat translate(@NotNull final Locale locale) {
            MessageFormat format = this.formats.get(Objects.requireNonNull(locale, "locale"));
            if (format == null) {
                format = this.formats.get(new Locale(locale.getLanguage()));
                if (format == null) {
                    format = this.formats.get(TranslationRegistryImpl.this.defaultLocale);
                    if (format == null) {
                        format = this.formats.get(TranslationLocales.global());
                    }
                }
            }
            return format;
        }
        
        @NotNull
        @Override
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return Stream.of((ExaminableProperty[])new ExaminableProperty[] { ExaminableProperty.of("key", this.key), ExaminableProperty.of("formats", this.formats) });
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Translation)) {
                return false;
            }
            final Translation that = (Translation)other;
            return this.key.equals(that.key) && this.formats.equals(that.formats);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.key, this.formats);
        }
        
        @Override
        public String toString() {
            return this.examine((Examiner<String>)StringExaminer.simpleEscaping());
        }
    }
}
