// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({ "allocation" })
public interface ParameterVisitable
{
     <S> void forEachParameter(final ParameterConsumer<S> action, final S state);
}
