// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.packets;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.remapper.PacketHandler;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.ServerboundPackets1_13;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.protocols.protocol1_14to1_13_2.ClientboundPackets1_14;
import com.viaversion.viabackwards.protocol.protocol1_13_2to1_14.Protocol1_13_2To1_14;
import com.viaversion.viaversion.api.rewriter.RewriterBase;

public class PlayerPackets1_14 extends RewriterBase<Protocol1_13_2To1_14>
{
    public PlayerPackets1_14(final Protocol1_13_2To1_14 protocol) {
        super(protocol);
    }
    
    @Override
    protected void registerPackets() {
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.SERVER_DIFFICULTY, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UNSIGNED_BYTE);
                this.map(Type.BOOLEAN, Type.NOTHING);
            }
        });
        ((AbstractProtocol<ClientboundPackets1_14, C2, S1, S2>)this.protocol).registerClientbound(ClientboundPackets1_14.OPEN_SIGN_EDITOR, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION1_14, Type.POSITION);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_13>)this.protocol).registerServerbound(ServerboundPackets1_13.QUERY_BLOCK_NBT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_13>)this.protocol).registerServerbound(ServerboundPackets1_13.PLAYER_DIGGING, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_13>)this.protocol).registerServerbound(ServerboundPackets1_13.RECIPE_BOOK_DATA, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final int type = wrapper.get((Type<Integer>)Type.VAR_INT, 0);
                        if (type == 0) {
                            wrapper.passthrough(Type.STRING);
                        }
                        else if (type == 1) {
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.passthrough((Type<Object>)Type.BOOLEAN);
                            wrapper.write(Type.BOOLEAN, false);
                            wrapper.write(Type.BOOLEAN, false);
                            wrapper.write(Type.BOOLEAN, false);
                            wrapper.write(Type.BOOLEAN, false);
                        }
                    }
                });
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_13>)this.protocol).registerServerbound(ServerboundPackets1_13.UPDATE_COMMAND_BLOCK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_13>)this.protocol).registerServerbound(ServerboundPackets1_13.UPDATE_STRUCTURE_BLOCK, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_13>)this.protocol).registerServerbound(ServerboundPackets1_13.UPDATE_SIGN, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.POSITION, Type.POSITION1_14);
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_13>)this.protocol).registerServerbound(ServerboundPackets1_13.PLAYER_BLOCK_PLACEMENT, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(new PacketHandler() {
                    @Override
                    public void handle(final PacketWrapper wrapper) throws Exception {
                        final Position position = wrapper.read(Type.POSITION);
                        final int face = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final int hand = wrapper.read((Type<Integer>)Type.VAR_INT);
                        final float x = wrapper.read((Type<Float>)Type.FLOAT);
                        final float y = wrapper.read((Type<Float>)Type.FLOAT);
                        final float z = wrapper.read((Type<Float>)Type.FLOAT);
                        wrapper.write(Type.VAR_INT, hand);
                        wrapper.write(Type.POSITION1_14, position);
                        wrapper.write(Type.VAR_INT, face);
                        wrapper.write(Type.FLOAT, x);
                        wrapper.write(Type.FLOAT, y);
                        wrapper.write(Type.FLOAT, z);
                        wrapper.write(Type.BOOLEAN, false);
                    }
                });
            }
        });
    }
}
