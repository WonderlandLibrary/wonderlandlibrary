/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http;

import java.util.Iterator;
import org.apache.http.Header;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface HeaderIterator
extends Iterator<Object> {
    @Override
    public boolean hasNext();

    public Header nextHeader();
}

