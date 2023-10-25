/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config.arbiters;

public interface Arbiter {
    public static final String ELEMENT_TYPE = "Arbiter";

    public boolean isCondition();
}

