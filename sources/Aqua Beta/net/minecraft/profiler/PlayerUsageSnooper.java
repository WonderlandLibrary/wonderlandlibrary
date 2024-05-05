package net.minecraft.profiler;

import com.google.common.collect.Maps;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import net.minecraft.profiler.IPlayerUsage;

public class PlayerUsageSnooper {
    private final Map<String, Object> snooperStats = Maps.newHashMap();
    private final Map<String, Object> clientStats = Maps.newHashMap();
    private final String uniqueID = UUID.randomUUID().toString();
    private final URL serverUrl;
    private final IPlayerUsage playerStatsCollector;
    private final Timer threadTrigger = new Timer("Snooper Timer", true);
    private final Object syncLock = new Object();
    private final long minecraftStartTimeMilis;
    private boolean isRunning;
    private int selfCounter;

    public PlayerUsageSnooper(String side, IPlayerUsage playerStatCollector, long startTime) {
        try {
            this.serverUrl = new URL("http://snoop.minecraft.net/" + side + "?version=" + 2);
        }
        catch (MalformedURLException var6) {
            throw new IllegalArgumentException();
        }
        this.playerStatsCollector = playerStatCollector;
        this.minecraftStartTimeMilis = startTime;
    }

    public void startSnooper() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.addOSData();
            this.threadTrigger.schedule((TimerTask)new /* Unavailable Anonymous Inner Class!! */, 0L, 900000L);
        }
    }

    private void addOSData() {
        this.addJvmArgsToSnooper();
        this.addClientStat("snooper_token", this.uniqueID);
        this.addStatToSnooper("snooper_token", this.uniqueID);
        this.addStatToSnooper("os_name", System.getProperty((String)"os.name"));
        this.addStatToSnooper("os_version", System.getProperty((String)"os.version"));
        this.addStatToSnooper("os_architecture", System.getProperty((String)"os.arch"));
        this.addStatToSnooper("java_version", System.getProperty((String)"java.version"));
        this.addClientStat("version", "1.8.9");
        this.playerStatsCollector.addServerTypeToSnooper(this);
    }

    private void addJvmArgsToSnooper() {
        RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
        List list = runtimemxbean.getInputArguments();
        int i = 0;
        for (String s : list) {
            if (!s.startsWith("-X")) continue;
            this.addClientStat("jvm_arg[" + i++ + "]", s);
        }
        this.addClientStat("jvm_args", i);
    }

    public void addMemoryStatsToSnooper() {
        this.addStatToSnooper("memory_total", Runtime.getRuntime().totalMemory());
        this.addStatToSnooper("memory_max", Runtime.getRuntime().maxMemory());
        this.addStatToSnooper("memory_free", Runtime.getRuntime().freeMemory());
        this.addStatToSnooper("cpu_cores", Runtime.getRuntime().availableProcessors());
        this.playerStatsCollector.addServerStatsToSnooper(this);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addClientStat(String statName, Object statValue) {
        Object object = this.syncLock;
        synchronized (object) {
            this.clientStats.put((Object)statName, statValue);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void addStatToSnooper(String statName, Object statValue) {
        Object object = this.syncLock;
        synchronized (object) {
            this.snooperStats.put((Object)statName, statValue);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public Map<String, String> getCurrentStats() {
        LinkedHashMap map = Maps.newLinkedHashMap();
        Object object = this.syncLock;
        synchronized (object) {
            this.addMemoryStatsToSnooper();
            for (Map.Entry entry : this.snooperStats.entrySet()) {
                map.put(entry.getKey(), (Object)entry.getValue().toString());
            }
            for (Map.Entry entry1 : this.clientStats.entrySet()) {
                map.put(entry1.getKey(), (Object)entry1.getValue().toString());
            }
            return map;
        }
    }

    public boolean isSnooperRunning() {
        return this.isRunning;
    }

    public void stopSnooper() {
        this.threadTrigger.cancel();
    }

    public String getUniqueID() {
        return this.uniqueID;
    }

    public long getMinecraftStartTimeMillis() {
        return this.minecraftStartTimeMilis;
    }

    static /* synthetic */ IPlayerUsage access$000(PlayerUsageSnooper x0) {
        return x0.playerStatsCollector;
    }

    static /* synthetic */ Object access$100(PlayerUsageSnooper x0) {
        return x0.syncLock;
    }

    static /* synthetic */ Map access$200(PlayerUsageSnooper x0) {
        return x0.clientStats;
    }

    static /* synthetic */ int access$300(PlayerUsageSnooper x0) {
        return x0.selfCounter;
    }

    static /* synthetic */ Map access$400(PlayerUsageSnooper x0) {
        return x0.snooperStats;
    }

    static /* synthetic */ int access$308(PlayerUsageSnooper x0) {
        return x0.selfCounter++;
    }

    static /* synthetic */ String access$500(PlayerUsageSnooper x0) {
        return x0.uniqueID;
    }

    static /* synthetic */ URL access$600(PlayerUsageSnooper x0) {
        return x0.serverUrl;
    }
}
