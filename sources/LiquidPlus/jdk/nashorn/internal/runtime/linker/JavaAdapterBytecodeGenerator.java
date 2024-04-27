/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.commons.InstructionAdapter;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.linker.AdaptationException;
import jdk.nashorn.internal.runtime.linker.AdaptationResult;
import jdk.nashorn.internal.runtime.linker.ClassAndLoader;
import jdk.nashorn.internal.runtime.linker.JavaAdapterClassLoader;
import jdk.nashorn.internal.runtime.linker.JavaAdapterServices;
import sun.reflect.CallerSensitive;

final class JavaAdapterBytecodeGenerator {
    private static final Type SCRIPTUTILS_TYPE = Type.getType(ScriptUtils.class);
    private static final Type OBJECT_TYPE = Type.getType(Object.class);
    private static final Type CLASS_TYPE = Type.getType(Class.class);
    static final String OBJECT_TYPE_NAME = OBJECT_TYPE.getInternalName();
    static final String SCRIPTUTILS_TYPE_NAME = SCRIPTUTILS_TYPE.getInternalName();
    static final String INIT = "<init>";
    static final String GLOBAL_FIELD_NAME = "global";
    static final String GLOBAL_TYPE_DESCRIPTOR = OBJECT_TYPE.getDescriptor();
    static final String SET_GLOBAL_METHOD_DESCRIPTOR = Type.getMethodDescriptor(Type.VOID_TYPE, OBJECT_TYPE);
    static final String VOID_NOARG_METHOD_DESCRIPTOR = Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]);
    private static final Type SCRIPT_OBJECT_TYPE = Type.getType(ScriptObject.class);
    private static final Type SCRIPT_FUNCTION_TYPE = Type.getType(ScriptFunction.class);
    private static final Type STRING_TYPE = Type.getType(String.class);
    private static final Type METHOD_TYPE_TYPE = Type.getType(MethodType.class);
    private static final Type METHOD_HANDLE_TYPE = Type.getType(MethodHandle.class);
    private static final String GET_HANDLE_OBJECT_DESCRIPTOR = Type.getMethodDescriptor(METHOD_HANDLE_TYPE, OBJECT_TYPE, STRING_TYPE, METHOD_TYPE_TYPE);
    private static final String GET_HANDLE_FUNCTION_DESCRIPTOR = Type.getMethodDescriptor(METHOD_HANDLE_TYPE, SCRIPT_FUNCTION_TYPE, METHOD_TYPE_TYPE);
    private static final String GET_CLASS_INITIALIZER_DESCRIPTOR = Type.getMethodDescriptor(OBJECT_TYPE, new Type[0]);
    private static final Type RUNTIME_EXCEPTION_TYPE = Type.getType(RuntimeException.class);
    private static final Type THROWABLE_TYPE = Type.getType(Throwable.class);
    private static final Type UNSUPPORTED_OPERATION_TYPE = Type.getType(UnsupportedOperationException.class);
    private static final String SERVICES_CLASS_TYPE_NAME = Type.getInternalName(JavaAdapterServices.class);
    private static final String RUNTIME_EXCEPTION_TYPE_NAME = RUNTIME_EXCEPTION_TYPE.getInternalName();
    private static final String ERROR_TYPE_NAME = Type.getInternalName(Error.class);
    private static final String THROWABLE_TYPE_NAME = THROWABLE_TYPE.getInternalName();
    private static final String UNSUPPORTED_OPERATION_TYPE_NAME = UNSUPPORTED_OPERATION_TYPE.getInternalName();
    private static final String METHOD_HANDLE_TYPE_DESCRIPTOR = METHOD_HANDLE_TYPE.getDescriptor();
    private static final String GET_GLOBAL_METHOD_DESCRIPTOR = Type.getMethodDescriptor(OBJECT_TYPE, new Type[0]);
    private static final String GET_CLASS_METHOD_DESCRIPTOR = Type.getMethodDescriptor(CLASS_TYPE, new Type[0]);
    private static final String EXPORT_RETURN_VALUE_METHOD_DESCRIPTOR = Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE);
    private static final String UNWRAP_METHOD_DESCRIPTOR = Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE);
    private static final String GET_CONVERTER_METHOD_DESCRIPTOR = Type.getMethodDescriptor(METHOD_HANDLE_TYPE, CLASS_TYPE);
    private static final String TO_CHAR_PRIMITIVE_METHOD_DESCRIPTOR = Type.getMethodDescriptor(Type.CHAR_TYPE, OBJECT_TYPE);
    private static final String TO_STRING_METHOD_DESCRIPTOR = Type.getMethodDescriptor(STRING_TYPE, OBJECT_TYPE);
    private static final String ADAPTER_PACKAGE_PREFIX = "jdk/nashorn/javaadapters/";
    private static final String ADAPTER_CLASS_NAME_SUFFIX = "$$NashornJavaAdapter";
    private static final String JAVA_PACKAGE_PREFIX = "java/";
    private static final int MAX_GENERATED_TYPE_NAME_LENGTH = 255;
    private static final String CLASS_INIT = "<clinit>";
    static final String SUPER_PREFIX = "super$";
    private static final Collection<MethodInfo> EXCLUDED = JavaAdapterBytecodeGenerator.getExcludedMethods();
    private final Class<?> superClass;
    private final List<Class<?>> interfaces;
    private final ClassLoader commonLoader;
    private final boolean classOverride;
    private final String superClassName;
    private final String generatedClassName;
    private final Set<String> usedFieldNames = new HashSet<String>();
    private final Set<String> abstractMethodNames = new HashSet<String>();
    private final String samName;
    private final Set<MethodInfo> finalMethods = new HashSet<MethodInfo>(EXCLUDED);
    private final Set<MethodInfo> methodInfos = new HashSet<MethodInfo>();
    private boolean autoConvertibleFromFunction = false;
    private boolean hasExplicitFinalizer = false;
    private final Map<Class<?>, String> converterFields = new LinkedHashMap();
    private final Set<Class<?>> samReturnTypes = new HashSet();
    private final ClassWriter cw;
    private static final AccessControlContext GET_DECLARED_MEMBERS_ACC_CTXT = ClassAndLoader.createPermAccCtxt("accessDeclaredMembers");

    JavaAdapterBytecodeGenerator(Class<?> superClass, List<Class<?>> interfaces, ClassLoader commonLoader, boolean classOverride) throws AdaptationException {
        assert (superClass != null && !superClass.isInterface());
        assert (interfaces != null);
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.classOverride = classOverride;
        this.commonLoader = commonLoader;
        this.cw = new ClassWriter(3){

            @Override
            protected String getCommonSuperClass(String type1, String type2) {
                return JavaAdapterBytecodeGenerator.this.getCommonSuperClass(type1, type2);
            }
        };
        this.superClassName = Type.getInternalName(superClass);
        this.generatedClassName = JavaAdapterBytecodeGenerator.getGeneratedClassName(superClass, interfaces);
        this.cw.visit(51, 33, this.generatedClassName, null, this.superClassName, JavaAdapterBytecodeGenerator.getInternalTypeNames(interfaces));
        this.generateGlobalFields();
        this.gatherMethods(superClass);
        this.gatherMethods(interfaces);
        this.samName = this.abstractMethodNames.size() == 1 ? this.abstractMethodNames.iterator().next() : null;
        this.generateHandleFields();
        this.generateConverterFields();
        if (classOverride) {
            this.generateClassInit();
        }
        this.generateConstructors();
        this.generateMethods();
        this.generateSuperMethods();
        if (this.hasExplicitFinalizer) {
            this.generateFinalizerMethods();
        }
        this.cw.visitEnd();
    }

    private void generateGlobalFields() {
        this.cw.visitField(0x12 | (this.classOverride ? 8 : 0), GLOBAL_FIELD_NAME, GLOBAL_TYPE_DESCRIPTOR, null, null).visitEnd();
        this.usedFieldNames.add(GLOBAL_FIELD_NAME);
    }

    JavaAdapterClassLoader createAdapterClassLoader() {
        return new JavaAdapterClassLoader(this.generatedClassName, this.cw.toByteArray());
    }

    boolean isAutoConvertibleFromFunction() {
        return this.autoConvertibleFromFunction;
    }

    private static String getGeneratedClassName(Class<?> superType, List<Class<?>> interfaces) {
        Class<?> namingType = superType == Object.class ? (interfaces.isEmpty() ? Object.class : interfaces.get(0)) : superType;
        Package pkg = namingType.getPackage();
        String namingTypeName = Type.getInternalName(namingType);
        StringBuilder buf = new StringBuilder();
        if (namingTypeName.startsWith(JAVA_PACKAGE_PREFIX) || pkg == null || pkg.isSealed()) {
            buf.append(ADAPTER_PACKAGE_PREFIX).append(namingTypeName);
        } else {
            buf.append(namingTypeName).append(ADAPTER_CLASS_NAME_SUFFIX);
        }
        Iterator<Class<?>> it = interfaces.iterator();
        if (superType == Object.class && it.hasNext()) {
            it.next();
        }
        while (it.hasNext()) {
            buf.append("$$").append(it.next().getSimpleName());
        }
        return buf.toString().substring(0, Math.min(255, buf.length()));
    }

    private static String[] getInternalTypeNames(List<Class<?>> classes) {
        int interfaceCount = classes.size();
        String[] interfaceNames = new String[interfaceCount];
        for (int i = 0; i < interfaceCount; ++i) {
            interfaceNames[i] = Type.getInternalName(classes.get(i));
        }
        return interfaceNames;
    }

    private void generateHandleFields() {
        int flags = 0x12 | (this.classOverride ? 8 : 0);
        for (MethodInfo mi : this.methodInfos) {
            this.cw.visitField(flags, mi.methodHandleFieldName, METHOD_HANDLE_TYPE_DESCRIPTOR, null, null).visitEnd();
        }
    }

    private void generateConverterFields() {
        int flags = 0x12 | (this.classOverride ? 8 : 0);
        for (MethodInfo mi : this.methodInfos) {
            Class<?> returnType = mi.type.returnType();
            if (returnType.isPrimitive() || returnType == Object.class || returnType == String.class || this.converterFields.containsKey(returnType)) continue;
            String name = this.nextName("convert");
            this.converterFields.put(returnType, name);
            if (mi.getName().equals(this.samName)) {
                this.samReturnTypes.add(returnType);
            }
            this.cw.visitField(flags, name, METHOD_HANDLE_TYPE_DESCRIPTOR, null, null).visitEnd();
        }
    }

    private void generateClassInit() {
        Label initGlobal;
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(8, CLASS_INIT, Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]), null, null));
        mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "getClassOverrides", GET_CLASS_INITIALIZER_DESCRIPTOR, false);
        if (this.samName != null) {
            Label notAFunction = new Label();
            mv.dup();
            mv.instanceOf(SCRIPT_FUNCTION_TYPE);
            mv.ifeq(notAFunction);
            mv.checkcast(SCRIPT_FUNCTION_TYPE);
            for (MethodInfo mi : this.methodInfos) {
                if (mi.getName().equals(this.samName)) {
                    mv.dup();
                    JavaAdapterBytecodeGenerator.loadMethodTypeAndGetHandle(mv, mi, GET_HANDLE_FUNCTION_DESCRIPTOR);
                } else {
                    mv.visitInsn(1);
                }
                mv.putstatic(this.generatedClassName, mi.methodHandleFieldName, METHOD_HANDLE_TYPE_DESCRIPTOR);
            }
            initGlobal = new Label();
            mv.goTo(initGlobal);
            mv.visitLabel(notAFunction);
        } else {
            initGlobal = null;
        }
        for (MethodInfo mi : this.methodInfos) {
            mv.dup();
            mv.aconst(mi.getName());
            JavaAdapterBytecodeGenerator.loadMethodTypeAndGetHandle(mv, mi, GET_HANDLE_OBJECT_DESCRIPTOR);
            mv.putstatic(this.generatedClassName, mi.methodHandleFieldName, METHOD_HANDLE_TYPE_DESCRIPTOR);
        }
        if (initGlobal != null) {
            mv.visitLabel(initGlobal);
        }
        JavaAdapterBytecodeGenerator.invokeGetGlobalWithNullCheck(mv);
        mv.putstatic(this.generatedClassName, GLOBAL_FIELD_NAME, GLOBAL_TYPE_DESCRIPTOR);
        this.generateConverterInit(mv, false);
        JavaAdapterBytecodeGenerator.endInitMethod(mv);
    }

    private void generateConverterInit(InstructionAdapter mv, boolean samOnly) {
        assert (!samOnly || !this.classOverride);
        for (Map.Entry<Class<?>, String> converterField : this.converterFields.entrySet()) {
            Class<?> returnType = converterField.getKey();
            if (!this.classOverride) {
                mv.visitVarInsn(25, 0);
            }
            if (samOnly && !this.samReturnTypes.contains(returnType)) {
                mv.visitInsn(1);
            } else {
                mv.aconst(Type.getType(converterField.getKey()));
                mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "getObjectConverter", GET_CONVERTER_METHOD_DESCRIPTOR, false);
            }
            if (this.classOverride) {
                mv.putstatic(this.generatedClassName, converterField.getValue(), METHOD_HANDLE_TYPE_DESCRIPTOR);
                continue;
            }
            mv.putfield(this.generatedClassName, converterField.getValue(), METHOD_HANDLE_TYPE_DESCRIPTOR);
        }
    }

    private static void loadMethodTypeAndGetHandle(InstructionAdapter mv, MethodInfo mi, String getHandleDescriptor) {
        mv.aconst(Type.getMethodType(mi.type.generic().toMethodDescriptorString()));
        mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "getHandle", getHandleDescriptor, false);
    }

    private static void invokeGetGlobalWithNullCheck(InstructionAdapter mv) {
        JavaAdapterBytecodeGenerator.invokeGetGlobal(mv);
        mv.dup();
        mv.invokevirtual(OBJECT_TYPE_NAME, "getClass", GET_CLASS_METHOD_DESCRIPTOR, false);
        mv.pop();
    }

    private void generateConstructors() throws AdaptationException {
        boolean gotCtor = false;
        for (Constructor<?> ctor : this.superClass.getDeclaredConstructors()) {
            int modifier = ctor.getModifiers();
            if ((modifier & 5) == 0 || JavaAdapterBytecodeGenerator.isCallerSensitive(ctor)) continue;
            this.generateConstructors(ctor);
            gotCtor = true;
        }
        if (!gotCtor) {
            throw new AdaptationException(AdaptationResult.Outcome.ERROR_NO_ACCESSIBLE_CONSTRUCTOR, this.superClass.getCanonicalName());
        }
    }

    private void generateConstructors(Constructor<?> ctor) {
        if (this.classOverride) {
            this.generateDelegatingConstructor(ctor);
        } else {
            this.generateOverridingConstructor(ctor, false);
            if (this.samName != null) {
                if (!this.autoConvertibleFromFunction && ctor.getParameterTypes().length == 0) {
                    this.autoConvertibleFromFunction = true;
                }
                this.generateOverridingConstructor(ctor, true);
            }
        }
    }

    private void generateDelegatingConstructor(Constructor<?> ctor) {
        Type originalCtorType = Type.getType(ctor);
        Type[] argTypes = originalCtorType.getArgumentTypes();
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(1 | (ctor.isVarArgs() ? 128 : 0), INIT, Type.getMethodDescriptor(originalCtorType.getReturnType(), argTypes), null, null));
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        int offset = 1;
        for (Type argType : argTypes) {
            mv.load(offset, argType);
            offset += argType.getSize();
        }
        mv.invokespecial(this.superClassName, INIT, originalCtorType.getDescriptor(), false);
        JavaAdapterBytecodeGenerator.endInitMethod(mv);
    }

    private void generateOverridingConstructor(Constructor<?> ctor, boolean fromFunction) {
        Type extraArgumentType;
        Type originalCtorType = Type.getType(ctor);
        Type[] originalArgTypes = originalCtorType.getArgumentTypes();
        int argLen = originalArgTypes.length;
        Type[] newArgTypes = new Type[argLen + 1];
        newArgTypes[argLen] = extraArgumentType = fromFunction ? SCRIPT_FUNCTION_TYPE : SCRIPT_OBJECT_TYPE;
        System.arraycopy(originalArgTypes, 0, newArgTypes, 0, argLen);
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(1, INIT, Type.getMethodDescriptor(originalCtorType.getReturnType(), newArgTypes), null, null));
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        Class<?>[] argTypes = ctor.getParameterTypes();
        int offset = 1;
        for (int i = 0; i < argLen; ++i) {
            Type argType = Type.getType(argTypes[i]);
            mv.load(offset, argType);
            offset += argType.getSize();
        }
        mv.invokespecial(this.superClassName, INIT, originalCtorType.getDescriptor(), false);
        String getHandleDescriptor = fromFunction ? GET_HANDLE_FUNCTION_DESCRIPTOR : GET_HANDLE_OBJECT_DESCRIPTOR;
        for (MethodInfo mi : this.methodInfos) {
            mv.visitVarInsn(25, 0);
            if (fromFunction && !mi.getName().equals(this.samName)) {
                mv.visitInsn(1);
            } else {
                mv.visitVarInsn(25, offset);
                if (!fromFunction) {
                    mv.aconst(mi.getName());
                }
                JavaAdapterBytecodeGenerator.loadMethodTypeAndGetHandle(mv, mi, getHandleDescriptor);
            }
            mv.putfield(this.generatedClassName, mi.methodHandleFieldName, METHOD_HANDLE_TYPE_DESCRIPTOR);
        }
        mv.visitVarInsn(25, 0);
        JavaAdapterBytecodeGenerator.invokeGetGlobalWithNullCheck(mv);
        mv.putfield(this.generatedClassName, GLOBAL_FIELD_NAME, GLOBAL_TYPE_DESCRIPTOR);
        this.generateConverterInit(mv, fromFunction);
        JavaAdapterBytecodeGenerator.endInitMethod(mv);
        if (!fromFunction) {
            newArgTypes[argLen] = OBJECT_TYPE;
            InstructionAdapter mv2 = new InstructionAdapter(this.cw.visitMethod(1, INIT, Type.getMethodDescriptor(originalCtorType.getReturnType(), newArgTypes), null, null));
            this.generateOverridingConstructorWithObjectParam(mv2, ctor, originalCtorType.getDescriptor());
        }
    }

    private void generateOverridingConstructorWithObjectParam(InstructionAdapter mv, Constructor<?> ctor, String ctorDescriptor) {
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        Class<?>[] argTypes = ctor.getParameterTypes();
        int offset = 1;
        for (int i = 0; i < argTypes.length; ++i) {
            Type argType = Type.getType(argTypes[i]);
            mv.load(offset, argType);
            offset += argType.getSize();
        }
        mv.invokespecial(this.superClassName, INIT, ctorDescriptor, false);
        mv.visitVarInsn(25, offset);
        mv.visitInsn(1);
        mv.visitInsn(1);
        mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "getHandle", GET_HANDLE_OBJECT_DESCRIPTOR, false);
        JavaAdapterBytecodeGenerator.endInitMethod(mv);
    }

    private static void endInitMethod(InstructionAdapter mv) {
        mv.visitInsn(177);
        JavaAdapterBytecodeGenerator.endMethod(mv);
    }

    private static void endMethod(InstructionAdapter mv) {
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static void invokeGetGlobal(InstructionAdapter mv) {
        mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "getGlobal", GET_GLOBAL_METHOD_DESCRIPTOR, false);
    }

    private static void invokeSetGlobal(InstructionAdapter mv) {
        mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "setGlobal", SET_GLOBAL_METHOD_DESCRIPTOR, false);
    }

    private String nextName(String name) {
        int i = 0;
        String nextName = name;
        while (!this.usedFieldNames.add(nextName)) {
            String ordinal = String.valueOf(i++);
            int maxNameLen = 255 - ordinal.length();
            nextName = (name.length() <= maxNameLen ? name : name.substring(0, maxNameLen)).concat(ordinal);
        }
        return nextName;
    }

    private void generateMethods() {
        for (MethodInfo mi : this.methodInfos) {
            this.generateMethod(mi);
        }
    }

    private void generateMethod(MethodInfo mi) {
        Label throwableHandler;
        Method method = mi.method;
        Class<?>[] exceptions = method.getExceptionTypes();
        String[] exceptionNames = JavaAdapterBytecodeGenerator.getExceptionNames(exceptions);
        MethodType type = mi.type;
        String methodDesc = type.toMethodDescriptorString();
        String name = mi.getName();
        Type asmType = Type.getMethodType(methodDesc);
        Type[] asmArgTypes = asmType.getArgumentTypes();
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(JavaAdapterBytecodeGenerator.getAccessModifiers(method), name, methodDesc, null, exceptionNames));
        mv.visitCode();
        Label handleDefined = new Label();
        Class<?> returnType = type.returnType();
        Type asmReturnType = Type.getType(returnType);
        if (this.classOverride) {
            mv.getstatic(this.generatedClassName, mi.methodHandleFieldName, METHOD_HANDLE_TYPE_DESCRIPTOR);
        } else {
            mv.visitVarInsn(25, 0);
            mv.getfield(this.generatedClassName, mi.methodHandleFieldName, METHOD_HANDLE_TYPE_DESCRIPTOR);
        }
        mv.visitInsn(89);
        mv.visitJumpInsn(199, handleDefined);
        if (Modifier.isAbstract(method.getModifiers())) {
            mv.anew(UNSUPPORTED_OPERATION_TYPE);
            mv.dup();
            mv.invokespecial(UNSUPPORTED_OPERATION_TYPE_NAME, INIT, VOID_NOARG_METHOD_DESCRIPTOR, false);
            mv.athrow();
        } else {
            mv.visitInsn(87);
            this.emitSuperCall(mv, method.getDeclaringClass(), name, methodDesc);
        }
        mv.visitLabel(handleDefined);
        if (this.classOverride) {
            mv.getstatic(this.generatedClassName, GLOBAL_FIELD_NAME, GLOBAL_TYPE_DESCRIPTOR);
        } else {
            mv.visitVarInsn(25, 0);
            mv.getfield(this.generatedClassName, GLOBAL_FIELD_NAME, GLOBAL_TYPE_DESCRIPTOR);
        }
        Label setupGlobal = new Label();
        mv.visitLabel(setupGlobal);
        int nextLocalVar = 1;
        for (Type t : asmArgTypes) {
            nextLocalVar += t.getSize();
        }
        int currentGlobalVar = nextLocalVar++;
        int globalsDifferVar = nextLocalVar++;
        mv.dup();
        JavaAdapterBytecodeGenerator.invokeGetGlobal(mv);
        mv.dup();
        mv.visitVarInsn(58, currentGlobalVar);
        Label globalsDiffer = new Label();
        mv.ifacmpne(globalsDiffer);
        mv.pop();
        mv.iconst(0);
        Label invokeHandle = new Label();
        mv.goTo(invokeHandle);
        mv.visitLabel(globalsDiffer);
        JavaAdapterBytecodeGenerator.invokeSetGlobal(mv);
        mv.iconst(1);
        mv.visitLabel(invokeHandle);
        mv.visitVarInsn(54, globalsDifferVar);
        int varOffset = 1;
        for (Type t : asmArgTypes) {
            mv.load(varOffset, t);
            JavaAdapterBytecodeGenerator.boxStackTop(mv, t);
            varOffset += t.getSize();
        }
        Label tryBlockStart = new Label();
        mv.visitLabel(tryBlockStart);
        JavaAdapterBytecodeGenerator.emitInvokeExact(mv, type.generic());
        this.convertReturnValue(mv, returnType, asmReturnType);
        Label tryBlockEnd = new Label();
        mv.visitLabel(tryBlockEnd);
        JavaAdapterBytecodeGenerator.emitFinally(mv, currentGlobalVar, globalsDifferVar);
        mv.areturn(asmReturnType);
        boolean throwableDeclared = JavaAdapterBytecodeGenerator.isThrowableDeclared(exceptions);
        if (!throwableDeclared) {
            throwableHandler = new Label();
            mv.visitLabel(throwableHandler);
            mv.anew(RUNTIME_EXCEPTION_TYPE);
            mv.dupX1();
            mv.swap();
            mv.invokespecial(RUNTIME_EXCEPTION_TYPE_NAME, INIT, Type.getMethodDescriptor(Type.VOID_TYPE, THROWABLE_TYPE), false);
        } else {
            throwableHandler = null;
        }
        Label rethrowHandler = new Label();
        mv.visitLabel(rethrowHandler);
        JavaAdapterBytecodeGenerator.emitFinally(mv, currentGlobalVar, globalsDifferVar);
        mv.athrow();
        Label methodEnd = new Label();
        mv.visitLabel(methodEnd);
        mv.visitLocalVariable("currentGlobal", GLOBAL_TYPE_DESCRIPTOR, null, setupGlobal, methodEnd, currentGlobalVar);
        mv.visitLocalVariable("globalsDiffer", Type.BOOLEAN_TYPE.getDescriptor(), null, setupGlobal, methodEnd, globalsDifferVar);
        if (throwableDeclared) {
            mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, rethrowHandler, THROWABLE_TYPE_NAME);
            assert (throwableHandler == null);
        } else {
            mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, rethrowHandler, RUNTIME_EXCEPTION_TYPE_NAME);
            mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, rethrowHandler, ERROR_TYPE_NAME);
            for (String excName : exceptionNames) {
                mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, rethrowHandler, excName);
            }
            mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, throwableHandler, THROWABLE_TYPE_NAME);
        }
        JavaAdapterBytecodeGenerator.endMethod(mv);
    }

    private void convertReturnValue(InstructionAdapter mv, Class<?> returnType, Type asmReturnType) {
        switch (asmReturnType.getSort()) {
            case 0: {
                mv.pop();
                break;
            }
            case 1: {
                JSType.TO_BOOLEAN.invoke(mv);
                break;
            }
            case 3: {
                JSType.TO_INT32.invoke(mv);
                mv.visitInsn(145);
                break;
            }
            case 4: {
                JSType.TO_INT32.invoke(mv);
                mv.visitInsn(147);
                break;
            }
            case 2: {
                mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "toCharPrimitive", TO_CHAR_PRIMITIVE_METHOD_DESCRIPTOR, false);
                break;
            }
            case 5: {
                JSType.TO_INT32.invoke(mv);
                break;
            }
            case 7: {
                JSType.TO_LONG.invoke(mv);
                break;
            }
            case 6: {
                JSType.TO_NUMBER.invoke(mv);
                mv.visitInsn(144);
                break;
            }
            case 8: {
                JSType.TO_NUMBER.invoke(mv);
                break;
            }
            default: {
                if (asmReturnType.equals(OBJECT_TYPE)) {
                    mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "exportReturnValue", EXPORT_RETURN_VALUE_METHOD_DESCRIPTOR, false);
                    break;
                }
                if (asmReturnType.equals(STRING_TYPE)) {
                    mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "toString", TO_STRING_METHOD_DESCRIPTOR, false);
                    break;
                }
                if (this.classOverride) {
                    mv.getstatic(this.generatedClassName, this.converterFields.get(returnType), METHOD_HANDLE_TYPE_DESCRIPTOR);
                } else {
                    mv.visitVarInsn(25, 0);
                    mv.getfield(this.generatedClassName, this.converterFields.get(returnType), METHOD_HANDLE_TYPE_DESCRIPTOR);
                }
                mv.swap();
                JavaAdapterBytecodeGenerator.emitInvokeExact(mv, MethodType.methodType(returnType, Object.class));
            }
        }
    }

    private static void emitInvokeExact(InstructionAdapter mv, MethodType type) {
        mv.invokevirtual(METHOD_HANDLE_TYPE.getInternalName(), "invokeExact", type.toMethodDescriptorString(), false);
    }

    private static void boxStackTop(InstructionAdapter mv, Type t) {
        switch (t.getSort()) {
            case 1: {
                JavaAdapterBytecodeGenerator.invokeValueOf(mv, "Boolean", 'Z');
                break;
            }
            case 3: 
            case 4: 
            case 5: {
                JavaAdapterBytecodeGenerator.invokeValueOf(mv, "Integer", 'I');
                break;
            }
            case 2: {
                JavaAdapterBytecodeGenerator.invokeValueOf(mv, "Character", 'C');
                break;
            }
            case 6: {
                mv.visitInsn(141);
                JavaAdapterBytecodeGenerator.invokeValueOf(mv, "Double", 'D');
                break;
            }
            case 7: {
                JavaAdapterBytecodeGenerator.invokeValueOf(mv, "Long", 'J');
                break;
            }
            case 8: {
                JavaAdapterBytecodeGenerator.invokeValueOf(mv, "Double", 'D');
                break;
            }
            case 9: 
            case 11: {
                break;
            }
            case 10: {
                if (!t.equals(OBJECT_TYPE)) break;
                mv.invokestatic(SCRIPTUTILS_TYPE_NAME, "unwrap", UNWRAP_METHOD_DESCRIPTOR, false);
                break;
            }
            default: {
                assert (false);
                break;
            }
        }
    }

    private static void invokeValueOf(InstructionAdapter mv, String boxedType, char unboxedType) {
        mv.invokestatic("java/lang/" + boxedType, "valueOf", "(" + unboxedType + ")Ljava/lang/" + boxedType + ";", false);
    }

    private static void emitFinally(InstructionAdapter mv, int currentGlobalVar, int globalsDifferVar) {
        mv.visitVarInsn(21, globalsDifferVar);
        Label skip = new Label();
        mv.ifeq(skip);
        mv.visitVarInsn(25, currentGlobalVar);
        JavaAdapterBytecodeGenerator.invokeSetGlobal(mv);
        mv.visitLabel(skip);
    }

    private static boolean isThrowableDeclared(Class<?>[] exceptions) {
        for (Class<?> exception : exceptions) {
            if (exception != Throwable.class) continue;
            return true;
        }
        return false;
    }

    private void generateSuperMethods() {
        for (MethodInfo mi : this.methodInfos) {
            if (Modifier.isAbstract(mi.method.getModifiers())) continue;
            this.generateSuperMethod(mi);
        }
    }

    private void generateSuperMethod(MethodInfo mi) {
        Method method = mi.method;
        String methodDesc = mi.type.toMethodDescriptorString();
        String name = mi.getName();
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(JavaAdapterBytecodeGenerator.getAccessModifiers(method), SUPER_PREFIX + name, methodDesc, null, JavaAdapterBytecodeGenerator.getExceptionNames(method.getExceptionTypes())));
        mv.visitCode();
        this.emitSuperCall(mv, method.getDeclaringClass(), name, methodDesc);
        JavaAdapterBytecodeGenerator.endMethod(mv);
    }

    private Class<?> findInvokespecialOwnerFor(Class<?> cl) {
        assert (Modifier.isInterface(cl.getModifiers())) : cl + " is not an interface";
        if (cl.isAssignableFrom(this.superClass)) {
            return this.superClass;
        }
        for (Class<?> iface : this.interfaces) {
            if (!cl.isAssignableFrom(iface)) continue;
            return iface;
        }
        throw new AssertionError((Object)("can't find the class/interface that extends " + cl));
    }

    private void emitSuperCall(InstructionAdapter mv, Class<?> owner, String name, String methodDesc) {
        mv.visitVarInsn(25, 0);
        int nextParam = 1;
        Type methodType = Type.getMethodType(methodDesc);
        for (Type t : methodType.getArgumentTypes()) {
            mv.load(nextParam, t);
            nextParam += t.getSize();
        }
        if (Modifier.isInterface(owner.getModifiers())) {
            mv.invokespecial(Type.getInternalName(this.findInvokespecialOwnerFor(owner)), name, methodDesc, false);
        } else {
            mv.invokespecial(this.superClassName, name, methodDesc, false);
        }
        mv.areturn(methodType.getReturnType());
    }

    private void generateFinalizerMethods() {
        String finalizerDelegateName = this.nextName("access$");
        this.generateFinalizerDelegate(finalizerDelegateName);
        this.generateFinalizerOverride(finalizerDelegateName);
    }

    private void generateFinalizerDelegate(String finalizerDelegateName) {
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(10, finalizerDelegateName, Type.getMethodDescriptor(Type.VOID_TYPE, OBJECT_TYPE), null, null));
        mv.visitVarInsn(25, 0);
        mv.checkcast(Type.getType(this.generatedClassName));
        mv.invokespecial(this.superClassName, "finalize", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]), false);
        mv.visitInsn(177);
        JavaAdapterBytecodeGenerator.endMethod(mv);
    }

    private void generateFinalizerOverride(String finalizerDelegateName) {
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(1, "finalize", VOID_NOARG_METHOD_DESCRIPTOR, null, null));
        mv.aconst(new Handle(6, this.generatedClassName, finalizerDelegateName, Type.getMethodDescriptor(Type.VOID_TYPE, OBJECT_TYPE)));
        mv.visitVarInsn(25, 0);
        mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "invokeNoPermissions", Type.getMethodDescriptor(METHOD_HANDLE_TYPE, OBJECT_TYPE), false);
        mv.visitInsn(177);
        JavaAdapterBytecodeGenerator.endMethod(mv);
    }

    private static String[] getExceptionNames(Class<?>[] exceptions) {
        String[] exceptionNames = new String[exceptions.length];
        for (int i = 0; i < exceptions.length; ++i) {
            exceptionNames[i] = Type.getInternalName(exceptions[i]);
        }
        return exceptionNames;
    }

    private static int getAccessModifiers(Method method) {
        return 1 | (method.isVarArgs() ? 128 : 0);
    }

    private void gatherMethods(Class<?> type) throws AdaptationException {
        if (Modifier.isPublic(type.getModifiers())) {
            Method[] typeMethods = type.isInterface() ? type.getMethods() : type.getDeclaredMethods();
            for (GenericDeclaration genericDeclaration : typeMethods) {
                int m;
                String name = ((Method)genericDeclaration).getName();
                if (name.startsWith(SUPER_PREFIX) || Modifier.isStatic(m = ((Method)genericDeclaration).getModifiers()) || !Modifier.isPublic(m) && !Modifier.isProtected(m)) continue;
                if (name.equals("finalize") && ((Method)genericDeclaration).getParameterCount() == 0) {
                    if (type == Object.class) continue;
                    this.hasExplicitFinalizer = true;
                    if (!Modifier.isFinal(m)) continue;
                    throw new AdaptationException(AdaptationResult.Outcome.ERROR_FINAL_FINALIZER, type.getCanonicalName());
                }
                MethodInfo mi = new MethodInfo((Method)genericDeclaration);
                if (Modifier.isFinal(m) || JavaAdapterBytecodeGenerator.isCallerSensitive((AccessibleObject)((Object)genericDeclaration))) {
                    this.finalMethods.add(mi);
                    continue;
                }
                if (this.finalMethods.contains(mi) || !this.methodInfos.add(mi)) continue;
                if (Modifier.isAbstract(m)) {
                    this.abstractMethodNames.add(mi.getName());
                }
                mi.setIsCanonical(this);
            }
        }
        if (!type.isInterface()) {
            Class<?> superType = type.getSuperclass();
            if (superType != null) {
                this.gatherMethods(superType);
            }
            for (GenericDeclaration genericDeclaration : type.getInterfaces()) {
                this.gatherMethods((Class<?>)genericDeclaration);
            }
        }
    }

    private void gatherMethods(List<Class<?>> classes) throws AdaptationException {
        for (Class<?> c : classes) {
            this.gatherMethods(c);
        }
    }

    private static Collection<MethodInfo> getExcludedMethods() {
        return AccessController.doPrivileged(new PrivilegedAction<Collection<MethodInfo>>(){

            @Override
            public Collection<MethodInfo> run() {
                try {
                    return Arrays.asList(new MethodInfo((Class)Object.class, "finalize", new Class[0]), new MethodInfo((Class)Object.class, "clone", new Class[0]));
                }
                catch (NoSuchMethodException e) {
                    throw new AssertionError((Object)e);
                }
            }
        }, GET_DECLARED_MEMBERS_ACC_CTXT);
    }

    private String getCommonSuperClass(String type1, String type2) {
        try {
            Class<?> c1 = Class.forName(type1.replace('/', '.'), false, this.commonLoader);
            Class<?> c2 = Class.forName(type2.replace('/', '.'), false, this.commonLoader);
            if (c1.isAssignableFrom(c2)) {
                return type1;
            }
            if (c2.isAssignableFrom(c1)) {
                return type2;
            }
            if (c1.isInterface() || c2.isInterface()) {
                return OBJECT_TYPE_NAME;
            }
            return JavaAdapterBytecodeGenerator.assignableSuperClass(c1, c2).getName().replace('.', '/');
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> assignableSuperClass(Class<?> c1, Class<?> c2) {
        Class<?> superClass = c1.getSuperclass();
        return superClass.isAssignableFrom(c2) ? superClass : JavaAdapterBytecodeGenerator.assignableSuperClass(superClass, c2);
    }

    private static boolean isCallerSensitive(AccessibleObject e) {
        return e.isAnnotationPresent(CallerSensitive.class);
    }

    private static class MethodInfo {
        private final Method method;
        private final MethodType type;
        private String methodHandleFieldName;

        private MethodInfo(Class<?> clazz, String name, Class<?> ... argTypes) throws NoSuchMethodException {
            this(clazz.getDeclaredMethod(name, argTypes));
        }

        private MethodInfo(Method method) {
            this.method = method;
            this.type = Lookup.MH.type(method.getReturnType(), method.getParameterTypes());
        }

        public boolean equals(Object obj) {
            return obj instanceof MethodInfo && this.equals((MethodInfo)obj);
        }

        private boolean equals(MethodInfo other) {
            return this.getName().equals(other.getName()) && this.type.equals((Object)other.type);
        }

        String getName() {
            return this.method.getName();
        }

        public int hashCode() {
            return this.getName().hashCode() ^ this.type.hashCode();
        }

        void setIsCanonical(JavaAdapterBytecodeGenerator self) {
            this.methodHandleFieldName = self.nextName(this.getName());
        }
    }
}

