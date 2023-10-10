// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util.platform.linux;

import java.io.File;
import oshi.util.GlobalConfig;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ProcPath
{
    public static final String PROC;
    public static final String ASOUND;
    public static final String CPUINFO;
    public static final String DISKSTATS;
    public static final String MEMINFO;
    public static final String MOUNTS;
    public static final String NET;
    public static final String PID_CMDLINE;
    public static final String PID_CWD;
    public static final String PID_EXE;
    public static final String PID_ENVIRON;
    public static final String PID_FD;
    public static final String PID_IO;
    public static final String PID_STAT;
    public static final String PID_STATM;
    public static final String PID_STATUS;
    public static final String SELF_STAT;
    public static final String STAT;
    public static final String SYS_FS_FILE_NR;
    public static final String TASK_PATH;
    public static final String TASK_COMM;
    public static final String TASK_STATUS;
    public static final String TASK_STAT;
    public static final String UPTIME;
    public static final String VERSION;
    public static final String VMSTAT;
    
    private ProcPath() {
    }
    
    private static String queryProcConfig() {
        String procPath = GlobalConfig.get("oshi.util.proc.path", "/proc");
        procPath = '/' + procPath.replaceAll("/$|^/", "");
        if (!new File(procPath).exists()) {
            throw new GlobalConfig.PropertyException("oshi.util.proc.path", "The path does not exist");
        }
        return procPath;
    }
    
    static {
        PROC = queryProcConfig();
        ASOUND = ProcPath.PROC + "/asound/";
        CPUINFO = ProcPath.PROC + "/cpuinfo";
        DISKSTATS = ProcPath.PROC + "/diskstats";
        MEMINFO = ProcPath.PROC + "/meminfo";
        MOUNTS = ProcPath.PROC + "/mounts";
        NET = ProcPath.PROC + "/net";
        PID_CMDLINE = ProcPath.PROC + "/%d/cmdline";
        PID_CWD = ProcPath.PROC + "/%d/cwd";
        PID_EXE = ProcPath.PROC + "/%d/exe";
        PID_ENVIRON = ProcPath.PROC + "/%d/environ";
        PID_FD = ProcPath.PROC + "/%d/fd";
        PID_IO = ProcPath.PROC + "/%d/io";
        PID_STAT = ProcPath.PROC + "/%d/stat";
        PID_STATM = ProcPath.PROC + "/%d/statm";
        PID_STATUS = ProcPath.PROC + "/%d/status";
        SELF_STAT = ProcPath.PROC + "/self/stat";
        STAT = ProcPath.PROC + "/stat";
        SYS_FS_FILE_NR = ProcPath.PROC + "/sys/fs/file-nr";
        TASK_PATH = ProcPath.PROC + "/%d/task";
        TASK_COMM = ProcPath.TASK_PATH + "/%d/comm";
        TASK_STATUS = ProcPath.TASK_PATH + "/%d/status";
        TASK_STAT = ProcPath.TASK_PATH + "/%d/stat";
        UPTIME = ProcPath.PROC + "/uptime";
        VERSION = ProcPath.PROC + "/version";
        VMSTAT = ProcPath.PROC + "/vmstat";
    }
}
