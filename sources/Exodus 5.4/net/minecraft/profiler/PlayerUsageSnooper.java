/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.profiler;

import com.google.common.collect.Maps;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.util.HttpUtil;

public class PlayerUsageSnooper {
    private final Timer threadTrigger;
    private final Object syncLock;
    private final Map<String, Object> field_152773_a = Maps.newHashMap();
    private int selfCounter;
    private final String uniqueID;
    private boolean isRunning;
    private final long minecraftStartTimeMilis;
    private final URL serverUrl;
    private final IPlayerUsage playerStatsCollector;
    private final Map<String, Object> field_152774_b = Maps.newHashMap();

    public long getMinecraftStartTimeMillis() {
        return this.minecraftStartTimeMilis;
    }

    public void startSnooper() {
        if (!this.isRunning) {
            this.isRunning = true;
            this.func_152766_h();
            this.threadTrigger.schedule(new TimerTask(){

                @Override
                public void run() {
                    if (PlayerUsageSnooper.this.playerStatsCollector.isSnooperEnabled()) {
                        HashMap hashMap;
                        Object object = PlayerUsageSnooper.this.syncLock;
                        synchronized (object) {
                            hashMap = Maps.newHashMap((Map)PlayerUsageSnooper.this.field_152774_b);
                            if (PlayerUsageSnooper.this.selfCounter == 0) {
                                hashMap.putAll(PlayerUsageSnooper.this.field_152773_a);
                            }
                            PlayerUsageSnooper playerUsageSnooper = PlayerUsageSnooper.this;
                            int n = playerUsageSnooper.selfCounter;
                            playerUsageSnooper.selfCounter = n + 1;
                            hashMap.put("snooper_count", n);
                            hashMap.put("snooper_token", PlayerUsageSnooper.this.uniqueID);
                        }
                        HttpUtil.postMap(PlayerUsageSnooper.this.serverUrl, hashMap, true);
                    }
                }
            }, 0L, 900000L);
        }
    }

    public PlayerUsageSnooper(String string, IPlayerUsage iPlayerUsage, long l) {
        this.uniqueID = UUID.randomUUID().toString();
        this.threadTrigger = new Timer("Snooper Timer", true);
        this.syncLock = new Object();
        try {
            this.serverUrl = new URL("http://snoop.minecraft.net/" + string + "?version=" + 2);
        }
        catch (MalformedURLException malformedURLException) {
            throw new IllegalArgumentException();
        }
        this.playerStatsCollector = iPlayerUsage;
        this.minecraftStartTimeMilis = l;
    }

    private void func_152766_h() {
        this.addJvmArgsToSnooper();
        this.addClientStat("snooper_token", this.uniqueID);
        this.addStatToSnooper("snooper_token", this.uniqueID);
        this.addStatToSnooper("os_name", System.getProperty("os.name"));
        this.addStatToSnooper("os_version", System.getProperty("os.version"));
        this.addStatToSnooper("os_architecture", System.getProperty("os.arch"));
        this.addStatToSnooper("java_version", System.getProperty("java.version"));
        this.addClientStat("version", "1.8.8");
        this.playerStatsCollector.addServerTypeToSnooper(this);
    }

    public void addStatToSnooper(String string, Object object) {
        Object object2 = this.syncLock;
        synchronized (object2) {
            this.field_152773_a.put(string, object);
        }
    }

    public void stopSnooper() {
        this.threadTrigger.cancel();
    }

    public Map<String, String> getCurrentStats() {
        LinkedHashMap linkedHashMap = Maps.newLinkedHashMap();
        Object object = this.syncLock;
        synchronized (object) {
            this.addMemoryStatsToSnooper();
            for (Map.Entry<String, Object> entry : this.field_152773_a.entrySet()) {
                linkedHashMap.put(entry.getKey(), entry.getValue().toString());
            }
            for (Map.Entry<String, Object> entry : this.field_152774_b.entrySet()) {
                linkedHashMap.put(entry.getKey(), entry.getValue().toString());
            }
            return linkedHashMap;
        }
    }

    public boolean isSnooperRunning() {
        return this.isRunning;
    }

    public String getUniqueID() {
        return this.uniqueID;
    }

    public void addMemoryStatsToSnooper() {
        this.addStatToSnooper("memory_total", Runtime.getRuntime().totalMemory());
        this.addStatToSnooper("memory_max", Runtime.getRuntime().maxMemory());
        this.addStatToSnooper("memory_free", Runtime.getRuntime().freeMemory());
        this.addStatToSnooper("cpu_cores", Runtime.getRuntime().availableProcessors());
        this.playerStatsCollector.addServerStatsToSnooper(this);
    }

    private void addJvmArgsToSnooper() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> list = runtimeMXBean.getInputArguments();
        int n = 0;
        for (String string : list) {
            if (!string.startsWith("-X")) continue;
            this.addClientStat("jvm_arg[" + n++ + "]", string);
        }
        this.addClientStat("jvm_args", n);
    }

    public void addClientStat(String string, Object object) {
        Object object2 = this.syncLock;
        synchronized (object2) {
            this.field_152774_b.put(string, object);
        }
    }
}

