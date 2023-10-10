// 
// Decompiled by Procyon v0.5.36
// 

package oshi.driver.windows.registry;

import oshi.annotation.concurrent.Immutable;
import oshi.util.tuples.Pair;
import com.sun.jna.platform.win32.WinBase;
import java.util.Iterator;
import oshi.util.tuples.Triplet;
import java.util.HashMap;
import java.util.List;
import oshi.driver.windows.perfmon.ThreadInformation;
import java.util.Map;
import java.util.Collection;
import oshi.annotation.concurrent.ThreadSafe;

@ThreadSafe
public final class ThreadPerformanceData
{
    private static final String THREAD = "Thread";
    
    private ThreadPerformanceData() {
    }
    
    public static Map<Integer, PerfCounterBlock> buildThreadMapFromRegistry(final Collection<Integer> pids) {
        final Triplet<List<Map<ThreadInformation.ThreadPerformanceProperty, Object>>, Long, Long> threadData = HkeyPerformanceDataUtil.readPerfDataFromRegistry("Thread", ThreadInformation.ThreadPerformanceProperty.class);
        if (threadData == null) {
            return null;
        }
        final List<Map<ThreadInformation.ThreadPerformanceProperty, Object>> threadInstanceMaps = threadData.getA();
        final long perfTime100nSec = threadData.getB();
        final long now = threadData.getC();
        final Map<Integer, PerfCounterBlock> threadMap = new HashMap<Integer, PerfCounterBlock>();
        for (final Map<ThreadInformation.ThreadPerformanceProperty, Object> threadInstanceMap : threadInstanceMaps) {
            final int pid = threadInstanceMap.get(ThreadInformation.ThreadPerformanceProperty.IDPROCESS);
            if ((pids == null || pids.contains(pid)) && pid > 0) {
                final int tid = threadInstanceMap.get(ThreadInformation.ThreadPerformanceProperty.IDTHREAD);
                final String name = threadInstanceMap.get(ThreadInformation.ThreadPerformanceProperty.NAME);
                long upTime = (perfTime100nSec - threadInstanceMap.get(ThreadInformation.ThreadPerformanceProperty.ELAPSEDTIME)) / 10000L;
                if (upTime < 1L) {
                    upTime = 1L;
                }
                final long user = threadInstanceMap.get(ThreadInformation.ThreadPerformanceProperty.PERCENTUSERTIME) / 10000L;
                final long kernel = threadInstanceMap.get(ThreadInformation.ThreadPerformanceProperty.PERCENTPRIVILEGEDTIME) / 10000L;
                final int priority = threadInstanceMap.get(ThreadInformation.ThreadPerformanceProperty.PRIORITYCURRENT);
                final int threadState = threadInstanceMap.get(ThreadInformation.ThreadPerformanceProperty.THREADSTATE);
                final int threadWaitReason = threadInstanceMap.get(ThreadInformation.ThreadPerformanceProperty.THREADWAITREASON);
                final Object addr = threadInstanceMap.get(ThreadInformation.ThreadPerformanceProperty.STARTADDRESS);
                final long startAddr = (long)(addr.getClass().equals(Long.class) ? addr : Integer.toUnsignedLong((int)addr));
                final int contextSwitches = threadInstanceMap.get(ThreadInformation.ThreadPerformanceProperty.CONTEXTSWITCHESPERSEC);
                threadMap.put(tid, new PerfCounterBlock(name, tid, pid, now - upTime, user, kernel, priority, threadState, threadWaitReason, startAddr, contextSwitches));
            }
        }
        return threadMap;
    }
    
