// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.common;

import oshi.util.GlobalConfig;
import java.util.Iterator;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.function.Function;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Comparator;
import oshi.software.os.OSProcess;
import java.util.function.Predicate;
import com.sun.jna.Platform;
import oshi.util.Memoizer;
import oshi.util.tuples.Pair;
import java.util.function.Supplier;
import oshi.software.os.OperatingSystem;

public abstract class AbstractOperatingSystem implements OperatingSystem
{
    public static final String OSHI_OS_UNIX_WHOCOMMAND = "oshi.os.unix.whoCommand";
    protected static final boolean USE_WHO_COMMAND;
    private final Supplier<String> manufacturer;
    private final Supplier<Pair<String, OSVersionInfo>> familyVersionInfo;
    private final Supplier<Integer> bitness;
    
    public AbstractOperatingSystem() {
        this.manufacturer = Memoizer.memoize(this::queryManufacturer);
        this.familyVersionInfo = Memoizer.memoize(this::queryFamilyVersionInfo);
        this.bitness = Memoizer.memoize(this::queryPlatformBitness);
    }
    
    @Override
    public String getManufacturer() {
        return this.manufacturer.get();
    }
    
    protected abstract String queryManufacturer();
    
    @Override
    public String getFamily() {
        return this.familyVersionInfo.get().getA();
    }
    
    @Override
    public OSVersionInfo getVersionInfo() {
        return this.familyVersionInfo.get().getB();
    }
    
    protected abstract Pair<String, OSVersionInfo> queryFamilyVersionInfo();
    
    @Override
    public int getBitness() {
        return this.bitness.get();
    }
    
    private int queryPlatformBitness() {
        if (Platform.is64Bit()) {
            return 64;
        }
        final int jvmBitness = System.getProperty("os.arch").contains("64") ? 64 : 32;
        return this.queryBitness(jvmBitness);
    }
    
    protected abstract int queryBitness(final int p0);
    
    @Override
    public List<OSProcess> getProcesses(final Predicate<OSProcess> filter, final Comparator<OSProcess> sort, final int limit) {
        return this.queryAllProcesses().stream().filter((Predicate<? super Object>)((filter == null) ? ProcessFiltering.ALL_PROCESSES : filter)).sorted((Comparator<? super Object>)((sort == null) ? ProcessSorting.NO_SORTING : sort)).limit((limit > 0) ? limit : Long.MAX_VALUE).collect((Collector<? super Object, ?, List<OSProcess>>)Collectors.toList());
    }
    
    protected abstract List<OSProcess> queryAllProcesses();
    
    @Override
    public List<OSProcess> getChildProcesses(final int parentPid, final Predicate<OSProcess> filter, final Comparator<OSProcess> sort, final int limit) {
        final List<OSProcess> childProcs = this.queryChildProcesses(parentPid);
        final OSProcess parent = childProcs.stream().filter(p -> p.getParentProcessID() == parentPid).findAny().orElse(null);
        final long parentStartTime = (parent == null) ? 0L : parent.getStartTime();
        final long n;
        return this.queryChildProcesses(parentPid).stream().filter((Predicate<? super Object>)((filter == null) ? ProcessFiltering.ALL_PROCESSES : filter)).filter(p -> p.getProcessID() != parentPid && p.getStartTime() >= n).sorted((Comparator<? super Object>)((sort == null) ? ProcessSorting.NO_SORTING : sort)).limit((limit > 0) ? limit : Long.MAX_VALUE).collect((Collector<? super Object, ?, List<OSProcess>>)Collectors.toList());
    }
    
    protected abstract List<OSProcess> queryChildProcesses(final int p0);
    
    @Override
    public List<OSProcess> getDescendantProcesses(final int parentPid, final Predicate<OSProcess> filter, final Comparator<OSProcess> sort, final int limit) {
        final List<OSProcess> descendantProcs = this.queryDescendantProcesses(parentPid);
        final OSProcess parent = descendantProcs.stream().filter(p -> p.getParentProcessID() == parentPid).findAny().orElse(null);
        final long parentStartTime = (parent == null) ? 0L : parent.getStartTime();
        final long n;
        return this.queryDescendantProcesses(parentPid).stream().filter((Predicate<? super Object>)((filter == null) ? ProcessFiltering.ALL_PROCESSES : filter)).filter(p -> p.getProcessID() != parentPid && p.getStartTime() >= n).sorted((Comparator<? super Object>)((sort == null) ? ProcessSorting.NO_SORTING : sort)).limit((limit > 0) ? limit : Long.MAX_VALUE).collect((Collector<? super Object, ?, List<OSProcess>>)Collectors.toList());
    }
    
    protected abstract List<OSProcess> queryDescendantProcesses(final int p0);
    
    protected static Set<Integer> getChildrenOrDescendants(final Collection<OSProcess> allProcs, final int parentPid, final boolean allDescendants) {
        final Map<Integer, Integer> parentPidMap = allProcs.stream().collect(Collectors.toMap((Function<? super OSProcess, ? extends Integer>)OSProcess::getProcessID, (Function<? super OSProcess, ? extends Integer>)OSProcess::getParentProcessID));
        return getChildrenOrDescendants(parentPidMap, parentPid, allDescendants);
    }
    
    protected static Set<Integer> getChildrenOrDescendants(final Map<Integer, Integer> parentPidMap, final int parentPid, final boolean allDescendants) {
        final Set<Integer> descendantPids = new HashSet<Integer>();
        descendantPids.add(parentPid);
        final Queue<Integer> queue = new ArrayDeque<Integer>();
        queue.add(parentPid);
        do {
            for (final int pid : getChildren(parentPidMap, queue.poll())) {
                if (!descendantPids.contains(pid)) {
                    descendantPids.add(pid);
                    queue.add(pid);
                }
            }
        } while (allDescendants && !queue.isEmpty());
        return descendantPids;
    }
    
    private static Set<Integer> getChildren(final Map<Integer, Integer> parentPidMap, final int parentPid) {
        return parentPidMap.entrySet().stream().filter(e -> e.getValue().equals(parentPid) && !((Integer)e.getKey()).equals(parentPid)).map((Function<? super Object, ?>)Map.Entry::getKey).collect((Collector<? super Object, ?, Set<Integer>>)Collectors.toSet());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getManufacturer()).append(' ').append(this.getFamily()).append(' ').append(this.getVersionInfo());
        return sb.toString();
    }
    
    static {
        USE_WHO_COMMAND = GlobalConfig.get("oshi.os.unix.whoCommand", false);
    }
}
