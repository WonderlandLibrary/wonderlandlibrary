/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.appender.db.nosql;

import java.io.Closeable;
import org.apache.logging.log4j.core.appender.db.nosql.NoSQLObject;

public interface NoSQLConnection<W, T extends NoSQLObject<W>>
extends Closeable {
    public T createObject();

    public T[] createList(int var1);

    public void insertObject(NoSQLObject<W> var1);

    @Override
    public void close();

    public boolean isClosed();
}

