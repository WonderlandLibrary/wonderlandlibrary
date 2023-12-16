// 
// Decompiled by Procyon v0.5.36
// 

package moonsense.network.websocket;

import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayOutputStream;
import java.net.UnknownHostException;
import java.net.SocketException;
import java.util.Iterator;
import moonsense.network.websocket.packets.Packet;
import java.io.InputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import moonsense.network.websocket.utils.Compression;
import java.io.IOException;
import java.net.DatagramPacket;
import moonsense.network.websocket.listener.UDPNetworkingListener;
import java.util.ArrayList;
import moonsense.network.websocket.packets.PacketProtocol;
import java.net.InetAddress;
import java.net.DatagramSocket;

public class UDPClient
{
    private final DatagramSocket socket;
    private final InetAddress address;
    private final int port;
    private PacketProtocol packetProtocol;
    private ArrayList<UDPNetworkingListener> listeners;
    private byte[] buf;
    private boolean useCompression;
    
    public UDPClient(final String ip, final int port, final PacketProtocol packetProtocol, final UDPNetworkingListener... listeners) throws SocketException, UnknownHostException {
        this.buf = new byte[2048];
        this.useCompression = true;
        this.port = port;
        this.packetProtocol = packetProtocol;
        this.socket = new DatagramSocket();
        this.address = InetAddress.getByName(ip);
        this.listeners = new ArrayList<UDPNetworkingListener>();
        for (final UDPNetworkingListener listener : listeners) {
            this.listeners.add(listener);
        }
        DatagramPacket packet;
        byte[] data;
        ByteArrayInputStream byteArrayInputStream;
        DataInputStream dataInputStream;
        Packet useFriendlyPacket;
        Packet finalUseFriendlyPacket;
        final Packet packet2;
        final Iterator<UDPNetworkingListener> iterator;
        UDPNetworkingListener listener2;
        final DatagramPacket datagramPacket;
        new Thread(() -> {
            while (true) {
                packet = new DatagramPacket(this.buf, this.buf.length);
                try {
                    this.socket.receive(packet);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                data = new byte[this.getBufferSize()];
                System.arraycopy(packet.getData(), 0, data, 0, this.getBufferSize());
                if (Compression.isCompressed(data)) {
                    data = Compression.decompress(data);
                }
                byteArrayInputStream = new ByteArrayInputStream(data);
                dataInputStream = new DataInputStream(byteArrayInputStream);
                useFriendlyPacket = null;
                try {
                    useFriendlyPacket = packetProtocol.createServerboundPacket(dataInputStream.readInt(), dataInputStream);
                }
                catch (IOException e2) {
                    e2.printStackTrace();
                }
                finalUseFriendlyPacket = useFriendlyPacket;
                new Thread(() -> {
                    if (!this.listeners.isEmpty() && packet2 != null) {
                        this.listeners.iterator();
                        while (iterator.hasNext()) {
                            listener2 = iterator.next();
                            listener2.onPacketReceived(packet2, datagramPacket.getAddress(), datagramPacket.getPort());
                        }
                    }
                }).start();
            }
        }).start();
    }
    
    public void setUseCompression(final boolean useCompression) {
        this.useCompression = useCompression;
    }
    
    public boolean isCompression() {
        return this.useCompression;
    }
    
    public PacketProtocol getPacketProtocol() {
        return this.packetProtocol;
    }
    
    public void setPacketProtocol(final PacketProtocol packetProtocol) {
        this.packetProtocol = packetProtocol;
    }
    
    public ArrayList<UDPNetworkingListener> getListeners() {
        if (this.listeners == null) {
            this.listeners = new ArrayList<UDPNetworkingListener>();
        }
        return this.listeners;
    }
    
    public void addListener(final UDPNetworkingListener listener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList<UDPNetworkingListener>();
        }
        this.listeners.add(listener);
    }
    
    public void clearListeners() {
        if (this.listeners == null) {
            this.listeners = new ArrayList<UDPNetworkingListener>();
        }
        this.listeners.clear();
    }
    
    public int getPort() {
        return this.port;
    }
    
    public InetAddress getAddress() {
        return this.address;
    }
    
    public void sendData(final Packet packet) throws IOException {
        ByteArrayOutputStream bufferedOutputStream;
        DataOutputStream dataOutputStream;
        byte[] msg;
        DatagramPacket p;
        new Thread(() -> {
            try {
                bufferedOutputStream = new ByteArrayOutputStream(this.getBufferSize());
                dataOutputStream = new DataOutputStream(bufferedOutputStream);
                dataOutputStream.writeInt(this.packetProtocol.getClientboundId(packet));
                packet.write(dataOutputStream);
                msg = bufferedOutputStream.toByteArray();
                if (this.isCompression()) {
                    msg = Compression.compress(msg);
                }
                p = new DatagramPacket(msg, msg.length, this.address, this.port);
                this.socket.send(p);
            }
            catch (IOException ex) {}
        }).start();
    }
    
    public void setBuffer(final int size) {
        this.buf = new byte[size];
    }
    
    public int getBufferSize() {
        return this.buf.length;
    }
    
    public void close() {
        this.socket.close();
    }
}
