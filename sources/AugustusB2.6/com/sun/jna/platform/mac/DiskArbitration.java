// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.mac;

import com.sun.jna.Native;
import com.sun.jna.Library;

public interface DiskArbitration extends Library
{
    public static final DiskArbitration INSTANCE = Native.load("DiskArbitration", DiskArbitration.class);
    
    DASessionRef DASessionCreate(final CoreFoundation.CFAllocatorRef p0);
    
    DADiskRef DADiskCreateFromBSDName(final CoreFoundation.CFAllocatorRef p0, final DASessionRef p1, final String p2);
    
    DADiskRef DADiskCreateFromIOMedia(final CoreFoundation.CFAllocatorRef p0, final DASessionRef p1, final IOKit.IOObject p2);
    
    CoreFoundation.CFDictionaryRef DADiskCopyDescription(final DADiskRef p0);
    
    String DADiskGetBSDName(final DADiskRef p0);
    
    public static class DASessionRef extends CoreFoundation.CFTypeRef
    {
    }
    
    public static class DADiskRef extends CoreFoundation.CFTypeRef
    {
    }
}
