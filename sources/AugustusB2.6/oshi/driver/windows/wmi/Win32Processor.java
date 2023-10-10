// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32Processor
{
    private static final String WIN32_PROCESSOR = "Win32_Processor";
    
    private Win32Processor() {
    }
    
    public static WbemcliUtil.WmiResult<VoltProperty> queryVoltage() {
        final WbemcliUtil.WmiQuery<VoltProperty> voltQuery = new WbemcliUtil.WmiQuery<VoltProperty>("Win32_Processor", VoltProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(voltQuery);
    }
    
    public static WbemcliUtil.WmiResult<ProcessorIdProperty> queryProcessorId() {
        final WbemcliUtil.WmiQuery<ProcessorIdProperty> idQuery = new WbemcliUtil.WmiQuery<ProcessorIdProperty>("Win32_Processor", ProcessorIdProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(idQuery);
    }
    
    public static WbemcliUtil.WmiResult<BitnessProperty> queryBitness() {
        final WbemcliUtil.WmiQuery<BitnessProperty> bitnessQuery = new WbemcliUtil.WmiQuery<BitnessProperty>("Win32_Processor", BitnessProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(bitnessQuery);
    }
    
    public enum VoltProperty
    {
        CURRENTVOLTAGE, 
        VOLTAGECAPS;
        
        private static /* synthetic */ VoltProperty[] $values() {
            return new VoltProperty[] { VoltProperty.CURRENTVOLTAGE, VoltProperty.VOLTAGECAPS };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public enum ProcessorIdProperty
    {
        PROCESSORID;
        
        private static /* synthetic */ ProcessorIdProperty[] $values() {
            return new ProcessorIdProperty[] { ProcessorIdProperty.PROCESSORID };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public enum BitnessProperty
    {
        ADDRESSWIDTH;
        
        private static /* synthetic */ BitnessProperty[] $values() {
            return new BitnessProperty[] { BitnessProperty.ADDRESSWIDTH };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
