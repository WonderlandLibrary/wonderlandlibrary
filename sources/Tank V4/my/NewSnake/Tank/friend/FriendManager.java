package my.NewSnake.Tank.friend;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import my.NewSnake.utils.FileUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;

public class FriendManager {
   public static ArrayList friendsList = new ArrayList();
   private static final File FRIEND_DIR = FileUtils.getConfigFile("Friends");

   public static void removeFriend(String var0) {
      Iterator var2 = friendsList.iterator();

      while(var2.hasNext()) {
         Friend var1 = (Friend)var2.next();
         if (var1.name.equalsIgnoreCase(var0)) {
            friendsList.remove(var1);
            break;
         }
      }

      save();
   }

   public static void addFriend(String var0, String var1) {
      friendsList.add(new Friend(var0, var1));
      save();
   }

   public static String replaceText(String var0) {
      Iterator var2 = friendsList.iterator();

      while(var2.hasNext()) {
         Friend var1 = (Friend)var2.next();
         if (var0.contains(var1.name)) {
            var0 = var1.alias;
         }
      }

      return var0;
   }

   public static String getAliasName(String var0) {
      String var1 = "";
      Iterator var3 = friendsList.iterator();

      while(var3.hasNext()) {
         Friend var2 = (Friend)var3.next();
         if (var2.name.equalsIgnoreCase(StringUtils.stripControlCodes(var0))) {
            var1 = var2.alias;
            break;
         }
      }

      Minecraft.getMinecraft();
      if (Minecraft.thePlayer != null) {
         Minecraft.getMinecraft();
         if (Minecraft.thePlayer.getGameProfile().getName() == var0) {
            return var0;
         }
      }

      return var1;
   }

   public static void start() {
      load();
      save();
   }

   public static void load() {
      friendsList.clear();
      List var0 = FileUtils.read(FRIEND_DIR);
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         String var1 = (String)var2.next();

         try {
            String[] var3 = var1.split(":");
            String var4 = var3[0];
            String var5 = var3[1];
            addFriend(var4, var5);
         } catch (Exception var6) {
         }
      }

   }

   public static void save() {
      ArrayList var0 = new ArrayList();
      Iterator var2 = friendsList.iterator();

      while(var2.hasNext()) {
         Friend var1 = (Friend)var2.next();
         String var3 = getAliasName(var1.name);
         var0.add(String.format("%s:%s", var1.name, var3));
      }

      FileUtils.write(FRIEND_DIR, var0, true);
   }

   public static boolean isFriend(String var0) {
      boolean var1 = false;
      Iterator var3 = friendsList.iterator();

      while(var3.hasNext()) {
         Friend var2 = (Friend)var3.next();
         if (var2.name.equalsIgnoreCase(StringUtils.stripControlCodes(var0))) {
            var1 = true;
            break;
         }
      }

      Minecraft.getMinecraft();
      if (Minecraft.thePlayer.getGameProfile().getName() == var0) {
         var1 = true;
      }

      return var1;
   }
}
