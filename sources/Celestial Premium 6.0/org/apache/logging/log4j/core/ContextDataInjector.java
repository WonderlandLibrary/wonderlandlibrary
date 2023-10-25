/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core;

import java.util.List;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.StringMap;

public interface ContextDataInjector {
    public StringMap injectContextData(List<Property> var1, StringMap var2);

    public ReadOnlyStringMap rawContextData();
}

