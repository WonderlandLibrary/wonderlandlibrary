// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.aix;

import org.slf4j.LoggerFactory;
import com.sun.jna.Pointer;
import com.sun.jna.NativeLong;
import java.util.Iterator;
import com.sun.jna.platform.unix.LibCAPI;
import com.sun.jna.Memory;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import oshi.util.tuples.Pair;
import oshi.util.tuples.Triplet;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import oshi.jna.platform.unix.AixLibc;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class PsInfo
{
    private static final Logger LOG;
    private static final AixLibc LIBC;
    private static final long PAGE_SIZE = 4096L;
    
    private PsInfo() {
    }
    
    public static AixLibc.AixPsInfo queryPsInfo(final int pid) {
        final Path path = Paths.get(String.format("/proc/%d/psinfo", pid), new String[0]);
        try {
            return new AixLibc.AixPsInfo(Files.readAllBytes(path));
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public static AixLibc.AIXLwpsInfo queryLwpsInfo(final int pid, final int tid) {
        final Path path = Paths.get(String.format("/proc/%d/lwp/%d/lwpsinfo", pid, tid), new String[0]);
        try {
            return new AixLibc.AIXLwpsInfo(Files.readAllBytes(path));
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public static Triplet<Integer, Long, Long> queryArgsEnvAddrs(final int pid, final AixLibc.AixPsInfo psinfo) {
        if (psinfo == null) {
            PsInfo.LOG.trace("Failed to read psinfo file for pid: {} ", (Object)pid);
            return null;
        }
        final int argc = psinfo.pr_argc;
        if (argc > 0) {
            final long argv = psinfo.pr_argv;
            final long envp = psinfo.pr_envp;
            return new Triplet<Integer, Long, Long>(argc, argv, envp);
        }
        PsInfo.LOG.trace("Failed argc sanity check: argc={}", (Object)argc);
        return null;
    }
    
    public static Pair<List<String>, Map<String, String>> queryArgsEnv(final int pid, final AixLibc.AixPsInfo psinfo) {
        final List<String> args = new ArrayList<String>();
        final Map<String, String> env = new LinkedHashMap<String, String>();
        final Triplet<Integer, Long, Long> addrs = queryArgsEnvAddrs(pid, psinfo);
        if (addrs != null) {
            final String procas = "/proc/" + pid + "/as";
            final int fd = PsInfo.LIBC.open(procas, 0);
            if (fd < 0) {
                PsInfo.LOG.trace("No permission to read file: {} ", procas);
                return new Pair<List<String>, Map<String, String>>(args, env);
            }
            try {
                final int argc = addrs.getA();
                final long argv = addrs.getB();
                final long envp = addrs.getC();
                final Path p = Paths.get("/proc/" + pid + "/status", new String[0]);
                long increment;
                try {
                    final byte[] status = Files.readAllBytes(p);
                    if (status[17] == 1) {
                        increment = 8L;
                    }
                    else {
                        increment = 4L;
                    }
                }
                catch (IOException e) {
                    return new Pair<List<String>, Map<String, String>>(args, env);
                }
                final Memory buffer = new Memory(8192L);
                final LibCAPI.size_t bufSize = new LibCAPI.size_t(buffer.size());
                long bufStart = conditionallyReadBufferFromStartOfPage(fd, buffer, bufSize, 0L, argv);
                final long[] argPtr = new long[argc];
                final long argp = (bufStart == 0L) ? 0L : getOffsetFromBuffer(buffer, argv - bufStart, increment);
                if (argp > 0L) {
                    for (int i = 0; i < argc; ++i) {
                        final long offset = argp + i * increment;
                        bufStart = conditionallyReadBufferFromStartOfPage(fd, buffer, bufSize, bufStart, offset);
                        argPtr[i] = ((bufStart == 0L) ? 0L : getOffsetFromBuffer(buffer, offset - bufStart, increment));
                    }
                }
                bufStart = conditionallyReadBufferFromStartOfPage(fd, buffer, bufSize, bufStart, envp);
                final List<Long> envPtrList = new ArrayList<Long>();
                final long addr = (bufStart == 0L) ? 0L : getOffsetFromBuffer(buffer, envp - bufStart, increment);
                int limit = 500;
                long offset2 = addr;
                while (addr != 0L && --limit > 0) {
                    bufStart = conditionallyReadBufferFromStartOfPage(fd, buffer, bufSize, bufStart, offset2);
                    final long envPtr = (bufStart == 0L) ? 0L : getOffsetFromBuffer(buffer, offset2 - bufStart, increment);
                    if (envPtr != 0L) {
                        envPtrList.add(envPtr);
                    }
                    offset2 += increment;
                }
                for (int j = 0; j < argPtr.length && argPtr[j] != 0L; ++j) {
                    bufStart = conditionallyReadBufferFromStartOfPage(fd, buffer, bufSize, bufStart, argPtr[j]);
                    if (bufStart != 0L) {
                        final String argStr = buffer.getString(argPtr[j] - bufStart);
                        if (!argStr.isEmpty()) {
                            args.add(argStr);
                        }
                    }
                }
                for (final Long envPtr2 : envPtrList) {
                    bufStart = conditionallyReadBufferFromStartOfPage(fd, buffer, bufSize, bufStart, envPtr2);
                    if (bufStart != 0L) {
                        final String envStr = buffer.getString(envPtr2 - bufStart);
                        final int idx = envStr.indexOf(61);
                        if (idx <= 0) {
                            continue;
                        }
                        env.put(envStr.substring(0, idx), envStr.substring(idx + 1));
                    }
                }
            }
            finally {
                PsInfo.LIBC.close(fd);
            }
        }
        return new Pair<List<String>, Map<String, String>>(args, env);
    }
    
    private static long conditionallyReadBufferFromStartOfPage(final int fd, final Memory buffer, final LibCAPI.size_t bufSize, final long bufStart, final long addr) {
        if (addr >= bufStart && addr - bufStart <= 4096L) {
            return bufStart;
        }
        final long newStart = Math.floorDiv(addr, 4096L) * 4096L;
        final LibCAPI.ssize_t result = PsInfo.LIBC.pread(fd, buffer, bufSize, new NativeLong(newStart));
        if (result.longValue() < 4096L) {
            PsInfo.LOG.debug("Failed to read page from address space: {} bytes read", (Object)result.longValue());
            return 0L;
        }
        return newStart;
    }
    
    private static long getOffsetFromBuffer(final Memory buffer, final long offset, final long increment) {
        return (increment == 8L) ? buffer.getLong(offset) : buffer.getInt(offset);
    }
    
    static {
        LOG = LoggerFactory.getLogger(PsInfo.class);
        LIBC = AixLibc.INSTANCE;
    }
}
