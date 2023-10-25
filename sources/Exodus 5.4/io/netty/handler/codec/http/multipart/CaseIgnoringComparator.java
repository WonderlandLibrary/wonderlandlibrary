/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import java.io.Serializable;
import java.util.Comparator;

final class CaseIgnoringComparator
implements Comparator<CharSequence>,
Serializable {
    private static final long serialVersionUID = 4582133183775373862L;
    static final CaseIgnoringComparator INSTANCE = new CaseIgnoringComparator();

    private CaseIgnoringComparator() {
    }

    private Object readResolve() {
        return INSTANCE;
    }

    @Override
    public int compare(CharSequence charSequence, CharSequence charSequence2) {
        int n = charSequence.length();
        int n2 = charSequence2.length();
        int n3 = Math.min(n, n2);
        for (int i = 0; i < n3; ++i) {
            char c;
            char c2 = charSequence.charAt(i);
            if (c2 == (c = charSequence2.charAt(i)) || (c2 = Character.toUpperCase(c2)) == (c = Character.toUpperCase(c)) || (c2 = Character.toLowerCase(c2)) == (c = Character.toLowerCase(c))) continue;
            return c2 - c;
        }
        return n - n2;
    }
}

