/*
 * Decompiled with CFR 0.152.
 */
package javassist.tools.reflect;

import javassist.tools.reflect.ClassMetaobject;
import javassist.tools.reflect.Metaobject;

public interface Metalevel {
    public ClassMetaobject _getClass();

    public Metaobject _getMetaobject();

    public void _setMetaobject(Metaobject var1);
}

