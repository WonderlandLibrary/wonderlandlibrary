// 
// Decompiled by Procyon v0.5.36
// 

package de.gerrygames.viarewind.protocol.protocol1_7_6to1_7_2;

import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.remapper.ValueTransformer;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ServerboundPackets1_7;
import de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.ClientboundPackets1_7;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;

public class Protocol1_7_6to1_7_2 extends AbstractProtocol<ClientboundPackets1_7, ClientboundPackets1_7, ServerboundPackets1_7, ServerboundPackets1_7>
{
    public static ValueTransformer<String, String> INSERT_DASHES;
    
    public Protocol1_7_6to1_7_2() {
        super(ClientboundPackets1_7.class, ClientboundPackets1_7.class, ServerboundPackets1_7.class, ServerboundPackets1_7.class);
    }
    
    @Override
    protected void registerPackets() {
        this.registerClientbound(State.LOGIN, 2, 2, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING, Protocol1_7_6to1_7_2.INSERT_DASHES);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_7, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_7.SPAWN_PLAYER, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.STRING, Protocol1_7_6to1_7_2.INSERT_DASHES);
                this.map(Type.STRING);
                this.create(Type.VAR_INT, 0);
            }
        });
    }
    
    static {
        Protocol1_7_6to1_7_2.INSERT_DASHES = new ValueTransformer<String, String>() {
            @Override
            public String transform(final PacketWrapper wrapper, final String inputValue) throws Exception {
                final StringBuilder builder = new StringBuilder(inputValue);
                builder.insert(20, "-");
                builder.insert(16, "-");
                builder.insert(12, "-");
                builder.insert(8, "-");
                return builder.toString();
            }
        };
    }
}
