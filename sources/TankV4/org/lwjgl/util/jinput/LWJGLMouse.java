package org.lwjgl.util.jinput;

import java.io.IOException;
import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Mouse;
import net.java.games.input.Rumbler;

final class LWJGLMouse extends Mouse {
   private static final int EVENT_X = 1;
   private static final int EVENT_Y = 2;
   private static final int EVENT_WHEEL = 3;
   private static final int EVENT_BUTTON = 4;
   private static final int EVENT_DONE = 5;
   private int event_state = 5;

   LWJGLMouse() {
      super("LWJGLMouse", createComponents(), new Controller[0], new Rumbler[0]);
   }

   private static Component[] createComponents() {
      return new Component[]{new LWJGLMouse.Axis(net.java.games.input.Component.Identifier.Axis.X), new LWJGLMouse.Axis(net.java.games.input.Component.Identifier.Axis.Y), new LWJGLMouse.Axis(net.java.games.input.Component.Identifier.Axis.Z), new LWJGLMouse.Button(net.java.games.input.Component.Identifier.Button.LEFT), new LWJGLMouse.Button(net.java.games.input.Component.Identifier.Button.MIDDLE), new LWJGLMouse.Button(net.java.games.input.Component.Identifier.Button.RIGHT)};
   }

   public synchronized void pollDevice() throws IOException {
      if (org.lwjgl.input.Mouse.isCreated()) {
         org.lwjgl.input.Mouse.poll();

         for(int var1 = 0; var1 < 3; ++var1) {
            this.setButtonState(var1);
         }

      }
   }

   private LWJGLMouse.Button map(int var1) {
      switch(var1) {
      case 0:
         return (LWJGLMouse.Button)this.getLeft();
      case 1:
         return (LWJGLMouse.Button)this.getRight();
      case 2:
         return (LWJGLMouse.Button)this.getMiddle();
      default:
         return null;
      }
   }

   private void setButtonState(int var1) {
      LWJGLMouse.Button var2 = this.map(var1);
      if (var2 != null) {
         var2.setValue(org.lwjgl.input.Mouse.isButtonDown(var1) ? 1.0F : 0.0F);
      }

   }

   protected synchronized boolean getNextDeviceEvent(Event var1) throws IOException {
      if (!org.lwjgl.input.Mouse.isCreated()) {
         return false;
      } else {
         while(true) {
            while(true) {
               long var2 = org.lwjgl.input.Mouse.getEventNanoseconds();
               switch(this.event_state) {
               case 1:
                  this.event_state = 2;
                  int var4 = org.lwjgl.input.Mouse.getEventDX();
                  if (var4 != 0) {
                     var1.set(this.getX(), (float)var4, var2);
                     return true;
                  }
                  break;
               case 2:
                  this.event_state = 3;
                  int var5 = -org.lwjgl.input.Mouse.getEventDY();
                  if (var5 != 0) {
                     var1.set(this.getY(), (float)var5, var2);
                     return true;
                  }
                  break;
               case 3:
                  this.event_state = 4;
                  int var6 = org.lwjgl.input.Mouse.getEventDWheel();
                  if (var6 != 0) {
                     var1.set(this.getWheel(), (float)var6, var2);
                     return true;
                  }
                  break;
               case 4:
                  this.event_state = 5;
                  int var7 = org.lwjgl.input.Mouse.getEventButton();
                  if (var7 != -1) {
                     LWJGLMouse.Button var8 = this.map(var7);
                     if (var8 != null) {
                        var1.set(var8, org.lwjgl.input.Mouse.getEventButtonState() ? 1.0F : 0.0F, var2);
                        return true;
                     }
                  }
                  break;
               case 5:
                  if (!org.lwjgl.input.Mouse.next()) {
                     return false;
                  }

                  this.event_state = 1;
               }
            }
         }
      }
   }

   static final class Button extends AbstractComponent {
      private float value;

      Button(net.java.games.input.Component.Identifier.Button var1) {
         super(var1.getName(), var1);
      }

      void setValue(float var1) {
         this.value = var1;
      }

      protected float poll() throws IOException {
         return this.value;
      }

      public boolean isRelative() {
         return false;
      }

      public boolean isAnalog() {
         return false;
      }
   }

   static final class Axis extends AbstractComponent {
      Axis(net.java.games.input.Component.Identifier.Axis var1) {
         super(var1.getName(), var1);
      }

      public boolean isRelative() {
         return true;
      }

      protected float poll() throws IOException {
         return 0.0F;
      }

      public boolean isAnalog() {
         return true;
      }
   }
}
