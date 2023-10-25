/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec;

import io.netty.util.Signal;

public class DecoderResult {
    public static final DecoderResult UNFINISHED;
    protected static final Signal SIGNAL_UNFINISHED;
    private final Throwable cause;
    public static final DecoderResult SUCCESS;
    protected static final Signal SIGNAL_SUCCESS;

    public Throwable cause() {
        if (this.isFailure()) {
            return this.cause;
        }
        return null;
    }

    public boolean isFinished() {
        return this.cause != SIGNAL_UNFINISHED;
    }

    public boolean isFailure() {
        return this.cause != SIGNAL_SUCCESS && this.cause != SIGNAL_UNFINISHED;
    }

    public static DecoderResult failure(Throwable throwable) {
        if (throwable == null) {
            throw new NullPointerException("cause");
        }
        return new DecoderResult(throwable);
    }

    static {
        SIGNAL_UNFINISHED = Signal.valueOf(DecoderResult.class.getName() + ".UNFINISHED");
        SIGNAL_SUCCESS = Signal.valueOf(DecoderResult.class.getName() + ".SUCCESS");
        UNFINISHED = new DecoderResult(SIGNAL_UNFINISHED);
        SUCCESS = new DecoderResult(SIGNAL_SUCCESS);
    }

    protected DecoderResult(Throwable throwable) {
        if (throwable == null) {
            throw new NullPointerException("cause");
        }
        this.cause = throwable;
    }

    public boolean isSuccess() {
        return this.cause == SIGNAL_SUCCESS;
    }

    public String toString() {
        if (this.isFinished()) {
            if (this.isSuccess()) {
                return "success";
            }
            String string = this.cause().toString();
            StringBuilder stringBuilder = new StringBuilder(string.length() + 17);
            stringBuilder.append("failure(");
            stringBuilder.append(string);
            stringBuilder.append(')');
            return stringBuilder.toString();
        }
        return "unfinished";
    }
}

