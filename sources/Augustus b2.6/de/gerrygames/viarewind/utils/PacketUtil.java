// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.utils;

import com.viaversion.viaversion.exception.CancelException;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;

public class PacketUtil
{
    public static void sendToServer(final PacketWrapper packet, final Class<? extends Protocol> packetProtocol) {
        sendToServer(packet, packetProtocol, true);
    }
    
    public static void sendToServer(final PacketWrapper packet, final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline) {
        sendToServer(packet, packetProtocol, skipCurrentPipeline, false);
    }
    
    public static void sendToServer(final PacketWrapper packet, final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline, final boolean currentThread) {
        try {
            if (currentThread) {
                packet.sendToServer(packetProtocol, skipCurrentPipeline);
            }
            else {
                packet.scheduleSendToServer(packetProtocol, skipCurrentPipeline);
            }
        }
        catch (CancelException ex2) {}
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static boolean sendPacket(final PacketWrapper packet, final Class<? extends Protocol> packetProtocol) {
        return sendPacket(packet, packetProtocol, true);
    }
    
    public static boolean sendPacket(final PacketWrapper packet, final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline) {
        return sendPacket(packet, packetProtocol, true, false);
    }
    
    public static boolean sendPacket(final PacketWrapper packet, final Class<? extends Protocol> packetProtocol, final boolean skipCurrentPipeline, final boolean currentThread) {
        try {
            if (currentThread) {
                packet.send(packetProtocol, skipCurrentPipeline);
            }
            else {
                packet.scheduleSend(packetProtocol, skipCurrentPipeline);
            }
            return true;
        }
        catch (CancelException ex2) {}
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
