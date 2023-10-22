package cn.hanabi.utils.jprocess.main.info;

import cn.hanabi.utils.jprocess.main.model.JProcessesResponse;
import cn.hanabi.utils.jprocess.main.model.ProcessInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

abstract class AbstractProcessesService implements ProcessesService {
   protected boolean fastMode = false;

   public List getList() {
      return this.getList((String)null);
   }

   public List getList(boolean fastMode) {
      return this.getList((String)null, fastMode);
   }

   public List getList(String name) {
      return this.getList(name, false);
   }

   public List getList(String name, boolean fastMode) {
      this.fastMode = fastMode;
      String rawData = this.getProcessesData(name);
      List mapList = this.parseList(rawData);
      return this.buildInfoFromMap(mapList);
   }

   public JProcessesResponse killProcess(int pid) {
      return this.kill(pid);
   }

   public JProcessesResponse killProcessGracefully(int pid) {
      return this.killGracefully(pid);
   }

   protected abstract List parseList(String var1);

   protected abstract String getProcessesData(String var1);

   protected abstract JProcessesResponse kill(int var1);

   protected abstract JProcessesResponse killGracefully(int var1);

   private List buildInfoFromMap(List mapList) {
      List infoList = new ArrayList();
      Iterator var3 = mapList.iterator();

      while(var3.hasNext()) {
         Map map = (Map)var3.next();
         ProcessInfo info = new ProcessInfo();
         info.setPid((String)map.get("pid"));
         info.setName((String)map.get("proc_name"));
         info.setTime((String)map.get("proc_time"));
         info.setCommand(map.get("command") != null ? (String)map.get("command") : "");
         info.setCpuUsage((String)map.get("cpu_usage"));
         info.setPhysicalMemory((String)map.get("physical_memory"));
         info.setStartTime((String)map.get("start_time"));
         info.setUser((String)map.get("user"));
         info.setVirtualMemory((String)map.get("virtual_memory"));
         info.setPriority((String)map.get("priority"));
         info.setExtraData(map);
         infoList.add(info);
      }

      return infoList;
   }
}
