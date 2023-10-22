package com.sun.webkit.dom;

import org.w3c.dom.views.AbstractView;

public class KeyboardEventImpl extends UIEventImpl {
   public static final int KEY_LOCATION_STANDARD = 0;
   public static final int KEY_LOCATION_LEFT = 1;
   public static final int KEY_LOCATION_RIGHT = 2;
   public static final int KEY_LOCATION_NUMPAD = 3;

   KeyboardEventImpl(long var1) {
      super(var1);
   }

   static KeyboardEventImpl getImpl(long var0) {
      return (KeyboardEventImpl)create(var0);
   }

   public String getKeyIdentifier() {
      return getKeyIdentifierImpl(this.getPeer());
   }

   static native String getKeyIdentifierImpl(long var0);

   public int getLocation() {
      return getLocationImpl(this.getPeer());
   }

   static native int getLocationImpl(long var0);

   public int getKeyLocation() {
      return getKeyLocationImpl(this.getPeer());
   }

   static native int getKeyLocationImpl(long var0);

   public boolean getCtrlKey() {
      return getCtrlKeyImpl(this.getPeer());
   }

   static native boolean getCtrlKeyImpl(long var0);

   public boolean getShiftKey() {
      return getShiftKeyImpl(this.getPeer());
   }

   static native boolean getShiftKeyImpl(long var0);

   public boolean getAltKey() {
      return getAltKeyImpl(this.getPeer());
   }

   static native boolean getAltKeyImpl(long var0);

   public boolean getMetaKey() {
      return getMetaKeyImpl(this.getPeer());
   }

   static native boolean getMetaKeyImpl(long var0);

   public boolean getAltGraphKey() {
      return getAltGraphKeyImpl(this.getPeer());
   }

   static native boolean getAltGraphKeyImpl(long var0);

   public int getKeyCode() {
      return getKeyCodeImpl(this.getPeer());
   }

   static native int getKeyCodeImpl(long var0);

   public int getCharCode() {
      return getCharCodeImpl(this.getPeer());
   }

   static native int getCharCodeImpl(long var0);

   public boolean getModifierState(String var1) {
      return getModifierStateImpl(this.getPeer(), var1);
   }

   static native boolean getModifierStateImpl(long var0, String var2);

   public void initKeyboardEvent(String var1, boolean var2, boolean var3, AbstractView var4, String var5, int var6, boolean var7, boolean var8, boolean var9, boolean var10, boolean var11) {
      initKeyboardEventImpl(this.getPeer(), var1, var2, var3, DOMWindowImpl.getPeer(var4), var5, var6, var7, var8, var9, var10, var11);
   }

   static native void initKeyboardEventImpl(long var0, String var2, boolean var3, boolean var4, long var5, String var7, int var8, boolean var9, boolean var10, boolean var11, boolean var12, boolean var13);

   public void initKeyboardEventEx(String var1, boolean var2, boolean var3, AbstractView var4, String var5, int var6, boolean var7, boolean var8, boolean var9, boolean var10) {
      initKeyboardEventExImpl(this.getPeer(), var1, var2, var3, DOMWindowImpl.getPeer(var4), var5, var6, var7, var8, var9, var10);
   }

   static native void initKeyboardEventExImpl(long var0, String var2, boolean var3, boolean var4, long var5, String var7, int var8, boolean var9, boolean var10, boolean var11, boolean var12);
}
