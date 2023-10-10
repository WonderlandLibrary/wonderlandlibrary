package org.lwjgl.input;

import java.util.ArrayList;
import java.util.Iterator;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Controller.Type;
import org.lwjgl.LWJGLException;

public class Controllers {
   private static ArrayList controllers = new ArrayList();
   private static int controllerCount;
   private static ArrayList events = new ArrayList();
   private static ControllerEvent event;
   private static boolean created;

   public static void create() throws LWJGLException {
      if (!created) {
         try {
            ControllerEnvironment var0 = ControllerEnvironment.getDefaultEnvironment();
            net.java.games.input.Controller[] var1 = var0.getControllers();
            ArrayList var2 = new ArrayList();
            net.java.games.input.Controller[] var3 = var1;
            int var4 = var1.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               net.java.games.input.Controller var6 = var3[var5];
               if (!var6.getType().equals(Type.KEYBOARD) && !var6.getType().equals(Type.MOUSE)) {
                  var2.add(var6);
               }
            }

            Iterator var8 = var2.iterator();

            while(var8.hasNext()) {
               net.java.games.input.Controller var9 = (net.java.games.input.Controller)var8.next();
               createController(var9);
            }

            created = true;
         } catch (Throwable var7) {
            throw new LWJGLException("Failed to initialise controllers", var7);
         }
      }
   }

   private static void createController(net.java.games.input.Controller var0) {
      net.java.games.input.Controller[] var1 = var0.getControllers();
      if (var1.length == 0) {
         JInputController var2 = new JInputController(controllerCount, var0);
         controllers.add(var2);
         ++controllerCount;
      } else {
         net.java.games.input.Controller[] var6 = var1;
         int var3 = var1.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            net.java.games.input.Controller var5 = var6[var4];
            createController(var5);
         }
      }

   }

   public static Controller getController(int var0) {
      return (Controller)controllers.get(var0);
   }

   public static int getControllerCount() {
      return controllers.size();
   }

   public static void poll() {
      for(int var0 = 0; var0 < controllers.size(); ++var0) {
         getController(var0).poll();
      }

   }

   public static void clearEvents() {
      events.clear();
   }

   public static boolean next() {
      if (events.size() == 0) {
         event = null;
         return false;
      } else {
         event = (ControllerEvent)events.remove(0);
         return event != null;
      }
   }

   public static boolean isCreated() {
      return created;
   }

   public static void destroy() {
   }

   public static Controller getEventSource() {
      return event.getSource();
   }

   public static int getEventControlIndex() {
      return event.getControlIndex();
   }

   public static boolean isEventButton() {
      return event.isButton();
   }

   public static boolean isEventAxis() {
      return event.isAxis();
   }

   public static boolean isEventXAxis() {
      return event.isXAxis();
   }

   public static boolean isEventYAxis() {
      return event.isYAxis();
   }

   public static boolean isEventPovX() {
      return event.isPovX();
   }

   public static boolean isEventPovY() {
      return event.isPovY();
   }

   public static long getEventNanoseconds() {
      return event.getTimeStamp();
   }

   public static boolean getEventButtonState() {
      return event.getButtonState();
   }

   public static float getEventXAxisValue() {
      return event.getXAxisValue();
   }

   public static float getEventYAxisValue() {
      return event.getYAxisValue();
   }

   static void addEvent(ControllerEvent var0) {
      if (var0 != null) {
         events.add(var0);
      }

   }
}
