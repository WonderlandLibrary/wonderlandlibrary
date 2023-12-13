/*
 * Decompiled with CFR 0.152.
 */
package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.Message;

public class SimpleMessage
implements Message {
    private static final long serialVersionUID = -8398002534962715992L;
    private final String message;

    public SimpleMessage() {
        this(null);
    }

    public SimpleMessage(String message) {
        this.message = message;
    }

    @Override
    public String getFormattedMessage() {
        return this.message;
    }

    @Override
    public String getFormat() {
        return this.message;
    }

    @Override
    public Object[] getParameters() {
        return null;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || this.getClass() != o2.getClass()) {
            return false;
        }
        SimpleMessage that = (SimpleMessage)o2;
        return !(this.message == null ? that.message != null : !this.message.equals(that.message));
    }

    public int hashCode() {
        return this.message != null ? this.message.hashCode() : 0;
    }

    public String toString() {
        return "SimpleMessage[message=" + this.message + "]";
    }

    @Override
    public Throwable getThrowable() {
        return null;
    }
}

