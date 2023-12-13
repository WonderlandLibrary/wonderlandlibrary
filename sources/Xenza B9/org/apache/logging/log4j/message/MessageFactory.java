// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.message;

public interface MessageFactory
{
    Message newMessage(final Object message);
    
    Message newMessage(final String message);
    
    Message newMessage(final String message, final Object... params);
}
