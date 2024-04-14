package de.violence.friend;

import java.util.HashMap;
import java.util.Map;

public class FriendManager {
   static Map friendList = new HashMap();

   public static String getAliasOf(String user) {
      return (String)getFriendList().get(user);
   }

   public static Map getFriendList() {
      return friendList;
   }
}
