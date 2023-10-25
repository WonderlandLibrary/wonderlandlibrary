/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.server;

import java.io.IOException;
import java.io.InputStream;
import org.apache.logging.log4j.core.net.server.LogEventBridge;
import org.apache.logging.log4j.core.net.server.TcpSocketServer;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;

public class SecureTcpSocketServer<T extends InputStream>
extends TcpSocketServer<T> {
    public SecureTcpSocketServer(int port, LogEventBridge<T> logEventInput, SslConfiguration sslConfig) throws IOException {
        super(port, logEventInput, sslConfig.getSslServerSocketFactory().createServerSocket(port));
    }
}

