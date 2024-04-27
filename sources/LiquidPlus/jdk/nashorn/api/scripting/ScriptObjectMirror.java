/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.api.scripting;

import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import javax.script.Bindings;
import jdk.Exported;
import jdk.nashorn.api.scripting.AbstractJSObject;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.ConsString;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ECMAException;
import jdk.nashorn.internal.runtime.JSONListAdapter;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.arrays.ArrayData;

@Exported
public final class ScriptObjectMirror
extends AbstractJSObject
implements Bindings {
    private static final AccessControlContext GET_CONTEXT_ACC_CTXT = ScriptObjectMirror.getContextAccCtxt();
    private final ScriptObject sobj;
    private final Global global;
    private final boolean strict;
    private final boolean jsonCompatible;

    private static AccessControlContext getContextAccCtxt() {
        Permissions perms = new Permissions();
        perms.add(new RuntimePermission("nashorn.getContext"));
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, perms)});
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ScriptObjectMirror) {
            return this.sobj.equals(((ScriptObjectMirror)other).sobj);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.sobj.hashCode();
    }

    public String toString() {
        return this.inGlobal(new Callable<String>(){

            @Override
            public String call() {
                return ScriptRuntime.safeToString(ScriptObjectMirror.this.sobj);
            }
        });
    }

    @Override
    public Object call(Object thiz, Object ... args2) {
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != this.global;
        try {
            if (globalChanged) {
                Context.setGlobal(this.global);
            }
            if (this.sobj instanceof ScriptFunction) {
                Object[] modArgs = globalChanged ? this.wrapArrayLikeMe(args2, oldGlobal) : args2;
                Object self = globalChanged ? this.wrapLikeMe(thiz, oldGlobal) : thiz;
                Object object = this.wrapLikeMe(ScriptRuntime.apply((ScriptFunction)this.sobj, ScriptObjectMirror.unwrap(self, this.global), ScriptObjectMirror.unwrapArray(modArgs, this.global)));
                return object;
            }
            try {
                throw new RuntimeException("not a function: " + this.toString());
            }
            catch (NashornException ne) {
                throw ne.initEcmaError(this.global);
            }
            catch (Error | RuntimeException e) {
                throw e;
            }
            catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }

    @Override
    public Object newObject(Object ... args2) {
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != this.global;
        try {
            if (globalChanged) {
                Context.setGlobal(this.global);
            }
            if (this.sobj instanceof ScriptFunction) {
                Object[] modArgs = globalChanged ? this.wrapArrayLikeMe(args2, oldGlobal) : args2;
                Object object = this.wrapLikeMe(ScriptRuntime.construct((ScriptFunction)this.sobj, ScriptObjectMirror.unwrapArray(modArgs, this.global)));
                return object;
            }
            try {
                throw new RuntimeException("not a constructor: " + this.toString());
            }
            catch (NashornException ne) {
                throw ne.initEcmaError(this.global);
            }
            catch (Error | RuntimeException e) {
                throw e;
            }
            catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }

    @Override
    public Object eval(final String s) {
        return this.inGlobal(new Callable<Object>(){

            @Override
            public Object call() {
                Context context = AccessController.doPrivileged(new PrivilegedAction<Context>(){

                    @Override
                    public Context run() {
                        return Context.getContext();
                    }
                }, GET_CONTEXT_ACC_CTXT);
                return ScriptObjectMirror.this.wrapLikeMe(context.eval(ScriptObjectMirror.this.global, s, ScriptObjectMirror.this.sobj, null));
            }
        });
    }

    public Object callMember(String functionName, Object ... args2) {
        Objects.requireNonNull(functionName);
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != this.global;
        try {
            Object val;
            if (globalChanged) {
                Context.setGlobal(this.global);
            }
            if ((val = this.sobj.get(functionName)) instanceof ScriptFunction) {
                Object[] modArgs = globalChanged ? this.wrapArrayLikeMe(args2, oldGlobal) : args2;
                Object object = this.wrapLikeMe(ScriptRuntime.apply((ScriptFunction)val, this.sobj, ScriptObjectMirror.unwrapArray(modArgs, this.global)));
                return object;
            }
            if (val instanceof JSObject && ((JSObject)val).isFunction()) {
                Object object = ((JSObject)val).call(this.sobj, args2);
                return object;
            }
            try {
                throw new NoSuchMethodException("No such function " + functionName);
            }
            catch (NashornException ne) {
                throw ne.initEcmaError(this.global);
            }
            catch (Error | RuntimeException e) {
                throw e;
            }
            catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }

    @Override
    public Object getMember(final String name) {
        Objects.requireNonNull(name);
        return this.inGlobal(new Callable<Object>(){

            @Override
            public Object call() {
                return ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.get(name));
            }
        });
    }

    @Override
    public Object getSlot(final int index) {
        return this.inGlobal(new Callable<Object>(){

            @Override
            public Object call() {
                return ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.get(index));
            }
        });
    }

    @Override
    public boolean hasMember(final String name) {
        Objects.requireNonNull(name);
        return this.inGlobal(new Callable<Boolean>(){

            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.has(name);
            }
        });
    }

    @Override
    public boolean hasSlot(final int slot) {
        return this.inGlobal(new Callable<Boolean>(){

            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.has(slot);
            }
        });
    }

    @Override
    public void removeMember(String name) {
        this.remove(Objects.requireNonNull(name));
    }

    @Override
    public void setMember(String name, Object value) {
        this.put(Objects.requireNonNull(name), value);
    }

    @Override
    public void setSlot(final int index, final Object value) {
        this.inGlobal(new Callable<Void>(){

            @Override
            public Void call() {
                ScriptObjectMirror.this.sobj.set(index, ScriptObjectMirror.unwrap(value, ScriptObjectMirror.this.global), ScriptObjectMirror.this.getCallSiteFlags());
                return null;
            }
        });
    }

    public void setIndexedPropertiesToExternalArrayData(final ByteBuffer buf) {
        this.inGlobal(new Callable<Void>(){

            @Override
            public Void call() {
                ScriptObjectMirror.this.sobj.setArray(ArrayData.allocate(buf));
                return null;
            }
        });
    }

    @Override
    public boolean isInstance(Object instance) {
        if (!(instance instanceof ScriptObjectMirror)) {
            return false;
        }
        final ScriptObjectMirror mirror = (ScriptObjectMirror)instance;
        if (this.global != mirror.global) {
            return false;
        }
        return this.inGlobal(new Callable<Boolean>(){

            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.isInstance(mirror.sobj);
            }
        });
    }

    @Override
    public String getClassName() {
        return this.sobj.getClassName();
    }

    @Override
    public boolean isFunction() {
        return this.sobj instanceof ScriptFunction;
    }

    @Override
    public boolean isStrictFunction() {
        return this.isFunction() && ((ScriptFunction)this.sobj).isStrict();
    }

    @Override
    public boolean isArray() {
        return this.sobj.isArray();
    }

    @Override
    public void clear() {
        this.inGlobal(new Callable<Object>(){

            @Override
            public Object call() {
                ScriptObjectMirror.this.sobj.clear(ScriptObjectMirror.this.strict);
                return null;
            }
        });
    }

    @Override
    public boolean containsKey(final Object key) {
        ScriptObjectMirror.checkKey(key);
        return this.inGlobal(new Callable<Boolean>(){

            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.containsKey(key);
            }
        });
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.inGlobal(new Callable<Boolean>(){

            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.containsValue(ScriptObjectMirror.unwrap(value, ScriptObjectMirror.this.global));
            }
        });
    }

    @Override
    public Set<Map.Entry<String, Object>> entrySet() {
        return this.inGlobal(new Callable<Set<Map.Entry<String, Object>>>(){

            @Override
            public Set<Map.Entry<String, Object>> call() {
                Iterator<String> iter = ScriptObjectMirror.this.sobj.propertyIterator();
                LinkedHashSet<AbstractMap.SimpleImmutableEntry<String, Object>> entries = new LinkedHashSet<AbstractMap.SimpleImmutableEntry<String, Object>>();
                while (iter.hasNext()) {
                    String key = iter.next();
                    Object value = ScriptObjectMirror.translateUndefined(ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.get(key)));
                    entries.add(new AbstractMap.SimpleImmutableEntry<String, Object>(key, value));
                }
                return Collections.unmodifiableSet(entries);
            }
        });
    }

    @Override
    public Object get(final Object key) {
        ScriptObjectMirror.checkKey(key);
        return this.inGlobal(new Callable<Object>(){

            @Override
            public Object call() {
                return ScriptObjectMirror.translateUndefined(ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.get(key)));
            }
        });
    }

    @Override
    public boolean isEmpty() {
        return this.inGlobal(new Callable<Boolean>(){

            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.isEmpty();
            }
        });
    }

    @Override
    public Set<String> keySet() {
        return this.inGlobal(new Callable<Set<String>>(){

            @Override
            public Set<String> call() {
                Iterator<String> iter = ScriptObjectMirror.this.sobj.propertyIterator();
                LinkedHashSet<String> keySet = new LinkedHashSet<String>();
                while (iter.hasNext()) {
                    keySet.add(iter.next());
                }
                return Collections.unmodifiableSet(keySet);
            }
        });
    }

    @Override
    public Object put(final String key, final Object value) {
        ScriptObjectMirror.checkKey(key);
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != this.global;
        return this.inGlobal(new Callable<Object>(){

            @Override
            public Object call() {
                Object modValue = globalChanged ? ScriptObjectMirror.this.wrapLikeMe(value, oldGlobal) : value;
                return ScriptObjectMirror.translateUndefined(ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.put(key, ScriptObjectMirror.unwrap(modValue, ScriptObjectMirror.this.global), ScriptObjectMirror.this.strict)));
            }
        });
    }

    @Override
    public void putAll(final Map<? extends String, ? extends Object> map) {
        Objects.requireNonNull(map);
        final Global oldGlobal = Context.getGlobal();
        final boolean globalChanged = oldGlobal != this.global;
        this.inGlobal(new Callable<Object>(){

            @Override
            public Object call() {
                for (Map.Entry entry : map.entrySet()) {
                    Object value = entry.getValue();
                    Object modValue = globalChanged ? ScriptObjectMirror.this.wrapLikeMe(value, oldGlobal) : value;
                    String key = (String)entry.getKey();
                    ScriptObjectMirror.checkKey(key);
                    ScriptObjectMirror.this.sobj.set((Object)key, ScriptObjectMirror.unwrap(modValue, ScriptObjectMirror.this.global), ScriptObjectMirror.this.getCallSiteFlags());
                }
                return null;
            }
        });
    }

    @Override
    public Object remove(final Object key) {
        ScriptObjectMirror.checkKey(key);
        return this.inGlobal(new Callable<Object>(){

            @Override
            public Object call() {
                return ScriptObjectMirror.translateUndefined(ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.remove(key, ScriptObjectMirror.this.strict)));
            }
        });
    }

    public boolean delete(final Object key) {
        return this.inGlobal(new Callable<Boolean>(){

            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.delete(ScriptObjectMirror.unwrap(key, ScriptObjectMirror.this.global), ScriptObjectMirror.this.strict);
            }
        });
    }

    @Override
    public int size() {
        return this.inGlobal(new Callable<Integer>(){

            @Override
            public Integer call() {
                return ScriptObjectMirror.this.sobj.size();
            }
        });
    }

    @Override
    public Collection<Object> values() {
        return this.inGlobal(new Callable<Collection<Object>>(){

            @Override
            public Collection<Object> call() {
                ArrayList<Object> values2 = new ArrayList<Object>(ScriptObjectMirror.this.size());
                Iterator<Object> iter = ScriptObjectMirror.this.sobj.valueIterator();
                while (iter.hasNext()) {
                    values2.add(ScriptObjectMirror.translateUndefined(ScriptObjectMirror.this.wrapLikeMe(iter.next())));
                }
                return Collections.unmodifiableList(values2);
            }
        });
    }

    public Object getProto() {
        return this.inGlobal(new Callable<Object>(){

            @Override
            public Object call() {
                return ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.getProto());
            }
        });
    }

    public void setProto(final Object proto) {
        this.inGlobal(new Callable<Void>(){

            @Override
            public Void call() {
                ScriptObjectMirror.this.sobj.setPrototypeOf(ScriptObjectMirror.unwrap(proto, ScriptObjectMirror.this.global));
                return null;
            }
        });
    }

    public Object getOwnPropertyDescriptor(final String key) {
        return this.inGlobal(new Callable<Object>(){

            @Override
            public Object call() {
                return ScriptObjectMirror.this.wrapLikeMe(ScriptObjectMirror.this.sobj.getOwnPropertyDescriptor(key));
            }
        });
    }

    public String[] getOwnKeys(final boolean all) {
        return this.inGlobal(new Callable<String[]>(){

            @Override
            public String[] call() {
                return ScriptObjectMirror.this.sobj.getOwnKeys(all);
            }
        });
    }

    public ScriptObjectMirror preventExtensions() {
        return this.inGlobal(new Callable<ScriptObjectMirror>(){

            @Override
            public ScriptObjectMirror call() {
                ScriptObjectMirror.this.sobj.preventExtensions();
                return ScriptObjectMirror.this;
            }
        });
    }

    public boolean isExtensible() {
        return this.inGlobal(new Callable<Boolean>(){

            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.isExtensible();
            }
        });
    }

    public ScriptObjectMirror seal() {
        return this.inGlobal(new Callable<ScriptObjectMirror>(){

            @Override
            public ScriptObjectMirror call() {
                ScriptObjectMirror.this.sobj.seal();
                return ScriptObjectMirror.this;
            }
        });
    }

    public boolean isSealed() {
        return this.inGlobal(new Callable<Boolean>(){

            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.isSealed();
            }
        });
    }

    public ScriptObjectMirror freeze() {
        return this.inGlobal(new Callable<ScriptObjectMirror>(){

            @Override
            public ScriptObjectMirror call() {
                ScriptObjectMirror.this.sobj.freeze();
                return ScriptObjectMirror.this;
            }
        });
    }

    public boolean isFrozen() {
        return this.inGlobal(new Callable<Boolean>(){

            @Override
            public Boolean call() {
                return ScriptObjectMirror.this.sobj.isFrozen();
            }
        });
    }

    public static boolean isUndefined(Object obj) {
        return obj == ScriptRuntime.UNDEFINED;
    }

    public <T> T to(final Class<T> type) {
        return (T)this.inGlobal(new Callable<T>(){

            @Override
            public T call() {
                return type.cast(ScriptUtils.convert(ScriptObjectMirror.this.sobj, type));
            }
        });
    }

    public static Object wrap(Object obj, Object homeGlobal) {
        return ScriptObjectMirror.wrap(obj, homeGlobal, false);
    }

    public static Object wrapAsJSONCompatible(Object obj, Object homeGlobal) {
        return ScriptObjectMirror.wrap(obj, homeGlobal, true);
    }

    private static Object wrap(Object obj, Object homeGlobal, boolean jsonCompatible) {
        if (obj instanceof ScriptObject) {
            if (!(homeGlobal instanceof Global)) {
                return obj;
            }
            ScriptObject sobj = (ScriptObject)obj;
            Global global = (Global)homeGlobal;
            ScriptObjectMirror mirror = new ScriptObjectMirror(sobj, global, jsonCompatible);
            if (jsonCompatible && sobj.isArray()) {
                return new JSONListAdapter(mirror, global);
            }
            return mirror;
        }
        if (obj instanceof ConsString) {
            return obj.toString();
        }
        if (jsonCompatible && obj instanceof ScriptObjectMirror) {
            return ((ScriptObjectMirror)obj).asJSONCompatible();
        }
        return obj;
    }

    private Object wrapLikeMe(Object obj, Object homeGlobal) {
        return ScriptObjectMirror.wrap(obj, homeGlobal, this.jsonCompatible);
    }

    private Object wrapLikeMe(Object obj) {
        return this.wrapLikeMe(obj, this.global);
    }

    public static Object unwrap(Object obj, Object homeGlobal) {
        if (obj instanceof ScriptObjectMirror) {
            ScriptObjectMirror mirror = (ScriptObjectMirror)obj;
            return mirror.global == homeGlobal ? mirror.sobj : obj;
        }
        if (obj instanceof JSONListAdapter) {
            return ((JSONListAdapter)obj).unwrap(homeGlobal);
        }
        return obj;
    }

    public static Object[] wrapArray(Object[] args2, Object homeGlobal) {
        return ScriptObjectMirror.wrapArray(args2, homeGlobal, false);
    }

    private static Object[] wrapArray(Object[] args2, Object homeGlobal, boolean jsonCompatible) {
        if (args2 == null || args2.length == 0) {
            return args2;
        }
        Object[] newArgs = new Object[args2.length];
        int index = 0;
        for (Object obj : args2) {
            newArgs[index] = ScriptObjectMirror.wrap(obj, homeGlobal, jsonCompatible);
            ++index;
        }
        return newArgs;
    }

    private Object[] wrapArrayLikeMe(Object[] args2, Object homeGlobal) {
        return ScriptObjectMirror.wrapArray(args2, homeGlobal, this.jsonCompatible);
    }

    public static Object[] unwrapArray(Object[] args2, Object homeGlobal) {
        if (args2 == null || args2.length == 0) {
            return args2;
        }
        Object[] newArgs = new Object[args2.length];
        int index = 0;
        for (Object obj : args2) {
            newArgs[index] = ScriptObjectMirror.unwrap(obj, homeGlobal);
            ++index;
        }
        return newArgs;
    }

    public static boolean identical(Object obj1, Object obj2) {
        Object o1 = obj1 instanceof ScriptObjectMirror ? ((ScriptObjectMirror)obj1).sobj : obj1;
        Object o2 = obj2 instanceof ScriptObjectMirror ? ((ScriptObjectMirror)obj2).sobj : obj2;
        return o1 == o2;
    }

    ScriptObjectMirror(ScriptObject sobj, Global global) {
        this(sobj, global, false);
    }

    private ScriptObjectMirror(ScriptObject sobj, Global global, boolean jsonCompatible) {
        assert (sobj != null) : "ScriptObjectMirror on null!";
        assert (global != null) : "home Global is null";
        this.sobj = sobj;
        this.global = global;
        this.strict = global.isStrictContext();
        this.jsonCompatible = jsonCompatible;
    }

    ScriptObject getScriptObject() {
        return this.sobj;
    }

    Global getHomeGlobal() {
        return this.global;
    }

    static Object translateUndefined(Object obj) {
        return obj == ScriptRuntime.UNDEFINED ? null : obj;
    }

    private int getCallSiteFlags() {
        return this.strict ? 2 : 0;
    }

    private <V> V inGlobal(Callable<V> callable) {
        boolean globalChanged;
        Global oldGlobal = Context.getGlobal();
        boolean bl = globalChanged = oldGlobal != this.global;
        if (globalChanged) {
            Context.setGlobal(this.global);
        }
        try {
            V v = callable.call();
            return v;
        }
        catch (NashornException ne) {
            throw ne.initEcmaError(this.global);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new AssertionError("Cannot happen", e);
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }

    private static void checkKey(Object key) {
        Objects.requireNonNull(key, "key can not be null");
        if (!(key instanceof String)) {
            throw new ClassCastException("key should be a String. It is " + key.getClass().getName() + " instead.");
        }
        if (((String)key).length() == 0) {
            throw new IllegalArgumentException("key can not be empty");
        }
    }

    @Override
    @Deprecated
    public double toNumber() {
        return this.inGlobal(new Callable<Double>(){

            @Override
            public Double call() {
                return JSType.toNumber(ScriptObjectMirror.this.sobj);
            }
        });
    }

    @Override
    public Object getDefaultValue(final Class<?> hint) {
        return this.inGlobal(new Callable<Object>(){

            @Override
            public Object call() {
                try {
                    return ScriptObjectMirror.this.sobj.getDefaultValue(hint);
                }
                catch (ECMAException e) {
                    throw new UnsupportedOperationException(e.getMessage(), e);
                }
            }
        });
    }

    private ScriptObjectMirror asJSONCompatible() {
        if (this.jsonCompatible) {
            return this;
        }
        return new ScriptObjectMirror(this.sobj, this.global, true);
    }
}

