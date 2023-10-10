// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import java.util.Collection;
import java.util.Objects;
import oshi.util.platform.windows.WmiQueryHandler;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import java.util.Set;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32Process
{
    private static final String WIN32_PROCESS = "Win32_Process";
    
    private Win32Process() {
    }
    
    public static WbemcliUtil.WmiResult<CommandLineProperty> queryCommandLines(final Set<Integer> pidsToQuery) {
        String sb = "Win32_Process";
        if (pidsToQuery != null) {
            sb = sb + " WHERE ProcessID=" + pidsToQuery.stream().map((Function<? super Object, ?>)String::valueOf).collect((Collector<? super Object, ?, String>)Collectors.joining(" OR PROCESSID="));
        }
        final WbemcliUtil.WmiQuery<CommandLineProperty> commandLineQuery = new WbemcliUtil.WmiQuery<CommandLineProperty>(sb, CommandLineProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(commandLineQuery);
    }
    
    public static WbemcliUtil.WmiResult<ProcessXPProperty> queryProcesses(final Collection<Integer> pids) {
        String sb = "Win32_Process";
        if (pids != null) {
            sb = sb + " WHERE ProcessID=" + pids.stream().map((Function<? super Integer, ?>)String::valueOf).collect((Collector<? super Object, ?, String>)Collectors.joining(" OR PROCESSID="));
        }
        final WbemcliUtil.WmiQuery<ProcessXPProperty> processQueryXP = new WbemcliUtil.WmiQuery<ProcessXPProperty>(sb, ProcessXPProperty.class);
        return Objects.requireNonNull(WmiQueryHandler.createInstance()).queryWMI(processQueryXP);
    }
    
    public enum CommandLineProperty
    {
        PROCESSID, 
        COMMANDLINE;
        
        private static /* synthetic */ CommandLineProperty[] $values() {
            return new CommandLineProperty[] { CommandLineProperty.PROCESSID, CommandLineProperty.COMMANDLINE };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public enum ProcessXPProperty
    {
        PROCESSID, 
        NAME, 
        KERNELMODETIME, 
        USERMODETIME, 
        THREADCOUNT, 
        PAGEFILEUSAGE, 
        HANDLECOUNT, 
        EXECUTABLEPATH;
        
        private static /* synthetic */ ProcessXPProperty[] $values() {
            return new ProcessXPProperty[] { ProcessXPProperty.PROCESSID, ProcessXPProperty.NAME, ProcessXPProperty.KERNELMODETIME, ProcessXPProperty.USERMODETIME, ProcessXPProperty.THREADCOUNT, ProcessXPProperty.PAGEFILEUSAGE, ProcessXPProperty.HANDLECOUNT, ProcessXPProperty.EXECUTABLEPATH };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
