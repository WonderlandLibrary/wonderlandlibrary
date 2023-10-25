/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.message;

import org.apache.logging.log4j.message.Clearable;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ObjectMessage;
import org.apache.logging.log4j.message.ParameterConsumer;
import org.apache.logging.log4j.message.ParameterVisitable;
import org.apache.logging.log4j.message.ReusableMessage;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.StringBuilders;

@PerformanceSensitive(value={"allocation"})
public class ReusableObjectMessage
implements ReusableMessage,
ParameterVisitable,
Clearable {
    private static final long serialVersionUID = 6922476812535519960L;
    private transient Object obj;

    public void set(Object object) {
        this.obj = object;
    }

    @Override
    public String getFormattedMessage() {
        return String.valueOf(this.obj);
    }

    @Override
    public void formatTo(StringBuilder buffer) {
        StringBuilders.appendValue(buffer, this.obj);
    }

    @Override
    public String getFormat() {
        return this.obj instanceof String ? (String)this.obj : null;
    }

    public Object getParameter() {
        return this.obj;
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{this.obj};
    }

    public String toString() {
        return this.getFormattedMessage();
    }

    @Override
    public Throwable getThrowable() {
        return this.obj instanceof Throwable ? (Throwable)this.obj : null;
    }

    @Override
    public Object[] swapParameters(Object[] emptyReplacement) {
        if (emptyReplacement.length == 0) {
            Object[] params = new Object[10];
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
    public <S> void forEachParameter(ParameterConsumer<S> action, S state) {
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

