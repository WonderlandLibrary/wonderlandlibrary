/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.HttpConnection;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpMessage;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.HttpConnectionMetricsImpl;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ChunkedOutputStream;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.ContentLengthOutputStream;
import org.apache.http.impl.io.EmptyInputStream;
import org.apache.http.impl.io.HttpTransportMetricsImpl;
import org.apache.http.impl.io.IdentityInputStream;
import org.apache.http.impl.io.IdentityOutputStream;
import org.apache.http.impl.io.SessionInputBufferImpl;
import org.apache.http.impl.io.SessionOutputBufferImpl;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.NetUtils;

public class BHttpConnectionBase
implements HttpConnection,
HttpInetConnection {
    private final SessionInputBufferImpl inbuffer;
    private final SessionOutputBufferImpl outbuffer;
    private final MessageConstraints messageConstraints;
    private final HttpConnectionMetricsImpl connMetrics;
    private final ContentLengthStrategy incomingContentStrategy;
    private final ContentLengthStrategy outgoingContentStrategy;
    private final AtomicReference<Socket> socketHolder;

    protected BHttpConnectionBase(int buffersize, int fragmentSizeHint, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints messageConstraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy) {
        Args.positive(buffersize, "Buffer size");
        HttpTransportMetricsImpl inTransportMetrics = new HttpTransportMetricsImpl();
        HttpTransportMetricsImpl outTransportMetrics = new HttpTransportMetricsImpl();
        this.inbuffer = new SessionInputBufferImpl(inTransportMetrics, buffersize, -1, messageConstraints != null ? messageConstraints : MessageConstraints.DEFAULT, chardecoder);
        this.outbuffer = new SessionOutputBufferImpl(outTransportMetrics, buffersize, fragmentSizeHint, charencoder);
        this.messageConstraints = messageConstraints;
        this.connMetrics = new HttpConnectionMetricsImpl(inTransportMetrics, outTransportMetrics);
        this.incomingContentStrategy = incomingContentStrategy != null ? incomingContentStrategy : LaxContentLengthStrategy.INSTANCE;
        this.outgoingContentStrategy = outgoingContentStrategy != null ? outgoingContentStrategy : StrictContentLengthStrategy.INSTANCE;
        this.socketHolder = new AtomicReference();
    }

    protected void ensureOpen() throws IOException {
        Socket socket = this.socketHolder.get();
        if (socket == null) {
            throw new ConnectionClosedException("Connection is closed");
        }
        if (!this.inbuffer.isBound()) {
            this.inbuffer.bind(this.getSocketInputStream(socket));
        }
        if (!this.outbuffer.isBound()) {
            this.outbuffer.bind(this.getSocketOutputStream(socket));
        }
    }

    protected InputStream getSocketInputStream(Socket socket) throws IOException {
        return socket.getInputStream();
    }

    protected OutputStream getSocketOutputStream(Socket socket) throws IOException {
        return socket.getOutputStream();
    }

    protected void bind(Socket socket) throws IOException {
        Args.notNull(socket, "Socket");
        this.socketHolder.set(socket);
        this.inbuffer.bind(null);
        this.outbuffer.bind(null);
    }

    protected SessionInputBuffer getSessionInputBuffer() {
        return this.inbuffer;
    }

    protected SessionOutputBuffer getSessionOutputBuffer() {
        return this.outbuffer;
    }

    protected void doFlush() throws IOException {
        this.outbuffer.flush();
    }

    @Override
    public boolean isOpen() {
        return this.socketHolder.get() != null;
    }

    protected Socket getSocket() {
        return this.socketHolder.get();
    }

    protected OutputStream createOutputStream(long len, SessionOutputBuffer outbuffer) {
        if (len == -2L) {
            return new ChunkedOutputStream(2048, outbuffer);
        }
        if (len == -1L) {
            return new IdentityOutputStream(outbuffer);
        }
        return new ContentLengthOutputStream(outbuffer, len);
    }

    protected OutputStream prepareOutput(HttpMessage message) throws HttpException {
        long len = this.outgoingContentStrategy.determineLength(message);
        return this.createOutputStream(len, this.outbuffer);
    }

    protected InputStream createInputStream(long len, SessionInputBuffer inbuffer) {
        if (len == -2L) {
            return new ChunkedInputStream(inbuffer, this.messageConstraints);
        }
        if (len == -1L) {
            return new IdentityInputStream(inbuffer);
        }
        if (len == 0L) {
            return EmptyInputStream.INSTANCE;
        }
        return new ContentLengthInputStream(inbuffer, len);
    }

    protected HttpEntity prepareInput(HttpMessage message) throws HttpException {
        Header contentEncodingHeader;
        BasicHttpEntity entity = new BasicHttpEntity();
        long len = this.incomingContentStrategy.determineLength(message);
        InputStream instream = this.createInputStream(len, this.inbuffer);
        if (len == -2L) {
            entity.setChunked(true);
            entity.setContentLength(-1L);
            entity.setContent(instream);
        } else if (len == -1L) {
            entity.setChunked(false);
            entity.setContentLength(-1L);
            entity.setContent(instream);
        } else {
            entity.setChunked(false);
            entity.setContentLength(len);
            entity.setContent(instream);
        }
        Header contentTypeHeader = message.getFirstHeader("Content-Type");
        if (contentTypeHeader != null) {
            entity.setContentType(contentTypeHeader);
        }
        if ((contentEncodingHeader = message.getFirstHeader("Content-Encoding")) != null) {
            entity.setContentEncoding(contentEncodingHeader);
        }
        return entity;
    }

    @Override
    public InetAddress getLocalAddress() {
        Socket socket = this.socketHolder.get();
        return socket != null ? socket.getLocalAddress() : null;
    }

    @Override
    public int getLocalPort() {
        Socket socket = this.socketHolder.get();
        return socket != null ? socket.getLocalPort() : -1;
    }

    @Override
    public InetAddress getRemoteAddress() {
        Socket socket = this.socketHolder.get();
        return socket != null ? socket.getInetAddress() : null;
    }

    @Override
    public int getRemotePort() {
        Socket socket = this.socketHolder.get();
        return socket != null ? socket.getPort() : -1;
    }

    @Override
    public void setSocketTimeout(int timeout) {
        Socket socket = this.socketHolder.get();
        if (socket != null) {
            try {
                socket.setSoTimeout(timeout);
            }
            catch (SocketException socketException) {
                // empty catch block
            }
        }
    }

    @Override
    public int getSocketTimeout() {
        Socket socket = this.socketHolder.get();
        if (socket != null) {
            try {
                return socket.getSoTimeout();
            }
            catch (SocketException ignore) {
                return -1;
            }
        }
        return -1;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void shutdown() throws IOException {
        Socket socket = this.socketHolder.getAndSet(null);
        if (socket != null) {
            try {
                socket.setSoLinger(true, 0);
            }
            catch (IOException iOException) {
            }
            finally {
                socket.close();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void close() throws IOException {
        Socket socket = this.socketHolder.getAndSet(null);
        if (socket != null) {
            try {
                this.inbuffer.clear();
                this.outbuffer.flush();
                try {
                    try {
                        socket.shutdownOutput();
                    }
                    catch (IOException ignore) {
                        // empty catch block
                    }
                    try {
                        socket.shutdownInput();
                    }
                    catch (IOException ignore) {
                    }
                }
                catch (UnsupportedOperationException unsupportedOperationException) {
                    // empty catch block
                }
            }
            finally {
                socket.close();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private int fillInputBuffer(int timeout) throws IOException {
        Socket socket = this.socketHolder.get();
        int oldtimeout = socket.getSoTimeout();
        try {
            socket.setSoTimeout(timeout);
            int n2 = this.inbuffer.fillBuffer();
            return n2;
        }
        finally {
            socket.setSoTimeout(oldtimeout);
        }
    }

    protected boolean awaitInput(int timeout) throws IOException {
        if (this.inbuffer.hasBufferedData()) {
            return true;
        }
        this.fillInputBuffer(timeout);
        return this.inbuffer.hasBufferedData();
    }

    @Override
    public boolean isStale() {
        if (!this.isOpen()) {
            return true;
        }
        try {
            int bytesRead = this.fillInputBuffer(1);
            return bytesRead < 0;
        }
        catch (SocketTimeoutException ex) {
            return false;
        }
        catch (IOException ex) {
            return true;
        }
    }

    protected void incrementRequestCount() {
        this.connMetrics.incrementRequestCount();
    }

    protected void incrementResponseCount() {
        this.connMetrics.incrementResponseCount();
    }

    @Override
    public HttpConnectionMetrics getMetrics() {
        return this.connMetrics;
    }

    public String toString() {
        Socket socket = this.socketHolder.get();
        if (socket != null) {
            StringBuilder buffer = new StringBuilder();
            SocketAddress remoteAddress = socket.getRemoteSocketAddress();
            SocketAddress localAddress = socket.getLocalSocketAddress();
            if (remoteAddress != null && localAddress != null) {
                NetUtils.formatAddress(buffer, localAddress);
                buffer.append("<->");
                NetUtils.formatAddress(buffer, remoteAddress);
            }
            return buffer.toString();
        }
        return "[Not bound]";
    }
}

