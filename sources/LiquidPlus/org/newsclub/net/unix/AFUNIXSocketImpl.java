/*
 * Decompiled with CFR 0.152.
 */
package org.newsclub.net.unix;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;
import org.newsclub.net.unix.AFUNIXSocketAddress;
import org.newsclub.net.unix.AFUNIXSocketException;
import org.newsclub.net.unix.NativeUnixSocket;

class AFUNIXSocketImpl
extends SocketImpl {
    private static final int SHUT_RD = 0;
    private static final int SHUT_WR = 1;
    private static final int SHUT_RD_WR = 2;
    private String socketFile;
    private boolean closed = false;
    private boolean bound = false;
    private boolean connected = false;
    private boolean closedInputStream = false;
    private boolean closedOutputStream = false;
    private final AFUNIXInputStream in = new AFUNIXInputStream();
    private final AFUNIXOutputStream out = new AFUNIXOutputStream();

    AFUNIXSocketImpl() {
        this.fd = new FileDescriptor();
    }

    FileDescriptor getFD() {
        return this.fd;
    }

    @Override
    protected void accept(SocketImpl socket) throws IOException {
        AFUNIXSocketImpl si = (AFUNIXSocketImpl)socket;
        NativeUnixSocket.accept(this.socketFile, this.fd, si.fd);
        si.socketFile = this.socketFile;
        si.connected = true;
    }

    @Override
    protected int available() throws IOException {
        return NativeUnixSocket.available(this.fd);
    }

    protected void bind(SocketAddress addr) throws IOException {
        this.bind(0, addr);
    }

    protected void bind(int backlog, SocketAddress addr) throws IOException {
        if (!(addr instanceof AFUNIXSocketAddress)) {
            throw new SocketException("Cannot bind to this type of address: " + addr.getClass());
        }
        AFUNIXSocketAddress socketAddress = (AFUNIXSocketAddress)addr;
        this.socketFile = socketAddress.getSocketFile();
        NativeUnixSocket.bind(this.socketFile, this.fd, backlog);
        this.bound = true;
        this.localport = socketAddress.getPort();
    }

    @Override
    protected void bind(InetAddress host, int port) throws IOException {
        throw new SocketException("Cannot bind to this type of address: " + InetAddress.class);
    }

    private void checkClose() throws IOException {
        if (!this.closedInputStream || this.closedOutputStream) {
            // empty if block
        }
    }

    @Override
    protected synchronized void close() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        if (this.fd.valid()) {
            NativeUnixSocket.shutdown(this.fd, 2);
            NativeUnixSocket.close(this.fd);
        }
        if (this.bound) {
            NativeUnixSocket.unlink(this.socketFile);
        }
        this.connected = false;
    }

    @Override
    protected void connect(String host, int port) throws IOException {
        throw new SocketException("Cannot bind to this type of address: " + InetAddress.class);
    }

    @Override
    protected void connect(InetAddress address, int port) throws IOException {
        throw new SocketException("Cannot bind to this type of address: " + InetAddress.class);
    }

    @Override
    protected void connect(SocketAddress addr, int timeout) throws IOException {
        if (!(addr instanceof AFUNIXSocketAddress)) {
            throw new SocketException("Cannot bind to this type of address: " + addr.getClass());
        }
        AFUNIXSocketAddress socketAddress = (AFUNIXSocketAddress)addr;
        this.socketFile = socketAddress.getSocketFile();
        NativeUnixSocket.connect(this.socketFile, this.fd);
        this.address = socketAddress.getAddress();
        this.port = socketAddress.getPort();
        this.localport = 0;
        this.connected = true;
    }

    @Override
    protected void create(boolean stream) throws IOException {
    }

    @Override
    protected InputStream getInputStream() throws IOException {
        if (!this.connected && !this.bound) {
            throw new IOException("Not connected/not bound");
        }
        return this.in;
    }

    @Override
    protected OutputStream getOutputStream() throws IOException {
        if (!this.connected && !this.bound) {
            throw new IOException("Not connected/not bound");
        }
        return this.out;
    }

    @Override
    protected void listen(int backlog) throws IOException {
        NativeUnixSocket.listen(this.fd, backlog);
    }

    @Override
    protected void sendUrgentData(int data) throws IOException {
        NativeUnixSocket.write(this.fd, new byte[]{(byte)(data & 0xFF)}, 0, 1);
    }

    @Override
    public String toString() {
        return super.toString() + "[fd=" + this.fd + "; file=" + this.socketFile + "; connected=" + this.connected + "; bound=" + this.bound + "]";
    }

    private static int expectInteger(Object value) throws SocketException {
        try {
            return (Integer)value;
        }
        catch (ClassCastException e) {
            throw new AFUNIXSocketException("Unsupported value: " + value, e);
        }
        catch (NullPointerException e) {
            throw new AFUNIXSocketException("Value must not be null", e);
        }
    }

    private static int expectBoolean(Object value) throws SocketException {
        try {
            return (Boolean)value != false ? 1 : 0;
        }
        catch (ClassCastException e) {
            throw new AFUNIXSocketException("Unsupported value: " + value, e);
        }
        catch (NullPointerException e) {
            throw new AFUNIXSocketException("Value must not be null", e);
        }
    }

    @Override
    public Object getOption(int optID) throws SocketException {
        try {
            switch (optID) {
                case 1: 
                case 8: {
                    return NativeUnixSocket.getSocketOptionInt(this.fd, optID) != 0;
                }
                case 128: 
                case 4097: 
                case 4098: 
                case 4102: {
                    return NativeUnixSocket.getSocketOptionInt(this.fd, optID);
                }
            }
            throw new AFUNIXSocketException("Unsupported option: " + optID);
        }
        catch (AFUNIXSocketException e) {
            throw e;
        }
        catch (Exception e) {
            throw new AFUNIXSocketException("Error while getting option", e);
        }
    }

    @Override
    public void setOption(int optID, Object value) throws SocketException {
        try {
            switch (optID) {
                case 128: {
                    if (value instanceof Boolean) {
                        boolean b = (Boolean)value;
                        if (b) {
                            throw new SocketException("Only accepting Boolean.FALSE here");
                        }
                        NativeUnixSocket.setSocketOptionInt(this.fd, optID, -1);
                        return;
                    }
                    NativeUnixSocket.setSocketOptionInt(this.fd, optID, AFUNIXSocketImpl.expectInteger(value));
                    return;
                }
                case 4097: 
                case 4098: 
                case 4102: {
                    NativeUnixSocket.setSocketOptionInt(this.fd, optID, AFUNIXSocketImpl.expectInteger(value));
                    return;
                }
                case 1: 
                case 8: {
                    NativeUnixSocket.setSocketOptionInt(this.fd, optID, AFUNIXSocketImpl.expectBoolean(value));
                    return;
                }
            }
            throw new AFUNIXSocketException("Unsupported option: " + optID);
        }
        catch (AFUNIXSocketException e) {
            throw e;
        }
        catch (Exception e) {
            throw new AFUNIXSocketException("Error while setting option", e);
        }
    }

    @Override
    protected void shutdownInput() throws IOException {
        if (!this.closed && this.fd.valid()) {
            NativeUnixSocket.shutdown(this.fd, 0);
        }
    }

    @Override
    protected void shutdownOutput() throws IOException {
        if (!this.closed && this.fd.valid()) {
            NativeUnixSocket.shutdown(this.fd, 1);
        }
    }

    static class Lenient
    extends AFUNIXSocketImpl {
        Lenient() {
        }

        @Override
        public void setOption(int optID, Object value) throws SocketException {
            try {
                super.setOption(optID, value);
            }
            catch (SocketException e) {
                switch (optID) {
                    case 1: {
                        return;
                    }
                }
                throw e;
            }
        }

        @Override
        public Object getOption(int optID) throws SocketException {
            try {
                return super.getOption(optID);
            }
            catch (SocketException e) {
                switch (optID) {
                    case 1: 
                    case 8: {
                        return false;
                    }
                }
                throw e;
            }
        }
    }

    private final class AFUNIXOutputStream
    extends OutputStream {
        private boolean streamClosed = false;

        private AFUNIXOutputStream() {
        }

        @Override
        public void write(int oneByte) throws IOException {
            byte[] buf1 = new byte[]{(byte)oneByte};
            this.write(buf1, 0, 1);
        }

        @Override
        public void write(byte[] buf, int off, int len) throws IOException {
            if (this.streamClosed) {
                throw new AFUNIXSocketException("This OutputStream has already been closed.");
            }
            if (len > buf.length - off) {
                throw new IndexOutOfBoundsException();
            }
            try {
                while (len > 0 && !Thread.interrupted()) {
                    int written = NativeUnixSocket.write(AFUNIXSocketImpl.this.fd, buf, off, len);
                    if (written == -1) {
                        throw new IOException("Unspecific error while writing");
                    }
                    len -= written;
                    off += written;
                }
            }
            catch (IOException e) {
                throw (IOException)new IOException(e.getMessage() + " at " + AFUNIXSocketImpl.this.toString()).initCause(e);
            }
        }

        @Override
        public void close() throws IOException {
            if (this.streamClosed) {
                return;
            }
            this.streamClosed = true;
            if (AFUNIXSocketImpl.this.fd.valid()) {
                NativeUnixSocket.shutdown(AFUNIXSocketImpl.this.fd, 1);
            }
            AFUNIXSocketImpl.this.closedOutputStream = true;
            AFUNIXSocketImpl.this.checkClose();
        }
    }

    private final class AFUNIXInputStream
    extends InputStream {
        private boolean streamClosed = false;

        private AFUNIXInputStream() {
        }

        @Override
        public int read(byte[] buf, int off, int len) throws IOException {
            if (this.streamClosed) {
                throw new IOException("This InputStream has already been closed.");
            }
            if (len == 0) {
                return 0;
            }
            int maxRead = buf.length - off;
            if (len > maxRead) {
                len = maxRead;
            }
            try {
                return NativeUnixSocket.read(AFUNIXSocketImpl.this.fd, buf, off, len);
            }
            catch (IOException e) {
                throw (IOException)new IOException(e.getMessage() + " at " + AFUNIXSocketImpl.this.toString()).initCause(e);
            }
        }

        @Override
        public int read() throws IOException {
            byte[] buf1 = new byte[1];
            int numRead = this.read(buf1, 0, 1);
            if (numRead <= 0) {
                return -1;
            }
            return buf1[0] & 0xFF;
        }

        @Override
        public void close() throws IOException {
            if (this.streamClosed) {
                return;
            }
            this.streamClosed = true;
            if (AFUNIXSocketImpl.this.fd.valid()) {
                NativeUnixSocket.shutdown(AFUNIXSocketImpl.this.fd, 0);
            }
            AFUNIXSocketImpl.this.closedInputStream = true;
            AFUNIXSocketImpl.this.checkClose();
        }

        @Override
        public int available() throws IOException {
            int av = NativeUnixSocket.available(AFUNIXSocketImpl.this.fd);
            return av;
        }
    }
}

