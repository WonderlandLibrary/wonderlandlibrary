// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.network.PacketBuffer;
import java.util.Iterator;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import java.util.Collection;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketEntityProperties implements Packet<INetHandlerPlayClient>
{
    private int entityId;
    private final List<Snapshot> snapshots;
    
    public SPacketEntityProperties() {
        this.snapshots = (List<Snapshot>)Lists.newArrayList();
    }
    
    public SPacketEntityProperties(final int entityIdIn, final Collection<IAttributeInstance> instances) {
        this.snapshots = (List<Snapshot>)Lists.newArrayList();
        this.entityId = entityIdIn;
        for (final IAttributeInstance iattributeinstance : instances) {
            this.snapshots.add(new Snapshot(iattributeinstance.getAttribute().getName(), iattributeinstance.getBaseValue(), iattributeinstance.getModifiers()));
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityId = buf.readVarInt();
        for (int i = buf.readInt(), j = 0; j < i; ++j) {
            final String s = buf.readString(64);
            final double d0 = buf.readDouble();
            final List<AttributeModifier> list = (List<AttributeModifier>)Lists.newArrayList();
            for (int k = buf.readVarInt(), l = 0; l < k; ++l) {
                final UUID uuid = buf.readUniqueId();
                list.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", buf.readDouble(), buf.readByte()));
            }
            this.snapshots.add(new Snapshot(s, d0, list));
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarInt(this.entityId);
        buf.writeInt(this.snapshots.size());
        for (final Snapshot spacketentityproperties$snapshot : this.snapshots) {
            buf.writeString(spacketentityproperties$snapshot.getName());
            buf.writeDouble(spacketentityproperties$snapshot.getBaseValue());
            buf.writeVarInt(spacketentityproperties$snapshot.getModifiers().size());
            for (final AttributeModifier attributemodifier : spacketentityproperties$snapshot.getModifiers()) {
                buf.writeUniqueId(attributemodifier.getID());
                buf.writeDouble(attributemodifier.getAmount());
                buf.writeByte(attributemodifier.getOperation());
            }
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleEntityProperties(this);
    }
    
    public int getEntityId() {
        return this.entityId;
    }
    
    public List<Snapshot> getSnapshots() {
        return this.snapshots;
    }
    
    public class Snapshot
    {
        private final String name;
        private final double baseValue;
        private final Collection<AttributeModifier> modifiers;
        
        public Snapshot(final String nameIn, final double baseValueIn, final Collection<AttributeModifier> modifiersIn) {
            this.name = nameIn;
            this.baseValue = baseValueIn;
            this.modifiers = modifiersIn;
        }
        
        public String getName() {
            return this.name;
        }
        
        public double getBaseValue() {
            return this.baseValue;
        }
        
        public Collection<AttributeModifier> getModifiers() {
            return this.modifiers;
        }
    }
}
