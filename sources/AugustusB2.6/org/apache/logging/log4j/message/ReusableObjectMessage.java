// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({ "allocation" })
public class ReusableObjectMessage implements ReusableMessage, ParameterVisitable, Clearable
{
    private static final long serialVersionUID = 6922476812535519960L;
    private transient Object obj;
    
    public void set(final Object object) {
        this.obj = object;
    }
    
    @Override
    public String getFormattedMessage() {
        return String.valueOf(this.obj);
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        StringBuilders.appendValue(buffer, this.obj);
    }
    
    @Override
    public String getFormat() {
        return (this.obj instanceof String) ? ((String)this.obj) : null;
    }
    
    public Object getParameter() {
        return this.obj;
    }
    
    @Override
    public Object[] getParameters() {
        return new Object[] { this.obj };
    }
    
    @Override
    public String toString() {
        return this.getFormattedMessage();
    }
    
    @Override
    public Throwable getThrowable() {
        return (this.obj instanceof Throwable) ? ((Throwable)this.obj) : null;
    }
    
    @Override
    public Object[] swapParameters(final Object[] emptyReplacement) {
        if (emptyReplacement.length == 0) {
            final Object[] params = new Object[10];
            params[0] = this.obj;
            return params;
        }
        emptyReplacement[0] = this.obj;
        return emptyReplacement;
    }
    
    @Override
    public short getParameterCount() {
        return 1;
    }
    
    @Override
    public <S> void forEachParameter(final ParameterConsumer<S> action, final S state) {
        action.accept(this.obj, 0, state);
    }
    
    @Override
    public Message memento() {
        return new ObjectMessage(this.obj);
    }
    
    @Override
    public void clear() {
        this.obj = null;
    }
}
