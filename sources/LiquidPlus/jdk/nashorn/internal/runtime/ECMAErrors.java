/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.JSErrorType;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.scripts.JS;

public final class ECMAErrors {
    private static final String MESSAGES_RESOURCE = "jdk.nashorn.internal.runtime.resources.Messages";
    private static final ResourceBundle MESSAGES_BUNDLE = ResourceBundle.getBundle("jdk.nashorn.internal.runtime.resources.Messages", Locale.getDefault());
    private static final String scriptPackage;

    private ECMAErrors() {
    }

    private static ECMAException error(Object thrown, Throwable cause) {
        return new ECMAException(thrown, cause);
    }

    public static ECMAException asEcmaException(ParserException e) {
        return ECMAErrors.asEcmaException(Context.getGlobal(), e);
    }

    public static ECMAException asEcmaException(Global global, ParserException e) {
        JSErrorType errorType = e.getErrorType();
        assert (errorType != null) : "error type for " + e + " was null";
        Global globalObj = global;
        String msg = e.getMessage();
        switch (errorType) {
            case ERROR: {
                return ECMAErrors.error(globalObj.newError(msg), e);
            }
            case EVAL_ERROR: {
                return ECMAErrors.error(globalObj.newEvalError(msg), e);
            }
            case RANGE_ERROR: {
                return ECMAErrors.error(globalObj.newRangeError(msg), e);
            }
            case REFERENCE_ERROR: {
                return ECMAErrors.error(globalObj.newReferenceError(msg), e);
            }
            case SYNTAX_ERROR: {
                return ECMAErrors.error(globalObj.newSyntaxError(msg), e);
            }
            case TYPE_ERROR: {
                return ECMAErrors.error(globalObj.newTypeError(msg), e);
            }
            case URI_ERROR: {
                return ECMAErrors.error(globalObj.newURIError(msg), e);
            }
        }
        throw new AssertionError((Object)e.getMessage());
    }

    public static ECMAException syntaxError(String msgId, String ... args2) {
        return ECMAErrors.syntaxError(Context.getGlobal(), msgId, args2);
    }

    public static ECMAException syntaxError(Global global, String msgId, String ... args2) {
        return ECMAErrors.syntaxError(global, null, msgId, args2);
    }

    public static ECMAException syntaxError(Throwable cause, String msgId, String ... args2) {
        return ECMAErrors.syntaxError(Context.getGlobal(), cause, msgId, args2);
    }

    public static ECMAException syntaxError(Global global, Throwable cause, String msgId, String ... args2) {
        String msg = ECMAErrors.getMessage("syntax.error." + msgId, args2);
        return ECMAErrors.error(global.newSyntaxError(msg), cause);
    }

    public static ECMAException typeError(String msgId, String ... args2) {
        return ECMAErrors.typeError(Context.getGlobal(), msgId, args2);
    }

    public static ECMAException typeError(Global global, String msgId, String ... args2) {
        return ECMAErrors.typeError(global, null, msgId, args2);
    }

    public static ECMAException typeError(Throwable cause, String msgId, String ... args2) {
        return ECMAErrors.typeError(Context.getGlobal(), cause, msgId, args2);
    }

    public static ECMAException typeError(Global global, Throwable cause, String msgId, String ... args2) {
        String msg = ECMAErrors.getMessage("type.error." + msgId, args2);
        return ECMAErrors.error(global.newTypeError(msg), cause);
    }

    public static ECMAException rangeError(String msgId, String ... args2) {
        return ECMAErrors.rangeError(Context.getGlobal(), msgId, args2);
    }

    public static ECMAException rangeError(Global global, String msgId, String ... args2) {
        return ECMAErrors.rangeError(global, null, msgId, args2);
    }

    public static ECMAException rangeError(Throwable cause, String msgId, String ... args2) {
        return ECMAErrors.rangeError(Context.getGlobal(), cause, msgId, args2);
    }

    public static ECMAException rangeError(Global global, Throwable cause, String msgId, String ... args2) {
        String msg = ECMAErrors.getMessage("range.error." + msgId, args2);
        return ECMAErrors.error(global.newRangeError(msg), cause);
    }

    public static ECMAException referenceError(String msgId, String ... args2) {
        return ECMAErrors.referenceError(Context.getGlobal(), msgId, args2);
    }

    public static ECMAException referenceError(Global global, String msgId, String ... args2) {
        return ECMAErrors.referenceError(global, null, msgId, args2);
    }

    public static ECMAException referenceError(Throwable cause, String msgId, String ... args2) {
        return ECMAErrors.referenceError(Context.getGlobal(), cause, msgId, args2);
    }

    public static ECMAException referenceError(Global global, Throwable cause, String msgId, String ... args2) {
        String msg = ECMAErrors.getMessage("reference.error." + msgId, args2);
        return ECMAErrors.error(global.newReferenceError(msg), cause);
    }

    public static ECMAException uriError(String msgId, String ... args2) {
        return ECMAErrors.uriError(Context.getGlobal(), msgId, args2);
    }

    public static ECMAException uriError(Global global, String msgId, String ... args2) {
        return ECMAErrors.uriError(global, null, msgId, args2);
    }

    public static ECMAException uriError(Throwable cause, String msgId, String ... args2) {
        return ECMAErrors.uriError(Context.getGlobal(), cause, msgId, args2);
    }

    public static ECMAException uriError(Global global, Throwable cause, String msgId, String ... args2) {
        String msg = ECMAErrors.getMessage("uri.error." + msgId, args2);
        return ECMAErrors.error(global.newURIError(msg), cause);
    }

    public static String getMessage(String msgId, String ... args2) {
        try {
            return new MessageFormat(MESSAGES_BUNDLE.getString(msgId)).format(args2);
        }
        catch (MissingResourceException e) {
            throw new RuntimeException("no message resource found for message id: " + msgId);
        }
    }

    public static boolean isScriptFrame(StackTraceElement frame) {
        String className = frame.getClassName();
        if (className.startsWith(scriptPackage) && !CompilerConstants.isInternalMethodName(frame.getMethodName())) {
            String source = frame.getFileName();
            return source != null && !source.endsWith(".java");
        }
        return false;
    }

    static {
        String name = JS.class.getName();
        scriptPackage = name.substring(0, name.lastIndexOf(46));
    }
}

