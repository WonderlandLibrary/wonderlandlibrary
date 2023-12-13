// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import javax.annotation.Nonnull;
import java.util.List;

public class ListHelper
{
    public static String longest(@Nonnull final List<String> list) {
        Objects.requireNonNull(list);
        if (list.isEmpty()) {
            return null;
        }
        String s = list.get(0);
        for (final String st : list) {
            if (st.length() > s.length()) {
                s = st;
            }
        }
        return s;
    }
    
    public static String longest(@Nonnull final String[] list) {
        Objects.requireNonNull(list);
        final List<String> list2 = Arrays.asList(list);
        if (list2.isEmpty()) {
            return null;
        }
        String s = list2.get(0);
        for (final String st : list) {
            if (st.length() > s.length()) {
                s = st;
            }
        }
        return s;
    }
}
