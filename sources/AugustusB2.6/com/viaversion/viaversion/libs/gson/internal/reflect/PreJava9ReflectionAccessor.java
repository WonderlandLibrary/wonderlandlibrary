// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.gson.internal.reflect;

import java.lang.reflect.AccessibleObject;

final class PreJava9ReflectionAccessor extends ReflectionAccessor
{
    @Override
    public void makeAccessible(final AccessibleObject ao) {
        ao.setAccessible(true);
    }
}
