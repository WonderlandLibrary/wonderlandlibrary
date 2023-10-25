/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.PseudoClass
 */
package com.sun.javafx.css;

import com.sun.javafx.css.BitSet;
import com.sun.javafx.css.PseudoClassImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.css.PseudoClass;

public final class PseudoClassState
extends BitSet<PseudoClass> {
    static final Map<String, Integer> pseudoClassMap = new HashMap<String, Integer>(64);
    static final List<PseudoClass> pseudoClasses = new ArrayList<PseudoClass>();

    public PseudoClassState() {
    }

    PseudoClassState(List<String> list) {
        int n = list != null ? list.size() : 0;
        for (int i = 0; i < n; ++i) {
            PseudoClass pseudoClass = PseudoClassState.getPseudoClass(list.get(i));
            this.add((Object)pseudoClass);
        }
    }

    public Object[] toArray() {
        return this.toArray((T[])new PseudoClass[this.size()]);
    }

    public <T> T[] toArray(T[] arrobject) {
        if (arrobject.length < this.size()) {
            arrobject = (Object[])new PseudoClass[this.size()];
        }
        int n = 0;
        while (n < this.getBits().length) {
            long l = this.getBits()[n];
            for (int i = 0; i < 64; ++i) {
                long l2 = 1L << i;
                if ((l & l2) != l2) continue;
                int n2 = n * 64 + i;
                PseudoClass pseudoClass = PseudoClassState.getPseudoClass(n2);
                arrobject[n++] = pseudoClass;
            }
        }
        return arrobject;
    }

    public String toString() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            arrayList.add(((PseudoClass)iterator.next()).getPseudoClassName());
        }
        return ((Object)arrayList).toString();
    }

    @Override
    protected PseudoClass cast(Object object) {
        if (object == null) {
            throw new NullPointerException("null arg");
        }
        PseudoClass pseudoClass = (PseudoClass)object;
        return pseudoClass;
    }

    @Override
    protected PseudoClass getT(int n) {
        return PseudoClassState.getPseudoClass(n);
    }

    @Override
    protected int getIndex(PseudoClass pseudoClass) {
        if (pseudoClass instanceof PseudoClassImpl) {
            return ((PseudoClassImpl)pseudoClass).getIndex();
        }
        String string = pseudoClass.getPseudoClassName();
        Integer n = pseudoClassMap.get(string);
        if (n == null) {
            n = pseudoClasses.size();
            pseudoClasses.add(new PseudoClassImpl(string, n));
            pseudoClassMap.put(string, n);
        }
        return n;
    }

    public static PseudoClass getPseudoClass(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException("pseudoClass cannot be null or empty String");
        }
        PseudoClass pseudoClass = null;
        Integer n = pseudoClassMap.get(string);
        int n2 = n != null ? n : -1;
        int n3 = pseudoClasses.size();
        assert (n2 < n3);
        if (n2 != -1 && n2 < n3) {
            pseudoClass = pseudoClasses.get(n2);
        }
        if (pseudoClass == null) {
            pseudoClass = new PseudoClassImpl(string, n3);
            pseudoClasses.add(pseudoClass);
            pseudoClassMap.put(string, n3);
        }
        return pseudoClass;
    }

    static PseudoClass getPseudoClass(int n) {
        if (0 <= n && n < pseudoClasses.size()) {
            return pseudoClasses.get(n);
        }
        return null;
    }
}

