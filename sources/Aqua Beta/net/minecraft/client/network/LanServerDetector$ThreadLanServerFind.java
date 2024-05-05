package net.minecraft.client.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import net.minecraft.client.network.LanServerDetector;

/*
 * Exception performing whole class analysis ignored.
 */
public static class LanServerDetector.ThreadLanServerFind
extends Thread {
    private final LanServerDetector.LanServerList localServerList;
    private final InetAddress broadcastAddress;
    private final MulticastSocket socket;

    public LanServerDetector.ThreadLanServerFind(LanServerDetector.LanServerList p_i1320_1_) throws IOException {
        super("LanServerDetector #" + LanServerDetector.access$000().incrementAndGet());
        this.localServerList = p_i1320_1_;
        this.setDaemon(true);
        this.socket = new MulticastSocket(4445);
        this.broadcastAddress = InetAddress.getByName((String)"224.0.2.60");
        this.socket.setSoTimeout(5000);
        this.socket.joinGroup(this.broadcastAddress);
    }

    public void run() {
        byte[] abyte = new byte[1024];
        while (!this.isInterrupted()) {
            DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length);
            try {
                this.socket.receive(datagrampacket);
            }
            catch (SocketTimeoutException var5) {
                continue;
            }
            catch (IOException ioexception) {
                LanServerDetector.access$100().error("Couldn't ping server", (Throwable)ioexception);
                break;
            }
            String s = new String(datagrampacket.getData(), datagrampacket.getOffset(), datagrampacket.getLength());
            LanServerDetector.access$100().debug(datagrampacket.getAddress() + ": " + s);
            this.localServerList.func_77551_a(s, datagrampacket.getAddress());
        }
        try {
            this.socket.leaveGroup(this.broadcastAddress);
        }
        catch (IOException iOException) {
            // empty catch block
        }
        this.socket.close();
    }
}
