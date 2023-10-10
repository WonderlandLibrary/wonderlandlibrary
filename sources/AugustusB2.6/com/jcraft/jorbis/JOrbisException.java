// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

public class JOrbisException extends Exception
{
    public JOrbisException() {
    }
    
    public JOrbisException(final String str) {
        super("JOrbis: " + str);
    }
}
