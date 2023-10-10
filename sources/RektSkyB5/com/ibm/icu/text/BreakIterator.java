/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.CSCharacterIterator;
import com.ibm.icu.impl.CacheValue;
import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.util.ICUCloneNotSupportedException;
import com.ibm.icu.util.ULocale;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;
import java.util.MissingResourceException;

public abstract class BreakIterator
implements Cloneable {
    private static final boolean DEBUG = ICUDebug.enabled("breakiterator");
    public static final int DONE = -1;
    public static final int WORD_NONE = 0;
    public static final int WORD_NONE_LIMIT = 100;
    public static final int WORD_NUMBER = 100;
    public static final int WORD_NUMBER_LIMIT = 200;
    public static final int WORD_LETTER = 200;
    public static final int WORD_LETTER_LIMIT = 300;
    public static final int WORD_KANA = 300;
    public static final int WORD_KANA_LIMIT = 400;
    public static final int WORD_IDEO = 400;
    public static final int WORD_IDEO_LIMIT = 500;
    public static final int KIND_CHARACTER = 0;
    public static final int KIND_WORD = 1;
    public static final int KIND_LINE = 2;
    public static final int KIND_SENTENCE = 3;
    public static final int KIND_TITLE = 4;
    private static final int KIND_COUNT = 5;
    private static final CacheValue<?>[] iterCache = new CacheValue[5];
    private static BreakIteratorServiceShim shim;
    private ULocale validLocale;
    private ULocale actualLocale;

    protected BreakIterator() {
    }

    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e2) {
            throw new ICUCloneNotSupportedException(e2);
        }
    }

    public abstract int first();

    public abstract int last();

    public abstract int next(int var1);

    public abstract int next();

    public abstract int previous();

    public abstract int following(int var1);

    public int preceding(int offset) {
        int pos = this.following(offset);
        while (pos >= offset && pos != -1) {
            pos = this.previous();
        }
        return pos;
    }

    public boolean isBoundary(int offset) {
        if (offset == 0) {
            return true;
        }
        return this.following(offset - 1) == offset;
    }

    public abstract int current();

    public int getRuleStatus() {
        return 0;
    }

    public int getRuleStatusVec(int[] fillInArray) {
        if (fillInArray != null && fillInArray.length > 0) {
            fillInArray[0] = 0;
        }
        return 1;
    }

    public abstract CharacterIterator getText();

    public void setText(String newText) {
        this.setText(new StringCharacterIterator(newText));
    }

    public void setText(CharSequence newText) {
        this.setText(new CSCharacterIterator(newText));
    }

    public abstract void setText(CharacterIterator var1);

    public static BreakIterator getWordInstance() {
        return BreakIterator.getWordInstance(ULocale.getDefault());
    }

    public static BreakIterator getWordInstance(Locale where) {
        return BreakIterator.getBreakInstance(ULocale.forLocale(where), 1);
    }

    public static BreakIterator getWordInstance(ULocale where) {
        return BreakIterator.getBreakInstance(where, 1);
    }

    public static BreakIterator getLineInstance() {
        return BreakIterator.getLineInstance(ULocale.getDefault());
    }

    public static BreakIterator getLineInstance(Locale where) {
        return BreakIterator.getBreakInstance(ULocale.forLocale(where), 2);
    }

    public static BreakIterator getLineInstance(ULocale where) {
        return BreakIterator.getBreakInstance(where, 2);
    }

    public static BreakIterator getCharacterInstance() {
        return BreakIterator.getCharacterInstance(ULocale.getDefault());
    }

    public static BreakIterator getCharacterInstance(Locale where) {
        return BreakIterator.getBreakInstance(ULocale.forLocale(where), 0);
    }

    public static BreakIterator getCharacterInstance(ULocale where) {
        return BreakIterator.getBreakInstance(where, 0);
    }

    public static BreakIterator getSentenceInstance() {
        return BreakIterator.getSentenceInstance(ULocale.getDefault());
    }

    public static BreakIterator getSentenceInstance(Locale where) {
        return BreakIterator.getBreakInstance(ULocale.forLocale(where), 3);
    }

    public static BreakIterator getSentenceInstance(ULocale where) {
        return BreakIterator.getBreakInstance(where, 3);
    }

    public static BreakIterator getTitleInstance() {
        return BreakIterator.getTitleInstance(ULocale.getDefault());
    }

    public static BreakIterator getTitleInstance(Locale where) {
        return BreakIterator.getBreakInstance(ULocale.forLocale(where), 4);
    }

    public static BreakIterator getTitleInstance(ULocale where) {
        return BreakIterator.getBreakInstance(where, 4);
    }

    public static Object registerInstance(BreakIterator iter, Locale locale, int kind) {
        return BreakIterator.registerInstance(iter, ULocale.forLocale(locale), kind);
    }

    public static Object registerInstance(BreakIterator iter, ULocale locale, int kind) {
        BreakIteratorCache cache;
        if (iterCache[kind] != null && (cache = (BreakIteratorCache)iterCache[kind].get()) != null && cache.getLocale().equals(locale)) {
            BreakIterator.iterCache[kind] = null;
        }
        return BreakIterator.getShim().registerInstance(iter, locale, kind);
    }

    public static boolean unregister(Object key) {
        if (key == null) {
            throw new IllegalArgumentException("registry key must not be null");
        }
        if (shim != null) {
            for (int kind = 0; kind < 5; ++kind) {
                BreakIterator.iterCache[kind] = null;
            }
            return shim.unregister(key);
        }
        return false;
    }

    @Deprecated
    public static BreakIterator getBreakInstance(ULocale where, int kind) {
        BreakIteratorCache cache;
        if (where == null) {
            throw new NullPointerException("Specified locale is null");
        }
        if (iterCache[kind] != null && (cache = (BreakIteratorCache)iterCache[kind].get()) != null && cache.getLocale().equals(where)) {
            return cache.createBreakInstance();
        }
        BreakIterator result = BreakIterator.getShim().createBreakIterator(where, kind);
        BreakIteratorCache cache2 = new BreakIteratorCache(where, result);
        BreakIterator.iterCache[kind] = CacheValue.getInstance(cache2);
        return result;
    }

    public static synchronized Locale[] getAvailableLocales() {
        return BreakIterator.getShim().getAvailableLocales();
    }

    public static synchronized ULocale[] getAvailableULocales() {
        return BreakIterator.getShim().getAvailableULocales();
    }

    private static BreakIteratorServiceShim getShim() {
        if (shim == null) {
            try {
                Class<?> cls = Class.forName("com.ibm.icu.text.BreakIteratorFactory");
                shim = (BreakIteratorServiceShim)cls.newInstance();
            }
            catch (MissingResourceException e2) {
                throw e2;
            }
            catch (Exception e3) {
                if (DEBUG) {
                    e3.printStackTrace();
                }
                throw new RuntimeException(e3.getMessage());
            }
        }
        return shim;
    }

    public final ULocale getLocale(ULocale.Type type) {
        return type == ULocale.ACTUAL_LOCALE ? this.actualLocale : this.validLocale;
    }

    final void setLocale(ULocale valid, ULocale actual) {
        if (valid == null != (actual == null)) {
            throw new IllegalArgumentException();
        }
        this.validLocale = valid;
        this.actualLocale = actual;
    }

    static abstract class BreakIteratorServiceShim {
        BreakIteratorServiceShim() {
        }

        public abstract Object registerInstance(BreakIterator var1, ULocale var2, int var3);

        public abstract boolean unregister(Object var1);

        public abstract Locale[] getAvailableLocales();

        public abstract ULocale[] getAvailableULocales();

        public abstract BreakIterator createBreakIterator(ULocale var1, int var2);
    }

    private static final class BreakIteratorCache {
        private BreakIterator iter;
        private ULocale where;

        BreakIteratorCache(ULocale where, BreakIterator iter) {
            this.where = where;
            this.iter = (BreakIterator)iter.clone();
        }

        ULocale getLocale() {
            return this.where;
        }

        BreakIterator createBreakInstance() {
            return (BreakIterator)this.iter.clone();
        }
    }
}

