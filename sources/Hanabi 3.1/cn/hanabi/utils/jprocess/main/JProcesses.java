package cn.hanabi.utils.jprocess.main;

import cn.hanabi.utils.jprocess.main.info.ProcessesFactory;
import cn.hanabi.utils.jprocess.main.info.ProcessesService;
import cn.hanabi.utils.jprocess.main.model.JProcessesResponse;
import cn.hanabi.utils.jprocess.main.model.ProcessInfo;
import java.util.List;

public class JProcesses {
   private boolean fastMode = false;

   private JProcesses() {
   }

   public static JProcesses get() {
      return new JProcesses();
   }

   public JProcesses fastMode() {
      this.fastMode = true;
      return this;
   }

   public JProcesses fastMode(boolean enabled) {
      this.fastMode = enabled;
      return this;
   }

   public List listProcesses() {
      return getService().getList(this.fastMode);
   }

   public List listProcesses(String name) {
      return getService().getList(name, this.fastMode);
   }

   public static List getProcessList() {
      return getService().getList();
   }

   public static List getProcessList(String name) {
      return getService().getList(name);
   }

   public static ProcessInfo getProcess(int pid) {
      return getService().getProcess(pid);
   }

   public static JProcessesResponse killProcess(int pid) {
      return getService().killProcess(pid);
   }

   public static JProcessesResponse killProcessGracefully(int pid) {
      return getService().killProcessGracefully(pid);
   }

   public static JProcessesResponse changePriority(int pid, int newPriority) {
      return getService().changePriority(pid, newPriority);
   }

   private static ProcessesService getService() {
      return ProcessesFactory.getService();
   }
}
