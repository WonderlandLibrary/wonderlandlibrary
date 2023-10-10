package org.newdawn.slick;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.util.Log;

public class Input {
   public static final int ANY_CONTROLLER = -1;
   private static final int MAX_BUTTONS = 100;
   public static final int KEY_ESCAPE = 1;
   public static final int KEY_1 = 2;
   public static final int KEY_2 = 3;
   public static final int KEY_3 = 4;
   public static final int KEY_4 = 5;
   public static final int KEY_5 = 6;
   public static final int KEY_6 = 7;
   public static final int KEY_7 = 8;
   public static final int KEY_8 = 9;
   public static final int KEY_9 = 10;
   public static final int KEY_0 = 11;
   public static final int KEY_MINUS = 12;
   public static final int KEY_EQUALS = 13;
   public static final int KEY_BACK = 14;
   public static final int KEY_TAB = 15;
   public static final int KEY_Q = 16;
   public static final int KEY_W = 17;
   public static final int KEY_E = 18;
   public static final int KEY_R = 19;
   public static final int KEY_T = 20;
   public static final int KEY_Y = 21;
   public static final int KEY_U = 22;
   public static final int KEY_I = 23;
   public static final int KEY_O = 24;
   public static final int KEY_P = 25;
   public static final int KEY_LBRACKET = 26;
   public static final int KEY_RBRACKET = 27;
   public static final int KEY_RETURN = 28;
   public static final int KEY_ENTER = 28;
   public static final int KEY_LCONTROL = 29;
   public static final int KEY_A = 30;
   public static final int KEY_S = 31;
   public static final int KEY_D = 32;
   public static final int KEY_F = 33;
   public static final int KEY_G = 34;
   public static final int KEY_H = 35;
   public static final int KEY_J = 36;
   public static final int KEY_K = 37;
   public static final int KEY_L = 38;
   public static final int KEY_SEMICOLON = 39;
   public static final int KEY_APOSTROPHE = 40;
   public static final int KEY_GRAVE = 41;
   public static final int KEY_LSHIFT = 42;
   public static final int KEY_BACKSLASH = 43;
   public static final int KEY_Z = 44;
   public static final int KEY_X = 45;
   public static final int KEY_C = 46;
   public static final int KEY_V = 47;
   public static final int KEY_B = 48;
   public static final int KEY_N = 49;
   public static final int KEY_M = 50;
   public static final int KEY_COMMA = 51;
   public static final int KEY_PERIOD = 52;
   public static final int KEY_SLASH = 53;
   public static final int KEY_RSHIFT = 54;
   public static final int KEY_MULTIPLY = 55;
   public static final int KEY_LMENU = 56;
   public static final int KEY_SPACE = 57;
   public static final int KEY_CAPITAL = 58;
   public static final int KEY_F1 = 59;
   public static final int KEY_F2 = 60;
   public static final int KEY_F3 = 61;
   public static final int KEY_F4 = 62;
   public static final int KEY_F5 = 63;
   public static final int KEY_F6 = 64;
   public static final int KEY_F7 = 65;
   public static final int KEY_F8 = 66;
   public static final int KEY_F9 = 67;
   public static final int KEY_F10 = 68;
   public static final int KEY_NUMLOCK = 69;
   public static final int KEY_SCROLL = 70;
   public static final int KEY_NUMPAD7 = 71;
   public static final int KEY_NUMPAD8 = 72;
   public static final int KEY_NUMPAD9 = 73;
   public static final int KEY_SUBTRACT = 74;
   public static final int KEY_NUMPAD4 = 75;
   public static final int KEY_NUMPAD5 = 76;
   public static final int KEY_NUMPAD6 = 77;
   public static final int KEY_ADD = 78;
   public static final int KEY_NUMPAD1 = 79;
   public static final int KEY_NUMPAD2 = 80;
   public static final int KEY_NUMPAD3 = 81;
   public static final int KEY_NUMPAD0 = 82;
   public static final int KEY_DECIMAL = 83;
   public static final int KEY_F11 = 87;
   public static final int KEY_F12 = 88;
   public static final int KEY_F13 = 100;
   public static final int KEY_F14 = 101;
   public static final int KEY_F15 = 102;
   public static final int KEY_KANA = 112;
   public static final int KEY_CONVERT = 121;
   public static final int KEY_NOCONVERT = 123;
   public static final int KEY_YEN = 125;
   public static final int KEY_NUMPADEQUALS = 141;
   public static final int KEY_CIRCUMFLEX = 144;
   public static final int KEY_AT = 145;
   public static final int KEY_COLON = 146;
   public static final int KEY_UNDERLINE = 147;
   public static final int KEY_KANJI = 148;
   public static final int KEY_STOP = 149;
   public static final int KEY_AX = 150;
   public static final int KEY_UNLABELED = 151;
   public static final int KEY_NUMPADENTER = 156;
   public static final int KEY_RCONTROL = 157;
   public static final int KEY_NUMPADCOMMA = 179;
   public static final int KEY_DIVIDE = 181;
   public static final int KEY_SYSRQ = 183;
   public static final int KEY_RMENU = 184;
   public static final int KEY_PAUSE = 197;
   public static final int KEY_HOME = 199;
   public static final int KEY_UP = 200;
   public static final int KEY_PRIOR = 201;
   public static final int KEY_LEFT = 203;
   public static final int KEY_RIGHT = 205;
   public static final int KEY_END = 207;
   public static final int KEY_DOWN = 208;
   public static final int KEY_NEXT = 209;
   public static final int KEY_INSERT = 210;
   public static final int KEY_DELETE = 211;
   public static final int KEY_LWIN = 219;
   public static final int KEY_RWIN = 220;
   public static final int KEY_APPS = 221;
   public static final int KEY_POWER = 222;
   public static final int KEY_SLEEP = 223;
   public static final int KEY_LALT = 56;
   public static final int KEY_RALT = 184;
   private static final int LEFT = 0;
   private static final int RIGHT = 1;
   private static final int UP = 2;
   private static final int DOWN = 3;
   private static final int BUTTON1 = 4;
   private static final int BUTTON2 = 5;
   private static final int BUTTON3 = 6;
   private static final int BUTTON4 = 7;
   private static final int BUTTON5 = 8;
   private static final int BUTTON6 = 9;
   private static final int BUTTON7 = 10;
   private static final int BUTTON8 = 11;
   private static final int BUTTON9 = 12;
   private static final int BUTTON10 = 13;
   public static final int MOUSE_LEFT_BUTTON = 0;
   public static final int MOUSE_RIGHT_BUTTON = 1;
   public static final int MOUSE_MIDDLE_BUTTON = 2;
   private static boolean controllersInited = false;
   private static ArrayList controllers = new ArrayList();
   private int lastMouseX;
   private int lastMouseY;
   protected boolean[] mousePressed = new boolean[10];
   private boolean[][] controllerPressed = new boolean[100][100];
   protected char[] keys = new char[1024];
   protected boolean[] pressed = new boolean[1024];
   protected long[] nextRepeat = new long[1024];
   private boolean[][] controls = new boolean[10][110];
   protected boolean consumed = false;
   protected HashSet allListeners = new HashSet();
   protected ArrayList keyListeners = new ArrayList();
   protected ArrayList keyListenersToAdd = new ArrayList();
   protected ArrayList mouseListeners = new ArrayList();
   protected ArrayList mouseListenersToAdd = new ArrayList();
   protected ArrayList controllerListeners = new ArrayList();
   private int wheel;
   private int height;
   private boolean displayActive = true;
   private boolean keyRepeat;
   private int keyRepeatInitial;
   private int keyRepeatInterval;
   private boolean paused;
   private float scaleX = 1.0F;
   private float scaleY = 1.0F;
   private float xoffset = 0.0F;
   private float yoffset = 0.0F;
   private int doubleClickDelay = 250;
   private long doubleClickTimeout = 0L;
   private int clickX;
   private int clickY;
   private int clickButton;
   private int pressedX = -1;
   private int pressedY = -1;
   private int mouseClickTolerance = 5;

