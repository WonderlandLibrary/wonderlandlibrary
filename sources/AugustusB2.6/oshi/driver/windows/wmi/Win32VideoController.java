// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32VideoController
{
    private static final String WIN32_VIDEO_CONTROLLER = "Win32_VideoController";
    
    private Win32VideoController() {
    }
    
    public static WbemcliUtil.WmiResult<VideoControllerProperty> queryVideoController() {
        final WbemcliUtil.WmiQuery<VideoControllerProperty> videoControllerQuery = new WbemcliUtil.WmiQuery<VideoControllerProperty>("Win32_VideoController", VideoControllerProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(videoControllerQuery);
    }
    
    public enum VideoControllerProperty
    {
        ADAPTERCOMPATIBILITY, 
        ADAPTERRAM, 
        DRIVERVERSION, 
        NAME, 
        PNPDEVICEID;
        
        private static /* synthetic */ VideoControllerProperty[] $values() {
            return new VideoControllerProperty[] { VideoControllerProperty.ADAPTERCOMPATIBILITY, VideoControllerProperty.ADAPTERRAM, VideoControllerProperty.DRIVERVERSION, VideoControllerProperty.NAME, VideoControllerProperty.PNPDEVICEID };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
