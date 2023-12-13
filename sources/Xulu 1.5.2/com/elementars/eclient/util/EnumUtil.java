// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.util;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class EnumUtil
{
    public static <T extends Enum<T>> ArrayList<String> enumConverter(final Class<T> clazz) {
        final ArrayList<String> options = new ArrayList<String>();
        final List<T> list = Arrays.asList(clazz.getEnumConstants());
        list.forEach(element -> options.add(toTitle(element.name())));
        return options;
    }
    
    public static String toTitle(String in) {
        in = Character.toUpperCase(in.toLowerCase().charAt(0)) + in.toLowerCase().substring(1);
        return in;
    }
}
