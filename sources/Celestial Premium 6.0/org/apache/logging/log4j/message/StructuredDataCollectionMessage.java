/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.message.MessageCollectionMessage;
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.util.StringBuilderFormattable;

public class StructuredDataCollectionMessage
implements StringBuilderFormattable,
MessageCollectionMessage<StructuredDataMessage> {
    private static final long serialVersionUID = 5725337076388822924L;
    private final List<StructuredDataMessage> structuredDataMessageList;

    public StructuredDataCollectionMessage(List<StructuredDataMessage> messages) {
        this.structuredDataMessageList = messages;
    }

    @Override
    public Iterator<StructuredDataMessage> iterator() {
        return this.structuredDataMessageList.iterator();
    }

    @Override
    public String getFormattedMessage() {
        StringBuilder sb = new StringBuilder();
        this.formatTo(sb);
        return sb.toString();
    }

    @Override
    public String getFormat() {
        StringBuilder sb = new StringBuilder();
        for (StructuredDataMessage msg : this.structuredDataMessageList) {
            if (msg.getFormat() == null) continue;
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(msg.getFormat());
        }
        return sb.toString();
    }

    @Override
    public void formatTo(StringBuilder buffer) {
        for (StructuredDataMessage msg : this.structuredDataMessageList) {
            msg.formatTo(buffer);
        }
    }

    @Override
    public Object[] getParameters() {
        ArrayList<Object[]> objectList = new ArrayList<Object[]>();
        int count = 0;
        for (StructuredDataMessage msg : this.structuredDataMessageList) {
            Object[] objects = msg.getParameters();
            if (objects == null) continue;
            objectList.add(objects);
            count += objects.length;
        }
        Object[] objects = new Object[count];
        int index = 0;
        Iterator iterator = objectList.iterator();
        while (iterator.hasNext()) {
            Object[] objs;
            for (Object obj : objs = (Object[])iterator.next()) {
                objects[index++] = obj;
            }
        }
        return objects;
    }

    @Override
    public Throwable getThrowable() {
        for (StructuredDataMessage msg : this.structuredDataMessageList) {
            Throwable t = msg.getThrowable();
            if (t == null) continue;
            return t;
        }
        return null;
    }
}

