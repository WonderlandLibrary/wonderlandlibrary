// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public enum CaseFormat
{
    LOWER_HYPHEN(0, CharMatcher.is('-'), "-") {
        @Override
        String normalizeWord(final String word) {
            return Ascii.toLowerCase(word);
        }
        
        @Override
        String convert(final CaseFormat format, final String s) {
            if (format == CaseFormat$1.LOWER_UNDERSCORE) {
                return s.replace('-', '_');
            }
            if (format == CaseFormat$1.UPPER_UNDERSCORE) {
                return Ascii.toUpperCase(s.replace('-', '_'));
            }
            return super.convert(format, s);
        }
    }, 
    LOWER_UNDERSCORE(1, CharMatcher.is('_'), "_") {
        @Override
        String normalizeWord(final String word) {
            return Ascii.toLowerCase(word);
        }
        
        @Override
        String convert(final CaseFormat format, final String s) {
            if (format == CaseFormat$2.LOWER_HYPHEN) {
                return s.replace('_', '-');
            }
            if (format == CaseFormat$2.UPPER_UNDERSCORE) {
                return Ascii.toUpperCase(s);
            }
            return super.convert(format, s);
        }
    }, 
    LOWER_CAMEL(2, CharMatcher.inRange('A', 'Z'), "") {
        @Override
        String normalizeWord(final String word) {
            return firstCharOnlyToUpper(word);
        }
        
        @Override
        String normalizeFirstWord(final String word) {
            return Ascii.toLowerCase(word);
        }
    }, 
    UPPER_CAMEL(3, CharMatcher.inRange('A', 'Z'), "") {
        @Override
        String normalizeWord(final String word) {
            return firstCharOnlyToUpper(word);
        }
    }, 
    UPPER_UNDERSCORE(4, CharMatcher.is('_'), "_") {
        @Override
        String normalizeWord(final String word) {
            return Ascii.toUpperCase(word);
        }
        
        @Override
        String convert(final CaseFormat format, final String s) {
            if (format == CaseFormat$5.LOWER_HYPHEN) {
                return Ascii.toLowerCase(s.replace('_', '-'));
            }
            if (format == CaseFormat$5.LOWER_UNDERSCORE) {
                return Ascii.toLowerCase(s);
            }
            return super.convert(format, s);
        }
    };
    
    private final CharMatcher wordBoundary;
    private final String wordSeparator;
    
    private CaseFormat(final CharMatcher wordBoundary, final String wordSeparator) {
        this.wordBoundary = wordBoundary;
        this.wordSeparator = wordSeparator;
    }
    
    public final String to(final CaseFormat format, final String str) {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(str);
        return (format == this) ? str : this.convert(format, str);
    }
    
    String convert(final CaseFormat format, final String s) {
        StringBuilder out = null;
        int i = 0;
        int j = -1;
        while ((j = this.wordBoundary.indexIn(s, ++j)) != -1) {
            if (i == 0) {
                out = new StringBuilder(s.length() + 4 * format.wordSeparator.length());
                out.append(format.normalizeFirstWord(s.substring(i, j)));
            }
            else {
                Objects.requireNonNull(out).append(format.normalizeWord(s.substring(i, j)));
            }
            out.append(format.wordSeparator);
            i = j + this.wordSeparator.length();
        }
        return (i == 0) ? format.normalizeFirstWord(s) : Objects.requireNonNull(out).append(format.normalizeWord(s.substring(i))).toString();
    }
    
    public Converter<String, String> converterTo(final CaseFormat targetFormat) {
        return new StringConverter(this, targetFormat);
    }
    
    abstract String normalizeWord(final String p0);
    
    String normalizeFirstWord(final String word) {
        return this.normalizeWord(word);
    }
    
    private static String firstCharOnlyToUpper(final String word) {
        String string;
        if (word.isEmpty()) {
            string = word;
        }
        else {
            final char upperCase = Ascii.toUpperCase(word.charAt(0));
            final String lowerCase = Ascii.toLowerCase(word.substring(1));
            string = new StringBuilder(1 + String.valueOf(lowerCase).length()).append(upperCase).append(lowerCase).toString();
        }
        return string;
    }
    
    private static /* synthetic */ CaseFormat[] $values() {
        return new CaseFormat[] { CaseFormat.LOWER_HYPHEN, CaseFormat.LOWER_UNDERSCORE, CaseFormat.LOWER_CAMEL, CaseFormat.UPPER_CAMEL, CaseFormat.UPPER_UNDERSCORE };
    }
    
    static {
        $VALUES = $values();
    }
    
    private static final class StringConverter extends Converter<String, String> implements Serializable
    {
        private final CaseFormat sourceFormat;
        private final CaseFormat targetFormat;
        private static final long serialVersionUID = 0L;
        
        StringConverter(final CaseFormat sourceFormat, final CaseFormat targetFormat) {
            this.sourceFormat = Preconditions.checkNotNull(sourceFormat);
            this.targetFormat = Preconditions.checkNotNull(targetFormat);
        }
        
        @Override
        protected String doForward(final String s) {
            return this.sourceFormat.to(this.targetFormat, s);
        }
        
        @Override
        protected String doBackward(final String s) {
            return this.targetFormat.to(this.sourceFormat, s);
        }
        
        @Override
        public boolean equals(@CheckForNull final Object object) {
            if (object instanceof StringConverter) {
                final StringConverter that = (StringConverter)object;
                return this.sourceFormat.equals(that.sourceFormat) && this.targetFormat.equals(that.targetFormat);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
        }
        
        @Override
        public String toString() {
            final String value = String.valueOf(this.sourceFormat);
            final String value2 = String.valueOf(this.targetFormat);
            return new StringBuilder(14 + String.valueOf(value).length() + String.valueOf(value2).length()).append(value).append(".converterTo(").append(value2).append(")").toString();
        }
    }
}
