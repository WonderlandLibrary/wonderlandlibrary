// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public class ObjectMessage implements Message, StringBuilderFormattable
{
    private static final long serialVersionUID = -5903272448334166185L;
    private transient Object obj;
    private transient String objectString;
    
    public ObjectMessage(final Object obj) {
        this.obj = ((obj == null) ? "null" : obj);
    }
    
    @Override
    public String getFormattedMessage() {
        if (this.objectString == null) {
            this.objectString = String.valueOf(this.obj);
        }
        return this.objectString;
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        if (this.objectString != null) {
            buffer.append(this.objectString);
        }
        else {
            StringBuilders.appendValue(buffer, this.obj);
        }
    }
    
    @Override
    public String getFormat() {
        return this.getFormattedMessage();
    }
    
    public Object getParameter() {
        return this.obj;
    }
    
    @Override
    public Object[] getParameters() {
        return new Object[] { this.obj };
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ObjectMessage that = (ObjectMessage)o;
        return (this.obj == null) ? (that.obj == null) : this.equalObjectsOrStrings(this.obj, that.obj);
    }
    
    private boolean equalObjectsOrStrings(final Object left, final Object right) {
        return left.equals(right) || String.valueOf(left).equals(String.valueOf(right));
    }
    
    @Override
    public int hashCode() {
        return (this.obj != null) ? this.obj.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return this.getFormattedMessage();
    }
    
    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        if (this.obj instanceof Serializable) {
            out.writeObject(this.obj);
        }
        else {
            out.writeObject(String.valueOf(this.obj));
        }
    }
    
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.obj = in.readObject();
    }
    
    @Override
    public Throwable getThrowable() {
        return (this.obj instanceof Throwable) ? ((Throwable)this.obj) : null;
    }
}
