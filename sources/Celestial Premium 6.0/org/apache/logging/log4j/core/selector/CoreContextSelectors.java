/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.selector;

import org.apache.logging.log4j.core.async.AsyncLoggerContextSelector;
import org.apache.logging.log4j.core.selector.BasicContextSelector;
import org.apache.logging.log4j.core.selector.ClassLoaderContextSelector;

public class CoreContextSelectors {
    public static final Class<?>[] CLASSES = new Class[]{ClassLoaderContextSelector.class, BasicContextSelector.class, AsyncLoggerContextSelector.class};
}

