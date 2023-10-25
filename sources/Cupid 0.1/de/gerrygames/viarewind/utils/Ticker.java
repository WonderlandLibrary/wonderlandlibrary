package de.gerrygames.viarewind.utils;

import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.data.UserConnection;

public class Ticker {
  private static boolean init = false;
  
  public static void init() {
    if (init)
      return; 
    synchronized (Ticker.class) {
      if (init)
        return; 
      init = true;
    } 
    Via.getPlatform().runRepeatingSync(() -> Via.getManager().getPortedPlayers().values().forEach(()), 
        
        Long.valueOf(1L));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewin\\utils\Ticker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */