// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.DatagramSocket;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLanServerPing extends Thread
{
    private static final AtomicInteger field_148658_a;
    private static final Logger logger;
    private final String motd;
    private final DatagramSocket socket;
    private boolean isStopping;
    private final String address;
    private static final String __OBFID = "CL_00001137";
    
    public ThreadLanServerPing(final String p_i1321_1_, final String p_i1321_2_) throws IOException {
        super("LanServerPinger #" + ThreadLanServerPing.field_148658_a.incrementAndGet());
        this.isStopping = true;
        this.motd = p_i1321_1_;
        this.address = p_i1321_2_;
        this.setDaemon(true);
        this.socket = new DatagramSocket();
    }
    
    @Override
    public void run() {
        final String var1 = getPingResponse(this.motd, this.address);
        final byte[] var2 = var1.getBytes();
        while (!this.isInterrupted() && this.isStopping) {
            try {
                final InetAddress var3 = InetAddress.getByName("224.0.2.60");
                final DatagramPacket var4 = new DatagramPacket(var2, var2.length, var3, 4445);
                this.socket.send(var4);
            }
            catch (IOException var5) {
                ThreadLanServerPing.logger.warn("LanServerPinger: " + var5.getMessage());
                break;
            }
            try {
                Thread.sleep(1500L);
            }
            catch (InterruptedException ex) {}
        }
    }
    
    @Override
    public void interrupt() {
        super.interrupt();
        this.isStopping = false;
    }
    
    public static String getPingResponse(final String p_77525_0_, final String p_77525_1_) {
        return "[MOTD]" + p_77525_0_ + "[/MOTD][AD]" + p_77525_1_ + "[/AD]";
    }
    
    public static String getMotdFromPingResponse(final String p_77524_0_) {
        final int var1 = p_77524_0_.indexOf("[MOTD]");
        if (var1 < 0) {
            return "missing no";
        }
        final int var2 = p_77524_0_.indexOf("[/MOTD]", var1 + "[MOTD]".length());
        return (var2 < var1) ? "missing no" : p_77524_0_.substring(var1 + "[MOTD]".length(), var2);
    }
    
    public static String getAdFromPingResponse(final String p_77523_0_) {
        final int var1 = p_77523_0_.indexOf("[/MOTD]");
        if (var1 < 0) {
            return null;
        }
        final int var2 = p_77523_0_.indexOf("[/MOTD]", var1 + "[/MOTD]".length());
        if (var2 >= 0) {
            return null;
        }
        final int var3 = p_77523_0_.indexOf("[AD]", var1 + "[/MOTD]".length());
        if (var3 < 0) {
            return null;
        }
        final int var4 = p_77523_0_.indexOf("[/AD]", var3 + "[AD]".length());
        return (var4 < var3) ? null : p_77523_0_.substring(var3 + "[AD]".length(), var4);
    }
    
    static {
        field_148658_a = new AtomicInteger(0);
        logger = LogManager.getLogger();
    }
}
