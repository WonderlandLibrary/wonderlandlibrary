// 
// Decompiled by Procyon v0.5.36
// 

package com.badlogic.gdx;

public interface Files
{
    String getExternalStoragePath();
    
    public enum FileType
    {
        Classpath, 
        Internal, 
        External, 
        Absolute, 
        Local;
    }
}
