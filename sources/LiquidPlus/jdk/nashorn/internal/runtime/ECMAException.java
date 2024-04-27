/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import javax.script.ScriptException;
import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;

public final class ECMAException
extends NashornException {
    public static final CompilerConstants.Call CREATE = CompilerConstants.staticCallNoLookup(ECMAException.class, "create", ECMAException.class, Object.class, String.class, Integer.TYPE, Integer.TYPE);
    public static final CompilerConstants.FieldAccess THROWN = CompilerConstants.virtualField(ECMAException.class, "thrown", Object.class);
    private static final String EXCEPTION_PROPERTY = "nashornException";
    public final Object thrown;

    private ECMAException(Object thrown, String fileName, int line, int column) {
        super(ScriptRuntime.safeToString(thrown), ECMAException.asThrowable(thrown), fileName, line, column);
        this.thrown = thrown;
        this.setExceptionToThrown();
    }

    public ECMAException(Object thrown, Throwable cause) {
        super(ScriptRuntime.safeToString(thrown), cause);
        this.thrown = thrown;
        this.setExceptionToThrown();
    }

    public static ECMAException create(Object thrown, String fileName, int line, int column) {
        ECMAException ee;
        Object exception;
        if (thrown instanceof ScriptObject && (exception = ECMAException.getException((ScriptObject)thrown)) instanceof ECMAException && (ee = (ECMAException)exception).getThrown() == thrown) {
            ee.setFileName(fileName);
            ee.setLineNumber(line);
            ee.setColumnNumber(column);
            return ee;
        }
        return new ECMAException(thrown, fileName, line, column);
    }

    @Override
    public Object getThrown() {
        return this.thrown;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String fileName = this.getFileName();
        int line = this.getLineNumber();
        int column = this.getColumnNumber();
        if (fileName != null) {
            sb.append(fileName);
            if (line >= 0) {
                sb.append(':');
                sb.append(line);
            }
            if (column >= 0) {
                sb.append(':');
                sb.append(column);
            }
            sb.append(' ');
        } else {
            sb.append("ECMAScript Exception: ");
        }
        sb.append(this.getMessage());
        return sb.toString();
    }

    public static Object getException(ScriptObject errObj) {
        if (errObj.hasOwnProperty(EXCEPTION_PROPERTY)) {
            return errObj.get(EXCEPTION_PROPERTY);
        }
        return null;
    }

    public static Object printStackTrace(ScriptObject errObj) {
        Object exception = ECMAException.getException(errObj);
        if (exception instanceof Throwable) {
            ((Throwable)exception).printStackTrace(Context.getCurrentErr());
        } else {
            Context.err("<stack trace not available>");
        }
        return ScriptRuntime.UNDEFINED;
    }

    public static Object getLineNumber(ScriptObject errObj) {
        Object e = ECMAException.getException(errObj);
        if (e instanceof NashornException) {
            return ((NashornException)e).getLineNumber();
        }
        if (e instanceof ScriptException) {
            return ((ScriptException)e).getLineNumber();
        }
        return ScriptRuntime.UNDEFINED;
    }

    public static Object getColumnNumber(ScriptObject errObj) {
        Object e = ECMAException.getException(errObj);
        if (e instanceof NashornException) {
            return ((NashornException)e).getColumnNumber();
        }
        if (e instanceof ScriptException) {
            return ((ScriptException)e).getColumnNumber();
        }
        return ScriptRuntime.UNDEFINED;
    }

    public static Object getFileName(ScriptObject errObj) {
        Object e = ECMAException.getException(errObj);
        if (e instanceof NashornException) {
            return ((NashornException)e).getFileName();
        }
        if (e instanceof ScriptException) {
            return ((ScriptException)e).getFileName();
        }
        return ScriptRuntime.UNDEFINED;
    }

    public static String safeToString(ScriptObject errObj) {
        Object name = ScriptRuntime.UNDEFINED;
        try {
            name = errObj.get("name");
        }
        catch (Exception exception) {
            // empty catch block
        }
        name = name == ScriptRuntime.UNDEFINED ? "Error" : ScriptRuntime.safeToString(name);
        Object msg = ScriptRuntime.UNDEFINED;
        try {
            msg = errObj.get("message");
        }
        catch (Exception exception) {
            // empty catch block
        }
        msg = msg == ScriptRuntime.UNDEFINED ? "" : ScriptRuntime.safeToString(msg);
        if (((String)name).isEmpty()) {
            return (String)msg;
        }
        if (((String)msg).isEmpty()) {
            return (String)name;
        }
        return name + ": " + msg;
    }

    private static Throwable asThrowable(Object obj) {
        return obj instanceof Throwable ? (Throwable)obj : null;
    }

    private void setExceptionToThrown() {
        if (this.thrown instanceof ScriptObject) {
            ScriptObject sobj = (ScriptObject)this.thrown;
            if (!sobj.has(EXCEPTION_PROPERTY)) {
                sobj.addOwnProperty(EXCEPTION_PROPERTY, 2, this);
            } else {
                sobj.set((Object)EXCEPTION_PROPERTY, (Object)this, 0);
            }
        }
    }
}

