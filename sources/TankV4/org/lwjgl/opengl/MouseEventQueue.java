package org.lwjgl.opengl;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

class MouseEventQueue extends EventQueue implements MouseListener, MouseMotionListener, MouseWheelListener {
   private static final int WHEEL_SCALE = 120;
   public static final int NUM_BUTTONS = 3;
   private final Component component;
   private boolean grabbed;
   private int accum_dx;
   private int accum_dy;
   private int accum_dz;
   private int last_x;
   private int last_y;
   private boolean saved_control_state;
   private final ByteBuffer event = ByteBuffer.allocate(22);
   private final byte[] buttons = new byte[3];

   MouseEventQueue(Component var1) {
      super(22);
      this.component = var1;
   }

   public synchronized void register() {
      this.resetCursorToCenter();
      if (this.component != null) {
         this.component.addMouseListener(this);
         this.component.addMouseMotionListener(this);
         this.component.addMouseWheelListener(this);
      }

   }

   public synchronized void unregister() {
      if (this.component != null) {
         this.component.removeMouseListener(this);
         this.component.removeMouseMotionListener(this);
         this.component.removeMouseWheelListener(this);
      }

   }

   protected Component getComponent() {
      return this.component;
   }

   public synchronized void setGrabbed(boolean var1) {
      this.grabbed = var1;
      this.resetCursorToCenter();
   }

   public synchronized boolean isGrabbed() {
      return this.grabbed;
   }

   protected int transformY(int var1) {
      return this.component != null ? this.component.getHeight() - 1 - var1 : var1;
   }

   protected void resetCursorToCenter() {
      this.clearEvents();
      this.accum_dx = this.accum_dy = 0;
      if (this.component != null) {
         Point var1 = AWTUtil.getCursorPosition(this.component);
         if (var1 != null) {
            this.last_x = var1.x;
            this.last_y = var1.y;
         }
      }

   }

   private void putMouseEvent(byte var1, byte var2, int var3, long var4) {
      if (this.grabbed) {
         this.putMouseEventWithCoords(var1, var2, 0, 0, var3, var4);
      } else {
         this.putMouseEventWithCoords(var1, var2, this.last_x, this.last_y, var3, var4);
      }

   }

   protected void putMouseEventWithCoords(byte var1, byte var2, int var3, int var4, int var5, long var6) {
      this.event.clear();
      this.event.put(var1).put(var2).putInt(var3).putInt(var4).putInt(var5).putLong(var6);
      this.event.flip();
      this.putEvent(this.event);
   }

   public synchronized void poll(IntBuffer var1, ByteBuffer var2) {
      if (this.grabbed) {
         var1.put(0, this.accum_dx);
         var1.put(1, this.accum_dy);
      } else {
         var1.put(0, this.last_x);
         var1.put(1, this.last_y);
      }

      var1.put(2, this.accum_dz);
      this.accum_dx = this.accum_dy = this.accum_dz = 0;
      int var3 = var2.position();
      var2.put(this.buttons, 0, this.buttons.length);
      var2.position(var3);
   }

   private void setCursorPos(int var1, int var2, long var3) {
      var2 = this.transformY(var2);
      if (!this.grabbed) {
         int var5 = var1 - this.last_x;
         int var6 = var2 - this.last_y;
         this.addDelta(var5, var6);
         this.last_x = var1;
         this.last_y = var2;
         this.putMouseEventWithCoords((byte)-1, (byte)0, var1, var2, 0, var3);
      }
   }

   protected void addDelta(int var1, int var2) {
      this.accum_dx += var1;
      this.accum_dy += var2;
   }

   public void mouseClicked(MouseEvent var1) {
   }

   public void mouseEntered(MouseEvent var1) {
   }

   public void mouseExited(MouseEvent var1) {
   }

   private void handleButton(MouseEvent var1) {
      byte var2;
      switch(var1.getID()) {
      case 501:
         var2 = 1;
         break;
      case 502:
         var2 = 0;
         break;
      default:
         throw new IllegalArgumentException("Not a valid event ID: " + var1.getID());
      }

      byte var3;
      switch(var1.getButton()) {
      case 0:
         return;
      case 1:
         if (var2 == 1) {
            this.saved_control_state = var1.isControlDown();
         }

         if (this.saved_control_state) {
            if (this.buttons[1] == var2) {
               return;
            }

            var3 = 1;
         } else {
            var3 = 0;
         }
         break;
      case 2:
         var3 = 2;
         break;
      case 3:
         if (this.buttons[1] == var2) {
            return;
         }

         var3 = 1;
         break;
      default:
         throw new IllegalArgumentException("Not a valid button: " + var1.getButton());
      }

      this.setButton(var3, var2, var1.getWhen() * 1000000L);
   }

   public synchronized void mousePressed(MouseEvent var1) {
      this.handleButton(var1);
   }

   private void setButton(byte var1, byte var2, long var3) {
      this.buttons[var1] = var2;
      this.putMouseEvent(var1, var2, 0, var3);
   }

   public synchronized void mouseReleased(MouseEvent var1) {
      this.handleButton(var1);
   }

   private void handleMotion(MouseEvent var1) {
      if (this.grabbed) {
         this.updateDeltas(var1.getWhen() * 1000000L);
      } else {
         this.setCursorPos(var1.getX(), var1.getY(), var1.getWhen() * 1000000L);
      }

   }

   public synchronized void mouseDragged(MouseEvent var1) {
      this.handleMotion(var1);
   }

   public synchronized void mouseMoved(MouseEvent var1) {
      this.handleMotion(var1);
   }

   private void handleWheel(int var1, long var2) {
      this.accum_dz += var1;
      this.putMouseEvent((byte)-1, (byte)0, var1, var2);
   }

   protected void updateDeltas(long var1) {
   }

   public synchronized void mouseWheelMoved(MouseWheelEvent var1) {
      int var2 = -var1.getWheelRotation() * 120;
      this.handleWheel(var2, var1.getWhen() * 1000000L);
   }
}
