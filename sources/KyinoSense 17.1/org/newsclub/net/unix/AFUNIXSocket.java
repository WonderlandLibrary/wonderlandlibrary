/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketImpl;
import org.newsclub.net.unix.NativeUnixSocket;

public class AFUNIXSocket
extends Socket {
    protected AFUNIXSocketImpl impl;
    AFUNIXSocketAddress addr;

    private AFUNIXSocket(AFUNIXSocketImpl impl) throws IOException {
        super(impl);
        try {
            NativeUnixSocket.setCreated(this);
        }
        catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }

    public static AFUNIXSocket newInstance() throws IOException {
        AFUNIXSocketImpl.Lenient impl = new AFUNIXSocketImpl.Lenient();
        AFUNIXSocket instance = new AFUNIXSocket(impl);
        instance.impl = impl;
        return instance;
    }

    public static AFUNIXSocket newStrictInstance() throws IOException {
        AFUNIXSocketImpl impl = new AFUNIXSocketImpl();
        AFUNIXSocket instance = new AFUNIXSocket(impl);
        instance.impl = impl;
        return instance;
    }

    public static AFUNIXSocket connectTo(AFUNIXSocketAddress addr) throws IOException {
        AFUNIXSocket socket = AFUNIXSocket.newInstance();
        socket.connect(addr);
        return socket;
    }

    @Override
    public void bind(SocketAddress bindpoint) throws IOException {
        super.bind(bindpoint);
        this.addr = (AFUNIXSocketAddress)bindpoint;
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        this.connect(endpoint, 0);
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        if (!(endpoint instanceof AFUNIXSocketAddress)) {
            throw new IOException("Can only connect to endpoints of type " + AFUNIXSocketAddress.class.getName());
        }
        this.impl.connect(endpoint, timeout);
        this.addr = (AFUNIXSocketAddress)endpoint;
        NativeUnixSocket.setConnected(this);
    }

    @Override
    public String toString() {
        if (this.isConnected()) {
            return "AFUNIXSocket[fd=" + this.impl.getFD() + ";path=" + this.addr.getSocketFile() + "]";
        }
        return "AFUNIXSocket[unconnected]";
    }

    public static boolean isSupported() {
        return NativeUnixSocket.isLoaded();
    }
}

