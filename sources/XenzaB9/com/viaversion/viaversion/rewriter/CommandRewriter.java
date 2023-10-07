// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.rewriter;

import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.type.Type;
import java.util.HashMap;
import java.util.Map;
import com.viaversion.viaversion.api.protocol.Protocol;

public class CommandRewriter
{
    protected final Protocol protocol;
    protected final Map<String, CommandArgumentConsumer> parserHandlers;
    
    public CommandRewriter(final Protocol protocol) {
        this.parserHandlers = new HashMap<String, CommandArgumentConsumer>();
        this.protocol = protocol;
        this.parserHandlers.put("brigadier:double", wrapper -> {
            final byte propertyFlags = wrapper.passthrough((Type<Byte>)Type.BYTE);
            if ((propertyFlags & 0x1) != 0x0) {
                wrapper.passthrough((Type<Object>)Type.DOUBLE);
            }
            if ((propertyFlags & 0x2) != 0x0) {
                wrapper.passthrough((Type<Object>)Type.DOUBLE);
            }
            return;
        });
        this.parserHandlers.put("brigadier:float", wrapper -> {
            final byte propertyFlags2 = wrapper.passthrough((Type<Byte>)Type.BYTE);
            if ((propertyFlags2 & 0x1) != 0x0) {
                wrapper.passthrough((Type<Object>)Type.FLOAT);
            }
            if ((propertyFlags2 & 0x2) != 0x0) {
                wrapper.passthrough((Type<Object>)Type.FLOAT);
            }
            return;
        });
        this.parserHandlers.put("brigadier:integer", wrapper -> {
            final byte propertyFlags3 = wrapper.passthrough((Type<Byte>)Type.BYTE);
            if ((propertyFlags3 & 0x1) != 0x0) {
                wrapper.passthrough((Type<Object>)Type.INT);
            }
            if ((propertyFlags3 & 0x2) != 0x0) {
                wrapper.passthrough((Type<Object>)Type.INT);
            }
            return;
        });
        this.parserHandlers.put("brigadier:long", wrapper -> {
            final byte propertyFlags4 = wrapper.passthrough((Type<Byte>)Type.BYTE);
            if ((propertyFlags4 & 0x1) != 0x0) {
                wrapper.passthrough((Type<Object>)Type.LONG);
            }
            if ((propertyFlags4 & 0x2) != 0x0) {
                wrapper.passthrough((Type<Object>)Type.LONG);
            }
            return;
        });
        this.parserHandlers.put("brigadier:string", wrapper -> wrapper.passthrough((Type<Object>)Type.VAR_INT));
        this.parserHandlers.put("minecraft:entity", wrapper -> wrapper.passthrough((Type<Object>)Type.BYTE));
        this.parserHandlers.put("minecraft:score_holder", wrapper -> wrapper.passthrough((Type<Object>)Type.BYTE));
        this.parserHandlers.put("minecraft:resource", wrapper -> wrapper.passthrough(Type.STRING));
        this.parserHandlers.put("minecraft:resource_or_tag", wrapper -> wrapper.passthrough(Type.STRING));
    }
    
    public void handleArgument(final PacketWrapper wrapper, final String argumentType) throws Exception {
        final CommandArgumentConsumer handler = this.parserHandlers.get(argumentType);
        if (handler != null) {
            handler.accept(wrapper);
        }
    }
    
    public void registerDeclareCommands(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        final byte flags = wrapper.passthrough((Type<Byte>)Type.BYTE);
                        wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                        if ((flags & 0x8) != 0x0) {
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        }
                        final byte nodeType = (byte)(flags & 0x3);
                        if (nodeType == 1 || nodeType == 2) {
                            wrapper.passthrough(Type.STRING);
                        }
                        if (nodeType == 2) {
                            final String argumentType = wrapper.read(Type.STRING);
                            final String newArgumentType = CommandRewriter.this.handleArgumentType(argumentType);
                            if (newArgumentType != null) {
                                wrapper.write(Type.STRING, newArgumentType);
                            }
                            CommandRewriter.this.handleArgument(wrapper, argumentType);
                        }
                        if ((flags & 0x10) != 0x0) {
                            wrapper.passthrough(Type.STRING);
                        }
                    }
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                });
            }
        });
    }
    
    public void registerDeclareCommands1_19(final ClientboundPacketType packetType) {
        this.protocol.registerClientbound(packetType, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        final byte flags = wrapper.passthrough((Type<Byte>)Type.BYTE);
                        wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                        if ((flags & 0x8) != 0x0) {
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        }
                        final byte nodeType = (byte)(flags & 0x3);
                        if (nodeType == 1 || nodeType == 2) {
                            wrapper.passthrough(Type.STRING);
                        }
                        if (nodeType == 2) {
                            final int argumentTypeId = wrapper.read((Type<Integer>)Type.VAR_INT);
                            final String argumentType = CommandRewriter.this.protocol.getMappingData().getArgumentTypeMappings().identifier(argumentTypeId);
                            final String newArgumentType = CommandRewriter.this.handleArgumentType(argumentType);
                            if (newArgumentType != null) {
                                wrapper.write(Type.VAR_INT, CommandRewriter.this.protocol.getMappingData().getArgumentTypeMappings().mappedId(newArgumentType));
                            }
                            CommandRewriter.this.handleArgument(wrapper, argumentType);
                        }
                        if ((flags & 0x10) != 0x0) {
                            wrapper.passthrough(Type.STRING);
                        }
                    }
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                });
            }
        });
    }
    
    public String handleArgumentType(final String argumentType) {
        if (this.protocol.getMappingData() != null && this.protocol.getMappingData().getArgumentTypeMappings() != null) {
            return this.protocol.getMappingData().getArgumentTypeMappings().mappedIdentifier(argumentType);
        }
        return argumentType;
    }
    
    @FunctionalInterface
    public interface CommandArgumentConsumer
    {
        void accept(final PacketWrapper p0) throws Exception;
    }
}
