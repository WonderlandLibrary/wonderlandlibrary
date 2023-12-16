// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.profiler;

import java.util.Iterator;
import java.util.List;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ManagementFactory;
import net.minecraft.util.HttpUtil;
import java.util.TimerTask;
import java.net.MalformedURLException;
import java.util.UUID;
import com.google.common.collect.Maps;
import java.util.Timer;
import java.net.URL;
import java.util.Map;

public class PlayerUsageSnooper
{
    private final Map<String, Object> snooperStats;
    private final Map<String, Object> clientStats;
    private final String uniqueID;
    private final URL serverUrl;
    private final IPlayerUsage playerStatsCollector;
    private final Timer threadTrigger;
    private final Object syncLock;
    private final long minecraftStartTimeMilis;
    private boolean isRunning;
    private int selfCounter;
    
    public PlayerUsageSnooper(final String side, final IPlayerUsage playerStatCollector, final long startTime) {
        this.snooperStats = Maps.newHashMap();
        this.clientStats = Maps.newHashMap();
        this.uniqueID = UUID.randomUUID().toString();
        this.threadTrigger = new Timer("Snooper Timer", true);
        this.syncLock = new Object();
        try {
            this.serverUrl = new URL("http://snoop.minecraft.net/" + side + "?version=" + 2);
        }
        catch (final MalformedURLException var6) {
            throw new IllegalArgumentException();
        }
        this.playerStatsCollector = playerStatCollector;
        this.minecraftStartTimeMilis = startTime;
    }
    
    public void startSnooper() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.addOSData();
            this.threadTrigger.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (PlayerUsageSnooper.this.playerStatsCollector.isSnooperEnabled()) {
                        final Map<String, Object> map;
                        synchronized (PlayerUsageSnooper.this.syncLock) {
                            map = Maps.newHashMap(PlayerUsageSnooper.this.clientStats);
                            if (PlayerUsageSnooper.this.selfCounter == 0) {
                                map.putAll(PlayerUsageSnooper.this.snooperStats);
                            }
                            final Map<String, Object> map2 = map;
                            final String s = "snooper_count";
                            final PlayerUsageSnooper this$0 = PlayerUsageSnooper.this;
                            final int access$3 = this$0.selfCounter;
                            PlayerUsageSnooper.access$5(this$0, access$3 + 1);
                            map2.put(s, access$3);
                            map.put("snooper_token", PlayerUsageSnooper.this.uniqueID);
                            monitorexit(PlayerUsageSnooper.this.syncLock);
                        }
                        HttpUtil.postMap(PlayerUsageSnooper.this.serverUrl, map, true);
                    }
                }
            }, 0L, 900000L);
        }
    }
    
    private void addOSData() {
        this.addJvmArgsToSnooper();
        this.addClientStat("snooper_token", this.uniqueID);
        this.addStatToSnooper("snooper_token", this.uniqueID);
        this.addStatToSnooper("os_name", System.getProperty("os.name"));
        this.addStatToSnooper("os_version", System.getProperty("os.version"));
        this.addStatToSnooper("os_architecture", System.getProperty("os.arch"));
        this.addStatToSnooper("java_version", System.getProperty("java.version"));
        this.addClientStat("version", "1.8.9");
        this.playerStatsCollector.addServerTypeToSnooper(this);
    }
    
    private void addJvmArgsToSnooper() {
        final RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
        final List<String> list = runtimemxbean.getInputArguments();
        int i = 0;
        for (final String s : list) {
            if (s.startsWith("-X")) {
                this.addClientStat("jvm_arg[" + i++ + "]", s);
            }
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
    
    public void addClientStat(final String statName, final Object statValue) {
        synchronized (this.syncLock) {
            this.clientStats.put(statName, statValue);
            monitorexit(this.syncLock);
        }
    }
    
    public void addStatToSnooper(final String statName, final Object statValue) {
        synchronized (this.syncLock) {
            this.snooperStats.put(statName, statValue);
            monitorexit(this.syncLock);
        }
    }
    
    public Map<String, String> getCurrentStats() {
        final Map<String, String> map = Maps.newLinkedHashMap();
        synchronized (this.syncLock) {
            this.addMemoryStatsToSnooper();
            for (final Map.Entry<String, Object> entry : this.snooperStats.entrySet()) {
                map.put(entry.getKey(), entry.getValue().toString());
            }
            for (final Map.Entry<String, Object> entry2 : this.clientStats.entrySet()) {
                map.put(entry2.getKey(), entry2.getValue().toString());
            }
            final Map<String, String> map2 = map;
            monitorexit(this.syncLock);
            return map2;
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
    
    static /* synthetic */ void access$5(final PlayerUsageSnooper playerUsageSnooper, final int selfCounter) {
        playerUsageSnooper.selfCounter = selfCounter;
    }
}
