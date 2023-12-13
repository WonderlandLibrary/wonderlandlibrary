package my.NewSnake.Tank.me.tireman.hexa.alts;

import java.util.ArrayList;

public class AltManager {
   public static ArrayList registry = new ArrayList();
   public static Alt lastAlt;

   public ArrayList getRegistry() {
      return registry;
   }

   public void setLastAlt(Alt var1) {
      lastAlt = var1;
   }
}
