// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.wmi;

import oshi.util.Memoizer;
import com.sun.jna.platform.win32.COM.WbemcliUtil;
import oshi.util.platform.windows.WmiUtil;
import java.util.Set;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import oshi.annotation.concurrent.GuardedBy;
import oshi.util.tuples.Pair;
import java.util.Map;
import java.util.function.Supplier;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class Win32ProcessCached
{
    private static final Supplier<Win32ProcessCached> INSTANCE;
    @GuardedBy("commandLineCacheLock")
    private final Map<Integer, Pair<Long, String>> commandLineCache;
    private final ReentrantLock commandLineCacheLock;
    
    private Win32ProcessCached() {
        this.commandLineCache = new HashMap<Integer, Pair<Long, String>>();
        this.commandLineCacheLock = new ReentrantLock();
    }
    
    public static Win32ProcessCached getInstance() {
        return Win32ProcessCached.INSTANCE.get();
    }
    
    private static Win32ProcessCached createInstance() {
        return new Win32ProcessCached();
    }
    
    public String getCommandLine(final int processId, final long startTime) {
        this.commandLineCacheLock.lock();
        try {
            final Pair<Long, String> pair = this.commandLineCache.get(processId);
            if (pair != null && startTime < pair.getA()) {
                return pair.getB();
            }
            final long now = System.currentTimeMillis();
            final WbemcliUtil.WmiResult<Win32Process.CommandLineProperty> commandLineAllProcs = Win32Process.queryCommandLines(null);
            if (this.commandLineCache.size() > commandLineAllProcs.getResultCount() * 2) {
                this.commandLineCache.clear();
            }
            String result = "";
            for (int i = 0; i < commandLineAllProcs.getResultCount(); ++i) {
                final int pid = WmiUtil.getUint32(commandLineAllProcs, Win32Process.CommandLineProperty.PROCESSID, i);
                final String cl = WmiUtil.getString(commandLineAllProcs, Win32Process.CommandLineProperty.COMMANDLINE, i);
                this.commandLineCache.put(pid, new Pair<Long, String>(now, cl));
                if (pid == processId) {
                    result = cl;
                }
            }
            return result;
        }
        finally {
            this.commandLineCacheLock.unlock();
        }
    }
    
    static {
        INSTANCE = Memoizer.memoize(Win32ProcessCached::createInstance);
    }
}
