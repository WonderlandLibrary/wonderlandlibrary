/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.nosql;

public interface NoSqlObject<W> {
    public void set(String var1, Object var2);

    public void set(String var1, NoSqlObject<W> var2);

    public void set(String var1, Object[] var2);

    public void set(String var1, NoSqlObject<W>[] var2);

    public W unwrap();
}

