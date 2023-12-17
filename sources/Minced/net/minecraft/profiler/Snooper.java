// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.profiler;

import java.util.Iterator;
import java.util.List;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ManagementFactory;
import net.minecraft.util.HttpUtil;
import net.minecraft.server.MinecraftServer;
import java.util.TimerTask;
import java.net.MalformedURLException;
import java.util.UUID;
import com.google.common.collect.Maps;
import java.util.Timer;
import java.net.URL;
import java.util.Map;

public class Snooper
{
    private final Map<String, Object> snooperStats;
    private final Map<String, Object> clientStats;
    private final String uniqueID;
    private final URL serverUrl;
    private final ISnooperInfo playerStatsCollector;
    private final Timer threadTrigger;
    private final Object syncLock;
    private final long minecraftStartTimeMilis;
    private boolean isRunning;
    private int selfCounter;
    
    public Snooper(final String side, final ISnooperInfo playerStatCollector, final long startTime) {
        this.snooperStats = (Map<String, Object>)Maps.newHashMap();
        this.clientStats = (Map<String, Object>)Maps.newHashMap();
        this.uniqueID = UUID.randomUUID().toString();
        this.threadTrigger = new Timer("Snooper Timer", true);
        this.syncLock = new Object();
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
            this.threadTrigger.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (Snooper.this.playerStatsCollector.isSnooperEnabled()) {
                        final Map<String, Object> map;
                        synchronized (Snooper.this.syncLock) {
                            map = (Map<String, Object>)Maps.newHashMap(Snooper.this.clientStats);
                            if (Snooper.this.selfCounter == 0) {
                                map.putAll(Snooper.this.snooperStats);
                            }
                            map.put("snooper_count", Snooper.this.selfCounter++);
                            map.put("snooper_token", Snooper.this.uniqueID);
                        }
                        final MinecraftServer minecraftserver = (Snooper.this.playerStatsCollector instanceof MinecraftServer) ? ((MinecraftServer)Snooper.this.playerStatsCollector) : null;
                        HttpUtil.postMap(Snooper.this.serverUrl, map, true, (minecraftserver == null) ? null : minecraftserver.getServerProxy());
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
        this.addClientStat("version", "1.12.2");
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
        }
    }
    
    public void addStatToSnooper(final String statName, final Object statValue) {
        synchronized (this.syncLock) {
            this.snooperStats.put(statName, statValue);
        }
    }
    
    public Map<String, String> getCurrentStats() {
        final Map<String, String> map = (Map<String, String>)Maps.newLinkedHashMap();
        synchronized (this.syncLock) {
            this.addMemoryStatsToSnooper();
            for (final Map.Entry<String, Object> entry : this.snooperStats.entrySet()) {
                map.put(entry.getKey(), entry.getValue().toString());
            }
            for (final Map.Entry<String, Object> entry2 : this.clientStats.entrySet()) {
                map.put(entry2.getKey(), entry2.getValue().toString());
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
}
