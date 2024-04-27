/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.lookup;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.SwitchPoint;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import jdk.nashorn.internal.lookup.MethodHandleFunctionality;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.Debug;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import jdk.nashorn.internal.runtime.logging.Loggable;
import jdk.nashorn.internal.runtime.logging.Logger;
import jdk.nashorn.internal.runtime.options.Options;

public final class MethodHandleFactory {
    private static final MethodHandles.Lookup PUBLIC_LOOKUP = MethodHandles.publicLookup();
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final Level TRACE_LEVEL = Level.INFO;
    private static final MethodHandleFunctionality FUNC = new StandardMethodHandleFunctionality();
    private static final boolean PRINT_STACKTRACE = Options.getBooleanProperty("nashorn.methodhandles.debug.stacktrace");
    private static final MethodHandle TRACE = FUNC.findStatic(LOOKUP, MethodHandleFactory.class, "traceArgs", MethodType.methodType(Void.TYPE, DebugLogger.class, String.class, Integer.TYPE, Object[].class));
    private static final MethodHandle TRACE_RETURN = FUNC.findStatic(LOOKUP, MethodHandleFactory.class, "traceReturn", MethodType.methodType(Object.class, DebugLogger.class, Object.class));
    private static final MethodHandle TRACE_RETURN_VOID = FUNC.findStatic(LOOKUP, MethodHandleFactory.class, "traceReturnVoid", MethodType.methodType(Void.TYPE, DebugLogger.class));
    private static final String VOID_TAG = "[VOID]";

    private MethodHandleFactory() {
    }

    public static String stripName(Object obj) {
        if (obj == null) {
            return "null";
        }
        if (obj instanceof Class) {
            return ((Class)obj).getSimpleName();
        }
        return obj.toString();
    }

    public static MethodHandleFunctionality getFunctionality() {
        return FUNC;
    }

    private static void err(String str) {
        Context.getContext().getErr().println(str);
    }

    static Object traceReturn(DebugLogger logger, Object value) {
        String str = "    return" + (VOID_TAG.equals(value) ? ";" : " " + MethodHandleFactory.stripName(value) + "; // [type=" + (value == null ? "null]" : MethodHandleFactory.stripName(value.getClass()) + ']'));
        if (logger == null) {
            MethodHandleFactory.err(str);
        } else if (logger.isEnabled()) {
            logger.log(TRACE_LEVEL, str);
        }
        return value;
    }

    static void traceReturnVoid(DebugLogger logger) {
        MethodHandleFactory.traceReturn(logger, VOID_TAG);
    }

