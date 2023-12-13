/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.SourceTargetUtility;
import com.ibm.icu.text.Transform;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UnicodeSet;
import java.util.HashMap;
import java.util.Map;

final class NormalizationTransliterator
extends Transliterator {
    private final Normalizer2 norm2;
    static final Map<Normalizer2, SourceTargetUtility> SOURCE_CACHE = new HashMap<Normalizer2, SourceTargetUtility>();

    static void register() {
        Transliterator.registerFactory("Any-NFC", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new NormalizationTransliterator("NFC", Normalizer2.getNFCInstance());
            }
        });
        Transliterator.registerFactory("Any-NFD", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new NormalizationTransliterator("NFD", Normalizer2.getNFDInstance());
            }
        });
        Transliterator.registerFactory("Any-NFKC", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new NormalizationTransliterator("NFKC", Normalizer2.getNFKCInstance());
            }
        });
        Transliterator.registerFactory("Any-NFKD", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new NormalizationTransliterator("NFKD", Normalizer2.getNFKDInstance());
            }
        });
        Transliterator.registerFactory("Any-FCD", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new NormalizationTransliterator("FCD", Norm2AllModes.getFCDNormalizer2());
            }
        });
        Transliterator.registerFactory("Any-FCC", new Transliterator.Factory(){

            @Override
            public Transliterator getInstance(String ID2) {
                return new NormalizationTransliterator("FCC", Norm2AllModes.getNFCInstance().fcc);
            }
        });
        Transliterator.registerSpecialInverse("NFC", "NFD", true);
        Transliterator.registerSpecialInverse("NFKC", "NFKD", true);
        Transliterator.registerSpecialInverse("FCC", "NFD", false);
        Transliterator.registerSpecialInverse("FCD", "FCD", false);
    }

    private NormalizationTransliterator(String id, Normalizer2 n2) {
        super(id, null);
        this.norm2 = n2;
    }

    @Override
    protected void handleTransliterate(Replaceable text, Transliterator.Position offsets, boolean isIncremental) {
        int start = offsets.start;
        int limit = offsets.limit;
        if (start >= limit) {
            return;
        }
        StringBuilder segment = new StringBuilder();
        StringBuilder normalized = new StringBuilder();
        int c2 = text.char32At(start);
        do {
            int prev = start;
            segment.setLength(0);
            do {
                segment.appendCodePoint(c2);
            } while ((start += Character.charCount(c2)) < limit && !this.norm2.hasBoundaryBefore(c2 = text.char32At(start)));
            if (start == limit && isIncremental && !this.norm2.hasBoundaryAfter(c2)) {
                start = prev;
                break;
            }
            this.norm2.normalize((CharSequence)segment, normalized);
            if (Normalizer2Impl.UTF16Plus.equal(segment, normalized)) continue;
            text.replace(prev, start, normalized.toString());
            int delta = normalized.length() - (start - prev);
            start += delta;
            limit += delta;
        } while (start < limit);
        offsets.start = start;
        offsets.contextLimit += limit - offsets.limit;
        offsets.limit = limit;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void addSourceTargetSet(UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
        SourceTargetUtility cache;
        Map<Normalizer2, SourceTargetUtility> map = SOURCE_CACHE;
        synchronized (map) {
            cache = SOURCE_CACHE.get(this.norm2);
            if (cache == null) {
                cache = new SourceTargetUtility(new NormalizingTransform(this.norm2), this.norm2);
                SOURCE_CACHE.put(this.norm2, cache);
            }
        }
        cache.addSourceTargetSet(this, inputFilter, sourceSet, targetSet);
    }

    static class NormalizingTransform
    implements Transform<String, String> {
        final Normalizer2 norm2;

        public NormalizingTransform(Normalizer2 norm2) {
            this.norm2 = norm2;
        }

        @Override
        public String transform(String source) {
            return this.norm2.normalize(source);
        }
    }
}

