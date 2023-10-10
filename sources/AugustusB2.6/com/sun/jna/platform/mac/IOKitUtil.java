// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.mac;

import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.IntByReference;

public class IOKitUtil
{
    private static final IOKit IO;
    private static final SystemB SYS;
    
    private IOKitUtil() {
    }
    
    public static int getMasterPort() {
        final IntByReference port = new IntByReference();
        IOKitUtil.IO.IOMasterPort(0, port);
        return port.getValue();
    }
    
    public static IOKit.IORegistryEntry getRoot() {
        final int masterPort = getMasterPort();
        final IOKit.IORegistryEntry root = IOKitUtil.IO.IORegistryGetRootEntry(masterPort);
        IOKitUtil.SYS.mach_port_deallocate(IOKitUtil.SYS.mach_task_self(), masterPort);
        return root;
    }
    
    public static IOKit.IOService getMatchingService(final String serviceName) {
        final CoreFoundation.CFMutableDictionaryRef dict = IOKitUtil.IO.IOServiceMatching(serviceName);
        if (dict != null) {
            return getMatchingService(dict);
        }
        return null;
    }
    
    public static IOKit.IOService getMatchingService(final CoreFoundation.CFDictionaryRef matchingDictionary) {
        final int masterPort = getMasterPort();
        final IOKit.IOService service = IOKitUtil.IO.IOServiceGetMatchingService(masterPort, matchingDictionary);
        IOKitUtil.SYS.mach_port_deallocate(IOKitUtil.SYS.mach_task_self(), masterPort);
        return service;
    }
    
    public static IOKit.IOIterator getMatchingServices(final String serviceName) {
        final CoreFoundation.CFMutableDictionaryRef dict = IOKitUtil.IO.IOServiceMatching(serviceName);
        if (dict != null) {
            return getMatchingServices(dict);
        }
        return null;
    }
    
    public static IOKit.IOIterator getMatchingServices(final CoreFoundation.CFDictionaryRef matchingDictionary) {
        final int masterPort = getMasterPort();
        final PointerByReference serviceIterator = new PointerByReference();
        final int result = IOKitUtil.IO.IOServiceGetMatchingServices(masterPort, matchingDictionary, serviceIterator);
        IOKitUtil.SYS.mach_port_deallocate(IOKitUtil.SYS.mach_task_self(), masterPort);
        if (result == 0 && serviceIterator.getValue() != null) {
            return new IOKit.IOIterator(serviceIterator.getValue());
        }
        return null;
    }
    
    public static CoreFoundation.CFMutableDictionaryRef getBSDNameMatchingDict(final String bsdName) {
        final int masterPort = getMasterPort();
        final CoreFoundation.CFMutableDictionaryRef result = IOKitUtil.IO.IOBSDNameMatching(masterPort, 0, bsdName);
        IOKitUtil.SYS.mach_port_deallocate(IOKitUtil.SYS.mach_task_self(), masterPort);
        return result;
    }
    
    static {
        IO = IOKit.INSTANCE;
        SYS = SystemB.INSTANCE;
    }
}
