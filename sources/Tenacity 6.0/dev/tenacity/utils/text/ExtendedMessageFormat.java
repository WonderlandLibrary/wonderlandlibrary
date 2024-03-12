// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text;

import dev.tenacity.utils.text.matcher.StringMatcherFactory;
import java.util.Objects;
import java.util.Iterator;
import java.util.Collection;
import java.text.ParsePosition;
import java.text.Format;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.text.MessageFormat;

public class ExtendedMessageFormat extends MessageFormat
{
    private static final long serialVersionUID = -2362048321261811743L;
    private static final int HASH_SEED = 31;
    private static final String DUMMY_PATTERN = "";
    private static final char START_FMT = ',';
    private static final char END_FE = '}';
    private static final char START_FE = '{';
    private static final char QUOTE = '\'';
    private String toPattern;
    private final Map<String, ? extends FormatFactory> registry;
    
    public ExtendedMessageFormat(final String pattern) {
        this(pattern, Locale.getDefault(Locale.Category.FORMAT));
    }
    
    public ExtendedMessageFormat(final String pattern, final Locale locale) {
        this(pattern, locale, null);
    }
    
    public ExtendedMessageFormat(final String pattern, final Map<String, ? extends FormatFactory> registry) {
        this(pattern, Locale.getDefault(Locale.Category.FORMAT), registry);
    }
    
    public ExtendedMessageFormat(final String pattern, final Locale locale, final Map<String, ? extends FormatFactory> registry) {
        super("");
        this.setLocale(locale);
        this.registry = registry;
        this.applyPattern(pattern);
    }
    
    @Override
    public String toPattern() {
        return this.toPattern;
    }
    
    @Override
    public final void applyPattern(final String pattern) {
        if (this.registry == null) {
            super.applyPattern(pattern);
            this.toPattern = super.toPattern();
            return;
        }
        final ArrayList<Format> foundFormats = new ArrayList<Format>();
        final ArrayList<String> foundDescriptions = new ArrayList<String>();
        final StringBuilder stripCustom = new StringBuilder(pattern.length());
        final ParsePosition pos = new ParsePosition(0);
        final char[] c = pattern.toCharArray();
        int fmtCount = 0;
        while (pos.getIndex() < pattern.length()) {
            switch (c[pos.getIndex()]) {
                case '\'': {
                    this.appendQuotedString(pattern, pos, stripCustom);
                    continue;
                }
                case '{': {
                    ++fmtCount;
                    this.seekNonWs(pattern, pos);
                    final int start = pos.getIndex();
                    final int index = this.readArgumentIndex(pattern, this.next(pos));
                    stripCustom.append('{').append(index);
                    this.seekNonWs(pattern, pos);
                    Format format = null;
                    String formatDescription = null;
                    if (c[pos.getIndex()] == ',') {
                        formatDescription = this.parseFormatDescription(pattern, this.next(pos));
                        format = this.getFormat(formatDescription);
                        if (format == null) {
                            stripCustom.append(',').append(formatDescription);
                        }
                    }
                    foundFormats.add(format);
                    foundDescriptions.add((format == null) ? null : formatDescription);
                    if (foundFormats.size() != fmtCount) {
                        throw new IllegalArgumentException("The validated expression is false");
                    }
                    if (foundDescriptions.size() != fmtCount) {
                        throw new IllegalArgumentException("The validated expression is false");
                    }
                    if (c[pos.getIndex()] != '}') {
                        throw new IllegalArgumentException("Unreadable format element at position " + start);
                    }
                    break;
                }
            }
            stripCustom.append(c[pos.getIndex()]);
            this.next(pos);
        }
        super.applyPattern(stripCustom.toString());
        this.toPattern = this.insertFormats(super.toPattern(), foundDescriptions);
        if (this.containsElements(foundFormats)) {
            final Format[] origFormats = this.getFormats();
            int i = 0;
            for (final Format f : foundFormats) {
                if (f != null) {
                    origFormats[i] = f;
                }
                ++i;
            }
            super.setFormats(origFormats);
        }
    }
    
