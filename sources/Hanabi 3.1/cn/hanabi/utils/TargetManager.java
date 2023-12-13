package cn.hanabi.utils;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;

public class TargetManager {
   private static final ArrayList target = new ArrayList();

   public static ArrayList getTarget() {
      return target;
   }

   public static boolean isTarget(EntityPlayer player) {
      Iterator var1 = target.iterator();

      String friendlist;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         Object o = var1.next();
         friendlist = (String)o;
      } while(!friendlist.equalsIgnoreCase(player.getName()));

      return true;
   }

   public static boolean isTarget(String player) {
      Iterator var1 = target.iterator();

      String targetlist;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         Object o = var1.next();
         targetlist = (String)o;
      } while(!targetlist.equalsIgnoreCase(player));

      return true;
   }
}
