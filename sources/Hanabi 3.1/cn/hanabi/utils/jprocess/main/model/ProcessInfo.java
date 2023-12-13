package cn.hanabi.utils.jprocess.main.model;

import java.util.HashMap;
import java.util.Map;

public class ProcessInfo {
   private String pid;
   private String time;
   private String name;
   private String user;
   private String virtualMemory;
   private String physicalMemory;
   private String cpuUsage;
   private String startTime;
   private String priority;
   private String command;
   private Map extraData = new HashMap();

   public ProcessInfo() {
   }

   public ProcessInfo(String pid, String time, String name, String user, String virtualMemory, String physicalMemory, String cpuUsage, String startTime, String priority, String command) {
      this.pid = pid;
      this.time = time;
      this.name = name;
      this.user = user;
      this.virtualMemory = virtualMemory;
      this.physicalMemory = physicalMemory;
      this.cpuUsage = cpuUsage;
      this.startTime = startTime;
      this.priority = priority;
      this.command = command;
   }

   public String getPid() {
      return this.pid;
   }

   public void setPid(String pid) {
      this.pid = pid;
   }

   public String getTime() {
      return this.time;
   }

   public void setTime(String time) {
      this.time = time;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getUser() {
      return this.user;
   }

   public void setUser(String user) {
      this.user = user;
   }

   public String getVirtualMemory() {
      return this.virtualMemory;
   }

   public void setVirtualMemory(String virtualMemory) {
      this.virtualMemory = virtualMemory;
   }

   public String getPhysicalMemory() {
      return this.physicalMemory;
   }

   public void setPhysicalMemory(String physicalMemory) {
      this.physicalMemory = physicalMemory;
   }

   public String getCpuUsage() {
      return this.cpuUsage;
   }

   public void setCpuUsage(String cpuUsage) {
      this.cpuUsage = cpuUsage;
   }

   public String getStartTime() {
      return this.startTime;
   }

   public void setStartTime(String startTime) {
      this.startTime = startTime;
   }

   public String getCommand() {
      return this.command;
   }

   public void setCommand(String command) {
      this.command = command;
   }

   public String getPriority() {
      return this.priority;
   }

   public void setPriority(String priority) {
      this.priority = priority;
   }

   public Map getExtraData() {
      return this.extraData;
   }

   public void setExtraData(Map extraData) {
      this.extraData = extraData;
   }

   public void addExtraData(String key, String value) {
      this.extraData.put(key, value);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         ProcessInfo that;
         label77: {
            that = (ProcessInfo)o;
            if (this.pid != null) {
               if (this.pid.equals(that.pid)) {
                  break label77;
               }
            } else if (that.pid == null) {
               break label77;
            }

            return false;
         }

         label70: {
            if (this.time != null) {
               if (this.time.equals(that.time)) {
                  break label70;
               }
            } else if (that.time == null) {
               break label70;
            }

            return false;
         }

         if (this.name != null) {
            if (!this.name.equals(that.name)) {
               return false;
            }
         } else if (that.name != null) {
            return false;
         }

         label56: {
            if (this.startTime != null) {
               if (this.startTime.equals(that.startTime)) {
                  break label56;
               }
            } else if (that.startTime == null) {
               break label56;
            }

            return false;
         }

         if (this.priority != null) {
            if (this.priority.equals(that.priority)) {
               return this.command != null ? this.command.equals(that.command) : that.command == null;
            }
         } else if (that.priority == null) {
            return this.command != null ? this.command.equals(that.command) : that.command == null;
         }

         return false;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = this.pid != null ? this.pid.hashCode() : 0;
      result = 31 * result + (this.time != null ? this.time.hashCode() : 0);
      result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
      result = 31 * result + (this.startTime != null ? this.startTime.hashCode() : 0);
      result = 31 * result + (this.priority != null ? this.priority.hashCode() : 0);
      result = 31 * result + (this.command != null ? this.command.hashCode() : 0);
      return result;
   }

   public String toString() {
      return "PID:" + this.pid + "\tCPU:" + this.cpuUsage + "\tMEM:" + this.physicalMemory + "\tPRIORITY:" + this.priority + "\tCMD:" + this.command;
   }
}
