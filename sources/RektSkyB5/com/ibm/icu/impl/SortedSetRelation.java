/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class SortedSetRelation {
    public static final int A_NOT_B = 4;
    public static final int A_AND_B = 2;
    public static final int B_NOT_A = 1;
    public static final int ANY = 7;
    public static final int CONTAINS = 6;
    public static final int DISJOINT = 5;
    public static final int ISCONTAINED = 3;
    public static final int NO_B = 4;
    public static final int EQUALS = 2;
    public static final int NO_A = 1;
    public static final int NONE = 0;
    public static final int ADDALL = 7;
    public static final int A = 6;
    public static final int COMPLEMENTALL = 5;
    public static final int B = 3;
    public static final int REMOVEALL = 4;
    public static final int RETAINALL = 2;
    public static final int B_REMOVEALL = 1;

    public static <T> boolean hasRelation(SortedSet<T> a2, int allow, SortedSet<T> b2) {
        if (allow < 0 || allow > 7) {
            throw new IllegalArgumentException("Relation " + allow + " out of range");
        }
        boolean anb = (allow & 4) != 0;
        boolean ab = (allow & 2) != 0;
        boolean bna = (allow & 1) != 0;
        switch (allow) {
            case 6: {
                if (a2.size() >= b2.size()) break;
                return false;
            }
            case 3: {
                if (a2.size() <= b2.size()) break;
                return false;
            }
            case 2: {
                if (a2.size() == b2.size()) break;
                return false;
            }
        }
        if (a2.size() == 0) {
            if (b2.size() == 0) {
                return true;
            }
            return bna;
        }
        if (b2.size() == 0) {
            return anb;
        }
        Iterator ait = a2.iterator();
        Iterator bit = b2.iterator();
        Object aa = ait.next();
        Object bb = bit.next();
        while (true) {
            int comp;
            if ((comp = ((Comparable)aa).compareTo(bb)) == 0) {
                if (!ab) {
                    return false;
                }
                if (!ait.hasNext()) {
                    if (!bit.hasNext()) {
                        return true;
                    }
                    return bna;
                }
                if (!bit.hasNext()) {
                    return anb;
                }
                aa = ait.next();
                bb = bit.next();
                continue;
            }
            if (comp < 0) {
                if (!anb) {
                    return false;
                }
                if (!ait.hasNext()) {
                    return bna;
                }
                aa = ait.next();
                continue;
            }
            if (!bna) {
                return false;
            }
            if (!bit.hasNext()) {
                return anb;
            }
            bb = bit.next();
        }
    }

    public static <T> SortedSet<? extends T> doOperation(SortedSet<T> a2, int relation, SortedSet<T> b2) {
        switch (relation) {
            case 7: {
                a2.addAll(b2);
                return a2;
            }
            case 6: {
                return a2;
            }
            case 3: {
                a2.clear();
                a2.addAll(b2);
                return a2;
            }
            case 4: {
                a2.removeAll(b2);
                return a2;
            }
            case 2: {
                a2.retainAll(b2);
                return a2;
            }
            case 5: {
                TreeSet<T> temp = new TreeSet<T>(b2);
                temp.removeAll(a2);
                a2.removeAll(b2);
                a2.addAll(temp);
                return a2;
            }
            case 1: {
                TreeSet<T> temp = new TreeSet<T>(b2);
                temp.removeAll(a2);
                a2.clear();
                a2.addAll(temp);
                return a2;
            }
            case 0: {
                a2.clear();
                return a2;
            }
        }
        throw new IllegalArgumentException("Relation " + relation + " out of range");
    }
}

