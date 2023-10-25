/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net;

import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.helpers.Strings;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.net.DatagramOutputStream;

public class DatagramSocketManager
extends AbstractSocketManager {
    private static final DatagramSocketManagerFactory FACTORY = new DatagramSocketManagerFactory();

    protected DatagramSocketManager(String name, OutputStream os, InetAddress address, String host, int port, Layout<? extends Serializable> layout) {
        super(name, os, address, host, port, layout);
    }

    public static DatagramSocketManager getSocketManager(String host, int port, Layout<? extends Serializable> layout) {
        if (Strings.isEmpty(host)) {
            throw new IllegalArgumentException("A host name is required");
        }
        if (port <= 0) {
            throw new IllegalArgumentException("A port value is required");
        }
        return (DatagramSocketManager)DatagramSocketManager.getManager("UDP:" + host + ":" + port, new FactoryData(host, port, layout), FACTORY);
    }

    @Override
    public Map<String, String> getContentFormat() {
        HashMap<String, String> result = new HashMap<String, String>(super.getContentFormat());
        result.put("protocol", "udp");
        result.put("direction", "out");
        return result;
    }

    private static class DatagramSocketManagerFactory
    implements ManagerFactory<DatagramSocketManager, FactoryData> {
        private DatagramSocketManagerFactory() {
        }

        @Override
        public DatagramSocketManager createManager(String name, FactoryData data) {
            InetAddress address;
            DatagramOutputStream os = new DatagramOutputStream(data.host, data.port, data.layout.getHeader(), data.layout.getFooter());
            try {
                address = InetAddress.getByName(data.host);
            }
            catch (UnknownHostException ex) {
                LOGGER.error("Could not find address of " + data.host, (Throwable)ex);
                return null;
            }
            return new DatagramSocketManager(name, os, address, data.host, data.port, data.layout);
        }
    }

    private static class FactoryData {
        private final String host;
        private final int port;
        private final Layout<? extends Serializable> layout;

        public FactoryData(String host, int port, Layout<? extends Serializable> layout) {
            this.host = host;
            this.port = port;
            this.layout = layout;
        }
    }
}

