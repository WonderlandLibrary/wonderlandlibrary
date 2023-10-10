// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import java.util.Arrays;
import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.apache.logging.log4j.message.Message;

public final class MementoMessage implements Message, StringBuilderFormattable
{
    private final String formattedMessage;
    private final String format;
    private final Object[] parameters;
    
    public MementoMessage(final String formattedMessage, final String format, final Object[] parameters) {
        this.formattedMessage = formattedMessage;
        this.format = format;
        this.parameters = parameters;
    }
    
    @Override
    public String getFormattedMessage() {
        return this.formattedMessage;
    }
    
    @Override
    public String getFormat() {
        return this.format;
    }
    
    @Override
    public Object[] getParameters() {
        return this.parameters;
    }
    
    @Override
    public Throwable getThrowable() {
        return null;
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        buffer.append(this.formattedMessage);
    }
    
    @Override
    public String toString() {
        return "MementoMessage{formattedMessage='" + this.formattedMessage + '\'' + ", format='" + this.format + '\'' + ", parameters=" + Arrays.toString(this.parameters) + '}';
    }
}
