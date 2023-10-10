/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http;

import java.util.Iterator;

public interface TokenIterator
extends Iterator<Object> {
    @Override
    public boolean hasNext();

    public String nextToken();
}

