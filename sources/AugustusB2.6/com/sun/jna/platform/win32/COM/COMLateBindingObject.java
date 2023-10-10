// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.platform.win32.OleAuto;
import java.util.Date;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.platform.win32.Guid;

public class COMLateBindingObject extends COMBindingBaseObject
{
    public COMLateBindingObject(final IDispatch iDispatch) {
        super(iDispatch);
    }
    
    public COMLateBindingObject(final Guid.CLSID clsid, final boolean useActiveInstance) {
        super(clsid, useActiveInstance);
    }
    
    public COMLateBindingObject(final String progId, final boolean useActiveInstance) throws COMException {
        super(progId, useActiveInstance);
    }
    
    protected IDispatch getAutomationProperty(final String propertyName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, propertyName);
        return (IDispatch)result.getValue();
    }
    
    protected IDispatch getAutomationProperty(final String propertyName, final Variant.VARIANT value) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, propertyName, value);
        return (IDispatch)result.getValue();
    }
    
    @Deprecated
    protected IDispatch getAutomationProperty(final String propertyName, final COMLateBindingObject comObject) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, propertyName);
        return (IDispatch)result.getValue();
    }
    
    @Deprecated
    protected IDispatch getAutomationProperty(final String propertyName, final COMLateBindingObject comObject, final Variant.VARIANT value) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, propertyName, value);
        return (IDispatch)result.getValue();
    }
    
    @Deprecated
    protected IDispatch getAutomationProperty(final String propertyName, final IDispatch iDispatch) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, propertyName);
        return (IDispatch)result.getValue();
    }
    
    protected boolean getBooleanProperty(final String propertyName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, propertyName);
        return result.booleanValue();
    }
    
    protected Date getDateProperty(final String propertyName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, propertyName);
        return result.dateValue();
    }
    
    protected int getIntProperty(final String propertyName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, propertyName);
        return result.intValue();
    }
    
    protected short getShortProperty(final String propertyName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, propertyName);
        return result.shortValue();
    }
    
    protected String getStringProperty(final String propertyName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(2, result, propertyName);
        final String res = result.stringValue();
        OleAuto.INSTANCE.VariantClear(result);
        return res;
    }
    
    protected Variant.VARIANT invoke(final String methodName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(1, result, methodName);
        return result;
    }
    
    protected Variant.VARIANT invoke(final String methodName, final Variant.VARIANT arg) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(1, result, methodName, arg);
        return result;
    }
    
    protected Variant.VARIANT invoke(final String methodName, final Variant.VARIANT[] args) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(1, result, methodName, args);
        return result;
    }
    
    protected Variant.VARIANT invoke(final String methodName, final Variant.VARIANT arg1, final Variant.VARIANT arg2) {
        return this.invoke(methodName, new Variant.VARIANT[] { arg1, arg2 });
    }
    
    protected Variant.VARIANT invoke(final String methodName, final Variant.VARIANT arg1, final Variant.VARIANT arg2, final Variant.VARIANT arg3) {
        return this.invoke(methodName, new Variant.VARIANT[] { arg1, arg2, arg3 });
    }
    
    protected Variant.VARIANT invoke(final String methodName, final Variant.VARIANT arg1, final Variant.VARIANT arg2, final Variant.VARIANT arg3, final Variant.VARIANT arg4) {
        return this.invoke(methodName, new Variant.VARIANT[] { arg1, arg2, arg3, arg4 });
    }
    
    @Deprecated
    protected void invokeNoReply(final String methodName, final IDispatch dispatch) {
        this.oleMethod(1, null, dispatch, methodName);
    }
    
    @Deprecated
    protected void invokeNoReply(final String methodName, final COMLateBindingObject comObject) {
        this.oleMethod(1, null, comObject.getIDispatch(), methodName);
    }
    
    protected void invokeNoReply(final String methodName, final Variant.VARIANT arg) {
        this.oleMethod(1, null, methodName, arg);
    }
    
    @Deprecated
    protected void invokeNoReply(final String methodName, final IDispatch dispatch, final Variant.VARIANT arg) {
        this.oleMethod(1, null, dispatch, methodName, arg);
    }
    
    @Deprecated
    protected void invokeNoReply(final String methodName, final IDispatch dispatch, final Variant.VARIANT arg1, final Variant.VARIANT arg2) {
        this.oleMethod(1, null, dispatch, methodName, new Variant.VARIANT[] { arg1, arg2 });
    }
    
    @Deprecated
    protected void invokeNoReply(final String methodName, final COMLateBindingObject comObject, final Variant.VARIANT arg1, final Variant.VARIANT arg2) {
        this.oleMethod(1, null, comObject.getIDispatch(), methodName, new Variant.VARIANT[] { arg1, arg2 });
    }
    
    @Deprecated
    protected void invokeNoReply(final String methodName, final COMLateBindingObject comObject, final Variant.VARIANT arg) {
        this.oleMethod(1, null, comObject.getIDispatch(), methodName, arg);
    }
    
    @Deprecated
    protected void invokeNoReply(final String methodName, final IDispatch dispatch, final Variant.VARIANT[] args) {
        this.oleMethod(1, null, dispatch, methodName, args);
    }
    
    protected void invokeNoReply(final String methodName) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(1, result, methodName);
    }
    
    protected void invokeNoReply(final String methodName, final Variant.VARIANT[] args) {
        final Variant.VARIANT.ByReference result = new Variant.VARIANT.ByReference();
        this.oleMethod(1, result, methodName, args);
    }
    
    protected void invokeNoReply(final String methodName, final Variant.VARIANT arg1, final Variant.VARIANT arg2) {
        this.invokeNoReply(methodName, new Variant.VARIANT[] { arg1, arg2 });
    }
    
    protected void invokeNoReply(final String methodName, final Variant.VARIANT arg1, final Variant.VARIANT arg2, final Variant.VARIANT arg3) {
        this.invokeNoReply(methodName, new Variant.VARIANT[] { arg1, arg2, arg3 });
    }
    
    protected void invokeNoReply(final String methodName, final Variant.VARIANT arg1, final Variant.VARIANT arg2, final Variant.VARIANT arg3, final Variant.VARIANT arg4) {
        this.invokeNoReply(methodName, new Variant.VARIANT[] { arg1, arg2, arg3, arg4 });
    }
    
    protected void setProperty(final String propertyName, final boolean value) {
        this.oleMethod(4, null, propertyName, new Variant.VARIANT(value));
    }
    
    protected void setProperty(final String propertyName, final Date value) {
        this.oleMethod(4, null, propertyName, new Variant.VARIANT(value));
    }
    
    protected void setProperty(final String propertyName, final Dispatch value) {
        this.oleMethod(4, null, propertyName, new Variant.VARIANT(value));
    }
    
    @Deprecated
    protected void setProperty(final String propertyName, final IDispatch value) {
        this.oleMethod(4, null, propertyName, new Variant.VARIANT(value));
    }
    
    protected void setProperty(final String propertyName, final int value) {
        this.oleMethod(4, null, propertyName, new Variant.VARIANT(value));
    }
    
    protected void setProperty(final String propertyName, final short value) {
        this.oleMethod(4, null, propertyName, new Variant.VARIANT(value));
    }
    
    protected void setProperty(final String propertyName, final String value) {
        final Variant.VARIANT wrappedValue = new Variant.VARIANT(value);
        try {
            this.oleMethod(4, null, propertyName, wrappedValue);
        }
        finally {
            OleAuto.INSTANCE.VariantClear(wrappedValue);
        }
    }
    
    protected void setProperty(final String propertyName, final Variant.VARIANT value) {
        this.oleMethod(4, null, propertyName, value);
    }
    
    @Deprecated
    protected void setProperty(final String propertyName, final IDispatch iDispatch, final Variant.VARIANT value) {
        this.oleMethod(4, null, iDispatch, propertyName, value);
    }
    
    @Deprecated
    protected void setProperty(final String propertyName, final COMLateBindingObject comObject, final Variant.VARIANT value) {
        this.oleMethod(4, null, comObject.getIDispatch(), propertyName, value);
    }
    
    public Variant.VARIANT toVariant() {
        return new Variant.VARIANT(this.getIDispatch());
    }
}
