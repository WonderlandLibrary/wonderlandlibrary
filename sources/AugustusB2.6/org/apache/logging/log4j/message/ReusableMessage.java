// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilderFormattable;

@PerformanceSensitive({ "allocation" })
public interface ReusableMessage extends Message, StringBuilderFormattable
{
    Object[] swapParameters(final Object[] emptyReplacement);
    
    short getParameterCount();
    
    Message memento();
}
