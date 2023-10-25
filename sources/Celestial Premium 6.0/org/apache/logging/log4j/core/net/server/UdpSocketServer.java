/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.server;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.net.server.AbstractSocketServer;
import org.apache.logging.log4j.core.net.server.JsonInputStreamLogEventBridge;
import org.apache.logging.log4j.core.net.server.LogEventBridge;
import org.apache.logging.log4j.core.net.server.ObjectInputStreamLogEventBridge;
import org.apache.logging.log4j.core.net.server.XmlInputStreamLogEventBridge;
import org.apache.logging.log4j.core.util.BasicCommandLineArguments;

public class UdpSocketServer<T extends InputStream>
extends AbstractSocketServer<T> {
    private final DatagramSocket datagramSocket;
    private final int maxBufferSize = 67584;

    public static UdpSocketServer<InputStream> createJsonSocketServer(int port) throws IOException {
        return new UdpSocketServer<InputStream>(port, new JsonInputStreamLogEventBridge());
    }

    public static UdpSocketServer<ObjectInputStream> createSerializedSocketServer(int port) throws IOException {
        return new UdpSocketServer<ObjectInputStream>(port, new ObjectInputStreamLogEventBridge());
    }

    public static UdpSocketServer<InputStream> createXmlSocketServer(int port) throws IOException {
        return new UdpSocketServer<InputStream>(port, new XmlInputStreamLogEventBridge());
    }

    public static void main(String[] args) throws Exception {
        AbstractSocketServer.CommandLineArguments cla = BasicCommandLineArguments.parseCommandLine(args, UdpSocketServer.class, new AbstractSocketServer.CommandLineArguments());
        if (cla.isHelp()) {
            return;
        }
        if (cla.getConfigLocation() != null) {
            ConfigurationFactory.setConfigurationFactory(new AbstractSocketServer.ServerConfigurationFactory(cla.getConfigLocation()));
        }
        UdpSocketServer<ObjectInputStream> socketServer = UdpSocketServer.createSerializedSocketServer(cla.getPort());
        Thread serverThread = socketServer.startNewThread();
        if (cla.isInteractive()) {
            socketServer.awaitTermination(serverThread);
        }
    }

    public UdpSocketServer(int port, LogEventBridge<T> logEventInput) throws IOException {
        super(port, logEventInput);
        this.datagramSocket = new DatagramSocket(port);
    }

    @Override
    public void run() {
        while (this.isActive()) {
            if (this.datagramSocket.isClosed()) {
                return;
            }
            try {
                byte[] buf = new byte[67584];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                this.datagramSocket.receive(packet);
                ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), packet.getOffset(), packet.getLength());
                this.logEventInput.logEvents(this.logEventInput.wrapStream(bais), this);
            }
            catch (OptionalDataException e) {
                if (this.datagramSocket.isClosed()) {
                    return;
                }
                this.logger.error("OptionalDataException eof=" + e.eof + " length=" + e.length, (Throwable)e);
            }
            catch (EOFException e) {
                if (this.datagramSocket.isClosed()) {
                    return;
                }
                this.logger.info("EOF encountered");
            }
            catch (IOException e) {
                if (this.datagramSocket.isClosed()) {
                    return;
                }
                this.logger.error("Exception encountered on accept. Ignoring. Stack Trace :", (Throwable)e);
            }
        }
    }

    @Override
    public void shutdown() {
        this.setActive(false);
        Thread.currentThread().interrupt();
        this.datagramSocket.close();
    }
}

