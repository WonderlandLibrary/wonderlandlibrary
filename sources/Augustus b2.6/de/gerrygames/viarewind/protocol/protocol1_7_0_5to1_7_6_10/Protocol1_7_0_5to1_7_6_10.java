// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_0_5to1_7_6_10;

import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.Iterator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.types.Types1_7_6_10;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ServerboundPackets1_7;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ClientboundPackets1_7;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_7_0_5to1_7_6_10 extends AbstractProtocol<ClientboundPackets1_7, ClientboundPackets1_7, ServerboundPackets1_7, ServerboundPackets1_7>
{
    public static final ValueTransformer<String, String> REMOVE_DASHES;
    
    public Protocol1_7_0_5to1_7_6_10() {
        super(ClientboundPackets1_7.class, ClientboundPackets1_7.class, ServerboundPackets1_7.class, ServerboundPackets1_7.class);
    }
    
    @Override
    protected void registerPackets() {
        this.registerClientbound(State.LOGIN, 2, 2, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING, Protocol1_7_0_5to1_7_6_10.REMOVE_DASHES);
                this.map(Type.STRING);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_7, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_7.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.STRING, Protocol1_7_0_5to1_7_6_10.REMOVE_DASHES);
                this.map(Type.STRING);
                int size;
                int i;
                this.handler(packetWrapper -> {
                    for (size = packetWrapper.read((Type<Integer>)Type.VAR_INT), i = 0; i < size * 3; ++i) {
                        packetWrapper.read(Type.STRING);
                    }
                    return;
                });
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.BYTE);
                this.map(Type.BYTE);
                this.map(Type.SHORT);
                this.map(Types1_7_6_10.METADATA_LIST);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_7, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_7.TEAMS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.BYTE);
                final byte mode;
                ArrayList<String> entryList;
                int size;
                int i;
                List<String> entryList2;
                final Iterator<String> iterator;
                String entry;
                this.handler(packetWrapper -> {
                    mode = packetWrapper.get((Type<Byte>)Type.BYTE, 0);
                    if (mode == 0 || mode == 2) {
                        packetWrapper.passthrough(Type.STRING);
                        packetWrapper.passthrough(Type.STRING);
                        packetWrapper.passthrough(Type.STRING);
                        packetWrapper.passthrough((Type<Object>)Type.BYTE);
                    }
                    if (mode == 0 || mode == 3 || mode == 4) {
                        entryList = new ArrayList<String>();
                        for (size = packetWrapper.read((Type<Short>)Type.SHORT), i = 0; i < size; ++i) {
                            entryList.add(packetWrapper.read(Type.STRING));
                        }
                        entryList2 = entryList.stream().map(it -> (it.length() > 16) ? it.substring(0, 16) : it).distinct().collect((Collector<? super Object, ?, List<String>>)Collectors.toList());
                        packetWrapper.write(Type.SHORT, (short)entryList2.size());
                        entryList2.iterator();
                        while (iterator.hasNext()) {
                            entry = iterator.next();
                            packetWrapper.write(Type.STRING, entry);
                        }
                    }
                });
            }
        });
    }
    
    @Override
    public void init(final UserConnection userConnection) {
    }
    
    static {
        REMOVE_DASHES = new ValueTransformer<String, String>() {
            @Override
            public String transform(final PacketWrapper packetWrapper, final String s) {
                return s.replace("-", "");
            }
        };
    }
}
