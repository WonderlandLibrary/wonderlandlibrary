/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.util.BiConsumer;

public interface PropertySource {
    public int getPriority();

    default public void forEach(BiConsumer<String, String> action) {
    }

    default public CharSequence getNormalForm(Iterable<? extends CharSequence> tokens) {
        return null;
    }

    default public String getProperty(String key) {
        return null;
    }

    default public boolean containsProperty(String key) {
        return false;
    }

    public static final class Util {
        private static final String PREFIXES = "(?i:^log4j2?[-._/]?|^org\\.apache\\.logging\\.log4j\\.)?";
        private static final Pattern PROPERTY_TOKENIZER = Pattern.compile("(?i:^log4j2?[-._/]?|^org\\.apache\\.logging\\.log4j\\.)?([A-Z]*[a-z0-9]+|[A-Z0-9]+)[-._/]?");
        private static final Map<CharSequence, List<CharSequence>> CACHE = new ConcurrentHashMap<CharSequence, List<CharSequence>>();

        public static List<CharSequence> tokenize(CharSequence value) {
            if (CACHE.containsKey(value)) {
                return CACHE.get(value);
            }
            ArrayList<CharSequence> tokens = new ArrayList<CharSequence>();
            Matcher matcher = PROPERTY_TOKENIZER.matcher(value);
            while (matcher.find()) {
                tokens.add(matcher.group(1).toLowerCase());
            }
            CACHE.put(value, tokens);
            return tokens;
        }

        public static CharSequence joinAsCamelCase(Iterable<? extends CharSequence> tokens) {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (CharSequence charSequence : tokens) {
                if (first) {
                    sb.append(charSequence);
                } else {
                    sb.append(Character.toUpperCase(charSequence.charAt(0)));
                    if (charSequence.length() > 1) {
                        sb.append(charSequence.subSequence(1, charSequence.length()));
                    }
                }
                first = false;
            }
            return sb.toString();
        }

        private Util() {
        }
    }

    public static class Comparator
    implements java.util.Comparator<PropertySource>,
    Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(PropertySource o1, PropertySource o2) {
            return Integer.compare(Objects.requireNonNull(o1).getPriority(), Objects.requireNonNull(o2).getPriority());
        }
    }
}

