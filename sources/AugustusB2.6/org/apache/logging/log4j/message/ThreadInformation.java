// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

public interface ThreadInformation
{
    void printThreadInfo(final StringBuilder sb);
    
    void printStack(final StringBuilder sb, final StackTraceElement[] trace);
}
