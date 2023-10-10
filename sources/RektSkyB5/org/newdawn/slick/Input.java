/*
 * Decompiled with CFR 0.152.
 */
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
import org.lwjgl.opengl.Display;
import org.newdawn.slick.ControlledInputReciever;
import org.newdawn.slick.ControllerListener;
import org.newdawn.slick.InputListener;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
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
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float xoffset = 0.0f;
    private float yoffset = 0.0f;
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

    public Input(int height) {
        this.init(height);
    }

    public void setDoubleClickInterval(int delay) {
        this.doubleClickDelay = delay;
    }

    public void setMouseClickTolerance(int mouseClickTolerance) {
        this.mouseClickTolerance = mouseClickTolerance;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void setOffset(float xoffset, float yoffset) {
        this.xoffset = xoffset;
        this.yoffset = yoffset;
    }

    public void resetInputTransform() {
        this.setOffset(0.0f, 0.0f);
        this.setScale(1.0f, 1.0f);
    }

    public void addListener(InputListener listener) {
        this.addKeyListener(listener);
        this.addMouseListener(listener);
        this.addControllerListener(listener);
    }

    public void addKeyListener(KeyListener listener) {
        this.keyListenersToAdd.add(listener);
    }

    private void addKeyListenerImpl(KeyListener listener) {
        if (this.keyListeners.contains(listener)) {
            return;
        }
        this.keyListeners.add(listener);
        this.allListeners.add(listener);
    }

    public void addMouseListener(MouseListener listener) {
        this.mouseListenersToAdd.add(listener);
    }

    private void addMouseListenerImpl(MouseListener listener) {
        if (this.mouseListeners.contains(listener)) {
            return;
        }
        this.mouseListeners.add(listener);
        this.allListeners.add(listener);
    }

    public void addControllerListener(ControllerListener listener) {
        if (this.controllerListeners.contains(listener)) {
            return;
        }
        this.controllerListeners.add(listener);
        this.allListeners.add(listener);
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

    public void addPrimaryListener(InputListener listener) {
        this.removeListener(listener);
        this.keyListeners.add(0, listener);
        this.mouseListeners.add(0, listener);
        this.controllerListeners.add(0, listener);
        this.allListeners.add(listener);
    }

    public void removeListener(InputListener listener) {
        this.removeKeyListener(listener);
        this.removeMouseListener(listener);
        this.removeControllerListener(listener);
    }

    public void removeKeyListener(KeyListener listener) {
        this.keyListeners.remove(listener);
        if (!this.mouseListeners.contains(listener) && !this.controllerListeners.contains(listener)) {
            this.allListeners.remove(listener);
        }
    }

    public void removeControllerListener(ControllerListener listener) {
        this.controllerListeners.remove(listener);
        if (!this.mouseListeners.contains(listener) && !this.keyListeners.contains(listener)) {
            this.allListeners.remove(listener);
        }
    }

    public void removeMouseListener(MouseListener listener) {
        this.mouseListeners.remove(listener);
        if (!this.controllerListeners.contains(listener) && !this.keyListeners.contains(listener)) {
            this.allListeners.remove(listener);
        }
    }

    void init(int height) {
        this.height = height;
        this.lastMouseX = this.getMouseX();
        this.lastMouseY = this.getMouseY();
    }

    public static String getKeyName(int code) {
        return Keyboard.getKeyName(code);
    }

    public boolean isKeyPressed(int code) {
        if (this.pressed[code]) {
            this.pressed[code] = false;
            return true;
        }
        return false;
    }

    public boolean isMousePressed(int button) {
        if (this.mousePressed[button]) {
            this.mousePressed[button] = false;
            return true;
        }
        return false;
    }

    public boolean isControlPressed(int button) {
        return this.isControlPressed(button, 0);
    }

    public boolean isControlPressed(int button, int controller) {
        if (this.controllerPressed[controller][button]) {
            this.controllerPressed[controller][button] = false;
            return true;
        }
        return false;
    }

    public void clearControlPressedRecord() {
        for (int i2 = 0; i2 < controllers.size(); ++i2) {
            Arrays.fill(this.controllerPressed[i2], false);
        }
    }

    public void clearKeyPressedRecord() {
        Arrays.fill(this.pressed, false);
    }

    public void clearMousePressedRecord() {
        Arrays.fill(this.mousePressed, false);
    }

    public boolean isKeyDown(int code) {
        return Keyboard.isKeyDown(code);
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

    public boolean isMouseButtonDown(int button) {
        return Mouse.isButtonDown(button);
    }

    private boolean anyMouseDown() {
        for (int i2 = 0; i2 < 3; ++i2) {
            if (!Mouse.isButtonDown(i2)) continue;
            return true;
        }
        return false;
    }

    public int getControllerCount() {
        try {
            this.initControllers();
        }
        catch (SlickException e2) {
            throw new RuntimeException("Failed to initialise controllers");
        }
        return controllers.size();
    }

    public int getAxisCount(int controller) {
        return ((Controller)controllers.get(controller)).getAxisCount();
    }

    public float getAxisValue(int controller, int axis) {
        return ((Controller)controllers.get(controller)).getAxisValue(axis);
    }

    public String getAxisName(int controller, int axis) {
        return ((Controller)controllers.get(controller)).getAxisName(axis);
    }

    public boolean isControllerLeft(int controller) {
        if (controller >= this.getControllerCount()) {
            return false;
        }
        if (controller == -1) {
            for (int i2 = 0; i2 < controllers.size(); ++i2) {
                if (!this.isControllerLeft(i2)) continue;
                return true;
            }
            return false;
        }
        return ((Controller)controllers.get(controller)).getXAxisValue() < -0.5f || ((Controller)controllers.get(controller)).getPovX() < -0.5f;
    }

    public boolean isControllerRight(int controller) {
        if (controller >= this.getControllerCount()) {
            return false;
        }
        if (controller == -1) {
            for (int i2 = 0; i2 < controllers.size(); ++i2) {
                if (!this.isControllerRight(i2)) continue;
                return true;
            }
            return false;
        }
        return ((Controller)controllers.get(controller)).getXAxisValue() > 0.5f || ((Controller)controllers.get(controller)).getPovX() > 0.5f;
    }

    public boolean isControllerUp(int controller) {
        if (controller >= this.getControllerCount()) {
            return false;
        }
        if (controller == -1) {
            for (int i2 = 0; i2 < controllers.size(); ++i2) {
                if (!this.isControllerUp(i2)) continue;
                return true;
            }
            return false;
        }
        return ((Controller)controllers.get(controller)).getYAxisValue() < -0.5f || ((Controller)controllers.get(controller)).getPovY() < -0.5f;
    }

    public boolean isControllerDown(int controller) {
        if (controller >= this.getControllerCount()) {
            return false;
        }
        if (controller == -1) {
            for (int i2 = 0; i2 < controllers.size(); ++i2) {
                if (!this.isControllerDown(i2)) continue;
                return true;
            }
            return false;
        }
        return ((Controller)controllers.get(controller)).getYAxisValue() > 0.5f || ((Controller)controllers.get(controller)).getPovY() > 0.5f;
    }

    public boolean isButtonPressed(int index, int controller) {
        if (controller >= this.getControllerCount()) {
            return false;
        }
        if (controller == -1) {
            for (int i2 = 0; i2 < controllers.size(); ++i2) {
                if (!this.isButtonPressed(index, i2)) continue;
                return true;
            }
            return false;
        }
        return ((Controller)controllers.get(controller)).isButtonPressed(index);
    }

    public boolean isButton1Pressed(int controller) {
        return this.isButtonPressed(0, controller);
    }

    public boolean isButton2Pressed(int controller) {
        return this.isButtonPressed(1, controller);
    }

    public boolean isButton3Pressed(int controller) {
        return this.isButtonPressed(2, controller);
    }

    public void initControllers() throws SlickException {
        if (controllersInited) {
            return;
        }
        controllersInited = true;
        try {
            int i2;
            Controllers.create();
            int count = Controllers.getControllerCount();
            for (i2 = 0; i2 < count; ++i2) {
                Controller controller = Controllers.getController(i2);
                if (controller.getButtonCount() < 3 || controller.getButtonCount() >= 100) continue;
                controllers.add(controller);
            }
            Log.info("Found " + controllers.size() + " controllers");
            for (i2 = 0; i2 < controllers.size(); ++i2) {
                Log.info(i2 + " : " + ((Controller)controllers.get(i2)).getName());
            }
        }
        catch (LWJGLException e2) {
            if (e2.getCause() instanceof ClassNotFoundException) {
                throw new SlickException("Unable to create controller - no jinput found - add jinput.jar to your classpath");
            }
            throw new SlickException("Unable to create controllers");
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            // empty catch block
        }
    }

    public void consumeEvent() {
        this.consumed = true;
    }

    private int resolveEventKey(int key, char c2) {
        if (c2 == '=' || key == 0) {
            return 13;
        }
        return key;
    }

    public void considerDoubleClick(int button, int x2, int y2) {
        if (this.doubleClickTimeout == 0L) {
            this.clickX = x2;
            this.clickY = y2;
            this.clickButton = button;
            this.doubleClickTimeout = System.currentTimeMillis() + (long)this.doubleClickDelay;
            this.fireMouseClicked(button, x2, y2, 1);
        } else if (this.clickButton == button && System.currentTimeMillis() < this.doubleClickTimeout) {
            this.fireMouseClicked(button, x2, y2, 2);
            this.doubleClickTimeout = 0L;
        }
    }

    public void poll(int width, int height) {
        int i2;
        if (this.paused) {
            this.clearControlPressedRecord();
            this.clearKeyPressedRecord();
            this.clearMousePressedRecord();
            while (Keyboard.next()) {
            }
            while (Mouse.next()) {
            }
            return;
        }
        if (!Display.isActive()) {
            this.clearControlPressedRecord();
            this.clearKeyPressedRecord();
            this.clearMousePressedRecord();
        }
        for (i2 = 0; i2 < this.keyListenersToAdd.size(); ++i2) {
            this.addKeyListenerImpl((KeyListener)this.keyListenersToAdd.get(i2));
        }
        this.keyListenersToAdd.clear();
        for (i2 = 0; i2 < this.mouseListenersToAdd.size(); ++i2) {
            this.addMouseListenerImpl((MouseListener)this.mouseListenersToAdd.get(i2));
        }
        this.mouseListenersToAdd.clear();
        if (this.doubleClickTimeout != 0L && System.currentTimeMillis() > this.doubleClickTimeout) {
            this.doubleClickTimeout = 0L;
        }
        this.height = height;
        for (ControlledInputReciever listener : this.allListeners) {
            listener.inputStarted();
        }
        block5: while (Keyboard.next()) {
            KeyListener listener;
            int i3;
            int eventKey;
            if (Keyboard.getEventKeyState()) {
                eventKey = this.resolveEventKey(Keyboard.getEventKey(), Keyboard.getEventCharacter());
                this.keys[eventKey] = Keyboard.getEventCharacter();
                this.pressed[eventKey] = true;
                this.nextRepeat[eventKey] = System.currentTimeMillis() + (long)this.keyRepeatInitial;
                this.consumed = false;
                for (i3 = 0; i3 < this.keyListeners.size(); ++i3) {
                    listener = (KeyListener)this.keyListeners.get(i3);
                    if (!listener.isAcceptingInput()) continue;
                    listener.keyPressed(eventKey, Keyboard.getEventCharacter());
                    if (this.consumed) continue block5;
                }
                continue;
            }
            eventKey = this.resolveEventKey(Keyboard.getEventKey(), Keyboard.getEventCharacter());
            this.nextRepeat[eventKey] = 0L;
            this.consumed = false;
            for (i3 = 0; i3 < this.keyListeners.size(); ++i3) {
                listener = (KeyListener)this.keyListeners.get(i3);
                if (!listener.isAcceptingInput()) continue;
                listener.keyReleased(eventKey, this.keys[eventKey]);
                if (this.consumed) continue block5;
            }
        }
        block8: while (Mouse.next()) {
            int i4;
            if (Mouse.getEventButton() >= 0) {
                if (Mouse.getEventButtonState()) {
                    this.consumed = false;
                    this.mousePressed[Mouse.getEventButton()] = true;
                    this.pressedX = (int)(this.xoffset + (float)Mouse.getEventX() * this.scaleX);
                    this.pressedY = (int)(this.yoffset + (float)(height - Mouse.getEventY()) * this.scaleY);
                    for (i4 = 0; i4 < this.mouseListeners.size(); ++i4) {
                        MouseListener listener = (MouseListener)this.mouseListeners.get(i4);
                        if (!listener.isAcceptingInput()) continue;
                        listener.mousePressed(Mouse.getEventButton(), this.pressedX, this.pressedY);
                        if (this.consumed) continue block8;
                    }
                    continue;
                }
                this.consumed = false;
                this.mousePressed[Mouse.getEventButton()] = false;
                int releasedX = (int)(this.xoffset + (float)Mouse.getEventX() * this.scaleX);
                int releasedY = (int)(this.yoffset + (float)(height - Mouse.getEventY()) * this.scaleY);
                if (this.pressedX != -1 && this.pressedY != -1 && Math.abs(this.pressedX - releasedX) < this.mouseClickTolerance && Math.abs(this.pressedY - releasedY) < this.mouseClickTolerance) {
                    this.considerDoubleClick(Mouse.getEventButton(), releasedX, releasedY);
                    this.pressedY = -1;
                    this.pressedX = -1;
                }
                for (int i5 = 0; i5 < this.mouseListeners.size(); ++i5) {
                    MouseListener listener = (MouseListener)this.mouseListeners.get(i5);
                    if (!listener.isAcceptingInput()) continue;
                    listener.mouseReleased(Mouse.getEventButton(), releasedX, releasedY);
                    if (this.consumed) continue block8;
                }
                continue;
            }
            if (Mouse.isGrabbed() && this.displayActive && (Mouse.getEventDX() != 0 || Mouse.getEventDY() != 0)) {
                this.consumed = false;
                for (i4 = 0; i4 < this.mouseListeners.size(); ++i4) {
                    MouseListener listener = (MouseListener)this.mouseListeners.get(i4);
                    if (!listener.isAcceptingInput()) continue;
                    if (this.anyMouseDown()) {
                        listener.mouseDragged(0, 0, Mouse.getEventDX(), -Mouse.getEventDY());
                    } else {
                        listener.mouseMoved(0, 0, Mouse.getEventDX(), -Mouse.getEventDY());
                    }
                    if (this.consumed) break;
                }
            }
            int dwheel = Mouse.getEventDWheel();
            this.wheel += dwheel;
            if (dwheel == 0) continue;
            this.consumed = false;
            for (int i6 = 0; i6 < this.mouseListeners.size(); ++i6) {
                MouseListener listener = (MouseListener)this.mouseListeners.get(i6);
                if (!listener.isAcceptingInput()) continue;
                listener.mouseWheelMoved(dwheel);
                if (this.consumed) continue block8;
            }
        }
        if (!this.displayActive || Mouse.isGrabbed()) {
            this.lastMouseX = this.getMouseX();
            this.lastMouseY = this.getMouseY();
        } else if (this.lastMouseX != this.getMouseX() || this.lastMouseY != this.getMouseY()) {
            this.consumed = false;
            for (int i7 = 0; i7 < this.mouseListeners.size(); ++i7) {
                MouseListener listener = (MouseListener)this.mouseListeners.get(i7);
                if (!listener.isAcceptingInput()) continue;
                if (this.anyMouseDown()) {
                    listener.mouseDragged(this.lastMouseX, this.lastMouseY, this.getMouseX(), this.getMouseY());
                } else {
                    listener.mouseMoved(this.lastMouseX, this.lastMouseY, this.getMouseX(), this.getMouseY());
                }
                if (this.consumed) break;
            }
            this.lastMouseX = this.getMouseX();
            this.lastMouseY = this.getMouseY();
        }
        if (controllersInited) {
            for (int i8 = 0; i8 < this.getControllerCount(); ++i8) {
                int count = ((Controller)controllers.get(i8)).getButtonCount() + 3;
                count = Math.min(count, 24);
                for (int c2 = 0; c2 <= count; ++c2) {
                    if (this.controls[i8][c2] && !this.isControlDwn(c2, i8)) {
                        this.controls[i8][c2] = false;
                        this.fireControlRelease(c2, i8);
                        continue;
                    }
                    if (this.controls[i8][c2] || !this.isControlDwn(c2, i8)) continue;
                    this.controllerPressed[i8][c2] = true;
                    this.controls[i8][c2] = true;
                    this.fireControlPress(c2, i8);
                }
            }
        }
        if (this.keyRepeat) {
            block16: for (int i9 = 0; i9 < 1024; ++i9) {
                if (!this.pressed[i9] || this.nextRepeat[i9] == 0L || System.currentTimeMillis() <= this.nextRepeat[i9]) continue;
                this.nextRepeat[i9] = System.currentTimeMillis() + (long)this.keyRepeatInterval;
                this.consumed = false;
                for (int j2 = 0; j2 < this.keyListeners.size(); ++j2) {
                    KeyListener listener = (KeyListener)this.keyListeners.get(j2);
                    if (!listener.isAcceptingInput()) continue;
                    listener.keyPressed(i9, this.keys[i9]);
                    if (this.consumed) continue block16;
                }
            }
        }
        for (ControlledInputReciever listener : this.allListeners) {
            listener.inputEnded();
        }
        if (Display.isCreated()) {
            this.displayActive = Display.isActive();
        }
    }

    public void enableKeyRepeat(int initial, int interval) {
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

    private void fireControlPress(int index, int controllerIndex) {
        this.consumed = false;
        for (int i2 = 0; i2 < this.controllerListeners.size(); ++i2) {
            ControllerListener listener = (ControllerListener)this.controllerListeners.get(i2);
            if (!listener.isAcceptingInput()) continue;
            switch (index) {
                case 0: {
                    listener.controllerLeftPressed(controllerIndex);
                    break;
                }
                case 1: {
                    listener.controllerRightPressed(controllerIndex);
                    break;
                }
                case 2: {
                    listener.controllerUpPressed(controllerIndex);
                    break;
                }
                case 3: {
                    listener.controllerDownPressed(controllerIndex);
                    break;
                }
                default: {
                    listener.controllerButtonPressed(controllerIndex, index - 4 + 1);
                }
            }
            if (this.consumed) break;
        }
    }

    private void fireControlRelease(int index, int controllerIndex) {
        this.consumed = false;
        for (int i2 = 0; i2 < this.controllerListeners.size(); ++i2) {
            ControllerListener listener = (ControllerListener)this.controllerListeners.get(i2);
            if (!listener.isAcceptingInput()) continue;
            switch (index) {
                case 0: {
                    listener.controllerLeftReleased(controllerIndex);
                    break;
                }
                case 1: {
                    listener.controllerRightReleased(controllerIndex);
                    break;
                }
                case 2: {
                    listener.controllerUpReleased(controllerIndex);
                    break;
                }
                case 3: {
                    listener.controllerDownReleased(controllerIndex);
                    break;
                }
                default: {
                    listener.controllerButtonReleased(controllerIndex, index - 4 + 1);
                }
            }
            if (this.consumed) break;
        }
    }

    private boolean isControlDwn(int index, int controllerIndex) {
        switch (index) {
            case 0: {
                return this.isControllerLeft(controllerIndex);
            }
            case 1: {
                return this.isControllerRight(controllerIndex);
            }
            case 2: {
                return this.isControllerUp(controllerIndex);
            }
            case 3: {
                return this.isControllerDown(controllerIndex);
            }
        }
        if (index >= 4) {
            return this.isButtonPressed(index - 4, controllerIndex);
        }
        throw new RuntimeException("Unknown control index");
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

    private void fireMouseClicked(int button, int x2, int y2, int clickCount) {
        this.consumed = false;
        for (int i2 = 0; i2 < this.mouseListeners.size(); ++i2) {
            MouseListener listener = (MouseListener)this.mouseListeners.get(i2);
            if (!listener.isAcceptingInput()) continue;
            listener.mouseClicked(button, x2, y2, clickCount);
            if (this.consumed) break;
        }
    }

    private class NullOutputStream
    extends OutputStream {
        private NullOutputStream() {
        }

        public void write(int b2) throws IOException {
        }
    }
}

