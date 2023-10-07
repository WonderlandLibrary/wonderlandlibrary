/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.FindProperty;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;
import jdk.nashorn.internal.scripts.JO;

public final class NativeJSAdapter
extends ScriptObject {
    public static final String __get__ = "__get__";
    public static final String __put__ = "__put__";
    public static final String __call__ = "__call__";
    public static final String __new__ = "__new__";
    public static final String __getIds__ = "__getIds__";
    public static final String __getKeys__ = "__getKeys__";
    public static final String __getValues__ = "__getValues__";
    public static final String __has__ = "__has__";
    public static final String __delete__ = "__delete__";
    public static final String __preventExtensions__ = "__preventExtensions__";
    public static final String __isExtensible__ = "__isExtensible__";
    public static final String __seal__ = "__seal__";
    public static final String __isSealed__ = "__isSealed__";
    public static final String __freeze__ = "__freeze__";
    public static final String __isFrozen__ = "__isFrozen__";
    private final ScriptObject adaptee;
    private final boolean overrides;
    private static final MethodHandle IS_JSADAPTOR = NativeJSAdapter.findOwnMH("isJSAdaptor", Boolean.TYPE, Object.class, Object.class, MethodHandle.class, Object.class, ScriptFunction.class);
    private static PropertyMap $nasgenmap$;

    NativeJSAdapter(Object overrides, ScriptObject adaptee, ScriptObject proto, PropertyMap map) {
        super(proto, map);
        this.adaptee = NativeJSAdapter.wrapAdaptee(adaptee);
        if (overrides instanceof ScriptObject) {
            this.overrides = true;
            ScriptObject sobj = (ScriptObject)overrides;
            this.addBoundProperties(sobj);
        } else {
            this.overrides = false;
        }
    }

    private static ScriptObject wrapAdaptee(ScriptObject adaptee) {
        return new JO(adaptee);
    }

    @Override
    public String getClassName() {
        return "JSAdapter";
    }

    @Override
    public int getInt(Object key, int programPoint) {
        return this.overrides && super.hasOwnProperty(key) ? super.getInt(key, programPoint) : this.callAdapteeInt(programPoint, __get__, key);
    }

    @Override
    public int getInt(double key, int programPoint) {
        return this.overrides && super.hasOwnProperty(key) ? super.getInt(key, programPoint) : this.callAdapteeInt(programPoint, __get__, key);
    }

    @Override
    public int getInt(int key, int programPoint) {
        return this.overrides && super.hasOwnProperty(key) ? super.getInt(key, programPoint) : this.callAdapteeInt(programPoint, __get__, key);
    }

    @Override
    public double getDouble(Object key, int programPoint) {
        return this.overrides && super.hasOwnProperty(key) ? super.getDouble(key, programPoint) : this.callAdapteeDouble(programPoint, __get__, key);
    }

    @Override
    public double getDouble(double key, int programPoint) {
        return this.overrides && super.hasOwnProperty(key) ? super.getDouble(key, programPoint) : this.callAdapteeDouble(programPoint, __get__, key);
    }

    @Override
    public double getDouble(int key, int programPoint) {
        return this.overrides && super.hasOwnProperty(key) ? super.getDouble(key, programPoint) : this.callAdapteeDouble(programPoint, __get__, key);
    }

    @Override
    public Object get(Object key) {
        return this.overrides && super.hasOwnProperty(key) ? super.get(key) : this.callAdaptee(__get__, key);
    }

    @Override
    public Object get(double key) {
        return this.overrides && super.hasOwnProperty(key) ? super.get(key) : this.callAdaptee(__get__, key);
    }

    @Override
    public Object get(int key) {
        return this.overrides && super.hasOwnProperty(key) ? super.get(key) : this.callAdaptee(__get__, key);
    }

    @Override
    public void set(Object key, int value, int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        } else {
            this.callAdaptee(__put__, key, value, flags);
        }
    }

    @Override
    public void set(Object key, double value, int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        } else {
            this.callAdaptee(__put__, key, value, flags);
        }
    }

    @Override
    public void set(Object key, Object value, int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        } else {
            this.callAdaptee(__put__, key, value, flags);
        }
    }

    @Override
    public void set(double key, int value, int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        } else {
            this.callAdaptee(__put__, key, value, flags);
        }
    }

    @Override
    public void set(double key, double value, int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        } else {
            this.callAdaptee(__put__, key, value, flags);
        }
    }

    @Override
    public void set(double key, Object value, int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        } else {
            this.callAdaptee(__put__, key, value, flags);
        }
    }

    @Override
    public void set(int key, int value, int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        } else {
            this.callAdaptee(__put__, key, value, flags);
        }
    }

    @Override
    public void set(int key, double value, int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        } else {
            this.callAdaptee(__put__, key, value, flags);
        }
    }

    @Override
    public void set(int key, Object value, int flags) {
        if (this.overrides && super.hasOwnProperty(key)) {
            super.set(key, value, flags);
        } else {
            this.callAdaptee(__put__, key, value, flags);
        }
    }

    @Override
    public boolean has(Object key) {
        if (this.overrides && super.hasOwnProperty(key)) {
            return true;
        }
        return JSType.toBoolean(this.callAdaptee(Boolean.FALSE, __has__, key));
    }

    @Override
    public boolean has(int key) {
        if (this.overrides && super.hasOwnProperty(key)) {
            return true;
        }
        return JSType.toBoolean(this.callAdaptee(Boolean.FALSE, __has__, key));
    }

    @Override
    public boolean has(double key) {
        if (this.overrides && super.hasOwnProperty(key)) {
            return true;
        }
        return JSType.toBoolean(this.callAdaptee(Boolean.FALSE, __has__, key));
    }

    @Override
    public boolean delete(int key, boolean strict) {
        if (this.overrides && super.hasOwnProperty(key)) {
            return super.delete(key, strict);
        }
        return JSType.toBoolean(this.callAdaptee(Boolean.TRUE, __delete__, key, strict));
    }

    @Override
    public boolean delete(double key, boolean strict) {
        if (this.overrides && super.hasOwnProperty(key)) {
            return super.delete(key, strict);
        }
        return JSType.toBoolean(this.callAdaptee(Boolean.TRUE, __delete__, key, strict));
    }

    @Override
    public boolean delete(Object key, boolean strict) {
        if (this.overrides && super.hasOwnProperty(key)) {
            return super.delete(key, strict);
        }
        return JSType.toBoolean(this.callAdaptee(Boolean.TRUE, __delete__, key, strict));
    }

    @Override
    public Iterator<String> propertyIterator() {
        Object func = this.adaptee.get(__getIds__);
        if (!(func instanceof ScriptFunction)) {
            func = this.adaptee.get(__getKeys__);
        }
        Object obj = func instanceof ScriptFunction ? ScriptRuntime.apply((ScriptFunction)func, this.adaptee, new Object[0]) : new NativeArray(0L);
        ArrayList<String> array = new ArrayList<String>();
        ArrayLikeIterator<Object> iter = ArrayLikeIterator.arrayLikeIterator(obj);
        while (iter.hasNext()) {
            array.add((String)iter.next());
        }
        return array.iterator();
    }

    @Override
    public Iterator<Object> valueIterator() {
        Object obj = this.callAdaptee(new NativeArray(0L), __getValues__, new Object[0]);
        return ArrayLikeIterator.arrayLikeIterator(obj);
    }

    @Override
    public ScriptObject preventExtensions() {
        this.callAdaptee(__preventExtensions__, new Object[0]);
        return this;
    }

    @Override
    public boolean isExtensible() {
        return JSType.toBoolean(this.callAdaptee(Boolean.TRUE, __isExtensible__, new Object[0]));
    }

    @Override
    public ScriptObject seal() {
        this.callAdaptee(__seal__, new Object[0]);
        return this;
    }

    @Override
    public boolean isSealed() {
        return JSType.toBoolean(this.callAdaptee(Boolean.FALSE, __isSealed__, new Object[0]));
    }

    @Override
    public ScriptObject freeze() {
        this.callAdaptee(__freeze__, new Object[0]);
        return this;
    }

    @Override
    public boolean isFrozen() {
        return JSType.toBoolean(this.callAdaptee(Boolean.FALSE, __isFrozen__, new Object[0]));
    }

    public static NativeJSAdapter construct(boolean isNew, Object self, Object ... args2) {
        Object adaptee;
        Object proto = ScriptRuntime.UNDEFINED;
        Object overrides = ScriptRuntime.UNDEFINED;
        if (args2 == null || args2.length == 0) {
            throw ECMAErrors.typeError("not.an.object", "null");
        }
        switch (args2.length) {
            case 1: {
                adaptee = args2[0];
                break;
            }
            case 2: {
                overrides = args2[0];
                adaptee = args2[1];
                break;
            }
            default: {
                proto = args2[0];
                overrides = args2[1];
                adaptee = args2[2];
            }
        }
        if (!(adaptee instanceof ScriptObject)) {
            throw ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(adaptee));
        }
        Global global = Global.instance();
        if (proto != null && !(proto instanceof ScriptObject)) {
            proto = global.getJSAdapterPrototype();
        }
        return new NativeJSAdapter(overrides, (ScriptObject)adaptee, (ScriptObject)proto, $nasgenmap$);
    }

    @Override
    protected GuardedInvocation findNewMethod(CallSiteDescriptor desc, LinkRequest request) {
        return this.findHook(desc, __new__, false);
    }

    @Override
    protected GuardedInvocation findCallMethodMethod(CallSiteDescriptor desc, LinkRequest request) {
        if (this.overrides && super.hasOwnProperty(desc.getNameToken(2))) {
            try {
                GuardedInvocation inv = super.findCallMethodMethod(desc, request);
                if (inv != null) {
                    return inv;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return this.findHook(desc, __call__);
    }

    @Override
    protected GuardedInvocation findGetMethod(CallSiteDescriptor desc, LinkRequest request, String operation) {
        String name = desc.getNameToken(2);
        if (this.overrides && super.hasOwnProperty(name)) {
            try {
                GuardedInvocation inv = super.findGetMethod(desc, request, operation);
                if (inv != null) {
                    return inv;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        switch (operation) {
            case "getProp": 
            case "getElem": {
                return this.findHook(desc, __get__);
            }
            case "getMethod": {
                Object value;
                FindProperty find = this.adaptee.findProperty(__call__, true);
                if (find != null && (value = find.getObjectValue()) instanceof ScriptFunction) {
                    ScriptFunction func = (ScriptFunction)value;
                    return new GuardedInvocation(Lookup.MH.dropArguments(Lookup.MH.constant(Object.class, func.createBound(this, new Object[]{name})), 0, Object.class), NativeJSAdapter.testJSAdaptor(this.adaptee, null, null, null), this.adaptee.getProtoSwitchPoints(__call__, find.getOwner()), null);
                }
                throw ECMAErrors.typeError("no.such.function", desc.getNameToken(2), ScriptRuntime.safeToString(this));
            }
        }
        throw new AssertionError((Object)"should not reach here");
    }

    @Override
    protected GuardedInvocation findSetMethod(CallSiteDescriptor desc, LinkRequest request) {
        if (this.overrides && super.hasOwnProperty(desc.getNameToken(2))) {
            try {
                GuardedInvocation inv = super.findSetMethod(desc, request);
                if (inv != null) {
                    return inv;
                }
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        return this.findHook(desc, __put__);
    }

    private Object callAdaptee(String name, Object ... args2) {
        return this.callAdaptee(ScriptRuntime.UNDEFINED, name, args2);
    }

    private double callAdapteeDouble(int programPoint, String name, Object ... args2) {
        return JSType.toNumberMaybeOptimistic(this.callAdaptee(name, args2), programPoint);
    }

    private int callAdapteeInt(int programPoint, String name, Object ... args2) {
        return JSType.toInt32MaybeOptimistic(this.callAdaptee(name, args2), programPoint);
    }

    private Object callAdaptee(Object retValue, String name, Object ... args2) {
        Object func = this.adaptee.get(name);
        if (func instanceof ScriptFunction) {
            return ScriptRuntime.apply((ScriptFunction)func, this.adaptee, args2);
        }
        return retValue;
    }

    private GuardedInvocation findHook(CallSiteDescriptor desc, String hook) {
        return this.findHook(desc, hook, true);
    }

    private GuardedInvocation findHook(CallSiteDescriptor desc, String hook, boolean useName) {
        FindProperty findData = this.adaptee.findProperty(hook, true);
        MethodType type = desc.getMethodType();
        if (findData != null) {
            String name = desc.getNameTokenCount() > 2 ? desc.getNameToken(2) : null;
            Object value = findData.getObjectValue();
            if (value instanceof ScriptFunction) {
                ScriptFunction func = (ScriptFunction)value;
                MethodHandle methodHandle = this.getCallMethodHandle(findData, type, useName ? name : null);
                if (methodHandle != null) {
                    return new GuardedInvocation(methodHandle, NativeJSAdapter.testJSAdaptor(this.adaptee, findData.getGetter(Object.class, -1, null), findData.getOwner(), func), this.adaptee.getProtoSwitchPoints(hook, findData.getOwner()), null);
                }
            }
        }
        switch (hook) {
            case "__call__": {
                throw ECMAErrors.typeError("no.such.function", desc.getNameToken(2), ScriptRuntime.safeToString(this));
            }
        }
        MethodHandle methodHandle = hook.equals(__put__) ? Lookup.MH.asType(Lookup.EMPTY_SETTER, type) : Lookup.emptyGetter(type.returnType());
        return new GuardedInvocation(methodHandle, NativeJSAdapter.testJSAdaptor(this.adaptee, null, null, null), this.adaptee.getProtoSwitchPoints(hook, null), null);
    }

    private static MethodHandle testJSAdaptor(Object adaptee, MethodHandle getter, Object where, ScriptFunction func) {
        return Lookup.MH.insertArguments(IS_JSADAPTOR, 1, adaptee, getter, where, func);
    }

    private static boolean isJSAdaptor(Object self, Object adaptee, MethodHandle getter, Object where, ScriptFunction func) {
        boolean res;
        boolean bl = res = self instanceof NativeJSAdapter && ((NativeJSAdapter)self).getAdaptee() == adaptee;
        if (res && getter != null) {
            try {
                return getter.invokeExact(where) == func;
            }
            catch (Error | RuntimeException e) {
                throw e;
            }
            catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        return res;
    }

    public ScriptObject getAdaptee() {
        return this.adaptee;
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NativeJSAdapter.class, name, Lookup.MH.type(rtype, types));
    }

    static {
        NativeJSAdapter.$clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}