    public static Map<Integer, PerfCounterBlock> buildThreadMapFromPerfCounters(final Collection<Integer> pids) {
        final Map<Integer, PerfCounterBlock> threadMap = new HashMap<Integer, PerfCounterBlock>();
        final Pair<List<String>, Map<ThreadInformation.ThreadPerformanceProperty, List<Long>>> instanceValues = ThreadInformation.queryThreadCounters();
        final long now = System.currentTimeMillis();
        final List<String> instances = instanceValues.getA();
        final Map<ThreadInformation.ThreadPerformanceProperty, List<Long>> valueMap = instanceValues.getB();
        final List<Long> tidList = valueMap.get(ThreadInformation.ThreadPerformanceProperty.IDTHREAD);
        final List<Long> pidList = valueMap.get(ThreadInformation.ThreadPerformanceProperty.IDPROCESS);
        final List<Long> userList = valueMap.get(ThreadInformation.ThreadPerformanceProperty.PERCENTUSERTIME);
        final List<Long> kernelList = valueMap.get(ThreadInformation.ThreadPerformanceProperty.PERCENTPRIVILEGEDTIME);
        final List<Long> startTimeList = valueMap.get(ThreadInformation.ThreadPerformanceProperty.ELAPSEDTIME);
        final List<Long> priorityList = valueMap.get(ThreadInformation.ThreadPerformanceProperty.PRIORITYCURRENT);
        final List<Long> stateList = valueMap.get(ThreadInformation.ThreadPerformanceProperty.THREADSTATE);
        final List<Long> waitReasonList = valueMap.get(ThreadInformation.ThreadPerformanceProperty.THREADWAITREASON);
        final List<Long> startAddrList = valueMap.get(ThreadInformation.ThreadPerformanceProperty.STARTADDRESS);
        final List<Long> contextSwitchesList = valueMap.get(ThreadInformation.ThreadPerformanceProperty.CONTEXTSWITCHESPERSEC);
        int nameIndex = 0;
        for (int inst = 0; inst < instances.size(); ++inst) {
            final int pid = pidList.get(inst).intValue();
            if (pids == null || pids.contains(pid)) {
                final int tid = tidList.get(inst).intValue();
                final String name = Integer.toString(nameIndex++);
                long startTime = startTimeList.get(inst);
                startTime = WinBase.FILETIME.filetimeToDate((int)(startTime >> 32), (int)(startTime & 0xFFFFFFFFL)).getTime();
                if (startTime > now) {
                    startTime = now - 1L;
                }
                final long user = userList.get(inst) / 10000L;
                final long kernel = kernelList.get(inst) / 10000L;
                final int priority = priorityList.get(inst).intValue();
                final int threadState = stateList.get(inst).intValue();
                final int threadWaitReason = waitReasonList.get(inst).intValue();
                final long startAddr = startAddrList.get(inst);
                final int contextSwitches = contextSwitchesList.get(inst).intValue();
                threadMap.put(tid, new PerfCounterBlock(name, tid, pid, startTime, user, kernel, priority, threadState, threadWaitReason, startAddr, contextSwitches));
            }
        }
        return threadMap;
    }
    
    @Immutable
    public static class PerfCounterBlock
    {
        private final String name;
        private final int threadID;
        private final int owningProcessID;
        private final long startTime;
        private final long userTime;
        private final long kernelTime;
        private final int priority;
        private final int threadState;
        private final int threadWaitReason;
        private final long startAddress;
        private final int contextSwitches;
        
        public PerfCounterBlock(final String name, final int threadID, final int owningProcessID, final long startTime, final long userTime, final long kernelTime, final int priority, final int threadState, final int threadWaitReason, final long startAddress, final int contextSwitches) {
            this.name = name;
            this.threadID = threadID;
            this.owningProcessID = owningProcessID;
            this.startTime = startTime;
            this.userTime = userTime;
            this.kernelTime = kernelTime;
            this.priority = priority;
            this.threadState = threadState;
            this.threadWaitReason = threadWaitReason;
            this.startAddress = startAddress;
            this.contextSwitches = contextSwitches;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getThreadID() {
            return this.threadID;
        }
        
        public int getOwningProcessID() {
            return this.owningProcessID;
        }
        
        public long getStartTime() {
            return this.startTime;
        }
        
        public long getUserTime() {
            return this.userTime;
        }
        
        public long getKernelTime() {
            return this.kernelTime;
        }
        
        public int getPriority() {
            return this.priority;
        }
        
        public int getThreadState() {
            return this.threadState;
        }
        
        public int getThreadWaitReason() {
            return this.threadWaitReason;
        }
        
        public long getStartAddress() {
            return this.startAddress;
        }
        
        public int getContextSwitches() {
            return this.contextSwitches;
        }
    }
}