   public static void disableControllers() {
      controllersInited = true;
   }

   public Input(int var1) {
      this.init(var1);
   }

   public void setDoubleClickInterval(int var1) {
      this.doubleClickDelay = var1;
   }

   public void setMouseClickTolerance(int var1) {
      this.mouseClickTolerance = var1;
   }

   public void setScale(float var1, float var2) {
      this.scaleX = var1;
      this.scaleY = var2;
   }

   public void setOffset(float var1, float var2) {
      this.xoffset = var1;
      this.yoffset = var2;
   }

   public void resetInputTransform() {
      this.setOffset(0.0F, 0.0F);
      this.setScale(1.0F, 1.0F);
   }

   public void addListener(InputListener var1) {
      this.addKeyListener(var1);
      this.addMouseListener(var1);
      this.addControllerListener(var1);
   }

   public void addKeyListener(KeyListener var1) {
      this.keyListenersToAdd.add(var1);
   }

   private void addKeyListenerImpl(KeyListener var1) {
      if (!this.keyListeners.contains(var1)) {
         this.keyListeners.add(var1);
         this.allListeners.add(var1);
      }
   }

   public void addMouseListener(MouseListener var1) {
      this.mouseListenersToAdd.add(var1);
   }

   private void addMouseListenerImpl(MouseListener var1) {
      if (!this.mouseListeners.contains(var1)) {
         this.mouseListeners.add(var1);
         this.allListeners.add(var1);
      }
   }

