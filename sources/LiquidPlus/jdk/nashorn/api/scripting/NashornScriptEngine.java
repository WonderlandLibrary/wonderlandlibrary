/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.api.scripting;

import java.io.IOException;
import java.io.Reader;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import jdk.Exported;
import jdk.nashorn.api.scripting.ClassFilter;
import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.Source;
import jdk.nashorn.internal.runtime.linker.JavaAdapterFactory;
import jdk.nashorn.internal.runtime.options.Options;

@Exported
public final class NashornScriptEngine
extends AbstractScriptEngine
implements Compilable,
Invocable {
    public static final String NASHORN_GLOBAL = "nashorn.global";
    private static final AccessControlContext CREATE_CONTEXT_ACC_CTXT = NashornScriptEngine.createPermAccCtxt("nashorn.createContext");
    private static final AccessControlContext CREATE_GLOBAL_ACC_CTXT = NashornScriptEngine.createPermAccCtxt("nashorn.createGlobal");
    private final ScriptEngineFactory factory;
    private final Context nashornContext;
    private final boolean _global_per_engine;
    private final Global global;
    private static final String MESSAGES_RESOURCE = "jdk.nashorn.api.scripting.resources.Messages";
    private static final ResourceBundle MESSAGES_BUNDLE = ResourceBundle.getBundle("jdk.nashorn.api.scripting.resources.Messages", Locale.getDefault());

    private static AccessControlContext createPermAccCtxt(String permName) {
        Permissions perms = new Permissions();
        perms.add(new RuntimePermission(permName));
        return new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain(null, perms)});
    }

    private static String getMessage(String msgId, String ... args2) {
        try {
            return new MessageFormat(MESSAGES_BUNDLE.getString(msgId)).format(args2);
        }
        catch (MissingResourceException e) {
            throw new RuntimeException("no message resource found for message id: " + msgId);
        }
    }

    NashornScriptEngine(NashornScriptEngineFactory factory, String[] args2, final ClassLoader appLoader, final ClassFilter classFilter) {
        assert (args2 != null) : "null argument array";
        this.factory = factory;
        final Options options = new Options("nashorn");
        options.process(args2);
        final Context.ThrowErrorManager errMgr = new Context.ThrowErrorManager();
        this.nashornContext = AccessController.doPrivileged(new PrivilegedAction<Context>(){

            @Override
            public Context run() {
                try {
                    return new Context(options, errMgr, appLoader, classFilter);
                }
                catch (RuntimeException e) {
                    if (Context.DEBUG) {
                        e.printStackTrace();
                    }
                    throw e;
                }
            }
        }, CREATE_CONTEXT_ACC_CTXT);
        this._global_per_engine = this.nashornContext.getEnv()._global_per_engine;
        this.global = this.createNashornGlobal();
        this.context.setBindings(new ScriptObjectMirror(this.global, this.global), 100);
    }

    @Override
    public Object eval(Reader reader, ScriptContext ctxt) throws ScriptException {
        return this.evalImpl(NashornScriptEngine.makeSource(reader, ctxt), ctxt);
    }

    @Override
    public Object eval(String script, ScriptContext ctxt) throws ScriptException {
        return this.evalImpl(NashornScriptEngine.makeSource(script, ctxt), ctxt);
    }

    @Override
    public ScriptEngineFactory getFactory() {
        return this.factory;
    }

    @Override
    public Bindings createBindings() {
        if (this._global_per_engine) {
            return new SimpleBindings();
        }
        return this.createGlobalMirror();
    }

    @Override
    public CompiledScript compile(Reader reader) throws ScriptException {
        return this.asCompiledScript(NashornScriptEngine.makeSource(reader, this.context));
    }

    @Override
    public CompiledScript compile(String str) throws ScriptException {
        return this.asCompiledScript(NashornScriptEngine.makeSource(str, this.context));
    }

    @Override
    public Object invokeFunction(String name, Object ... args2) throws ScriptException, NoSuchMethodException {
        return this.invokeImpl(null, name, args2);
    }

    @Override
    public Object invokeMethod(Object thiz, String name, Object ... args2) throws ScriptException, NoSuchMethodException {
        if (thiz == null) {
            throw new IllegalArgumentException(NashornScriptEngine.getMessage("thiz.cannot.be.null", new String[0]));
        }
        return this.invokeImpl(thiz, name, args2);
    }

    @Override
    public <T> T getInterface(Class<T> clazz) {
        return this.getInterfaceInner(null, clazz);
    }

    @Override
    public <T> T getInterface(Object thiz, Class<T> clazz) {
        if (thiz == null) {
            throw new IllegalArgumentException(NashornScriptEngine.getMessage("thiz.cannot.be.null", new String[0]));
        }
        return this.getInterfaceInner(thiz, clazz);
    }

    private static Source makeSource(Reader reader, ScriptContext ctxt) throws ScriptException {
        try {
            return Source.sourceFor(NashornScriptEngine.getScriptName(ctxt), reader);
        }
        catch (IOException e) {
            throw new ScriptException(e);
        }
    }

    private static Source makeSource(String src, ScriptContext ctxt) {
        return Source.sourceFor(NashornScriptEngine.getScriptName(ctxt), src);
    }

    private static String getScriptName(ScriptContext ctxt) {
        Object val = ctxt.getAttribute("javax.script.filename");
        return val != null ? val.toString() : "<eval>";
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private <T> T getInterfaceInner(Object thiz, Class<T> clazz) {
        assert (!(thiz instanceof ScriptObject)) : "raw ScriptObject not expected here";
        if (clazz == null) throw new IllegalArgumentException(NashornScriptEngine.getMessage("interface.class.expected", new String[0]));
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException(NashornScriptEngine.getMessage("interface.class.expected", new String[0]));
        }
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            if (!Modifier.isPublic(clazz.getModifiers())) {
                throw new SecurityException(NashornScriptEngine.getMessage("implementing.non.public.interface", clazz.getName()));
            }
            Context.checkPackageAccess(clazz);
        }
        ScriptObject realSelf = null;
        Global realGlobal = null;
        if (thiz == null) {
            realGlobal = this.getNashornGlobalFrom(this.context);
            realSelf = realGlobal;
        } else if (thiz instanceof ScriptObjectMirror) {
            ScriptObjectMirror mirror = (ScriptObjectMirror)thiz;
            realSelf = mirror.getScriptObject();
            realGlobal = mirror.getHomeGlobal();
            if (!NashornScriptEngine.isOfContext(realGlobal, this.nashornContext)) {
                throw new IllegalArgumentException(NashornScriptEngine.getMessage("script.object.from.another.engine", new String[0]));
            }
        }
        if (realSelf == null) {
            throw new IllegalArgumentException(NashornScriptEngine.getMessage("interface.on.non.script.object", new String[0]));
        }
        try {
            Global oldGlobal = Context.getGlobal();
            boolean globalChanged = oldGlobal != realGlobal;
            try {
                if (globalChanged) {
                    Context.setGlobal(realGlobal);
                }
                if (!NashornScriptEngine.isInterfaceImplemented(clazz, realSelf)) {
                    T t = null;
                    return t;
                }
                T t = clazz.cast(JavaAdapterFactory.getConstructor(realSelf.getClass(), clazz, MethodHandles.publicLookup()).invoke(realSelf));
                return t;
            }
            finally {
                if (globalChanged) {
                    Context.setGlobal(oldGlobal);
                }
            }
        }
        catch (Error | RuntimeException e) {
            throw e;
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private Global getNashornGlobalFrom(ScriptContext ctxt) {
        Global glob;
        Global glob2;
        if (this._global_per_engine) {
            return this.global;
        }
        Bindings bindings = ctxt.getBindings(100);
        if (bindings instanceof ScriptObjectMirror && (glob2 = this.globalFromMirror((ScriptObjectMirror)bindings)) != null) {
            return glob2;
        }
        Object scope = bindings.get(NASHORN_GLOBAL);
        if (scope instanceof ScriptObjectMirror && (glob = this.globalFromMirror((ScriptObjectMirror)scope)) != null) {
            return glob;
        }
        ScriptObjectMirror mirror = this.createGlobalMirror();
        bindings.put(NASHORN_GLOBAL, (Object)mirror);
        mirror.getHomeGlobal().setInitScriptContext(ctxt);
        return mirror.getHomeGlobal();
    }

    private Global globalFromMirror(ScriptObjectMirror mirror) {
        ScriptObject sobj = mirror.getScriptObject();
        if (sobj instanceof Global && NashornScriptEngine.isOfContext((Global)sobj, this.nashornContext)) {
            return (Global)sobj;
        }
        return null;
    }

    private ScriptObjectMirror createGlobalMirror() {
        Global newGlobal = this.createNashornGlobal();
        return new ScriptObjectMirror(newGlobal, newGlobal);
    }

    private Global createNashornGlobal() {
        Global newGlobal = AccessController.doPrivileged(new PrivilegedAction<Global>(){

            @Override
            public Global run() {
                try {
                    return NashornScriptEngine.this.nashornContext.newGlobal();
                }
                catch (RuntimeException e) {
                    if (Context.DEBUG) {
                        e.printStackTrace();
                    }
                    throw e;
                }
            }
        }, CREATE_GLOBAL_ACC_CTXT);
        this.nashornContext.initGlobal(newGlobal, this);
        return newGlobal;
    }

    private Object invokeImpl(Object selfObject, String name, Object ... args2) throws ScriptException, NoSuchMethodException {
        Objects.requireNonNull(name);
        assert (!(selfObject instanceof ScriptObject)) : "raw ScriptObject not expected here";
        Global invokeGlobal = null;
        ScriptObjectMirror selfMirror = null;
        if (selfObject instanceof ScriptObjectMirror) {
            selfMirror = (ScriptObjectMirror)selfObject;
            if (!NashornScriptEngine.isOfContext(selfMirror.getHomeGlobal(), this.nashornContext)) {
                throw new IllegalArgumentException(NashornScriptEngine.getMessage("script.object.from.another.engine", new String[0]));
            }
            invokeGlobal = selfMirror.getHomeGlobal();
        } else if (selfObject == null) {
            Global ctxtGlobal;
            invokeGlobal = ctxtGlobal = this.getNashornGlobalFrom(this.context);
            selfMirror = (ScriptObjectMirror)ScriptObjectMirror.wrap(ctxtGlobal, ctxtGlobal);
        }
        if (selfMirror != null) {
            try {
                return ScriptObjectMirror.translateUndefined(selfMirror.callMember(name, args2));
            }
            catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof NoSuchMethodException) {
                    throw (NoSuchMethodException)cause;
                }
                NashornScriptEngine.throwAsScriptException(e, invokeGlobal);
                throw new AssertionError((Object)"should not reach here");
            }
        }
        throw new IllegalArgumentException(NashornScriptEngine.getMessage("interface.on.non.script.object", new String[0]));
    }

    private Object evalImpl(Source src, ScriptContext ctxt) throws ScriptException {
        return this.evalImpl(this.compileImpl(src, ctxt), ctxt);
    }

    private Object evalImpl(ScriptFunction script, ScriptContext ctxt) throws ScriptException {
        return this.evalImpl(script, ctxt, this.getNashornGlobalFrom(ctxt));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Object evalImpl(Context.MultiGlobalCompiledScript mgcs, ScriptContext ctxt, Global ctxtGlobal) throws ScriptException {
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != ctxtGlobal;
        try {
            if (globalChanged) {
                Context.setGlobal(ctxtGlobal);
            }
            ScriptFunction script = mgcs.getFunction(ctxtGlobal);
            ScriptContext oldCtxt = ctxtGlobal.getScriptContext();
            ctxtGlobal.setScriptContext(ctxt);
            try {
                Object object = ScriptObjectMirror.translateUndefined(ScriptObjectMirror.wrap(ScriptRuntime.apply(script, ctxtGlobal, new Object[0]), ctxtGlobal));
                ctxtGlobal.setScriptContext(oldCtxt);
                return object;
            }
            catch (Throwable throwable) {
                try {
                    ctxtGlobal.setScriptContext(oldCtxt);
                    throw throwable;
                }
                catch (Exception e) {
                    NashornScriptEngine.throwAsScriptException(e, ctxtGlobal);
                    throw new AssertionError((Object)"should not reach here");
                }
            }
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private Object evalImpl(ScriptFunction script, ScriptContext ctxt, Global ctxtGlobal) throws ScriptException {
        if (script == null) {
            return null;
        }
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != ctxtGlobal;
        try {
            if (globalChanged) {
                Context.setGlobal(ctxtGlobal);
            }
            ScriptContext oldCtxt = ctxtGlobal.getScriptContext();
            ctxtGlobal.setScriptContext(ctxt);
            try {
                Object object = ScriptObjectMirror.translateUndefined(ScriptObjectMirror.wrap(ScriptRuntime.apply(script, ctxtGlobal, new Object[0]), ctxtGlobal));
                ctxtGlobal.setScriptContext(oldCtxt);
                return object;
            }
            catch (Throwable throwable) {
                try {
                    ctxtGlobal.setScriptContext(oldCtxt);
                    throw throwable;
                }
                catch (Exception e) {
                    NashornScriptEngine.throwAsScriptException(e, ctxtGlobal);
                    throw new AssertionError((Object)"should not reach here");
                }
            }
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }

    private static void throwAsScriptException(Exception e, Global global) throws ScriptException {
        if (e instanceof ScriptException) {
            throw (ScriptException)e;
        }
        if (e instanceof NashornException) {
            NashornException ne = (NashornException)e;
            ScriptException se = new ScriptException(ne.getMessage(), ne.getFileName(), ne.getLineNumber(), ne.getColumnNumber());
            ne.initEcmaError(global);
            se.initCause(e);
            throw se;
        }
        if (e instanceof RuntimeException) {
            throw (RuntimeException)e;
        }
        throw new ScriptException(e);
    }

    private CompiledScript asCompiledScript(Source source) throws ScriptException {
        ScriptFunction func;
        Context.MultiGlobalCompiledScript mgcs;
        Global newGlobal;
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != (newGlobal = this.getNashornGlobalFrom(this.context));
        try {
            if (globalChanged) {
                Context.setGlobal(newGlobal);
            }
            mgcs = this.nashornContext.compileScript(source);
            func = mgcs.getFunction(newGlobal);
        }
        catch (Exception e) {
            NashornScriptEngine.throwAsScriptException(e, newGlobal);
            throw new AssertionError((Object)"should not reach here");
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
        return new CompiledScript(){

            @Override
            public Object eval(ScriptContext ctxt) throws ScriptException {
                Global globalObject = NashornScriptEngine.this.getNashornGlobalFrom(ctxt);
                if (func.getScope() == globalObject) {
                    return NashornScriptEngine.this.evalImpl(func, ctxt, globalObject);
                }
                return NashornScriptEngine.this.evalImpl(mgcs, ctxt, globalObject);
            }

            @Override
            public ScriptEngine getEngine() {
                return NashornScriptEngine.this;
            }
        };
    }

    private ScriptFunction compileImpl(Source source, ScriptContext ctxt) throws ScriptException {
        return this.compileImpl(source, this.getNashornGlobalFrom(ctxt));
    }

    private ScriptFunction compileImpl(Source source, Global newGlobal) throws ScriptException {
        Global oldGlobal = Context.getGlobal();
        boolean globalChanged = oldGlobal != newGlobal;
        try {
            if (globalChanged) {
                Context.setGlobal(newGlobal);
            }
            ScriptFunction scriptFunction = this.nashornContext.compileScript(source, newGlobal);
            return scriptFunction;
        }
        catch (Exception e) {
            NashornScriptEngine.throwAsScriptException(e, newGlobal);
            throw new AssertionError((Object)"should not reach here");
        }
        finally {
            if (globalChanged) {
                Context.setGlobal(oldGlobal);
            }
        }
    }

    private static boolean isInterfaceImplemented(Class<?> iface, ScriptObject sobj) {
        for (Method method : iface.getMethods()) {
            Object obj;
            if (method.getDeclaringClass() == Object.class || !Modifier.isAbstract(method.getModifiers()) || (obj = sobj.get(method.getName())) instanceof ScriptFunction) continue;
            return false;
        }
        return true;
    }

    private static boolean isOfContext(Global global, Context context) {
        return global.isOfContext(context);
    }
}

