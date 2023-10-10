// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

import org.apache.logging.log4j.util.StringBuilders;
import org.apache.logging.log4j.util.Constants;
import java.util.Arrays;
import org.apache.logging.log4j.util.PerformanceSensitive;

@PerformanceSensitive({ "allocation" })
public class ReusableParameterizedMessage implements ReusableMessage, ParameterVisitable, Clearable
{
    private static final int MIN_BUILDER_SIZE = 512;
    private static final int MAX_PARMS = 10;
    private static final long serialVersionUID = 7800075879295123856L;
    private transient ThreadLocal<StringBuilder> buffer;
    private String messagePattern;
    private int argCount;
    private int usedCount;
    private final int[] indices;
    private transient Object[] varargs;
    private transient Object[] params;
    private transient Throwable throwable;
    transient boolean reserved;
    
    public ReusableParameterizedMessage() {
        this.indices = new int[256];
        this.params = new Object[10];
        this.reserved = false;
    }
    
    private Object[] getTrimmedParams() {
        return (this.varargs == null) ? Arrays.copyOf(this.params, this.argCount) : this.varargs;
    }
    
    private Object[] getParams() {
        return (this.varargs == null) ? this.params : this.varargs;
    }
    
    @Override
    public Object[] swapParameters(final Object[] emptyReplacement) {
        Object[] result;
        if (this.varargs == null) {
            result = this.params;
            if (emptyReplacement.length >= 10) {
                this.params = emptyReplacement;
            }
            else if (this.argCount <= emptyReplacement.length) {
                System.arraycopy(this.params, 0, emptyReplacement, 0, this.argCount);
                for (int i = 0; i < this.argCount; ++i) {
                    this.params[i] = null;
                }
                result = emptyReplacement;
            }
            else {
                this.params = new Object[10];
            }
        }
        else {
            if (this.argCount <= emptyReplacement.length) {
                result = emptyReplacement;
            }
            else {
                result = new Object[this.argCount];
            }
            System.arraycopy(this.varargs, 0, result, 0, this.argCount);
        }
        return result;
    }
    
    @Override
    public short getParameterCount() {
        return (short)this.argCount;
    }
    
    @Override
    public <S> void forEachParameter(final ParameterConsumer<S> action, final S state) {
        final Object[] parameters = this.getParams();
        for (short i = 0; i < this.argCount; ++i) {
            action.accept(parameters[i], i, state);
        }
    }
    
    @Override
    public Message memento() {
        return new ParameterizedMessage(this.messagePattern, this.getTrimmedParams());
    }
    
    private void init(final String messagePattern, final int argCount, final Object[] paramArray) {
        this.varargs = null;
        this.messagePattern = messagePattern;
        this.argCount = argCount;
        final int placeholderCount = count(messagePattern, this.indices);
        this.initThrowable(paramArray, argCount, placeholderCount);
        this.usedCount = Math.min(placeholderCount, argCount);
    }
    
    private static int count(final String messagePattern, final int[] indices) {
        try {
            return ParameterFormatter.countArgumentPlaceholders2(messagePattern, indices);
        }
        catch (Exception ex) {
            return ParameterFormatter.countArgumentPlaceholders(messagePattern);
        }
    }
    
    private void initThrowable(final Object[] params, final int argCount, final int usedParams) {
        if (usedParams < argCount && params[argCount - 1] instanceof Throwable) {
            this.throwable = (Throwable)params[argCount - 1];
        }
        else {
            this.throwable = null;
        }
    }
    
    ReusableParameterizedMessage set(final String messagePattern, final Object... arguments) {
        this.init(messagePattern, (arguments == null) ? 0 : arguments.length, arguments);
        this.varargs = arguments;
        return this;
    }
    
    ReusableParameterizedMessage set(final String messagePattern, final Object p0) {
        this.params[0] = p0;
        this.init(messagePattern, 1, this.params);
        return this;
    }
    
    ReusableParameterizedMessage set(final String messagePattern, final Object p0, final Object p1) {
        this.params[0] = p0;
        this.params[1] = p1;
        this.init(messagePattern, 2, this.params);
        return this;
    }
    
