/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.multiplayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadLanServerPing
extends Thread {
    private final String address;
    private static final AtomicInteger field_148658_a = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private boolean isStopping = true;
    private final String motd;
    private final DatagramSocket socket;

    @Override
    public void interrupt() {
        super.interrupt();
        this.isStopping = false;
    }

    public static String getAdFromPingResponse(String string) {
        int n = string.indexOf("[/MOTD]");
        if (n < 0) {
            return null;
        }
        int n2 = string.indexOf("[/MOTD]", n + 7);
        if (n2 >= 0) {
            return null;
        }
        int n3 = string.indexOf("[AD]", n + 7);
        if (n3 < 0) {
            return null;
        }
        int n4 = string.indexOf("[/AD]", n3 + 4);
        return n4 < n3 ? null : string.substring(n3 + 4, n4);
    }

    @Override
    public void run() {
        String string = ThreadLanServerPing.getPingResponse(this.motd, this.address);
        byte[] byArray = string.getBytes();
        while (!this.isInterrupted() && this.isStopping) {
            try {
                InetAddress inetAddress = InetAddress.getByName("224.0.2.60");
                DatagramPacket datagramPacket = new DatagramPacket(byArray, byArray.length, inetAddress, 4445);
                this.socket.send(datagramPacket);
            }
            catch (IOException iOException) {
                logger.warn("LanServerPinger: " + iOException.getMessage());
                break;
            }
            try {
                ThreadLanServerPing.sleep(1500L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
    }

    public ThreadLanServerPing(String string, String string2) throws IOException {
        super("LanServerPinger #" + field_148658_a.incrementAndGet());
        this.motd = string;
        this.address = string2;
        this.setDaemon(true);
        this.socket = new DatagramSocket();
    }

    public static String getMotdFromPingResponse(String string) {
        int n = string.indexOf("[MOTD]");
        if (n < 0) {
            return "missing no";
        }
        int n2 = string.indexOf("[/MOTD]", n + 6);
        return n2 < n ? "missing no" : string.substring(n + 6, n2);
    }

    public static String getPingResponse(String string, String string2) {
        return "[MOTD]" + string + "[/MOTD][AD]" + string2 + "[/AD]";
    }
}

