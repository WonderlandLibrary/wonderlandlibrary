// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.mac;

import org.slf4j.LoggerFactory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.IOKit;
import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.platform.mac.IOKitUtil;
import java.util.ArrayList;
import oshi.hardware.Display;
import java.util.List;
import org.slf4j.Logger;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractDisplay;

@Immutable
final class MacDisplay extends AbstractDisplay
{
    private static final Logger LOG;
    
    MacDisplay(final byte[] edid) {
        super(edid);
        MacDisplay.LOG.debug("Initialized MacDisplay");
    }
    
    public static List<Display> getDisplays() {
        final List<Display> displays = new ArrayList<Display>();
        final IOKit.IOIterator serviceIterator = IOKitUtil.getMatchingServices("IODisplayConnect");
        if (serviceIterator != null) {
            final CoreFoundation.CFStringRef cfEdid = CoreFoundation.CFStringRef.createCFString("IODisplayEDID");
            for (IOKit.IORegistryEntry sdService = serviceIterator.next(); sdService != null; sdService = serviceIterator.next()) {
                final IOKit.IORegistryEntry properties = sdService.getChildEntry("IOService");
                if (properties != null) {
                    final CoreFoundation.CFTypeRef edidRaw = properties.createCFProperty(cfEdid);
                    if (edidRaw != null) {
                        final CoreFoundation.CFDataRef edid = new CoreFoundation.CFDataRef(edidRaw.getPointer());
                        final int length = edid.getLength();
                        final Pointer p = edid.getBytePtr();
                        displays.add(new MacDisplay(p.getByteArray(0L, length)));
                        edid.release();
                    }
                    properties.release();
                }
                sdService.release();
            }
            serviceIterator.release();
            cfEdid.release();
        }
        return displays;
    }
    
    static {
        LOG = LoggerFactory.getLogger(MacDisplay.class);
    }
}