   public void addControllerListener(ControllerListener var1) {
      if (!this.controllerListeners.contains(var1)) {
         this.controllerListeners.add(var1);
         this.allListeners.add(var1);
      }
   }

   public void removeAllListeners() {
      this.removeAllKeyListeners();
      this.removeAllMouseListeners();
      this.removeAllControllerListeners();
   }

   public void removeAllKeyListeners() {
      this.allListeners.removeAll(this.keyListeners);
      this.keyListeners.clear();
   }

   public void removeAllMouseListeners() {
      this.allListeners.removeAll(this.mouseListeners);
      this.mouseListeners.clear();
   }

   public void removeAllControllerListeners() {
      this.allListeners.removeAll(this.controllerListeners);
      this.controllerListeners.clear();
   }

   public void addPrimaryListener(InputListener var1) {
      this.removeListener(var1);
      this.keyListeners.add(0, var1);
      this.mouseListeners.add(0, var1);
      this.controllerListeners.add(0, var1);
      this.allListeners.add(var1);
   }

   public void removeListener(InputListener var1) {
      this.removeKeyListener(var1);
      this.removeMouseListener(var1);
      this.removeControllerListener(var1);
   }

   public void removeKeyListener(KeyListener var1) {
      this.keyListeners.remove(var1);
      if (!this.mouseListeners.contains(var1) && !this.controllerListeners.contains(var1)) {
         this.allListeners.remove(var1);
      }

   }

   public void removeControllerListener(ControllerListener var1) {
      this.controllerListeners.remove(var1);
      if (!this.mouseListeners.contains(var1) && !this.keyListeners.contains(var1)) {
         this.allListeners.remove(var1);
      }

   }

   public void removeMouseListener(MouseListener var1) {
      this.mouseListeners.remove(var1);
      if (!this.controllerListeners.contains(var1) && !this.keyListeners.contains(var1)) {
         this.allListeners.remove(var1);
      }

   }

   void init(int var1) {
      this.height = var1;
      this.lastMouseX = this.getMouseX();
      this.lastMouseY = this.getMouseY();
   }

   public static String getKeyName(int var0) {
      return Keyboard.getKeyName(var0);
   }

   public boolean isKeyPressed(int var1) {
      if (this.pressed[var1]) {
         this.pressed[var1] = false;
         return true;
      } else {
         return false;
      }
   }

   public boolean isMousePressed(int var1) {
      if (this.mousePressed[var1]) {
         this.mousePressed[var1] = false;
         return true;
      } else {
         return false;
      }
   }

   public boolean isControlPressed(int var1) {
      return this.isControlPressed(var1, 0);
   }

   public boolean isControlPressed(int var1, int var2) {
      if (this.controllerPressed[var2][var1]) {
         this.controllerPressed[var2][var1] = false;
         return true;
      } else {
         return false;
      }
   }

   public void clearControlPressedRecord() {
      for(int var1 = 0; var1 < controllers.size(); ++var1) {
         Arrays.fill(this.controllerPressed[var1], false);
      }

   }

   public void clearKeyPressedRecord() {
      Arrays.fill(this.pressed, false);
   }

   public void clearMousePressedRecord() {
      Arrays.fill(this.mousePressed, false);
   }

   public boolean isKeyDown(int var1) {
      return Keyboard.isKeyDown(var1);
   }

   public int getAbsoluteMouseX() {
      return Mouse.getX();
   }

   public int getAbsoluteMouseY() {
      return this.height - Mouse.getY();
   }

   public int getMouseX() {
      return (int)((float)Mouse.getX() * this.scaleX + this.xoffset);
   }

   public int getMouseY() {
      return (int)((float)(this.height - Mouse.getY()) * this.scaleY + this.yoffset);
   }

   public boolean isMouseButtonDown(int var1) {
      return Mouse.isButtonDown(var1);
   }

   public int getControllerCount() {
      try {
         this.initControllers();
      } catch (SlickException var2) {
         throw new RuntimeException("Failed to initialise controllers");
      }

      return controllers.size();
   }

   public int getAxisCount(int var1) {
      return ((Controller)controllers.get(var1)).getAxisCount();
   }

   public float getAxisValue(int var1, int var2) {
      return ((Controller)controllers.get(var1)).getAxisValue(var2);
   }

   public String getAxisName(int var1, int var2) {
      return ((Controller)controllers.get(var1)).getAxisName(var2);
   }

   public boolean isButton1Pressed(int var1) {
      return this.isButtonPressed(0, var1);
   }

   public boolean isButton2Pressed(int var1) {
      return this.isButtonPressed(1, var1);
   }

