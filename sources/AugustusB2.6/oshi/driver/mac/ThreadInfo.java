// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.mac;

import oshi.software.os.OSProcess;
import oshi.annotation.concurrent.Immutable;
import java.util.regex.Matcher;
import java.util.Iterator;
import oshi.util.ParseUtil;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import oshi.util.ExecutingCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ThreadInfo
{
    private static final Pattern PS_M;
    
    private ThreadInfo() {
    }
    
    public static List<ThreadStats> queryTaskThreads(final int pid) {
        final String pidStr = " " + pid + " ";
        final List<ThreadStats> taskThreads = new ArrayList<ThreadStats>();
        final List<String> psThread = ExecutingCommand.runNative("ps -awwxM").stream().filter(s -> s.contains(pidStr)).collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
        int tid = 0;
        for (final String thread : psThread) {
            final Matcher m = ThreadInfo.PS_M.matcher(thread);
            if (m.matches() && pid == ParseUtil.parseIntOrDefault(m.group(1), -1)) {
                final double cpu = ParseUtil.parseDoubleOrDefault(m.group(2), 0.0);
                final char state = m.group(3).charAt(0);
                final int pri = ParseUtil.parseIntOrDefault(m.group(4), 0);
                final long sTime = ParseUtil.parseDHMSOrDefault(m.group(5), 0L);
                final long uTime = ParseUtil.parseDHMSOrDefault(m.group(6), 0L);
                taskThreads.add(new ThreadStats(tid++, cpu, state, sTime, uTime, pri));
            }
        }
        return taskThreads;
    }
    
    static {
        PS_M = Pattern.compile("\\D+(\\d+).+(\\d+\\.\\d)\\s+(\\w)\\s+(\\d+)\\D+(\\d+:\\d{2}\\.\\d{2})\\s+(\\d+:\\d{2}\\.\\d{2}).+");
    }
    
    @Immutable
    public static class ThreadStats
    {
        private final int threadId;
        private final long userTime;
        private final long systemTime;
        private final long upTime;
        private final OSProcess.State state;
        private final int priority;
        
        public ThreadStats(final int tid, final double cpu, final char state, final long sTime, final long uTime, final int pri) {
            this.threadId = tid;
            this.userTime = uTime;
            this.systemTime = sTime;
            this.upTime = (long)((uTime + sTime) / (cpu / 100.0 + 5.0E-4));
            switch (state) {
                case 'I':
                case 'S': {
                    this.state = OSProcess.State.SLEEPING;
                    break;
                }
                case 'U': {
                    this.state = OSProcess.State.WAITING;
                    break;
                }
                case 'R': {
                    this.state = OSProcess.State.RUNNING;
                    break;
                }
                case 'Z': {
                    this.state = OSProcess.State.ZOMBIE;
                    break;
                }
                case 'T': {
                    this.state = OSProcess.State.STOPPED;
                    break;
                }
                default: {
                    this.state = OSProcess.State.OTHER;
                    break;
                }
            }
            this.priority = pri;
        }
        
        public int getThreadId() {
            return this.threadId;
        }
        
        public long getUserTime() {
            return this.userTime;
        }
        
        public long getSystemTime() {
            return this.systemTime;
        }
        
        public long getUpTime() {
            return this.upTime;
        }
        
        public OSProcess.State getState() {
            return this.state;
        }
        
        public int getPriority() {
            return this.priority;
        }
    }
}
