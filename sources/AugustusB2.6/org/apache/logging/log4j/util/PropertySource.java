// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Objects;
import java.io.Serializable;
import java.util.Comparator;

public interface PropertySource
{
    int getPriority();
    
    default void forEach(final BiConsumer<String, String> action) {
    }
    
    default CharSequence getNormalForm(final Iterable<? extends CharSequence> tokens) {
        return null;
    }
    
    default String getProperty(final String key) {
        return null;
    }
    
    default boolean containsProperty(final String key) {
        return false;
    }
    
    public static class Comparator implements java.util.Comparator<PropertySource>, Serializable
    {
        private static final long serialVersionUID = 1L;
        
        @Override
        public int compare(final PropertySource o1, final PropertySource o2) {
            return Integer.compare(Objects.requireNonNull(o1).getPriority(), Objects.requireNonNull(o2).getPriority());
        }
    }
    
    public static final class Util
    {
        private static final String PREFIXES = "(?i:^log4j2?[-._/]?|^org\\.apache\\.logging\\.log4j\\.)?";
        private static final Pattern PROPERTY_TOKENIZER;
        private static final Map<CharSequence, List<CharSequence>> CACHE;
        
        public static List<CharSequence> tokenize(final CharSequence value) {
            if (Util.CACHE.containsKey(value)) {
                return Util.CACHE.get(value);
            }
            final List<CharSequence> tokens = new ArrayList<CharSequence>();
            final Matcher matcher = Util.PROPERTY_TOKENIZER.matcher(value);
            while (matcher.find()) {
                tokens.add(matcher.group(1).toLowerCase());
            }
            Util.CACHE.put(value, tokens);
            return tokens;
        }
        
        public static CharSequence joinAsCamelCase(final Iterable<? extends CharSequence> tokens) {
            final StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (final CharSequence token : tokens) {
                if (first) {
                    sb.append(token);
                }
                else {
                    sb.append(Character.toUpperCase(token.charAt(0)));
                    if (token.length() > 1) {
                        sb.append(token.subSequence(1, token.length()));
                    }
                }
                first = false;
            }
            return sb.toString();
        }
        
        private Util() {
        }
        
        static {
            PROPERTY_TOKENIZER = Pattern.compile("(?i:^log4j2?[-._/]?|^org\\.apache\\.logging\\.log4j\\.)?([A-Z]*[a-z0-9]+|[A-Z0-9]+)[-._/]?");
            CACHE = new ConcurrentHashMap<CharSequence, List<CharSequence>>();
        }
    }
}
