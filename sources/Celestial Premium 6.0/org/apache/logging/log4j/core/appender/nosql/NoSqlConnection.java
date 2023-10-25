/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.nosql;

import java.io.Closeable;
import org.apache.logging.log4j.core.appender.nosql.NoSqlObject;

public interface NoSqlConnection<W, T extends NoSqlObject<W>>
extends Closeable {
    public T createObject();

    public T[] createList(int var1);

    public void insertObject(NoSqlObject<W> var1);

    @Override
    public void close();

    public boolean isClosed();
}

