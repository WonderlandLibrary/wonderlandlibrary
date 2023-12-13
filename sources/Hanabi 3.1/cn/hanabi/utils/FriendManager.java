package cn.hanabi.utils;

import java.util.ArrayList;
import java.util.Iterator;

public class FriendManager {
   private static final ArrayList friends = new ArrayList();

   public static ArrayList getFriends() {
      return friends;
   }

   public static boolean isFriend(String player) {
      Iterator var1 = friends.iterator();

      String friendlist;
      do {
         if (!var1.hasNext()) {
            return false;
         }

         Object friend = var1.next();
         friendlist = (String)friend;
      } while(!friendlist.equalsIgnoreCase(player));

      return true;
   }
}
