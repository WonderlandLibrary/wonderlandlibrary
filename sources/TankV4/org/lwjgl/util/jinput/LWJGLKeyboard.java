package org.lwjgl.util.jinput;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Keyboard;
import net.java.games.input.Rumbler;

final class LWJGLKeyboard extends Keyboard {
   LWJGLKeyboard() {
      super("LWJGLKeyboard", createComponents(), new Controller[0], new Rumbler[0]);
   }

   private static Component[] createComponents() {
      ArrayList var0 = new ArrayList();
      Field[] var1 = org.lwjgl.input.Keyboard.class.getFields();
      Field[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Field var5 = var2[var4];

         try {
            if (Modifier.isStatic(var5.getModifiers()) && var5.getType() == Integer.TYPE && var5.getName().startsWith("KEY_")) {
               int var6 = var5.getInt((Object)null);
               net.java.games.input.Component.Identifier.Key var7 = KeyMap.map(var6);
               if (var7 != net.java.games.input.Component.Identifier.Key.UNKNOWN) {
                  var0.add(new LWJGLKeyboard.Key(var7, var6));
               }
            }
         } catch (IllegalAccessException var8) {
            throw new RuntimeException(var8);
         }
      }

      return (Component[])var0.toArray(new Component[var0.size()]);
   }

   public synchronized void pollDevice() throws IOException {
      if (org.lwjgl.input.Keyboard.isCreated()) {
         org.lwjgl.input.Keyboard.poll();
         Component[] var1 = this.getComponents();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            Component var4 = var1[var3];
            LWJGLKeyboard.Key var5 = (LWJGLKeyboard.Key)var4;
            var5.update();
         }

      }
   }

   protected synchronized boolean getNextDeviceEvent(Event var1) throws IOException {
      if (!org.lwjgl.input.Keyboard.isCreated()) {
         return false;
      } else if (!org.lwjgl.input.Keyboard.next()) {
         return false;
      } else {
         int var2 = org.lwjgl.input.Keyboard.getEventKey();
         if (var2 == 0) {
            return false;
         } else {
            net.java.games.input.Component.Identifier.Key var3 = KeyMap.map(var2);
            if (var3 == null) {
               return false;
            } else {
               Component var4 = this.getComponent(var3);
               if (var4 == null) {
                  return false;
               } else {
                  float var5 = org.lwjgl.input.Keyboard.getEventKeyState() ? 1.0F : 0.0F;
                  var1.set(var4, var5, org.lwjgl.input.Keyboard.getEventNanoseconds());
                  return true;
               }
            }
         }
      }
   }

   private static final class Key extends AbstractComponent {
      private final int lwjgl_key;
      private float value;

      Key(net.java.games.input.Component.Identifier.Key var1, int var2) {
         super(var1.getName(), var1);
         this.lwjgl_key = var2;
      }

      public void update() {
         this.value = org.lwjgl.input.Keyboard.isKeyDown(this.lwjgl_key) ? 1.0F : 0.0F;
      }

      protected float poll() {
         return this.value;
      }

      public boolean isRelative() {
         return false;
      }

      public boolean isAnalog() {
         return false;
      }
   }
}
