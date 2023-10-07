/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import jdk.internal.dynalink.beans.BeansLinker;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.GuardingDynamicLinker;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.internal.dynalink.linker.TypeBasedGuardingDynamicLinker;
import jdk.internal.dynalink.support.CallSiteDescriptorFactory;
import jdk.internal.dynalink.support.LinkRequestImpl;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.objects.NativeArray;
import jdk.nashorn.internal.runtime.AccessorProperty;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.Property;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.arrays.ArrayData;
import jdk.nashorn.internal.runtime.linker.Bootstrap;
import jdk.nashorn.internal.runtime.linker.InvokeByName;
import jdk.nashorn.internal.runtime.linker.NashornBeansLinker;

public final class NativeObject {
    public static final MethodHandle GET__PROTO__ = NativeObject.findOwnMH("get__proto__", ScriptObject.class, Object.class);
    public static final MethodHandle SET__PROTO__ = NativeObject.findOwnMH("set__proto__", Object.class, Object.class, Object.class);
    private static final Object TO_STRING = new Object();
    private static final MethodType MIRROR_GETTER_TYPE = MethodType.methodType(Object.class, ScriptObjectMirror.class);
    private static final MethodType MIRROR_SETTER_TYPE = MethodType.methodType(Object.class, ScriptObjectMirror.class, Object.class);
    private static PropertyMap $nasgenmap$;

    private static InvokeByName getTO_STRING() {
        return Global.instance().getInvokeByName(TO_STRING, new Callable<InvokeByName>(){

            @Override
            public InvokeByName call() {
                return new InvokeByName("toString", ScriptObject.class);
            }
        });
    }

    private static ScriptObject get__proto__(Object self) {
        ScriptObject sobj = Global.checkObject(Global.toObject(self));
        return sobj.getProto();
    }

    private static Object set__proto__(Object self, Object proto) {
        Global.checkObjectCoercible(self);
        if (!(self instanceof ScriptObject)) {
            return ScriptRuntime.UNDEFINED;
        }
        ScriptObject sobj = (ScriptObject)self;
        if (proto == null || proto instanceof ScriptObject) {
            sobj.setPrototypeOf(proto);
        }
        return ScriptRuntime.UNDEFINED;
    }

    private NativeObject() {
        throw new UnsupportedOperationException();
    }

    private static ECMAException notAnObject(Object obj) {
        return ECMAErrors.typeError("not.an.object", ScriptRuntime.safeToString(obj));
    }

    public static ScriptObject setIndexedPropertiesToExternalArrayData(Object self, Object obj, Object buf) {
        Global.checkObject(obj);
        ScriptObject sobj = (ScriptObject)obj;
        if (!(buf instanceof ByteBuffer)) {
            throw ECMAErrors.typeError("not.a.bytebuffer", "setIndexedPropertiesToExternalArrayData's buf argument");
        }
        sobj.setArray(ArrayData.allocate((ByteBuffer)buf));
        return sobj;
    }

