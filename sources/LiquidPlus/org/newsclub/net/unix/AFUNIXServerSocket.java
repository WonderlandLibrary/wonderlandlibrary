/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import org.newsclub.net.unix.AFUNIXSocket;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketImpl;
import org.newsclub.net.unix.NativeUnixSocket;

public class AFUNIXServerSocket
extends ServerSocket {
    private final AFUNIXSocketImpl implementation;
    private AFUNIXSocketAddress boundEndpoint = null;
    private final Thread shutdownThread = new Thread(){

        @Override
        public void run() {
            try {
                if (AFUNIXServerSocket.this.boundEndpoint != null) {
                    NativeUnixSocket.unlink(AFUNIXServerSocket.this.boundEndpoint.getSocketFile());
                }
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    };

    protected AFUNIXServerSocket() throws IOException {
        this.implementation = new AFUNIXSocketImpl();
        NativeUnixSocket.initServerImpl(this, this.implementation);
        Runtime.getRuntime().addShutdownHook(this.shutdownThread);
        NativeUnixSocket.setCreatedServer(this);
    }

    public static AFUNIXServerSocket newInstance() throws IOException {
        AFUNIXServerSocket instance = new AFUNIXServerSocket();
        return instance;
    }

    public static AFUNIXServerSocket bindOn(AFUNIXSocketAddress addr) throws IOException {
        AFUNIXServerSocket socket = AFUNIXServerSocket.newInstance();
        socket.bind(addr);
        return socket;
    }

    @Override
    public void bind(SocketAddress endpoint, int backlog) throws IOException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        if (this.isBound()) {
            throw new SocketException("Already bound");
        }
        if (!(endpoint instanceof AFUNIXSocketAddress)) {
            throw new IOException("Can only bind to endpoints of type " + AFUNIXSocketAddress.class.getName());
        }
        this.implementation.bind(backlog, endpoint);
        this.boundEndpoint = (AFUNIXSocketAddress)endpoint;
    }

    @Override
    public boolean isBound() {
        return this.boundEndpoint != null;
    }

    @Override
    public Socket accept() throws IOException {
        if (this.isClosed()) {
            throw new SocketException("Socket is closed");
        }
        AFUNIXSocket as = AFUNIXSocket.newInstance();
        this.implementation.accept(as.impl);
        as.addr = this.boundEndpoint;
        NativeUnixSocket.setConnected(as);
        return as;
    }

    @Override
    public String toString() {
        if (!this.isBound()) {
            return "AFUNIXServerSocket[unbound]";
        }
        return "AFUNIXServerSocket[" + this.boundEndpoint.getSocketFile() + "]";
    }

    @Override
    public void close() throws IOException {
        if (this.isClosed()) {
            return;
        }
        super.close();
        this.implementation.close();
        if (this.boundEndpoint != null) {
            NativeUnixSocket.unlink(this.boundEndpoint.getSocketFile());
        }
        try {
            Runtime.getRuntime().removeShutdownHook(this.shutdownThread);
        }
        catch (IllegalStateException illegalStateException) {
            // empty catch block
        }
    }

    public static boolean isSupported() {
        return NativeUnixSocket.isLoaded();
    }
}