    ReusableParameterizedMessage set(final String messagePattern, final Object p0, final Object p1, final Object p2) {
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.init(messagePattern, 3, this.params);
        return this;
    }
    
    ReusableParameterizedMessage set(final String messagePattern, final Object p0, final Object p1, final Object p2, final Object p3) {
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.params[3] = p3;
        this.init(messagePattern, 4, this.params);
        return this;
    }
    
    ReusableParameterizedMessage set(final String messagePattern, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4) {
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.params[3] = p3;
        this.params[4] = p4;
        this.init(messagePattern, 5, this.params);
        return this;
    }
    
    ReusableParameterizedMessage set(final String messagePattern, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5) {
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.params[3] = p3;
        this.params[4] = p4;
        this.params[5] = p5;
        this.init(messagePattern, 6, this.params);
        return this;
    }
    
    ReusableParameterizedMessage set(final String messagePattern, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6) {
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.params[3] = p3;
        this.params[4] = p4;
        this.params[5] = p5;
        this.params[6] = p6;
        this.init(messagePattern, 7, this.params);
        return this;
    }
    
    ReusableParameterizedMessage set(final String messagePattern, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7) {
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.params[3] = p3;
        this.params[4] = p4;
        this.params[5] = p5;
        this.params[6] = p6;
        this.params[7] = p7;
        this.init(messagePattern, 8, this.params);
        return this;
    }
    
    ReusableParameterizedMessage set(final String messagePattern, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8) {
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.params[3] = p3;
        this.params[4] = p4;
        this.params[5] = p5;
        this.params[6] = p6;
        this.params[7] = p7;
        this.params[8] = p8;
        this.init(messagePattern, 9, this.params);
        return this;
    }
    
    ReusableParameterizedMessage set(final String messagePattern, final Object p0, final Object p1, final Object p2, final Object p3, final Object p4, final Object p5, final Object p6, final Object p7, final Object p8, final Object p9) {
        this.params[0] = p0;
        this.params[1] = p1;
        this.params[2] = p2;
        this.params[3] = p3;
        this.params[4] = p4;
        this.params[5] = p5;
        this.params[6] = p6;
        this.params[7] = p7;
        this.params[8] = p8;
        this.params[9] = p9;
        this.init(messagePattern, 10, this.params);
        return this;
    }
    
    @Override
    public String getFormat() {
        return this.messagePattern;
    }
    
    @Override
    public Object[] getParameters() {
        return this.getTrimmedParams();
    }
    
    @Override
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    @Override
    public String getFormattedMessage() {
        final StringBuilder sb = this.getBuffer();
        this.formatTo(sb);
        final String result = sb.toString();
        StringBuilders.trimToMaxSize(sb, Constants.MAX_REUSABLE_MESSAGE_SIZE);
        return result;
    }
    
    private StringBuilder getBuffer() {
        if (this.buffer == null) {
            this.buffer = new ThreadLocal<StringBuilder>();
        }
        StringBuilder result = this.buffer.get();
        if (result == null) {
            final int currentPatternLength = (this.messagePattern == null) ? 0 : this.messagePattern.length();
            result = new StringBuilder(Math.max(512, currentPatternLength * 2));
            this.buffer.set(result);
        }
        result.setLength(0);
        return result;
    }
    
    @Override
    public void formatTo(final StringBuilder builder) {
        if (this.indices[0] < 0) {
            ParameterFormatter.formatMessage(builder, this.messagePattern, this.getParams(), this.argCount);
        }
        else {
            ParameterFormatter.formatMessage2(builder, this.messagePattern, this.getParams(), this.usedCount, this.indices);
        }
    }
    
    ReusableParameterizedMessage reserve() {
        this.reserved = true;
        return this;
    }
    
    @Override
    public String toString() {
        return "ReusableParameterizedMessage[messagePattern=" + this.getFormat() + ", stringArgs=" + Arrays.toString(this.getParameters()) + ", throwable=" + this.getThrowable() + ']';
    }
    
    @Override
    public void clear() {
        this.reserved = false;
        this.varargs = null;
        this.messagePattern = null;
        this.throwable = null;
    }
}
