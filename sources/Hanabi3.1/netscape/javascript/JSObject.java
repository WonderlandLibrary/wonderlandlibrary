package netscape.javascript;

import java.applet.Applet;

public abstract class JSObject {
   protected JSObject() {
   }

   public abstract Object call(String var1, Object... var2) throws JSException;

   public abstract Object eval(String var1) throws JSException;

   public abstract Object getMember(String var1) throws JSException;

   public abstract void setMember(String var1, Object var2) throws JSException;

   public abstract void removeMember(String var1) throws JSException;

   public abstract Object getSlot(int var1) throws JSException;

   public abstract void setSlot(int var1, Object var2) throws JSException;

   public static JSObject getWindow(Applet var0) throws JSException {
      throw new JSException("Unexpected error: This method should not be used unless loaded from plugin.jar");
   }
}
