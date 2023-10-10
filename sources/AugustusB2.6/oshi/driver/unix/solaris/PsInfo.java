// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.unix.solaris;

import oshi.util.ParseUtil;
import oshi.util.ExecutingCommand;
import org.slf4j.LoggerFactory;
import com.sun.jna.NativeLong;
import java.util.Iterator;
import com.sun.jna.platform.unix.LibCAPI;
import com.sun.jna.Memory;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import oshi.util.tuples.Pair;
import com.sun.jna.Pointer;
import oshi.util.tuples.Quartet;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import oshi.jna.platform.unix.SolarisLibc;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class PsInfo
{
    private static final Logger LOG;
    private static final SolarisLibc LIBC;
    private static final long PAGE_SIZE;
    
    private PsInfo() {
    }
    
    public static SolarisLibc.SolarisPsInfo queryPsInfo(final int pid) {
        final Path path = Paths.get(String.format("/proc/%d/psinfo", pid), new String[0]);
        try {
            return new SolarisLibc.SolarisPsInfo(Files.readAllBytes(path));
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public static SolarisLibc.SolarisLwpsInfo queryLwpsInfo(final int pid, final int tid) {
        final Path path = Paths.get(String.format("/proc/%d/lwp/%d/lwpsinfo", pid, tid), new String[0]);
        try {
            return new SolarisLibc.SolarisLwpsInfo(Files.readAllBytes(path));
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public static SolarisLibc.SolarisPrUsage queryPrUsage(final int pid) {
        final Path path = Paths.get(String.format("/proc/%d/usage", pid), new String[0]);
        try {
            return new SolarisLibc.SolarisPrUsage(Files.readAllBytes(path));
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public static SolarisLibc.SolarisPrUsage queryPrUsage(final int pid, final int tid) {
        final Path path = Paths.get(String.format("/proc/%d/lwp/%d/usage", pid, tid), new String[0]);
        try {
            return new SolarisLibc.SolarisPrUsage(Files.readAllBytes(path));
        }
        catch (IOException e) {
            return null;
        }
    }
    
    public static Quartet<Integer, Long, Long, Byte> queryArgsEnvAddrs(final int pid, final SolarisLibc.SolarisPsInfo psinfo) {
        if (psinfo == null) {
            PsInfo.LOG.trace("Failed to read psinfo file for pid: {} ", (Object)pid);
            return null;
        }
        final int argc = psinfo.pr_argc;
        if (argc <= 0) {
            PsInfo.LOG.trace("Failed argc sanity check: argc={}", (Object)argc);
            return null;
        }
        final long argv = Pointer.nativeValue(psinfo.pr_argv);
        final long envp = Pointer.nativeValue(psinfo.pr_envp);
        final byte dmodel = psinfo.pr_dmodel;
        if (dmodel * 4 == (envp - argv) / (argc + 1)) {
            return new Quartet<Integer, Long, Long, Byte>(argc, argv, envp, dmodel);
        }
        PsInfo.LOG.trace("Failed data model and offset increment sanity check: dm={} diff={}", (Object)dmodel, envp - argv);
        return null;
    }
    
    public static Pair<List<String>, Map<String, String>> queryArgsEnv(final int pid, final SolarisLibc.SolarisPsInfo psinfo) {
        final List<String> args = new ArrayList<String>();
        final Map<String, String> env = new LinkedHashMap<String, String>();
        final Quartet<Integer, Long, Long, Byte> addrs = queryArgsEnvAddrs(pid, psinfo);
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
                final long increment = addrs.getD() * 4L;
                long bufStart = 0L;
                final Memory buffer = new Memory(PsInfo.PAGE_SIZE * 2L);
                final LibCAPI.size_t bufSize = new LibCAPI.size_t(buffer.size());
                final long[] argp = new long[argc];
                long offset = argv;
                for (int i = 0; i < argc; ++i) {
                    bufStart = conditionallyReadBufferFromStartOfPage(fd, buffer, bufSize, bufStart, offset);
                    argp[i] = ((bufStart == 0L) ? 0L : getOffsetFromBuffer(buffer, offset - bufStart, increment));
                    offset += increment;
                }
                final List<Long> envPtrList = new ArrayList<Long>();
                offset = envp;
                long addr = 0L;
                int limit = 500;
                do {
                    bufStart = conditionallyReadBufferFromStartOfPage(fd, buffer, bufSize, bufStart, offset);
                    addr = ((bufStart == 0L) ? 0L : getOffsetFromBuffer(buffer, offset - bufStart, increment));
                    if (addr != 0L) {
                        envPtrList.add(addr);
                    }
                    offset += increment;
                } while (addr != 0L && --limit > 0);
                for (int j = 0; j < argp.length && argp[j] != 0L; ++j) {
                    bufStart = conditionallyReadBufferFromStartOfPage(fd, buffer, bufSize, bufStart, argp[j]);
                    if (bufStart != 0L) {
                        final String argStr = buffer.getString(argp[j] - bufStart);
                        if (!argStr.isEmpty()) {
                            args.add(argStr);
                        }
                    }
                }
                for (final Long envPtr : envPtrList) {
                    bufStart = conditionallyReadBufferFromStartOfPage(fd, buffer, bufSize, bufStart, envPtr);
                    if (bufStart != 0L) {
                        final String envStr = buffer.getString(envPtr - bufStart);
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
        if (addr >= bufStart && addr - bufStart <= PsInfo.PAGE_SIZE) {
            return bufStart;
        }
        final long newStart = Math.floorDiv(addr, PsInfo.PAGE_SIZE) * PsInfo.PAGE_SIZE;
        final LibCAPI.ssize_t result = PsInfo.LIBC.pread(fd, buffer, bufSize, new NativeLong(newStart));
        if (result.longValue() < PsInfo.PAGE_SIZE) {
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
        LIBC = SolarisLibc.INSTANCE;
        PAGE_SIZE = ParseUtil.parseLongOrDefault(ExecutingCommand.getFirstAnswer("pagesize"), 4096L);
    }
}
