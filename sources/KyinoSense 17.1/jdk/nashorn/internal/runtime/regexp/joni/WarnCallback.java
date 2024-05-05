/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

public interface WarnCallback {
    public static final WarnCallback DEFAULT = new WarnCallback(){

        @Override
        public void warn(String message) {
            System.err.println(message);
        }
    };

    public void warn(String var1);
}

