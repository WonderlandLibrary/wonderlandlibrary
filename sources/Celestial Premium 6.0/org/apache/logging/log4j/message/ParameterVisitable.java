/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.ParameterConsumer;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive(value={"allocation"})
public interface ParameterVisitable {
    public <S> void forEachParameter(ParameterConsumer<S> var1, S var2);
}

