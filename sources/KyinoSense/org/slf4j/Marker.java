/*
 * Decompiled with CFR 0.152.
 */
package org.slf4j;

import java.io.Serializable;
import java.util.Iterator;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface Marker
extends Serializable {
    public static final String ANY_MARKER = "*";
    public static final String ANY_NON_NULL_MARKER = "+";

    public String getName();

    public void add(Marker var1);

    public boolean remove(Marker var1);

    public boolean hasChildren();

    public boolean hasReferences();

    public Iterator<Marker> iterator();

    public boolean contains(Marker var1);

    public boolean contains(String var1);

    public boolean equals(Object var1);

    public int hashCode();
}