    static void traceArgs(DebugLogger logger, String tag, int paramStart, Object ... args2) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag);
        for (int i = paramStart; i < args2.length; ++i) {
            if (i == paramStart) {
                sb.append(" => args: ");
            }
            sb.append('\'').append(MethodHandleFactory.stripName(MethodHandleFactory.argString(args2[i]))).append('\'').append(' ').append('[').append("type=").append(args2[i] == null ? "null" : MethodHandleFactory.stripName(args2[i].getClass())).append(']');
            if (i + 1 >= args2.length) continue;
            sb.append(", ");
        }
        if (logger == null) {
            MethodHandleFactory.err(sb.toString());
        } else {
            logger.log(TRACE_LEVEL, sb);
        }
        MethodHandleFactory.stacktrace(logger);
    }

    private static void stacktrace(DebugLogger logger) {
        if (!PRINT_STACKTRACE) {
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        new Throwable().printStackTrace(ps);
        String st = baos.toString();
        if (logger == null) {
            MethodHandleFactory.err(st);
        } else {
            logger.log(TRACE_LEVEL, st);
        }
    }

    private static String argString(Object arg) {
        if (arg == null) {
            return "null";
        }
        if (arg.getClass().isArray()) {
            ArrayList<String> list = new ArrayList<String>();
            for (Object elem : (Object[])arg) {
                list.add('\'' + MethodHandleFactory.argString(elem) + '\'');
            }
            return ((Object)list).toString();
        }
        if (arg instanceof ScriptObject) {
            return arg.toString() + " (map=" + Debug.id(((ScriptObject)arg).getMap()) + ')';
        }
        return arg.toString();
    }

    public static MethodHandle addDebugPrintout(MethodHandle mh, Object tag) {
        return MethodHandleFactory.addDebugPrintout(null, Level.OFF, mh, 0, true, tag);
    }

    public static MethodHandle addDebugPrintout(DebugLogger logger, Level level, MethodHandle mh, Object tag) {
        return MethodHandleFactory.addDebugPrintout(logger, level, mh, 0, true, tag);
    }

    public static MethodHandle addDebugPrintout(MethodHandle mh, int paramStart, boolean printReturnValue, Object tag) {
        return MethodHandleFactory.addDebugPrintout(null, Level.OFF, mh, paramStart, printReturnValue, tag);
    }

    public static MethodHandle addDebugPrintout(DebugLogger logger, Level level, MethodHandle mh, int paramStart, boolean printReturnValue, Object tag) {
        MethodType type = mh.type();
        if (logger == null || !logger.isLoggable(level)) {
            return mh;
        }
        assert (TRACE != null);
        MethodHandle trace = MethodHandles.insertArguments(TRACE, 0, logger, tag, paramStart);
        trace = MethodHandles.foldArguments(mh, trace.asCollector(Object[].class, type.parameterCount()).asType(type.changeReturnType(Void.TYPE)));
        Class<?> retType = type.returnType();
        if (printReturnValue) {
            if (retType != Void.TYPE) {
                MethodHandle traceReturn = MethodHandles.insertArguments(TRACE_RETURN, 0, logger);
                trace = MethodHandles.filterReturnValue(trace, traceReturn.asType(traceReturn.type().changeParameterType(0, retType).changeReturnType(retType)));
            } else {
                trace = MethodHandles.filterReturnValue(trace, MethodHandles.insertArguments(TRACE_RETURN_VOID, 0, logger));
            }
        }
        return trace;
    }

    @Logger(name="methodhandles")
    private static class StandardMethodHandleFunctionality
    implements MethodHandleFunctionality,
    Loggable {
        private DebugLogger log = DebugLogger.DISABLED_LOGGER;

        @Override
        public DebugLogger initLogger(Context context) {
            this.log = context.getLogger(this.getClass());
            return this.log;
        }

        @Override
        public DebugLogger getLogger() {
            return this.log;
        }

        protected static String describe(Object ... data) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; ++i) {
                Object d = data[i];
                if (d == null) {
                    sb.append("<null> ");
                } else if (JSType.isString(d)) {
                    sb.append(d.toString());
                    sb.append(' ');
                } else if (d.getClass().isArray()) {
                    sb.append("[ ");
                    for (Object da : (Object[])d) {
                        sb.append(StandardMethodHandleFunctionality.describe(da)).append(' ');
                    }
                    sb.append("] ");
                } else {
                    sb.append(d).append('{').append(Integer.toHexString(System.identityHashCode(d))).append('}');
                }
                if (i + 1 >= data.length) continue;
                sb.append(", ");
            }
            return sb.toString();
        }

        public MethodHandle debug(MethodHandle master, String str, Object ... args2) {
            if (this.log.isEnabled()) {
                if (PRINT_STACKTRACE) {
                    MethodHandleFactory.stacktrace(this.log);
                }
                return MethodHandleFactory.addDebugPrintout(this.log, Level.INFO, master, Integer.MAX_VALUE, false, str + ' ' + StandardMethodHandleFunctionality.describe(args2));
            }
            return master;
        }

        @Override
        public MethodHandle filterArguments(MethodHandle target, int pos, MethodHandle ... filters) {
            MethodHandle mh = MethodHandles.filterArguments(target, pos, filters);
            return this.debug(mh, "filterArguments", target, pos, filters);
        }

        @Override
        public MethodHandle filterReturnValue(MethodHandle target, MethodHandle filter) {
            MethodHandle mh = MethodHandles.filterReturnValue(target, filter);
            return this.debug(mh, "filterReturnValue", target, filter);
        }

        @Override
        public MethodHandle guardWithTest(MethodHandle test, MethodHandle target, MethodHandle fallback) {
            MethodHandle mh = MethodHandles.guardWithTest(test, target, fallback);
            return this.debug(mh, "guardWithTest", test, target, fallback);
        }

        @Override
        public MethodHandle insertArguments(MethodHandle target, int pos, Object ... values2) {
            MethodHandle mh = MethodHandles.insertArguments(target, pos, values2);
            return this.debug(mh, "insertArguments", target, pos, values2);
        }

        @Override
        public MethodHandle dropArguments(MethodHandle target, int pos, Class<?> ... values2) {
            MethodHandle mh = MethodHandles.dropArguments(target, pos, values2);
            return this.debug(mh, "dropArguments", target, pos, values2);
        }

        @Override
        public MethodHandle dropArguments(MethodHandle target, int pos, List<Class<?>> values2) {
            MethodHandle mh = MethodHandles.dropArguments(target, pos, values2);
            return this.debug(mh, "dropArguments", target, pos, values2);
        }

        @Override
        public MethodHandle asType(MethodHandle handle, MethodType type) {
            MethodHandle mh = handle.asType(type);
            return this.debug(mh, "asType", handle, type);
        }

        @Override
        public MethodHandle bindTo(MethodHandle handle, Object x) {
            MethodHandle mh = handle.bindTo(x);
            return this.debug(mh, "bindTo", handle, x);
        }

        @Override
        public MethodHandle foldArguments(MethodHandle target, MethodHandle combiner) {
            MethodHandle mh = MethodHandles.foldArguments(target, combiner);
            return this.debug(mh, "foldArguments", target, combiner);
        }

        @Override
        public MethodHandle explicitCastArguments(MethodHandle target, MethodType type) {
            MethodHandle mh = MethodHandles.explicitCastArguments(target, type);
            return this.debug(mh, "explicitCastArguments", target, type);
        }

        @Override
        public MethodHandle arrayElementGetter(Class<?> type) {
            MethodHandle mh = MethodHandles.arrayElementGetter(type);
            return this.debug(mh, "arrayElementGetter", type);
        }

        @Override
        public MethodHandle arrayElementSetter(Class<?> type) {
            MethodHandle mh = MethodHandles.arrayElementSetter(type);
            return this.debug(mh, "arrayElementSetter", type);
        }

        @Override
        public MethodHandle throwException(Class<?> returnType, Class<? extends Throwable> exType) {
            MethodHandle mh = MethodHandles.throwException(returnType, exType);
            return this.debug(mh, "throwException", returnType, exType);
        }

        @Override
        public MethodHandle catchException(MethodHandle target, Class<? extends Throwable> exType, MethodHandle handler) {
            MethodHandle mh = MethodHandles.catchException(target, exType, handler);
            return this.debug(mh, "catchException", exType);
        }

        @Override
        public MethodHandle constant(Class<?> type, Object value) {
            MethodHandle mh = MethodHandles.constant(type, value);
            return this.debug(mh, "constant", type, value);
        }

        @Override
        public MethodHandle identity(Class<?> type) {
            MethodHandle mh = MethodHandles.identity(type);
            return this.debug(mh, "identity", type);
        }

        @Override
        public MethodHandle asCollector(MethodHandle handle, Class<?> arrayType, int arrayLength) {
            MethodHandle mh = handle.asCollector(arrayType, arrayLength);
            return this.debug(mh, "asCollector", handle, arrayType, arrayLength);
        }

        @Override
        public MethodHandle asSpreader(MethodHandle handle, Class<?> arrayType, int arrayLength) {
            MethodHandle mh = handle.asSpreader(arrayType, arrayLength);
            return this.debug(mh, "asSpreader", handle, arrayType, arrayLength);
        }

        @Override
        public MethodHandle getter(MethodHandles.Lookup explicitLookup, Class<?> clazz, String name, Class<?> type) {
            try {
                MethodHandle mh = explicitLookup.findGetter(clazz, name, type);
                return this.debug(mh, "getter", explicitLookup, clazz, name, type);
            }
            catch (IllegalAccessException | NoSuchFieldException e) {
                throw new LookupException(e);
            }
        }

        @Override
        public MethodHandle staticGetter(MethodHandles.Lookup explicitLookup, Class<?> clazz, String name, Class<?> type) {
            try {
                MethodHandle mh = explicitLookup.findStaticGetter(clazz, name, type);
                return this.debug(mh, "static getter", explicitLookup, clazz, name, type);
            }
            catch (IllegalAccessException | NoSuchFieldException e) {
                throw new LookupException(e);
            }
        }

        @Override
        public MethodHandle setter(MethodHandles.Lookup explicitLookup, Class<?> clazz, String name, Class<?> type) {
            try {
                MethodHandle mh = explicitLookup.findSetter(clazz, name, type);
                return this.debug(mh, "setter", explicitLookup, clazz, name, type);
            }
            catch (IllegalAccessException | NoSuchFieldException e) {
                throw new LookupException(e);
            }
        }

        @Override
        public MethodHandle staticSetter(MethodHandles.Lookup explicitLookup, Class<?> clazz, String name, Class<?> type) {
            try {
                MethodHandle mh = explicitLookup.findStaticSetter(clazz, name, type);
                return this.debug(mh, "static setter", explicitLookup, clazz, name, type);
            }
            catch (IllegalAccessException | NoSuchFieldException e) {
                throw new LookupException(e);
            }
        }

        @Override
        public MethodHandle find(Method method) {
            try {
                MethodHandle mh = PUBLIC_LOOKUP.unreflect(method);
                return this.debug(mh, "find", method);
            }
            catch (IllegalAccessException e) {
                throw new LookupException(e);
            }
        }

        @Override
        public MethodHandle findStatic(MethodHandles.Lookup explicitLookup, Class<?> clazz, String name, MethodType type) {
            try {
                MethodHandle mh = explicitLookup.findStatic(clazz, name, type);
                return this.debug(mh, "findStatic", explicitLookup, clazz, name, type);
            }
            catch (IllegalAccessException | NoSuchMethodException e) {
                throw new LookupException(e);
            }
        }

        @Override
        public MethodHandle findSpecial(MethodHandles.Lookup explicitLookup, Class<?> clazz, String name, MethodType type, Class<?> thisClass) {
            try {
                MethodHandle mh = explicitLookup.findSpecial(clazz, name, type, thisClass);
                return this.debug(mh, "findSpecial", explicitLookup, clazz, name, type);
            }
            catch (IllegalAccessException | NoSuchMethodException e) {
                throw new LookupException(e);
            }
        }

        @Override
        public MethodHandle findVirtual(MethodHandles.Lookup explicitLookup, Class<?> clazz, String name, MethodType type) {
            try {
                MethodHandle mh = explicitLookup.findVirtual(clazz, name, type);
                return this.debug(mh, "findVirtual", explicitLookup, clazz, name, type);
            }
            catch (IllegalAccessException | NoSuchMethodException e) {
                throw new LookupException(e);
            }
        }

        @Override
        public SwitchPoint createSwitchPoint() {
            SwitchPoint sp = new SwitchPoint();
            this.log.log(TRACE_LEVEL, "createSwitchPoint ", sp);
            return sp;
        }

        @Override
        public MethodHandle guardWithTest(SwitchPoint sp, MethodHandle before, MethodHandle after) {
            MethodHandle mh = sp.guardWithTest(before, after);
            return this.debug(mh, "guardWithTest", sp, before, after);
        }

        @Override
        public MethodType type(Class<?> returnType, Class<?> ... paramTypes) {
            MethodType mt = MethodType.methodType(returnType, paramTypes);
            this.log.log(TRACE_LEVEL, "methodType ", returnType, " ", Arrays.toString(paramTypes), " ", mt);
            return mt;
        }
    }

    public static class LookupException
    extends RuntimeException {
        public LookupException(Exception e) {
            super(e);
        }
    }
}

