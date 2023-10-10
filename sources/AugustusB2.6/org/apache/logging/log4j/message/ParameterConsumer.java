// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

public interface ParameterConsumer<S>
{
    void accept(final Object parameter, final int parameterIndex, final S state);
}
