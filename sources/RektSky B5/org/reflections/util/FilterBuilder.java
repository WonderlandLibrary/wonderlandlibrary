/*
 * Decompiled with CFR 0.152.
 */
package org.reflections.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import org.reflections.ReflectionsException;
import org.reflections.util.Utils;

public class FilterBuilder
implements Predicate<String> {
    private final List<Predicate<String>> chain;

    public FilterBuilder() {
        this.chain = new ArrayList<Predicate<String>>();
    }

    private FilterBuilder(Collection<Predicate<String>> filters) {
        this.chain = new ArrayList<Predicate<String>>(filters);
    }

    public FilterBuilder include(String regex) {
        return this.add(new Include(regex));
    }

    public FilterBuilder exclude(String regex) {
        this.add(new Exclude(regex));
        return this;
    }

    public FilterBuilder add(Predicate<String> filter) {
        this.chain.add(filter);
        return this;
    }

    public FilterBuilder includePackage(Class<?> aClass) {
        return this.add(new Include(FilterBuilder.packageNameRegex(aClass)));
    }

    public FilterBuilder excludePackage(Class<?> aClass) {
        return this.add(new Exclude(FilterBuilder.packageNameRegex(aClass)));
    }

    public FilterBuilder includePackage(String ... prefixes) {
        for (String prefix : prefixes) {
            this.add(new Include(FilterBuilder.prefix(prefix)));
        }
        return this;
    }

    public FilterBuilder excludePackage(String prefix) {
        return this.add(new Exclude(FilterBuilder.prefix(prefix)));
    }

    private static String packageNameRegex(Class<?> aClass) {
        return FilterBuilder.prefix(aClass.getPackage().getName() + ".");
    }

    public static String prefix(String qualifiedName) {
        return qualifiedName.replace(".", "\\.") + ".*";
    }

    public String toString() {
        return Utils.join(this.chain, ", ");
    }

    @Override
    public boolean test(String regex) {
        boolean accept = this.chain.isEmpty() || this.chain.get(0) instanceof Exclude;
        for (Predicate<String> filter : this.chain) {
            if (accept && filter instanceof Include || !accept && filter instanceof Exclude || (accept = filter.test(regex)) || !(filter instanceof Exclude)) continue;
            break;
        }
        return accept;
    }

    public static FilterBuilder parse(String includeExcludeString) {
        ArrayList<Predicate<String>> filters = new ArrayList<Predicate<String>>();
        if (!Utils.isEmpty(includeExcludeString)) {
            for (String string : includeExcludeString.split(",")) {
                Matcher filter;
                String trimmed = string.trim();
                char prefix = trimmed.charAt(0);
                String pattern = trimmed.substring(1);
                switch (prefix) {
                    case '+': {
                        filter = new Include(pattern);
                        break;
                    }
                    case '-': {
                        filter = new Exclude(pattern);
                        break;
                    }
                    default: {
                        throw new ReflectionsException("includeExclude should start with either + or -");
                    }
                }
                filters.add(filter);
            }
            return new FilterBuilder(filters);
        }
        return new FilterBuilder();
    }

    public static FilterBuilder parsePackages(String includeExcludeString) {
        ArrayList<Predicate<String>> filters = new ArrayList<Predicate<String>>();
        if (!Utils.isEmpty(includeExcludeString)) {
            for (String string : includeExcludeString.split(",")) {
                Matcher filter;
                String trimmed = string.trim();
                char prefix = trimmed.charAt(0);
                String pattern = trimmed.substring(1);
                if (!pattern.endsWith(".")) {
                    pattern = pattern + ".";
                }
                pattern = FilterBuilder.prefix(pattern);
                switch (prefix) {
                    case '+': {
                        filter = new Include(pattern);
                        break;
                    }
                    case '-': {
                        filter = new Exclude(pattern);
                        break;
                    }
                    default: {
                        throw new ReflectionsException("includeExclude should start with either + or -");
                    }
                }
                filters.add(filter);
            }
            return new FilterBuilder(filters);
        }
        return new FilterBuilder();
    }

    public static class Exclude
    extends Matcher {
        public Exclude(String patternString) {
            super(patternString);
        }

        @Override
        public boolean test(String regex) {
            return !this.pattern.matcher(regex).matches();
        }

        @Override
        public String toString() {
            return "-" + super.toString();
        }
    }

    public static class Include
    extends Matcher {
        public Include(String patternString) {
            super(patternString);
        }

        @Override
        public boolean test(String regex) {
            return this.pattern.matcher(regex).matches();
        }

        @Override
        public String toString() {
            return "+" + super.toString();
        }
    }

    public static abstract class Matcher
    implements Predicate<String> {
        final Pattern pattern;

        public Matcher(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        public String toString() {
            return this.pattern.pattern();
        }
    }
}

