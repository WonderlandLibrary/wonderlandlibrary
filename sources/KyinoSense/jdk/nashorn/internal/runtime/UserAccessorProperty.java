/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.concurrent.Callable;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.SpillProperty;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;
import jdk.nashorn.internal.runtime.linker.Bootstrap;

public final class UserAccessorProperty
extends SpillProperty {
    private static final long serialVersionUID = -5928687246526840321L;
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
    private static final MethodHandle INVOKE_OBJECT_GETTER = UserAccessorProperty.findOwnMH_S("invokeObjectGetter", Object.class, Accessors.class, MethodHandle.class, Object.class);
    private static final MethodHandle INVOKE_INT_GETTER = UserAccessorProperty.findOwnMH_S("invokeIntGetter", Integer.TYPE, Accessors.class, MethodHandle.class, Integer.TYPE, Object.class);
    private static final MethodHandle INVOKE_NUMBER_GETTER = UserAccessorProperty.findOwnMH_S("invokeNumberGetter", Double.TYPE, Accessors.class, MethodHandle.class, Integer.TYPE, Object.class);
    private static final MethodHandle INVOKE_OBJECT_SETTER = UserAccessorProperty.findOwnMH_S("invokeObjectSetter", Void.TYPE, Accessors.class, MethodHandle.class, String.class, Object.class, Object.class);
    private static final MethodHandle INVOKE_INT_SETTER = UserAccessorProperty.findOwnMH_S("invokeIntSetter", Void.TYPE, Accessors.class, MethodHandle.class, String.class, Object.class, Integer.TYPE);
    private static final MethodHandle INVOKE_NUMBER_SETTER = UserAccessorProperty.findOwnMH_S("invokeNumberSetter", Void.TYPE, Accessors.class, MethodHandle.class, String.class, Object.class, Double.TYPE);
    private static final Object OBJECT_GETTER_INVOKER_KEY = new Object();
    private static final Object OBJECT_SETTER_INVOKER_KEY = new Object();

    private static MethodHandle getObjectGetterInvoker() {
        return Context.getGlobal().getDynamicInvoker(OBJECT_GETTER_INVOKER_KEY, new Callable<MethodHandle>(){

            @Override
            public MethodHandle call() throws Exception {
                return UserAccessorProperty.getINVOKE_UA_GETTER(Object.class, -1);
            }
        });
    }

    static MethodHandle getINVOKE_UA_GETTER(Class<?> returnType, int programPoint) {
        if (UnwarrantedOptimismException.isValid(programPoint)) {
            int flags = 8 | programPoint << 11;
            return Bootstrap.createDynamicInvoker("dyn:call", flags, returnType, Object.class, Object.class);
        }
        return Bootstrap.createDynamicInvoker("dyn:call", Object.class, Object.class, Object.class);
    }

    private static MethodHandle getObjectSetterInvoker() {
        return Context.getGlobal().getDynamicInvoker(OBJECT_SETTER_INVOKER_KEY, new Callable<MethodHandle>(){

            @Override
            public MethodHandle call() throws Exception {
                return UserAccessorProperty.getINVOKE_UA_SETTER(Object.class);
            }
        });
    }

    static MethodHandle getINVOKE_UA_SETTER(Class<?> valueType) {
        return Bootstrap.createDynamicInvoker("dyn:call", Void.TYPE, Object.class, Object.class, valueType);
    }

    UserAccessorProperty(String key, int flags, int slot) {
        super(key, flags, slot);
    }

    private UserAccessorProperty(UserAccessorProperty property) {
        super(property);
    }

    private UserAccessorProperty(UserAccessorProperty property, Class<?> newType) {
        super(property, newType);
    }

    @Override
    public Property copy() {
        return new UserAccessorProperty(this);
    }

    @Override
    public Property copy(Class<?> newType) {
        return new UserAccessorProperty(this, newType);
    }

    void setAccessors(ScriptObject sobj, PropertyMap map, Accessors gs) {
        try {
            super.getSetter(Object.class, map).invokeExact(sobj, gs);
        }
        catch (Error | RuntimeException t) {
            throw t;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    Accessors getAccessors(ScriptObject sobj) {
        try {
            Object gs = super.getGetter(Object.class).invokeExact(sobj);
            return (Accessors)gs;
        }
        catch (Error | RuntimeException t) {
            throw t;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    protected Class<?> getLocalType() {
        return Object.class;
    }

    @Override
    public boolean hasGetterFunction(ScriptObject sobj) {
        return this.getAccessors((ScriptObject)sobj).getter != null;
    }

    @Override
    public boolean hasSetterFunction(ScriptObject sobj) {
        return this.getAccessors((ScriptObject)sobj).setter != null;
    }

    @Override
    public int getIntValue(ScriptObject self, ScriptObject owner) {
        return (Integer)this.getObjectValue(self, owner);
    }

    @Override
    public double getDoubleValue(ScriptObject self, ScriptObject owner) {
        return (Double)this.getObjectValue(self, owner);
    }

    @Override
    public Object getObjectValue(ScriptObject self, ScriptObject owner) {
        try {
            return UserAccessorProperty.invokeObjectGetter(this.getAccessors(owner != null ? owner : self), UserAccessorProperty.getObjectGetterInvoker(), self);
        }
        catch (Error | RuntimeException t) {
            throw t;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public void setValue(ScriptObject self, ScriptObject owner, int value, boolean strict) {
        this.setValue(self, owner, (Object)value, strict);
    }

    @Override
    public void setValue(ScriptObject self, ScriptObject owner, double value, boolean strict) {
        this.setValue(self, owner, (Object)value, strict);
    }

    @Override
    public void setValue(ScriptObject self, ScriptObject owner, Object value, boolean strict) {
        try {
            UserAccessorProperty.invokeObjectSetter(this.getAccessors(owner != null ? owner : self), UserAccessorProperty.getObjectSetterInvoker(), strict ? this.getKey() : null, self, value);
        }
        catch (Error | RuntimeException t) {
            throw t;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Override
    public MethodHandle getGetter(Class<?> type) {
        return Lookup.filterReturnType(INVOKE_OBJECT_GETTER, type);
    }

    @Override
    public MethodHandle getOptimisticGetter(Class<?> type, int programPoint) {
        if (type == Integer.TYPE) {
            return INVOKE_INT_GETTER;
        }
        if (type == Double.TYPE) {
            return INVOKE_NUMBER_GETTER;
        }
        assert (type == Object.class);
        return INVOKE_OBJECT_GETTER;
    }

    @Override
    void initMethodHandles(Class<?> structure) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScriptFunction getGetterFunction(ScriptObject sobj) {
        Object value = this.getAccessors((ScriptObject)sobj).getter;
        return value instanceof ScriptFunction ? (ScriptFunction)value : null;
    }

    @Override
    public MethodHandle getSetter(Class<?> type, PropertyMap currentMap) {
        if (type == Integer.TYPE) {
            return INVOKE_INT_SETTER;
        }
        if (type == Double.TYPE) {
            return INVOKE_NUMBER_SETTER;
        }
        assert (type == Object.class);
        return INVOKE_OBJECT_SETTER;
    }

    @Override
    public ScriptFunction getSetterFunction(ScriptObject sobj) {
        Object value = this.getAccessors((ScriptObject)sobj).setter;
        return value instanceof ScriptFunction ? (ScriptFunction)value : null;
    }

    MethodHandle getAccessorsGetter() {
        return super.getGetter(Object.class).asType(MethodType.methodType(Accessors.class, Object.class));
    }

    private static Object invokeObjectGetter(Accessors gs, MethodHandle invoker, Object self) throws Throwable {
        Object func = gs.getter;
        if (func instanceof ScriptFunction) {
            return invoker.invokeExact(func, self);
        }
        return ScriptRuntime.UNDEFINED;
    }

    private static int invokeIntGetter(Accessors gs, MethodHandle invoker, int programPoint, Object self) throws Throwable {
        Object func = gs.getter;
        if (func instanceof ScriptFunction) {
            return invoker.invokeExact(func, self);
        }
        throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint);
    }

    private static double invokeNumberGetter(Accessors gs, MethodHandle invoker, int programPoint, Object self) throws Throwable {
        Object func = gs.getter;
        if (func instanceof ScriptFunction) {
            return invoker.invokeExact(func, self);
        }
        throw new UnwarrantedOptimismException(ScriptRuntime.UNDEFINED, programPoint);
    }

    private static void invokeObjectSetter(Accessors gs, MethodHandle invoker, String name, Object self, Object value) throws Throwable {
        Object func = gs.setter;
        if (func instanceof ScriptFunction) {
            invoker.invokeExact(func, self, value);
        } else if (name != null) {
            throw ECMAErrors.typeError("property.has.no.setter", name, ScriptRuntime.safeToString(self));
        }
    }

    private static void invokeIntSetter(Accessors gs, MethodHandle invoker, String name, Object self, int value) throws Throwable {
        Object func = gs.setter;
        if (func instanceof ScriptFunction) {
            invoker.invokeExact(func, self, value);
        } else if (name != null) {
            throw ECMAErrors.typeError("property.has.no.setter", name, ScriptRuntime.safeToString(self));
        }
    }

    private static void invokeNumberSetter(Accessors gs, MethodHandle invoker, String name, Object self, double value) throws Throwable {
        Object func = gs.setter;
        if (func instanceof ScriptFunction) {
            invoker.invokeExact(func, self, value);
        } else if (name != null) {
            throw ECMAErrors.typeError("property.has.no.setter", name, ScriptRuntime.safeToString(self));
        }
    }

    private static MethodHandle findOwnMH_S(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(LOOKUP, UserAccessorProperty.class, name, Lookup.MH.type(rtype, types));
    }

    static final class Accessors {
        Object getter;
        Object setter;

        Accessors(Object getter, Object setter) {
            this.set(getter, setter);
        }

        final void set(Object getter, Object setter) {
            this.getter = getter;
            this.setter = setter;
        }

        public String toString() {
            return "[getter=" + this.getter + " setter=" + this.setter + ']';
        }
    }
}

