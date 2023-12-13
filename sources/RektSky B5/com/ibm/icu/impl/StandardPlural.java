/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum StandardPlural {
    ZERO("zero"),
    ONE("one"),
    TWO("two"),
    FEW("few"),
    MANY("many"),
    OTHER("other");

    public static final int OTHER_INDEX;
    public static final List<StandardPlural> VALUES;
    public static final int COUNT;
    private final String keyword;

    private StandardPlural(String kw) {
        this.keyword = kw;
    }

    public final String getKeyword() {
        return this.keyword;
    }

    public static final StandardPlural orNullFromString(CharSequence keyword) {
        switch (keyword.length()) {
            case 3: {
                if ("one".contentEquals(keyword)) {
                    return ONE;
                }
                if ("two".contentEquals(keyword)) {
                    return TWO;
                }
                if (!"few".contentEquals(keyword)) break;
                return FEW;
            }
            case 4: {
                if ("many".contentEquals(keyword)) {
                    return MANY;
                }
                if (!"zero".contentEquals(keyword)) break;
                return ZERO;
            }
            case 5: {
                if (!"other".contentEquals(keyword)) break;
                return OTHER;
            }
        }
        return null;
    }

    public static final StandardPlural orOtherFromString(CharSequence keyword) {
        StandardPlural p2 = StandardPlural.orNullFromString(keyword);
        return p2 != null ? p2 : OTHER;
    }

    public static final StandardPlural fromString(CharSequence keyword) {
        StandardPlural p2 = StandardPlural.orNullFromString(keyword);
        if (p2 != null) {
            return p2;
        }
        throw new IllegalArgumentException(keyword.toString());
    }

    public static final int indexOrNegativeFromString(CharSequence keyword) {
        StandardPlural p2 = StandardPlural.orNullFromString(keyword);
        return p2 != null ? p2.ordinal() : -1;
    }

    public static final int indexOrOtherIndexFromString(CharSequence keyword) {
        StandardPlural p2 = StandardPlural.orNullFromString(keyword);
        return p2 != null ? p2.ordinal() : OTHER.ordinal();
    }

    public static final int indexFromString(CharSequence keyword) {
        StandardPlural p2 = StandardPlural.orNullFromString(keyword);
        if (p2 != null) {
            return p2.ordinal();
        }
        throw new IllegalArgumentException(keyword.toString());
    }

    static {
        OTHER_INDEX = OTHER.ordinal();
        VALUES = Collections.unmodifiableList(Arrays.asList(StandardPlural.values()));
        COUNT = VALUES.size();
    }
}

