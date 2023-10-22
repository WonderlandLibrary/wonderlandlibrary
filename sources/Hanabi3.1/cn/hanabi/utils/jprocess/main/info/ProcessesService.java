package cn.hanabi.utils.jprocess.main.info;

import cn.hanabi.utils.jprocess.main.model.JProcessesResponse;
import cn.hanabi.utils.jprocess.main.model.ProcessInfo;
import java.util.List;

public interface ProcessesService {
   List getList();

   List getList(boolean var1);

   List getList(String var1);

   List getList(String var1, boolean var2);

   ProcessInfo getProcess(int var1);

   ProcessInfo getProcess(int var1, boolean var2);

   JProcessesResponse killProcess(int var1);

   JProcessesResponse killProcessGracefully(int var1);

   JProcessesResponse changePriority(int var1, int var2);
}
