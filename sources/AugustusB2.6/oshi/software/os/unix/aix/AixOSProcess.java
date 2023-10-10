// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.unix.aix;

import org.slf4j.LoggerFactory;
import com.sun.jna.Native;
import oshi.util.UserGroupInfo;
import java.util.ArrayList;
import oshi.software.os.OSThread;
import oshi.util.ParseUtil;
import oshi.util.Constants;
import java.util.Iterator;
import oshi.util.ExecutingCommand;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.File;
import oshi.driver.unix.aix.PsInfo;
import oshi.driver.unix.aix.perfstat.PerfstatCpu;
import oshi.util.Memoizer;
import com.sun.jna.platform.unix.aix.Perfstat;
import oshi.software.os.OSProcess;
import java.util.Map;
import java.util.List;
import oshi.util.tuples.Pair;
import oshi.jna.platform.unix.AixLibc;
import java.util.function.Supplier;
import org.slf4j.Logger;
import oshi.annotation.concurrent.ThreadSafe;
import oshi.software.common.AbstractOSProcess;

@ThreadSafe
public class AixOSProcess extends AbstractOSProcess
{
    private static final Logger LOG;
    private Supplier<Integer> bitness;
    private Supplier<AixLibc.AixPsInfo> psinfo;
    private Supplier<String> commandLine;
    private Supplier<Pair<List<String>, Map<String, String>>> cmdEnv;
    private final Supplier<Long> affinityMask;
    private String name;
    private String path;
    private String commandLineBackup;
    private String user;
    private String userID;
    private String group;
    private String groupID;
    private OSProcess.State state;
    private int parentProcessID;
    private int threadCount;
    private int priority;
    private long virtualSize;
    private long residentSetSize;
    private long kernelTime;
    private long userTime;
    private long startTime;
    private long upTime;
    private long bytesRead;
    private long bytesWritten;
    private Supplier<Perfstat.perfstat_process_t[]> procCpu;
    
    public AixOSProcess(final int pid, final Pair<Long, Long> userSysCpuTime, final Supplier<Perfstat.perfstat_process_t[]> procCpu) {
        super(pid);
        this.bitness = Memoizer.memoize(this::queryBitness);
        this.psinfo = Memoizer.memoize(this::queryPsInfo, Memoizer.defaultExpiration());
        this.commandLine = Memoizer.memoize(this::queryCommandLine);
        this.cmdEnv = Memoizer.memoize(this::queryCommandlineEnvironment);
        this.affinityMask = Memoizer.memoize(PerfstatCpu::queryCpuAffinityMask, Memoizer.defaultExpiration());
        this.path = "";
        this.state = OSProcess.State.INVALID;
        this.procCpu = procCpu;
        this.updateAttributes(userSysCpuTime);
    }
    
