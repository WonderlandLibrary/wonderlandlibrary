/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.MethodEmitter;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Source;

public final class CompilerConstants
extends Enum<CompilerConstants> {
    public static final /* enum */ CompilerConstants __FILE__ = new CompilerConstants();
    public static final /* enum */ CompilerConstants __DIR__ = new CompilerConstants();
    public static final /* enum */ CompilerConstants __LINE__ = new CompilerConstants();
    public static final /* enum */ CompilerConstants INIT = new CompilerConstants("<init>");
    public static final /* enum */ CompilerConstants CLINIT = new CompilerConstants("<clinit>");
    public static final /* enum */ CompilerConstants EVAL = new CompilerConstants("eval");
    public static final /* enum */ CompilerConstants SOURCE = new CompilerConstants("source", Source.class);
    public static final /* enum */ CompilerConstants CONSTANTS = new CompilerConstants("constants", Object[].class);
    public static final /* enum */ CompilerConstants STRICT_MODE = new CompilerConstants("strictMode", Boolean.TYPE);
    public static final /* enum */ CompilerConstants DEFAULT_SCRIPT_NAME = new CompilerConstants("Script");
    public static final /* enum */ CompilerConstants ANON_FUNCTION_PREFIX = new CompilerConstants("L:");
    public static final /* enum */ CompilerConstants NESTED_FUNCTION_SEPARATOR = new CompilerConstants("#");
    public static final /* enum */ CompilerConstants ID_FUNCTION_SEPARATOR = new CompilerConstants("-");
    public static final /* enum */ CompilerConstants PROGRAM = new CompilerConstants(":program");
    public static final /* enum */ CompilerConstants CREATE_PROGRAM_FUNCTION = new CompilerConstants(":createProgramFunction");
    public static final /* enum */ CompilerConstants THIS = new CompilerConstants("this", Object.class);
    public static final /* enum */ CompilerConstants THIS_DEBUGGER = new CompilerConstants(":this");
    public static final /* enum */ CompilerConstants SCOPE = new CompilerConstants(":scope", ScriptObject.class, 2);
    public static final /* enum */ CompilerConstants RETURN = new CompilerConstants(":return");
    public static final /* enum */ CompilerConstants CALLEE = new CompilerConstants(":callee", ScriptFunction.class);
    public static final /* enum */ CompilerConstants VARARGS = new CompilerConstants(":varargs", Object[].class);
    public static final /* enum */ CompilerConstants ARGUMENTS_VAR = new CompilerConstants("arguments", Object.class);
    public static final /* enum */ CompilerConstants ARGUMENTS = new CompilerConstants(":arguments", ScriptObject.class);
    public static final /* enum */ CompilerConstants EXPLODED_ARGUMENT_PREFIX = new CompilerConstants(":xarg");
    public static final /* enum */ CompilerConstants ITERATOR_PREFIX = new CompilerConstants(":i", Iterator.class);
    public static final /* enum */ CompilerConstants SWITCH_TAG_PREFIX = new CompilerConstants(":s");
    public static final /* enum */ CompilerConstants EXCEPTION_PREFIX = new CompilerConstants(":e", Throwable.class);
    public static final /* enum */ CompilerConstants QUICK_PREFIX = new CompilerConstants(":q");
    public static final /* enum */ CompilerConstants TEMP_PREFIX = new CompilerConstants(":t");
    public static final /* enum */ CompilerConstants LITERAL_PREFIX = new CompilerConstants(":l");
    public static final /* enum */ CompilerConstants REGEX_PREFIX = new CompilerConstants(":r");
    public static final /* enum */ CompilerConstants JAVA_THIS = new CompilerConstants(null, 0);
    public static final /* enum */ CompilerConstants INIT_MAP = new CompilerConstants(null, 1);
    public static final /* enum */ CompilerConstants INIT_SCOPE = new CompilerConstants(null, 2);
    public static final /* enum */ CompilerConstants INIT_ARGUMENTS = new CompilerConstants(null, 3);
    public static final /* enum */ CompilerConstants JS_OBJECT_DUAL_FIELD_PREFIX = new CompilerConstants("JD");
    public static final /* enum */ CompilerConstants JS_OBJECT_SINGLE_FIELD_PREFIX = new CompilerConstants("JO");
    public static final /* enum */ CompilerConstants ALLOCATE = new CompilerConstants("allocate");
    public static final /* enum */ CompilerConstants SPLIT_PREFIX = new CompilerConstants(":split");
    public static final /* enum */ CompilerConstants SPLIT_ARRAY_ARG = new CompilerConstants(":split_array", 3);
    public static final /* enum */ CompilerConstants GET_STRING = new CompilerConstants(":getString");
    public static final /* enum */ CompilerConstants GET_MAP = new CompilerConstants(":getMap");
    public static final /* enum */ CompilerConstants SET_MAP = new CompilerConstants(":setMap");
    public static final /* enum */ CompilerConstants GET_ARRAY_PREFIX = new CompilerConstants(":get");
    public static final /* enum */ CompilerConstants GET_ARRAY_SUFFIX = new CompilerConstants("$array");
    private static Set<String> symbolNames;
    private static final String INTERNAL_METHOD_PREFIX = ":";
    private final String symbolName;
    private final Class<?> type;
    private final int slot;
    private static final /* synthetic */ CompilerConstants[] $VALUES;

    public static CompilerConstants[] values() {
        return (CompilerConstants[])$VALUES.clone();
    }

    public static CompilerConstants valueOf(String name) {
        return Enum.valueOf(CompilerConstants.class, name);
    }

    private CompilerConstants() {
        this.symbolName = this.name();
        this.type = null;
        this.slot = -1;
    }

    private CompilerConstants(String symbolName) {
        this(symbolName, -1);
    }

    private CompilerConstants(String symbolName, int slot) {
        this(symbolName, null, slot);
    }

    private CompilerConstants(String symbolName, Class<?> type) {
        this(symbolName, type, -1);
    }

    private CompilerConstants(String symbolName, Class<?> type, int slot) {
        this.symbolName = symbolName;
        this.type = type;
        this.slot = slot;
    }

    public static boolean isCompilerConstant(String name) {
        CompilerConstants.ensureSymbolNames();
        return symbolNames.contains(name);
    }

    private static void ensureSymbolNames() {
        if (symbolNames == null) {
            symbolNames = new HashSet<String>();
            for (CompilerConstants cc : CompilerConstants.values()) {
                symbolNames.add(cc.symbolName);
            }
        }
    }

    public final String symbolName() {
        return this.symbolName;
    }

    public final Class<?> type() {
        return this.type;
    }

    public final int slot() {
        return this.slot;
    }

    public final String descriptor() {
        assert (this.type != null) : " asking for descriptor of typeless constant";
        return CompilerConstants.typeDescriptor(this.type);
    }

    public static String className(Class<?> type) {
        return Type.getInternalName(type);
    }

    public static String methodDescriptor(Class<?> rtype, Class<?> ... ptypes) {
        return Type.getMethodDescriptor(rtype, ptypes);
    }

    public static String typeDescriptor(Class<?> clazz) {
        return Type.typeFor(clazz).getDescriptor();
    }

    public static Call constructorNoLookup(Class<?> clazz) {
        return CompilerConstants.specialCallNoLookup(clazz, INIT.symbolName(), Void.TYPE, new Class[0]);
    }

    public static Call constructorNoLookup(String className, Class<?> ... ptypes) {
        return CompilerConstants.specialCallNoLookup(className, INIT.symbolName(), CompilerConstants.methodDescriptor(Void.TYPE, ptypes));
    }

    public static Call constructorNoLookup(Class<?> clazz, Class<?> ... ptypes) {
        return CompilerConstants.specialCallNoLookup(clazz, INIT.symbolName(), Void.TYPE, ptypes);
    }

    public static Call specialCallNoLookup(String className, String name, final String desc) {
        return new Call(null, className, name, desc){

            @Override
            MethodEmitter invoke(MethodEmitter method) {
                return method.invokespecial(this.className, this.name, this.descriptor);
            }

            @Override
            public void invoke(MethodVisitor mv) {
                mv.visitMethodInsn(183, this.className, this.name, desc, false);
            }
        };
    }

    public static Call specialCallNoLookup(Class<?> clazz, String name, Class<?> rtype, Class<?> ... ptypes) {
        return CompilerConstants.specialCallNoLookup(CompilerConstants.className(clazz), name, CompilerConstants.methodDescriptor(rtype, ptypes));
    }

    public static Call staticCallNoLookup(String className, String name, final String desc) {
        return new Call(null, className, name, desc){

            @Override
            MethodEmitter invoke(MethodEmitter method) {
                return method.invokestatic(this.className, this.name, this.descriptor);
            }

            @Override
            public void invoke(MethodVisitor mv) {
                mv.visitMethodInsn(184, this.className, this.name, desc, false);
            }
        };
    }

    public static Call staticCallNoLookup(Class<?> clazz, String name, Class<?> rtype, Class<?> ... ptypes) {
        return CompilerConstants.staticCallNoLookup(CompilerConstants.className(clazz), name, CompilerConstants.methodDescriptor(rtype, ptypes));
    }

    public static Call virtualCallNoLookup(Class<?> clazz, String name, Class<?> rtype, Class<?> ... ptypes) {
        return new Call(null, CompilerConstants.className(clazz), name, CompilerConstants.methodDescriptor(rtype, ptypes)){

            @Override
            MethodEmitter invoke(MethodEmitter method) {
                return method.invokevirtual(this.className, this.name, this.descriptor);
            }

            @Override
            public void invoke(MethodVisitor mv) {
                mv.visitMethodInsn(182, this.className, this.name, this.descriptor, false);
            }
        };
    }

    public static Call interfaceCallNoLookup(Class<?> clazz, String name, Class<?> rtype, Class<?> ... ptypes) {
        return new Call(null, CompilerConstants.className(clazz), name, CompilerConstants.methodDescriptor(rtype, ptypes)){

            @Override
            MethodEmitter invoke(MethodEmitter method) {
                return method.invokeinterface(this.className, this.name, this.descriptor);
            }

            @Override
            public void invoke(MethodVisitor mv) {
                mv.visitMethodInsn(185, this.className, this.name, this.descriptor, true);
            }
        };
    }

    public static FieldAccess virtualField(String className, String name, String desc) {
        return new FieldAccess(className, name, desc){

            @Override
            public MethodEmitter get(MethodEmitter method) {
                return method.getField(this.className, this.name, this.descriptor);
            }

            @Override
            public void put(MethodEmitter method) {
                method.putField(this.className, this.name, this.descriptor);
            }
        };
    }

    public static FieldAccess virtualField(Class<?> clazz, String name, Class<?> type) {
        return CompilerConstants.virtualField(CompilerConstants.className(clazz), name, CompilerConstants.typeDescriptor(type));
    }

    public static FieldAccess staticField(String className, String name, String desc) {
        return new FieldAccess(className, name, desc){

            @Override
            public MethodEmitter get(MethodEmitter method) {
                return method.getStatic(this.className, this.name, this.descriptor);
            }

            @Override
            public void put(MethodEmitter method) {
                method.putStatic(this.className, this.name, this.descriptor);
            }
        };
    }

    public static FieldAccess staticField(Class<?> clazz, String name, Class<?> type) {
        return CompilerConstants.staticField(CompilerConstants.className(clazz), name, CompilerConstants.typeDescriptor(type));
    }

    public static Call staticCall(MethodHandles.Lookup lookup, Class<?> clazz, String name, Class<?> rtype, Class<?> ... ptypes) {
        return new Call(Lookup.MH.findStatic(lookup, clazz, name, Lookup.MH.type(rtype, ptypes)), CompilerConstants.className(clazz), name, CompilerConstants.methodDescriptor(rtype, ptypes)){

            @Override
            MethodEmitter invoke(MethodEmitter method) {
                return method.invokestatic(this.className, this.name, this.descriptor);
            }

            @Override
            public void invoke(MethodVisitor mv) {
                mv.visitMethodInsn(184, this.className, this.name, this.descriptor, false);
            }
        };
    }

    public static Call virtualCall(MethodHandles.Lookup lookup, Class<?> clazz, String name, Class<?> rtype, Class<?> ... ptypes) {
        return new Call(Lookup.MH.findVirtual(lookup, clazz, name, Lookup.MH.type(rtype, ptypes)), CompilerConstants.className(clazz), name, CompilerConstants.methodDescriptor(rtype, ptypes)){

            @Override
            MethodEmitter invoke(MethodEmitter method) {
                return method.invokevirtual(this.className, this.name, this.descriptor);
            }

            @Override
            public void invoke(MethodVisitor mv) {
                mv.visitMethodInsn(182, this.className, this.name, this.descriptor, false);
            }
        };
    }

    public static Call specialCall(MethodHandles.Lookup lookup, Class<?> clazz, String name, Class<?> rtype, Class<?> ... ptypes) {
        return new Call(Lookup.MH.findSpecial(lookup, clazz, name, Lookup.MH.type(rtype, ptypes), clazz), CompilerConstants.className(clazz), name, CompilerConstants.methodDescriptor(rtype, ptypes)){

            @Override
            MethodEmitter invoke(MethodEmitter method) {
                return method.invokespecial(this.className, this.name, this.descriptor);
            }

            @Override
            public void invoke(MethodVisitor mv) {
                mv.visitMethodInsn(183, this.className, this.name, this.descriptor, false);
            }
        };
    }

    public static boolean isInternalMethodName(String methodName) {
        return methodName.startsWith(INTERNAL_METHOD_PREFIX) && !methodName.equals(CompilerConstants.PROGRAM.symbolName);
    }

    static {
        $VALUES = new CompilerConstants[]{__FILE__, __DIR__, __LINE__, INIT, CLINIT, EVAL, SOURCE, CONSTANTS, STRICT_MODE, DEFAULT_SCRIPT_NAME, ANON_FUNCTION_PREFIX, NESTED_FUNCTION_SEPARATOR, ID_FUNCTION_SEPARATOR, PROGRAM, CREATE_PROGRAM_FUNCTION, THIS, THIS_DEBUGGER, SCOPE, RETURN, CALLEE, VARARGS, ARGUMENTS_VAR, ARGUMENTS, EXPLODED_ARGUMENT_PREFIX, ITERATOR_PREFIX, SWITCH_TAG_PREFIX, EXCEPTION_PREFIX, QUICK_PREFIX, TEMP_PREFIX, LITERAL_PREFIX, REGEX_PREFIX, JAVA_THIS, INIT_MAP, INIT_SCOPE, INIT_ARGUMENTS, JS_OBJECT_DUAL_FIELD_PREFIX, JS_OBJECT_SINGLE_FIELD_PREFIX, ALLOCATE, SPLIT_PREFIX, SPLIT_ARRAY_ARG, GET_STRING, GET_MAP, SET_MAP, GET_ARRAY_PREFIX, GET_ARRAY_SUFFIX};
        for (CompilerConstants c : CompilerConstants.values()) {
            String symbolName = c.symbolName();
            if (symbolName == null) continue;
            symbolName.intern();
        }
    }

    public static abstract class Call
    extends Access {
        protected Call(String className, String name, String descriptor) {
            super(null, className, name, descriptor);
        }

        protected Call(MethodHandle methodHandle, String className, String name, String descriptor) {
            super(methodHandle, className, name, descriptor);
        }

        abstract MethodEmitter invoke(MethodEmitter var1);

        public abstract void invoke(MethodVisitor var1);
    }

    public static abstract class FieldAccess
    extends Access {
        protected FieldAccess(String className, String name, String descriptor) {
            super(null, className, name, descriptor);
        }

        protected abstract MethodEmitter get(MethodEmitter var1);

        protected abstract void put(MethodEmitter var1);
    }

    private static abstract class Access {
        protected final MethodHandle methodHandle;
        protected final String className;
        protected final String name;
        protected final String descriptor;

        protected Access(MethodHandle methodHandle, String className, String name, String descriptor) {
            this.methodHandle = methodHandle;
            this.className = className;
            this.name = name;
            this.descriptor = descriptor;
        }

        public MethodHandle methodHandle() {
            return this.methodHandle;
        }

        public String className() {
            return this.className;
        }

        public String name() {
            return this.name;
        }

        public String descriptor() {
            return this.descriptor;
        }
    }
}

