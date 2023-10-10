// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.unix;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import oshi.driver.unix.Xrandr;
import oshi.hardware.Display;
import java.util.List;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.hardware.common.AbstractDisplay;

@ThreadSafe
public final class UnixDisplay extends AbstractDisplay
{
    UnixDisplay(final byte[] edid) {
        super(edid);
    }
    
    public static List<Display> getDisplays() {
        return Xrandr.getEdidArrays().stream().map((Function<? super Object, ?>)UnixDisplay::new).collect((Collector<? super Object, ?, List<Display>>)Collectors.toList());
    }
}
