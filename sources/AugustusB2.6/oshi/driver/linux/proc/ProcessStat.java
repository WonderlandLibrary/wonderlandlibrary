// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.linux.proc;

import oshi.software.os.OSProcess;
import oshi.util.Constants;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.HashMap;
import java.io.File;
import java.util.EnumMap;
import oshi.util.ParseUtil;
import oshi.util.FileUtil;
import oshi.util.platform.linux.ProcPath;
import java.util.Map;
import oshi.util.tuples.Triplet;
import java.util.regex.Pattern;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ProcessStat
{
    private static final Pattern SOCKET;
    public static final int PROC_PID_STAT_LENGTH;
    
    private ProcessStat() {
    }
    
    public static Triplet<String, Character, Map<PidStat, Long>> getPidStats(final int pid) {
        final String stat = FileUtil.getStringFromFile(String.format(ProcPath.PID_STAT, pid));
        if (stat.isEmpty()) {
            return null;
        }
        final int nameStart = stat.indexOf(40) + 1;
        final int nameEnd = stat.indexOf(41);
        final String name = stat.substring(nameStart, nameEnd);
        final Character state = stat.charAt(nameEnd + 2);
        final String[] split = ParseUtil.whitespaces.split(stat.substring(nameEnd + 4).trim());
        final Map<PidStat, Long> statMap = new EnumMap<PidStat, Long>(PidStat.class);
        final PidStat[] enumArray = PidStat.class.getEnumConstants();
        for (int i = 3; i < enumArray.length && i - 3 < split.length; ++i) {
            statMap.put(enumArray[i], ParseUtil.parseLongOrDefault(split[i - 3], 0L));
        }
        return new Triplet<String, Character, Map<PidStat, Long>>(name, state, statMap);
    }
    
    public static Map<PidStatM, Long> getPidStatM(final int pid) {
        final String statm = FileUtil.getStringFromFile(String.format(ProcPath.PID_STATM, pid));
        if (statm.isEmpty()) {
            return null;
        }
        final String[] split = ParseUtil.whitespaces.split(statm);
        final Map<PidStatM, Long> statmMap = new EnumMap<PidStatM, Long>(PidStatM.class);
        final PidStatM[] enumArray = PidStatM.class.getEnumConstants();
        for (int i = 0; i < enumArray.length && i < split.length; ++i) {
            statmMap.put(enumArray[i], ParseUtil.parseLongOrDefault(split[i], 0L));
        }
        return statmMap;
    }
    
    public static File[] getFileDescriptorFiles(final int pid) {
        return listNumericFiles(String.format(ProcPath.PID_FD, pid));
    }
    
    public static File[] getPidFiles() {
        return listNumericFiles(ProcPath.PROC);
    }
    
    public static Map<Integer, Integer> querySocketToPidMap() {
        final Map<Integer, Integer> pidMap = new HashMap<Integer, Integer>();
        for (final File f : getPidFiles()) {
            final int pid = ParseUtil.parseIntOrDefault(f.getName(), -1);
            final File[] fileDescriptorFiles;
            final File[] fds = fileDescriptorFiles = getFileDescriptorFiles(pid);
            for (final File fd : fileDescriptorFiles) {
                final String symLink = FileUtil.readSymlinkTarget(fd);
                if (symLink != null) {
                    final Matcher m = ProcessStat.SOCKET.matcher(symLink);
                    if (m.matches()) {
                        pidMap.put(ParseUtil.parseIntOrDefault(m.group(1), -1), pid);
                    }
                }
            }
        }
        return pidMap;
    }
    
    public static List<Integer> getThreadIds(final int pid) {
        final File[] threads = listNumericFiles(String.format(ProcPath.TASK_PATH, pid));
        return Arrays.stream(threads).map(thread -> ParseUtil.parseIntOrDefault(thread.getName(), 0)).filter(threadId -> threadId != pid).collect((Collector<? super Object, ?, List<Integer>>)Collectors.toList());
    }
    
    private static File[] listNumericFiles(final String path) {
        final File directory = new File(path);
        final File[] numericFiles = directory.listFiles(file -> Constants.DIGITS.matcher(file.getName()).matches());
        return (numericFiles == null) ? new File[0] : numericFiles;
    }
    
    public static OSProcess.State getState(final char stateValue) {
        OSProcess.State state = null;
        switch (stateValue) {
            case 'R': {
                state = OSProcess.State.RUNNING;
                break;
            }
            case 'S': {
                state = OSProcess.State.SLEEPING;
                break;
            }
            case 'D': {
                state = OSProcess.State.WAITING;
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
        SOCKET = Pattern.compile("socket:\\[(\\d+)\\]");
        final String stat = FileUtil.getStringFromFile(ProcPath.SELF_STAT);
        if (stat.contains(")")) {
            PROC_PID_STAT_LENGTH = ParseUtil.countStringToLongArray(stat, ' ') + 3;
        }
        else {
            PROC_PID_STAT_LENGTH = 52;
        }
    }
    
    public enum PidStat
    {
        PID, 
        COMM, 
        STATE, 
        PPID, 
        PGRP, 
        SESSION, 
        TTY_NR, 
        PTGID, 
        FLAGS, 
        MINFLT, 
        CMINFLT, 
        MAJFLT, 
        CMAJFLT, 
        UTIME, 
        STIME, 
        CUTIME, 
        CSTIME, 
        PRIORITY, 
        NICE, 
        NUM_THREADS, 
        ITREALVALUE, 
        STARTTIME, 
        VSIZE, 
        RSS, 
        RSSLIM, 
        STARTCODE, 
        ENDCODE, 
        STARTSTACK, 
        KSTKESP, 
        KSTKEIP, 
        SIGNAL, 
        BLOCKED, 
        SIGIGNORE, 
        SIGCATCH, 
        WCHAN, 
        NSWAP, 
        CNSWAP, 
        EXIT_SIGNAL, 
        PROCESSOR, 
        RT_PRIORITY, 
        POLICY, 
        DELAYACCT_BLKIO_TICKS, 
        GUEST_TIME, 
        CGUEST_TIME, 
        START_DATA, 
        END_DATA, 
        START_BRK, 
        ARG_START, 
        ARG_END, 
        ENV_START, 
        ENV_END, 
        EXIT_CODE;
        
        private static /* synthetic */ PidStat[] $values() {
            return new PidStat[] { PidStat.PID, PidStat.COMM, PidStat.STATE, PidStat.PPID, PidStat.PGRP, PidStat.SESSION, PidStat.TTY_NR, PidStat.PTGID, PidStat.FLAGS, PidStat.MINFLT, PidStat.CMINFLT, PidStat.MAJFLT, PidStat.CMAJFLT, PidStat.UTIME, PidStat.STIME, PidStat.CUTIME, PidStat.CSTIME, PidStat.PRIORITY, PidStat.NICE, PidStat.NUM_THREADS, PidStat.ITREALVALUE, PidStat.STARTTIME, PidStat.VSIZE, PidStat.RSS, PidStat.RSSLIM, PidStat.STARTCODE, PidStat.ENDCODE, PidStat.STARTSTACK, PidStat.KSTKESP, PidStat.KSTKEIP, PidStat.SIGNAL, PidStat.BLOCKED, PidStat.SIGIGNORE, PidStat.SIGCATCH, PidStat.WCHAN, PidStat.NSWAP, PidStat.CNSWAP, PidStat.EXIT_SIGNAL, PidStat.PROCESSOR, PidStat.RT_PRIORITY, PidStat.POLICY, PidStat.DELAYACCT_BLKIO_TICKS, PidStat.GUEST_TIME, PidStat.CGUEST_TIME, PidStat.START_DATA, PidStat.END_DATA, PidStat.START_BRK, PidStat.ARG_START, PidStat.ARG_END, PidStat.ENV_START, PidStat.ENV_END, PidStat.EXIT_CODE };
        }
        
        static {
            $VALUES = $values();
        }
    }
    
    public enum PidStatM
    {
        SIZE, 
        RESIDENT, 
        SHARED, 
        TEXT, 
        LIB, 
        DATA, 
        DT;
        
        private static /* synthetic */ PidStatM[] $values() {
            return new PidStatM[] { PidStatM.SIZE, PidStatM.RESIDENT, PidStatM.SHARED, PidStatM.TEXT, PidStatM.LIB, PidStatM.DATA, PidStatM.DT };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
