// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.packets;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_8.ServerboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ServerboundPackets1_9;
import com.viaversion.viaversion.protocols.protocol1_8.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.ClientboundPackets1_9;
import com.viaversion.viaversion.api.protocol.Protocol;

public class ScoreboardPackets
{
    public static void register(final Protocol<ClientboundPackets1_9, ClientboundPackets1_8, ServerboundPackets1_9, ServerboundPackets1_8> protocol) {
        protocol.registerClientbound(ClientboundPackets1_9.TEAMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                final byte mode;
                this.handler(packetWrapper -> {
                    mode = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    if (mode == 0 || mode == 2) {
                        packetWrapper.passthrough(Type.STRING);
                        packetWrapper.passthrough(Type.STRING);
                        packetWrapper.passthrough(Type.STRING);
                        packetWrapper.passthrough((Type<Object>)Type.BYTE);
                        packetWrapper.passthrough(Type.STRING);
                        packetWrapper.read(Type.STRING);
                        packetWrapper.passthrough((Type<Object>)Type.BYTE);
                    }
                    if (mode == 0 || mode == 3 || mode == 4) {
                        packetWrapper.passthrough(Type.STRING_ARRAY);
                    }
                });
            }
        });
    }
}
