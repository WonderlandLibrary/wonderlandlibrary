/*
 * Decompiled with CFR 0.150.
 */
package com.sun.jna.platform.mac;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import java.nio.IntBuffer;

public interface Carbon {
    public static final Carbon INSTANCE = Native.loadLibrary("Carbon", Carbon.class);
    public static final int cmdKey = 256;
    public static final int shiftKey = 512;
    public static final int optionKey = 2048;
    public static final int controlKey = 4096;

    public Pointer GetEventDispatcherTarget();

    public int InstallEventHandler(Pointer var1, EventHandlerProcPtr var2, int var3, EventTypeSpec[] var4, Pointer var5, PointerByReference var6);

    public int RegisterEventHotKey(int var1, int var2, EventHotKeyID.ByValue var3, Pointer var4, int var5, PointerByReference var6);

    public int GetEventParameter(Pointer var1, int var2, int var3, Pointer var4, int var5, IntBuffer var6, EventHotKeyID var7);

    public int RemoveEventHandler(Pointer var1);

    public int UnregisterEventHotKey(Pointer var1);

    public static interface EventHandlerProcPtr
    extends Callback {
        public int callback(Pointer var1, Pointer var2, Pointer var3);
    }

    public static class EventHotKeyID
    extends Structure {
        public int signature;
        public int id;

        public static class ByValue
        extends EventHotKeyID
        implements Structure.ByValue {
        }
    }

    public static class EventTypeSpec
    extends Structure {
        public int eventClass;
        public int eventKind;
    }
}

