/*
 * Decompiled with CFR 0.152.
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
    private static final AtomicInteger field_148658_a = new AtomicInteger(0);
    private static final Logger logger = LogManager.getLogger();
    private final String motd;
    private final DatagramSocket socket;
    private boolean isStopping = true;
    private final String address;

    public ThreadLanServerPing(String p_i1321_1_, String p_i1321_2_) throws IOException {
        super("LanServerPinger #" + field_148658_a.incrementAndGet());
        this.motd = p_i1321_1_;
        this.address = p_i1321_2_;
        this.setDaemon(true);
        this.socket = new DatagramSocket();
    }

    @Override
    public void run() {
        String s2 = ThreadLanServerPing.getPingResponse(this.motd, this.address);
        byte[] abyte = s2.getBytes();
        while (!this.isInterrupted() && this.isStopping) {
            try {
                InetAddress inetaddress = InetAddress.getByName("224.0.2.60");
                DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length, inetaddress, 4445);
                this.socket.send(datagrampacket);
            }
            catch (IOException ioexception) {
                logger.warn("LanServerPinger: " + ioexception.getMessage());
                break;
            }
            try {
                ThreadLanServerPing.sleep(1500L);
            }
            catch (InterruptedException interruptedException) {}
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
        this.isStopping = false;
    }

    public static String getPingResponse(String p_77525_0_, String p_77525_1_) {
        return "[MOTD]" + p_77525_0_ + "[/MOTD][AD]" + p_77525_1_ + "[/AD]";
    }

    public static String getMotdFromPingResponse(String p_77524_0_) {
        int i2 = p_77524_0_.indexOf("[MOTD]");
        if (i2 < 0) {
            return "missing no";
        }
        int j2 = p_77524_0_.indexOf("[/MOTD]", i2 + "[MOTD]".length());
        return j2 < i2 ? "missing no" : p_77524_0_.substring(i2 + "[MOTD]".length(), j2);
    }

    public static String getAdFromPingResponse(String p_77523_0_) {
        int i2 = p_77523_0_.indexOf("[/MOTD]");
        if (i2 < 0) {
            return null;
        }
        int j2 = p_77523_0_.indexOf("[/MOTD]", i2 + "[/MOTD]".length());
        if (j2 >= 0) {
            return null;
        }
        int k2 = p_77523_0_.indexOf("[AD]", i2 + "[/MOTD]".length());
        if (k2 < 0) {
            return null;
        }
        int l2 = p_77523_0_.indexOf("[/AD]", k2 + "[AD]".length());
        return l2 < k2 ? null : p_77523_0_.substring(k2 + "[AD]".length(), l2);
    }
}

