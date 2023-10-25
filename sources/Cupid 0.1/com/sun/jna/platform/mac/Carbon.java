package com.sun.jna.platform.mac;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import java.nio.IntBuffer;

public interface Carbon {
  public static final Carbon INSTANCE = (Carbon)Native.loadLibrary("Carbon", Carbon.class);
  
  public static final int cmdKey = 256;
  
  public static final int shiftKey = 512;
  
  public static final int optionKey = 2048;
  
  public static final int controlKey = 4096;
  
  Pointer GetEventDispatcherTarget();
  
  int InstallEventHandler(Pointer paramPointer1, EventHandlerProcPtr paramEventHandlerProcPtr, int paramInt, EventTypeSpec[] paramArrayOfEventTypeSpec, Pointer paramPointer2, PointerByReference paramPointerByReference);
  
  int RegisterEventHotKey(int paramInt1, int paramInt2, EventHotKeyID.ByValue paramByValue, Pointer paramPointer, int paramInt3, PointerByReference paramPointerByReference);
  
  int GetEventParameter(Pointer paramPointer1, int paramInt1, int paramInt2, Pointer paramPointer2, int paramInt3, IntBuffer paramIntBuffer, EventHotKeyID paramEventHotKeyID);
  
  int RemoveEventHandler(Pointer paramPointer);
  
  int UnregisterEventHotKey(Pointer paramPointer);
  
  public static class EventTypeSpec extends Structure {
    public int eventClass;
    
    public int eventKind;
  }
  
  public static class EventHotKeyID extends Structure {
    public int signature;
    
    public int id;
    
    public static class ByValue extends EventHotKeyID implements Structure.ByValue {}
  }
  
  public static class ByValue extends EventHotKeyID implements Structure.ByValue {}
  
  public static interface EventHandlerProcPtr extends Callback {
    int callback(Pointer param1Pointer1, Pointer param1Pointer2, Pointer param1Pointer3);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\mac\Carbon.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */