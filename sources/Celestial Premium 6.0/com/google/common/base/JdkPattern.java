/*
 * Decompiled with CFR 0.150.
 */
package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.CommonMatcher;
import com.google.common.base.CommonPattern;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@GwtIncompatible
final class JdkPattern
extends CommonPattern
implements Serializable {
    private final Pattern pattern;
    private static final long serialVersionUID = 0L;

    JdkPattern(Pattern pattern) {
        this.pattern = Preconditions.checkNotNull(pattern);
    }

    @Override
    CommonMatcher matcher(CharSequence t) {
        return new JdkMatcher(this.pattern.matcher(t));
    }

    @Override
    String pattern() {
        return this.pattern.pattern();
    }

    @Override
    int flags() {
        return this.pattern.flags();
    }

    @Override
    public String toString() {
        return this.pattern.toString();
    }

    @Override
    public int hashCode() {
        return this.pattern.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof JdkPattern)) {
            return false;
        }
        return this.pattern.equals(((JdkPattern)o).pattern);
    }

    private static final class JdkMatcher
    extends CommonMatcher {
        final Matcher matcher;

        JdkMatcher(Matcher matcher) {
            this.matcher = Preconditions.checkNotNull(matcher);
        }

        @Override
        boolean matches() {
            return this.matcher.matches();
        }

        @Override
        boolean find() {
            return this.matcher.find();
        }

        @Override
        boolean find(int index) {
            return this.matcher.find(index);
        }

        @Override
        String replaceAll(String replacement) {
            return this.matcher.replaceAll(replacement);
        }

        @Override
        int end() {
            return this.matcher.end();
        }

        @Override
        int start() {
            return this.matcher.start();
        }
    }
}

