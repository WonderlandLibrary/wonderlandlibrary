/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.network;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanServerDetector {
    private static final AtomicInteger field_148551_a = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();

    public static class ThreadLanServerFind
    extends Thread {
        private final InetAddress broadcastAddress;
        private final LanServerList localServerList;
        private final MulticastSocket socket;

        @Override
        public void run() {
            byte[] byArray = new byte[1024];
            while (!this.isInterrupted()) {
                DatagramPacket datagramPacket = new DatagramPacket(byArray, byArray.length);
                try {
                    this.socket.receive(datagramPacket);
                }
                catch (SocketTimeoutException socketTimeoutException) {
                    continue;
                }
                catch (IOException iOException) {
                    logger.error("Couldn't ping server", (Throwable)iOException);
                    break;
                }
                String string = new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength());
                logger.debug(datagramPacket.getAddress() + ": " + string);
                this.localServerList.func_77551_a(string, datagramPacket.getAddress());
            }
            try {
                this.socket.leaveGroup(this.broadcastAddress);
            }
            catch (IOException iOException) {
                // empty catch block
            }
            this.socket.close();
        }

        public ThreadLanServerFind(LanServerList lanServerList) throws IOException {
            super("LanServerDetector #" + field_148551_a.incrementAndGet());
            this.localServerList = lanServerList;
            this.setDaemon(true);
            this.socket = new MulticastSocket(4445);
            this.broadcastAddress = InetAddress.getByName("224.0.2.60");
            this.socket.setSoTimeout(5000);
            this.socket.joinGroup(this.broadcastAddress);
        }
    }

    public static class LanServerList {
        private List<LanServer> listOfLanServers = Lists.newArrayList();
        boolean wasUpdated;

        public synchronized void setWasNotUpdated() {
            this.wasUpdated = false;
        }

        public synchronized boolean getWasUpdated() {
            return this.wasUpdated;
        }

        public synchronized List<LanServer> getLanServers() {
            return Collections.unmodifiableList(this.listOfLanServers);
        }

        public synchronized void func_77551_a(String string, InetAddress inetAddress) {
            String string2 = ThreadLanServerPing.getMotdFromPingResponse(string);
            String string3 = ThreadLanServerPing.getAdFromPingResponse(string);
            if (string3 != null) {
                string3 = String.valueOf(inetAddress.getHostAddress()) + ":" + string3;
                boolean bl = false;
                for (LanServer lanServer : this.listOfLanServers) {
                    if (!lanServer.getServerIpPort().equals(string3)) continue;
                    lanServer.updateLastSeen();
                    bl = true;
                    break;
                }
                if (!bl) {
                    this.listOfLanServers.add(new LanServer(string2, string3));
                    this.wasUpdated = true;
                }
            }
        }
    }

    public static class LanServer {
        private String lanServerIpPort;
        private long timeLastSeen;
        private String lanServerMotd;

        public String getServerMotd() {
            return this.lanServerMotd;
        }

        public void updateLastSeen() {
            this.timeLastSeen = Minecraft.getSystemTime();
        }

        public LanServer(String string, String string2) {
            this.lanServerMotd = string;
            this.lanServerIpPort = string2;
            this.timeLastSeen = Minecraft.getSystemTime();
        }

        public String getServerIpPort() {
            return this.lanServerIpPort;
        }
    }
}

