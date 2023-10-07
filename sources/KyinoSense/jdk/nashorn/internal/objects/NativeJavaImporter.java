/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.objects;

import java.util.Collections;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.beans.StaticClass;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.NativeJavaPackage;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.ScriptRuntime;
import jdk.nashorn.internal.runtime.UnwarrantedOptimismException;

public final class NativeJavaImporter
extends ScriptObject {
    private final Object[] args;
    private static PropertyMap $nasgenmap$;

    private NativeJavaImporter(Object[] args2, ScriptObject proto, PropertyMap map) {
        super(proto, map);
        this.args = args2;
    }

    private NativeJavaImporter(Object[] args2, Global global) {
        this(args2, global.getJavaImporterPrototype(), $nasgenmap$);
    }

    private NativeJavaImporter(Object[] args2) {
        this(args2, Global.instance());
    }

    @Override
    public String getClassName() {
        return "JavaImporter";
    }

    public static NativeJavaImporter constructor(boolean isNew, Object self, Object ... args2) {
        return new NativeJavaImporter(args2);
    }

    public static Object __noSuchProperty__(Object self, Object name) {
        if (!(self instanceof NativeJavaImporter)) {
            throw ECMAErrors.typeError("not.a.java.importer", ScriptRuntime.safeToString(self));
        }
        return ((NativeJavaImporter)self).createProperty(JSType.toString(name));
    }

    public static Object __noSuchMethod__(Object self, Object ... args2) {
        throw ECMAErrors.typeError("not.a.function", ScriptRuntime.safeToString(args2[0]));
    }

    @Override
    public GuardedInvocation noSuchProperty(CallSiteDescriptor desc, LinkRequest request) {
        return this.createAndSetProperty(desc) ? super.lookup(desc, request) : super.noSuchProperty(desc, request);
    }

    @Override
    public GuardedInvocation noSuchMethod(CallSiteDescriptor desc, LinkRequest request) {
        return this.createAndSetProperty(desc) ? super.lookup(desc, request) : super.noSuchMethod(desc, request);
    }

    @Override
    protected Object invokeNoSuchProperty(String name, boolean isScope, int programPoint) {
        Object retval = this.createProperty(name);
        if (UnwarrantedOptimismException.isValid(programPoint)) {
            throw new UnwarrantedOptimismException(retval, programPoint);
        }
        return retval;
    }

    private boolean createAndSetProperty(CallSiteDescriptor desc) {
        String name = desc.getNameToken(2);
        Object value = this.createProperty(name);
        if (value != null) {
            this.set((Object)name, value, 0);
            return true;
        }
        return false;
    }

    private Object createProperty(String name) {
        int len = this.args.length;
        for (int i = len - 1; i > -1; --i) {
            Object obj = this.args[i];
            if (obj instanceof StaticClass) {
                if (!((StaticClass)obj).getRepresentedClass().getSimpleName().equals(name)) continue;
                return obj;
            }
            if (!(obj instanceof NativeJavaPackage)) continue;
            String pkgName = ((NativeJavaPackage)obj).getName();
            String fullName = pkgName.isEmpty() ? name : pkgName + "." + name;
            Context context = Global.instance().getContext();
            try {
                return StaticClass.forClass(context.findClass(fullName));
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        return null;
    }

    static {
        NativeJavaImporter.$clinit$();
    }

    public static void $clinit$() {
        $nasgenmap$ = PropertyMap.newMap(Collections.EMPTY_LIST);
    }
}

