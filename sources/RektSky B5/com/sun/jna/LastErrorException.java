/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Platform;

public class LastErrorException
extends RuntimeException {
    private int errorCode;

    private static String formatMessage(int code) {
        return Platform.isWindows() ? "GetLastError() returned " + code : "errno was " + code;
    }

    private static String parseMessage(String m2) {
        try {
            return LastErrorException.formatMessage(Integer.parseInt(m2));
        }
        catch (NumberFormatException e2) {
            return m2;
        }
    }

    public LastErrorException(String msg) {
        super(LastErrorException.parseMessage(msg.trim()));
        try {
            if (msg.startsWith("[")) {
                msg = msg.substring(1, msg.indexOf("]"));
            }
            this.errorCode = Integer.parseInt(msg);
        }
        catch (NumberFormatException e2) {
            this.errorCode = -1;
        }
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public LastErrorException(int code) {
        super(LastErrorException.formatMessage(code));
        this.errorCode = code;
    }
}

