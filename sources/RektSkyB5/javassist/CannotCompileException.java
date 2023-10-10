/*
 * Decompiled with CFR 0.152.
 */
package javassist;

import javassist.NotFoundException;
import javassist.compiler.CompileError;

public class CannotCompileException
extends Exception {
    private static final long serialVersionUID = 1L;
    private Throwable myCause;
    private String message;

    @Override
    public synchronized Throwable getCause() {
        return this.myCause == this ? null : this.myCause;
    }

    @Override
    public synchronized Throwable initCause(Throwable cause) {
        this.myCause = cause;
        return this;
    }

    public String getReason() {
        if (this.message != null) {
            return this.message;
        }
        return this.toString();
    }

    public CannotCompileException(String msg) {
        super(msg);
        this.message = msg;
        this.initCause(null);
    }

    public CannotCompileException(Throwable e2) {
        super("by " + e2.toString());
        this.message = null;
        this.initCause(e2);
    }

    public CannotCompileException(String msg, Throwable e2) {
        this(msg);
        this.initCause(e2);
    }

    public CannotCompileException(NotFoundException e2) {
        this("cannot find " + e2.getMessage(), e2);
    }

    public CannotCompileException(CompileError e2) {
        this("[source error] " + e2.getMessage(), e2);
    }

    public CannotCompileException(ClassNotFoundException e2, String name) {
        this("cannot find " + name, e2);
    }

    public CannotCompileException(ClassFormatError e2, String name) {
        this("invalid class format: " + name, e2);
    }
}

