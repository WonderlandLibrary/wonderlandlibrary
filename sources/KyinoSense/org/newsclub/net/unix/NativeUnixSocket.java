/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import org.newsclub.net.unix.AFUNIXServerSocket;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketException;
import org.newsclub.net.unix.AFUNIXSocketImpl;

final class NativeUnixSocket {
    private static boolean loaded = false;

    NativeUnixSocket() {
    }

    static boolean isLoaded() {
        return loaded;
    }

    static void checkSupported() {
    }

    static native void bind(String var0, FileDescriptor var1, int var2) throws IOException;

    static native void listen(FileDescriptor var0, int var1) throws IOException;

    static native void accept(String var0, FileDescriptor var1, FileDescriptor var2) throws IOException;

    static native void connect(String var0, FileDescriptor var1) throws IOException;

    static native int read(FileDescriptor var0, byte[] var1, int var2, int var3) throws IOException;

    static native int write(FileDescriptor var0, byte[] var1, int var2, int var3) throws IOException;

    static native void close(FileDescriptor var0) throws IOException;

    static native void shutdown(FileDescriptor var0, int var1) throws IOException;

    static native int getSocketOptionInt(FileDescriptor var0, int var1) throws IOException;

    static native void setSocketOptionInt(FileDescriptor var0, int var1, int var2) throws IOException;

    static native void unlink(String var0) throws IOException;

    static native int available(FileDescriptor var0) throws IOException;

    static native void initServerImpl(AFUNIXServerSocket var0, AFUNIXSocketImpl var1);

    static native void setCreated(AFUNIXSocket var0);

    static native void setConnected(AFUNIXSocket var0);

    static native void setBound(AFUNIXSocket var0);

    static native void setCreatedServer(AFUNIXServerSocket var0);

    static native void setBoundServer(AFUNIXServerSocket var0);

    static native void setPort(AFUNIXSocketAddress var0, int var1);

    static void setPort1(AFUNIXSocketAddress addr, int port) throws AFUNIXSocketException {
        if (port < 0) {
            throw new IllegalArgumentException("port out of range:" + port);
        }
        boolean setOk = false;
        try {
            Field holderField = InetSocketAddress.class.getDeclaredField("holder");
            if (holderField != null) {
                Field portField;
                holderField.setAccessible(true);
                Object holder = holderField.get(addr);
                if (holder != null && (portField = holder.getClass().getDeclaredField("port")) != null) {
                    portField.setAccessible(true);
                    portField.set(holder, port);
                    setOk = true;
                }
            } else {
                NativeUnixSocket.setPort(addr, port);
            }
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e) {
            if (e instanceof AFUNIXSocketException) {
                throw (AFUNIXSocketException)e;
            }
            throw new AFUNIXSocketException("Could not set port", e);
        }
        if (!setOk) {
            throw new AFUNIXSocketException("Could not set port");
        }
    }

    static {
        try {
            Class.forName("org.newsclub.net.unix.NarSystem").getMethod("loadLibrary", new Class[0]).invoke(null, new Object[0]);
        }
        catch (ClassNotFoundException e) {
            throw new IllegalStateException("Could not find NarSystem class.\n\n*** ECLIPSE USERS ***\nIf you're running from within Eclipse, please try closing the \"junixsocket-native-common\" project\n", e);
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        loaded = true;
    }
}

