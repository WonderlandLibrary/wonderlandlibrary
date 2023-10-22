package cn.hanabi.utils.jprocess.main.info;

import cn.hanabi.utils.jprocess.main.model.JProcessesResponse;
import cn.hanabi.utils.jprocess.main.model.ProcessInfo;
import cn.hanabi.utils.jprocess.main.util.ProcessesUtils;
import cn.hanabi.utils.jprocess.wmi4java.WMI4Java;
import cn.hanabi.utils.jprocess.wmi4java.WMIClass;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class WindowsProcessesService extends AbstractProcessesService {
   private final Map userData;
   private final Map cpuData;
   private static final String LINE_BREAK_REGEX = "\\r?\\n";
   private static final Map keyMap;
   private Map processMap;
   private static final String NAME_PROPNAME = "Name";
   private static final String PROCESSID_PROPNAME = "ProcessId";
   private static final String USERMODETIME_PROPNAME = "UserModeTime";
   private static final String PRIORITY_PROPNAME = "Priority";
   private static final String VIRTUALSIZE_PROPNAME = "VirtualSize";
   private static final String WORKINGSETSIZE_PROPNAME = "WorkingSetSize";
   private static final String COMMANDLINE_PROPNAME = "CommandLine";
   private static final String CREATIONDATE_PROPNAME = "CreationDate";
   private static final String CAPTION_PROPNAME = "Caption";
   private final WMI4Java wmi4Java;

   public WindowsProcessesService() {
      this((WMI4Java)null);
   }

   WindowsProcessesService(WMI4Java wmi4Java) {
      this.userData = new HashMap();
      this.cpuData = new HashMap();
      this.wmi4Java = wmi4Java;
   }

   public WMI4Java getWmi4Java() {
      return this.wmi4Java == null ? WMI4Java.get() : this.wmi4Java;
   }

   protected List parseList(String rawData) {
      List processesDataList = new ArrayList();
      String[] dataStringLines = rawData.split("\\r?\\n");
      String[] var4 = dataStringLines;
      int var5 = dataStringLines.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String dataLine = var4[var6];
         if (dataLine.trim().length() > 0) {
            this.processLine(dataLine, processesDataList);
         }
      }

      return processesDataList;
   }

   private void processLine(String dataLine, List processesDataList) {
      if (dataLine.startsWith("Caption")) {
         this.processMap = new HashMap();
         processesDataList.add(this.processMap);
      }

      if (this.processMap != null) {
         String[] dataStringInfo = dataLine.split(":", 2);
         this.processMap.put(normalizeKey(dataStringInfo[0].trim()), normalizeValue(dataStringInfo[0].trim(), dataStringInfo[1].trim()));
         if ("ProcessId".equals(dataStringInfo[0].trim())) {
            this.processMap.put("user", this.userData.get(dataStringInfo[1].trim()));
            this.processMap.put("cpu_usage", this.cpuData.get(dataStringInfo[1].trim()));
         }

         if ("CreationDate".equals(dataStringInfo[0].trim())) {
            this.processMap.put("start_datetime", ProcessesUtils.parseWindowsDateTimeToFullDate(dataStringInfo[1].trim()));
         }
      }

   }

   protected String getProcessesData(String name) {
      if (!this.fastMode) {
         this.fillExtraProcessData();
      }

      return name != null ? this.getWmi4Java().VBSEngine().properties(Arrays.asList("Caption", "ProcessId", "Name", "UserModeTime", "CommandLine", "WorkingSetSize", "CreationDate", "VirtualSize", "Priority")).filters(Collections.singletonList("Name like '%" + name + "%'")).getRawWMIObjectOutput(WMIClass.WIN32_PROCESS) : this.getWmi4Java().VBSEngine().getRawWMIObjectOutput(WMIClass.WIN32_PROCESS);
   }

   protected JProcessesResponse kill(int pid) {
      JProcessesResponse response = new JProcessesResponse();
      if (ProcessesUtils.executeCommandAndGetCode("taskkill", "/PID", String.valueOf(pid), "/F") == 0) {
         response.setSuccess(true);
      }

      return response;
   }

   protected JProcessesResponse killGracefully(int pid) {
      JProcessesResponse response = new JProcessesResponse();
      if (ProcessesUtils.executeCommandAndGetCode("taskkill", "/PID", String.valueOf(pid)) == 0) {
         response.setSuccess(true);
      }

      return response;
   }

   private static String normalizeKey(String origKey) {
      return (String)keyMap.get(origKey);
   }

   private static String normalizeValue(String origKey, String origValue) {
      if ("UserModeTime".equals(origKey)) {
         long seconds = Long.parseLong(origValue) * 100L / 1000000L / 1000L;
         return nomalizeTime(seconds);
      } else if (("VirtualSize".equals(origKey) || "WorkingSetSize".equals(origKey)) && !origValue.isEmpty()) {
         return String.valueOf(Long.parseLong(origValue) / 1024L);
      } else {
         return "CreationDate".equals(origKey) ? ProcessesUtils.parseWindowsDateTimeToSimpleTime(origValue) : origValue;
      }
   }

   private static String nomalizeTime(long seconds) {
      long hours = seconds / 3600L;
      long minutes = seconds % 3600L / 60L;
      return String.format("%02d:%02d:%02d", hours, minutes, seconds);
   }

   private void fillExtraProcessData() {
      String perfData = this.getWmi4Java().VBSEngine().getRawWMIObjectOutput(WMIClass.WIN32_PERFFORMATTEDDATA_PERFPROC_PROCESS);
      String[] dataStringLines = perfData.split("\\r?\\n");
      String pid = null;
      String cpuUsage = null;
      String[] var5 = dataStringLines;
      int var6 = dataStringLines.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         String dataLine = var5[var7];
         if (dataLine.trim().length() > 0) {
            if (dataLine.startsWith("Caption")) {
               if (pid != null && cpuUsage != null) {
                  this.cpuData.put(pid, cpuUsage);
                  pid = null;
                  cpuUsage = null;
               }
            } else {
               if (pid == null) {
                  pid = checkAndGetDataInLine("IDProcess", dataLine);
               }

               if (cpuUsage == null) {
                  cpuUsage = checkAndGetDataInLine("PercentProcessorTime", dataLine);
               }
            }
         }
      }

      String processesData = VBScriptHelper.getProcessesOwner();
      if (processesData != null) {
         dataStringLines = processesData.split("\\r?\\n");
         String[] var12 = dataStringLines;
         var7 = dataStringLines.length;

         for(int var13 = 0; var13 < var7; ++var13) {
            String dataLine = var12[var13];
            String[] dataStringInfo = dataLine.split(":", 2);
            if (dataStringInfo.length == 2) {
               this.userData.put(dataStringInfo[0].trim(), dataStringInfo[1].trim());
            }
         }
      }

   }

   private static String checkAndGetDataInLine(String dataName, String dataLine) {
      if (dataLine.startsWith(dataName)) {
         String[] dataStringInfo = dataLine.split(":");
         if (dataStringInfo.length == 2) {
            return dataStringInfo[1].trim();
         }
      }

      return null;
   }

   public JProcessesResponse changePriority(int pid, int priority) {
      JProcessesResponse response = new JProcessesResponse();
      String message = VBScriptHelper.changePriority(pid, priority);
      if (message != null && message.length() != 0) {
         response.setMessage(message);
      } else {
         response.setSuccess(true);
      }

      return response;
   }

   public ProcessInfo getProcess(int pid) {
      return this.getProcess(pid, false);
   }

   public ProcessInfo getProcess(int pid, boolean fastMode) {
      this.fastMode = fastMode;
      List allProcesses = this.parseList(this.getProcessesData((String)null));
      Iterator var4 = allProcesses.iterator();

      Map process;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         process = (Map)var4.next();
      } while(!String.valueOf(pid).equals(process.get("pid")));

      ProcessInfo info = new ProcessInfo();
      info.setPid((String)process.get("pid"));
      info.setName((String)process.get("proc_name"));
      info.setTime((String)process.get("proc_time"));
      info.setCommand((String)process.get("command"));
      info.setCpuUsage((String)process.get("cpu_usage"));
      info.setPhysicalMemory((String)process.get("physical_memory"));
      info.setStartTime((String)process.get("start_time"));
      info.setUser((String)process.get("user"));
      info.setVirtualMemory((String)process.get("virtual_memory"));
      info.setPriority((String)process.get("priority"));
      return info;
   }

   static {
      Map tmpMap = new HashMap();
      tmpMap.put("Name", "proc_name");
      tmpMap.put("ProcessId", "pid");
      tmpMap.put("UserModeTime", "proc_time");
      tmpMap.put("Priority", "priority");
      tmpMap.put("VirtualSize", "virtual_memory");
      tmpMap.put("WorkingSetSize", "physical_memory");
      tmpMap.put("CommandLine", "command");
      tmpMap.put("CreationDate", "start_time");
      keyMap = Collections.unmodifiableMap(tmpMap);
   }
}
