/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.beans.BeansLinker;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.support.Guards;
import jdk.nashorn.internal.lookup.MethodHandleFactory;
import jdk.nashorn.internal.lookup.MethodHandleFunctionality;
import jdk.nashorn.internal.objects.annotations.Function;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;

public final class NativeJavaPackage
extends ScriptObject {
    private static final MethodHandleFunctionality MH = MethodHandleFactory.getFunctionality();
    private static final MethodHandle CLASS_NOT_FOUND = NativeJavaPackage.findOwnMH("classNotFound", Void.TYPE, NativeJavaPackage.class);
    private static final MethodHandle TYPE_GUARD = Guards.getClassGuard(NativeJavaPackage.class);
    private final String name;

    public NativeJavaPackage(String name, ScriptObject proto) {
        super(proto, null);
        Context.checkPackageAccess(name);
        this.name = name;
    }

    @Override
    public String getClassName() {
        return "JavaPackage";
    }

    public boolean equals(Object other) {
        if (other instanceof NativeJavaPackage) {
            return this.name.equals(((NativeJavaPackage)other).name);
        }
        return false;
    }

    public int hashCode() {
        return this.name == null ? 0 : this.name.hashCode();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String safeToString() {
        return this.toString();
    }

    public String toString() {
        return "[JavaPackage " + this.name + "]";
    }

    @Override
    public Object getDefaultValue(Class<?> hint) {
        if (hint == String.class) {
            return this.toString();
        }
        return super.getDefaultValue(hint);
    }

    @Override
    protected GuardedInvocation findNewMethod(CallSiteDescriptor desc, LinkRequest request) {
        return NativeJavaPackage.createClassNotFoundInvocation(desc);
    }

    @Override
    protected GuardedInvocation findCallMethod(CallSiteDescriptor desc, LinkRequest request) {
        return NativeJavaPackage.createClassNotFoundInvocation(desc);
    }

    private static GuardedInvocation createClassNotFoundInvocation(CallSiteDescriptor desc) {
        MethodType type = desc.getMethodType();
        return new GuardedInvocation(MH.dropArguments(CLASS_NOT_FOUND, 1, type.parameterList().subList(1, type.parameterCount())), type.parameterType(0) == NativeJavaPackage.class ? null : TYPE_GUARD);
    }

    private static void classNotFound(NativeJavaPackage pkg) throws ClassNotFoundException {
        throw new ClassNotFoundException(pkg.name);
    }

    @Function(attributes=2)
    public static Object __noSuchProperty__(Object self, Object name) {
        throw new AssertionError((Object)"__noSuchProperty__ placeholder called");
    }

    @Function(attributes=2)
    public static Object __noSuchMethod__(Object self, Object ... args2) {
        throw new AssertionError((Object)"__noSuchMethod__ placeholder called");
    }

    @Override
    public GuardedInvocation noSuchProperty(CallSiteDescriptor desc, LinkRequest request) {
        String propertyName = desc.getNameToken(2);
        this.createProperty(propertyName);
        return super.lookup(desc, request);
    }

    @Override
    protected Object invokeNoSuchProperty(String key, boolean isScope, int programPoint) {
        Object retval = this.createProperty(key);
        if (UnwarrantedOptimismException.isValid(programPoint)) {
            throw new UnwarrantedOptimismException(retval, programPoint);
        }
        return retval;
    }

    @Override
    public GuardedInvocation noSuchMethod(CallSiteDescriptor desc, LinkRequest request) {
        return this.noSuchProperty(desc, request);
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return MH.findStatic(MethodHandles.lookup(), NativeJavaPackage.class, name, MH.type(rtype, types));
    }

    private Object createProperty(String propertyName) {
        String fullName = this.name.isEmpty() ? propertyName : this.name + "." + propertyName;
        Context context = Context.getContextTrusted();
        Class<?> javaClass = null;
        try {
            javaClass = context.findClass(fullName);
        }
        catch (ClassNotFoundException | NoClassDefFoundError throwable) {
            // empty catch block
        }
        int openBrace = propertyName.indexOf(40);
        int closeBrace = propertyName.lastIndexOf(41);
        if (openBrace != -1 || closeBrace != -1) {
            int lastChar = propertyName.length() - 1;
            if (openBrace == -1 || closeBrace != lastChar) {
                throw ECMAErrors.typeError("improper.constructor.signature", propertyName);
            }
            String className = this.name + "." + propertyName.substring(0, openBrace);
            try {
                javaClass = context.findClass(className);
            }
            catch (ClassNotFoundException | NoClassDefFoundError e) {
                throw ECMAErrors.typeError(e, "no.such.java.class", className);
            }
            Object constructor = BeansLinker.getConstructorMethod(javaClass, propertyName.substring(openBrace + 1, lastChar));
            if (constructor != null) {
                this.set((Object)propertyName, constructor, 0);
                return constructor;
            }
            throw ECMAErrors.typeError("no.such.java.constructor", propertyName);
        }
        Object propertyValue = javaClass == null ? new NativeJavaPackage(fullName, this.getProto()) : StaticClass.forClass(javaClass);
        this.set((Object)propertyName, propertyValue, 0);
        return propertyValue;
    }
}

