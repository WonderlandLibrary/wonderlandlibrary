// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32.COM;

import com.sun.jna.Pointer;

public interface IMoniker extends IPersistStream
{
    void BindToObject();
    
    void BindToStorage();
    
    void Reduce();
    
    void ComposeWith();
    
    void Enum();
    
    void IsEqual();
    
    void Hash();
    
    void IsRunning();
    
    void GetTimeOfLastChange();
    
    void Inverse();
    
    void CommonPrefixWith();
    
    String GetDisplayName(final Pointer p0, final Pointer p1);
    
    void ParseDisplayName();
    
    void IsSystemMoniker();
    
    void RelativePathTo();
}
