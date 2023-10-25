package net.minecraft.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.apache.logging.log4j.Logger;

public class Util {
  public static EnumOS getOSType() {
    String s = System.getProperty("os.name").toLowerCase();
    return s.contains("win") ? EnumOS.WINDOWS : (s.contains("mac") ? EnumOS.OSX : (s.contains("solaris") ? EnumOS.SOLARIS : (s.contains("sunos") ? EnumOS.SOLARIS : (s.contains("linux") ? EnumOS.LINUX : (s.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
  }
  
  public static <V> V func_181617_a(FutureTask<V> p_181617_0_, Logger p_181617_1_) {
    try {
      p_181617_0_.run();
      return p_181617_0_.get();
    } catch (ExecutionException executionexception) {
      p_181617_1_.fatal("Error executing task", executionexception);
    } catch (InterruptedException interruptedexception) {
      p_181617_1_.fatal("Error executing task", interruptedexception);
    } 
    return null;
  }
  
  public enum EnumOS {
    LINUX, SOLARIS, WINDOWS, OSX, UNKNOWN;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraf\\util\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */