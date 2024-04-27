/*
 * Decompiled with CFR 0.152.
 */
package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;

public interface HierarchyEventListener {
    public void addAppenderEvent(Category var1, Appender var2);

    public void removeAppenderEvent(Category var1, Appender var2);
}

