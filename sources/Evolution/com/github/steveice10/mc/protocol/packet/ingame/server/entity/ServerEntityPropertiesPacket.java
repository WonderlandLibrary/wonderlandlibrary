/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.packet.ingame.server.entity;

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.Attribute;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeModifier;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.AttributeType;
import com.github.steveice10.mc.protocol.data.game.entity.attribute.ModifierOperation;
import com.github.steveice10.mc.protocol.packet.MinecraftPacket;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerEntityPropertiesPacket
extends MinecraftPacket {
    private int entityId;
    private List<Attribute> attributes;

    private ServerEntityPropertiesPacket() {
    }

    public ServerEntityPropertiesPacket(int entityId, List<Attribute> attributes) {
        this.entityId = entityId;
        this.attributes = attributes;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public List<Attribute> getAttributes() {
        return this.attributes;
    }

    @Override
    public void read(NetInput in) throws IOException {
        this.entityId = in.readVarInt();
        this.attributes = new ArrayList<Attribute>();
        int length = in.readInt();
        int index = 0;
        while (index < length) {
            String key = in.readString();
            double value = in.readDouble();
            ArrayList<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();
            int len = in.readVarInt();
            int ind = 0;
            while (ind < len) {
                modifiers.add(new AttributeModifier(in.readUUID(), in.readDouble(), MagicValues.key(ModifierOperation.class, in.readByte())));
                ++ind;
            }
            this.attributes.add(new Attribute(MagicValues.key(AttributeType.class, key), value, modifiers));
            ++index;
        }
    }

    @Override
    public void write(NetOutput out) throws IOException {
        out.writeVarInt(this.entityId);
        out.writeInt(this.attributes.size());
        for (Attribute attribute : this.attributes) {
            out.writeString(MagicValues.value(String.class, (Object)attribute.getType()));
            out.writeDouble(attribute.getValue());
            out.writeVarInt(attribute.getModifiers().size());
            for (AttributeModifier modifier : attribute.getModifiers()) {
                out.writeUUID(modifier.getUUID());
                out.writeDouble(modifier.getAmount());
                out.writeByte(MagicValues.value(Integer.class, (Object)modifier.getOperation()));
            }
        }
    }
}