    public static Object getPrototypeOf(Object self, Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).getProto();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).getProto();
        }
        JSType type = JSType.of(obj);
        if (type == JSType.OBJECT) {
            return null;
        }
        throw NativeObject.notAnObject(obj);
    }

    public static Object setPrototypeOf(Object self, Object obj, Object proto) {
        if (obj instanceof ScriptObject) {
            ((ScriptObject)obj).setPrototypeOf(proto);
            return obj;
        }
        if (obj instanceof ScriptObjectMirror) {
            ((ScriptObjectMirror)obj).setProto(proto);
            return obj;
        }
        throw NativeObject.notAnObject(obj);
    }

    public static Object getOwnPropertyDescriptor(Object self, Object obj, Object prop) {
        if (obj instanceof ScriptObject) {
            String key = JSType.toString(prop);
            ScriptObject sobj = (ScriptObject)obj;
            return sobj.getOwnPropertyDescriptor(key);
        }
        if (obj instanceof ScriptObjectMirror) {
            String key = JSType.toString(prop);
            ScriptObjectMirror sobjMirror = (ScriptObjectMirror)obj;
            return sobjMirror.getOwnPropertyDescriptor(key);
        }
        throw NativeObject.notAnObject(obj);
    }

    public static ScriptObject getOwnPropertyNames(Object self, Object obj) {
        if (obj instanceof ScriptObject) {
            return new NativeArray(((ScriptObject)obj).getOwnKeys(true));
        }
        if (obj instanceof ScriptObjectMirror) {
            return new NativeArray(((ScriptObjectMirror)obj).getOwnKeys(true));
        }
        throw NativeObject.notAnObject(obj);
    }

    public static ScriptObject create(Object self, Object proto, Object props) {
        if (proto != null) {
            Global.checkObject(proto);
        }
        ScriptObject newObj = Global.newEmptyInstance();
        newObj.setProto((ScriptObject)proto);
        if (props != ScriptRuntime.UNDEFINED) {
            NativeObject.defineProperties(self, newObj, props);
        }
        return newObj;
    }

    public static ScriptObject defineProperty(Object self, Object obj, Object prop, Object attr) {
        ScriptObject sobj = Global.checkObject(obj);
        sobj.defineOwnProperty(JSType.toString(prop), attr, true);
        return sobj;
    }

    public static ScriptObject defineProperties(Object self, Object obj, Object props) {
        ScriptObject sobj = Global.checkObject(obj);
        Object propsObj = Global.toObject(props);
        if (propsObj instanceof ScriptObject) {
            String[] keys2;
            for (String key : keys2 = ((ScriptObject)propsObj).getOwnKeys(false)) {
                String prop = JSType.toString(key);
                sobj.defineOwnProperty(prop, ((ScriptObject)propsObj).get(prop), true);
            }
        }
        return sobj;
    }

    public static Object seal(Object self, Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).seal();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).seal();
        }
        throw NativeObject.notAnObject(obj);
    }

    public static Object freeze(Object self, Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).freeze();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).freeze();
        }
        throw NativeObject.notAnObject(obj);
    }

    public static Object preventExtensions(Object self, Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).preventExtensions();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).preventExtensions();
        }
        throw NativeObject.notAnObject(obj);
    }

    public static boolean isSealed(Object self, Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).isSealed();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).isSealed();
        }
        throw NativeObject.notAnObject(obj);
    }

    public static boolean isFrozen(Object self, Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).isFrozen();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).isFrozen();
        }
        throw NativeObject.notAnObject(obj);
    }

    public static boolean isExtensible(Object self, Object obj) {
        if (obj instanceof ScriptObject) {
            return ((ScriptObject)obj).isExtensible();
        }
        if (obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).isExtensible();
        }
        throw NativeObject.notAnObject(obj);
    }

    public static ScriptObject keys(Object self, Object obj) {
        if (obj instanceof ScriptObject) {
            ScriptObject sobj = (ScriptObject)obj;
            return new NativeArray(sobj.getOwnKeys(false));
        }
        if (obj instanceof ScriptObjectMirror) {
            ScriptObjectMirror sobjMirror = (ScriptObjectMirror)obj;
            return new NativeArray(sobjMirror.getOwnKeys(false));
        }
        throw NativeObject.notAnObject(obj);
    }

    public static Object construct(boolean newObj, Object self, Object value) {
        JSType type = JSType.ofNoFunction(value);
        if (newObj || type == JSType.NULL || type == JSType.UNDEFINED) {
            switch (type) {
                case BOOLEAN: 
                case NUMBER: 
                case STRING: {
                    return Global.toObject(value);
                }
                case OBJECT: {
                    return value;
                }
            }
            return Global.newEmptyInstance();
        }
        return Global.toObject(value);
    }

    public static String toString(Object self) {
        return ScriptRuntime.builtinObjectToString(self);
    }

    public static Object toLocaleString(Object self) {
        Object obj = JSType.toScriptObject(self);
        if (obj instanceof ScriptObject) {
            InvokeByName toStringInvoker = NativeObject.getTO_STRING();
            ScriptObject sobj = (ScriptObject)obj;
            try {
                Object toString2 = toStringInvoker.getGetter().invokeExact(sobj);
                if (Bootstrap.isCallable(toString2)) {
                    return toStringInvoker.getInvoker().invokeExact(toString2, sobj);
                }
            }
            catch (Error | RuntimeException e) {
                throw e;
            }
            catch (Throwable t) {
                throw new RuntimeException(t);
            }
            throw ECMAErrors.typeError("not.a.function", "toString");
        }
        return ScriptRuntime.builtinObjectToString(self);
    }

    public static Object valueOf(Object self) {
        return Global.toObject(self);
    }

    public static boolean hasOwnProperty(Object self, Object v) {
        Object key = JSType.toPrimitive(v, String.class);
        Object obj = Global.toObject(self);
        return obj instanceof ScriptObject && ((ScriptObject)obj).hasOwnProperty(key);
    }

    public static boolean isPrototypeOf(Object self, Object v) {
        if (!(v instanceof ScriptObject)) {
            return false;
        }
        Object obj = Global.toObject(self);
        ScriptObject proto = (ScriptObject)v;
        do {
            if ((proto = proto.getProto()) != obj) continue;
            return true;
        } while (proto != null);
        return false;
    }

    public static boolean propertyIsEnumerable(Object self, Object v) {
        String str = JSType.toString(v);
        Object obj = Global.toObject(self);
        if (obj instanceof ScriptObject) {
            Property property = ((ScriptObject)obj).getMap().findProperty(str);
            return property != null && property.isEnumerable();
        }
        return false;
    }

    public static Object bindProperties(Object self, Object target, Object source) {
        ScriptObject targetObj = Global.checkObject(target);
        Global.checkObjectCoercible(source);
        if (source instanceof ScriptObject) {
            ScriptObject sourceObj = (ScriptObject)source;
            PropertyMap sourceMap = sourceObj.getMap();
            Property[] properties = sourceMap.getProperties();
            ArrayList<Property> propList = new ArrayList<Property>();
            for (Property prop : properties) {
                if (!prop.isEnumerable()) continue;
                Object value = sourceObj.get(prop.getKey());
                prop.setType(Object.class);
                prop.setValue(sourceObj, sourceObj, value, false);
                propList.add(prop);
            }
            if (!propList.isEmpty()) {
                targetObj.addBoundProperties(sourceObj, propList.toArray(new Property[propList.size()]));
            }
        } else if (source instanceof ScriptObjectMirror) {
            ScriptObjectMirror mirror = (ScriptObjectMirror)source;
            String[] keys2 = mirror.getOwnKeys(false);
            if (keys2.length == 0) {
                return target;
            }
            AccessorProperty[] props = new AccessorProperty[keys2.length];
            for (int idx = 0; idx < keys2.length; ++idx) {
                String name = keys2[idx];
                MethodHandle getter = Bootstrap.createDynamicInvoker("dyn:getMethod|getProp|getElem:" + name, MIRROR_GETTER_TYPE);
                MethodHandle setter = Bootstrap.createDynamicInvoker("dyn:setProp|setElem:" + name, MIRROR_SETTER_TYPE);
                props[idx] = AccessorProperty.create(name, 0, getter, setter);
            }
            targetObj.addBoundProperties(source, props);
        } else if (source instanceof StaticClass) {
            Class<?> clazz = ((StaticClass)source).getRepresentedClass();
            Bootstrap.checkReflectionAccess(clazz, true);
            NativeObject.bindBeanProperties(targetObj, source, BeansLinker.getReadableStaticPropertyNames(clazz), BeansLinker.getWritableStaticPropertyNames(clazz), BeansLinker.getStaticMethodNames(clazz));
        } else {
            Class<?> clazz = source.getClass();
            Bootstrap.checkReflectionAccess(clazz, false);
            NativeObject.bindBeanProperties(targetObj, source, BeansLinker.getReadableInstancePropertyNames(clazz), BeansLinker.getWritableInstancePropertyNames(clazz), BeansLinker.getInstanceMethodNames(clazz));
        }
        return target;
    }

    public static Object bindAllProperties(ScriptObject target, ScriptObjectMirror source) {
        Set<String> keys2 = source.keySet();
        AccessorProperty[] props = new AccessorProperty[keys2.size()];
        int idx = 0;
        for (String name : keys2) {
            MethodHandle getter = Bootstrap.createDynamicInvoker("dyn:getMethod|getProp|getElem:" + name, MIRROR_GETTER_TYPE);
            MethodHandle setter = Bootstrap.createDynamicInvoker("dyn:setProp|setElem:" + name, MIRROR_SETTER_TYPE);
            props[idx] = AccessorProperty.create(name, 0, getter, setter);
            ++idx;
        }
        target.addBoundProperties(source, props);
        return target;
    }

    private static void bindBeanProperties(ScriptObject targetObj, Object source, Collection<String> readablePropertyNames, Collection<String> writablePropertyNames, Collection<String> methodNames) {
        HashSet<String> propertyNames = new HashSet<String>(readablePropertyNames);
        propertyNames.addAll(writablePropertyNames);
        Class<?> clazz = source.getClass();
        MethodType getterType = MethodType.methodType(Object.class, clazz);
        MethodType setterType = MethodType.methodType(Object.class, clazz, Object.class);
        TypeBasedGuardingDynamicLinker linker = BeansLinker.getLinkerForClass(clazz);
        ArrayList<AccessorProperty> properties = new ArrayList<AccessorProperty>(propertyNames.size() + methodNames.size());
        for (String methodName : methodNames) {
            MethodHandle method;
            try {
                method = NativeObject.getBeanOperation(linker, "dyn:getMethod:" + methodName, getterType, source);
            }
            catch (IllegalAccessError e) {
                continue;
            }
            properties.add(AccessorProperty.create(methodName, 1, NativeObject.getBoundBeanMethodGetter(source, method), Lookup.EMPTY_SETTER));
        }
        for (String propertyName : propertyNames) {
            MethodHandle setter;
            boolean isWritable;
            MethodHandle getter;
            if (readablePropertyNames.contains(propertyName)) {
                try {
                    getter = NativeObject.getBeanOperation(linker, "dyn:getProp:" + propertyName, getterType, source);
                }
                catch (IllegalAccessError e) {
                    getter = Lookup.EMPTY_GETTER;
                }
            } else {
                getter = Lookup.EMPTY_GETTER;
            }
            if (isWritable = writablePropertyNames.contains(propertyName)) {
                try {
                    setter = NativeObject.getBeanOperation(linker, "dyn:setProp:" + propertyName, setterType, source);
                }
                catch (IllegalAccessError e) {
                    setter = Lookup.EMPTY_SETTER;
                }
            } else {
                setter = Lookup.EMPTY_SETTER;
            }
            if (getter == Lookup.EMPTY_GETTER && setter == Lookup.EMPTY_SETTER) continue;
            properties.add(AccessorProperty.create(propertyName, isWritable ? 0 : 1, getter, setter));
        }
        targetObj.addBoundProperties(source, properties.toArray(new AccessorProperty[properties.size()]));
    }

    private static MethodHandle getBoundBeanMethodGetter(Object source, MethodHandle methodGetter) {
        try {
            return MethodHandles.dropArguments(MethodHandles.constant(Object.class, Bootstrap.bindCallable(methodGetter.invoke(source), source, null)), 0, new Class[]{Object.class});
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private static MethodHandle getBeanOperation(GuardingDynamicLinker linker, String operation, MethodType methodType, Object source) {
        GuardedInvocation inv;
        try {
            inv = NashornBeansLinker.getGuardedInvocation(linker, NativeObject.createLinkRequest(operation, methodType, source), Bootstrap.getLinkerServices());
            assert (NativeObject.passesGuard(source, inv.getGuard()));
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
        assert (inv.getSwitchPoints() == null);
        return inv.getInvocation();
    }

    private static boolean passesGuard(Object obj, MethodHandle guard) throws Throwable {
        return guard == null || guard.invoke(obj);
    }

    private static LinkRequest createLinkRequest(String operation, MethodType methodType, Object source) {
        return new LinkRequestImpl(CallSiteDescriptorFactory.create(MethodHandles.publicLookup(), operation, methodType), null, 0, false, source);
    }

    private static MethodHandle findOwnMH(String name, Class<?> rtype, Class<?> ... types) {
        return Lookup.MH.findStatic(MethodHandles.lookup(), NativeObject.class, name, Lookup.MH.type(rtype, types));
    }

    static {
        NativeObject.$clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}

