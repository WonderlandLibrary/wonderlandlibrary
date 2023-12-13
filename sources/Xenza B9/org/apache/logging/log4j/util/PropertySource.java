// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.util;

import java.util.Arrays;
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
import java.util.Collections;
import java.util.Collection;

public interface PropertySource
{
    int getPriority();
    
    default void forEach(final BiConsumer<String, String> action) {
    }
    
    default Collection<String> getPropertyNames() {
        return (Collection<String>)Collections.emptySet();
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
        private static final Pattern PREFIX_PATTERN;
        private static final Pattern PROPERTY_TOKENIZER;
        private static final Map<CharSequence, List<CharSequence>> CACHE;
        
        public static List<CharSequence> tokenize(final CharSequence value) {
            if (Util.CACHE.containsKey(value)) {
                return Util.CACHE.get(value);
            }
            final List<CharSequence> tokens = new ArrayList<CharSequence>();
            int start = 0;
            final Matcher prefixMatcher = Util.PREFIX_PATTERN.matcher(value);
            if (prefixMatcher.find(start)) {
                start = prefixMatcher.end();
                for (Matcher matcher = Util.PROPERTY_TOKENIZER.matcher(value); matcher.find(start); start = matcher.end()) {
                    tokens.add(matcher.group(1).toLowerCase());
                }
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
            PREFIX_PATTERN = Pattern.compile("(^log4j2?[-._/]?|^org\\.apache\\.logging\\.log4j\\.)|(?=AsyncLogger(Config)?\\.)", 2);
            PROPERTY_TOKENIZER = Pattern.compile("([A-Z]*[a-z0-9]+|[A-Z0-9]+)[-._/]?");
            (CACHE = new ConcurrentHashMap<CharSequence, List<CharSequence>>()).put("disableThreadContext", Arrays.asList("disable", "thread", "context"));
            Util.CACHE.put("disableThreadContextStack", Arrays.asList("disable", "thread", "context", "stack"));
            Util.CACHE.put("disableThreadContextMap", Arrays.asList("disable", "thread", "context", "map"));
            Util.CACHE.put("isThreadContextMapInheritable", Arrays.asList("is", "thread", "context", "map", "inheritable"));
        }
    }
}