    @Override
    public void setFormat(final int formatElementIndex, final Format newFormat) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setFormatByArgumentIndex(final int argumentIndex, final Format newFormat) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setFormats(final Format[] newFormats) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setFormatsByArgumentIndex(final Format[] newFormats) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!Objects.equals(this.getClass(), obj.getClass())) {
            return false;
        }
        final ExtendedMessageFormat rhs = (ExtendedMessageFormat)obj;
        return Objects.equals(this.toPattern, rhs.toPattern) && super.equals(obj) && Objects.equals(this.registry, rhs.registry);
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(this.registry);
        result = 31 * result + Objects.hashCode(this.toPattern);
        return result;
    }
    
    private Format getFormat(final String desc) {
        if (this.registry != null) {
            String name = desc;
            String args = null;
            final int i = desc.indexOf(44);
            if (i > 0) {
                name = desc.substring(0, i).trim();
                args = desc.substring(i + 1).trim();
            }
            final FormatFactory factory = (FormatFactory)this.registry.get(name);
            if (factory != null) {
                return factory.getFormat(name, args, this.getLocale());
            }
        }
        return null;
    }
    
    private int readArgumentIndex(final String pattern, final ParsePosition pos) {
        final int start = pos.getIndex();
        this.seekNonWs(pattern, pos);
        final StringBuilder result = new StringBuilder();
        boolean error = false;
        while (!error && pos.getIndex() < pattern.length()) {
            char c = pattern.charAt(pos.getIndex());
            Label_0149: {
                if (Character.isWhitespace(c)) {
                    this.seekNonWs(pattern, pos);
                    c = pattern.charAt(pos.getIndex());
                    if (c != ',' && c != '}') {
                        error = true;
                        break Label_0149;
                    }
                }
                if ((c == ',' || c == '}') && result.length() > 0) {
                    try {
                        return Integer.parseInt(result.toString());
                    }
                    catch (NumberFormatException ex) {}
                }
                error = !Character.isDigit(c);
                result.append(c);
            }
            this.next(pos);
        }
        if (error) {
            throw new IllegalArgumentException("Invalid format argument index at position " + start + ": " + pattern.substring(start, pos.getIndex()));
        }
        throw new IllegalArgumentException("Unterminated format element at position " + start);
    }
    
    private String parseFormatDescription(final String pattern, final ParsePosition pos) {
        final int start = pos.getIndex();
        this.seekNonWs(pattern, pos);
        final int text = pos.getIndex();
        int depth = 1;
        while (pos.getIndex() < pattern.length()) {
            switch (pattern.charAt(pos.getIndex())) {
                case '{': {
                    ++depth;
                    this.next(pos);
                    continue;
                }
                case '}': {
                    if (--depth == 0) {
                        return pattern.substring(text, pos.getIndex());
                    }
                    this.next(pos);
                    continue;
                }
                case '\'': {
                    this.getQuotedString(pattern, pos);
                    continue;
                }
                default: {
                    this.next(pos);
                    continue;
                }
            }
        }
        throw new IllegalArgumentException("Unterminated format element at position " + start);
    }
    
    private String insertFormats(final String pattern, final ArrayList<String> customPatterns) {
        if (!this.containsElements(customPatterns)) {
            return pattern;
        }
        final StringBuilder sb = new StringBuilder(pattern.length() * 2);
        final ParsePosition pos = new ParsePosition(0);
        int fe = -1;
        int depth = 0;
        while (pos.getIndex() < pattern.length()) {
            final char c = pattern.charAt(pos.getIndex());
            switch (c) {
                case '\'': {
                    this.appendQuotedString(pattern, pos, sb);
                    continue;
                }
                case '{': {
                    ++depth;
                    sb.append('{').append(this.readArgumentIndex(pattern, this.next(pos)));
                    if (depth == 1) {
                        ++fe;
                        final String customPattern = customPatterns.get(fe);
                        if (customPattern == null) {
                            continue;
                        }
                        sb.append(',').append(customPattern);
                        continue;
                    }
                    continue;
                }
                case '}': {
                    --depth;
                    break;
                }
            }
            sb.append(c);
            this.next(pos);
        }
        return sb.toString();
    }
    
    private void seekNonWs(final String pattern, final ParsePosition pos) {
        int len = 0;
        final char[] buffer = pattern.toCharArray();
        do {
            len = StringMatcherFactory.INSTANCE.splitMatcher().isMatch(buffer, pos.getIndex(), 0, buffer.length);
            pos.setIndex(pos.getIndex() + len);
        } while (len > 0 && pos.getIndex() < pattern.length());
    }
    
    private ParsePosition next(final ParsePosition pos) {
        pos.setIndex(pos.getIndex() + 1);
        return pos;
    }
    
    private StringBuilder appendQuotedString(final String pattern, final ParsePosition pos, final StringBuilder appendTo) {
        assert pattern.toCharArray()[pos.getIndex()] == '\'' : "Quoted string must start with quote character";
        if (appendTo != null) {
            appendTo.append('\'');
        }
        this.next(pos);
        final int start = pos.getIndex();
        final char[] c = pattern.toCharArray();
        for (int i = pos.getIndex(); i < pattern.length(); ++i) {
            if (c[pos.getIndex()] == '\'') {
                this.next(pos);
                return (appendTo == null) ? null : appendTo.append(c, start, pos.getIndex() - start);
            }
            this.next(pos);
        }
        throw new IllegalArgumentException("Unterminated quoted string at position " + start);
    }
    
    private void getQuotedString(final String pattern, final ParsePosition pos) {
        this.appendQuotedString(pattern, pos, null);
    }
    
    private boolean containsElements(final Collection<?> coll) {
        if (coll == null || coll.isEmpty()) {
            return false;
        }
        for (final Object name : coll) {
            if (name != null) {
                return true;
            }
        }
        return false;
    }
}
