// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.log4j.spi;

import org.apache.logging.log4j.util.Strings;
import java.util.List;
import java.lang.reflect.Method;
import org.apache.log4j.Category;
import java.io.Serializable;

public class ThrowableInformation implements Serializable
{
    static final long serialVersionUID = -4748765566864322735L;
    private transient Throwable throwable;
    private transient Category category;
    private String[] rep;
    private static final Method TO_STRING_LIST;
    
    public ThrowableInformation(final String[] r) {
        this.rep = (String[])((r != null) ? ((String[])r.clone()) : null);
    }
    
    public ThrowableInformation(final Throwable throwable) {
        this.throwable = throwable;
    }
    
    public ThrowableInformation(final Throwable throwable, final Category category) {
        this(throwable);
        this.category = category;
        this.rep = null;
    }
    
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    public synchronized String[] getThrowableStrRep() {
        if (ThrowableInformation.TO_STRING_LIST != null && this.throwable != null) {
            try {
                final List<String> elements = (List<String>)ThrowableInformation.TO_STRING_LIST.invoke(null, this.throwable);
                if (elements != null) {
                    return elements.toArray(Strings.EMPTY_ARRAY);
                }
            }
            catch (final ReflectiveOperationException ex) {}
        }
        return this.rep;
    }
    
    static {
        Method method = null;
        try {
            final Class<?> throwables = Class.forName("org.apache.logging.log4j.core.util.Throwables");
            method = throwables.getMethod("toStringList", Throwable.class);
        }
        catch (final ClassNotFoundException | NoSuchMethodException ex) {}
        TO_STRING_LIST = method;
    }
}
