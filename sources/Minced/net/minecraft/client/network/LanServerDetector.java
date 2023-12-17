// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.network;

import java.nio.charset.StandardCharsets;
import java.net.SocketTimeoutException;
import java.net.DatagramPacket;
import java.io.IOException;
import java.net.MulticastSocket;
import java.util.Iterator;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import java.net.InetAddress;
import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class LanServerDetector
{
    private static final AtomicInteger ATOMIC_COUNTER;
    private static final Logger LOGGER;
    
    static {
        ATOMIC_COUNTER = new AtomicInteger(0);
        LOGGER = LogManager.getLogger();
    }
    
    public static class LanServerList
    {
        private final List<LanServerInfo> listOfLanServers;
        boolean wasUpdated;
        
        public LanServerList() {
            this.listOfLanServers = (List<LanServerInfo>)Lists.newArrayList();
        }
        
        public synchronized boolean getWasUpdated() {
            return this.wasUpdated;
        }
        
        public synchronized void setWasNotUpdated() {
            this.wasUpdated = false;
        }
        
        public synchronized List<LanServerInfo> getLanServers() {
            return Collections.unmodifiableList((List<? extends LanServerInfo>)this.listOfLanServers);
        }
        
        public synchronized void addServer(final String pingResponse, final InetAddress ipAddress) {
            final String s = ThreadLanServerPing.getMotdFromPingResponse(pingResponse);
            String s2 = ThreadLanServerPing.getAdFromPingResponse(pingResponse);
            if (s2 != null) {
                s2 = ipAddress.getHostAddress() + ":" + s2;
                boolean flag = false;
                for (final LanServerInfo lanserverinfo : this.listOfLanServers) {
                    if (lanserverinfo.getServerIpPort().equals(s2)) {
                        lanserverinfo.updateLastSeen();
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    this.listOfLanServers.add(new LanServerInfo(s, s2));
                    this.wasUpdated = true;
                }
            }
        }
    }
    
    public static class ThreadLanServerFind extends Thread
    {
        private final LanServerList localServerList;
        private final InetAddress broadcastAddress;
        private final MulticastSocket socket;
        
        public ThreadLanServerFind(final LanServerList list) throws IOException {
            super("LanServerDetector #" + LanServerDetector.ATOMIC_COUNTER.incrementAndGet());
            this.localServerList = list;
            this.setDaemon(true);
            this.socket = new MulticastSocket(4445);
            this.broadcastAddress = InetAddress.getByName("224.0.2.60");
            this.socket.setSoTimeout(5000);
            this.socket.joinGroup(this.broadcastAddress);
        }
        
        @Override
        public void run() {
            final byte[] abyte = new byte[1024];
            while (!this.isInterrupted()) {
                final DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length);
                try {
                    this.socket.receive(datagrampacket);
                }
                catch (SocketTimeoutException var5) {
                    continue;
                }
                catch (IOException ioexception) {
                    LanServerDetector.LOGGER.error("Couldn't ping server", (Throwable)ioexception);
                    break;
                }
                final String s = new String(datagrampacket.getData(), datagrampacket.getOffset(), datagrampacket.getLength(), StandardCharsets.UTF_8);
                LanServerDetector.LOGGER.debug("{}: {}", (Object)datagrampacket.getAddress(), (Object)s);
                this.localServerList.addServer(s, datagrampacket.getAddress());
            }
            try {
                this.socket.leaveGroup(this.broadcastAddress);
            }
            catch (IOException ex) {}
            this.socket.close();
        }
    }
}
