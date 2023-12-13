// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.reflect;

import me.gong.mcleaks.util.google.common.base.Preconditions;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

abstract class TypeCapture<T>
{
    final Type capture() {
        final Type superclass = this.getClass().getGenericSuperclass();
        Preconditions.checkArgument(superclass instanceof ParameterizedType, "%s isn't parameterized", superclass);
        return ((ParameterizedType)superclass).getActualTypeArguments()[0];
    }
}
