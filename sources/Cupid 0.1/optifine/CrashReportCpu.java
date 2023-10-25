package optifine;

import java.util.concurrent.Callable;
import net.minecraft.client.renderer.OpenGlHelper;

public class CrashReportCpu implements Callable {
  public Object call() throws Exception {
    return OpenGlHelper.func_183029_j();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\optifine\CrashReportCpu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */