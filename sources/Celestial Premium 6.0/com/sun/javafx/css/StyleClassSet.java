/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.StyleClass
 */
package com.sun.javafx.css;

import com.sun.javafx.css.BitSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.css.StyleClass;

public final class StyleClassSet
extends BitSet<StyleClass> {
    static final Map<String, Integer> styleClassMap = new HashMap<String, Integer>(64);
    static final List<StyleClass> styleClasses = new ArrayList<StyleClass>();

    public StyleClassSet() {
    }

    StyleClassSet(List<String> list) {
        int n = list != null ? list.size() : 0;
        for (int i = 0; i < n; ++i) {
            String string = list.get(i);
            if (string == null || string.isEmpty()) continue;
            StyleClass styleClass = StyleClassSet.getStyleClass(string);
            this.add((Object)styleClass);
        }
    }

    public Object[] toArray() {
        return this.toArray((T[])new StyleClass[this.size()]);
    }

    public <T> T[] toArray(T[] arrobject) {
        if (arrobject.length < this.size()) {
            arrobject = (Object[])new StyleClass[this.size()];
        }
        int n = 0;
        while (n < this.getBits().length) {
            long l = this.getBits()[n];
            for (int i = 0; i < 64; ++i) {
                long l2 = 1L << i;
                if ((l & l2) != l2) continue;
                int n2 = n * 64 + i;
                StyleClass styleClass = StyleClassSet.getStyleClass(n2);
                arrobject[n++] = styleClass;
            }
        }
        return arrobject;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("style-classes: [");
        Iterator iterator = this.iterator();
        while (iterator.hasNext()) {
            stringBuilder.append(((StyleClass)iterator.next()).getStyleClassName());
            if (!iterator.hasNext()) continue;
            stringBuilder.append(", ");
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    @Override
    protected StyleClass cast(Object object) {
        if (object == null) {
            throw new NullPointerException("null arg");
        }
        StyleClass styleClass = (StyleClass)object;
        return styleClass;
    }

    @Override
    protected StyleClass getT(int n) {
        return StyleClassSet.getStyleClass(n);
    }

    @Override
    protected int getIndex(StyleClass styleClass) {
        return styleClass.getIndex();
    }

    public static StyleClass getStyleClass(String string) {
        if (string == null || string.trim().isEmpty()) {
            throw new IllegalArgumentException("styleClass cannot be null or empty String");
        }
        StyleClass styleClass = null;
        Integer n = styleClassMap.get(string);
        int n2 = n != null ? n : -1;
        int n3 = styleClasses.size();
        assert (n2 < n3);
        if (n2 != -1 && n2 < n3) {
            styleClass = styleClasses.get(n2);
        }
        if (styleClass == null) {
            styleClass = new StyleClass(string, n3);
            styleClasses.add(styleClass);
            styleClassMap.put(string, n3);
        }
        return styleClass;
    }

    static StyleClass getStyleClass(int n) {
        if (0 <= n && n < styleClasses.size()) {
            return styleClasses.get(n);
        }
        return null;
    }
}

