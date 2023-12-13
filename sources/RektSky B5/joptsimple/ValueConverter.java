/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface ValueConverter<V> {
    public V convert(String var1);

    public Class<? extends V> valueType();

    public String valuePattern();
}

