package net.minecraft.profiler;

import com.google.common.collect.Maps;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.util.HttpUtil;

public class PlayerUsageSnooper {
    private final Map<String, Object> snooperStats = Maps.newHashMap();
    private final Map<String, Object> clientStats = Maps.newHashMap();
    private final String uniqueID = UUID.randomUUID().toString();

    /**
     * URL of the server to send the report to
     */
    private final URL serverUrl;
    private final IPlayerUsage playerStatsCollector;

    /**
     * set to fire the snooperThread every 15 mins
     */
    private final Timer threadTrigger = new Timer("Snooper Timer", true);
    private final Object syncLock = new Object();
    private final long minecraftStartTimeMilis;
    private boolean isRunning;

    /**
     * incremented on every getSelfCounterFor
     */
    private int selfCounter;

    public PlayerUsageSnooper(String p_i1563_1_, IPlayerUsage playerStatCollector, long startTime) {
        try {
            serverUrl = new URL("http://snoop.minecraft.net/" + p_i1563_1_ + "?version=" + 2);
        } catch (MalformedURLException var6) {
            throw new IllegalArgumentException();
        }

        playerStatsCollector = playerStatCollector;
        minecraftStartTimeMilis = startTime;
    }

    /**
     * Note issuing start multiple times is not an error.
     */
    public void startSnooper() {
        if (isRunning)
            return;

        isRunning = true;
        func_152766_h();
        threadTrigger.schedule(new TimerTask() {
            public void run() {
                if (playerStatsCollector.isSnooperEnabled()) {
                    Map<String, Object> map;

                    synchronized (syncLock) {
                        map = Maps.newHashMap(clientStats);
                        if (selfCounter == 0) map.putAll(snooperStats);
                        map.put("snooper_count", selfCounter++);
                        map.put("snooper_token", uniqueID);
                    }

                    HttpUtil.postMap(serverUrl, map, true);
                }
            }
        }, 0L, 900000L);
    }

    private void func_152766_h() {
        addJvmArgsToSnooper();
        addClientStat("snooper_token", uniqueID);
        addStatToSnooper("snooper_token", uniqueID);
        addStatToSnooper("os_name", System.getProperty("os.name"));
        addStatToSnooper("os_version", System.getProperty("os.version"));
        addStatToSnooper("os_architecture", System.getProperty("os.arch"));
        addStatToSnooper("java_version", System.getProperty("java.version"));
        addClientStat("version", "1.8.8");
        playerStatsCollector.addServerTypeToSnooper(this);
    }

    private void addJvmArgsToSnooper() {
        RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
        List<String> list = runtimemxbean.getInputArguments();
        int i = 0;

        for (String s : list) {
            if (s.startsWith("-X"))
                addClientStat("jvm_arg[" + i++ + "]", s);
        }

        addClientStat("jvm_args", i);
    }

    public void addMemoryStatsToSnooper() {
        addStatToSnooper("memory_total", Runtime.getRuntime().totalMemory());
        addStatToSnooper("memory_max", Runtime.getRuntime().maxMemory());
        addStatToSnooper("memory_free", Runtime.getRuntime().freeMemory());
        addStatToSnooper("cpu_cores", Runtime.getRuntime().availableProcessors());
        playerStatsCollector.addServerStatsToSnooper(this);
    }

    public void addClientStat(String p_152768_1_, Object p_152768_2_) {
        synchronized (syncLock) {
            clientStats.put(p_152768_1_, p_152768_2_);
        }
    }

    public void addStatToSnooper(String p_152767_1_, Object p_152767_2_) {
        synchronized (syncLock) {
            snooperStats.put(p_152767_1_, p_152767_2_);
        }
    }

    public Map<String, String> getCurrentStats() {
        Map<String, String> map = Maps.newLinkedHashMap();

        synchronized (syncLock) {
            addMemoryStatsToSnooper();

            for (Entry<String, Object> entry : snooperStats.entrySet())
                map.put(entry.getKey(), entry.getValue().toString());

            for (Entry<String, Object> entry1 : clientStats.entrySet())
                map.put(entry1.getKey(), entry1.getValue().toString());

            return map;
        }
    }

    public boolean isSnooperRunning() {
        return isRunning;
    }

    public void stopSnooper() {
        threadTrigger.cancel();
    }

    public String getUniqueID() {
        return uniqueID;
    }

    /**
     * Returns the saved value of System#currentTimeMillis when the game started
     */
    public long getMinecraftStartTimeMillis() {
        return minecraftStartTimeMilis;
    }
}