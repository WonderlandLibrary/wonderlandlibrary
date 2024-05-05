package net.minecraft.profiler;

import com.google.common.collect.Maps;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import net.minecraft.util.HttpUtil;

public class PlayerUsageSnooper
{
  private final Map field_152773_a = Maps.newHashMap();
  private final Map field_152774_b = Maps.newHashMap();
  private final String uniqueID = UUID.randomUUID().toString();
  private final URL serverUrl;
  private final IPlayerUsage playerStatsCollector;
  private final Timer threadTrigger = new Timer("Snooper Timer", true);
  private final Object syncLock = new Object();
  private final long minecraftStartTimeMilis;
  private boolean isRunning;
  private int selfCounter;
  private static final String __OBFID = "CL_00001515";
  
  public PlayerUsageSnooper(String p_i1563_1_, IPlayerUsage p_i1563_2_, long p_i1563_3_)
  {
    try
    {
      this.serverUrl = new URL("http://snoop.minecraft.net/" + p_i1563_1_ + "?version=" + 2);
    }
    catch (MalformedURLException var6)
    {
      throw new IllegalArgumentException();
    }
    this.playerStatsCollector = p_i1563_2_;
    this.minecraftStartTimeMilis = p_i1563_3_;
  }
  
  public void startSnooper()
  {
    if (!this.isRunning)
    {
      this.isRunning = true;
      func_152766_h();
      this.threadTrigger.schedule(new TimerTask()
      {
        private static final String __OBFID = "CL_00001516";
        
        public void run()
        {
          if (PlayerUsageSnooper.this.playerStatsCollector.isSnooperEnabled())
          {
            synchronized (PlayerUsageSnooper.this.syncLock)
            {
              HashMap var1 = Maps.newHashMap(PlayerUsageSnooper.this.field_152774_b);
              if (PlayerUsageSnooper.this.selfCounter == 0) {
                var1.putAll(PlayerUsageSnooper.this.field_152773_a);
              }
              var1.put("snooper_count", Integer.valueOf(PlayerUsageSnooper.access$308(PlayerUsageSnooper.this)));
              var1.put("snooper_token", PlayerUsageSnooper.this.uniqueID);
            }
            HashMap var1;
            HttpUtil.postMap(PlayerUsageSnooper.this.serverUrl, var1, true);
          }
        }
      }, 0L, 900000L);
    }
  }
  
  private void func_152766_h()
  {
    addJvmArgsToSnooper();
    addClientStat("snooper_token", this.uniqueID);
    addStatToSnooper("snooper_token", this.uniqueID);
    addStatToSnooper("os_name", System.getProperty("os.name"));
    addStatToSnooper("os_version", System.getProperty("os.version"));
    addStatToSnooper("os_architecture", System.getProperty("os.arch"));
    addStatToSnooper("java_version", System.getProperty("java.version"));
    addStatToSnooper("version", "1.8");
    this.playerStatsCollector.addServerTypeToSnooper(this);
  }
  
  private void addJvmArgsToSnooper()
  {
    RuntimeMXBean var1 = ManagementFactory.getRuntimeMXBean();
    List var2 = var1.getInputArguments();
    int var3 = 0;
    Iterator var4 = var2.iterator();
    while (var4.hasNext())
    {
      String var5 = (String)var4.next();
      if (var5.startsWith("-X")) {
        addClientStat("jvm_arg[" + var3++ + "]", var5);
      }
    }
    addClientStat("jvm_args", Integer.valueOf(var3));
  }
  
  public void addMemoryStatsToSnooper()
  {
    addStatToSnooper("memory_total", Long.valueOf(Runtime.getRuntime().totalMemory()));
    addStatToSnooper("memory_max", Long.valueOf(Runtime.getRuntime().maxMemory()));
    addStatToSnooper("memory_free", Long.valueOf(Runtime.getRuntime().freeMemory()));
    addStatToSnooper("cpu_cores", Integer.valueOf(Runtime.getRuntime().availableProcessors()));
    this.playerStatsCollector.addServerStatsToSnooper(this);
  }
  
  public void addClientStat(String p_152768_1_, Object p_152768_2_)
  {
    Object var3 = this.syncLock;
    synchronized (this.syncLock)
    {
      this.field_152774_b.put(p_152768_1_, p_152768_2_);
    }
  }
  
  public void addStatToSnooper(String p_152767_1_, Object p_152767_2_)
  {
    Object var3 = this.syncLock;
    synchronized (this.syncLock)
    {
      this.field_152773_a.put(p_152767_1_, p_152767_2_);
    }
  }
  
  public Map getCurrentStats()
  {
    LinkedHashMap var1 = Maps.newLinkedHashMap();
    Object var2 = this.syncLock;
    synchronized (this.syncLock)
    {
      addMemoryStatsToSnooper();
      Iterator var3 = this.field_152773_a.entrySet().iterator();
      while (var3.hasNext())
      {
        Map.Entry var4 = (Map.Entry)var3.next();
        var1.put(var4.getKey(), var4.getValue().toString());
      }
      var3 = this.field_152774_b.entrySet().iterator();
      while (var3.hasNext())
      {
        Map.Entry var4 = (Map.Entry)var3.next();
        var1.put(var4.getKey(), var4.getValue().toString());
      }
      return var1;
    }
  }
  
  public boolean isSnooperRunning()
  {
    return this.isRunning;
  }
  
  public void stopSnooper()
  {
    this.threadTrigger.cancel();
  }
  
  public String getUniqueID()
  {
    return this.uniqueID;
  }
  
  public long getMinecraftStartTimeMillis()
  {
    return this.minecraftStartTimeMilis;
  }
}
