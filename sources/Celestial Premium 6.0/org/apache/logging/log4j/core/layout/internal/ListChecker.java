/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.layout.internal;

public interface ListChecker {
    public static final NoopChecker NOOP_CHECKER = new NoopChecker();

    public boolean check(String var1);

    public static class NoopChecker
    implements ListChecker {
        @Override
        public boolean check(String key) {
            return true;
        }

        public String toString() {
            return "";
        }
    }
}

