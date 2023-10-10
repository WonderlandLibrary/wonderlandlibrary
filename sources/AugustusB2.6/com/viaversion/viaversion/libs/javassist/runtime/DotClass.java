// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.javassist.runtime;

public class DotClass
{
    public static NoClassDefFoundError fail(final ClassNotFoundException e) {
        return new NoClassDefFoundError(e.getMessage());
    }
}
