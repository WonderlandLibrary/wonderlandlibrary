/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j;

import java.io.Serializable;

public interface Marker
extends Serializable {
    public String getName();

    public Marker getParent();

    public boolean isInstanceOf(Marker var1);

    public boolean isInstanceOf(String var1);
}

