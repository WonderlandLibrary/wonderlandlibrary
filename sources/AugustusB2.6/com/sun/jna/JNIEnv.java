// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

public final class JNIEnv
{
    public static final JNIEnv CURRENT;
    
    private JNIEnv() {
    }
    
    static {
        CURRENT = new JNIEnv();
    }
}
