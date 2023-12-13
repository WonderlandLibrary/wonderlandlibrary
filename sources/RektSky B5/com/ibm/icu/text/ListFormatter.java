/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.SimpleFormatterImpl;
import com.ibm.icu.util.ICUUncheckedIOException;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

public final class ListFormatter {
    private final String two;
    private final String start;
    private final String middle;
    private final String end;
    private final ULocale locale;
    static Cache cache = new Cache();

    @Deprecated
    public ListFormatter(String two, String start, String middle, String end) {
        this(ListFormatter.compilePattern(two, new StringBuilder()), ListFormatter.compilePattern(start, new StringBuilder()), ListFormatter.compilePattern(middle, new StringBuilder()), ListFormatter.compilePattern(end, new StringBuilder()), null);
    }

    private ListFormatter(String two, String start, String middle, String end, ULocale locale) {
        this.two = two;
        this.start = start;
        this.middle = middle;
        this.end = end;
        this.locale = locale;
    }

    private static String compilePattern(String pattern, StringBuilder sb) {
        return SimpleFormatterImpl.compileToStringMinMaxArguments(pattern, sb, 2, 2);
    }

    public static ListFormatter getInstance(ULocale locale) {
        return ListFormatter.getInstance(locale, Style.STANDARD);
    }

    public static ListFormatter getInstance(Locale locale) {
        return ListFormatter.getInstance(ULocale.forLocale(locale), Style.STANDARD);
    }

    @Deprecated
    public static ListFormatter getInstance(ULocale locale, Style style) {
        return cache.get(locale, style.getName());
    }

    public static ListFormatter getInstance() {
        return ListFormatter.getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public String format(Object ... items) {
        return this.format(Arrays.asList(items));
    }

    public String format(Collection<?> items) {
        return this.format(items, -1).toString();
    }

    FormattedListBuilder format(Collection<?> items, int index) {
        Iterator<?> it = items.iterator();
        int count = items.size();
        switch (count) {
            case 0: {
                return new FormattedListBuilder("", false);
            }
            case 1: {
                return new FormattedListBuilder(it.next(), index == 0);
            }
            case 2: {
                return new FormattedListBuilder(it.next(), index == 0).append(this.two, it.next(), index == 1);
            }
        }
        FormattedListBuilder builder = new FormattedListBuilder(it.next(), index == 0);
        builder.append(this.start, it.next(), index == 1);
        for (int idx = 2; idx < count - 1; ++idx) {
            builder.append(this.middle, it.next(), index == idx);
        }
        return builder.append(this.end, it.next(), index == count - 1);
    }

    public String getPatternForNumItems(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("count must be > 0");
        }
        ArrayList<String> list = new ArrayList<String>();
        for (int i2 = 0; i2 < count; ++i2) {
            list.add(String.format("{%d}", i2));
        }
        return this.format(list);
    }

    @Deprecated
    public ULocale getLocale() {
        return this.locale;
    }

    private static class Cache {
        private final ICUCache<String, ListFormatter> cache = new SimpleCache<String, ListFormatter>();

        private Cache() {
        }

        public ListFormatter get(ULocale locale, String style) {
            String key = String.format("%s:%s", locale.toString(), style);
            ListFormatter result = this.cache.get(key);
            if (result == null) {
                result = Cache.load(locale, style);
                this.cache.put(key, result);
            }
            return result;
        }

        private static ListFormatter load(ULocale ulocale, String style) {
            ICUResourceBundle r2 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b", ulocale);
            StringBuilder sb = new StringBuilder();
            return new ListFormatter(ListFormatter.compilePattern(r2.getWithFallback("listPattern/" + style + "/2").getString(), sb), ListFormatter.compilePattern(r2.getWithFallback("listPattern/" + style + "/start").getString(), sb), ListFormatter.compilePattern(r2.getWithFallback("listPattern/" + style + "/middle").getString(), sb), ListFormatter.compilePattern(r2.getWithFallback("listPattern/" + style + "/end").getString(), sb), ulocale);
        }
    }

    static class FormattedListBuilder {
        private StringBuilder current;
        private int offset;

        public FormattedListBuilder(Object start, boolean recordOffset) {
            this.current = new StringBuilder(start.toString());
            this.offset = recordOffset ? 0 : -1;
        }

        public FormattedListBuilder append(String pattern, Object next, boolean recordOffset) {
            int[] offsets = (int[])(recordOffset || this.offsetRecorded() ? new int[2] : null);
            SimpleFormatterImpl.formatAndReplace(pattern, this.current, offsets, this.current, next.toString());
            if (offsets != null) {
                if (offsets[0] == -1 || offsets[1] == -1) {
                    throw new IllegalArgumentException("{0} or {1} missing from pattern " + pattern);
                }
                this.offset = recordOffset ? offsets[1] : (this.offset += offsets[0]);
            }
            return this;
        }

        public void appendTo(Appendable appendable) {
            try {
                appendable.append(this.current);
            }
            catch (IOException e2) {
                throw new ICUUncheckedIOException(e2);
            }
        }

        public String toString() {
            return this.current.toString();
        }

        public int getOffset() {
            return this.offset;
        }

        private boolean offsetRecorded() {
            return this.offset >= 0;
        }
    }

    @Deprecated
    public static enum Style {
        STANDARD("standard"),
        DURATION("unit"),
        DURATION_SHORT("unit-short"),
        DURATION_NARROW("unit-narrow");

        private final String name;

        private Style(String name) {
            this.name = name;
        }

        @Deprecated
        public String getName() {
            return this.name;
        }
    }
}

