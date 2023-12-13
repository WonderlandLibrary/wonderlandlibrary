// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.config;

public class PropertySetterException extends Exception
{
    private static final long serialVersionUID = -1352613734254235861L;
    protected Throwable rootCause;
    
    public PropertySetterException(final String msg) {
        super(msg);
    }
    
    public PropertySetterException(final Throwable rootCause) {
        this.rootCause = rootCause;
    }
    
    @Override
    public String getMessage() {
        String msg = super.getMessage();
        if (msg == null && this.rootCause != null) {
            msg = this.rootCause.getMessage();
        }
        return msg;
    }
}
