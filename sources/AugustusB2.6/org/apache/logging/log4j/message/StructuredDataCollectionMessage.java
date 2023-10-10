// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public class StructuredDataCollectionMessage implements StringBuilderFormattable, MessageCollectionMessage<StructuredDataMessage>
{
    private static final long serialVersionUID = 5725337076388822924L;
    private final List<StructuredDataMessage> structuredDataMessageList;
    
    public StructuredDataCollectionMessage(final List<StructuredDataMessage> messages) {
        this.structuredDataMessageList = messages;
    }
    
    @Override
    public Iterator<StructuredDataMessage> iterator() {
        return this.structuredDataMessageList.iterator();
    }
    
    @Override
    public String getFormattedMessage() {
        final StringBuilder sb = new StringBuilder();
        this.formatTo(sb);
        return sb.toString();
    }
    
    @Override
    public String getFormat() {
        final StringBuilder sb = new StringBuilder();
        for (final StructuredDataMessage msg : this.structuredDataMessageList) {
            if (msg.getFormat() != null) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(msg.getFormat());
            }
        }
        return sb.toString();
    }
    
    @Override
    public void formatTo(final StringBuilder buffer) {
        for (final StructuredDataMessage msg : this.structuredDataMessageList) {
            msg.formatTo(buffer);
        }
    }
    
    @Override
    public Object[] getParameters() {
        final List<Object[]> objectList = new ArrayList<Object[]>();
        int count = 0;
        for (final StructuredDataMessage msg : this.structuredDataMessageList) {
            final Object[] objects = msg.getParameters();
            if (objects != null) {
                objectList.add(objects);
                count += objects.length;
            }
        }
        final Object[] objects2 = new Object[count];
        int index = 0;
        for (final Object[] array : objectList) {
            final Object[] objs = array;
            for (final Object obj : array) {
                objects2[index++] = obj;
            }
        }
        return objects2;
    }
    
    @Override
    public Throwable getThrowable() {
        for (final StructuredDataMessage msg : this.structuredDataMessageList) {
            final Throwable t = msg.getThrowable();
            if (t != null) {
                return t;
            }
        }
        return null;
    }
}
