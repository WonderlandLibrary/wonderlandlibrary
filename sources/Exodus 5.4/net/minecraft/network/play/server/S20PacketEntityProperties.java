/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S20PacketEntityProperties
implements Packet<INetHandlerPlayClient> {
    private int entityId;
    private final List<Snapshot> field_149444_b = Lists.newArrayList();

    public List<Snapshot> func_149441_d() {
        return this.field_149444_b;
    }

    @Override
    public void readPacketData(PacketBuffer packetBuffer) throws IOException {
        this.entityId = packetBuffer.readVarIntFromBuffer();
        int n = packetBuffer.readInt();
        int n2 = 0;
        while (n2 < n) {
            String string = packetBuffer.readStringFromBuffer(64);
            double d = packetBuffer.readDouble();
            ArrayList arrayList = Lists.newArrayList();
            int n3 = packetBuffer.readVarIntFromBuffer();
            int n4 = 0;
            while (n4 < n3) {
                UUID uUID = packetBuffer.readUuid();
                arrayList.add(new AttributeModifier(uUID, "Unknown synced attribute modifier", packetBuffer.readDouble(), packetBuffer.readByte()));
                ++n4;
            }
            this.field_149444_b.add(new Snapshot(string, d, arrayList));
            ++n2;
        }
    }

    @Override
    public void processPacket(INetHandlerPlayClient iNetHandlerPlayClient) {
        iNetHandlerPlayClient.handleEntityProperties(this);
    }

    @Override
    public void writePacketData(PacketBuffer packetBuffer) throws IOException {
        packetBuffer.writeVarIntToBuffer(this.entityId);
        packetBuffer.writeInt(this.field_149444_b.size());
        for (Snapshot snapshot : this.field_149444_b) {
            packetBuffer.writeString(snapshot.func_151409_a());
            packetBuffer.writeDouble(snapshot.func_151410_b());
            packetBuffer.writeVarIntToBuffer(snapshot.func_151408_c().size());
            for (AttributeModifier attributeModifier : snapshot.func_151408_c()) {
                packetBuffer.writeUuid(attributeModifier.getID());
                packetBuffer.writeDouble(attributeModifier.getAmount());
                packetBuffer.writeByte(attributeModifier.getOperation());
            }
        }
    }

    public S20PacketEntityProperties(int n, Collection<IAttributeInstance> collection) {
        this.entityId = n;
        for (IAttributeInstance iAttributeInstance : collection) {
            this.field_149444_b.add(new Snapshot(iAttributeInstance.getAttribute().getAttributeUnlocalizedName(), iAttributeInstance.getBaseValue(), iAttributeInstance.func_111122_c()));
        }
    }

    public S20PacketEntityProperties() {
    }

    public int getEntityId() {
        return this.entityId;
    }

    public class Snapshot {
        private final Collection<AttributeModifier> field_151411_d;
        private final String field_151412_b;
        private final double field_151413_c;

        public Snapshot(String string, double d, Collection<AttributeModifier> collection) {
            this.field_151412_b = string;
            this.field_151413_c = d;
            this.field_151411_d = collection;
        }

        public Collection<AttributeModifier> func_151408_c() {
            return this.field_151411_d;
        }

        public String func_151409_a() {
            return this.field_151412_b;
        }

        public double func_151410_b() {
            return this.field_151413_c;
        }
    }
}

