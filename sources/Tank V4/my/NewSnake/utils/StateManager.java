package my.NewSnake.utils;

public class StateManager {
   private static boolean offsetLastPacketAura;

   public static void setOffsetLastPacketAura(boolean var0) {
      offsetLastPacketAura = var0;
   }

   public static boolean offsetLastPacketAura() {
      return offsetLastPacketAura;
   }
}
