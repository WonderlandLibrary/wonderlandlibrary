// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({ "allocation" })
public class ReusableSimpleMessage implements ReusableMessage, CharSequence, ParameterVisitable, Clearable
{
    private static final long serialVersionUID = -9199974506498249809L;
    private CharSequence charSequence;
    
    public void set(final String message) {
        this.charSequence = message;
    }
    
    public void set(final CharSequence charSequence) {
        this.charSequence = charSequence;
    }
    
    @Override
    public String getFormattedMessage() {
        return String.valueOf(this.charSequence);
    }
    
    @Override
    public String getFormat() {
        return (this.charSequence instanceof String) ? ((String)this.charSequence) : null;
    }
    
    @Override
    public Object[] getParameters() {
        return Constants.EMPTY_OBJECT_ARRAY;
    }
    
    @Override
    public Throwable getThrowable() {
        return null;
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        buffer.append(this.charSequence);
    }
    
    @Override
    public Object[] swapParameters(final Object[] emptyReplacement) {
        return emptyReplacement;
    }
    
    @Override
    public short getParameterCount() {
        return 0;
    }
    
    @Override
    public <S> void forEachParameter(final ParameterConsumer<S> action, final S state) {
    }
    
    @Override
    public Message memento() {
        return new SimpleMessage(this.charSequence);
    }
    
    @Override
    public int length() {
        return (this.charSequence == null) ? 0 : this.charSequence.length();
    }
    
    @Override
    public char charAt(final int index) {
        return this.charSequence.charAt(index);
    }
    
    @Override
    public CharSequence subSequence(final int start, final int end) {
        return this.charSequence.subSequence(start, end);
    }
    
    @Override
    public void clear() {
        this.charSequence = null;
    }
}