    private AixLibc.AixPsInfo queryPsInfo() {
        return PsInfo.queryPsInfo(this.getProcessID());
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public String getPath() {
        return this.path;
    }
    
    @Override
    public String getCommandLine() {
        return this.commandLine.get();
    }
    
    private String queryCommandLine() {
        final String cl = String.join(" ", this.getArguments());
        return cl.isEmpty() ? this.commandLineBackup : cl;
    }
    
    @Override
    public List<String> getArguments() {
        return this.cmdEnv.get().getA();
    }
    
    @Override
    public Map<String, String> getEnvironmentVariables() {
        return this.cmdEnv.get().getB();
    }
    
    private Pair<List<String>, Map<String, String>> queryCommandlineEnvironment() {
        return PsInfo.queryArgsEnv(this.getProcessID(), this.psinfo.get());
    }
    
    @Override
    public String getCurrentWorkingDirectory() {
        try {
            final String cwdLink = "/proc" + this.getProcessID() + "/cwd";
            final String cwd = new File(cwdLink).getCanonicalPath();
            if (!cwd.equals(cwdLink)) {
                return cwd;
            }
        }
        catch (IOException e) {
            AixOSProcess.LOG.trace("Couldn't find cwd for pid {}: {}", (Object)this.getProcessID(), e.getMessage());
        }
        return "";
    }
    
    @Override
    public String getUser() {
        return this.user;
    }
    
    @Override
    public String getUserID() {
        return this.userID;
    }
    
    @Override
    public String getGroup() {
        return this.group;
    }
    
    @Override
    public String getGroupID() {
        return this.groupID;
    }
    
    @Override
    public OSProcess.State getState() {
        return this.state;
    }
    
    @Override
    public int getParentProcessID() {
        return this.parentProcessID;
    }
    
    @Override
    public int getThreadCount() {
        return this.threadCount;
    }
    
    @Override
    public int getPriority() {
        return this.priority;
    }
    
    @Override
    public long getVirtualSize() {
        return this.virtualSize;
    }
    
    @Override
    public long getResidentSetSize() {
        return this.residentSetSize;
    }
    
    @Override
    public long getKernelTime() {
        return this.kernelTime;
    }
    
    @Override
    public long getUserTime() {
        return this.userTime;
    }
    
    @Override
    public long getUpTime() {
        return this.upTime;
    }
    
    @Override
    public long getStartTime() {
        return this.startTime;
    }
    
    @Override
    public long getBytesRead() {
        return this.bytesRead;
    }
    
    @Override
    public long getBytesWritten() {
        return this.bytesWritten;
    }
    
    @Override
    public long getOpenFiles() {
        try {
            final Stream<Path> fd = Files.list(Paths.get("/proc/" + this.getProcessID() + "/fd", new String[0]));
            try {
                final long count = fd.count();
                if (fd != null) {
                    fd.close();
                }
                return count;
            }
            catch (Throwable t) {
                if (fd != null) {
                    try {
                        fd.close();
                    }
                    catch (Throwable exception) {
                        t.addSuppressed(exception);
                    }
                }
                throw t;
            }
        }
        catch (IOException e) {
            return 0L;
        }
    }
    
    @Override
    public int getBitness() {
        return this.bitness.get();
    }
    
    private int queryBitness() {
        final List<String> pflags = ExecutingCommand.runNative("pflags " + this.getProcessID());
        for (final String line : pflags) {
            if (line.contains("data model")) {
                if (line.contains("LP32")) {
                    return 32;
                }
                if (line.contains("LP64")) {
                    return 64;
                }
                continue;
            }
        }
        return 0;
    }
    
    @Override
    public long getAffinityMask() {
        long mask = 0L;
        final File directory = new File(String.format("/proc/%d/lwp", this.getProcessID()));
        final File[] numericFiles = directory.listFiles(file -> Constants.DIGITS.matcher(file.getName()).matches());
        if (numericFiles == null) {
            return mask;
        }
        for (final File lwpidFile : numericFiles) {
            final int lwpidNum = ParseUtil.parseIntOrDefault(lwpidFile.getName(), 0);
            final AixLibc.AIXLwpsInfo info = PsInfo.queryLwpsInfo(this.getProcessID(), lwpidNum);
            if (info != null) {
                mask |= info.pr_bindpro;
            }
        }
        mask &= this.affinityMask.get();
        return mask;
    }
    
    @Override
    public List<OSThread> getThreadDetails() {
        final List<OSThread> threads = new ArrayList<OSThread>();
        final File directory = new File(String.format("/proc/%d/lwp", this.getProcessID()));
        final File[] numericFiles = directory.listFiles(file -> Constants.DIGITS.matcher(file.getName()).matches());
        if (numericFiles == null) {
            return threads;
        }
        for (final File lwpidFile : numericFiles) {
            final int lwpidNum = ParseUtil.parseIntOrDefault(lwpidFile.getName(), 0);
            final OSThread thread = new AixOSThread(this.getProcessID(), lwpidNum);
            if (thread.getState() != OSProcess.State.INVALID) {
                threads.add(thread);
            }
        }
        return threads;
    }
    
    @Override
    public boolean updateAttributes() {
        final Perfstat.perfstat_process_t[] array;
        final Perfstat.perfstat_process_t[] perfstat = array = this.procCpu.get();
        for (final Perfstat.perfstat_process_t stat : array) {
            final int statpid = (int)stat.pid;
            if (statpid == this.getProcessID()) {
                return this.updateAttributes(new Pair<Long, Long>((long)stat.ucpu_time, (long)stat.scpu_time));
            }
        }
        this.state = OSProcess.State.INVALID;
        return false;
    }
    
    private boolean updateAttributes(final Pair<Long, Long> userSysCpuTime) {
        final AixLibc.AixPsInfo info = this.psinfo.get();
        if (info == null) {
            this.state = OSProcess.State.INVALID;
            return false;
        }
        final long now = System.currentTimeMillis();
        this.state = getStateFromOutput((char)info.pr_lwp.pr_sname);
        this.parentProcessID = (int)info.pr_ppid;
        this.userID = Long.toString(info.pr_euid);
        this.user = UserGroupInfo.getUser(this.userID);
        this.groupID = Long.toString(info.pr_egid);
        this.group = UserGroupInfo.getGroupName(this.groupID);
        this.threadCount = info.pr_nlwp;
        this.priority = info.pr_lwp.pr_pri;
        this.virtualSize = info.pr_size * 1024L;
        this.residentSetSize = info.pr_rssize * 1024L;
        this.startTime = info.pr_start.tv_sec * 1000L + info.pr_start.tv_nsec / 1000000L;
        final long elapsedTime = now - this.startTime;
        this.upTime = ((elapsedTime < 1L) ? 1L : elapsedTime);
        this.userTime = userSysCpuTime.getA();
        this.kernelTime = userSysCpuTime.getB();
        this.commandLineBackup = Native.toString(info.pr_psargs);
        this.path = ParseUtil.whitespaces.split(this.commandLineBackup)[0];
        this.name = this.path.substring(this.path.lastIndexOf(47) + 1);
        if (this.name.isEmpty()) {
            this.name = Native.toString(info.pr_fname);
        }
        return true;
    }
    
    static OSProcess.State getStateFromOutput(final char stateValue) {
        OSProcess.State state = null;
        switch (stateValue) {
            case 'O': {
                state = OSProcess.State.INVALID;
                break;
            }
            case 'A':
            case 'R': {
                state = OSProcess.State.RUNNING;
                break;
            }
            case 'I': {
                state = OSProcess.State.WAITING;
                break;
            }
            case 'S':
            case 'W': {
                state = OSProcess.State.SLEEPING;
                break;
            }
            case 'Z': {
                state = OSProcess.State.ZOMBIE;
                break;
            }
            case 'T': {
                state = OSProcess.State.STOPPED;
                break;
            }
            default: {
                state = OSProcess.State.OTHER;
                break;
            }
        }
        return state;
    }
    
    static {
        LOG = LoggerFactory.getLogger(AixOSProcess.class);
    }
}
