/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.lang.CharSequences;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.Transform;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import java.util.HashSet;
import java.util.Set;

class SourceTargetUtility {
    final Transform<String, String> transform;
    final UnicodeSet sourceCache;
    final Set<String> sourceStrings;
    static final UnicodeSet NON_STARTERS = new UnicodeSet("[:^ccc=0:]").freeze();
    static Normalizer2 NFC = Normalizer2.getNFCInstance();

    public SourceTargetUtility(Transform<String, String> transform) {
        this(transform, null);
    }

    public SourceTargetUtility(Transform<String, String> transform, Normalizer2 normalizer) {
        this.transform = transform;
        this.sourceCache = normalizer != null ? new UnicodeSet("[:^ccc=0:]") : new UnicodeSet();
        this.sourceStrings = new HashSet<String>();
        for (int i2 = 0; i2 <= 0x10FFFF; ++i2) {
            String d2;
            String s2 = transform.transform(UTF16.valueOf(i2));
            boolean added = false;
            if (!CharSequences.equals(i2, s2)) {
                this.sourceCache.add(i2);
                added = true;
            }
            if (normalizer == null || (d2 = NFC.getDecomposition(i2)) == null) continue;
            s2 = transform.transform(d2);
            if (!d2.equals(s2)) {
                this.sourceStrings.add(d2);
            }
            if (added || normalizer.isInert(i2)) continue;
            this.sourceCache.add(i2);
        }
        this.sourceCache.freeze();
    }

    public void addSourceTargetSet(Transliterator transliterator, UnicodeSet inputFilter, UnicodeSet sourceSet, UnicodeSet targetSet) {
        UnicodeSet myFilter = transliterator.getFilterAsUnicodeSet(inputFilter);
        UnicodeSet affectedCharacters = new UnicodeSet(this.sourceCache).retainAll(myFilter);
        sourceSet.addAll(affectedCharacters);
        for (String s2 : affectedCharacters) {
            targetSet.addAll(this.transform.transform(s2));
        }
        for (String s2 : this.sourceStrings) {
            String t2;
            if (!myFilter.containsAll(s2) || s2.equals(t2 = this.transform.transform(s2))) continue;
            targetSet.addAll(t2);
            sourceSet.addAll(s2);
        }
    }
}