   public boolean isButton3Pressed(int var1) {
      return this.isButtonPressed(2, var1);
   }

   public void initControllers() throws SlickException {
      if (!controllersInited) {
         controllersInited = true;

         try {
            Controllers.create();
            int var1 = Controllers.getControllerCount();

            int var2;
            for(var2 = 0; var2 < var1; ++var2) {
               Controller var3 = Controllers.getController(var2);
               if (var3.getButtonCount() >= 3 && var3.getButtonCount() < 100) {
                  controllers.add(var3);
               }
            }

            Log.info("Found " + controllers.size() + " controllers");

            for(var2 = 0; var2 < controllers.size(); ++var2) {
               Log.info(var2 + " : " + ((Controller)controllers.get(var2)).getName());
            }
         } catch (LWJGLException var4) {
            if (var4.getCause() instanceof ClassNotFoundException) {
               throw new SlickException("Unable to create controller - no jinput found - add jinput.jar to your classpath");
            }

            throw new SlickException("Unable to create controllers");
         } catch (NoClassDefFoundError var5) {
         }

      }
   }

   public void consumeEvent() {
      this.consumed = true;
   }

   private int resolveEventKey(int var1, char var2) {
      return var2 != '=' && var1 != 0 ? var1 : 13;
   }

   public void considerDoubleClick(int var1, int var2, int var3) {
      if (this.doubleClickTimeout == 0L) {
         this.clickX = var2;
         this.clickY = var3;
         this.clickButton = var1;
         this.doubleClickTimeout = System.currentTimeMillis() + (long)this.doubleClickDelay;
         this.fireMouseClicked(var1, var2, var3, 1);
      } else if (this.clickButton == var1 && System.currentTimeMillis() < this.doubleClickTimeout) {
         this.fireMouseClicked(var1, var2, var3, 2);
         this.doubleClickTimeout = 0L;
      }

   }

   public void poll(int param1, int param2) {
      // $FF: Couldn't be decompiled
   }

   /** @deprecated */
   public void enableKeyRepeat(int var1, int var2) {
      Keyboard.enableRepeatEvents(true);
   }

   public void enableKeyRepeat() {
      Keyboard.enableRepeatEvents(true);
   }

   public void disableKeyRepeat() {
      Keyboard.enableRepeatEvents(false);
   }

   public boolean isKeyRepeatEnabled() {
      return Keyboard.areRepeatEventsEnabled();
   }

   private void fireControlPress(int var1, int var2) {
      this.consumed = false;

      for(int var3 = 0; var3 < this.controllerListeners.size(); ++var3) {
         ControllerListener var4 = (ControllerListener)this.controllerListeners.get(var3);
         if (var4.isAcceptingInput()) {
            switch(var1) {
            case 0:
               var4.controllerLeftPressed(var2);
               break;
            case 1:
               var4.controllerRightPressed(var2);
               break;
            case 2:
               var4.controllerUpPressed(var2);
               break;
            case 3:
               var4.controllerDownPressed(var2);
               break;
            default:
               var4.controllerButtonPressed(var2, var1 - 4 + 1);
            }

            if (this.consumed) {
               break;
            }
         }
      }

   }

   private void fireControlRelease(int var1, int var2) {
      this.consumed = false;

      for(int var3 = 0; var3 < this.controllerListeners.size(); ++var3) {
         ControllerListener var4 = (ControllerListener)this.controllerListeners.get(var3);
         if (var4.isAcceptingInput()) {
            switch(var1) {
            case 0:
               var4.controllerLeftReleased(var2);
               break;
            case 1:
               var4.controllerRightReleased(var2);
               break;
            case 2:
               var4.controllerUpReleased(var2);
               break;
            case 3:
               var4.controllerDownReleased(var2);
               break;
            default:
               var4.controllerButtonReleased(var2, var1 - 4 + 1);
            }

            if (this.consumed) {
               break;
            }
         }
      }

   }

   public void pause() {
      this.paused = true;
      this.clearKeyPressedRecord();
      this.clearMousePressedRecord();
      this.clearControlPressedRecord();
   }

   public void resume() {
      this.paused = false;
   }

   private void fireMouseClicked(int var1, int var2, int var3, int var4) {
      this.consumed = false;

      for(int var5 = 0; var5 < this.mouseListeners.size(); ++var5) {
         MouseListener var6 = (MouseListener)this.mouseListeners.get(var5);
         if (var6.isAcceptingInput()) {
            var6.mouseClicked(var1, var2, var3, var4);
            if (this.consumed) {
               break;
            }
         }
      }

   }

   private class NullOutputStream extends OutputStream {
      private final Input this$0;

      private NullOutputStream(Input var1) {
         this.this$0 = var1;
      }

      public void write(int var1) throws IOException {
      }
   }
}
