/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.tools.reflect;

import com.viaversion.viaversion.libs.javassist.tools.reflect.ClassMetaobject;
import com.viaversion.viaversion.libs.javassist.tools.reflect.Metalevel;
import com.viaversion.viaversion.libs.javassist.tools.reflect.Metaobject;

public class Sample {
    private Metaobject _metaobject;
    private static ClassMetaobject _classobject;

    public Object trap(Object[] args2, int identifier) throws Throwable {
        Metaobject mobj = this._metaobject;
        if (mobj == null) {
            return ClassMetaobject.invoke(this, identifier, args2);
        }
        return mobj.trapMethodcall(identifier, args2);
    }

    public static Object trapStatic(Object[] args2, int identifier) throws Throwable {
        return _classobject.trapMethodcall(identifier, args2);
    }

    public static Object trapRead(Object[] args2, String name) {
        if (args2[0] == null) {
            return _classobject.trapFieldRead(name);
        }
        return ((Metalevel)args2[0])._getMetaobject().trapFieldRead(name);
    }

    public static Object trapWrite(Object[] args2, String name) {
        Metalevel base = (Metalevel)args2[0];
        if (base == null) {
            _classobject.trapFieldWrite(name, args2[1]);
        } else {
            base._getMetaobject().trapFieldWrite(name, args2[1]);
        }
        return null;
    }
}

