package cn.hanabi.utils;

import cn.hanabi.Client;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;

public class ReflectionUtils {
   public static void clickMouse() {
      try {
         String s = !Client.map ? "clickMouse" : "clickMouse";
         Minecraft mc = Minecraft.getMinecraft();
         Class c = mc.getClass();
         Method m = c.getDeclaredMethod(s);
         m.setAccessible(true);
         m.invoke(mc);
      } catch (Exception var4) {
      }

   }

   public static void rightClickMouse() {
      try {
         String s = !Client.map ? "rightClickMouse" : "rightClickMouse";
         Minecraft mc = Minecraft.getMinecraft();
         Class c = mc.getClass();
         Method m = c.getDeclaredMethod(s);
         m.setAccessible(true);
         m.invoke(mc);
      } catch (Exception var4) {
      }

   }

   public static void setLeftClickCounter(int i) {
      try {
         String s = !Client.map ? "leftClickCounter" : "leftClickCounter";
         Minecraft mc = Minecraft.getMinecraft();
         Class c = mc.getClass();
         Field f = c.getDeclaredField(s);
         f.setAccessible(true);
         f.set(mc, i);
      } catch (Exception var5) {
      }

   }

   public static void setRightClickDelayTimer(int i) {
      try {
         String s = !Client.map ? "rightClickDelayTimer" : "rightClickDelayTimer";
         Minecraft mc = Minecraft.getMinecraft();
         Class c = mc.getClass();
         Field f = c.getDeclaredField(s);
         f.setAccessible(true);
         f.set(mc, i);
      } catch (Exception var5) {
      }

   }
}
