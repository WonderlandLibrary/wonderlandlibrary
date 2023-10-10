// 
// Decompiled by Procyon v0.5.36
// 

package oshi.jna.platform.mac;

import com.sun.jna.Pointer;
import com.sun.jna.Native;
import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.Library;

public interface SystemConfiguration extends Library
{
    public static final SystemConfiguration INSTANCE = Native.load("SystemConfiguration", SystemConfiguration.class);
    
    CoreFoundation.CFArrayRef SCNetworkInterfaceCopyAll();
    
    CoreFoundation.CFStringRef SCNetworkInterfaceGetBSDName(final SCNetworkInterfaceRef p0);
    
    CoreFoundation.CFStringRef SCNetworkInterfaceGetLocalizedDisplayName(final SCNetworkInterfaceRef p0);
    
    public static class SCNetworkInterfaceRef extends CoreFoundation.CFTypeRef
    {
        public SCNetworkInterfaceRef() {
        }
        
        public SCNetworkInterfaceRef(final Pointer p) {
            super(p);
        }
    }
}
