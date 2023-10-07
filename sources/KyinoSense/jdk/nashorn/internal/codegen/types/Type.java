/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.codegen.types;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.nashorn.internal.codegen.CompilerConstants;
import jdk.nashorn.internal.codegen.types.ArrayType;
import jdk.nashorn.internal.codegen.types.BitwiseType;
import jdk.nashorn.internal.codegen.types.BooleanType;
import jdk.nashorn.internal.codegen.types.BytecodeOps;
import jdk.nashorn.internal.codegen.types.IntType;
import jdk.nashorn.internal.codegen.types.LongType;
import jdk.nashorn.internal.codegen.types.NumberType;
import jdk.nashorn.internal.codegen.types.NumericType;
import jdk.nashorn.internal.codegen.types.ObjectType;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.Undefined;
import jdk.nashorn.internal.runtime.linker.Bootstrap;

public abstract class Type
implements Comparable<Type>,
BytecodeOps,
Serializable {
    private static final long serialVersionUID = 1L;
    private final transient String name;
    private final transient String descriptor;
    private final transient int weight;
    private final transient int slots;
    private final Class<?> clazz;
    private static final Map<Class<?>, jdk.internal.org.objectweb.asm.Type> INTERNAL_TYPE_CACHE = Collections.synchronizedMap(new WeakHashMap());
    private final transient jdk.internal.org.objectweb.asm.Type internalType;
    protected static final int MIN_WEIGHT = -1;
    protected static final int MAX_WEIGHT = 20;
    static final CompilerConstants.Call BOOTSTRAP = CompilerConstants.staticCallNoLookup(Bootstrap.class, "mathBootstrap", CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, Integer.TYPE);
    static final Handle MATHBOOTSTRAP = new Handle(6, BOOTSTRAP.className(), "mathBootstrap", BOOTSTRAP.descriptor());
    private static final ConcurrentMap<Class<?>, Type> cache = new ConcurrentHashMap();
    public static final Type BOOLEAN = Type.putInCache(new BooleanType());
    public static final BitwiseType INT = Type.putInCache(new IntType());
    public static final NumericType NUMBER = Type.putInCache(new NumberType());
    public static final Type LONG = Type.putInCache(new LongType());
    public static final Type STRING = Type.putInCache(new ObjectType(String.class));
    public static final Type CHARSEQUENCE = Type.putInCache(new ObjectType(CharSequence.class));
    public static final Type OBJECT = Type.putInCache(new ObjectType());
    public static final Type UNDEFINED = Type.putInCache(new ObjectType(Undefined.class));
    public static final Type SCRIPT_OBJECT = Type.putInCache(new ObjectType(ScriptObject.class));
    public static final ArrayType INT_ARRAY = Type.putInCache(new ArrayType((Class)int[].class){
        private static final long serialVersionUID = 1L;

        @Override
        public void astore(MethodVisitor method) {
            method.visitInsn(79);
        }

        @Override
        public Type aload(MethodVisitor method) {
            method.visitInsn(46);
            return INT;
        }

        @Override
        public Type newarray(MethodVisitor method) {
            method.visitIntInsn(188, 10);
            return this;
        }

        @Override
        public Type getElementType() {
            return INT;
        }
    });
    public static final ArrayType LONG_ARRAY = Type.putInCache(new ArrayType((Class)long[].class){
        private static final long serialVersionUID = 1L;

        @Override
        public void astore(MethodVisitor method) {
            method.visitInsn(80);
        }

        @Override
        public Type aload(MethodVisitor method) {
            method.visitInsn(47);
            return LONG;
        }

        @Override
        public Type newarray(MethodVisitor method) {
            method.visitIntInsn(188, 11);
            return this;
        }

        @Override
        public Type getElementType() {
            return LONG;
        }
    });
    public static final ArrayType NUMBER_ARRAY = Type.putInCache(new ArrayType((Class)double[].class){
        private static final long serialVersionUID = 1L;

        @Override
        public void astore(MethodVisitor method) {
            method.visitInsn(82);
        }

        @Override
        public Type aload(MethodVisitor method) {
            method.visitInsn(49);
            return NUMBER;
        }

        @Override
        public Type newarray(MethodVisitor method) {
            method.visitIntInsn(188, 7);
            return this;
        }

        @Override
        public Type getElementType() {
            return NUMBER;
        }
    });
    public static final ArrayType OBJECT_ARRAY = Type.putInCache(new ArrayType(Object[].class));
    public static final Type THIS = new ObjectType(){
        private static final long serialVersionUID = 1L;

        @Override
        public String toString() {
            return "this";
        }
    };
    public static final Type SCOPE = new ObjectType(){
        private static final long serialVersionUID = 1L;

        @Override
        public String toString() {
            return "scope";
        }
    };
    public static final Type UNKNOWN = new ValueLessType("<unknown>"){
        private static final long serialVersionUID = 1L;

        @Override
        public String getDescriptor() {
            return "<unknown>";
        }

        @Override
        public char getBytecodeStackType() {
            return 'U';
        }
    };
    public static final Type SLOT_2 = new ValueLessType("<slot_2>"){
        private static final long serialVersionUID = 1L;

        @Override
        public String getDescriptor() {
            return "<slot_2>";
        }

        @Override
        public char getBytecodeStackType() {
            throw new UnsupportedOperationException("getBytecodeStackType");
        }
    };

    Type(String name, Class<?> clazz, int weight, int slots) {
        this.name = name;
        this.clazz = clazz;
        this.descriptor = jdk.internal.org.objectweb.asm.Type.getDescriptor(clazz);
        this.weight = weight;
        assert (weight >= -1 && weight <= 20) : "illegal type weight: " + weight;
        this.slots = slots;
        this.internalType = Type.getInternalType(clazz);
    }

    public int getWeight() {
        return this.weight;
    }

    public Class<?> getTypeClass() {
        return this.clazz;
    }

    public Type nextWider() {
        return null;
    }

    public Class<?> getBoxedType() {
        assert (!this.getTypeClass().isPrimitive());
        return null;
    }

    public abstract char getBytecodeStackType();

    public static String getMethodDescriptor(Type returnType, Type ... types) {
        jdk.internal.org.objectweb.asm.Type[] itypes = new jdk.internal.org.objectweb.asm.Type[types.length];
        for (int i = 0; i < types.length; ++i) {
            itypes[i] = types[i].getInternalType();
        }
        return jdk.internal.org.objectweb.asm.Type.getMethodDescriptor(returnType.getInternalType(), itypes);
    }

    public static String getMethodDescriptor(Class<?> returnType, Class<?> ... types) {
        jdk.internal.org.objectweb.asm.Type[] itypes = new jdk.internal.org.objectweb.asm.Type[types.length];
        for (int i = 0; i < types.length; ++i) {
            itypes[i] = Type.getInternalType(types[i]);
        }
        return jdk.internal.org.objectweb.asm.Type.getMethodDescriptor(Type.getInternalType(returnType), itypes);
    }

    public static char getShortSignatureDescriptor(Type type) {
        if (type instanceof BooleanType) {
            return 'Z';
        }
        return type.getBytecodeStackType();
    }

    static Type typeFor(jdk.internal.org.objectweb.asm.Type itype) {
        switch (itype.getSort()) {
            case 1: {
                return BOOLEAN;
            }
            case 5: {
                return INT;
            }
            case 7: {
                return LONG;
            }
            case 8: {
                return NUMBER;
            }
            case 10: {
                if (Context.isStructureClass(itype.getClassName())) {
                    return SCRIPT_OBJECT;
                }
                try {
                    return Type.typeFor(Class.forName(itype.getClassName()));
                }
                catch (ClassNotFoundException e) {
                    throw new AssertionError((Object)e);
                }
            }
            case 0: {
                return null;
            }
            case 9: {
                switch (itype.getElementType().getSort()) {
                    case 8: {
                        return NUMBER_ARRAY;
                    }
                    case 5: {
                        return INT_ARRAY;
                    }
                    case 7: {
                        return LONG_ARRAY;
                    }
                    default: {
                        assert (false);
                        break;
                    }
                    case 10: 
                }
                return OBJECT_ARRAY;
            }
        }
        assert (false) : "Unknown itype : " + itype + " sort " + itype.getSort();
        return null;
    }

    public static Type getMethodReturnType(String methodDescriptor) {
        return Type.typeFor(jdk.internal.org.objectweb.asm.Type.getReturnType(methodDescriptor));
    }

    public static Type[] getMethodArguments(String methodDescriptor) {
        jdk.internal.org.objectweb.asm.Type[] itypes = jdk.internal.org.objectweb.asm.Type.getArgumentTypes(methodDescriptor);
        Type[] types = new Type[itypes.length];
        for (int i = 0; i < itypes.length; ++i) {
            types[i] = Type.typeFor(itypes[i]);
        }
        return types;
    }

    public static void writeTypeMap(Map<Integer, Type> typeMap, DataOutput output) throws IOException {
        if (typeMap == null) {
            output.writeInt(0);
        } else {
            output.writeInt(typeMap.size());
            for (Map.Entry<Integer, Type> e : typeMap.entrySet()) {
                int typeChar;
                output.writeInt(e.getKey());
                Type type = e.getValue();
                if (type == OBJECT) {
                    typeChar = 76;
                } else if (type == NUMBER) {
                    typeChar = 68;
                } else if (type == LONG) {
                    typeChar = 74;
                } else {
                    throw new AssertionError();
                }
                output.writeByte(typeChar);
            }
        }
    }

    public static Map<Integer, Type> readTypeMap(DataInput input) throws IOException {
        int size = input.readInt();
        if (size <= 0) {
            return null;
        }
        TreeMap<Integer, Type> map = new TreeMap<Integer, Type>();
        block5: for (int i = 0; i < size; ++i) {
            Type type;
            int pp = input.readInt();
            byte typeChar = input.readByte();
            switch (typeChar) {
                case 76: {
                    type = OBJECT;
                    break;
                }
                case 68: {
                    type = NUMBER;
                    break;
                }
                case 74: {
                    type = LONG;
                    break;
                }
                default: {
                    continue block5;
                }
            }
            map.put(pp, type);
        }
        return map;
    }

    static jdk.internal.org.objectweb.asm.Type getInternalType(String className) {
        return jdk.internal.org.objectweb.asm.Type.getType(className);
    }

    private jdk.internal.org.objectweb.asm.Type getInternalType() {
        return this.internalType;
    }

    private static jdk.internal.org.objectweb.asm.Type lookupInternalType(Class<?> type) {
        Map<Class<?>, jdk.internal.org.objectweb.asm.Type> c = INTERNAL_TYPE_CACHE;
        jdk.internal.org.objectweb.asm.Type itype = c.get(type);
        if (itype != null) {
            return itype;
        }
        itype = jdk.internal.org.objectweb.asm.Type.getType(type);
        c.put(type, itype);
        return itype;
    }

    private static jdk.internal.org.objectweb.asm.Type getInternalType(Class<?> type) {
        return Type.lookupInternalType(type);
    }

    static void invokestatic(MethodVisitor method, CompilerConstants.Call call) {
        method.visitMethodInsn(184, call.className(), call.name(), call.descriptor(), false);
    }

    public String getInternalName() {
        return jdk.internal.org.objectweb.asm.Type.getInternalName(this.getTypeClass());
    }

    public static String getInternalName(Class<?> clazz) {
        return jdk.internal.org.objectweb.asm.Type.getInternalName(clazz);
    }

    public boolean isUnknown() {
        return this.equals(UNKNOWN);
    }

    public boolean isJSPrimitive() {
        return !this.isObject() || this.isString();
    }

    public boolean isBoolean() {
        return this.equals(BOOLEAN);
    }

    public boolean isInteger() {
        return this.equals(INT);
    }

    public boolean isLong() {
        return this.equals(LONG);
    }

    public boolean isNumber() {
        return this.equals(NUMBER);
    }

    public boolean isNumeric() {
        return this instanceof NumericType;
    }

    public boolean isArray() {
        return this instanceof ArrayType;
    }

    public boolean isCategory2() {
        return this.getSlots() == 2;
    }

    public boolean isObject() {
        return this instanceof ObjectType;
    }

    public boolean isPrimitive() {
        return !this.isObject();
    }

    public boolean isString() {
        return this.equals(STRING);
    }

    public boolean isCharSequence() {
        return this.equals(CHARSEQUENCE);
    }

    public boolean isEquivalentTo(Type type) {
        return this.weight() == type.weight() || this.isObject() && type.isObject();
    }

    public static boolean isAssignableFrom(Type type0, Type type1) {
        if (type0.isObject() && type1.isObject()) {
            return type0.weight() >= type1.weight();
        }
        return type0.weight() == type1.weight();
    }

    public boolean isAssignableFrom(Type type) {
        return Type.isAssignableFrom(this, type);
    }

    public static boolean areEquivalent(Type type0, Type type1) {
        return type0.isEquivalentTo(type1);
    }

    public int getSlots() {
        return this.slots;
    }

    public static Type widest(Type type0, Type type1) {
        if (type0.isArray() && type1.isArray()) {
            return ((ArrayType)type0).getElementType() == ((ArrayType)type1).getElementType() ? type0 : OBJECT;
        }
        if (type0.isArray() != type1.isArray()) {
            return OBJECT;
        }
        if (type0.isObject() && type1.isObject() && type0.getTypeClass() != type1.getTypeClass()) {
            return OBJECT;
        }
        return type0.weight() > type1.weight() ? type0 : type1;
    }

    public static Class<?> widest(Class<?> type0, Class<?> type1) {
        return Type.widest(Type.typeFor(type0), Type.typeFor(type1)).getTypeClass();
    }

    public static Type widestReturnType(Type t1, Type t2) {
        if (t1.isUnknown()) {
            return t2;
        }
        if (t2.isUnknown()) {
            return t1;
        }
        if (t1.isBoolean() != t2.isBoolean() || t1.isNumeric() != t2.isNumeric()) {
            return OBJECT;
        }
        return Type.widest(t1, t2);
    }

    public static Type generic(Type type) {
        return type.isObject() ? OBJECT : type;
    }

    public static Type narrowest(Type type0, Type type1) {
        return type0.narrowerThan(type1) ? type0 : type1;
    }

    public boolean narrowerThan(Type type) {
        return this.weight() < type.weight();
    }

    public boolean widerThan(Type type) {
        return this.weight() > type.weight();
    }

    public static Type widest(Type type0, Type type1, Type limit) {
        Type type = Type.widest(type0, type1);
        if (type.weight() > limit.weight()) {
            return limit;
        }
        return type;
    }

    public static Type narrowest(Type type0, Type type1, Type limit) {
        Type type;
        Type type2 = type = type0.weight() < type1.weight() ? type0 : type1;
        if (type.weight() < limit.weight()) {
            return limit;
        }
        return type;
    }

    public Type narrowest(Type other) {
        return Type.narrowest(this, other);
    }

    public Type widest(Type other) {
        return Type.widest(this, other);
    }

    int weight() {
        return this.weight;
    }

    public String getDescriptor() {
        return this.descriptor;
    }

    public String getShortDescriptor() {
        return this.descriptor;
    }

    public String toString() {
        return this.name;
    }

    public static Type typeFor(Class<?> clazz) {
        Type type = (Type)cache.get(clazz);
        if (type != null) {
            return type;
        }
        assert (!clazz.isPrimitive() || clazz == Void.TYPE);
        ObjectType newType = clazz.isArray() ? new ArrayType(clazz) : new ObjectType(clazz);
        Type existingType = cache.putIfAbsent(clazz, newType);
        return existingType == null ? newType : existingType;
    }

    @Override
    public int compareTo(Type o) {
        return o.weight() - this.weight();
    }

    @Override
    public Type dup(MethodVisitor method, int depth) {
        return Type.dup(method, this, depth);
    }

    @Override
    public Type swap(MethodVisitor method, Type other) {
        Type.swap(method, this, other);
        return other;
    }

    @Override
    public Type pop(MethodVisitor method) {
        Type.pop(method, this);
        return this;
    }

    @Override
    public Type loadEmpty(MethodVisitor method) {
        assert (false) : "unsupported operation";
        return null;
    }

    protected static void pop(MethodVisitor method, Type type) {
        method.visitInsn(type.isCategory2() ? 88 : 87);
    }

    private static Type dup(MethodVisitor method, Type type, int depth) {
        boolean cat2 = type.isCategory2();
        switch (depth) {
            case 0: {
                method.visitInsn(cat2 ? 92 : 89);
                break;
            }
            case 1: {
                method.visitInsn(cat2 ? 93 : 90);
                break;
            }
            case 2: {
                method.visitInsn(cat2 ? 94 : 91);
                break;
            }
            default: {
                return null;
            }
        }
        return type;
    }

    private static void swap(MethodVisitor method, Type above, Type below) {
        if (below.isCategory2()) {
            if (above.isCategory2()) {
                method.visitInsn(94);
                method.visitInsn(88);
            } else {
                method.visitInsn(91);
                method.visitInsn(87);
            }
        } else if (above.isCategory2()) {
            method.visitInsn(93);
            method.visitInsn(88);
        } else {
            method.visitInsn(95);
        }
    }

    private static <T extends Type> T putInCache(T type) {
        cache.put(type.getTypeClass(), type);
        return type;
    }

    protected final Object readResolve() {
        return Type.typeFor(this.clazz);
    }

    private static abstract class ValueLessType
    extends Type {
        private static final long serialVersionUID = 1L;

        ValueLessType(String name) {
            super(name, Unknown.class, -1, 1);
        }

        @Override
        public Type load(MethodVisitor method, int slot) {
            throw new UnsupportedOperationException("load " + slot);
        }

        @Override
        public void store(MethodVisitor method, int slot) {
            throw new UnsupportedOperationException("store " + slot);
        }

        @Override
        public Type ldc(MethodVisitor method, Object c) {
            throw new UnsupportedOperationException("ldc " + c);
        }

        @Override
        public Type loadUndefined(MethodVisitor method) {
            throw new UnsupportedOperationException("load undefined");
        }

        @Override
        public Type loadForcedInitializer(MethodVisitor method) {
            throw new UnsupportedOperationException("load forced initializer");
        }

        @Override
        public Type convert(MethodVisitor method, Type to) {
            throw new UnsupportedOperationException("convert => " + to);
        }

        @Override
        public void _return(MethodVisitor method) {
            throw new UnsupportedOperationException("return");
        }

        @Override
        public Type add(MethodVisitor method, int programPoint) {
            throw new UnsupportedOperationException("add");
        }
    }

    private static interface Unknown {
    }
}

