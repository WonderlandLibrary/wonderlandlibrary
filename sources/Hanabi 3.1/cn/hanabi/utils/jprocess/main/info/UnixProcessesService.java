package cn.hanabi.utils.jprocess.main.info;

import cn.hanabi.utils.jprocess.main.model.JProcessesResponse;
import cn.hanabi.utils.jprocess.main.model.ProcessInfo;
import cn.hanabi.utils.jprocess.main.util.OSDetector;
import cn.hanabi.utils.jprocess.main.util.ProcessesUtils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class UnixProcessesService extends AbstractProcessesService {
   private static final String PS_COLUMNS = "pid,ruser,vsize,rss,%cpu,lstart,cputime,nice,ucomm";
   private static final String PS_FULL_COMMAND = "pid,command";
   private static final int PS_COLUMNS_SIZE = "pid,ruser,vsize,rss,%cpu,lstart,cputime,nice,ucomm".split(",").length;
   private static final int PS_FULL_COMMAND_SIZE = "pid,command".split(",").length;
   private String nameFilter = null;

   protected List parseList(String rawData) {
      List processesDataList = new ArrayList();
      String[] dataStringLines = rawData.split("\\r?\\n");
      String[] var5 = dataStringLines;
      int var6 = dataStringLines.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         String dataLine = var5[var7];
         String line = dataLine.trim();
         if (!line.startsWith("PID")) {
            Map element = new LinkedHashMap();
            String[] elements = line.split("\\s+", PS_COLUMNS_SIZE + 5);
            int index = 0;
            element.put("pid", elements[index++]);
            element.put("user", elements[index++]);
            element.put("virtual_memory", elements[index++]);
            element.put("physical_memory", elements[index++]);
            element.put("cpu_usage", elements[index++]);
            ++index;
            String longDate = elements[index++] + " " + elements[index++] + " " + elements[index++] + " " + elements[index++];
            element.put("start_time", elements[index - 2]);

            try {
               element.put("start_datetime", ProcessesUtils.parseUnixLongTimeToFullDate(longDate));
            } catch (ParseException var14) {
               element.put("start_datetime", "01/01/2000 00:00:00");
               System.err.println("Failed formatting date from ps: " + longDate + ", using \"01/01/2000 00:00:00\"");
            }

            element.put("proc_time", elements[index++]);
            element.put("priority", elements[index++]);
            element.put("proc_name", elements[index++]);
            element.put("command", elements[index - 1]);
            processesDataList.add(element);
         }
      }

      loadFullCommandData(processesDataList);
      if (this.nameFilter != null) {
         this.filterByName(processesDataList);
      }

      return processesDataList;
   }

   protected String getProcessesData(String name) {
      if (name != null) {
         if (OSDetector.isLinux()) {
            return ProcessesUtils.executeCommand("ps", "-o", "pid,ruser,vsize,rss,%cpu,lstart,cputime,nice,ucomm", "-C", name);
         }

         this.nameFilter = name;
      }

      return ProcessesUtils.executeCommand("ps", "-e", "-o", "pid,ruser,vsize,rss,%cpu,lstart,cputime,nice,ucomm");
   }

   protected JProcessesResponse kill(int pid) {
      JProcessesResponse response = new JProcessesResponse();
      if (ProcessesUtils.executeCommandAndGetCode("kill", "-9", String.valueOf(pid)) == 0) {
         response.setSuccess(true);
      }

      return response;
   }

   protected JProcessesResponse killGracefully(int pid) {
      JProcessesResponse response = new JProcessesResponse();
      if (ProcessesUtils.executeCommandAndGetCode("kill", "-15", String.valueOf(pid)) == 0) {
         response.setSuccess(true);
      }

      return response;
   }

   public JProcessesResponse changePriority(int pid, int priority) {
      JProcessesResponse response = new JProcessesResponse();
      if (ProcessesUtils.executeCommandAndGetCode("renice", String.valueOf(priority), "-p", String.valueOf(pid)) == 0) {
         response.setSuccess(true);
      }

      return response;
   }

   public ProcessInfo getProcess(int pid) {
      return this.getProcess(pid, false);
   }

   public ProcessInfo getProcess(int pid, boolean fastMode) {
      this.fastMode = fastMode;
      List processList = this.parseList(ProcessesUtils.executeCommand("ps", "-o", "pid,ruser,vsize,rss,%cpu,lstart,cputime,nice,ucomm", "-p", String.valueOf(pid)));
      if (processList != null && !processList.isEmpty()) {
         Map processData = (Map)processList.get(0);
         ProcessInfo info = new ProcessInfo();
         info.setPid((String)processData.get("pid"));
         info.setName((String)processData.get("proc_name"));
         info.setTime((String)processData.get("proc_time"));
         info.setCommand((String)processData.get("command"));
         info.setCpuUsage((String)processData.get("cpu_usage"));
         info.setPhysicalMemory((String)processData.get("physical_memory"));
         info.setStartTime((String)processData.get("start_time"));
         info.setUser((String)processData.get("user"));
         info.setVirtualMemory((String)processData.get("virtual_memory"));
         info.setPriority((String)processData.get("priority"));
         return info;
      } else {
         return null;
      }
   }

   private static void loadFullCommandData(List processesDataList) {
      Map commandsMap = new HashMap();
      String data = ProcessesUtils.executeCommand("ps", "-e", "-o", "pid,command");
      String[] dataStringLines = data.split("\\r?\\n");
      String[] var4 = dataStringLines;
      int var5 = dataStringLines.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String dataLine = var4[var6];
         if (!dataLine.trim().startsWith("PID")) {
            String[] elements = dataLine.trim().split("\\s+", PS_FULL_COMMAND_SIZE);
            if (elements.length == PS_FULL_COMMAND_SIZE) {
               commandsMap.put(elements[0], elements[1]);
            }
         }
      }

      Iterator var9 = processesDataList.iterator();

      while(var9.hasNext()) {
         Map process = (Map)var9.next();
         if (commandsMap.containsKey(process.get("pid"))) {
            process.put("command", commandsMap.get(process.get("pid")));
         }
      }

   }

   private void filterByName(List processesDataList) {
      List processesToRemove = new ArrayList();
      Iterator var3 = processesDataList.iterator();

      while(var3.hasNext()) {
         Map process = (Map)var3.next();
         if (!this.nameFilter.equals(process.get("proc_name"))) {
            processesToRemove.add(process);
         }
      }

      processesDataList.removeAll(processesToRemove);
   }
}
